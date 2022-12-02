package com.yourname.backen.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourname.backen.entity.TrainNumber;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
public interface TrainNumberMapper extends BaseMapper<TrainNumber> {

    int insertSelective(TrainNumber trainNumber);

    int updateToStationIdById(@Param("toStationId") Integer toStationId, @Param("id") Integer id);


    TrainNumber selectByName(@Param("name") String name);

}
