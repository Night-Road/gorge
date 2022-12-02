package com.yourname.backen.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @Description TODO
 * @Author dell
 * @Date 10/22/2022 11:07 PM
 */
@Getter
@Setter
public class TrainNumberParam {


    private Integer id;

    @NotBlank(message = "车次不可以为空")
    @Length(min = 2,max = 10,message = "车次名称长度要在2-10之间")
    private String name;

    @NotBlank(message = "车次类型不可以为空")
    private String trainType;

    @Max(value = 4,message = "类型不合法")
    @Min(value = 1,message = "类型不合法")
    private Integer type;
}
