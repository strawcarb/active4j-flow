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
            view.addObject("message", "?????????????????????????????????");
            return view;
        }

        /**
         * ??????type?????????????????????????????????????????????????????????
         * 0????????????????????????
         * 1??????????????????????????????
         * 2: ??????????????????
         * 3??????????????????????????????????????????????????????
         */
        if (StringUtils.equals("0", type)) {
            view = new ModelAndView("flow/projrelease/apply");
        } else if (StringUtils.equals("1", type)) {
            view = new ModelAndView("flow/projrelease/applyshow");

            //??????businessKey????????????list
            String currentName = ShiroUtils.getSessionUserName();

            //????????????????????????
            List<Comment> lstComments = workflowService.findCommentsListByBusinessKey(id);
            view.addObject("lstComments", lstComments);
            view.addObject("currentName", currentName);
            view.addObject("show", "0");

        } else if (StringUtils.equals("2", type)) {
            view = new ModelAndView("flow/include/approve");

            //??????businessKey????????????list
            String currentName = ShiroUtils.getSessionUserName();
            List<Task> lstTasks = workflowService.findTaskListByBusinessKey(id, currentName);
            view.addObject("lstTasks", lstTasks);
            view.addObject("action", "flow/biz/projrelease/doApprove");
        } else if (StringUtils.equals("3", type)) {
            view = new ModelAndView("flow/projrelease/applyshow");

            //??????businessKey????????????list
            String currentName = ShiroUtils.getSessionUserName();
            List<Task> lstTasks = workflowService.findTaskListByBusinessKey(id, currentName);
            view.addObject("lstTasks", lstTasks);

            //????????????????????????
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
        // TODO ???????????????????????????
        WorkflowMngEntity workflow = workflowMngService.getById(workflowBaseEntity.getWorkflowId());
        if (null == workflow) {
            j.setSuccess(false);
            j.setMsg("???????????????????????????????????????");
            return j;
        }
        Date current = new Date();
        String operator = ShiroUtils.getSessionUserName();

        if (StringUtils.equals(optType, "1")) {
            //??????????????????
            if (StringUtils.isBlank(workflowBaseEntity.getId())) {
                workflowBaseEntity.setApplyDate(current);
                workflowBaseEntity.setApplyName(ShiroUtils.getSessionUser().getRealName());
                workflowBaseEntity.setUserName(operator);
                workflowBaseEntity.setCategoryId(workflow.getCategoryId());
                workflowBaseEntity.setWorkflowId(workflow.getId());
                workflowBaseEntity.setWorkFlowName(workflow.getName());
                workflowBaseEntity.setStatus("1"); //???????????? 0????????? 1??? ?????????  2??? ????????? 3??? ????????? 4??? ?????????
                //??????????????????
                flowProjReleaseApprovalService.saveOrUpdate(workflowBaseEntity, data);

                //????????????
                //??????????????????
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
                    //?????????
                    base.setStatus("1");
                    base.setUpdateDate(current);
                    base.setUpdateName(operator);
                    biz.setUpdateDate(current);
                    biz.setUpdateName(ShiroUtils.getSessionUserName());
                    flowProjReleaseApprovalService.saveOrUpdate(base, biz);

                    //????????????
                    //????????????
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
            //????????????
            //??????
            if (StringUtils.isEmpty(workflowBaseEntity.getId())) {
                workflowBaseEntity.setApplyDate(DateUtils.getDate());
                workflowBaseEntity.setApplyName(ShiroUtils.getSessionUser().getRealName());
                workflowBaseEntity.setUserName(ShiroUtils.getSessionUserName());
                workflowBaseEntity.setCategoryId(workflow.getCategoryId());
                workflowBaseEntity.setWorkflowId(workflow.getId());
                workflowBaseEntity.setWorkFlowName(workflow.getName());
                workflowBaseEntity.setStatus("0"); //???????????? 0????????? 1??? ?????????  2??? ????????? 3??? ????????? 4??? ?????????
                flowProjReleaseApprovalService.saveOrUpdate(workflowBaseEntity, data);
            } else {
                try {
                    WorkflowBaseEntity base = workflowBaseService.getById(workflowBaseEntity.getId());
                    MyBeanUtils.copyBeanNotNull2Bean(workflowBaseEntity, base);
                    FlowProjReleaseApproveEntity biz = flowProjReleaseApprovalService.getById(workflowBaseEntity.getBusinessId());
                    MyBeanUtils.copyBeanNotNull2Bean(data, biz);
                    //?????????
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
            j.setMsg("???????????????????????????:" + e.getMessage());
            log.error("???????????????????????????", e);
        }

        return j;
    }

    /**
     * ????????????
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
                j.setMsg("????????????????????????");
                j.setSuccess(false);
                return j;
            }
            if (StringUtils.isEmpty(taskId)) {
                j.setMsg("??????????????????");
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
            log.error("??????????????????", e);
        }

        return j;
    }
}
