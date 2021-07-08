<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="col-md-12">
    <div class="form-group">
        <label class="col-sm-3 control-label">软件名称：</label>
        <div class="col-sm-9">
            <input type="text" name="softwareName" class="form-control" placeholder="请输入软件名称" value="${base.softwareName }">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">版本编号：</label>
        <div class="col-sm-9">
            <input type="text" name="version" class="form-control" placeholder="请输入版本号"  value="${base.version}">
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-3 control-label">软件描述：</label>
        <div class="col-sm-9">
            <textarea type="text" name="description" class="form-control" placeholder="请输入版本描述">
                ${base.description}
            </textarea>
        </div>
    </div>
    <%--    隐藏--%>
   <%-- <div class="form-group"  style="display:none;">
        <label class="col-sm-3 control-label">签名文件：</label>
        <div class="col-sm-9">
            <p class="form-control-static">这里是纯文字信息</p>
        </div>
    </div>--%>
    <div class="form-group">
        <label class="col-sm-3 control-label">版本文件：</label>
        <div class="col-sm-9">
         <input id="file"   type="file" name="file" class="form-control">--%>

        </div>
    </div>

    <div class="form-group"  style="display:none;">
        <label class="col-sm-3 control-label">版本包大小：</label>
        <div class="col-sm-9">
            <p class="form-control-static">版本包大小</p>
        </div>
    </div>

</div>