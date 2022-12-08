package com.yourname.frontend.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Description TODO
 * @Author 你的名字
 * @Date 2022/12/6 13:56
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCountLeftParam {

    private int fromStationId;

    private int toStationId;

    @NotNull(message = "日期不可以为空")
    @Size(max = 8, min = 8, message = "日期不合法")
    private String date;
}
