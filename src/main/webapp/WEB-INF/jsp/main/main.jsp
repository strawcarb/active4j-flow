<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <t:base type="default"></t:base>
   <script>if(window.top !== window.self){ window.top.location = window.location;}</script>
   <style type="text/css">
       img {
           max-width: 100%;
           height: auto;
       }
   </style>
   <script type="text/javascript">
   
  $(function() {
	  //系统每隔1分钟执行一次
	  //setInterval(fun, 60000);
  });
   
   var fun = function getMsgAction() {
	   $.post("personController.do?getMessage", {}, function(data) {
		   var o = $.parseJSON(data);
		   if(o.success) {
			   var type = o.attributes.type;
			   if('-1' == type) {
				   
			   }else if('0' == type) {
				   var content = o.attributes.content;
				   var id = o.attributes.id;
				   qhTip("<a href='javascript:void(0);' onclick=showMessageAction('" + id + "');>" + content + "</a>");
			   }else if('1' == type) {
				   var content = o.attributes.content;
				   
				   qhTip("<a href='javascript:void(0);' onclick=showMailAction();>" + content + "</a>");
			   }
		   }
	   });
	 
   };
   
    function showMessageAction(id) {
    	var url = "messageController.do?view&id=" + id;
    	parent.layer.open({
    		type : 2,
    		title : '查看',
    		shadeClose : true,
    		shade : 0.8,
    		area : [ '70%', '70%' ],
    		content : url, // iframe的url
    		end: function() {
    		} 
    	});
    }
    
    function showMailAction(id) {
    	var url = "oaInfoMailController.do?list";
    	parent.layer.open({
    		type : 2,
    		title : '查看邮件',
    		shadeClose : true,
    		shade : 0.8,
    		area : [ '90%', '90%' ],
    		content : url, // iframe的url
    		end: function() {
    		} 
    	});
    }
   
   	function goPersonInfo() {
   		$(".profile-element").removeClass("open");
   	}
   	
   	function goDynamics() {
   		$(".profile-element").removeClass("open");
   	}
   	
   	
   	function goHomePage(type) {
   		
   		if(1 == type) {
   			//默认门户
   			$("#content-main iframe").attr("src", "index");
   			$("#content-main iframe").attr("data-id", "index");
   			$("#tabItem").attr("href","index");
   			$("#tabItem").attr("data-index","index");
   		}else if(2 == type){
   			//公司门户
   			
   		}else if(3 == type){
   			//部门门户
   			
   		}else if(4 == type){
   			//个人门户
   			
   		}else if(5 == type){
   			//工作门户
   			
   		}
   		
   	}
   </script>
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">
    <div id="wrapper">
        <!--左侧导航开始-->
        <nav class="navbar-default navbar-static-side" role="navigation">
            <div class="nav-close"><i class="fa fa-times-circle"></i>
            </div>
            <div class="sidebar-collapse">
                <ul class="nav" id="side-menu">
                    <li class="nav-header">
                        <div  >
                            <img style="margin-top:5%;width: 100px"  src="static/img/logo.png">
                        </div>
                    </li>
                    
                     <li>
                        <a id="tabItem" class="J_menuItem" href="console" data-index="console">
                            <i class="fa fa-home"></i>
                            <span class="nav-label">首页</span>
                        </a>
                    </li>
                    
                    <!-- 右侧导航菜单 -->
                    <t:menu></t:menu>

                </ul>
            </div>
        </nav>
        <!--左侧导航结束-->
        <!--右侧部分开始-->
        <div id="page-wrapper" class="gray-bg dashbard-1">
            <div class="row border-bottom">
                <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                    <div class="navbar-header"><a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#"><i class="fa fa-bars"></i> </a>
                    	
                    </div>
                    <ul class="nav navbar-top-links navbar-right">
                    	<li class="dropdown hidden-xs">
                           	 <a data-toggle="dropdown" class="dropdown-toggle text-center" href="#">
                                <i class="fa fa-user"></i>${user.realName }
                            </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
                                <li><a class="J_menuItem" href="sys/user/goinfo" onclick="goPersonInfo();">个人资料</a>
                                </li>
                                <li><a class="J_menuItem" href="sys/user/gopwd" onclick="goPersonInfo();">修改密码</a>
                                </li>
                                <li class="divider"></li>
                                <li class="divider"></li>
                                <li><a href="javascript:;" onclick="exit();">安全退出</a>
                                </li>
                            </ul>
                        </li>
                        <li class="dropdown">
                            <a class="dropdown-toggle count-info J_menuItem6" data-toggle="dropdown" href="oaInfoMailController.do?list" title="收件箱">
                                <i class="fa fa-envelope"></i> <span class="label label-warning">${noReadNo }</span>
                            </a>
                        </li>
                        <li class="dropdown">
                            <a class="dropdown-toggle count-info" data-toggle="dropdown" href="#">
                                <i class="fa fa-bell"></i> <span class="label label-primary">${totalMsgCount }</span>
                            </a>
                            <ul class="dropdown-menu dropdown-alerts">
                                <li>
                                    <a href="messageController.do?list" class="J_menuItem6" title="系统消息">
                                        <div>
                                            <i class="fa fa-envelope fa-fw"></i> 您有${sysMsgCount }条未读消息
                                            <span class="pull-right text-muted small">${msgTime }</span>
                                        </div>
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="dropdown hidden-xs">
                            <a class="right-sidebar-toggle" aria-expanded="false">
                                <i class="fa fa-tasks"></i>门户管理
                            </a>
                        </li>
                        <!-- <li class="hidden-xs">
                           <a href="index_v1.html" class="J_menuItem" data-index="0"><i class="fa fa-cart-arrow-down"></i> 主题</a>
                        </li> -->
                    </ul>
                </nav>
            </div>
            <div class="row content-tabs">
                <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
                </button>
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content">
                        <a href="javascript:;" class="active J_menuTab" data-id="console">首页</a>
                    </div>
                </nav>
                <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
                </button>
                <div class="btn-group roll-nav roll-right">
                    <button class="dropdown J_tabClose" data-toggle="dropdown">关闭操作<span class="caret"></span>

                    </button>
                    <ul role="menu" class="dropdown-menu dropdown-menu-right">
                        <li class="J_tabShowActive"><a>定位当前选项卡</a>
                        </li>
                        <li class="divider"></li>
                        <li class="J_tabCloseAll"><a>关闭全部选项卡</a>
                        </li>
                        <li class="J_tabCloseOther"><a>关闭其他选项卡</a>
                        </li>
                    </ul>
                </div>
                <a href="javascript:;" class="roll-nav roll-right J_tabExit" onclick="exit();"><i class="fa fa fa-sign-out"></i> 退出</a>
            </div>
            <div class="row J_mainContent" id="content-main">
                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="http://www.easier.cn/" frameborder="0" data-id="console" seamless></iframe>
            </div>
            <div class="footer">

            </div>
        </div>
        <!--右侧部分结束-->
        <!--右侧边栏开始-->
        <div id="right-sidebar" style="height: 40%">
            <div class="sidebar-container">
                <div class="tab-content">
                    <div class="setings-item" style="text-align: center;">
                        <a href="javascript:void(0);" style="color: black;" onclick="goHomePage(1)">
                       		<span>默认门户</span>
                   		</a>
                    </div>
                    <div class="setings-item" style="text-align: center;">
                        <a href="javascript:void(0);" style="color: black;" onclick="goHomePage(2)">
                       		<span>公司门户</span>
                   		</a>
                    </div>
                    <div class="setings-item" style="text-align: center;">
                        <a href="javascript:void(0);" style="color: black;" onclick="goHomePage(3)">
                       		<span>部门门户</span>
                   		</a>
                    </div>
                    <div class="setings-item" style="text-align: center;">
                        <a href="javascript:void(0);" style="color: black;" onclick="goHomePage(4)">
                       		<span>个人门户</span>
                   		</a>
                    </div>
                    <div class="setings-item" style="text-align: center;">
                        <a href="javascript:void(0);" style="color: black;" onclick="goHomePage(5)">
                       		<span>工作门户</span>
                   		</a>
                    </div>
                </div>
            </div>
        </div>
        <!--右侧边栏结束-->
    </div>
    
</body>


</html>

