package com.yourname.backen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.param.TrainNumberParam;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface ITrainNumberService extends IService<TrainNumber> {
    void saveOne(TrainNumberParam trainNumberParam);

    void updateOne(TrainNumberParam trainNumberParam);




}
