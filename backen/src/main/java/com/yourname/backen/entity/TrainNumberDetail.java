package com.yourname.backen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("train_number_detail")
@ApiModel(value="TrainNumberDetail对象", description="")
public class TrainNumberDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "车次")
    private Integer trainNumberId;

    @ApiModelProperty(value = "上一站")
    private Integer fromStationId;

    @ApiModelProperty(value = "上一站所在的城市")
    private Integer fromCityId;

    @ApiModelProperty(value = "到站")
    private Integer toStationId;

    @ApiModelProperty(value = "到站所在的城市")
    private Integer toCityId;

    @ApiModelProperty(value = "在整个车次中的顺序")
    private Integer stationIndex;

    @ApiModelProperty(value = "相对出发时间的分钟数")
    private Integer relativeTime;

    @ApiModelProperty(value = "到此站等待时间")
    private Integer waitTime;

    @ApiModelProperty(value = "价格")
    private String money;


}
