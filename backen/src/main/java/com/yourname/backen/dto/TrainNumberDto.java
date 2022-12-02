package com.yourname.backen.dto;

import com.yourname.backen.entity.TrainNumber;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/20/2022 9:41 AM
 */


@Getter
@Setter
public class TrainNumberDto extends TrainNumber {
    private String fromStation;
    private String toStation;
}
