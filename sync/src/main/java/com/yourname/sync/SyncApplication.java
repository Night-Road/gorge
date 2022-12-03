package com.yourname.sync;

import com.google.common.collect.Lists;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;

@MapperScan("com.yourname")
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class SyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncApplication.class, args);
    }

    //定义redisCilent的实例
    @Bean(name = "shardedJedisPool")
    public ShardedJedisPool shardedJedisPool(){
        JedisShardInfo jedisShardInfo = new JedisShardInfo("10.100.76.116",6379);
        ArrayList<JedisShardInfo> jedisShardInforList = Lists.newArrayList(jedisShardInfo);
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, jedisShardInforList);
        return shardedJedisPool;
    }

}
