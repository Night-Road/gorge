package com.yourname.backen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yourname.backen.param.TrainCityParam;
import com.yourname.backen.entity.TrainCity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface ITrainCityService extends IService<TrainCity> {


     List<TrainCity> getAll();
     Map<Integer,String> getAllMaps();
     void save(TrainCityParam param);
     void updateName(TrainCityParam param);

}
