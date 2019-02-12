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
        console.log("${pageContext.request.contextPath}");
        var contextPath = "${pageContext.request.contextPath}";
    </script>
    <script src="<%=request.getContextPath()%>/js/modules/batch_register.js" type="text/javascript"></script>

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
            <th>批次</th>
            <th>有效期</th>
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
            <td>
                <input class='checkInNo' id='checkInNo_1' type='text' style="width: 62px;"/>
            </td>
            <td>
                <input class='legalDate' id='legalDate_1' type='text' style="width: 62px;"/>
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
