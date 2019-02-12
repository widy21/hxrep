<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>药房管理系统</title>
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap-closable-tab.js"></script>
    <style>
        #main-nav {
            margin-left: 1px;
        }

        #main-nav.nav-tabs1.nav-stacked > li > a {
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

        #main-nav.nav-tabs1.nav-stacked > li > a > span {
            color: #4A515B;
        }

        #main-nav.nav-tabs1.nav-stacked > li.active > a, #main-nav.nav-tabs1.nav-stacked > li > a:hover {
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

        #main-nav.nav-tabs1.nav-stacked > li.active > a, #main-nav.nav-tabs1.nav-stacked > li > a:hover > span {
            color: #FFF;
        }

        #main-nav.nav-tabs1.nav-stacked > li {
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

    <script>
        $(document).ready(function () {

             $("#systemSetting li a").click(function(){
                 console.log($(this).html());
                 console.log($(this).attr("tab_id"));
                 var item = {
                     'id':$(this).attr("tab_id"),
                     'name':$(this).html(),
                     'url':$(this).attr("tab_url"),
                     'closable':true
                 };
                 closableTab.addTab(item);
             });
            //默认打开tab页。
            $("#systemSetting li").eq(1).find("a").click();
         });

        /*$(function(){
            var item = {'id':'1','name':'首页','url':'show_order_checkout','closable':true};
            closableTab.addTab(item);
            var item1 = {'id':'2','name':'撒的发','url':'show_order_checkout','closable':true  };
            closableTab.addTab(item1);
        })*/
        function add(item){
//            var id = 1;
//            var name = 123;
//            var uri = 'son.html';
//            var closable = 1;
//            var item = {'id':id,'name':name,'url':uri,'closable':closable==1?true:false};
            closableTab.addTab(item);
        }
    </script>
</head>

<body>

<div class="navbar navbar-duomi navbar-static-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header center-block">
            <a class="navbar-brand" href="#" id="logo">药房管理系统
            </a>#
        </div>
    </div>
</div>
<div class="container-fluid">
    <div class="row">
        <div class="col-md-2">
            <ul id="main-nav" class="nav nav-tabs1 nav-stacked" style="">
                <li class="active">
                    <a href="/drug/med/showloginpage">
                        <i class="glyphicon glyphicon-th-large"></i>
                        首页
                    </a>
                </li>
                <li>
                    <a href="#systemSetting" class="nav-header collapse in" data-toggle="collapse">
                        <i class="glyphicon glyphicon-cog"></i>
                        系统管理
                        <span class="pull-right glyphicon glyphicon-chevron-down"></span>
                    </a>
                    <ul id="systemSetting" class="nav nav-list collapse in secondmenu" style="height:0px;">
                        <li><a href="javascript:void(0)" onclick="return false;" tab_id="0" tab_url="good_register" target="iFrame1"><i class="glyphicon glyphicon-user"></i>来货登记</a></li>
                        <li><a href="javascript:void(0)" onclick="return false;" tab_id="01" tab_url="good_register_new" target="iFrame1-2"><i class="glyphicon glyphicon-user"></i>来货批量登记</a></li>
                        <li><a href="javascript:void(0)" onclick="return false;" tab_id="1" tab_url="show_order_checkout" target="iFrame2"><i class="glyphicon glyphicon-euro"></i>订单日结</a></li>
                        <li><a href="javascript:void(0)" onclick="return false;" tab_id="2" tab_url="show_order_msg" target="iFrame3"><i class="glyphicon glyphicon-search"></i>销售信息查询</a></li>
                        <li><a href="javascript:void(0)" onclick="return false;" tab_id="3" tab_url="show_drug_msg" target="iFrame4"><i class="glyphicon glyphicon-zoom-in"></i>库存信息查询</a></li>
                    </ul>
                </li>
            </ul>
        </div>
        <div class="col-md-10">
            <!-- 此处是相关代码 -->
            <ul class="nav nav-tabs" role="tablist">
            </ul>
            <div class="tab-content" style="width:100%;">
            </div>
            <!-- 相关代码结束 -->
        </div>
    </div>
</div>
</body>
</html>
