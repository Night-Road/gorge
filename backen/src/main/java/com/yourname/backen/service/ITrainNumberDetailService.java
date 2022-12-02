package com.yourname.backen.service;

import com.yourname.backen.entity.TrainNumberDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yourname.backen.param.TrainNumberDetailParam;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface ITrainNumberDetailService extends IService<TrainNumberDetail> {
    void save(TrainNumberDetailParam param);

}
