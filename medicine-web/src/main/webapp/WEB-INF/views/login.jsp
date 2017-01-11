<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2016/12/31 0031
  Time: 下午 9:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>登录</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function(){

            function login(){
                data = {
                    "userName" : $("#userName").val(),
                    "password" : $("#password").val()
                }

                $.ajax({
                    type : "POST",  //提交方式
                    url : "${pageContext.request.contextPath}/med/login",//路径
                    'contentType' : 'application/json',
                    'dataType' : 'json',
                    'data' : JSON.stringify(data),
                    //数据，这里使用的是Json格式进行传输
                    success : function(result) {//返回数据根据结果进行相应的处理
                        if(result.loginFlag=="false"){
                            $("#error_info").html("用户名密码输入错误");
                        }else{
                            $("#error_info").html("");
                            var main_url = "${pageContext.request.contextPath}/med/main?username="+$("#userName").val();
                            window.location.href=main_url;
                        }
                    }
                });
            }

            document.onkeydown = function(e){
                var ev = document.all ? window.event : e;
                if(ev.keyCode==13) {
                    login();
                }
            }

            $("#login_btn").click(function(){
                login();
            });
        });
    </script>
    <style>
        #login_div {
            padding:25px;
            margin-top: 200px;
            background-color: #269abc;
        }

        body{
            background-color: #46b8da;
        }

        .input_width{
            width:200px;
        }
    </style>
</head>

<body>
<div id="login_div" align="center">
    <form class="form-horizontal" role="form" style="align-content: center;width: 500px;">
        <div class="form-group">
            <label for="userName" class="col-sm-2 control-label">用户名</label>
            <div class="col-sm-10">
                <input type="text" id="userName" class="form-control input_width" placeholder="输入用户名...">
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">密码</label>
            <div class="col-sm-10">
                <input type="password" id="password" class="form-control input_width" placeholder="输入密码...">
            </div>
        </div>
        <span id="error_info" class="form-group"></span>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="button" id="login_btn" class="btn btn-default">登录</button>
            </div>
        </div>
    </form>

</div>
</body>
</html>
