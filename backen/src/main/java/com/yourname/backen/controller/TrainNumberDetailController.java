package com.yourname.backen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourname.backen.common.R;
import com.yourname.backen.dto.TrainNumberDetailDto;
import com.yourname.backen.dtoMapper.TrainNumberDetailConvertor;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.backen.entity.TrainStation;
import com.yourname.backen.exception.BusinessException;
import com.yourname.backen.mapper.TrainNumberDetailMapper;
import com.yourname.backen.mapper.TrainNumberMapper;
import com.yourname.backen.param.TrainNumberDetailParam;
import com.yourname.backen.service.impl.TrainNumberDetailServiceImpl;
import com.yourname.backen.service.impl.TrainNumberServiceImpl;
import com.yourname.backen.service.impl.TrainStationServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Controller
@RequestMapping("admin/train/numberDetail")
public class TrainNumberDetailController {

    @Resource
    TrainNumberDetailServiceImpl trainNumberDetailService;
    @Resource
    TrainNumberMapper trainNumberMapper;
    @Resource
    TrainNumberDetailMapper trainNumberDetailMapper;
    @Resource
    TrainStationServiceImpl trainStationService;

    @Resource
    TrainNumberServiceImpl trainNumberService;

    @Resource
    TrainNumberDetailConvertor trainNumberDetailConvertor;

    @RequestMapping("list.page")
    public String page() {
        return "trainNumberDetail";
    }

    @ResponseBody
    @RequestMapping("list.json")
    public R list() {
        List<TrainNumberDetail> trainNumberDetailList = trainNumberDetailService.list();
        List<TrainStation> trainStationList = trainStationService.list();
        Map<Integer, String> stationMap = trainStationList.stream().collect(Collectors.toMap(TrainStation::getId, TrainStation::getName));
        List<TrainNumber> trainNumberList = trainNumberService.list();
        Map<Integer, String> numberMap = trainNumberList.stream().collect(Collectors.toMap(TrainNumber::getId, TrainNumber::getName));
        List<TrainNumberDetail> dtoList = trainNumberDetailList.stream().map(detail -> {
            //TrainNumberDetailDto dto = new TrainNumberDetailDto();
            //dto.setId(detail.getId());
            //dto.setFromStation(stationMap.get(detail.getFromStationId()));
            //dto.setToStation(stationMap.get(detail.getToStationId()));
            //dto.setFromCityId(detail.getFromCityId());
            //dto.setToCityId(detail.getToCityId());
            //dto.setFromStationId(detail.getFromStationId());
            //dto.setToStationId(detail.getToStationId());
            //dto.setTrainNumberId(detail.getTrainNumberId());
            //dto.setTrainNumber(numberMap.get(detail.getTrainNumberId()));
            //dto.setStationIndex(detail.getStationIndex());
            //dto.setRelativeTime(detail.getRelativeTime());
            //dto.setWaitTime(detail.getWaitTime());
            //dto.setMoney(detail.getMoney());
            TrainNumberDetailDto dto= trainNumberDetailConvertor.trainNumberDetailToDto(detail);
            dto.setFromStation(stationMap.get(detail.getFromStationId()));
            dto.setToStation(stationMap.get(detail.getToStationId()));
            dto.setTrainNumber(numberMap.get(detail.getTrainNumberId()));
            return dto;
        }).collect(Collectors.toList());
        return R.success(dtoList);
    }

    @ResponseBody
    @RequestMapping("save.json")
    public R save(TrainNumberDetailParam param) {
        trainNumberDetailService.save(param);
        return R.success();
    }


    @ResponseBody
    @RequestMapping("delete.json")
    public R delete(@RequestParam("id") Integer id) {
        TrainNumberDetail trainNumberDetail = trainNumberDetailService.getById(id);
        List<TrainNumberDetail> trainNumberDetailList =
                trainNumberDetailMapper.selectAllByTrainNumberIdOrderByStationIndex(trainNumberDetail.getTrainNumberId());
        System.out.println(trainNumberDetailList.get(trainNumberDetailList.size() - 1).getId());
        if (!trainNumberDetailList.get(trainNumberDetailList.size() - 1).getId().equals(id)) {

            throw new BusinessException("非末班站点不能删除");
        }

        trainNumberDetailService.removeById(id);

        TrainNumber trainNumber = trainNumberService.getOne(new QueryWrapper<TrainNumber>()
                .eq("id", trainNumberDetail.getTrainNumberId()));

        //用于前台显示为空
        trainNumberMapper.updateToStationIdById(12025875, trainNumber.getId());
        return R.success();
    }
}
