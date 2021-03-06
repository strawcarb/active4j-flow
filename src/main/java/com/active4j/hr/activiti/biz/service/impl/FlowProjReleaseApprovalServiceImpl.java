package com.active4j.hr.activiti.biz.service.impl;

import com.active4j.hr.activiti.biz.dao.FlowProjReleaseApprovalDao;
import com.active4j.hr.activiti.biz.entity.FlowProjReleaseApproveEntity;
import com.active4j.hr.activiti.biz.service.FlowProjReleaseApprovalService;
import com.active4j.hr.activiti.entity.WorkflowBaseEntity;
import com.active4j.hr.activiti.service.WorkflowBaseService;
import com.active4j.hr.core.util.StringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("flowProjReleaseApprovalService")
@Transactional
public class FlowProjReleaseApprovalServiceImpl extends ServiceImpl<FlowProjReleaseApprovalDao, FlowProjReleaseApproveEntity> implements FlowProjReleaseApprovalService {

    @Autowired
    private WorkflowBaseService workflowBaseService;

    @Override
    public void saveOrUpdate(WorkflowBaseEntity workflowBaseEntity, FlowProjReleaseApproveEntity entity) {
        boolean isInsert = StringUtil.isEmpty(entity.getId());
        this.saveOrUpdate(entity);
        if (isInsert) {
            workflowBaseEntity.setBusinessId(entity.getId());
        }
        workflowBaseService.saveOrUpdate(workflowBaseEntity);
    }
}
