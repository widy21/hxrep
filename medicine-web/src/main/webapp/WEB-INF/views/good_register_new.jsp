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
        var name2Id = {};//对应关系对象
        var spell_array = [];
        $(document).ready(function () {
            var getSpellInfo = function () {
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
                                var key = ele.pinyin + ' 【' + ele.drugName + '】'+ ' 【' + ele.drugNo + '】';
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

            $('.drugNo').typeahead({
                source: function (query, process) {
                    process(spell_array);
                },
                updater: function (item) {
                    console.log(name2Id[item]);//打印对应的id
                    $("#drugNo").val(name2Id[item]);
                    return name2Id[item];
                }
            });

            $('.drugNo').change(function () {
                var index = $(this).parent().parent().index();
                qryFun($(this).val(), index);
                console.log($(this).parent().parent().index());
            });

            $(".registNo").bind("input propertychange", function () {
                var purchasePrice = parseFloat($(this).parent().parent().find("td").eq(4).text());
                var registNum = parseInt($(this).val());
//                console.log($(this).parent().parent().html());
                console.log(purchasePrice);
                console.log(registNum);
                if ($.trim($(this).val()) != '' && isNaN(registNum)) {
                    alert('进货数量输入错误');
                    return false;
                }
                $(this).parent().parent().find("td").eq(9).text(parseFloat(purchasePrice * registNum).toFixed(2));
                //计算总金额
                caculateTotalAmount();
            });

            $("#save_new_drug").click(function () {
                if (!$("#add_drug_form").validate(validate_add_config).form()) {
                    return false;
                }
                $(this).attr({"disabled": "disabled"});
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

            //$("#drugQryBtn").click(qryFun);
            document.onkeydown = function (e) {
                var ev = document.all ? window.event : e;
                if (ev.keyCode == 13) {
                    console.log(e.srcElement.id);
                    if (e.srcElement.id.match('registNo_') != null) {
                        draw_tr($("#drug_tab tr:last").find("td").eq(8).find("input"));
                    }
                }
            }

            $("#edit_drug").click(function () {
                if (!$("#edit_drug_form").validate(validate_edit_config).form()) {
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
                            var index = $("#drugEditIndex").val();
                            qryFun($("#drugNoEdit").val(), index);
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("修改药品信息错误,请检查配置信息是否正确!");
                    }
                });
            });

            $("#batch_regist_btn").click(function () {
                var drugNo = $("#drug_tab tr").eq(1).find("td").eq(1).find("input").val();
                var registNum = $("#drug_tab tr").eq(1).find("td").eq(8).find("input").val();
                if ($.trim(drugNo) == '' || $.trim(registNum) == '') {
                    alert('药品信息为空');
                    return false;
                }
                if (isNaN(parseInt(registNum))) {
                    alert('进货数量输入错误');
                    return false;
                }

                var regist_obj_array = [];
                var check_flag = true;
                $("#drug_tab tr:gt(0)").each(function () {
                    var regist_obj = {};
                    regist_obj.drug_no = $(this).find("td").eq(1).find("input").val();
                    regist_obj.regist_num = $(this).find("td").eq(8).find("input").val();
                    if (isNaN(parseInt(regist_obj.regist_num))) {
                        check_flag = false;
                    }
                    regist_obj_array.push(regist_obj);
                });
                if (!check_flag) {
                    alert('进货数量输入错误');
                    return false;
                }
                console.log('regist_obj_array --> ' + JSON.stringify(regist_obj_array));
                $.ajax({
                    type: "POST",  //提交方式
                    url: "${pageContext.request.contextPath}/med/batch_add_drugNum",//路径
                    'contentType': 'application/json',
                    'dataType': 'json',
                    //数据，这里使用的是Json格式进行传输
                    'data': JSON.stringify(regist_obj_array),
                    success: function (result) {//返回数据根据结果进行相应的处理
                        if (result.update_flag == "false") {
                            alert("库存增加失败!");
                        } else {
                            alert("库存增加成功!");
//                            $("#drug_tab tr:gt(1)").remove();
                            window.location.reload();
                        }
                    },
                    error: function (result) {
                        console.log(result.responseText);
                        alert("库存批量增加错误,请检查配置信息是否正确!");
                    }
                });
            });

            //批量删除表格数据
            $("#batch_del_btn").click(function () {
//                $("#drug_tab tr:gt(1)").remove();
//                $("#drug_tab input").val('');
//                $("#drug_tab tr").eq(1).find("td:gt(1):lt(8)").text('');
                window.location.reload();
            });

            var validate_config = {
                rules: {
                    drugNum: {
                        required: true,
                        number: true
                    },
                    drugNo: {
                        required: true
                    }
                },
                messages: {
                    drugNum: {
                        required: "请输入进货数量",
                        number: "进货数量必须为数字"
                    },
                    drugNo: {
                        required: "请输入药品编号"
                    }
                },
                errorPlacement: function (error, element) {
                    console.log(element.attr('id'));
                    $('#' + element.attr('id') + '_errorinfo').append(error);
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
                        number: true
                    },
                    drugSellPriceAdd: {
                        required: true,
                        number: true
                    }
                },
                messages: {
                    drugNoAdd: {
                        required: "必填选项"
                    },
                    drugNameAdd: {
                        required: "必填选项"
                    },
                    drugSpeAdd: {
                        required: "必填选项"
                    },
                    drugOriginalAdd: {
                        required: "必填选项"
                    },
                    drugPriceAdd: {
                        required: "必填选项",
                        number: "必须为数字"
                    },
                    drugSellPriceAdd: {
                        required: "必填选项",
                        number: "必须为数字"
                    }
                },
                errorPlacement: function (error, element) {
                    console.log(element.attr('id'));
                    $('#' + element.attr('id') + '_errorinfo').append(error);
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
                    sellingPriceEdit: {
                        required: true,
                        number: true
                    }
                },
                messages: {
                    drugNoEdit: {
                        required: "必填选项"
                    },
                    drugNameEdit: {
                        required: "必填选项"
                    },
                    drugSpeEdit: {
                        required: "必填选项"
                    },
                    drugOriginalEdit: {
                        required: "必填选项"
                    },
                    purchasePriceEdit: {
                        required: "必填选项",
                        number: "必须为数字"
                    },
                    drugNumEdit: {
                        required: "必填选项",
                        number: "必须为数字"
                    },
                    sellingPriceEdit: {
                        required: "必填选项",
                        number: "必须为数字"
                    }
                },
                errorPlacement: function (error, element) {
                    console.log(element.attr('id'));
                    $('#' + element.attr('id') + '_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

            var validate_drugno_config = {
                rules: {
                    drugNo: {
                        required: true
                    }
                },
                messages: {
                    drugNo: {
                        required: "请输入药品编号"
                    }
                },
                errorPlacement: function (error, element) {
                    console.log(element.attr('id'));
                    $('#' + element.attr('id') + '_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }

            var validate_drugnum_config = {
                rules: {
                    drugNum: {
                        required: true,
                        digits: true
                    }
                },
                messages: {
                    drugNum: {
                        required: "请输入进货数量",
                        digits: "进货数量必须为数字"
                    }
                },
                errorPlacement: function (error, element) {
                    console.log(element.attr('id'));
                    $('#' + element.attr('id') + '_errorinfo').append(error);
                    console.log(error.innerText);
                }
            }
        });

        function draw_tr(obj) {
            $("#drug_tab").append($(obj).parent().parent().clone(false));
            var index = $("#drug_tab tr:last").index();
            console.log(index);
            $("#drug_tab tr").eq(index).find("td").eq(8).find("input").attr("id", "registNo_" + index);
            $("#drug_tab tr").eq(index).find("td").eq(0).text(index);
            $("#drug_tab tr").eq(index).find("td").eq(1).find("input").focus().select();

            $('.drugNo').typeahead({
                source: function (query, process) {
                    process(spell_array);
                },
                updater: function (item) {
                    console.log(name2Id[item]);//打印对应的id
//                    $("#drugNo").val(name2Id[item]);
                    var index = $(obj).parent().parent().index();
                    return name2Id[item];
                }
            });

            $('.drugNo').change(function () {
                var index = $(this).parent().parent().index();
                qryFun($(this).val(), index);
//                console.log($(this).parent().html());
                console.log($(this).parent().parent().index());
            });

            //计算总金额
            caculateTotalAmount();

            $(".registNo").bind("input propertychange", function () {
                var purchasePrice = parseFloat($(this).parent().parent().find("td").eq(4).text());
                var registNum = parseInt($(this).val());
//                console.log($(this).parent().parent().html());
                console.log(purchasePrice);
                console.log(registNum);
                if ($.trim($(this).val()) != '' && isNaN(registNum)) {
                    alert('进货数量输入错误');
                    return false;
                }
                $(this).parent().parent().find("td").eq(9).text(parseFloat(purchasePrice * registNum).toFixed(2));
                //计算总金额
                caculateTotalAmount();
            });
        }

        //删除一行记录
        function del_tr(obj) {
            var index = $(obj).parent().parent().index();
            //第一行需要被复制，不能删除
            if (index != 1) {
                $(obj).parent().parent().remove();
            } else {
                alert('第一行不能删除.');
            }
            //计算总金额
            caculateTotalAmount();
        }

        //修改一行记录
        function edit_tr(obj) {
            var index = $(obj).parent().parent().index();
//            var drugNo = $("#registNo_"+index).val();
            var drugNo = $(obj).parent().parent().find("td").eq(1).find("input").val();
            if ($.trim(drugNo) == '') {
                alert('药品编码为空');
                return false;
            }
            $("#drugNoEdit").val(drugNo);
            $("#drugOriginalEdit").val($(obj).parent().parent().find("td").eq(7).text());
            $("#drugNameEdit").val($(obj).parent().parent().find("td").eq(2).text());
            $("#purchasePriceEdit").val($(obj).parent().parent().find("td").eq(4).text());
            $("#sellingPriceEdit").val($(obj).parent().parent().find("td").eq(5).text());
            $("#drugNumEdit").val($(obj).parent().parent().find("td").eq(6).text());
            $("#drugSpeEdit").val($(obj).parent().parent().find("td").eq(3).text());
            $("#drugEditIndex").val(index);
            $("#editModal").modal("show");
        }


        function qryFun(drugNo, index) {
            data = {
                "drugNoAdd": drugNo
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
                        console.log(result.drug);
                        $("#drugNo_errorinfo").empty();
                        var drug = result.drug;
                        $("#drug_tab tr").eq(index).find("td").eq(2).text(drug.drugName);
                        $("#drug_tab tr").eq(index).find("td").eq(3).text(drug.specification);
                        $("#drug_tab tr").eq(index).find("td").eq(4).text(drug.purchasePrice);
                        $("#drug_tab tr").eq(index).find("td").eq(5).text(drug.sellingPrice);
                        $("#drug_tab tr").eq(index).find("td").eq(6).text(drug.number);
                        $("#drug_tab tr").eq(index).find("td").eq(7).text(drug.origin);
                        $("#drug_tab tr").eq(index).find("td").eq(8).find("input").focus().select();
                    }
                },
                error: function (result) {
                    console.log(result.responseText);
                    alert("查询药品错误,请检查查询条件是否正确!");
                }
            });
        }

        //计算总金额
        function caculateTotalAmount(){
            var total_amount = 0;
            $("#drug_tab tr:gt(0)").each(function () {
                var amount = parseFloat($(this).find("td").eq(9).text());
                if (!isNaN(amount)) {
                    total_amount += amount;
                }
            });
            console.log(total_amount.toFixed(2));
            $("#totalAmount").val(total_amount.toFixed(2));
        }
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
<br><br>

<div>
    <button type="button" id="add_btn" class="btn btn-primary" style="float:right;" data-toggle="modal"
            data-target="#myModal">新增药品
    </button>
    <button type="button" id="batch_regist_btn" class="btn btn-primary" style="float:left;" data-toggle="modal">批量入库
    </button>
    <button type="button" id="batch_del_btn" class="btn btn-primary" style="float:left;margin-left: 10px;"
            data-toggle="modal">全部删除
    </button>
    <table class="table" id="drug_tab">
        <tr>
            <th>行号</th>
            <th>药品编码</th>
            <th>药品名称</th>
            <th>药品规格</th>
            <th>含税进价(元)</th>
            <th>零售价格(元)</th>
            <th>库存</th>
            <th>产地</th>
            <th>进货数量</th>
            <th>进货金额</th>
            <th>操作</th>
        </tr>
        <tr>
            <td>1</td>
            <td>
                <input class='drugNo' type='text' style="width: 62px;"/>
            </td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td>
                <input class='registNo' id='registNo_1' type='text' style="width: 62px;"/>
            </td>
            <td class='registAmount'></td>
            <td>
                <button type='button' class='btn btn-default add_item_btn' onclick="draw_tr(this)"><i title="复制"
                                                                                                      class="glyphicon glyphicon-plus"></i>
                </button>
                <button type='button' class='btn btn-default edit_btn' onclick="edit_tr(this)"><i title="修改"
                                                                                                  class="glyphicon glyphicon-edit"></i>
                </button>
                <button type='button' class='btn btn-default del_btn' onclick="del_tr(this)"><i title="删除"
                                                                                                class="glyphicon glyphicon-minus"></i>
                </button>
            </td>
        </tr>
    </table>
    <div>
        <span style="font-weight: 600;font-size: medium">进货总金额：</span><input type="text" id="totalAmount" readonly/>(元)
    </div>
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
                    <input type="hidden" id='drugEditIndex'/>
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
