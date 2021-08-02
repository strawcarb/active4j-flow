package com.active4j.hr.activiti.biz.service;

import com.active4j.hr.activiti.biz.entity.FlowProjReleaseApproveEntity;
import com.active4j.hr.activiti.biz.entity.FlowVersionUploadApproveEntity;
import com.active4j.hr.activiti.entity.WorkflowBaseEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FlowVersionUploadApprovalService extends IService<FlowVersionUploadApproveEntity> {

    void saveOrUpdate(WorkflowBaseEntity workflowBaseEntity, FlowVersionUploadApproveEntity entity);

    Integer checkVersion(String workflowId, String version);
}
