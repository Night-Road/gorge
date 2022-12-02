package com.yourname.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yourName
 * @since 2022-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("train_seat")
@ApiModel(value="TrainSeat对象", description="")
public class TrainSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "日期，yyyyMMdd")
    private String ticket;

    @ApiModelProperty(value = "登录用户id")
    private Long userId;

    @ApiModelProperty(value = "绑定的乘客id")
    private Long travellerId;

    @ApiModelProperty(value = "车次")
    private Integer trainNumberId;

    @ApiModelProperty(value = "车厢")
    private Integer carriageNumber;

    @ApiModelProperty(value = "排")
    private Integer rowNumber;

    @ApiModelProperty(value = "A~F")
    private Integer seatNumber;

    @ApiModelProperty(value = "座位等级，0：特等座，1：一等座，2：二等座，3：无座")
    private Integer seatLevel;

    @ApiModelProperty(value = "发车时间")
    private LocalDateTime trainStart;

    @ApiModelProperty(value = "到达时间")
    private LocalDateTime trainEnd;

    @ApiModelProperty(value = "价格")
    private Integer money;

    @ApiModelProperty(value = "展示")
    private String showNumber;

    @ApiModelProperty(value = "状态，0:初始，1：已放票，2：占有票等待支付，3：已支付，4：不外放")
    private Integer status;

    private Integer fromStationId;

    private Integer toStationId;


}
