package com.yourname.backen.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.yourname.backen.beans.PageQuery;
import com.yourname.backen.common.TrainSeatLevel;
import com.yourname.backen.common.TrainType;
import com.yourname.backen.common.TrainTypeSeatConstant;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.backen.entity.TrainSeat;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainNumberDetailMapper;
import com.yourname.backen.mapper.TrainNumberMapper;
import com.yourname.backen.mapper.TrainSeatMapper;
import com.yourname.backen.param.GenerateTicketParam;
import com.yourname.backen.param.PublishTicketParam;
import com.yourname.backen.param.TrainSeatSearchParam;
import com.yourname.backen.service.ITrainSeatService;
import com.yourname.backen.util.BeanValidator;
import com.yourname.backen.util.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Service
public class TrainSeatServiceImpl extends ServiceImpl<TrainSeatMapper, TrainSeat> implements ITrainSeatService {

    @Resource
    TrainSeatMapper trainSeatMapper;

    @Resource
    TrainNumberMapper trainNumberMapper;

    @Resource
    TrainNumberDetailMapper trainNumberDetailMapper;

    @Override
    public List<TrainSeat> generate(GenerateTicketParam param) {
        BeanValidator.check(param);

        TrainNumber trainNumber = trainNumberMapper.selectById(param.getTrainNumberId());
        if (trainNumber == null) {
            throw new BusinessException("该车次不存在");
        }
        List<TrainNumberDetail> detailList = trainNumberDetailMapper.selectList(new QueryWrapper<TrainNumberDetail>()
                .eq("train_number_id", param.getTrainNumberId()));
        if (CollectionUtils.isEmpty(detailList)) {
            throw new BusinessException("该车次无详情，请先添加详情");
        }
        //Collections.sort(detailList, Comparator.comparingInt(TrainNumberDetail::getStationIndex));
        //取出当前车次的座位详情
        TrainType trainType = TrainType.valueOf(trainNumber.getTrainType());
        Table<Integer, Integer, Pair<Integer, Integer>> table = TrainTypeSeatConstant.getTable(trainType);


        //时间
        ZoneId zoneId = ZoneId.systemDefault();
        //DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
        //        .parseCaseInsensitive()
        //        .appendLiteral(" ")
        //        .toFormatter(Locale.CHINESE)
        //        .withResolverStyle(ResolverStyle.LENIENT);
        //LocalDateTime time = LocalDateTime.parse(param.getFromTime(),dateTimeFormatter);
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd[ HH:mm]")
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
        LocalDateTime time = LocalDateTime.parse(param.getFromTime(), formatter);

        //LocalDateTime time = LocalDateTime.parse(param.getFromTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd " +
        //        "MM:mm:ss"));


        List<TrainSeat> trainSeats = Lists.newArrayList();
        String ticket = time.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        //生成座位
        for (TrainNumberDetail trainNumberDetail : detailList) {
            //查询是否已经添加
            int count = trainSeatMapper.selectCount(new QueryWrapper<TrainSeat>()
                    .eq("from_station_id", trainNumberDetail.getFromStationId())
                    .eq("train_number_id", trainNumberDetail.getTrainNumberId())
                    .eq("to_station_id", trainNumberDetail.getToStationId()));

            if (count > 0) continue;

            //出发时间和到达时间
            LocalDateTime from = time.atZone(zoneId).toLocalDateTime();
            // (LocalDateTime) from = Date.from(time.atZone(zoneId).toInstant());
            LocalDateTime to = time.plusMinutes(trainNumberDetail.getRelativeTime()).atZone(zoneId).toLocalDateTime();

            //钱
            Map<Integer, Integer> seatMoney = spiltSeatMoney(trainNumberDetail.getMoney());
            for (Table.Cell<Integer, Integer, Pair<Integer, Integer>> cell : table.cellSet()) {
                //遍历每一节车厢
                Integer carriage = cell.getRowKey(); //获取车厢
                Integer row = cell.getColumnKey();  //获取排
                TrainSeatLevel level = TrainTypeSeatConstant.getLevel(trainType, carriage);
                Integer money = seatMoney.get(level.getLevel());
                Pair<Integer, Integer> rowSeatRange = table.get(carriage, row); //获取指定车厢指定排的座位号的范围
                for (int index = rowSeatRange.getKey(); index <= rowSeatRange.getValue(); index++) {
                    //生成座位
                    TrainSeat trainSeat = TrainSeat.builder()
                            .carriageNumber(carriage)
                            .rowNumber(row)
                            .seatNumber(index)
                            .seatLevel(level.getLevel())
                            .ticket(ticket)
                            .money(money)
                            .trainStart(from)
                            .trainEnd(to)
                            .trainNumberId(trainNumber.getId())
                            .showNumber(carriage + "车" + row + "排" + index)
                            .status(0)
                            .fromStationId(trainNumberDetail.getFromStationId())
                            .toStationId(trainNumberDetail.getToStationId())
                            .build();

                    trainSeats.add(trainSeat);

                }
            }

            time = to.plusMinutes(trainNumberDetail.getWaitTime());

        }

        //批量插入
        return trainSeats;
    }

