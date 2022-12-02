package com.yourname.backen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yourname.backen.beans.PageQuery;
import com.yourname.backen.entity.TrainSeat;
import com.yourname.backen.param.GenerateTicketParam;
import com.yourname.backen.param.PublishTicketParam;
import com.yourname.backen.param.TrainSeatSearchParam;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */

public interface ITrainSeatService extends IService<TrainSeat> {


    List<TrainSeat> generate(GenerateTicketParam param);

    List<TrainSeat> searchList(TrainSeatSearchParam param, PageQuery pageQuery);

    int countList(TrainSeatSearchParam trainSeatSearchParam);

    @Override
    boolean saveBatch(Collection<TrainSeat> entityList, int batchSize);

    void publish(PublishTicketParam param);
}
