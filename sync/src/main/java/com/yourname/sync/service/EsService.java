package com.yourname.sync.service;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
        try {
            return esClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es.bulk error");
            e.printStackTrace();
            throw e;
        }
    }
}
