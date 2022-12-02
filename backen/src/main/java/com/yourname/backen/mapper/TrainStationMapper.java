package com.yourname.backen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourname.backen.entity.TrainStation;
import org.apache.ibatis.annotations.MapKey;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface TrainStationMapper extends BaseMapper<TrainStation> {

    @MapKey("id")
    Map<Integer,Map<Integer,TrainStation>> getTrainStatiomMap();


}
