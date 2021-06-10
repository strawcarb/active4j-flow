<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="col-md-12">
    <div class="form-group">
        <label class="col-sm-3 control-label">软件名称：</label>
        <div class="col-sm-9">
            <%--            <p class="form-control-static" name="softwareName">软件名称</p>--%>
            <input id="name" name="name" minlength="2" type="text" class="form-control" required=""
                   value="${base.name }">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">申请编号：</label>
        <div class="col-sm-5">
            <input id="projectNo" name="projectNo" minlength="2" type="text"
                   class="form-control" required="" value="${base.projectNo }">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">版本编号：</label>
        <div class="col-sm-9">
            <input type="text" name="version" class="form-control" placeholder="软件版本号" value="${biz.version}">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">软件描述：</label>
        <div class="col-sm-9">
            <textarea type="text" name="description" class="form-control" placeholder="软件描述">
                ${biz.description}
            </textarea>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">紧急程度：</label>
        <div class="col-sm-5">
            <c:choose>
                <c:when test="${empty base.level}">
                    <t:dictSelect name="level" type="radio" typeGroupCode="workflowlevel" defaultVal="0"></t:dictSelect>
                </c:when>
                <c:otherwise>
                    <t:dictSelect name="level" type="radio" typeGroupCode="workflowlevel"
                                  defaultVal="${base.level}"></t:dictSelect>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>