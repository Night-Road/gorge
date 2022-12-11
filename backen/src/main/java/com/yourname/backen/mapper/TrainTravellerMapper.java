package com.yourname.backen.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourname.backen.entity.TrainTraveller;
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
public interface TrainTravellerMapper extends BaseMapper<TrainTraveller> {
    List<TrainTraveller> selectAllByIdList(@Param("idList") List<Long> idList);
}
