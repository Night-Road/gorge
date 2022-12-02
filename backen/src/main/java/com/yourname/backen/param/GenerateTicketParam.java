package com.yourname.backen.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/30/2022 9:46 AM
 */
@Data
public class GenerateTicketParam {

    @NotNull(message = "车次不可以为空")
    private Integer trainNumberId;

    @NotNull(message = "发车时间不能为空")
    private String fromTime;
}
