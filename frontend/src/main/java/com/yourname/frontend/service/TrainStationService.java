package com.yourname.frontend.service;

import com.yourname.backen.entity.TrainStation;
import com.yourname.backen.mapper.TrainStationMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/6/2022 10:54 AM
 */

@Service
public class TrainStationService {

    @Resource
    TrainStationMapper trainStationMapper;

    public List<TrainStation> getAll(){
        return trainStationMapper.selectList(null);
    }
}
