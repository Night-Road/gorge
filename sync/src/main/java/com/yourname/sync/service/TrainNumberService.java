package com.yourname.sync.service;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.google.common.collect.Lists;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.backen.mapper.TrainNumberDetailMapper;
import com.yourname.backen.mapper.TrainNumberMapper;
import com.yourname.backen.util.JsonMapper;
import com.yourname.sync.common.TrainESConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/3/2022 10:46 AM
 */
@Service
@Slf4j
public class TrainNumberService {

    @Resource
    private TrainNumberMapper trainNumberMapper;

    @Resource
    private TrainNumberDetailMapper trainNumberDetailMapper;

    //@Autowired
    //private TrainCacheService trainCacheService;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    @Resource
    private EsService esService;

    public void handle(List<Column> columns, EventType eventType) {
        if (eventType != EventType.UPDATE) {
            log.info("no need update,no need care");
            return;
        }
        //获取id
        int trainNumberId = 0;
        for (Column column : columns) {
            if ("id".equals(column.getName())) {
                trainNumberId = Integer.parseInt(column.getValue());
                break;
            }
        }
        TrainNumber trainNumber = trainNumberMapper.selectById(trainNumberId);
        if (trainNumber == null) {
            log.error("not find trainNumber,trainNumberId:{}", trainNumberId);
            return;
        }
        // Wrapper<TrainNumberDetail> wrapper =  new QueryWrapper<TrainNumberDetail>().eq("train_number_id",
        //   trainNumberId);
        List<TrainNumberDetail> trainNumberDetails = trainNumberDetailMapper.selectAllByTrainNumberIdOrderByStationIndex(trainNumberId);
        //  List<TrainNumberDetail> trainNumberDetails1 = trainNumberDetailMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(trainNumberDetails)) {
            log.warn("this trainNumber has no detail,id is {}", trainNumber.getName());
            return;
        }
        //redis 存储车次信息
        redisTemplate.opsForValue().set("TN_" + trainNumber.getName(), JsonMapper.obj2String(trainNumberDetails));
        log.info("车次信息已修改");

        //es 存储车次信息

    }

    /**
     * @Description A->B fromStationId->toStationId
     * @Author 你的名字
     * @Date 2022/12/4 17:13
     * trainNumber与它的trainNumberDetailList
     */
    public void savaEs(List<TrainNumberDetail> trainNumberDetailList, TrainNumber trainNumber) throws Exception {
        ArrayList<String> list = Lists.newArrayList();
        // 检验trainNumberDetailList是否为空
        if (CollectionUtils.isEmpty(trainNumberDetailList)) {
            log.warn("车次无详情");
            // return;
        }
        if (trainNumberDetailList.size() == 1) {
            list.add(trainNumber.getFromStationId() + "_" + trainNumber.getToStationId());
        } else {  //trainNumberDetailList必须保证是有序的
            int fromStationId = trainNumber.getFromStationId();
            for (int i = 0; i < trainNumberDetailList.size(); i++) {
                for (int j = i; j < trainNumberDetailList.size(); j++) {
                    int toStationId = trainNumber.getToStationId();
                    list.add(fromStationId + "_" + toStationId);
                }
            }
        }
        // 建立请求
        MultiGetRequest multiGetRequest = new MultiGetRequest();
        for (String item : list) {
            multiGetRequest.add(new MultiGetRequest.Item(TrainESConstant.INDEX, TrainESConstant.TYPE, item));
        }
        // 使用客户端发送请求
        MultiGetResponse multiGetItemResponses = esService.multiGet(multiGetRequest);
        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            //无效响应
            if(itemResponse.isFailed()){
                log.error("multiGet failed,itemResponse:{}",itemResponse);
                continue;
            }
            GetResponse response = itemResponse.getResponse();
            if(response==null){
                log.error("no response here,response:{}",itemResponse);
                continue;
            }
            //有效响应
            Map<String, Object> map = response.getSourceAsMap();
            if(map == null){
                // TODO add index
                continue;
            }
        }
    }
}
