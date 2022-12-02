package com.yourname.backen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yourName
 * @since 2022-10-07
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("train_number")
@ApiModel(value="TrainNumber对象", description="")
public class TrainNumber implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "车次")
    private String name;

    @ApiModelProperty(value = "始发站")
    private Integer fromStationId;

    @ApiModelProperty(value = "始发城市")
    private Integer fromCityId;

    @ApiModelProperty(value = "终点站")
    private Integer toStationId;

    @ApiModelProperty(value = "终点城市")
    private Integer toCityId;

    @ApiModelProperty(value = "座位类型，CRH2,CRH5")
    private String trainType;

    @ApiModelProperty(value = "类型，1：高铁，2：动车，3：特快，4：普快")
    private Integer type;

    @ApiModelProperty(value = "车厢数量")
    private Integer seatNum;


}
