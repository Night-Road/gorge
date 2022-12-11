package com.yourname.backen.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourname.backen.entity.TrainUserTraveller;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface TrainUserTravellerMapper extends BaseMapper<TrainUserTraveller> {

    List<TrainUserTraveller> searchAllByUserId(@Param("userId") Long userId);


}
