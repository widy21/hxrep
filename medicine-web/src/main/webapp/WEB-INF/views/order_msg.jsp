<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单查询</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function(){
            var qryFun = function(obj){
                data = {
                    "qryStartDate": $("#qryStartDate").val(),
                    "qryEndDate": $("#qryEndDate").val(),
                    "opUser": $("#opUser").val(),
                    "currentPage": obj.page_num,
                    "start": $("#start").val()
                }
                $.ajax({
                    type: "POST",  //提交方式
                    url: "${pageContext.request.contextPath}/med/order_qry",//路径
                    'contentType': 'application/json',
                    'dataType': 'json',
                    'data': JSON.stringify(data),
                    //数据，这里使用的是Json格式进行传输
                    success: function (result) {//返回数据根据结果进行相应的处理
                        if (result.query_flag == "none") {
                            $("#drugNo_error_info").html("药品不存在!");
                        } else if (result.query_flag == "false") {
                            $("#drugNo_error_info").html("查询出错!");
                        } else {
                            console.log(result);
//                            $("#drugNo_error_info").empty();
//                            var drug = result.drug;
//                            $("#drug_tab tr:eq(1)").remove();
//                            var trHTML = "<tr><td>1</td><td>"
//                                    +drug.drugNo+"</td><td>"
//                                    +drug.drugName+"</td><td>"
//                                    +drug.specification+"</td><td>"
//                                    +drug.purchasePrice+"</td><td>"
//                                    +drug.sellingPrice+"</td><td>"
//                                    +drug.number+"</td><td>"
//                                    +drug.origin+"</td>"
//                                    +"<td><button type='button' " +
//                                    "edit_drug_no='"+drug.drugNo+"' " +
//                                    "edit_drug_origin='"+drug.origin+"' " +
//                                    "edit_drug_name='"+drug.drugName+"' " +
//                                    "edit_drug_spe='"+drug.specification+"' " +
//                                    "edit_drug_purchase_price='"+drug.purchasePrice+"' " +
//                                    "edit_drug_sell_price='"+drug.sellingPrice+"' " +
//                                    "edit_drug_num='"+drug.number+"' " +
//                                    "class='btn btn-default edit_btn'>修改</button></td></tr>"
//                            $("#drug_tab").append(trHTML);
//                            $(".edit_btn").click(edit_func);
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("查询药品错误,请检查查询条件是否正确!");
                    }
                });
            };

            $("#qry_btn").click(qryFun);
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
    </style>
</head>

<body>
<div align="center" id="main_div">
    <form role="form" class="form-inline" style="align-content: center;width: 100%;">
        <div class="form-group">
            <label for="qryStartDate">开始时间</label>
            <input type="text" id="qryStartDate" class="form-control">
        </div>

        <div class="form-group">
            <label for="qryEndDate">结束时间</label>
            <input type="text" id="qryEndDate" class="form-control">


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
        <th>操作</th>
    </table>
    <div>
        <c:if test="${currentPage != 1}">
        <nav class="pull-right">
            <ul class="pagination">
                <li class="${currentPage==1?'disabled':''}">
                    <a style="cursor: pointer" onclick="${currentPage==1?'':qryFun(this)}" page_num="${currentPage-1}">前一页</a>
                </li>
                <%
                    int page_num=10;
                    //计算要显示的页数
                    var num=para0%page_num==0?para0-(page_num-1):parseInt(para0/page_num)*page_num+1;
                    for(i in range(0,page_num+1)){
                        if((i+num)>para2) break;
                %>
                <li class="${(num+i)==para0?'active':''} text-info">
                    <a style="cursor: pointer;" onclick="${para1}" page_num="${num+i,numberFormat='#'}">${num+i,numberFormat="#"}</a>
                </li>
                <%}%>
                <li  class="${para0==para2?'disabled':''}">
                    <a onclick="${para0==para2?'':para1}" style="cursor: pointer" page_num="${para0+1}">后一页</a>
                </li>
            </ul>
            <p class="text-right">第${para0}页  共${para2}页 共${para3}条</p></nav>
        </c:if>
    </div>
</div>
</body>
</html>
