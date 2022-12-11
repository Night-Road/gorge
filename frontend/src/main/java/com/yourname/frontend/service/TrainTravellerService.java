package com.yourname.frontend.service;

import com.google.common.collect.Lists;
import com.yourname.backen.entity.TrainTraveller;
import com.yourname.backen.entity.TrainUserTraveller;
import com.yourname.backen.mapper.TrainTravellerMapper;
import com.yourname.backen.mapper.TrainUserTravellerMapper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/10/2022 9:15 AM
 */
@Service
public class TrainTravellerService {
    @Resource
    private TrainTravellerMapper trainTravellerMapper;

    @Resource
    private TrainUserTravellerMapper trainUserTravellerMapper;

    public List<TrainTraveller> getByUserId(long userId){
        List<TrainUserTraveller> trainUserTravellerList = trainUserTravellerMapper.searchAllByUserId(userId);
        if(CollectionUtils.isEmpty(trainUserTravellerList))return null;
        ArrayList<Long> idList = Lists.newArrayList();
        trainUserTravellerList.forEach(t->{
            idList.add(t.getTravellerId());
                });
        return trainTravellerMapper.selectAllByIdList(idList);
    }
}
