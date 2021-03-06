package com.active4j.hr.activiti.biz.controller;

import com.active4j.hr.activiti.biz.entity.FlowContractApprovalEntity;
import com.active4j.hr.activiti.biz.entity.FlowVersionUploadApproveEntity;
import com.active4j.hr.activiti.biz.service.FlowVersionUploadApprovalService;
import com.active4j.hr.activiti.entity.WorkflowBaseEntity;
import com.active4j.hr.activiti.entity.WorkflowMngEntity;
import com.active4j.hr.activiti.service.WorkflowBaseService;
import com.active4j.hr.activiti.service.WorkflowMngService;
import com.active4j.hr.activiti.service.WorkflowService;
import com.active4j.hr.activiti.util.AddSigunature;
import com.active4j.hr.base.controller.BaseController;
import com.active4j.hr.common.constant.GlobalConstant;
import com.active4j.hr.core.beanutil.MyBeanUtils;
import com.active4j.hr.core.model.AjaxJson;
import com.active4j.hr.core.shiro.ShiroUtils;
import com.active4j.hr.core.util.DateUtils;
import com.active4j.hr.system.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.security.Security;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("flow/biz/softrelease")
public class FlowVersionUploadApprovalController extends BaseController {

    @Autowired
    private Environment environment;

    @Autowired
    private WorkflowBaseService workflowBaseService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private WorkflowMngService workflowMngService;

    @Autowired
    private FlowVersionUploadApprovalService flowVersionUploadApprovalService;

    @Autowired
    private AddSigunature addSigunature;

    @RequestMapping("/go")
    public ModelAndView go(String formId, String type, String workflowId, String id, HttpServletRequest request) {
        ModelAndView view = new ModelAndView("flow/softrelease/apply");

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
            view = new ModelAndView("flow/softrelease/apply");
        } else if (StringUtils.equals("1", type)) {
            view = new ModelAndView("flow/softrelease/applyshow");

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
            view.addObject("action", "flow/biz/softrelease/doApprove");
        } else if (StringUtils.equals("3", type)) {
            view = new ModelAndView("flow/softrelease/applyshow");

            //??????businessKey????????????list
            String currentName = ShiroUtils.getSessionUserName();
            List<Task> lstTasks = workflowService.findTaskListByBusinessKey(id, currentName);
            view.addObject("lstTasks", lstTasks);

            //????????????????????????
            List<Comment> lstComments = workflowService.findCommentsListByBusinessKey(id);
            view.addObject("lstComments", lstComments);
            view.addObject("currentName", currentName);
            view.addObject("show", "1");
            view.addObject("action", "flow/biz/softrelease/doApprove");
        } else {
            view = new ModelAndView("flow/softrelease/apply");
        }


