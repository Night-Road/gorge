package com.yourname.sync.service;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainSeat;
import com.yourname.backen.mapper.TrainNumberMapper;
import com.yourname.backen.util.BeanUtil;
import com.yourname.backen.util.StringUtil;
import com.yourname.sync.common.TrainSeatStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @Author dell
 * @Date 11/5/2022 11:53 PM
 */
@Service
@Slf4j
public class TrainSeatServeice {

    //@Resource
    //TrainCacheService trainCacheService;

    @Resource
    TrainNumberMapper trainNumberMapper;

    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    public void handle(List<Column> columns, EventType eventType) {
        if (eventType != EventType.UPDATE) {
            log.info("not update,no care necessary");
            return;
        }
        TrainSeat trainSeat = new TrainSeat();
        //boolean isUpdate = false;
        for (Column column : columns) {
            if ("status".equals(column.getName()) && !column.getUpdated()) {
                //isUpdate = true;
                log.info("status not update ,no care");
                return;
            }
            //if (!isUpdate) {
            //    log.info("status not update ,no care");
            //    return;
            //}
        }
        for (Column column : columns) {
            String field = StringUtil.camelName(column.getName());
            BeanUtil.setFieldValueByName(trainSeat, field, column.getValue());
        }

        log.info("train seat update,trainSeat:{}", trainSeat.getShowNumber());

        cache(trainSeat);


    }

    /**
     * 1 :指定座位被占：hash
     * cacheKey: 车次_日期, for example:D386_20221106
     * filed:carriage_row_seat_fromStationId_toStationId
     * value:0空的，1占座
     */
    public void cache(TrainSeat trainSeat) {

        TrainNumber trainNumber = trainNumberMapper.selectById(trainSeat.getTrainNumberId());

        if (trainSeat.getStatus() == TrainSeatStatus.TICKET) {
            //trainCacheService.hset(
            //        trainNumber.getName() + "_" + trainSeat.getTicket(),
            //        trainSeat.getCarriageNumber() + "_" + trainSeat.getRowNumber() + "_" + trainSeat.getSeatLevel()
            //                + "_" +trainSeat.getFromStationId()+"_"+trainSeat.getToStationId(),"0");
            redisTemplate.opsForHash().put(trainNumber.getName() + "_" + trainSeat.getTicket(),
                    trainSeat.getCarriageNumber() + "_" + trainSeat.getRowNumber()
                            + "_" + trainSeat.getSeatNumber() + "_" +
                            trainSeat.getFromStationId() + "_" + trainSeat.getToStationId(), "0");
            redisTemplate.opsForHash().increment(trainNumber.getName() + "_" + trainSeat.getTicket()+"_Count"
            ,trainSeat.getFromStationId() + "_" + trainSeat.getToStationId(), 1L);

            log.info("seat+1,trainNumber:{},trainSeat:{}",trainNumber.getName(),trainSeat);
        } else if (trainSeat.getStatus() == TrainSeatStatus.OCCUPY_TICKET) {
            //trainCacheService.hset(
            //        trainNumber.getName() + "_" + trainSeat.getTicket(),
            //        trainSeat.getCarriageNumber() + "_" + trainSeat.getRowNumber() + "_" + trainSeat.getSeatLevel()
            //                + "_" + trainSeat.getFromStationId() + "_" + trainSeat.getToStationId(), "1");
            redisTemplate.opsForHash().put(trainNumber.getName() + "_" + trainSeat.getTicket(),
                    trainSeat.getCarriageNumber() + "_" + trainSeat.getRowNumber() + "_" +
                            trainSeat.getSeatNumber() + "_" + trainSeat.getFromStationId() + "_" +
                            trainSeat.getToStationId(), "1");
            redisTemplate.opsForHash().increment(trainNumber.getName() + "_" + trainSeat.getTicket()+"_Count"
                    ,trainSeat.getFromStationId() + "_" + trainSeat.getToStationId(),-1L);

            log.info("seat-1,trainNumber:{},trainSeat:{}",trainNumber.getName(),trainSeat);
        }else {
            log.warn("没有修改座位状态，无需进入缓存");
        }
    }
}
