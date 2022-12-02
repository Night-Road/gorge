package com.yourname.backen.param;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/21/2022 11:22 PM
 */
@Getter
@Setter
@ToString
public class TrainCityParam {

    private Integer id;

    @NotBlank(message = "城市名称不可以为空")
    @Length(min = 2, max = 20, message = "城市名称长度在2-20个字符之间")
    private String name;
}
