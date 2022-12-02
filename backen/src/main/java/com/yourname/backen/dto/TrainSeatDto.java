package com.yourname.backen.dto;

import com.yourname.backen.entity.TrainSeat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description TODO
 * @Author dell
 * @Date 11/1/2022 11:37 PM
 */
@Getter
@Setter
@ToString
public class TrainSeatDto extends TrainSeat {
    private String trainNumber;
    private String fromStation;
    private String toStation;
    private String showStart;
    private String showEnd;

}
