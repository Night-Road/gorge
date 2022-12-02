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

    @Resource
    TrainCacheService trainCacheService;

    @Resource
    TrainNumberMapper trainNumberMapper;

    public void handle(List<Column> columns, EventType eventType) {
        if (eventType != EventType.UPDATE) {
            log.info("not update,no care necessary");
            return;
        }
        TrainSeat trainseat = new TrainSeat();
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
            String field = StringUtil.camelName(column.getName());
            BeanUtil.setFieldValueByName(trainseat, field, column.getValue());
        }
        log.info("train seat update,trainSeat:{}", trainseat);

        /**
         * 1 :指定座位被占：hash
         * cacheKey: 车次_日期, for example:D386_20221106
         * filed:carriage_row_seat_fromStationId_toStationId
         * value:0空的，1占座
         *
         *
         */
        TrainNumber trainNumber = trainNumberMapper.selectById(trainseat.getTrainNumberId());

        if (trainseat.getStatus() == TrainSeatStatus.TICKET) {
            trainCacheService.hset(
                    trainNumber.getName() + "_" + trainseat.getTicket(),
                    trainseat.getCarriageNumber() + "_" + trainseat.getRowNumber() + "_" + trainseat.getSeatLevel()
                            + "_" +trainseat.getFromStationId()+"_"+trainseat.getToStationId(),"0"
            );
        } else if (trainseat.getStatus() == TrainSeatStatus.OCCUPY_TICKET) {
            trainCacheService.hset(
                    trainNumber.getName() + "_" + trainseat.getTicket(),
                    trainseat.getCarriageNumber() + "_" + trainseat.getRowNumber() + "_" + trainseat.getSeatLevel()
                            + "_" +trainseat.getFromStationId()+"_"+trainseat.getToStationId(),"1"
            );
        }
    }
}
