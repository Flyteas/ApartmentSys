(function() {
    /* 公寓选择 */
    $("#aptSelect").bsSuggest({
    	url: "ApartmentSelect.do?topParam=100&kw=",//返回前100条
        showHeader: true,
        allowNoKeyword: true,
        searchFields: [ "id","name","address"],
        getDataMethod: "url",
        autoSelect: false,
        showBtn: true,
        idField: "id",
        keyField: "name",
        fnPreprocessKeyword: function(keyword) { //请求数据前，对输入关键字作进一步处理方法。注意，应返回字符串
        	empId = $('#empId').val();
        	if(empId == null || empId == "")
        	{
        		return keyword;
        	}
            return keyword + "&empId=" + empId; //对请求参数预处理
        },
    }).on('onDataRequestSuccess', function (e, result) {
    	
    }).on('onSetSelectValue', function (e, keyword, data) {
    	$('#aptId').val(data.id); //设置公寓选择的aptId
    	
    }).on('onUnsetSelectValue', function () {
    	
    });

}());
