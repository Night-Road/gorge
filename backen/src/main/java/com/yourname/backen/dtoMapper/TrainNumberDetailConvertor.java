package com.yourname.backen.dtoMapper;


import com.yourname.backen.dto.TrainNumberDetailDto;
import com.yourname.backen.entity.TrainNumberDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface TrainNumberDetailConvertor {
    TrainNumberDetail DtoToTrainNumberDetail(TrainNumberDetailDto dto);

    TrainNumberDetailDto trainNumberDetailToDto(TrainNumberDetail detail);
}
