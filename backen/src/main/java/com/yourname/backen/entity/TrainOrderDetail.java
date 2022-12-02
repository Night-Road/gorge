package com.yourname.backen.entity;

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
 * @since 2022-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("train_order_detail")
@ApiModel(value="TrainOrderDetail对象", description="")
public class TrainOrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "主订单号")
    private String parentOrderId;

    @ApiModelProperty(value = "车票号")
    private String ticket;

    @ApiModelProperty(value = "登录用户")
    private Long userId;

    @ApiModelProperty(value = "绑定的乘客")
    private Long travellerId;

    @ApiModelProperty(value = "车次")
    private Integer trainNumberId;

    @ApiModelProperty(value = "出发站点")
    private Integer fromStationId;

    @ApiModelProperty(value = "到达站点")
    private Integer toStationId;

    @ApiModelProperty(value = "车厢")
    private Integer carriageNumber;

    @ApiModelProperty(value = "排")
    private Integer rowNumber;

    @ApiModelProperty(value = "A~F")
    private Integer seatNumber;

    @ApiModelProperty(value = "座位等级，0：特等座，1：一等座，2：二等座，3：无座")
    private Integer seatLevel;

    @ApiModelProperty(value = "价格")
    private Integer money;

    @ApiModelProperty(value = "展示")
    private String showNumber;

    @ApiModelProperty(value = "状态，10：已占票等待支付，20：已支付，30：超时未支付自动取消")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "最晚支付时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "发车时间")
    private LocalDateTime trainStart;

    @ApiModelProperty(value = "到达时间")
    private LocalDateTime trainEnd;

    @ApiModelProperty(value = "最后更新时间")
    private LocalDateTime updateTime;


}
