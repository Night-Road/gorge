package com.yourname.backen.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/31/2022 12:26 PM
 */
@Data
public class TrainSeatSearchParam {
    @NotNull(message = "车次不可以为空")
    @Length(min=2,max = 20,message = "车次长度必须在0-20之间")
    private String trainNumber;

    @NotNull(message = "出发日期不可以为空")
    @Length(min = 8,max = 8,message = "出发日期必须为yyyyMMdd")
    private String ticket;

    private Integer status;

    private Integer carriageNum;

    private Integer rowNum;

    private Integer seatNum;
}
