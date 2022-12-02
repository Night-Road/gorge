package com.yourname.backen;

import com.yourname.backen.service.impl.TrainCityServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class BackenApplicationTests {

    @Autowired
    private TrainCityServiceImpl trainCityService;

    @Test
    void contextLoads() {
        Map<Integer, String> maps = trainCityService.getAllMaps();
        System.out.println(maps);

    }

}
