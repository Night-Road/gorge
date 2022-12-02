package com.yourname.backen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainNumberDetailMapper;
import com.yourname.backen.mapper.TrainNumberMapper;
import com.yourname.backen.param.TrainNumberDetailParam;
import com.yourname.backen.service.ITrainNumberDetailService;
import com.yourname.backen.util.BeanValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Service
public class TrainNumberDetailServiceImpl extends ServiceImpl<TrainNumberDetailMapper, TrainNumberDetail> implements ITrainNumberDetailService {

    @Resource
    TrainNumberDetailMapper trainNumberDetailMapper;
    @Resource
    TrainNumberMapper trainNumberMapper;

    @Autowired
    TrainStationServiceImpl trainStationService;

    @Override
    public void save(TrainNumberDetailParam param) {
        BeanValidator.check(param);
        TrainNumber trainNumber = trainNumberMapper.selectById(param.getTrainNumberId());
        if (trainNumber == null) {
            throw new BusinessException("不存在相关的车次");
        }
        List<TrainNumberDetail> trainNumberDetailList =
                trainNumberDetailMapper.selectAllByTrainNumberIdOrderByStationIndex(param.getTrainNumberId());

        TrainNumberDetail trainNumberDetail = new TrainNumberDetail();
        BeanUtils.copyProperties(param, trainNumberDetail);
        trainNumberDetail.setStationIndex(trainNumberDetailList.size())
                .setFromCityId(trainStationService.getCityIdByStation(param.getFromStationId()))
                .setToCityId(trainStationService.getCityIdByStation(param.getToStationId()));

        trainNumberDetailMapper.insertSelective(trainNumberDetail);
        if (param.getEnd() == 1) {
            trainNumberDetailList.add(trainNumberDetail);
            trainNumber.setFromCityId(trainNumberDetailList.get(0).getFromCityId())
                    .setFromStationId(trainNumberDetailList.get(0).getFromStationId())
                    .setToStationId(trainNumberDetailList.get(trainNumberDetailList.size()-1).getToStationId())
                    .setToCityId(trainNumberDetailList.get(trainNumberDetailList.size()-1).getToCityId());
            trainNumberMapper.updateById(trainNumber);

            //TODO 考虑前台方便查询所有车次
        }

    }




}
