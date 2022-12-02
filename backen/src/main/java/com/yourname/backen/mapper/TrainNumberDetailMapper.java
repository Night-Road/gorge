package com.yourname.backen.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourname.backen.entity.TrainNumberDetail;
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
public interface TrainNumberDetailMapper extends BaseMapper<TrainNumberDetail> {



    int insertSelective(TrainNumberDetail trainNumberDetail);

    List<TrainNumberDetail> selectAllByTrainNumberIdOrderByStationIndex(@Param("trainNumberId") Integer trainNumberId);
}
