//package com.yourname.sync.service;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.ShardedJedis;
//import redis.clients.jedis.ShardedJedisPool;
//
//import javax.annotation.Resource;
//
///**
// * @Description
// * @Author dell
// * @Date 11/6/2022 11:26 PM
// */
//@Service
//@Slf4j
//public class TrainCacheService {
//    @Resource
//    private ShardedJedisPool shardedJedisPool;
//
//    private ShardedJedis getInstance() {
//        return shardedJedisPool.getResource();
//    }
//
//    /**
//     * 关闭连接
//     */
//    private void safeClosed(ShardedJedis shardedJedis) {
//        try {
//            if (shardedJedis != null) {
//                shardedJedis.close();
//            }
//        } catch (Exception e) {
//            log.error("jedis close error:{}", e);
//        }
//    }
//
//    public void set(String cacheKey,String value){
//        if(value==null){
//            return;
//        }
//        ShardedJedis shardedJedis = null;
//        try {
//            ShardedJedis jedis = getInstance();
//            jedis.set(cacheKey,value);
//        }catch (Exception e){
//            log.error("jdies set error,cacheKey:{},value:{}",cacheKey,value);
//            throw e;
//        }finally {
//            safeClosed(shardedJedis);
//        }
//    }
//
//    public void get(String cacheKey){
//        ShardedJedis shardedJedis = null;
//        try {
//            ShardedJedis jedis = getInstance();
//            jedis.get(cacheKey);
//        }catch (Exception e){
//            log.error("jdies set error,cacheKey:{}",cacheKey);
//            throw e;
//        }finally {
//            safeClosed(shardedJedis);
//        }
//    }
//
//    public void hset(String cacheKey,String field, String value){
//        ShardedJedis shardedJedis = null;
//        try {
//            ShardedJedis jedis = getInstance();
//            jedis.hset(cacheKey,field,value);
//        }catch (Exception e){
//            log.error("jdies hset error,cacheKey:{},field:{},value:{}",cacheKey,field,value);
//            throw e;
//        }finally {
//            safeClosed(shardedJedis);
//        }
//    }
//
//    public void hincr(String cacheKey,String field, Long value){
//        ShardedJedis shardedJedis = null;
//        try {
//            ShardedJedis jedis = getInstance();
//            jedis.hincrBy(cacheKey,field,value);
//        }catch (Exception e){
//            log.error("jdies hincr error,cacheKey:{},field:{},value:{}",cacheKey,field,value);
//            throw e;
//        }finally {
//            safeClosed(shardedJedis);
//        }
//    }
//}
