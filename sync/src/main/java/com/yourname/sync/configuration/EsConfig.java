package com.yourname.sync.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description TODO
 * @Author 你的名字
 * @Date 2022/12/4 13:45
 */

@Slf4j
@Configuration
public class EsConfig {

    private final static int CONNECT_TIMEOUT = 100;
    private final static int SOCKET_TIMEOUT = 60 * 1000;
    private final static int REQUSET_TIMEOUT = SOCKET_TIMEOUT;

    public final static BasicHeader[] basicHeaders = new BasicHeader[]{
            new BasicHeader("Accept", "application/json;charset=UTF-8")
    };


    @Bean
    public RestHighLevelClient Client() {
        log.info("es client init start");
        RestClientBuilder builder = RestClient.builder(new HttpHost(IpConfig.IP,
                9200, "http"));
        builder.setDefaultHeaders(basicHeaders).
                setRequestConfigCallback((RequestConfig.Builder configBuilder) -> {
                    configBuilder.setConnectionRequestTimeout(REQUSET_TIMEOUT);
                    configBuilder.setConnectTimeout(CONNECT_TIMEOUT);
                    configBuilder.setSocketTimeout(SOCKET_TIMEOUT);
                    return configBuilder;
                });
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
        log.info("es client init end");
        return restHighLevelClient;
    }
}
