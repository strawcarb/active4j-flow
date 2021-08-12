package com.active4j.hr.activiti.biz.dao;


import com.active4j.hr.activiti.biz.entity.FlowProjReleaseApproveEntity;
import com.active4j.hr.activiti.biz.entity.FlowVersionUploadApproveEntity;
import com.active4j.hr.activiti.biz.service.FlowVersionUploadApprovalService;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 软件版本上传DAO
 */
public interface FlowVersionUploadApprovalDao extends BaseMapper<FlowVersionUploadApproveEntity> {
    Integer selectByVersion( String version,String workflowId);
}
