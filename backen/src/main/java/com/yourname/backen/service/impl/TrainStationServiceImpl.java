package com.yourname.backen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.yourname.backen.entity.TrainStation;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainStationMapper;
import com.yourname.backen.service.ITrainStationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Service
public class TrainStationServiceImpl extends ServiceImpl<TrainStationMapper, TrainStation> implements ITrainStationService {
    @Resource
    TrainStationMapper trainStationMapper;


    @Override
    public Map<Integer,String> getAllMap() {

        List<TrainStation> list = trainStationMapper.selectList(null);
        HashMap<Integer, String> map = Maps.newHashMap();
        for (TrainStation trainStation : list) {
            map.put(trainStation.getId(),trainStation.getName());
        }
        //map.putAll();
        return map;
    }

    @Override
    public Integer getCityIdByStation(int stationId) {

        TrainStation trainStation = trainStationMapper.selectById(stationId);
        if(trainStation==null){
            throw new BusinessException("待更新的站点不存在");
        }
        return trainStation.getCityId();
    }
}
