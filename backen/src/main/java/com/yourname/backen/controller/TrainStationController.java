package com.yourname.backen.controller;


import com.google.common.collect.Lists;
import com.yourname.backen.common.R;
import com.yourname.backen.dto.TrainStationDto;
import com.yourname.backen.entity.TrainStation;
import com.yourname.backen.service.impl.TrainCityServiceImpl;
import com.yourname.backen.service.impl.TrainStationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Controller
@RequestMapping("admin/train/station")
public class TrainStationController {
    @Autowired
    TrainCityServiceImpl trainCityService;
    @Autowired
    TrainStationServiceImpl trainStationService;
    @RequestMapping("list.page")
    public String page(){
        return "trainStation";
    }

    /**
     * 数据适配
     * @return
     */
    @ResponseBody
    @GetMapping("list.json")
    public R list(){
        List<TrainStation> trainStationList = trainStationService.list();
        ArrayList<TrainStationDto> trainStationDtos = Lists.newArrayList();
        Map map = trainCityService.getAllMaps();


        for (int i = 0; i < trainStationList.size(); i++) {
            TrainStationDto trainStationDto = new TrainStationDto();
            trainStationDto.setId(trainStationList.get(i).getId());
            trainStationDto.setCityId(trainStationList.get(i).getCityId());
            trainStationDto.setName(trainStationList.get(i).getName());
            trainStationDto.setCityName((String) map.get(trainStationList.get(i).getCityId()));

            trainStationDtos.add(trainStationDto);

        }

        return R.success(trainStationDtos);
    }


    @ResponseBody
    @RequestMapping("save.json")
    public R save(){
        return R.success();
    }
    @ResponseBody
    @RequestMapping("update.json")
    public R update(){
        return R.success();
    }

}
