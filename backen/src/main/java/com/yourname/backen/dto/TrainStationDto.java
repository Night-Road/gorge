package com.yourname.backen.dto;

import com.yourname.backen.entity.TrainStation;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/20/2022 9:40 AM
 */

public class TrainStationDto extends TrainStation {
    @Getter
    @Setter
    private String cityName;
}
