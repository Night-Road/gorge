package com.yourname.backen.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yourname.backen.beans.PageQuery;
import com.yourname.backen.beans.PageResult;
import com.yourname.backen.common.R;
import com.yourname.backen.dto.TrainSeatDto;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainSeat;
import com.yourname.backen.entity.TrainStation;
import com.yourname.backen.param.GenerateTicketParam;
import com.yourname.backen.param.PublishTicketParam;
import com.yourname.backen.param.TrainSeatSearchParam;
import com.yourname.backen.service.ITrainNumberDetailService;
import com.yourname.backen.service.ITrainNumberService;
import com.yourname.backen.service.ITrainSeatService;
import com.yourname.backen.service.ITrainStationService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
@RequestMapping("admin/train/seat")
public class TrainSeatController {

    @Autowired
    ITrainSeatService trainSeatService;

    @Autowired
    ITrainNumberDetailService trainNumberDetailService;

    @Autowired
    ITrainNumberService trainNumberService;

    @Autowired
    ITrainStationService trainStationService;

    @ApiOperation(value = "显示座位信息")
    @GetMapping(value = "list.page")
    public ModelAndView page() {
        return new ModelAndView("trainSeat");
    }


    @ApiOperation(value = "分页显示座位信息并返回json")
    @RequestMapping("search.json")
    @ResponseBody
    public R search(TrainSeatSearchParam param, PageQuery pageQuery) {
        List<TrainSeat> trainSeatList = trainSeatService.searchList(param, pageQuery);
        int total = trainSeatService.countList(param);
        if (total == 0) {
            return R.success(PageResult.<TrainSeatDto>builder().total(0).build());
        }
        if (CollectionUtils.isEmpty(trainSeatList)) {
            return R.success(PageResult.<TrainSeatDto>builder().total(total).build());
        }

        List<TrainStation> trainStationList = trainStationService.list();
        Map<Integer, String> map = trainStationList.stream().collect(Collectors.toMap(TrainStation::getId, TrainStation::getName));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        ZoneId zoneId = ZoneId.systemDefault();

        //数据适配
        List<TrainSeatDto> dtoList = trainSeatList.stream().map(trainSeat -> {
                    TrainSeatDto trainSeatDto = new TrainSeatDto();
                    trainSeatDto.setId(trainSeat.getId());
                    trainSeatDto.setFromStation(map.get(trainSeat.getFromStationId()));
                    trainSeatDto.setFromStationId(trainSeat.getFromStationId());
                    trainSeatDto.setToStation(map.get(trainSeat.getToStationId()));
                    trainSeatDto.setToStationId(trainSeat.getToStationId());
                    trainSeatDto.setTrainNumber(param.getTrainNumber());
                    trainSeatDto.setTrainNumberId(trainSeat.getTrainNumberId());
                    trainSeatDto.setShowStart(trainSeat.getTrainStart().atZone(zoneId).format(formatter));
                    trainSeatDto.setShowEnd(trainSeat.getTrainEnd().atZone(zoneId).format(formatter));
                    trainSeatDto.setStatus(trainSeat.getStatus());
                    trainSeatDto.setSeatLevel(trainSeat.getSeatLevel());
                    trainSeatDto.setCarriageNumber(trainSeat.getCarriageNumber());
                    trainSeatDto.setRowNumber(trainSeat.getRowNumber());
                    trainSeatDto.setSeatNumber(trainSeat.getSeatNumber());
                    trainSeatDto.setMoney(trainSeat.getMoney());
                    return trainSeatDto;
                }
        ).collect(Collectors.toList());

        return R.success(PageResult.<TrainSeatDto>builder().data(dtoList).total(total).build());
    }

    @ApiOperation(value = "生成座位")
    @PostMapping("generate.json")
    @ResponseBody
    public R generate(GenerateTicketParam param) {
        List<TrainSeat> trainSeatList = trainSeatService.generate(param);
        TrainNumber trainNumber = trainNumberService.getOne(new QueryWrapper<TrainNumber>()
                .eq("id", param.getTrainNumberId()));
        if (!trainSeatList.isEmpty()) {
            trainSeatService.saveBatch(trainSeatList, Integer.MAX_VALUE);
            return R.success();
        }

        List<TrainSeat> trainSeatList1 = trainSeatService.list(new QueryWrapper<TrainSeat>().eq("from_station_id",
                trainNumber.getFromStationId())
                .eq("train_number_id", trainNumber.getId()));
        TrainSeat trainSeat = trainSeatList1.get(0);
        if (trainSeatList.size() == 0 && param.getFromTime().equals(trainSeat.getTrainStart()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))) {
            return R.failed("该车次已经添加座位号，请修改发车时间");
        } else if (trainSeatList.size() == 0 && !param.getFromTime().equals(trainSeat.getTrainStart().toString())) {
            trainSeatService.remove(null);
            for (int i = 0; i < 3; i++) {
                generate(param);
            }
            return R.success("发车时间已改变！");

            //return R.success("发车时间已改变，如继续请再次提交");

        }

        //trainSeatService.saveBatch(trainSeatList, Integer.MAX_VALUE);
        return R.failed("不知道的异常");
    }

    @ResponseBody
    @PostMapping("publish.json")
    public R publish(PublishTicketParam param){
        trainSeatService.publish(param);
        return R.success();
    }
}
