(function() {
	/* 员工选择 */
    $("#empSelect").bsSuggest({
        url: "EmployeeSelect.do?topParam=100&kw=", //返回前100条
        showHeader: true,
        allowNoKeyword: true,
        searchFields: [ "empId","name","phone"],
        getDataMethod: "url",
        autoSelect: false,
        showBtn: true,
        idField: "empId",
        keyField: "name"
    }).on('onDataRequestSuccess', function (e, result) {
    	
    }).on('onSetSelectValue', function (e, keyword, data) {
        $('#empId').val(data.empId); //设置员工选择的empId
        
    }).on('onUnsetSelectValue', function () {
    	
    });

}());
