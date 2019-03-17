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
                "legalDate": $("#legalDate").val(),
                "checkInNo": $("#checkInNo").val(),
                "currentPage": pagenow
            }
            console.log('data= ',data);
            $.ajax({
                type: "POST",  //提交方式
                url: contextPath + "/med/drug_page_qry",//路径
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
                            + drug.createTime.substring(0,10) + "</td><td>"
                            + drug.checkInNo + "</td><td>"
                            + drug.legalDate + "</td>"

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