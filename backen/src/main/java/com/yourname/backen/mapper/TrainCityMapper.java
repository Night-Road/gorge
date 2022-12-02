package com.yourname.backen.mapper;

import com.yourname.backen.entity.TrainCity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface TrainCityMapper extends BaseMapper<TrainCity> {

    int countByNameAndId(@Param("name") String name, @Param("trainCityId") Integer trainCityId);

    int insertSelective(TrainCity trainCity);
}
