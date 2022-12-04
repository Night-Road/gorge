package com.yourname.sync;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableCaching
@EnableAsync
@MapperScan("com.yourname")
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication
public class SyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SyncApplication.class, args);
    }

    //定义redisCilent的实例
    //@Bean(name = "shardedJedisPool")
    //public ShardedJedisPool shardedJedisPool(){
    //    JedisShardInfo jedisShardInfo = new JedisShardInfo("10.100.76.116",6379);
    //    ArrayList<JedisShardInfo> jedisShardInforList = Lists.newArrayList(jedisShardInfo);
    //    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    //    ShardedJedisPool shardedJedisPool = new ShardedJedisPool(config, jedisShardInforList);
    //    return shardedJedisPool;
    //}

}
