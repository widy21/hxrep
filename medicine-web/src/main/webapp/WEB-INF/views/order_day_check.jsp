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
    <script src="<%=request.getContextPath()%>/js/bootstrap-typeahead.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.validate.min.js" type="text/javascript"></script>
    <script>
        $(document).ready(function () {
            var qry_drug_info = {};
            var add_drug_info = {};
            var name2Id = {};//对应关系对象
            var spell_array = [];
            var getSpellInfo = function(){
                $.ajax({
                    type: "POST",  //提交方式
                    url: "${pageContext.request.contextPath}/med/get_drug_spell_info",
                    'contentType': 'application/json',
                    'dataType': 'json',
                    success: function (result) {//返回数据根据结果进行相应的处理
                        if (result.query_flag == "false") {
                            alert("查询药品错误!");
                        } else {
                            var drugs = result.allDrugSpell;
                            console.log(drugs.length);
                            $.each(drugs, function (index, ele) {
                                var key = ele.pinyin+' 【'+ele.drugName+'】';
                                name2Id[key] = ele.drugNo;//键值对保存下来。
                                spell_array.push(key);
                            });
                            $("#drugNo").focus();
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("新增药品错误,请检查配置信息是否正确!");
                    }
                });
            }

            //加载拼写数据
            getSpellInfo();

            $('#drugNo').typeahead({
                source: function (query, process) {
                    process(spell_array);
                },
                updater: function (item) {
                    console.log(name2Id[item]);//打印对应的id
                    $("#drugNo").val(name2Id[item]);
                    qryFun();
                    return name2Id[item];
                }

            });

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
                            //添加查询成功药品信息
                            qry_drug_info[$("#drugNo").val()] = $("#drugNumShow").val();
                        }
                    }
                });
            };
            //$("#drugQryBtn").click(qryFun);

            document.onkeydown = function(e){
                //console.log(e);
                var ev = document.all ? window.event : e;
                if(ev.keyCode==13) {
                    if(e.srcElement.id=='drugNo'){
                        qryFun();
                        $("#sellNum").val('0');
                        $("#sellNum").select();
                        $("#costAmount").val('');
                        $("#sellAmount").val('');
                    }
                    if(e.srcElement.id=='sellNum'){
                        var flag = $("#sellNum").change();
                        if(!flag){
                            $("#add_row_btn").click();
                        }
                    }

                }
            }

            $("#sellNum").change(function(){
                if(parseInt($(this).val())>parseInt($("#drugNumShow").val())){
                    alert("销售数量不能大于库存数量!");
                    return false;
                }
                var costAmount = parseFloat($(this).val())*parseFloat($("#purchasePriceShow").val());
                var amount = parseFloat($(this).val())*parseFloat($("#sellingPriceShow").val());
                $("#costAmount").val(costAmount.toFixed(2));
                $("#sellAmount").val(amount.toFixed(2));
                $("#add_row_btn").focus();
            });

            var isEnableSellNum = function(offset){
                var key = $("#drugNo").val();
                var drug_number = qry_drug_info[key];
                var sell_number = add_drug_info[key];
                console.log('drug_number='+drug_number);
                console.log('sell_number='+(sell_number+offset));
                if(parseInt(sell_number+offset)>parseInt(drug_number)){
                    return false;
                }
                return true;
            }

            $("#add_row_btn").click(function () {
                if(!$("#qry_form").validate(validate_config).form()){
                    return false;
                }
                if($("#sellAmount").val() == ''){
                    alert("请先输入药品编码进行查询&销售操作!");
                    return;
                }
                //判断药品总库存是否大于药品库存
                var offset = parseInt($("#sellNum").val());
                if(!isEnableSellNum(offset)){
                    alert("添加药品总销量大于药品库存,请检查销售数量配置!");
                    return false;
                }
                //添加判断成功药品销量
                var key = $("#drugNo").val();
                console.log('添加前销量：'+add_drug_info[key]);
                if(add_drug_info[key]==undefined){
                    add_drug_info[key] = offset;
                }else{
                    add_drug_info[key] = parseInt(add_drug_info[key])+offset;
                }
                console.log('添加后销量：'+add_drug_info[key]);
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
                $("#drugNo").select();
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
                //税额 = 含税毛利（实收-成本） * 0.15
                var totalCostAmount = parseFloat($("#totalCostAmount").val());
                var tax = (parseFloat($(this).val())-totalCostAmount)*0.15;
                var reduceTaxAmount = parseFloat($(this).val()) - tax;
                var grossProfit = parseFloat(reduceTaxAmount) - parseFloat($("#totalCostAmount").val());
                $("#tax").val(tax.toFixed(2));
                $("#reduceTaxAmount").val(reduceTaxAmount.toFixed(2));
                $("#grossProfit").val(grossProfit.toFixed(2));
            });

            $("#confirm_btn").click(function(){
                if(!$("#totalInfoForm").validate(validate_confirm_config).form()){
                    return false;
                }
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
                            clearform();
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("订单入库错误,请检查配置信息是否正确!");
                    }
                });
            });

            var validate_config = {
                rules: {
                    sellNum:{
                        required: true,
                        positiveinteger:true
                    },
                    sellAmount:{
                        number:true,
                        required: true
                    }
                },
                messages: {
                    sellNum:{
                        required: "请输入销售数量",
                        positiveinteger:"销售数量必须为正整数"
                    },
                    sellAmount:{
                        required: "请输入销售金额",
                        number:"销售数量必须为数字"
                    }
                },
                errorPlacement:function(error,element){
                    console.log(element.attr('id'));
                    $('#'+element.attr('id')+'_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

            var validate_confirm_config = {
                rules: {
                    acturalAmount:{
                        required: true,
                        number:true
                    }
                },
                messages: {
                    acturalAmount:{
                        required: "请输入实收金额",
                        number:"实收金额必须为数字"
                    }
                },
                errorPlacement:function(error,element){
                    console.log(element.attr('id'));
                    $('#'+element.attr('id')+'_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }


        });

        function clearform(){
            $("#main_div input").val("");
            $("#totalInfoForm input").val("");
            $("#drug_tab tr:gt(0)").remove();
            $("#drugNo").focus();
        }

        jQuery.validator.addMethod("positiveinteger", function(value, element) {
            var aint=parseInt(value);
            return aint>0&& (aint+"")==value;
        }, "Please enter a valid number.");
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

        .error_info{
            color: red;
            font-weight: 100;
        }
    </style>
</head>

<body>
<div id="main_div">
    <form role="form" id="qry_form" class="form-inline" style="align-content: center;width:100%;">
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
            <input type="text" id="sellNum" name="sellNum" class="form-control">
            <label id="sellNum_errorinfo" class="error_info"></label>
        </div>
        <div class="form-group">
            <label for="costAmount">成本总额</label>
            <input type="text" id="costAmount" class="form-control" readonly>
        </div><br>
        <div class="form-group">
            <label for="sellAmount">销售金额</label>
            <input type="text" id="sellAmount" name="sellAmount" class="form-control">
            <label id="sellAmount_errorinfo" class="error_info"></label>
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
            <input type="text" id="acturalAmount" name="acturalAmount" class="form-control input-width">
            <label id="acturalAmount_errorinfo" class="error_info"></label>
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
