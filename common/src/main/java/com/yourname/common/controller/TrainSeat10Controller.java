package com.yourname.common.controller;


import com.yourname.common.entity.TrainSeat;
import com.yourname.common.service.impl.TrainSeatServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yourName
 * @since 2022-10-16
 */
@RestController
@RequestMapping("/train-seat10")
public class TrainSeat10Controller {

    @Autowired
    private TrainSeatServiceImpl seatService;

    @ApiOperation(value = "获取用户位置信息")
    @GetMapping(value = "/info10")
    public TrainSeat get10(){

        return seatService.get10();
    }
    @ApiOperation(value = "获取用户位置信息")
    @GetMapping(value = "/info5")
    public TrainSeat get5(){

        return seatService.get5();
    }

    @ApiOperation(value = "获取用户位置信息")
    @PostMapping(value = "/info5")
    public int add(){

        return seatService.add();
    }
}
