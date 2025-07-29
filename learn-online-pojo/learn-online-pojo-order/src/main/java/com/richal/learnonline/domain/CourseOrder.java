package com.richal.learnonline.domain;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Richal
 * @since 2025-07-29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_course_order")
public class CourseOrder extends Model<CourseOrder> {

    private static final long serialVersionUID = 1L;

    public static final Integer NO_PAY = 0;

    public static final Integer PAID = 1;

    public static final Integer ORDER_CANCEL = 2;

    public static final Integer PAID_FAIL = 3;

    @TableField(exist = false)
    private List<CourseOrderItem> courseOrderItems;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
    /**
     * 最后支付更新时间
     */
    @TableField("update_time")
    private Date updateTime;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 支付总的价格
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;
    /**
     * 秒杀数量
     */
    @TableField("total_count")
    private Integer totalCount;
    /**
     *     // 订单状态 ：
    //0下单成功待支付，
    //1支付成功订单完成
    //2用户手动取消订单(未支付)
    //3.支付失败
    //4.超时自动订单取消
     */
    @TableField("status_order")
    private Integer statusOrder;
    /**
     * 用户
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 订单标题
     */
    private String title;
    private Integer version;
    /**
     * 支付方式:0余额直接，1支付宝，2微信,3银联
     */
    @TableField("pay_type")
    private Integer payType;


    @Override
    protected Serializable pkVal() {
        return null;
    }
}
