package com.yourname.frontend.controller;

import com.yourname.backen.common.R;
import com.yourname.backen.entity.TrainUser;
import com.yourname.frontend.service.TrainStationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/6/2022 10:27 AM
 */
@Controller
public class IndexController {

    @Resource
    TrainStationService trainStationService;

    @RequestMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @RequestMapping("mockLogin.json")
    @ResponseBody
    public R mockLogin(HttpServletRequest request) {
        TrainUser trainUser = TrainUser.builder().id(1L).name("test").build();
        request.getSession().setAttribute("user", trainUser);
        return R.success();
    }

    @RequestMapping("logout.json")
    @ResponseBody
    public R logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return R.success();
    }

    @RequestMapping("stationList.json")
    @ResponseBody
    public R stationList() {
        return R.success(trainStationService.getAll());
    }
    @RequestMapping("info.json")
    @ResponseBody
    public R info(HttpServletRequest request) {
        return R.success(request.getSession().getAttribute("user"));
    }
}
