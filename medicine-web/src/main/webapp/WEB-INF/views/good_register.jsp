<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>来货登记</title>

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

            var qryFun = function(){
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
                            //console.log(result.drug);
                            $("#drugNo_error_info").empty();
                            var drug = result.drug;
                            $("#drug_tab tr:eq(1)").remove();
                            var trHTML = "<tr><td>1</td><td>"
                                    +drug.drugNo+"</td><td>"
                                    +drug.drugName+"</td><td>"
                                    +drug.specification+"</td><td>"
                                    +drug.purchasePrice+"</td><td>"
                                    +drug.sellingPrice+"</td><td>"
                                    +drug.number+"</td><td>"
                                    +drug.origin+"</td>"
                                    +"<td><button type='button' " +
                                    "edit_drug_no='"+drug.drugNo+"' " +
                                    "edit_drug_origin='"+drug.origin+"' " +
                                    "edit_drug_name='"+drug.drugName+"' " +
                                    "edit_drug_spe='"+drug.specification+"' " +
                                    "edit_drug_purchase_price='"+drug.purchasePrice+"' " +
                                    "edit_drug_sell_price='"+drug.sellingPrice+"' " +
                                    "edit_drug_num='"+drug.number+"' " +
                                    "class='btn btn-default edit_btn'>修改</button></td></tr>"
                            $("#drug_tab").append(trHTML);
                            $(".edit_btn").click(edit_func);
                        }
                    }
                });
            };

            var edit_func = function(){
                $("#drugNoEdit").val($(this).attr('edit_drug_no'));
                $("#drugOriginalEdit").val($(this).attr('edit_drug_origin'));
                $("#drugNameEdit").val($(this).attr('edit_drug_name'));
                $("#purchasePriceEdit").val($(this).attr('edit_drug_purchase_price'));
                $("#sellingPriceEdit").val($(this).attr('edit_drug_sell_price'));
                $("#drugNumEdit").val($(this).attr('edit_drug_num'));
                $("#drugSpeEdit").val($(this).attr('edit_drug_spe'));
                $("#editModal").modal("show");
            }

            $("#drugQryBtn").click(qryFun);

            $("#edit_drug").click(function(){
                data = {
                    "drugNoAdd": $("#drugNoEdit").val(),
                    "drugOriginalAdd": $("#drugOriginalEdit").val(),
                    "drugNameAdd": $("#drugNameEdit").val(),
                    "number": $("#drugNumEdit").val(),
                    "drugSpeAdd": $("#drugSpeEdit").val()
                }
                $.ajax({
                    type: "POST",  //提交方式
                    url: "${pageContext.request.contextPath}/med/edit_drug",//路径
                    'contentType': 'application/json',
                    'dataType': 'json',
                    'data': JSON.stringify(data),
                    //数据，这里使用的是Json格式进行传输
                    success: function (result) {//返回数据根据结果进行相应的处理
                        if (result.update_flag == "false") {
                            alert("药品信息修改失败!");
                        } else {
                            alert("药品信息修改成功!");
                            $("#editModal").modal("hide");
                        }
                    }
                });
            });

            $("#reg_btn").click(function(){
                data = {
                    "drugNoAdd": $("#drugNo").val(),
                    "number": $("#drugNum").val()
                }
                $.ajax({
                    type: "POST",  //提交方式
                    url: "${pageContext.request.contextPath}/med/add_drugNum",//路径
                    'contentType': 'application/json',
                    'dataType': 'json',
                    'data': JSON.stringify(data),
                    //数据，这里使用的是Json格式进行传输
                    success: function (result) {//返回数据根据结果进行相应的处理
                        if (result.update_flag == "false") {
                            alert("库存增加失败!");
                        } else {
                            alert("库存增加成功!");
                            qryFun();
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
    </style>
</head>

<body>
<div align="center" id="main_div">
    <form role="form" class="form-inline" style="align-content: center;width: 100%;">
        <div class="form-group">
            <label for="drugNo">药品编号</label>

            <input type="text" id="drugNo" class="form-control">
            <button type="button" id="drugQryBtn" class="btn btn-default">查询</button>
            <span id="drugNo_error_info" class="form-group" style="color: red;"></span>
        </div>

        <div class="form-group">
            <label for="drugNum">进货数量</label>

            <input type="text" id="drugNum" class="form-control">


        </div>
        <div class="form-group">
            <label for="drugNum">业务员</label>

            <input type="text" id="name" class="form-control" value="${requestScope.user.name}" readonly>

        </div>
        <button type="button" id="reg_btn" class="btn btn-default">录入</button>
    </form>
</div>
<div>
    <button type="button" id="add_btn" class="btn btn-primary btn-lg" style="float:right;" data-toggle="modal"
            data-target="#myModal">新增药品
    </button>
    <table class="table" id="drug_tab">
        <th>行号</th>
        <th>药品编码</th>
        <th>药品名称</th>
        <th>药品规格</th>
        <th>含税进价(元)</th>
        <th>零售价格(元)</th>
        <th>库存</th>
        <th>产地</th>
        <th>操作</th>
    </table>
</div>

<!-- add Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">新增药品</h4>
            </div>
            <div class="modal-body">
                <form role="form" class="form-inline" style="align-content: center;width: 100%;">
                    <div class="form-group">
                        <label for="drugNoAdd">药品编号</label>
                        <input type="text" id="drugNoAdd" class="form-control">
                        <span id="error_info" class="form-group"></span>
                    </div>

                    <div class="form-group">
                        <label for="drugNameAdd">药品名称</label>
                        <input type="text" id="drugNameAdd" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="drugSpeAdd">药品规格</label>
                        <input type="text" id="drugSpeAdd" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="drugOriginalAdd">药品产地</label>
                        <input type="text" id="drugOriginalAdd" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="drugPriceAdd">含税进价</label>
                        <input type="text" id="drugPriceAdd" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="drugSellPriceAdd">零售价格</label>
                        <input type="text" id="drugSellPriceAdd" class="form-control">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="save_new_drug">保存</button>
            </div>
        </div>
    </div>
</div>

<!-- edit Modal -->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title">药品信息修改</h4>
            </div>
            <div class="modal-body">
                <form role="form" class="form-inline" style="align-content: center;width: 100%;">
                    <div class="form-group">
                        <label for="drugNoEdit">药品编号</label>
                        <input type="text" id="drugNoEdit" class="form-control" readonly>
                    </div>

                    <div class="form-group">
                        <label for="drugNameEdit">药品名称</label>
                        <input type="text" id="drugNameEdit" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="drugSpeEdit">药品规格</label>
                        <input type="text" id="drugSpeEdit" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="drugOriginalEdit">药品产地</label>
                        <input type="text" id="drugOriginalEdit" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="purchasePriceEdit">含税进价</label>
                        <input type="text" id="purchasePriceEdit" class="form-control" readonly>
                    </div>
                    <div class="form-group">
                        <label for="sellingPriceEdit">零售价格</label>
                        <input type="text" id="sellingPriceEdit" class="form-control" readonly>
                    </div>
                    <div class="form-group">
                        <label for="drugNumEdit">药品库存</label>
                        <input type="text" id="drugNumEdit" class="form-control">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" id="edit_drug">保存</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
