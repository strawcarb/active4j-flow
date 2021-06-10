package com.active4j.hr.activiti.biz.entity;

import com.active4j.hr.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * 版本发布流程审批实体对象
 * <p>
 * 软件信息应该包含以下基本信息:
 * 软件名称（默认，创建应用指定）、
 * 版本号（唯一性）、
 * 版本描述、时间（自动）、
 * 哈希值（sha1自动）、
 * 签名结果（自动）、
 * 以及版本包（支持上传多个）、
 * 版本包大小（自动）
 * 附属附件：如测试报告等文件，支持多附属附件上传，每个文件可单独设置权限，默认外部成员不可见，作为审批参考数据。
 */
@Getter
@Setter
@TableName("FLOW_PROJ_RELEASE_APPROVE")
public class FlowProjReleaseApproveEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -3521617027079496837L;

    /**
     * 软件名称
     */
    @TableField("SOFTWARE_NAME")
    private String softwareName;

    /**
     * 软件版本
     */
    @TableField("VERSION")
    private String version;

    /**
     * 描述
     */
    @TableField("DESCRIPTION")
    private String description;

    /**
     * 版本下载地址
     */
    @TableField("URL")
    private String url;

    /**
     * 软件包的Hash值
     */
    @TableField("HASH_CODE")
    private String hashCode;

    /**
     * 软件发布时间
     */
    @TableField("RELEASE_TIME")
    private Date releaseTime;

}
