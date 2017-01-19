<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>订单每日汇总结算</title>

    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function () {
            $("#save_new_drug").click(function () {
                data = {
                    "drugNoAdd": $("#drugNoAdd").val(),
                    "drugOriginalAdd": $("#drugOriginalAdd").val(),
                    "drugNameAdd": $("#drugNameAdd").val(),
                    "price": $("#drugPriceAdd").val(),
                    "drugSellPriceAdd": $("#drugSellPriceAdd").val(),
                    "drugSpeAdd": $("#drugSpeAdd").val()
                }
                $.ajax({
                    type: "POST",  //提交方式
                    url: "${pageContext.request.contextPath}/med/save_new_drug",//路径
                    'contentType': 'application/json',
                    'dataType': 'json',
                    'data': JSON.stringify(data),
                    //数据，这里使用的是Json格式进行传输
                    success: function (result) {//返回数据根据结果进行相应的处理
                        if (result.loginFlag == "false") {
                            alert("新增商品错误!");
                        } else {
                            alert("新增商品成功!");
                        }
                    }
                });
            });

            var qryFun = function () {
                $("#drugQryBtn").attr("disabled",true);
                data = {
                    "drugNoAdd": $("#drugNo").val()
                }
                $.ajax({
                    type: "POST",  //提交方式
                    url: "${pageContext.request.contextPath}/med/query_drug_byno",//路径
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
                            $("#drugQryBtn").attr("disabled",false);
                            console.log(result.drug);
                            $("#drugNo_error_info").empty();
                            var drug = result.drug;
                            $("#drugNameShow").val(drug.drugName);
                            $("#drugSpeShow").val(drug.specification);
                            $("#drugOriginalShow").val(drug.origin);
                            $("#purchasePriceShow").val(drug.purchasePrice);
                            $("#drugNumShow").val(drug.number);
                            $("#sellingPriceShow").val(drug.sellingPrice);
                        }
                    }
                });
            };
            //$("#drugQryBtn").click(qryFun);

            document.onkeydown = function(e){
                var ev = document.all ? window.event : e;
                if(ev.keyCode==13) {
                    qryFun();
                    $("#sellNum").val('0');
                    $("#sellNum").select();
                    $("#costAmount").val('');
                    $("#sellAmount").val('');
                }
            }

            $("#sellNum").change(function(){
                if(parseInt($(this).val())>parseInt($("#drugNumShow").val())){
                    alert("销售数量不能大于库存数量!");
                    return;
                }
                var costAmount = parseFloat($(this).val())*parseFloat($("#purchasePriceShow").val());
                var amount = parseFloat($(this).val())*parseFloat($("#sellingPriceShow").val());
                $("#costAmount").val(costAmount.toFixed(2));
                $("#sellAmount").val(amount.toFixed(2));
                $("#add_row_btn").focus();
            });

            $("#add_row_btn").click(function () {
                if($("#sellAmount").val() == ''){
                    alert("请先输入药品编码进行查询&销售操作!");
                    return;
                }
                var trHTML = "<tr><td>"
                        +$("#drugNo").val()+"</td><td>"
                        +$("#drugNameShow").val()+"</td><td>"
                        +$("#drugSpeShow").val()+"</td><td>"
                        +$("#purchasePriceShow").val()+"</td><td>"
                        +$("#costAmount").val()+"</td><td>"
                        +$("#sellAmount").val()+"</td><td>"
                        +$("#sellNum").val()+"</td>"
                        +"<td><button type='button' class='btn btn-default del_row_btn'>删除</button></td></tr>"
                $("#drug_tab").append(trHTML);
                calculateTotalAmount();
                $("#totalInfoForm").show();
                $("#drugNo").focus();
                $(".del_row_btn").click(function () {
                    $(this).parent().parent().remove();
                    calculateTotalAmount();
                });
            });

            function calculateTotalAmount(){
                var totalAmount = 0;
                var totalCostAmount = 0;
                $("#drug_tab tr :gt(0)").each(function(){
                    var tempCostAmount = parseFloat($(this).find("td").eq(4).text());
                    var tempAmount = parseFloat($(this).find("td").eq(5).text());
                    console.log($(this).find("td").eq(4).text() );
                    totalAmount += tempAmount;
                    totalCostAmount += tempCostAmount;
                });
                $("#totalCostAmount").val(totalCostAmount.toFixed(2));
                $("#totalAmount").val(totalAmount.toFixed(2));
                $("#acturalAmount").val(totalAmount.toFixed(2));
                $("#acturalAmount").change();
            }

            $("#acturalAmount").change(function(){
                var tax = parseFloat($(this).val())*0.15;
                var reduceTaxAmount = parseFloat($(this).val()) - tax;
                var grossProfit = parseFloat(reduceTaxAmount) - parseFloat($("#totalCostAmount").val());
                $("#tax").val(tax.toFixed(2));
                $("#reduceTaxAmount").val(reduceTaxAmount.toFixed(2));
                $("#grossProfit").val(grossProfit.toFixed(2));
            });

            $("#confirm_btn").click(function(){
                var detailArray = [];
                $("#drug_tab tr:gt(0)").each(function(){
                    var obj = {};
                    obj.drugNo=$(this).find("td:eq(0)").text();
                    obj.sellNum=$(this).find("td:eq(6)").text();
                    obj.costAmount=$(this).find("td:eq(4)").text();
                    obj.sellAmount=$(this).find("td:eq(5)").text();
                    detailArray.push(obj);
                });
                var sellDetails = JSON.stringify(detailArray);
                console.log('sellDetails = '+sellDetails);
                data = {
                    "receivableAmount": $("#totalAmount").val(),
                    "paidAmount": $("#acturalAmount").val(),
                    "costAmount": $("#totalCostAmount").val(),
                    "grossProfit": $("#grossProfit").val(),
                    "tax": $("#tax").val(),
                    "reduceTaxAmount": $("#reduceTaxAmount").val(),
                    "opUser": $("#opUser").val(),
                    "sellDetails": sellDetails
                }
                console.log('data = '+JSON.stringify(data));
                $.ajax({
                    type: "POST",  //提交方式
                    url: "${pageContext.request.contextPath}/med/order_checkout",//路径
                    'contentType': 'application/json',
                    'dataType': 'json',
                    'data': JSON.stringify(data),
                    //数据，这里使用的是Json格式进行传输
                    success: function (result) {//返回数据根据结果进行相应的处理
                        if (result.opt_flag == "false") {
                            alert("订单入库错误!");
                        } else {
                            alert("订单入库成功!");
                            window.location.reload();
                        }
                    }
                });
            });
        });
    </script>

    <style>
        #main_div {
            padding: 25px;
            margin-top: 20px;
            /*background-color : #269abc;*/
        }

        body {
            /*background-color :;*/
        }

        .input-width{
            width:200px;
        }
    </style>
