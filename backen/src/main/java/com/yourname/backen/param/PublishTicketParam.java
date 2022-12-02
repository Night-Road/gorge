package com.yourname.backen.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description TODO
 * @Author dell
 * @Date 11/3/2022 9:22 AM
 */
@Data
public class PublishTicketParam {
    @NotNull(message = "车次并不可以为空")
    private String trainNumber;

    @NotNull(message = "必须选中座位")
    private String trainSeatIds;
}
