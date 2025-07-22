package com.richal.learnonline.domain;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员基本信息
 * </p>
 *
 * @author Richal
 * @since 2025-07-20
 */
@TableName("t_user_base_info")
public class UserBaseInfo extends Model<UserBaseInfo> {

    private static final long serialVersionUID = 1L;

    private Long id;
    @TableField("create_time")
    private Long createTime;
    @TableField("update_time")
    private Long updateTime;
    /**
     * 注册渠道
     */
    @TableField("reg_channel")
    private Integer regChannel;
    /**
     * QQ
     */
    private String qq;
    /**
     * 用户等级
     */
    private Integer level;
    /**
     * 成长值
     */
    @TableField("grow_score")
    private Integer growScore;
    /**
     * 推荐人
     */
    @TableField("refer_id")
    private Long referId;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 生日
     */
    private Date birthday;
    @TableField("area_code")
    private String areaCode;
    private String address;

    // 构造函数，供Builder使用
    public UserBaseInfo() {}
    
    // 静态方法创建Builder实例
    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRegChannel() {
        return regChannel;
    }

    public void setRegChannel(Integer regChannel) {
        this.regChannel = regChannel;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getGrowScore() {
        return growScore;
    }

    public void setGrowScore(Integer growScore) {
        this.growScore = growScore;
    }

    public Long getReferId() {
        return referId;
    }

    public void setReferId(Long referId) {
        this.referId = referId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "UserBaseInfo{" +
        ", id=" + id +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", regChannel=" + regChannel +
        ", qq=" + qq +
        ", level=" + level +
        ", growScore=" + growScore +
        ", referId=" + referId +
        ", sex=" + sex +
        ", birthday=" + birthday +
        ", areaCode=" + areaCode +
        ", address=" + address +
        "}";
    }
    
    // Builder内部类
    public static class Builder {
        private UserBaseInfo userBaseInfo = new UserBaseInfo();
        
        public Builder id(Long id) {
            userBaseInfo.setId(id);
            return this;
        }
        
        public Builder createTime(Long createTime) {
            userBaseInfo.setCreateTime(createTime);
            return this;
        }
        
        public Builder updateTime(Long updateTime) {
            userBaseInfo.setUpdateTime(updateTime);
            return this;
        }
        
        public Builder regChannel(Integer regChannel) {
            userBaseInfo.setRegChannel(regChannel);
            return this;
        }
        
        public Builder qq(String qq) {
            userBaseInfo.setQq(qq);
            return this;
        }
        
        public Builder level(Integer level) {
            userBaseInfo.setLevel(level);
            return this;
        }
        
        public Builder growScore(Integer growScore) {
            userBaseInfo.setGrowScore(growScore);
            return this;
        }
        
        public Builder referId(Long referId) {
            userBaseInfo.setReferId(referId);
            return this;
        }
        
        public Builder sex(Integer sex) {
            userBaseInfo.setSex(sex);
            return this;
        }
        
        public Builder birthday(Date birthday) {
            userBaseInfo.setBirthday(birthday);
            return this;
        }
        
        public Builder areaCode(String areaCode) {
            userBaseInfo.setAreaCode(areaCode);
            return this;
        }
        
        public Builder address(String address) {
            userBaseInfo.setAddress(address);
            return this;
        }
        
        public UserBaseInfo build() {
            return userBaseInfo;
        }
    }
}
