<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>药品信息查询</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.twbsPagination.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap-datetimepicker.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap-datetimepicker.fr.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/bootstrap-datetimepicker.zh-CN.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.validate.min.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/dateutil.js" type="text/javascript"></script>
    <script src="<%=request.getContextPath()%>/js/xdate.js" type="text/javascript"></script>
    <script>
        console.log("${pageContext.request.contextPath}");
        var contextPath = "${pageContext.request.contextPath}";
    </script>
    <script src="<%=request.getContextPath()%>/js/modules/drug_msg.js" type="text/javascript"></script>

    <style>
        body {
        //background-color: #46b8da;
        }

        #main_div {
            padding: 25px;
            margin-top: 20px;
            /*background-color : #269abc;*/
        }

        .error_info {
            color: red;
            font-weight: 100;
        }

        .layout-content-title {
            font-size: 16px;
            font-weight: 600;
            color: #006dcc;
            border-left: 5px solid #7c9be8;
            padding-left: 10px;
        }
    </style>
</head>

<body>
<div id="main_div">
    <form role="form" id="qry_form" class="form-inline" style="align-content: center;width: 100%;">

        <div class="form-group">
            <label for="drugNo">药品编号</label>
            <input type="text" id="drugNo" class="form-control">
            <label id="drugNo_errorinfo"></label>
        </div>

        <div class="form-group">
            <label for="drugName">药品名称</label>
            <input type="text" id="drugName" class="form-control">
            <label id="drugName_errorinfo"></label>
        </div>
        <div class="form-group">
            <label for="drugSpe">药品规格</label>
            <input type="text" id="drugSpe" class="form-control">
            <label id="drugSpe_errorinfo"></label>
        </div>
        <div class="form-group">
            <label for="drugOriginal">药品产地</label>
            <input type="text" id="drugOriginal" class="form-control">
            <label id="drugOriginal_errorinfo"></label>
        </div><br>
        <div class="form-group">
            <label for="number">药品库存</label>
            <input type="text" id="number" name="number" class="form-control">
            <label id="number_errorinfo" class="error_info"></label>
        </div>

        <div class="form-group">
            <label for="qryStartDate">开始时间</label>
            <input type="text" id="qryStartDate" name="qryStartDate" class="form-control date_format" value=""
                   readonly>
            <label id="qryStartDate_errorinfo"></label>
        </div>

        <div class="form-group">
            <label for="qryEndDate">结束时间</label>
            <input type="text" id="qryEndDate" name="qryEndDate" class="form-control date_format" value="" readonly>
        </div>
        <div class="form-group">
            <label for="qryEndDate">库存金额</label>
            <input type="text" id="totalFee" class="form-control" value="" readonly>
        </div><br>

        <div class="form-group">
            <label for="qryEndDate">生产批次</label>
            <input type="text" id="checkInNo" name="checkInNo" class="form-control date_format" value="">
        </div>
        <div class="form-group">
            <label for="qryEndDate">有效期</label>
            <input type="text" id="legalDate" name="legalDate" class="form-control" value="">
        </div><br>

    </form>
    <div class="center">
        <button type="button" id="qry_btn" class="btn btn-default">查询</button>
        <button type="button" id="export_btn" class="btn btn-default">导出</button>
    </div>
</div>
<div>
    <h3>详细数据</h3>
    <table class="table" id="drug_tab">
        <th>行号</th>
        <th>药品编码</th>
        <th>药品名称</th>
        <th>药品规格</th>
        <th>含税进价(元)</th>
        <th>零售价格(元)</th>
        <th>库存</th>
        <th>产地</th>
        <th>创建时间</th>
        <th>批次</th>
        <th>有效期</th>
    </table>
    <span style="font-size:14px;">
            <div class="text-center">
                <ul id="pagination-log" class="pagination-sm"></ul>
            </div>
        </span>
</div>
</body>

</html>