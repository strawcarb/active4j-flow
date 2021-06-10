package com.active4j.hr.activiti.biz.service;

import com.active4j.hr.activiti.biz.entity.FlowProjReleaseApproveEntity;
import com.active4j.hr.activiti.entity.WorkflowBaseEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FlowProjReleaseApprovalService extends IService<FlowProjReleaseApproveEntity> {

    void saveOrUpdate(WorkflowBaseEntity workflowBaseEntity, FlowProjReleaseApproveEntity entity);
}
