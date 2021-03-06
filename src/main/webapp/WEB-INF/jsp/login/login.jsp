<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Version Publish System</title>
    <meta name="keywords" content="Version Publish System">
    <meta name="description" content="Version Publish System">
    <link rel="shortcut icon" href="static/bootstrap/image/favicon.ico"> 
    <link href="static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="static/bootstrap/css/font-awesome.min.css" rel="stylesheet">
    <link href="static/bootstrap/css/animate.min.css" rel="stylesheet">
    <link href="static/bootstrap/css/style.min.css" rel="stylesheet">
    <link href="static/toastr/css/toastr.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html" />
    <![endif]-->
    <script>if(window.top !== window.self){ window.top.location = window.location;}</script>


</head>
<body class="gray-bg">
    <div class="middle-box text-center loginscreen  animated fadeInDown">
        <div>
            <img style="margin-top:50%;width: 150px"  src="static/img/logo.png">
            <form class="form-horizontal m-t" id="signupForm" action="login" method="post">
                <div class="form-group">
                    <input type="text" name="userName" id="userName" value="" class="form-control" placeholder="用户名" required="">
                </div>
                <div class="form-group">
                    <input type="password" name="password" id="password" value="" class="form-control" placeholder="密码" required="">
                </div>
                <div class="form-group">
                    <input id="randCode" name="randCode" type="text" class="form-control" placeholder="验证码" required="" style="width: 60%">
                    <div style="float: right; margin-top: -3.4rem;">
                         <img id="randCodeImage" src="" />
                    </div>
                </div>
                <button type="button" class="btn btn-primary block full-width m-b" onclick="doSubmit()">登 录</button>
                
                </p>
            </form>
        </div>
    </div>
    <script src="static/jquery/js/jquery.min.js"></script>
    <script src="static/bootstrap/js/bootstrap.min.js"></script>
    <script src="static/bootstrap/js/qq.js"></script>
    <script src="static/validate/js/jquery.validate.min.js"></script>
    <script src="static/validate/js/messages_zh.min.js"></script>
    <script src="static/login/js/login.js"></script>
    <script src="static/toastr/js/toastr.min.js"></script>
</body>


</html>

