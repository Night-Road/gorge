package com.yourname.frontend.controller;

import com.yourname.backen.common.R;
import com.yourname.frontend.Dto.TrainNumberLeftDto;
import com.yourname.frontend.param.SearchCountLeftParam;
import com.yourname.frontend.service.TrainSeatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @Author 你的名字
 * @Date 2022/12/6 13:52
 */
@Slf4j
@Controller
@RequestMapping("front")
public class FrontController {

    @Resource(name = "FrontedTrainSeatService")
    TrainSeatService trainSeatService;

    /**
     * @Description 查询余票
     * @Author 你的名字
     * @Date 2022/12/6 13:54
     */
    @ResponseBody
    @RequestMapping("searchLeftCount.json")
    public R searchCountLeft(SearchCountLeftParam param) {
        try {
            List<TrainNumberLeftDto> dtoList = trainSeatService.searchCountLeftService(param);
            return R.success(dtoList);
        }catch (Exception e){
            log.error("查询失败");
            return R.failed("查询失败，请稍后重试");
        }


    }

}
