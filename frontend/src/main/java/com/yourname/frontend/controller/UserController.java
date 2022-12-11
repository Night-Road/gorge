package com.yourname.frontend.controller;

import com.yourname.backen.common.R;
import com.yourname.backen.entity.TrainTraveller;
import com.yourname.backen.entity.TrainUser;
import com.yourname.frontend.service.TrainTravellerService;
import com.yourname.frontend.util.DesensitizedUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/10/2022 9:12 AM
 */
@Controller
@RequestMapping("user")
public class UserController {

    @Resource
    TrainTravellerService trainUserTravellerService;

    @RequestMapping("getTravellers.json")
    @ResponseBody
    public R getTravellers(HttpServletRequest request) {

        //判断是否登录
        TrainUser user = (TrainUser) request.getSession().getAttribute("user");
        if (user == null) {
            return R.failed("用户未登录");
        }
        //查询 travellers
        List<TrainTraveller> travellers = trainUserTravellerService.getByUserId(user.getId());

        //数据脱敏
        List<TrainTraveller> desensitiveTrainTravellerList = travellers.stream().map(traveller -> {
            return TrainTraveller.builder()
                    .id(traveller.getId())
                    .name(traveller.getName())
                    .adultFlag(traveller.getAdultFlag())
                    .idNumber(DesensitizedUtils.hideIdNumber(traveller.getIdNumber()))
                    .build();
        }).collect(Collectors.toList());
        return R.success(desensitiveTrainTravellerList);
    }
}
