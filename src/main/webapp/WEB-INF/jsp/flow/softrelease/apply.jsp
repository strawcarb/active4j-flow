<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/context/mytags.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <t:base type="default,laydate,icheck"></t:base>
</head>
<body class="gray-bg">
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="col-sm-12">
            <div class="ibox float-e-margins">
                <div class="ibox-title">
                    <h5>软件版本发布表单</h5>
                </div>
                <div class="ibox-content">
                    <form class="form-horizontal m-t" id="commonForm" action="flow/biz/softrelease/save" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="workflowId" id="workflowId" value="${workflowId}">
                        <input type="hidden" name="optType" id="optType">
                        <input type="hidden" name="entityId" id="entityId" value="${biz.id}">
                        <input type="hidden" name="id" id="id" value="${base.id}">
                        <%@include file="/WEB-INF/jsp/flow/softrelease/form.jsp" %>
                        <div class="form-group" style="margin-top: 30px;">
                            <div class="col-sm-4 col-sm-offset-3">
                                <button class="btn btn-primary" type="button" onclick="doBtnSaveDraftAction();">保存草稿
                                </button>
                                <button class="btn btn-primary" type="button" onclick="doBtnSaveApplyAction();">发起申请
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">

    //表单验证
    $(function () {
        $("#commonForm").validate({
            submitHandler: function (form) {
                $(form).ajaxSubmit({
                    success: function (o) {
                        if (o.success) {
                            qhTipSuccess('保存成功');
                            location.href = 'common/goSuccess';
                        } else {
                            qhTipWarning(o.msg);
                        }
                    },
                    error: function (data) {
                        qhTipError('系统错误，请联系系统管理员');
                    }
                });
            }
        });
    });


    //保存草稿
    function doBtnSaveDraftAction() {
        $("#optType").val("0");
        $("#commonForm").submit();
    }

    //保存申请
    function doBtnSaveApplyAction() {
        $("#optType").val("1");
        $("#commonForm").submit();
    }

    // function upload() {
    //     // 验证文件内容
    //     var file = $("#file")[0].files[0];
    //     if (!file.name.endWith(".apk")) {
    //         alert("请选择.apk文件");
    //         return;
    //     }
    //     // 上传
    //     doIt()
    // }
    //
    // function doIt() {
    //     var formData = new FormData();
    //     formData.append("file", $("#file")[0].files[0]);
    //     $.ajax({
    //         contentType : "multipart/form-data",
    //         url : "/mote/app/upload.action",
    //         type : "POST",
    //         data : formData,
    //         processData : false, // 告诉jQuery不要去处理发送的数据
    //         contentType : false, // 告诉jQuery不要去设置Content-Type请求头
    //         success : function(data) {
    //             $("#appUrl").val(data); // 保存文件路径
    //         },
    //         xhr : function() {
    //             var xhr = $.ajaxSettings.xhr();
    //             if (xhr.upload) {
    //                 //处理进度条的事件
    //                 xhr.upload.addEventListener("progress", progressHandle,
    //                     false);
    //                 //加载完成的事件
    //                 xhr.addEventListener("load", completeHandle, false);
    //                 //加载出错的事件
    //                 xhr.addEventListener("error", failedHandle, false);
    //                 return xhr;
    //             }
    //         }
    //     });
    // }
    //
    // //进度条更新
    // function progressHandle(e) {
    //     $('.progress-body progress').attr({
    //         value : e.loaded,
    //         max : e.total
    //     });
    //     var percent = e.loaded / e.total * 100;
    //     $('.progress-body percentage').html(percent.toFixed(2) + "%");
    // };
    // //上传完成处理函数
    // function completeHandle(e) {
    //     console.log("上传完成");
    // };
    // //上传出错处理函数
    // function failedHandle(e) {
    //     console.log("上传失败");
    // };
    //
    // String.prototype.endWith = function(endStr) {
    //     var d = this.length - endStr.length;
    //     return (d >= 0 && this.lastIndexOf(endStr) == d)
    // }
    //
    // // 添加内容
    // $("#addBtn").click(function() {
    //     var params = {
    //         version : $("#version").val(),
    //         url : $("#appUrl").val(),
    //         description : $("#description").val()
    //     }
    //
    //     $.ajax({
    //         url : "/mote/app/add.action",
    //         data : JSON.stringify(params),
    //         type : "POST",
    //         contentType : "application/json",
    //         success : function(data) {
    //             if (data == -1)
    //                 alert("该版本已存在")
    //             if (data == 1)
    //                 alert("上传成功")
    //         },
    //         error : function(data) {
    //             alert("服务器繁忙");
    //         }
    //     });
    //
    // });

</script>
</html>

