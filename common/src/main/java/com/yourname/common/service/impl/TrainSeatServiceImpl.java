package com.yourname.common.service.impl;

import com.yourname.common.entity.TrainSeat;
import com.yourname.common.mapper.TrainSeatMapper;
import com.yourname.common.service.ITrainSeatService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yourName
 * @since 2022-10-16
 */
@Service
public class TrainSeatServiceImpl extends ServiceImpl<TrainSeatMapper, TrainSeat> implements ITrainSeatService {

    @Override
    public TrainSeat get10() {
        return baseMapper.selectById(10);
    }

    @Override
    public TrainSeat get5() {
        return baseMapper.selectById(5);
    }

    @Override
    public int add() {
        TrainSeat trainSeat = new TrainSeat();
        trainSeat.setUserId((long)5);
        return baseMapper.insert(trainSeat);
    }
}
