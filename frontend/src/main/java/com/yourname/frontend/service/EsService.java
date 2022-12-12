package com.yourname.frontend.service;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yourname.backen.entity.TrainNumber;
import com.yourname.backen.entity.TrainNumberDetail;
import com.yourname.sync.common.TrainESConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.*;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description TODO
 * @Author 你的名字
 * @Date 2022/12/4 14:25
 */
@Slf4j
@Service
public class EsService {


    @Resource
    RestHighLevelClient esClient;

    public IndexResponse index(IndexRequest indexRequest) throws Exception {
        try {
            return esClient.index(indexRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es.index error");
            throw e;
        }
    }

    public UpdateResponse update(UpdateRequest updateRequest) throws Exception {
        try {
            return esClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es.update error");
            throw e;
        }
    }

    public GetResponse get(GetRequest getRequest) throws Exception {
        try {
            return esClient.get(getRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es.get error");
            throw e;
        }
    }

    public MultiGetResponse multiGet(MultiGetRequest multiGetRequest) throws Exception {
        try {
            return esClient.multiGet(multiGetRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es.multiGet error");
            throw e;
        }
    }

    public BulkResponse bulk(BulkRequest bulkRequest) throws Exception {
        if (bulkRequest.requests().size() == 0) {
            log.warn("车次路线没有改变，无需更新es！");
            return null;
        }
        try {
            return esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es.bulk error");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * @Description A->B fromStationId->toStationId
     * @Author 你的名字
     * @Date 2022/12/4 17:13
     * trainNumber与它的trainNumberDetailList
     */
   // @Async("asyncServiceExecutor")
    public void savaEs(List<TrainNumberDetail> trainNumberDetailList, TrainNumber trainNumber) throws Exception {
        ArrayList<String> list =
                Lists.newArrayListWithExpectedSize(trainNumberDetailList.size() * trainNumberDetailList.size());
        // 检验trainNumberDetailList是否为空
        if (CollectionUtils.isEmpty(trainNumberDetailList)) {
            log.warn("车次无详情");
            // return;
        }
        if (trainNumberDetailList.size() == 1) {
            list.add(trainNumber.getFromStationId() + "_" + trainNumber.getToStationId());
        } else {  //trainNumberDetailList必须保证是有序的
            for (int i = 0; i < trainNumberDetailList.size(); i++) {
                int fromStationId = trainNumberDetailList.get(i).getFromStationId();
                for (int j = i; j < trainNumberDetailList.size(); j++) {
                    int toStationId = trainNumberDetailList.get(j).getToStationId();
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
        // 批量增加
        BulkRequest bulkRequest = new BulkRequest();
        MultiGetResponse multiGetItemResponses = multiGet(multiGetRequest);
        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            //无效响应
            if (itemResponse.isFailed()) {
                log.error("multiGet failed,itemResponse:{}", itemResponse);
                continue;
            }
            GetResponse response = itemResponse.getResponse();
            if (response == null) {
                log.error("no response here,response:{}", itemResponse);
                continue;
            }
            HashMap<String, Object> dataMap = Maps.newHashMap();
            //有效响应
            Map<String, Object> map = response.getSourceAsMap();
            if (map == null || !response.isExists()) {
                // add index
                dataMap.put(TrainESConstant.COLUMN_TRAIN_NUMBER, trainNumber.getName());
                IndexRequest indexRequest = new IndexRequest(TrainESConstant.INDEX, TrainESConstant.TYPE,
                        response.getId()).source(dataMap);
                //indexRequest.source(dataMap, XContentType.JSON);
                bulkRequest.add(indexRequest);
                continue;
            }

            String origin = (String) map.get(TrainESConstant.COLUMN_TRAIN_NUMBER);
            HashSet<String> set = Sets.newHashSet(Splitter.on(",").trimResults().omitEmptyStrings().split(origin));
            if (!set.contains(trainNumber.getName())) {
                // update index
                dataMap.put(TrainESConstant.COLUMN_TRAIN_NUMBER, origin + "," + trainNumber.getName());
                UpdateRequest updateRequest = new UpdateRequest(TrainESConstant.INDEX, TrainESConstant.TYPE,
                        response.getId()).doc(dataMap);
                bulkRequest.add(updateRequest);

            }
        }
        BulkResponse bulkResponse = bulk(bulkRequest);
        if(bulkResponse==null)return;
        log.info("es批量添加成功");
        if (bulkResponse.hasFailures()) {
            log.error(bulkResponse.buildFailureMessage());
            throw new RuntimeException("es bulk failed" + bulkResponse.buildFailureMessage());
        }
    }
}
