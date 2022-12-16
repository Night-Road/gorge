package com.yourname.frontend.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.yourname.backen.common.TrainType;
import com.yourname.backen.common.TrainTypeSeatConstant;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.backen.entity.TrainTraveller;
import com.yourname.backen.entity.TrainUser;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainNumberDetailMapper;
import com.yourname.backen.util.BeanValidator;
import com.yourname.backen.util.JsonMapper;
import com.yourname.frontend.Dto.TrainNumberLeftDto;
import com.yourname.frontend.param.RubTicketParam;
import com.yourname.frontend.param.SearchCountLeftParam;
import com.yourname.sync.common.TrainESConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/7/2022 10:46 AM
 */
@Service("FrontedTrainSeatService")
@Slf4j
public class TrainSeatService {

    @Resource
    EsService esService;

    @Resource(name = "FrontedTrainNumberService")
    TrainNumberService trainNumberService;

    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    @Resource
    TrainNumberDetailMapper trainNumberDetailMapper;

    @Resource
    TrainTravellerService trainTravellerService;

    /**
     * 查询余票
     *
     * @param param
     * @return
     * @throws Exception
     */
    public List<TrainNumberLeftDto> searchCountLeftService(SearchCountLeftParam param) throws Exception {
        //对参数进行校验
        BeanValidator.check(param);
        //数据列表
        ArrayList<TrainNumberLeftDto> dtoList = Lists.newArrayList();
        GetRequest getRequest = new GetRequest(TrainESConstant.INDEX, TrainESConstant.TYPE, param.getFromStationId()
                + "_" + param.getToStationId());
        GetResponse getResponse = esService.get(getRequest);
        if (getResponse == null) {
            throw new BusinessException("es数据查询失败，请重试");
        }
        Map<String, Object> map = getResponse.getSourceAsMap();
        if (MapUtils.isEmpty(map)) {
            return dtoList;
        } else {
            String trainNumbers = (String) map.get(TrainESConstant.COLUMN_TRAIN_NUMBER);
            List<String> numberList = Splitter.on(",").omitEmptyStrings().splitToList(trainNumbers);
            numberList.parallelStream().forEach(trainNumberName -> {
                //查询车次
                TrainNumber trainNumber = trainNumberService.findByName(trainNumberName);
                if (trainNumber == null) return;
                //获取车次详情
                Map<Integer, TrainNumberDetail> detailMap = getTrainNumberDetailWithFromStationInRedis(trainNumberName);

                //遍历所有详情
                int curFromStationId = param.getFromStationId();
                int targetToStationId = param.getToStationId();
                //车票数
                long min = Long.MAX_VALUE;
                boolean isSuccess = false;
                //redisKey
                String seatKey = trainNumberName + "_" + param.getDate() + "_Count";
                while (true) {
                    TrainNumberDetail trainNumberDetail = detailMap.get(curFromStationId);
                    if (trainNumberDetail == null) {
                        //redis没有命中，去查数据库
                        //TrainNumberDetail trainNumberDetail1 = trainNumberDetailMapper.selectOne(new QueryWrapper<TrainNumberDetail>().eq("from_station_id",
                        //        curFromStationId));
                        //if(trainNumberDetail !=null){
                        //    //命中数据库，写入redis TODO
                        //    //但是手工放票，不能直接查数据库
                        //}else{
                        if (curFromStationId == 12025875) {
                            break;
                        }
                        log.error("车次详情为空，车次为{}，起始车站为{}", trainNumberName, curFromStationId);
                        break;
                        //}
                    } else {
                        //从redis中获取座位信息
                        //更新票数
                        min = Math.min(min, NumberUtils.toLong((String) redisTemplate.opsForHash().get(seatKey,
                                trainNumberDetail.getFromStationId() + "_" + trainNumberDetail.getToStationId())));
                    }
                    if (trainNumberDetail.getToStationId() == targetToStationId) {
                        isSuccess = true;
                        break;
                    }
                    curFromStationId = trainNumberDetail.getToStationId();
                }
                if (isSuccess) dtoList.add(new TrainNumberLeftDto(trainNumber.getId(), trainNumberName, min));
            });
        }
        return dtoList;
    }

    public void rubTicket(RubTicketParam param, TrainUser trainUser) {
        BeanValidator.check(param);

        //从db查询乘车人
        List<TrainTraveller> travellers = trainTravellerService.getByTravellerIds(param.getTravellerIds());
        if (CollectionUtils.isEmpty(travellers)) {
            throw new BusinessException("乘车人不存在，请先指定乘车人");
        }
        //获取车次信息
        TrainNumber trainNumber = trainNumberService.findByName(param.getNumberName());
        //从缓存获取车次详情映射列表
        Map<Integer, TrainNumberDetail> detailMap = getTrainNumberDetailWithFromStationInRedis(param.getNumberName());
        //获取包含用户路线车次的车次详情
        List<TrainNumberDetail> details = getTrainNumberDetailInUserWay(detailMap, param);
        //从缓存中获取该车次的所有车票
        Map seatMap = redisTemplate.opsForHash().entries(param.getNumberName());
        //指定车次座位布局情况
        TrainType trainType = TrainType.valueOf(trainNumber.getTrainType());
        Table<Integer, Integer, Pair<Integer, Integer>> seatTable = TrainTypeSeatConstant.getTable(trainType);



    }


    private Map<Integer, TrainNumberDetail> getTrainNumberDetailWithFromStationInRedis(String trainNumberName) {
        String deteilStr = (String) redisTemplate.opsForValue().get("TN_" + trainNumberName);
        List<TrainNumberDetail> detailList = JsonMapper.string2Obj(deteilStr,
                new TypeReference<List<TrainNumberDetail>>() {
                });

        Map<Integer, TrainNumberDetail> map = detailList.stream().
                collect(Collectors.toMap(TrainNumberDetail::getFromStationId, t -> t));

        return map;
    }

    /**
     * 获取包含用户路线车次的车次详情
     * @param detailMap
     * @param param
     * @return
     */
    private List<TrainNumberDetail> getTrainNumberDetailInUserWay(Map<Integer, TrainNumberDetail> detailMap,
                                                                  RubTicketParam param){
        ArrayList<TrainNumberDetail> details = Lists.newArrayListWithCapacity(detailMap.size());

        int curFromStationId = param.getFromStationId();
        int targetToStationId = param.getToStationId();

        while (true) {
            TrainNumberDetail trainNumberDetail = detailMap.get(curFromStationId);
            if (trainNumberDetail == null) {
                if (curFromStationId == 12025875) {
                    break;
                }
                log.error("车次详情为空，车次为{}，起始车站为{}", param.getNumberName(), curFromStationId);
                break;
            } else {
                details.add(trainNumberDetail);
            }
            if (trainNumberDetail.getToStationId() == targetToStationId) {
                break;
            }
            curFromStationId = trainNumberDetail.getToStationId();
        }
        return details;
    }
}
