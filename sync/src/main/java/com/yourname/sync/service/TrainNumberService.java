package com.yourname.sync.service;

import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.backen.mapper.TrainNumberDetailMapper;
import com.yourname.backen.mapper.TrainNumberMapper;
import com.yourname.backen.util.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/3/2022 10:46 AM
 */
@Service
@Slf4j
public class TrainNumberService {

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Resource
    private TrainNumberDetailMapper trainNumberDetailMapper;

    @Autowired
    private TrainCacheService trainCacheService;


    public void handle(List<Column> columns, EventType eventType) {
        if (eventType != EventType.UPDATE) {
            log.info("no need update,no need care");
            return;
        }

        //获取id
        int trainNumberId = 0;
        for (Column column : columns) {
            if ("id".equals(column.getName())) {
                trainNumberId = Integer.parseInt(column.getValue());
                break;
            }
        }
        TrainNumber trainNumber = trainNumberMapper.selectById(trainNumberId);
        if (trainNumber == null) {
            log.error("not find trainNumber,trainNumberId:{}", trainNumberId);
            return;
        }
        List<TrainNumberDetail> trainNumberDetails = trainNumberDetailMapper.selectAllByTrainNumberIdOrderByStationIndex(trainNumberId);
        if(CollectionUtils.isEmpty(trainNumberDetails)){
            log.warn("this trainNumber has no detail,id is {}",trainNumber.getName());
            return;
        }
        trainCacheService.set("TN_"+trainNumber.getName(), JsonMapper.obj2String(trainNumberDetails));

    }
}
