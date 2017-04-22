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
    <script src="<%=request.getContextPath()%>/js/bootstrap-typeahead.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.json-2.2-min.js"></script>
    <script src="<%=request.getContextPath()%>/js/jquery.validate.min.js" type="text/javascript"></script>
    <script>
        $(document).ready(function () {

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
                    }/*,
                    error: function (result) {
                        console.log(result.responseText);
                        alert("新增药品错误,请检查配置信息是否正确!");
                    }*/
                });
            }

            //加载拼写数据
            getSpellInfo();

            $('#drugNo').typeahead({
                source: function (query, process) {
                    process(spell_array);
                },
                /* matcher: function (obj) {
                 console.log(obj);
                 var item = obj;
                 return item.name.toLowerCase().indexOf(this.query.toLowerCase())
                 },

                 highlighter: function(item) {
                 return "==>" + item + "<==";
                 },*/
                updater: function (item) {
                    console.log(name2Id[item]);//打印对应的id
                    $("#drugNo").val(name2Id[item]);
                    qryFun();
                    return name2Id[item];
                }

            });

            $("#save_new_drug").click(function () {
                if(!$("#add_drug_form").validate(validate_add_config).form()){
                    return false;
                }
                $(this).attr({"disabled":"disabled"});
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
                        $(this).removeAttr("disabled");//将按钮可用
                        if (result.update_flag == "false") {
                            alert("新增药品错误!");
                            $("#myModal").modal("hide");
                        } else {
                            alert("新增药品成功!");
                            $("#myModal").modal("hide");
                            window.location.reload();
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("新增药品错误,请检查配置信息是否正确!");
                        $("#save_new_drug").removeAttr("disabled");//将按钮可用
                    }
                });
            });

            var qryFun = function () {
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
                            $("#drugNo_errorinfo").html("药品不存在!");
                        } else if (result.query_flag == "false") {
                            $("#drugNo_errorinfo").html("查询出错!");
                        } else {
                            //console.log(result.drug);
                            $("#drugNo_errorinfo").empty();
                            var drug = result.drug;
                            $("#drug_tab tr:eq(1)").remove();
                            var trHTML = "<tr><td>1</td><td>"
                                    + drug.drugNo + "</td><td>"
                                    + drug.drugName + "</td><td>"
                                    + drug.specification + "</td><td>"
                                    + drug.purchasePrice + "</td><td>"
                                    + drug.sellingPrice + "</td><td>"
                                    + drug.number + "</td><td>"
                                    + drug.origin + "</td>"
                                    + "<td><button type='button' " +
                                    "edit_drug_no='" + drug.drugNo + "' " +
                                    "edit_drug_origin='" + drug.origin + "' " +
                                    "edit_drug_name='" + drug.drugName + "' " +
                                    "edit_drug_spe='" + drug.specification + "' " +
                                    "edit_drug_purchase_price='" + drug.purchasePrice + "' " +
                                    "edit_drug_sell_price='" + drug.sellingPrice + "' " +
                                    "edit_drug_num='" + drug.number + "' " +
                                    "class='btn btn-default edit_btn'>修改</button></td></tr>"
                            $("#drug_tab").append(trHTML);
                            $(".edit_btn").click(edit_func);
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("查询药品错误,请检查查询条件是否正确!");
                    }
                });
            };

            var edit_func = function () {
                $("#drugNoEdit").val($(this).attr('edit_drug_no'));
                $("#drugOriginalEdit").val($(this).attr('edit_drug_origin'));
                $("#drugNameEdit").val($(this).attr('edit_drug_name'));
                $("#purchasePriceEdit").val($(this).attr('edit_drug_purchase_price'));
                $("#sellingPriceEdit").val($(this).attr('edit_drug_sell_price'));
                $("#drugNumEdit").val($(this).attr('edit_drug_num'));
                $("#drugSpeEdit").val($(this).attr('edit_drug_spe'));
                $("#editModal").modal("show");
            }

            //$("#drugQryBtn").click(qryFun);
            document.onkeydown = function (e) {
                var ev = document.all ? window.event : e;
                if (ev.keyCode == 13) {
                    console.log(e.srcElement.id);
                    if(e.srcElement.id=='drugNo'){
                        /*
                         if(!$("#qry_form").validate(validate_drugno_config).form()){
                         return false;
                         }
                         */
                        $("#drugNum").focus().select();
                        qryFun();
                    }
                    if(e.srcElement.id=='drugNum'){
                        $("#reg_btn").click();
                    }

                }
            }

            $("#edit_drug").click(function () {
                if(!$("#edit_drug_form").validate(validate_edit_config).form()){
                    return false;
                }
                data = {
                    "drugNoAdd": $("#drugNoEdit").val(),
                    "drugOriginalAdd": $("#drugOriginalEdit").val(),
                    "drugNameAdd": $("#drugNameEdit").val(),
                    "number": $("#drugNumEdit").val(),
                    "price": $("#purchasePriceEdit").val(),
                    "drugSellPriceAdd": $("#sellingPriceEdit").val(),
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
                            qryFun();
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("修改药品信息错误,请检查配置信息是否正确!");
                    }
                });
            });

            $("#reg_btn").click(function () {
                if(!$("#qry_form").validate(validate_config).form()){
                    return false;
                }
                if($('#drugNo_errorinfo').text()!=''){
                    return false;
                }
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
                            $("#drugNum").val('0');
                            $("#drugNo").select();
                            qryFun();
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("库存增加错误,请检查配置信息是否正确!");
                    }
                });
            });

            var validate_config = {
                rules: {
                    drugNum: {
                        required: true,
                        number:true
                    },
                    drugNo:{
                        required: true
                    }
                },
                messages: {
                    drugNum: {
                        required: "请输入进货数量",
                        number:"进货数量必须为数字"
                    },
                    drugNo:{
                        required: "请输入药品编号"
                    }
                },
                errorPlacement:function(error,element){
                    console.log(element.attr('id'));
                    $('#'+element.attr('id')+'_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

            var validate_add_config = {
                rules: {
                    drugNoAdd: {
                        required: true
                    },
                    drugNameAdd: {
                        required: true
                    },
                    drugSpeAdd: {
                        required: true
                    },
                    drugOriginalAdd: {
                        required: true
                    },
                    drugPriceAdd: {
                        required: true,
                        number:true
                    },
                    drugSellPriceAdd:{
                        required: true,
                        number:true
                    }
                },
                messages: {
                    drugNoAdd: {
                        required: "必填选项"
                    },
                    drugNameAdd:{
                        required: "必填选项"
                    },
                    drugSpeAdd:{
                        required: "必填选项"
                    },
                    drugOriginalAdd:{
                        required: "必填选项"
                    },
                    drugPriceAdd:{
                        required: "必填选项",
                        number: "必须为数字"
                    },
                    drugSellPriceAdd:{
                        required: "必填选项",
                        number: "必须为数字"
                    }
                },
                errorPlacement:function(error,element){
                    console.log(element.attr('id'));
                    $('#'+element.attr('id')+'_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

            var validate_edit_config = {
                rules: {
                    drugNoEdit: {
                        required: true
                    },
                    drugNameEdit: {
                        required: true
                    },
                    drugSpeEdit: {
                        required: true
                    },
                    drugOriginalEdit: {
                        required: true
                    },
                    purchasePriceEdit: {
                        required: true,
                        number: true
                    },
                    drugNumEdit: {
                        required: true,
                        number: true
                    },
                    sellingPriceEdit:{
                        required: true,
                        number: true
                    }
                },
                messages: {
                    drugNoEdit: {
                        required: "必填选项"
                    },
                    drugNameEdit:{
                        required: "必填选项"
                    },
                    drugSpeEdit:{
                        required: "必填选项"
                    },
                    drugOriginalEdit:{
                        required: "必填选项"
                    },
                    purchasePriceEdit:{
                        required: "必填选项",
                        number: "必须为数字"
                    },
                    drugNumEdit:{
                        required: "必填选项",
                        number: "必须为数字"
                    },
                    sellingPriceEdit:{
                        required: "必填选项",
                        number: "必须为数字"
                    }
                },
                errorPlacement:function(error,element){
                    console.log(element.attr('id'));
                    $('#'+element.attr('id')+'_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

            var validate_drugno_config = {
                rules: {
                    drugNo:{
                        required: true
                    }
                },
                messages: {
                    drugNo:{
                        required: "请输入药品编号"
                    }
                },
                errorPlacement:function(error,element){
                    console.log(element.attr('id'));
                    $('#'+element.attr('id')+'_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

            var validate_drugnum_config = {
                rules: {
                    drugNum: {
                        required: true,
                        digits:true
                    }
                },
                messages: {
                    drugNum: {
                        required: "请输入进货数量",
                        digits:"进货数量必须为数字"
                    }
                },
                errorPlacement:function(error,element){
                    console.log(element.attr('id'));
                    $('#'+element.attr('id')+'_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

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
<div align="center" id="main_div">
    <form role="form" id="qry_form" class="form-inline" style="align-content: center;width: 100%;">
        <div class="form-group">
            <label for="drugNo">药品编号</label>

            <input type="text" id="drugNo" name="drugNo" class="form-control" data-provide="typeahead">
            <!--button type="button" id="drugQryBtn" class="btn btn-default">查询</button-->
            <span id="drugNo_errorinfo" class="form-group error_info"></span>
        </div>

        <div class="form-group">
            <label for="drugNum">进货数量</label>

            <input type="text" id="drugNum" name="drugNum" class="form-control">
            <label id="drugNum_errorinfo" class="error_info"><label>

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
                <form role="form" class="form-inline" id="add_drug_form" style="align-content: center;width: 100%;">
                    <div class="form-group">
                        <label for="drugNoAdd">药品编号</label>
                        <input type="text" id="drugNoAdd" name="drugNoAdd" class="form-control">
                        <span id="drugNoAdd_errorinfo" class="error_info"></span>
                    </div>

                    <div class="form-group">
                        <label for="drugNameAdd">药品名称</label>
                        <input type="text" id="drugNameAdd" name="drugNameAdd" class="form-control">
                        <span id="drugNameAdd_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="drugSpeAdd">药品规格</label>
                        <input type="text" id="drugSpeAdd" name="drugSpeAdd" class="form-control">
                        <span id="drugSpeAdd_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="drugOriginalAdd">药品产地</label>
                        <input type="text" id="drugOriginalAdd" name="drugOriginalAdd" class="form-control">
                        <span id="drugOriginalAdd_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="drugPriceAdd">含税进价</label>
                        <input type="text" id="drugPriceAdd" name="drugPriceAdd" class="form-control">
                        <span id="drugPriceAdd_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="drugSellPriceAdd">零售价格</label>
                        <input type="text" id="drugSellPriceAdd" name="drugSellPriceAdd" class="form-control">
                        <span id="drugSellPriceAdd_errorinfo" class="error_info"></span>
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
                <form role="form" class="form-inline" id="edit_drug_form" style="align-content: center;width: 100%;">
                    <div class="form-group">
                        <label for="drugNoEdit">药品编号</label>
                        <input type="text" id="drugNoEdit" class="form-control" readonly>
                    </div>

                    <div class="form-group">
                        <label for="drugNameEdit">药品名称</label>
                        <input type="text" id="drugNameEdit" name="drugNameEdit" class="form-control">
                        <span id="drugNameEdit_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="drugSpeEdit">药品规格</label>
                        <input type="text" id="drugSpeEdit" name="drugSpeEdit" class="form-control">
                        <span id="drugSpeEdit_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="drugOriginalEdit">药品产地</label>
                        <input type="text" id="drugOriginalEdit" name="drugOriginalEdit" class="form-control">
                        <span id="drugOriginalEdit_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="purchasePriceEdit">含税进价</label>
                        <input type="text" id="purchasePriceEdit" name="purchasePriceEdit" class="form-control">
                        <span id="purchasePriceEdit_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="sellingPriceEdit">零售价格</label>
                        <input type="text" id="sellingPriceEdit" name="sellingPriceEdit" class="form-control">
                        <span id="sellingPriceEdit_errorinfo" class="error_info"></span>
                    </div>
                    <div class="form-group">
                        <label for="drugNumEdit">药品库存</label>
                        <input type="text" id="drugNumEdit" name="drugNumEdit" class="form-control">
                        <span id="drugNumEdit_errorinfo" class="error_info"></span>
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
