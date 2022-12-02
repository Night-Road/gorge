package com.yourname.backen.controller;


import com.google.common.collect.Lists;
import com.yourname.backen.common.R;
import com.yourname.backen.dto.TrainNumberDto;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.param.TrainNumberParam;
import com.yourname.backen.service.impl.TrainNumberServiceImpl;
import com.yourname.backen.service.impl.TrainStationServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Controller
@RequestMapping("admin/train/number")
public class TrainNumberController {
    @Autowired
    TrainNumberServiceImpl trainNumberService;
    @Autowired
    TrainStationServiceImpl trainStationService;

    @RequestMapping("list.page")
    public String page() {
        return "trainNumber";
    }

    @ResponseBody
    @RequestMapping("list.json")
    public R list() {
        List<TrainNumber> trainNumberList = trainNumberService.list();
        Map<Integer,String> trainStationMap = trainStationService.getAllMap();
        ArrayList<TrainNumberDto> trainNumberDtos = Lists.newArrayList();
        for (TrainNumber trainNumber : trainNumberList) {

            TrainNumberDto trainNumberDto = new TrainNumberDto();
            BeanUtils.copyProperties(trainNumber, trainNumberDto);

            //QueryWrapper<TrainStation> wrapperfrom = new QueryWrapper<>();
            //wrapperfrom.eq("id",trainNumber.getFromCityId());
            //
            //TrainStation from = trainStationService.getOne(wrapperfrom);
            String from = trainStationMap.get(trainNumber.getFromStationId());
            trainNumberDto.setFromStation(from);

            //QueryWrapper<TrainStation> wrapperto = new QueryWrapper<>();
            //wrapperto.eq("id",trainNumber.getToStationId());
            //
            //TrainStation to = trainStationService.getOne(wrapperto);
            String to = trainStationMap.get(trainNumber.getToStationId());
            trainNumberDto.setToStation(to);
            trainNumberDtos.add(trainNumberDto);
        }

        return R.success(trainNumberDtos);
    }

    @ResponseBody
    @RequestMapping("save.json")
    public R save(TrainNumberParam trainNumberParam) {
        trainNumberService.saveOne(trainNumberParam);
        return R.success();
    }

    @ResponseBody
    @RequestMapping("update.json")
    public R update(TrainNumberParam trainNumberParam) {
        trainNumberService.updateOne(trainNumberParam);
        return R.success();
    }
}
