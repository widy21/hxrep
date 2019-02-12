var name2Id = {};//对应关系对象
var spell_array = [];
$(document).ready(function () {

    var getSpellInfo = function () {
        $.ajax({
            type: "POST",  //提交方式
            url: contextPath + "/med/get_drug_spell_info",
            'contentType': 'application/json',
            'dataType': 'json',
            success: function (result) {//返回数据根据结果进行相应的处理
                if (result.query_flag == "false") {
                    alert("查询药品错误!");
                } else {
                    var drugs = result.allDrugSpell;
                    console.log(drugs.length);
                    $.each(drugs, function (index, ele) {
                        var key = ele.drugNo + '-' + ele.pinyin + ' 【' + ele.drugName + '】';
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
        $(this).parent().parent().find("td").eq(11).text(parseFloat(purchasePrice * registNum).toFixed(2));
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
            url: contextPath + "/med/save_new_drug",//路径
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
                var index = e.srcElement.id.split('_')[1];
                console.log('index = '+index);
                $("#checkInNo_"+index).focus().select();
            }
            if (e.srcElement.id.match('checkInNo_') != null) {
                var index = e.srcElement.id.split('_')[1];
                console.log('index = '+index);
                $("#legalDate_"+index).focus().select();
            }
            if (e.srcElement.id.match('legalDate_') != null) {
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
            url: contextPath + "/med/edit_drug",//路径
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
            regist_obj.checkIn_no = $(this).find("td").eq(9).find("input").val();
            regist_obj.legal_date = $(this).find("td").eq(10).find("input").val();
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
            url: contextPath + "/med/batch_add_drugNum",//路径
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
    $("#drug_tab tr").eq(index).find("td").eq(9).find("input").attr("id", "checkInNo_" + index);
    $("#drug_tab tr").eq(index).find("td").eq(10).find("input").attr("id", "legalDate_" + index);
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
        $(this).parent().parent().find("td").eq(11).text(parseFloat(purchasePrice * registNum).toFixed(2));
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
        url: contextPath + "/med/query_drug_byno",//路径
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
function caculateTotalAmount() {
    var total_amount = 0;
    $("#drug_tab tr:gt(0)").each(function () {
        var amount = parseFloat($(this).find("td").eq(11).text());
        if (!isNaN(amount)) {
            total_amount += amount;
        }
    });
    console.log(total_amount.toFixed(2));
    $("#totalAmount").val(total_amount.toFixed(2));
}