package com.yourname.backen.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yourname.backen.entity.TrainSeat;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@DS("sharding")
public interface TrainSeatMapper extends BaseMapper<TrainSeat> {

    int updateTrainStartAndTrainEnd(@Param("trainStart") LocalDateTime trainStart, @Param("trainEnd") LocalDateTime trainEnd);



    List<TrainSeat> searchAll(@Param("trainNumberId") int trainNumberId, @Param("ticket") String ticket, @Param(
            "status") Integer status, @Param("carriageNum") Integer carriageNum, @Param("rowNum") Integer rowNum,
                              @Param("seatNum") Integer seatNum, @Param("offset") int offset,
                              @Param("pageSize") Integer pageSize);

    int countList(@Param("trainNumberId") int trainNumberId, @Param("ticket") String ticket, @Param(
            "status") Integer status, @Param("carriageNum") Integer carriageNum, @Param("rowNum") Integer rowNum,
                  @Param("seatNum") Integer seatNum);

    int batchPublish(@Param("trainSeatIdList") List<Long> trainSeatIdList,@Param("trainNumberId") int trainNumberId);

}
