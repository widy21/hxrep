<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>第三十七大药房</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function(){
            $('#myTab a').click(function (e) {
                //e.preventDefault()
                //$(this).tab('show')
            })
        });
    </script>
    <style>
        #menu_div {
            padding:25px;
        //margin-top: 200px;
        //background-color: #269abc;
        }

        body{
        //background-color: #46b8da;
        }
    </style>
</head>

<body>
<div id="menu_div" align="center">
    <ul id="myTab">
        <li><a  href="${pageContext.request.contextPath}/med/good_register" ><h3>来货登记</h3></a></li>
        <li><a  href="${pageContext.request.contextPath}/med/show_order_checkout" ><h3>订单日结</h3></a></li>
        <li><a  href="${pageContext.request.contextPath}/med/show_order_msg" ><h3>订单信息查询</h3></a></li>
        <li><a  href="${pageContext.request.contextPath}/med/show_drug_msg" ><h3>药品信息查询</h3></a></li>
    </ul>
</div>
</body>
</html>
