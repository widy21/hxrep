<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title></title>
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <style>
        #main-nav {
            margin-left: 1px;
        }

        #main-nav.nav-tabs.nav-stacked > li > a {
            padding: 10px 8px;
            font-size: 12px;
            font-weight: 600;
            color: #4A515B;
            background: #E9E9E9;
            background: -moz-linear-gradient(top, #FAFAFA 0%, #E9E9E9 100%);
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#FAFAFA), color-stop(100%,#E9E9E9));
            background: -webkit-linear-gradient(top, #FAFAFA 0%,#E9E9E9 100%);
            background: -o-linear-gradient(top, #FAFAFA 0%,#E9E9E9 100%);
            background: -ms-linear-gradient(top, #FAFAFA 0%,#E9E9E9 100%);
            background: linear-gradient(top, #FAFAFA 0%,#E9E9E9 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#FAFAFA', endColorstr='#E9E9E9');
            -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr='#FAFAFA', endColorstr='#E9E9E9')";
            border: 1px solid #D5D5D5;
            border-radius: 4px;
        }

        #main-nav.nav-tabs.nav-stacked > li > a > span {
            color: #4A515B;
        }

        #main-nav.nav-tabs.nav-stacked > li.active > a, #main-nav.nav-tabs.nav-stacked > li > a:hover {
            color: #FFF;
            background: #3C4049;
            background: -moz-linear-gradient(top, #4A515B 0%, #3C4049 100%);
            background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#4A515B), color-stop(100%,#3C4049));
            background: -webkit-linear-gradient(top, #4A515B 0%,#3C4049 100%);
            background: -o-linear-gradient(top, #4A515B 0%,#3C4049 100%);
            background: -ms-linear-gradient(top, #4A515B 0%,#3C4049 100%);
            background: linear-gradient(top, #4A515B 0%,#3C4049 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#4A515B', endColorstr='#3C4049');
            -ms-filter: "progid:DXImageTransform.Microsoft.gradient(startColorstr='#4A515B', endColorstr='#3C4049')";
            border-color: #2B2E33;
        }

        #main-nav.nav-tabs.nav-stacked > li.active > a, #main-nav.nav-tabs.nav-stacked > li > a:hover > span {
            color: #FFF;
        }

        #main-nav.nav-tabs.nav-stacked > li {
            margin-bottom: 4px;
        }

        /*定义二级菜单样式*/
        .secondmenu a {
            font-size: 10px;
            color: #4A515B;
            text-align: center;
        }

        .navbar-static-top {
            background-color: #212121;
            margin-bottom: 5px;
        }

        .navbar-brand {
            background: url('') no-repeat 10px 8px;
            display: inline-block;
            vertical-align: middle;
            padding-left: 50px;
            color: #fff;
        }
    </style>
</head>

<body>

<div class="navbar navbar-duomi navbar-static-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#" id="logo">药房管理系统
            </a>#
        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-2">
            <ul id="main-nav" class="nav nav-tabs nav-stacked" style="">
                <li class="active">
                    <a href="#">
                        <i class="glyphicon glyphicon-th-large"></i>
                        首页
                    </a>
                </li>
                <li>
                    <a href="#systemSetting" class="nav-header collapsed" data-toggle="collapse">
                        <i class="glyphicon glyphicon-cog"></i>
                        系统管理
                        <span class="pull-right glyphicon glyphicon-chevron-down"></span>
                    </a>
                    <ul id="systemSetting" class="nav nav-list collapse secondmenu" style="height: 0px;">
                        <li><a href="/drug/med/good_register" target="iFrame1"><i class="glyphicon glyphicon-user"></i>来货登记</a></li>
                        <li><a href="/drug/med/show_order_checkout" target="iFrame1"><i class="glyphicon glyphicon-th-list"></i>订单日结</a></li>
                        <li><a href="/drug/med/show_order_msg" target="iFrame1"><i class="glyphicon glyphicon-asterisk"></i>订单信息查询</a></li>
                        <li><a href="/drug/med/show_drug_msg" target="iFrame1"><i class="glyphicon glyphicon-edit"></i>药品信息查询</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <div class="col-md-10">
            <iframe src="about:blank" name="iFrame1"
                    frameborder="no" border="0" height="500" width="100%"></iframe>
        </div>
    </div>
</div>
</body>
</html>
