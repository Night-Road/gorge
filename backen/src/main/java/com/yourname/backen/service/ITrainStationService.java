package com.yourname.backen.service;

import com.yourname.backen.entity.TrainStation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface ITrainStationService extends IService<TrainStation> {

    /**
     * 没有遍历整个map，原因是怕降低效率
     * @return
     */
    Map<Integer,String> getAllMap();

    Integer getCityIdByStation(int stationId);
}
