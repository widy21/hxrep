<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单信息查询</title>

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
                format: 'yyyy-mm-dd'      /*此属性是显示顺序，还有显示顺序是mm-dd-yyyy*/
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
                        "qryStartDate": $("#qryStartDate").val(),
                        "qryEndDate": $("#qryEndDate").val(),
                        "opUser": $("#opUser").val(),
                        "currentPage": pagenow
                    }
                    $.ajax({
                        type: "POST",  //提交方式
                        url: "${pageContext.request.contextPath}/med/order_qry",//路径
                        'contentType': 'application/json',
                        'dataType': 'json',
                        'data': JSON.stringify(data),
                        success : function(data) {
                            var htmlobj = "";
                            console.log(data);
                            totalPage = data.pageCount;//将后台数据复制给总页数
                            totalcount = data.recordNum;
                            $("#drug_tab tr:gt(0)").empty();
                            $.each(data.orders, function(index, order) {
                                htmlobj = htmlobj + "<tr><td>"
                                + (index+1) + "</td><td>"
                                + order.receivableAmount + "</td><td>"
                                + order.paidAmount + "</td><td>"
                                + order.costAmount + "</td><td>"
                                + order.grossProfit + "</td><td>"
                                + order.tax + "</td><td>"
                                + order.reduceTaxAmount + "</td><td>"
                                + order.opUserName + "</td><td>"
                                + order.createTime.substring(0,10) + "</td>"

                                htmlobj = htmlobj + "</tr>";

                                $("#drug_tab").append(htmlobj);
                                htmlobj = "";

                            });

                            $("#total_drug_tab tr:gt(0)").empty();
                            var totalOrder = data.totalOrder;
                            if(totalOrder != undefined){
                                var total_htmlobj = "<tr><td>1</td><td>"
                                        + totalOrder.receivableAmount + "</td><td>"
                                        + totalOrder.paidAmount + "</td><td>"
                                        + totalOrder.costAmount + "</td><td>"
                                        + totalOrder.grossProfit + "</td><td>"
                                        + totalOrder.tax + "</td><td>"
                                        + totalOrder.reduceTaxAmount + "</td><td>"
                                        + totalOrder.opUserName + "</td></tr>";

                                $("#total_drug_tab").append(total_htmlobj);
                            }

                            if(data.orders.length == 0){
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
                    }
                },
                messages: {
                    qryStartDate: {
                        required: "请输入开始日期"
                    },
                    qryEndDate: {
                        required: "请输入结束日期"
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
<div class="layout-content-title">
    <span class="layout-content-title">订单信息查询</span>
</div>
<div align="center" id="main_div">
    <form role="form" id="qry_form" class="form-inline" style="align-content: center;width: 100%;">
        <div class="form-group">
            <label for="qryStartDate">开始时间</label>
            <input type="text" id="qryStartDate" name="qryStartDate" class="form-control date_format" value="" readonly>
            <label id="qryStartDate_errorinfo"><label>
        </div>

        <div class="form-group">
            <label for="qryEndDate">结束时间</label>
            <input type="text" id="qryEndDate" name="qryEndDate" class="form-control date_format" value="" readonly>


        </div>
        <div class="form-group" style="min-width: 500px;margin-bottom:5px;">
            <label for="opUser">业务员&nbsp;&nbsp;&nbsp;</label>
            <select id="opUser"   class="form-control" style="min-width: 200px;">
                <c:forEach items="${allUsers}" var="user" varStatus="vs">
                    <option value="${user.id}">${user.name}</option>
                </c:forEach>
            </select>
        </div>
        <button type="button" id="qry_btn" class="btn btn-default">查询</button>
    </form>
</div>
<div>
    <h3>详细数据</h3>
    <table class="table" id="drug_tab">
        <th>行号</th>
        <th>应收金额</th>
        <th>实收金额</th>
        <th>成本金额</th>
        <th>毛利</th>
        <th>税额</th>
        <th>扣税金额</th>
        <th>业务员</th>
        <th>创建时间</th>
    </table>
    <span style="font-size:14px;"><div class="text-center">
        <ul id="pagination-log" class="pagination-sm"></ul>
    </div></span>
</div>
<div>
    <h3>汇总数据</h3>
    <table class="table" id="total_drug_tab">
        <th>行号</th>
        <th>应收金额</th>
        <th>实收金额</th>
        <th>成本金额</th>
        <th>毛利</th>
        <th>税额</th>
        <th>扣税金额</th>
        <th>业务员</th>
    </table>
</div>
</body>
</html>
