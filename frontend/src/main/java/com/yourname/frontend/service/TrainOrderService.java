package com.yourname.frontend.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import com.yourname.backen.entity.*;
import com.yourname.backen.exception.BusinessException;
import com.yourname.frontend.param.RubTicketParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Description TODO
 * @Author 你的名字
 * @Date 2022/12/16 13:14
 */
@Slf4j
@Service
public class TrainOrderService {


    /**
     * @Description 生成一个订单
     * @Author 你的名字
     * @Date 2022/12/16 13:15
     */
    public TrainOrder generateOrder(RubTicketParam param, List<TrainTraveller> travellers,
                                    Table<Integer, Integer, Pair<Integer, Integer>> seatTable,
                                    Map<String, String> seatMap,
                                    List<TrainNumberDetail> detailList,
                                    TrainNumber trainNumber,
                                    Long travellerId,
                                    Long userId) {
        // 主订单号
        String parentOrderId = UUID.randomUUID().toString();
        //订单详情列表
        ArrayList<TrainOrderDetail> trainOrderDetails = Lists.newArrayList();
        //最终抢到的座位列表
        ArrayList<TrainSeat> trainSeats = Lists.newArrayList();
        //为每一个乘客生成一个座位，并创建订单
        for (TrainTraveller traveller : travellers) {
            //筛选座位
            TrainSeat trainSeatTemp = selectOneMatch(seatTable, seatMap, detailList, trainNumber, travellerId, userId);
            if (trainSeatTemp == null) {
                //log.error("座位已经被占，请重试或者尝试别的座位");
                //throw new BusinessException("");
                break;
            }
            //构建订单详情
            TrainOrderDetail orderDetail = TrainOrderDetail.builder().build();//TODO
            trainOrderDetails.add(orderDetail);
            //在占座列表中注册
            trainSeats.add(trainSeatTemp);
        }
        if (trainSeats.size() < travellers.size()){
            rollBackPlace(trainSeats,detailList);
            throw new BusinessException("座位不足");
        }
        //生成主订单
        TrainOrder trainOrder = TrainOrder.builder().build(); //TODO

        //保存订单及其详情（事务型操作）

        //发送订单创建成功消息：可以处理消息时添加其他业务，比如发送消息

        //发送订单支付延迟检查消息

        //返回核心订单数据
        return trainOrder;
    }

    /**
     * @Description 为用户筛选出一个空座位：满足carriage\roe\seat为空，并且完成占座
     * @Author 你的名字
     * @Date 2022/12/16 13:44
     */
    private TrainSeat selectOneMatch(Table<Integer, Integer, Pair<Integer, Integer>> seatTable,
                                     Map<String, String> seatMap,
                                     List<TrainNumberDetail> detailList,
                                     TrainNumber trainNumber,
                                     Long travellerId,
                                     Long userId) {

        //TODO
        return null;

    }

    /**
     * @Description 在路线上的每一段都要进行占座
     * 比如北京->大连 ： 北京->京州，京州->大连
     * 因此这两个车次详情都需要进行同步占座，即事务性操作
     * @Author 你的名字
     * @Date 2022/12/16 13:51
     */
    private TrainSeat place(TrainSeat trainSeat, List<TrainNumberDetail> trainNumberDetailList) {
        //TODO
        return null;

    }

    /**
     * @Description 占座失败，进行回滚
     * @Author 你的名字
     * @Date 2022/12/16 13:55
     */
    private void rollBackPlace(List<TrainSeat> trainSeatList, List<TrainNumberDetail> trainNumberDetailList){

        //TODO
    }

}
