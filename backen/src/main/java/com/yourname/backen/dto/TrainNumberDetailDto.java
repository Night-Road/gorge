package com.yourname.backen.dto;


import com.yourname.backen.entity.TrainNumberDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class TrainNumberDetailDto extends TrainNumberDetail {
    @Getter
    @Setter
    private String fromStation;

    @Getter
    @Setter
    private String toStation;

    @Getter
    @Setter
    private String trainNumber;
}
