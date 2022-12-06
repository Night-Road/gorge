package com.yourname.frontend.Dto1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description TODO
 * @Author 你的名字
 * @Date 2022/12/6 14:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainNumberLeftDto {

    private int id;
    private String number;
    private long countLeft;
}
