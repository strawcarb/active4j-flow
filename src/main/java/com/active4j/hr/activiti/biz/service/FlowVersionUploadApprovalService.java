package com.active4j.hr.activiti.biz.service;

import com.active4j.hr.activiti.biz.entity.FlowProjReleaseApproveEntity;
import com.active4j.hr.activiti.biz.entity.FlowVersionUploadApproveEntity;
import com.active4j.hr.activiti.entity.WorkflowBaseEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FlowVersionUploadApprovalService extends IService<FlowVersionUploadApproveEntity> {

    void saveOrUpdate(WorkflowBaseEntity workflowBaseEntity, FlowVersionUploadApproveEntity entity);
}
