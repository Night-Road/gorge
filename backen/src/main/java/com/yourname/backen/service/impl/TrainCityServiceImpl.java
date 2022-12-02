package com.yourname.backen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.yourname.backen.param.TrainCityParam;
import com.yourname.backen.entity.TrainCity;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainCityMapper;
import com.yourname.backen.service.ITrainCityService;
import com.yourname.backen.util.BeanValidator;
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
public class TrainCityServiceImpl extends ServiceImpl<TrainCityMapper, TrainCity> implements ITrainCityService {

    @Resource
    TrainCityMapper trainCityMapper;

    @Override
    public List<TrainCity> getAll() {
        return trainCityMapper.selectList(null);
    }

    @Override
    public Map<Integer, String> getAllMaps() {
        List<TrainCity> trainCities = trainCityMapper.selectList(null);
        HashMap<Integer, String> trainCityMap = Maps.newHashMap();
        for (TrainCity city : trainCities) {
            trainCityMap.put(city.getId(), city.getName());
        }
        return trainCityMap;
    }

    @Override
    public void save(TrainCityParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new BusinessException("存在相同的城市");
        }
        TrainCity trainCity = TrainCity.builder().name(param.getName()).build();
        trainCityMapper.insertSelective(trainCity);
    }

    @Override
    public void updateName(TrainCityParam param) {
        BeanValidator.check(param);
        if (checkExist(param.getName(), param.getId())) {
            throw new BusinessException("存在相同的城市");
        }
        TrainCity before = trainCityMapper.selectById(param.getId());
        if (before == null) {
            throw new BusinessException("需要更新的城市不存在");
        }
        TrainCity trainCity = TrainCity.builder().id(param.getId()).name(param.getName()).build();
        trainCityMapper.updateById(trainCity);
    }

    private boolean checkExist(String name, Integer trainCityId) {

        return trainCityMapper.countByNameAndId(name, trainCityId) > 0;

    }
}
