package com.yourname.backen.param;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/23/2022 11:00 AM
 */
@Data
public class TrainNumberDetailParam {

    @NotNull(message = "车次不可以为空")
    private Integer trainNumberId;

    @NotNull(message = "出发站不可以为空")
    private Integer fromStationId;

    @NotNull(message = "到达站不可以为空")
    private Integer toStationId;

    @NotNull(message = "相对出发时间不可以为空")
    private Integer relativeTime;

    @NotNull(message = "等待时间不可以为空")
    private Integer waitTime;

    @NotNull(message = "价钱不可以为空")
    private String money;

    @Min(0)
    @Max(1)//1表示已经添加完毕
    private Integer end;
}
