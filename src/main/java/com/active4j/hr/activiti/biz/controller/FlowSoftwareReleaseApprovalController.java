package com.active4j.hr.activiti.biz.controller;

import com.active4j.hr.activiti.biz.entity.FlowCostApprovalEntity;
import com.active4j.hr.activiti.biz.entity.FlowProjReleaseApproveEntity;
import com.active4j.hr.activiti.biz.service.FlowProjReleaseApprovalService;
import com.active4j.hr.activiti.entity.WorkflowBaseEntity;
import com.active4j.hr.activiti.entity.WorkflowMngEntity;
import com.active4j.hr.activiti.service.WorkflowBaseService;
import com.active4j.hr.activiti.service.WorkflowMngService;
import com.active4j.hr.activiti.service.WorkflowService;
import com.active4j.hr.base.controller.BaseController;
import com.active4j.hr.common.constant.GlobalConstant;
import com.active4j.hr.core.beanutil.MyBeanUtils;
import com.active4j.hr.core.model.AjaxJson;
import com.active4j.hr.core.shiro.ShiroUtils;
import com.active4j.hr.core.util.DateUtils;
import com.active4j.hr.system.util.SystemUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("flow/biz/projrelease")
public class FlowSoftwareReleaseApprovalController extends BaseController {

    @Autowired
    private WorkflowBaseService workflowBaseService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private WorkflowMngService workflowMngService;

    @Autowired
    private FlowProjReleaseApprovalService flowProjReleaseApprovalService;

    @RequestMapping("/go")
    public ModelAndView go(String formId, String type, String workflowId, String id, HttpServletRequest request) {
        ModelAndView view;

        if (StringUtils.isEmpty(formId)) {
            view = new ModelAndView("system/common/warning");
            view.addObject("message", "该流程没有配置相应表单");
            return view;
        }

        /**
         * 根据type值，绝对跳转到哪个页面，主要有两种页面
         * 0：新增，编辑页面
         * 1：审批时显示详情页面
         * 2: 直接办理流程
         * 3：审批时显示详情页面，并附带审批功能
         */
        if (StringUtils.equals("0", type)) {
            view = new ModelAndView("flow/projrelease/apply");
        } else if (StringUtils.equals("1", type)) {
            view = new ModelAndView("flow/projrelease/applyshow");

            //根据businessKey查询任务list
            String currentName = ShiroUtils.getSessionUserName();

            //查看历史审批意见
            List<Comment> lstComments = workflowService.findCommentsListByBusinessKey(id);
            view.addObject("lstComments", lstComments);
            view.addObject("currentName", currentName);
            view.addObject("show", "0");

        } else if (StringUtils.equals("2", type)) {
            view = new ModelAndView("flow/include/approve");

            //根据businessKey查询任务list
            String currentName = ShiroUtils.getSessionUserName();
            List<Task> lstTasks = workflowService.findTaskListByBusinessKey(id, currentName);
            view.addObject("lstTasks", lstTasks);
            view.addObject("action", "flow/biz/projrelease/doApprove");
        } else if (StringUtils.equals("3", type)) {
            view = new ModelAndView("flow/projrelease/applyshow");

            //根据businessKey查询任务list
            String currentName = ShiroUtils.getSessionUserName();
            List<Task> lstTasks = workflowService.findTaskListByBusinessKey(id, currentName);
            view.addObject("lstTasks", lstTasks);

            //查看历史审批意见
            List<Comment> lstComments = workflowService.findCommentsListByBusinessKey(id);
            view.addObject("lstComments", lstComments);
            view.addObject("currentName", currentName);
            view.addObject("show", "1");
            view.addObject("action", "flow/biz/signetapproval/doApprove");
        } else {
            view = new ModelAndView("flow/projrelease/apply");
        }
        if(StringUtils.isNotEmpty(id)) {
            WorkflowBaseEntity base = workflowBaseService.getById(id);
            view.addObject("base", base);

            FlowProjReleaseApproveEntity biz = flowProjReleaseApprovalService.getById(base.getBusinessId());
            view.addObject("biz", biz);
        }
        view.addObject("workflowId", workflowId);
        return view;
    }

