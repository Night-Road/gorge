package com.yourname.frontend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainNumberDetailMapper;
import com.yourname.backen.util.BeanValidator;
import com.yourname.backen.util.JsonMapper;
import com.yourname.frontend.Dto.TrainNumberLeftDto;
import com.yourname.frontend.param.SearchCountLeftParam;
import com.yourname.sync.common.TrainESConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
                String deteilStr = (String) redisTemplate.opsForValue().get("TN_" + trainNumberName);
                List<TrainNumberDetail> detailList = JsonMapper.string2Obj(deteilStr,
                        new TypeReference<List<TrainNumberDetail>>() {
                        });


                //封装map 1:{TrainNumberDetail}
                Map<Integer, TrainNumberDetail> map1 = detailList.stream().
                        collect(Collectors.toMap(TrainNumberDetail::getFromStationId, t -> t));

                //遍历所有详情
                int curFromStationId = param.getFromStationId();
                int targetToStationId = param.getToStationId();
                //车票数
                long min = Long.MAX_VALUE;
                boolean isSuccess = false;
                //redisKey
                String seatKey = trainNumberName + "_" + param.getDate() + "_Count";
                while (true) {
                    TrainNumberDetail trainNumberDetail = map1.get(curFromStationId);
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
}
