package com.yourname.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yourname.common.entity.TrainSeat;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yourName
 * @since 2022-10-16
 */
public interface ITrainSeatService extends IService<TrainSeat> {

    public TrainSeat get10();


    public TrainSeat get5();


    public int add();


}
