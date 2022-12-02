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
@TableName("train_order")
@ApiModel(value="TrainOrder对象", description="")
public class TrainOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderId;

    @ApiModelProperty(value = "车票号")
    private String ticket;

    @ApiModelProperty(value = "登录用户")
    private Long userId;

    @ApiModelProperty(value = "车次")
    private Integer trainNumberId;

    @ApiModelProperty(value = "出发站点")
    private Integer fromStationId;

    @ApiModelProperty(value = "到达站点")
    private Integer toStationId;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "发车时间")
    private LocalDateTime trainStart;

    @ApiModelProperty(value = "到达时间")
    private LocalDateTime trainEnd;

    @ApiModelProperty(value = "价格")
    private Integer totalMoney;

    @ApiModelProperty(value = "支付状态，10：已占票等待支付，20：已支付，30：超时未支付自动取消，40：支付后退款")
    private Integer status;

    @ApiModelProperty(value = "退款状态，10：提交退款，20：退款审核成功，30：退款中，40：退款成功, 50:退款失败")
    private Integer refundStatus;

    @ApiModelProperty(value = "最晚支付时间")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "最后更新时间")
    private LocalDateTime updateTime;


}
