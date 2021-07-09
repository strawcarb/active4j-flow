<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="col-md-12">
    <div class="form-group">
        <label class="col-sm-3 control-label">软件名称：</label>
        <div class="col-sm-9">
            <input type="text" name="softwareName" minlength="2" class="form-control" placeholder="请输入软件名称" value="${base.name }">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">版本编号：</label>
        <div class="col-sm-9">
            <input type="text" name="version" minlength="2" class="form-control" placeholder="请输入版本号"  value="${biz.version}">
        </div>
    </div>

    <div class="form-group">
        <label class="col-sm-3 control-label">软件描述：</label>
        <div class="col-sm-9">
            <textarea type="text" name="description" class="form-control" placeholder="请输入版本描述">
                ${biz.description}
            </textarea>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">版本文件：</label>
        <%--   当ID存在的时候 ：创建流程成功，文件待下载     --%>

        <div class="col-sm-9">
            <c:if test="${not empty base.id}" >
<%--            <input id="download" type="text" name="file" class="form-control" value="${biz.url}"  >--%>
            <a  href=".${biz.url}" download="${biz.fileName}">${biz.fileName}</a> <br>
            <a  href=".${biz.url}.asc" download="${biz.fileName}.asc">${biz.fileName}.asc</a>
            </c:if>

            <c:if test="${empty base.id}">
                <input id="file"   type="file" name="file" class="form-control" >
            </c:if>
        </div>
    </div>
    <%--    隐藏--%>
   <%-- <div class="form-group"  style="display:none;">
        <label class="col-sm-3 control-label">签名文件：</label>
        <div class="col-sm-9">
            <p class="form-control-static">这里是纯文字信息</p>
        </div>
    </div>--%>


    <div class="form-group"  style="display:none;">
        <label class="col-sm-3 control-label">版本包大小：</label>
        <div class="col-sm-9">
            <p class="form-control-static">版本包大小</p>
        </div>
    </div>

</div>