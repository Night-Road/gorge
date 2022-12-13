package com.yourname.frontend.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Description TODO
 * @Author dell
 * @Date 12/13/2022 9:16 AM
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RubTicketParam {

    private int fromStationId;

    private int toStationId;

    @NotNull(message = "日期不可以为空")
    @Size(max = 8, min = 8, message = "日期不合法")
    private String date;

    @NotNull(message = "车次不可以为空")
    private String numberName;

    @NotNull(message = "必须选择乘车人")
    private String travellerIds;
}
