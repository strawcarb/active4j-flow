<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <t:base type="default,jqgrid,datetimePicker,laydate"></t:base>
</head>

<body class="gray-bg">
<!-- 页面部分 -->
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="row">
                <div class="col-sm-12" id="searchGroupId">
                </div>
            </div>
            <div class="ibox">
                <div class="ibox-content">
                    <div id="finishWorkFlow" class="jqGrid_wrapper"></div>
                </div>
            </div>
        </div>
    </div>
</div>


</div>
<!-- 脚本部分 -->
<t:datagrid actionUrl="flow/biz/task/finishedWorkflow" tableContentId="finishWorkFlow" searchGroupId="searchGroupId"
            fit="true" caption="已完结流程" name="finishworkflowlist" pageSize="20" sortName="applyDate" sortOrder="desc">
    <t:dgCol name="id" label="编号" hidden="true" key="true" width="20"></t:dgCol>
    <t:dgCol name="projectNo" label="编号" width="120" query="true"></t:dgCol>
    <t:dgCol name="name" label="软件名称" width="120" query="true"></t:dgCol>
    <t:dgCol name="version" label="版本编号" width="120" query="true" ></t:dgCol>
    <t:dgCol name="status" label="状态" width="70" dictionary="actstatus" classes="text-navy"></t:dgCol>
    <t:dgCol name="applyDate" label="申请时间" width="120" query="true" datefmt="yyyy-MM-dd HH:mm:ss" queryModel="group" datePlugin="laydate"></t:dgCol>
    <t:dgToolBar label="查看详情" icon="fa fa-eye" url="flow/biz/task/view" type="read" width="90%"  height="90%"></t:dgToolBar>
<%--    <t:dgToolBar label="查看流程图" icon="fa fa-binoculars" url="flow/biz/my/viewImage" type="read" width="80%"  height="80%"></t:dgToolBar>--%>
    <t:dgToolBar type="refresh"></t:dgToolBar>
</t:datagrid>
<script type="text/javascript">
    $(function(){
        getVersion()
        laydate({elem:"#applyDate_begin",event:"focus",istime: true, format: 'YYYY-MM-DD hh:mm:ss'});
        laydate({elem:"#applyDate_end",event:"focus",istime: true, format: 'YYYY-MM-DD hh:mm:ss'});
    });
    function getVersion() {
        $.post("flow/biz/task/getVersion", {}, function(data) {
            var noDataDom = '<label class="col-sm-1 control-label">版本编号</label><div class="col-sm-2 m-b"><select name="version" class="form-control" id="version"><option></option></select></div>';
            var searchObj = $(".form-horizontal .form-group ").children('div').eq(0);
            $(".form-horizontal .form-group ").children('div').eq(0).children('label').eq(2).remove()
            $(".form-horizontal .form-group ").children('div').eq(0).children('div').eq(2).remove()
            if(data.length!=0) {
                if(!searchObj) return
                var versionOptionDom = '<label class="col-sm-1 control-label">版本编号</label><div class="col-sm-2 m-b"><select name="version" class="form-control" id="version"><option></option>';
                for (var i = 0; i < data.length; i++) {
                    versionOptionDom+='<option value="'+data[i].id+'">' + data[i].version + '</option>';
                }
                versionOptionDom +='</select></div>';
                searchObj.prepend(versionOptionDom)
            } else {
                searchObj.prepend(noDataDom)
            }
        });
    }

</script>
</body>


</html>