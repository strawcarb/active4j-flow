package com.active4j.hr.activiti.biz.controller;

import com.active4j.hr.activiti.service.WorkflowBaseService;
import com.active4j.hr.activiti.service.WorkflowService;
import com.active4j.hr.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

public class FlowProjectReleaseController extends BaseController {

    @Autowired
    private WorkflowBaseService workflowBaseService;

    @Autowired
    private WorkflowService workflowService;

}