    @Override
    public List<TrainSeat> searchList(TrainSeatSearchParam param, PageQuery pageQuery) {
        BeanValidator.check(param);
        BeanValidator.check(pageQuery);
        TrainNumber trainNumber = trainNumberMapper.selectByName(param.getTrainNumber());
        if (trainNumber == null) {
            throw new BusinessException("待查询的车次不存在");
        }

        return trainSeatMapper.searchAll(trainNumber.getId(), param.getTicket(), param.getStatus(),
                param.getCarriageNum(), param.getRowNum(), param.getSeatNum(),
                pageQuery.getOffset(), pageQuery.getPageSize());
    }

    @Override
    public int countList(TrainSeatSearchParam param) {
        BeanValidator.check(param);
        TrainNumber trainNumber = trainNumberMapper.selectByName(param.getTrainNumber());
        if (trainNumber == null) {
            throw new BusinessException("待查询的车次不存在");
        }
        return trainSeatMapper.countList(trainNumber.getId(), param.getTicket(), param.getStatus(),
                param.getCarriageNum(), param.getRowNum(), param.getSeatNum());
    }


    @DS("sharding")
    @Override
    public boolean saveBatch(Collection<TrainSeat> entityList, int batchSize) {
        return super.saveBatch(entityList, batchSize);
    }

    @Override
    public void publish(PublishTicketParam param) {
        BeanValidator.check(param);
        TrainNumber trainNumber = trainNumberMapper.selectByName(param.getTrainNumber());
        if(trainNumber==null){
            throw new BusinessException("该车次不存在");
        }
        List<Long> trainSeatIdList = StringUtil.splitToListLong(param.getTrainSeatIds());

        //将列表分割成1000大小的列表，减小数据库插入压力
        List<List<Long>> partition = Lists.partition(trainSeatIdList, 1000);
        for (List<Long> partitionList : partition) {
            int count = trainSeatMapper.batchPublish(partitionList,trainNumber.getId());
            if(count!=partitionList.size()){
                throw new BusinessException("部分车次不满足条件，请重新查询【初始】状态的座位号进行放票");
            }
        }
    }

    private Map<Integer, Integer> spiltSeatMoney(String money) {
        //money 0:100 1:80 2:60
        try {
            List<String> list = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(money);
            //Map<Integer,Integer> map = Maps.newHashMap();
            return list.stream().map(str -> str.split(":")).collect(Collectors.toMap(str -> Integer.parseInt(str[0]),
                    str -> Integer.parseInt(str[1])));

            //Map<Integer, Integer> map = list.stream().collect(Collectors.toMap(str -> Integer.parseInt(str.split(":")[0]),
            //        str -> Integer.parseInt(str.split(":")[1])));
            //list.stream().forEach(
            //        str ->{
            //            String[] array = str.split(":");
            //            map.put(Integer.parseInt(array[0]),Integer.parseInt(array[1]));
            //        });
            //return map;
        } catch (Exception e) {
            throw new BusinessException("价钱解析出错，请按格式填写价格");
        }

    }
}
