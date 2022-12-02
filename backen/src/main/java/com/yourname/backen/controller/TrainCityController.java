package com.yourname.backen.controller;


import com.yourname.backen.param.TrainCityParam;
import com.yourname.backen.common.R;
import com.yourname.backen.entity.TrainCity;
import com.yourname.backen.service.impl.TrainCityServiceImpl;
import com.yourname.backen.util.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@Controller
@RequestMapping("admin/train/city")
public class TrainCityController {

    @Autowired
    TrainCityServiceImpl trainCityService;


    @RequestMapping("list.page")
    public String page() {
        return "trainCity";
    }


    /**
     * 需要对返回数据做一个适配
     * @return
     */
    @ResponseBody
    @GetMapping("list.json")
    public R list() {
        List<TrainCity> trainCities = trainCityService.getAll();
        return R.success(trainCities);

    }

    @ResponseBody
    @RequestMapping("save.json")
    public R save(TrainCityParam param) {

        BeanValidator.check(param);
        trainCityService.save(param);
        return R.success();
    }

    @ResponseBody
    @PostMapping("update.json")
    public R update(TrainCityParam param) {
        trainCityService.updateName(param);

        return R.success();
    }
}