        if(StringUtils.isNotEmpty(id)) {
            WorkflowBaseEntity base = workflowBaseService.getById(id);
            view.addObject("base", base);

            FlowVersionUploadApproveEntity biz = flowVersionUploadApprovalService.getById(base.getBusinessId());
            view.addObject("biz", biz);
        }
        view.addObject("workflowId", workflowId);
        return view;
    }

    /**
     *
     * @param workflowBaseEntity
     * @param data
     * @param optType ???????????? 0 ??? ???????????? 1
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/save")
    @ResponseBody
    public AjaxJson save(WorkflowBaseEntity workflowBaseEntity, FlowVersionUploadApproveEntity data,  String optType, HttpServletRequest request) throws IOException {
        AjaxJson j = new AjaxJson();
        data.setId(data.getEntityId());
        WorkflowBaseEntity baseEntity  = workflowBaseService.getById(workflowBaseEntity.getId());
        if (!ObjectUtils.isEmpty(baseEntity) && (optType.equals("0") || baseEntity.getStatus().equals("5"))){

            workflowBaseEntity.setWorkflowId(baseEntity.getWorkflowId());
        }

        workflowBaseEntity.setName(data.getSoftwareName());
        workflowBaseEntity.setProjectNo(UUID.randomUUID().toString().substring(13).replaceAll("-",""));
//        workflowBaseEntity.setLevel("1");
        if (!workflowBaseService.validWorkflowBase(workflowBaseEntity, j).isSuccess()) {
            j.setMsg("????????????????????????");
            return j;
        }

        WorkflowMngEntity workflow = workflowMngService.getById(workflowBaseEntity.getWorkflowId());

        if (null == workflow) {
            j.setSuccess(false);
            j.setMsg("???????????????????????????????????????");
            return j;

        } else if(ObjectUtils.isEmpty(data.getId()) && workflow.getVersions() == data.getVersions()) {
            j.setSuccess(false);
            j.setMsg("???????????????,??????????????????");
            return j;

        }


        Integer count = flowVersionUploadApprovalService.checkVersion(workflowBaseEntity.getWorkflowId(),data.getVersion());
        String status = "1";
        if (!ObjectUtils.isEmpty(baseEntity)){
            status = baseEntity.getStatus();
        }
        //???????????????????????????,?????????????????????????????????????????????????????????????????????
        //???????????????????????????,?????????????????????????????????????????????????????????????????????
        //???????????? 0????????? 1??? ????????? 2??? ????????? 3??? ????????? 4??? ????????? 5?????????
        if (count>0 && optType.equals("1") ) {
            j.setSuccess(false);
            j.setMsg("???????????????,??????????????????");
            return j;
        }
        if (!ObjectUtils.isEmpty(data.getFile())){
            String  fileName = data.getFile().getOriginalFilename();

            String filePath = environment.getProperty("upload.file.path");

            String[] split = fileName.split("\\.", fileName.lastIndexOf("."));

            log.info("?????????????????????{}",filePath);
            String[] filename = fileName.split("\\.");

            String suffix = "";
            for (int i = 0; i < filename.length - 1; i++) {
                suffix += filename[i];
                if (i <  filename.length -2 ){
                    suffix += ".";
                }
            }
            String prefix =filename[filename.length -1];
            if(suffix.length() < 3){
                j.setSuccess(false);
                j.setMsg("???????????????,???????????????????????????");
                return j;
            }
            File file = File.createTempFile(suffix,"."+prefix,new File(filePath));
            FileUtils.copyInputStreamToFile(data.getFile().getInputStream(),file);

//            file.deleteOnExit();
            data.setHashCode(String.valueOf(workflowBaseEntity.getProjectNo().hashCode()));
            data.setUrl("/uploadFile/"+file.getName());
            data.setFileName(file.getName());
            addSigunature.addSigunature(filePath +"/"+ file.getName());
        }
        Date current = new Date();
        String operator = ShiroUtils.getSessionUserName();
//        SysUserEntity principal = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
//        if (!ObjectUtils.isEmpty(principal)) {
//            data.setUpdateName(principal.getUserName());
//        }
        data.setUpdateName(operator);
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
                flowVersionUploadApprovalService.saveNewVersion(workflowBaseEntity, data);

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

                    FlowVersionUploadApproveEntity biz = flowVersionUploadApprovalService.getById(base.getBusinessId());
                    MyBeanUtils.copyBeanNotNull2Bean(data, biz);
                    //?????????
                    base.setStatus("1");
                    base.setUpdateDate(current);
                    base.setUpdateName(operator);
                    biz.setUpdateDate(current);
                    biz.setUpdateName(ShiroUtils.getSessionUserName());
                    flowVersionUploadApprovalService.saveOrUpdate(base, biz);

                    //????????????
                    //????????????
                    Map<String, Object> variables = new HashMap<>();
                    workflowService.startProcessInstanceByKey(workflow.getProcessKey(),
                            biz.getId(),
                            true,
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
                flowVersionUploadApprovalService.saveNewVersion(workflowBaseEntity, data);
            } else {
                try {
                    WorkflowBaseEntity base = workflowBaseService.getById(workflowBaseEntity.getId());
                    MyBeanUtils.copyBeanNotNull2Bean(workflowBaseEntity, base);
                    FlowVersionUploadApproveEntity biz = flowVersionUploadApprovalService.getById(base.getBusinessId());
                    MyBeanUtils.copyBeanNotNull2Bean(data, biz);
                    //?????????
                    base.setStatus("0");
                    base.setUpdateDate(current);
                    base.setUpdateName(operator);
                    biz.setUpdateDate(current);
                    biz.setUpdateName(ShiroUtils.getSessionUserName());
                    flowVersionUploadApprovalService.saveOrUpdate(base, biz);
                } catch (Exception e) {
                    j.setSuccess(false);
                    log.error("update or save failed{}",e.getMessage());
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
                flowVersionUploadApprovalService.removeById(businessId);
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
