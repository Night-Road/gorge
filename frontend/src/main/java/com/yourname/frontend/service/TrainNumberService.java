package com.yourname.frontend.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.mapper.TrainNumberMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/8/2022 12:01 PM
 */
@Service("FrontedTrainNumberService")
public class TrainNumberService {

    private static Cache<String, TrainNumber> trainNumberCache = CacheBuilder.newBuilder().expireAfterWrite(5,
            TimeUnit.MINUTES).build();
    @Resource
    TrainNumberMapper trainNumberMapper;

    public TrainNumber findByName(String name){
        TrainNumber trainNumber = trainNumberCache.getIfPresent(name);
        if (trainNumber == null) {
            trainNumber = trainNumberMapper.selectByName(name);
            if (trainNumber != null) {
                trainNumberCache.put(name, trainNumber);
            }
        }
        return trainNumber;
    }
}
