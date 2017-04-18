(function() {
    /* 员工选择 */
    $("#empSelect").bsSuggest({
    	url: "EmployeeSelect.do?topParam=100&kw=",//返回前100条
        showHeader: true,
        allowNoKeyword: true,
        searchFields: [ "empId","name","phone"],
        getDataMethod: "url",
        autoSelect: false,
        showBtn: true,
        idField: "empId",
        keyField: "name",
        fnPreprocessKeyword: function(keyword) { //请求数据前，对输入关键字作进一步处理方法。注意，应返回字符串
        	aptId = $('#aptId').val();
        	if(aptId == null || aptId == "")
        	{
        		return keyword;
        	}
            return keyword + "&aptId=" + aptId; //对请求参数预处理
        },
    }).on('onDataRequestSuccess', function (e, result) {
    	
    }).on('onSetSelectValue', function (e, keyword, data) {
    	$('#empId').val(data.empId); //设置员工选择的empId
    	
    }).on('onUnsetSelectValue', function () {
    	
    });

}());
