package com.active4j.hr.activiti.biz.service.impl;

import com.active4j.hr.activiti.biz.dao.FlowVersionUploadApprovalDao;
import com.active4j.hr.activiti.biz.entity.FlowVersionUploadApproveEntity;
import com.active4j.hr.activiti.biz.service.FlowVersionUploadApprovalService;
import com.active4j.hr.activiti.entity.WorkflowBaseEntity;
import com.active4j.hr.activiti.service.WorkflowBaseService;
import com.active4j.hr.core.util.StringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("flowVersionUploadApprovalService")
@Transactional
public class FlowVersionUploadApprovalServiceImpl extends ServiceImpl<FlowVersionUploadApprovalDao, FlowVersionUploadApproveEntity> implements FlowVersionUploadApprovalService {

    @Autowired
    private WorkflowBaseService workflowBaseService;
    @Autowired
    private FlowVersionUploadApprovalDao flowVersionUploadApprovalDao;


    @Override
    public void saveNewVersion(WorkflowBaseEntity workflowBaseEntity, FlowVersionUploadApproveEntity entity) {

        boolean isInsert = StringUtil.isEmpty(entity.getId());
        this.save(entity);
        if (isInsert) {
            workflowBaseEntity.setBusinessId(entity.getId());
        }
        workflowBaseService.save(workflowBaseEntity);
    }

    @Override
    public Boolean saveOrUpdate(WorkflowBaseEntity workflowBaseEntity, FlowVersionUploadApproveEntity entity) {
        if (StringUtils.isNotBlank(workflowBaseEntity.getId()) && StringUtils.isNotBlank(entity.getId())){
            boolean b = this.updateById(entity);
            if (b){
                return workflowBaseService.updateById(workflowBaseEntity);
            }else {
                return Boolean.FALSE;
            }
        }else {
            boolean isInsert = StringUtil.isEmpty(entity.getId());
            if (!this.save(entity)){
                return Boolean.FALSE;
            }
            if ( isInsert) {
                workflowBaseEntity.setBusinessId(entity.getId());
            }
            return workflowBaseService.save(workflowBaseEntity);
        }
    }

    @Override
    public Integer checkVersion(String workflowId, String version) {
        return flowVersionUploadApprovalDao.selectByVersion(version,workflowId);
    }
}