    @RequestMapping("/save")
    @ResponseBody
    public AjaxJson save(WorkflowBaseEntity workflowBaseEntity, FlowProjReleaseApproveEntity data, String optType, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        if (!workflowBaseService.validWorkflowBase(workflowBaseEntity, j).isSuccess()) {
            return j;
        }
        // TODO 检查版本号是否重复
        WorkflowMngEntity workflow = workflowMngService.getById(workflowBaseEntity.getWorkflowId());
        if (null == workflow) {
            j.setSuccess(false);
            j.setMsg("参数错误，系统中没有该流程");
            return j;
        }
        Date current = new Date();
        String operator = ShiroUtils.getSessionUserName();

        if (StringUtils.equals(optType, "1")) {
            //直接申请流程
            if (StringUtils.isBlank(workflowBaseEntity.getId())) {
                workflowBaseEntity.setApplyDate(current);
                workflowBaseEntity.setApplyName(ShiroUtils.getSessionUser().getRealName());
                workflowBaseEntity.setUserName(operator);
                workflowBaseEntity.setCategoryId(workflow.getCategoryId());
                workflowBaseEntity.setWorkflowId(workflow.getId());
                workflowBaseEntity.setWorkFlowName(workflow.getName());
                workflowBaseEntity.setStatus("1"); //草稿状态 0：草稿 1： 已申请  2： 审批中 3： 已完成 4： 已归档
                //保存业务数据
                flowProjReleaseApprovalService.saveOrUpdate(workflowBaseEntity, data);

                //启动流程
                //赋值流程变量
                Map<String, Object> variables = new HashMap<String, Object>();
                workflowService.startProcessInstanceByKey(workflow.getProcessKey(),
                        workflowBaseEntity.getId(),
                        true,
                        workflowBaseEntity.getUserName(),
                        variables);
            } else {
                try {
                    WorkflowBaseEntity base = workflowBaseService.getById(workflowBaseEntity.getId());
                    MyBeanUtils.copyBeanNotNull2Bean(workflowBaseEntity, base);
                    FlowProjReleaseApproveEntity biz = flowProjReleaseApprovalService.getById(workflowBaseEntity.getBusinessId());
                    MyBeanUtils.copyBeanNotNull2Bean(data, biz);
                    //已申请
                    base.setStatus("1");
                    base.setUpdateDate(current);
                    base.setUpdateName(operator);
                    biz.setUpdateDate(current);
                    biz.setUpdateName(ShiroUtils.getSessionUserName());
                    flowProjReleaseApprovalService.saveOrUpdate(base, biz);

                    //启动流程
                    //启动流程
                    Map<String, Object> variables = new HashMap<>();
                    workflowService.startProcessInstanceByKey(workflow.getProcessKey(),
                            biz.getId(),
                            false,
                            base.getUserName(),
                            variables);
                } catch (Exception e) {
                    log.error("");
                }
            }

        } else {
            //保存草稿
            //新增
            if (StringUtils.isEmpty(workflowBaseEntity.getId())) {
                workflowBaseEntity.setApplyDate(DateUtils.getDate());
                workflowBaseEntity.setApplyName(ShiroUtils.getSessionUser().getRealName());
                workflowBaseEntity.setUserName(ShiroUtils.getSessionUserName());
                workflowBaseEntity.setCategoryId(workflow.getCategoryId());
                workflowBaseEntity.setWorkflowId(workflow.getId());
                workflowBaseEntity.setWorkFlowName(workflow.getName());
                workflowBaseEntity.setStatus("0"); //草稿状态 0：草稿 1： 已申请  2： 审批中 3： 已完成 4： 已归档
                flowProjReleaseApprovalService.saveOrUpdate(workflowBaseEntity, data);
            } else {
                try {
                    WorkflowBaseEntity base = workflowBaseService.getById(workflowBaseEntity.getId());
                    MyBeanUtils.copyBeanNotNull2Bean(workflowBaseEntity, base);
                    FlowProjReleaseApproveEntity biz = flowProjReleaseApprovalService.getById(workflowBaseEntity.getBusinessId());
                    MyBeanUtils.copyBeanNotNull2Bean(data, biz);
                    //已申请
                    base.setStatus("1");
                    base.setUpdateDate(current);
                    base.setUpdateName(operator);
                    biz.setUpdateDate(current);
                    biz.setUpdateName(ShiroUtils.getSessionUserName());
                    flowProjReleaseApprovalService.saveOrUpdate(base, biz);
                } catch (Exception e) {
                    log.error("");
                }
            }
        }
        return j;
    }

    @RequestMapping("/del")
    @ResponseBody
    public AjaxJson del(String businessId, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isNotBlank(businessId)) {
                flowProjReleaseApprovalService.removeById(businessId);
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("删除失败，错误信息:" + e.getMessage());
            log.error("删除流程业务表失败", e);
        }

        return j;
    }

    /**
     * 流程审批
     *
     * @param id
     * @param taskId
     * @param comment
     * @param request
     * @return
     */
    @RequestMapping("/doApprove")
    @ResponseBody
    public AjaxJson doApprove(String id, String taskId, String comment, String result, HttpServletRequest request){
        AjaxJson j = new AjaxJson();

        try {
            if (StringUtils.isEmpty(comment)) {
                j.setMsg("审批意见不能为空");
                j.setSuccess(false);
                return j;
            }
            if (StringUtils.isEmpty(taskId)) {
                j.setMsg("任务不能为空");
                j.setSuccess(false);
                return j;
            }
            Map<String, Object> variables = new HashMap<String, Object>();
            if(StringUtils.equals("N", result)) {
                variables.put("flag", "N");
                workflowService.saveBackTask(taskId, id, comment, variables);
            }else if(StringUtils.equals("Y", result)){
                variables.put("flag", "Y");
                workflowService.saveSubmitTask(taskId, id, comment, variables);
            }else {
                workflowService.saveSubmitTask(taskId, id, comment, variables);
            }
//            workflowService.saveSubmitTask(taskId, id, comment, variables);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(GlobalConstant.ERROR_MSG);
            log.error("流程审批失败", e);
        }

        return j;
    }
}
