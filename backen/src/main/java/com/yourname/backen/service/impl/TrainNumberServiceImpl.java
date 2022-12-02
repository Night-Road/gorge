package com.yourname.backen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourname.backen.common.TrainType;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainNumberMapper;
import com.yourname.backen.param.TrainNumberParam;
import com.yourname.backen.service.ITrainNumberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourname.backen.util.BeanValidator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Service
public class TrainNumberServiceImpl extends ServiceImpl<TrainNumberMapper, TrainNumber> implements ITrainNumberService {

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Override
    public void saveOne(TrainNumberParam trainNumberParam) {

        BeanValidator.check(trainNumberParam);
        TrainNumber trainNumber = trainNumberMapper.selectOne(new QueryWrapper<TrainNumber>().eq("name",
                trainNumberParam.getName()));

        if (trainNumber != null) {
            throw new BusinessException("该车次已经存在");
        }
        TrainNumber trainNumber1 = TrainNumber.builder()
                .name(trainNumberParam.getName())
                .trainType(trainNumberParam.getTrainType())
                .type(trainNumberParam.getType())
                .seatNum(TrainType.valueOf(trainNumberParam.getTrainType()).getCount())
                .build();
        trainNumberMapper.insertSelective(trainNumber1);

    }

    @Override
    public void updateOne(TrainNumberParam trainNumberParam) {
        BeanValidator.check(trainNumberParam);
        TrainNumber trainNumber = trainNumberMapper.selectOne(new QueryWrapper<TrainNumber>().eq("name",
                trainNumberParam.getName()));

        if (trainNumber != null && trainNumber.getId().intValue() != trainNumberParam.getId().intValue()) {
            throw new BusinessException("该车次已经存在");
        }

        //TODO seat判断是否有分配，不推荐修改
        TrainNumber trainNumber1 = TrainNumber.builder()
                .id(trainNumberParam.getId())
                .name(trainNumberParam.getName())
                .trainType(trainNumberParam.getTrainType())
                .type(trainNumberParam.getType())
                .seatNum(TrainType.valueOf(trainNumberParam.getTrainType()).getCount())
                .build();

        trainNumberMapper.updateById(trainNumber1);
    }
}
