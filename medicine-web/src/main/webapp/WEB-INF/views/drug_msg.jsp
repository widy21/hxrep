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
        $(document).ready(function(){

            $('.date_format').datetimepicker({
                language:'zh-CN',
                todayHighlight:true,
                weekStart:1,
                inline:true,
                minView:'month',
                autoclose:true,
                format: 'yyyy-mm-dd' /*此属性是显示顺序，还有显示顺序是mm-dd-yyyy*/
            });

            var initDate = function(){
                $("#qryStartDate").val(Utils.DateUtils.firstDateOfMonth(new Date()));
                $("#qryEndDate").val(Utils.DateUtils.endDateOfMonth(new Date()));
            }

            var initDrugData =function() {
                //重置分页组件否则保留上次查询的，一般来说很多问题出现与这三行代码有关如：虽然数据正确但是页码不对仍然为上一次查询出的一致
                $('#pagination-log').empty();
                $('#pagination-log').removeData("twbs-pagination");
                $('#pagination-log').unbind("page");
                //将页面的数据容器制空
                $("#drug_tab tr :gt(0)").remove();
                //设置默认当前页
                var pagenow = 1;
                //设置默认总页数
                var totalPage = 1;
                //设置默认可见页数
                var visiblecount = 5;
                //加载后台数据页面
                function loaddata() {
                    data = {
                        "drugNo": $("#drugNo").val(),
                        "drugOriginal": $("#drugOriginal").val(),
                        "drugName": $("#drugName").val(),
                        "drugSpe": $("#drugSpe").val(),
                        "number": $("#number").val(),
                        "qryStartDate": $("#qryStartDate").val(),
                        "qryEndDate": $("#qryEndDate").val(),
                        "currentPage": pagenow
                    }
                    $.ajax({
                        type: "POST",  //提交方式
                        url: "${pageContext.request.contextPath}/med/drug_page_qry",//路径
                        'contentType': 'application/json',
                        'dataType': 'json',
                        'data': JSON.stringify(data),
                        success : function(data) {
                            var htmlobj = "";
                            console.log(data);
                            totalPage = data.pageCount;//将后台数据复制给总页数
                            totalcount = data.recordNum;
                            totalFee = data.totalFee;
                            $("#totalFee").val(totalFee);
                            $("#drug_tab tr:gt(0)").empty();
                            $.each(data.drugs, function(index, drug) {
                                htmlobj = htmlobj + "<tr><td>"
                                + (index+1) + "</td><td>"
                                + drug.drugNo + "</td><td>"
                                + drug.drugName + "</td><td>"
                                + drug.specification + "</td><td>"
                                + drug.purchasePrice + "</td><td>"
                                + drug.sellingPrice + "</td><td>"
                                + drug.number + "</td><td>"
                                + drug.origin + "</td><td>"
                                + drug.createTime.substring(0,10) + "</td>"

                                htmlobj = htmlobj + "</tr>";

                                $("#drug_tab").append(htmlobj);
                                htmlobj = "";

                            });

                            if(data.drugs.length == 0){
                                $("#drug_tab").append("<tr><td colspan='9' align='center' style='color:red;font-size:14px'>无记录。</td></tr>");
                            }

                            //后台总页数与可见页数比较如果小于可见页数则可见页数设置为总页数，
                            if (totalPage < visiblecount) {
                                visiblecount = totalPage;
                            }
                            $('#pagination-log').twbsPagination({
                                first:'首页',
                                prev:'上一页',
                                next:'下一页',
                                last:'尾页',
                                totalPages : totalPage,
                                visiblePages : visiblecount,
                                version : '1.1',
                                //页面点击时触发事件
                                onPageClick : function(event, page) {
                                    // 将当前页数重置为page
                                    pagenow = page
                                    //调用后台获取数据函数加载点击的页码数据
                                    loaddata();

                                }
                            });

                        },
                        error : function(e) {
                            alert("s数据访问失败")
                        }
                    });
                }
                //函数初始化是调用内部函数
                loaddata();
            }

            var validate_config = {
                rules: {
                    qryStartDate: {
                        required: true
                    },
                    qryEndDate: {
                        required: true
                    },
                    number: {
                        number:true
                    }
                },
                messages: {
                    qryStartDate: {
                        required: "请输入开始日期"
                    },
                    qryEndDate: {
                        required: "请输入结束日期"
                    },
                    number: {
                        number: "库存必须为数字"
                    }
                },
                errorPlacement:function(error,element){
                    console.log(element.attr('id'));
                    $('#'+element.attr('id')+'_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

            $("#qry_btn").click(function(){
                if(!$("#qry_form").validate(validate_config).form()){
                    return false;
                }
                initDrugData();
            });

            $("#export_btn").click(function(){
                if(!$("#qry_form").validate(validate_config).form()){
                    return false;
                }
                initDrugData();
            });

            initDate();
        });
    </script>
    <style>
        body{
        //background-color: #46b8da;
        }
        #main_div {
            padding: 25px;
            margin-top: 20px;
            /*background-color : #269abc;*/
        }
        .error_info{
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
            <input type="text" id="qryStartDate" name="qryStartDate" class="form-control date_format" value="" readonly>
            <label id="qryStartDate_errorinfo"></label>
        </div>

        <div class="form-group">
            <label for="qryEndDate">结束时间</label>
            <input type="text" id="qryEndDate" name="qryEndDate" class="form-control date_format" value="" readonly>
        </div>
        <div class="form-group">
            <label for="qryEndDate">库存金额</label>
            <input type="text" id="totalFee" class="form-control" value="" readonly>
        </div>
        <button type="button" id="qry_btn" class="btn btn-default">查询</button>
        <button type="button" id="export_btn" class="btn btn-default">导出</button>
    </form>
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
    </table>
    <span style="font-size:14px;"><div class="text-center">
        <ul id="pagination-log" class="pagination-sm"></ul>
    </div></span>
</div>
</body>
</html>