</head>

<body>
<div id="main_div">
    <form role="form" class="form-inline" style="align-content: center;width:100%;">
        <div class="form-group" style="min-width: 500px;margin-bottom:5px;">
            <label for="opUser">业务员&nbsp;&nbsp;&nbsp;</label>
            <select id="opUser"   class="form-control" style="min-width: 200px;">
                <c:forEach items="${allUsers}" var="user" varStatus="vs">
                    <option value="${user.id}">${user.name}</option>
                </c:forEach>
            </select>
        </div><hr>
        <div class="form-group">
            <label for="drugNo">药品编号</label>
            <input type="text" id="drugNo" class="form-control">
            <!--button type="button" id="drugQryBtn" class="btn btn-default">查询</button-->
            <span id="drugNo_error_info" class="form-group" style="color: red;"></span>
        </div>
        <div class="form-group">
            <label for="drugNameShow">药品名称</label>
            <input type="text" id="drugNameShow" class="form-control" readonly>
        </div>
        <div class="form-group">
            <label for="drugSpeShow">药品规格</label>
            <input type="text" id="drugSpeShow" class="form-control" readonly>
        </div><br>
        <div class="form-group">
            <label for="drugOriginalShow">药品产地</label>
            <input type="text" id="drugOriginalShow" class="form-control" readonly>
        </div>
        <div class="form-group">
            <label for="purchasePriceShow">含税进价</label>
            <input type="text" id="purchasePriceShow" class="form-control" readonly>
        </div>
        <div class="form-group">
            <label for="sellingPriceShow">零售价格</label>
            <input type="text" id="sellingPriceShow" class="form-control" readonly>
        </div><br>
        <div class="form-group">
            <label for="drugNumShow">药品库存</label>
            <input type="text" id="drugNumShow" class="form-control" readonly>
        </div><hr>
        <div class="form-group">
            <label for="sellNum">销售数量</label>
            <input type="text" id="sellNum" class="form-control">
        </div>
        <div class="form-group">
            <label for="costAmount">成本总额</label>
            <input type="text" id="costAmount" class="form-control" readonly>
        </div><br>
        <div class="form-group">
            <label for="sellAmount">销售金额</label>
            <input type="text" id="sellAmount" class="form-control" readonly>
        </div>
        <button type="button" id="add_row_btn" class="btn btn-default">添加</button>
    </form>
</div>
<div>
    <table class="table" id="drug_tab">
        <th>药品编码</th>
        <th>药品名称</th>
        <th>药品规格</th>
        <th>含税进价(元)</th>
        <th>成本金额</th>
        <th>销售金额</th>
        <th>销售数量</th>
        <th>操作</th>
    </table>
    <form role="form" class="form-inline" id="totalInfoForm" style="align-content: center;width:100%;display:none;">
        <div class="form-group">
            <label for="totalCostAmount">成本金额</label>
            <input type="text" id="totalCostAmount" class="form-control input-width" readonly>
        </div>
        <div class="form-group">
            <label for="totalAmount">应收总额</label>
            <input type="text" id="totalAmount" class="form-control input-width" readonly>
        </div>
        <div class="form-group">
            <label for="acturalAmount">实收金额</label>
            <input type="text" id="acturalAmount" class="form-control input-width">
        </div><br>
        <div class="form-group">
            <label for="tax">税额</label>
            <input type="text" id="tax" class="form-control input-width" readonly>
        </div>
        <div class="form-group">
            <label for="reduceTaxAmount">扣税金额</label>
            <input type="text" id="reduceTaxAmount" class="form-control input-width" readonly>
        </div>
        <div class="form-group">
            <label for="grossProfit">毛利(扣税金额 - 成本金额)</label>
            <input type="text" id="grossProfit" class="form-control input-width" readonly>
        </div>
        <div class="form-group central-block">
            <button type="button" id="confirm_btn" class="btn btn-primary btn-lg" data-toggle="modal"
                    data-target="#myModal">汇总录入
            </button>
        </div>
    </form>
</div>

</body>
</html>
