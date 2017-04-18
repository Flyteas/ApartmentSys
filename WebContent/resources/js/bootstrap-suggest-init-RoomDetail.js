(function() {
	/* 学生选择 */
    $("#stuSelect").bsSuggest({
        url: "StudentSelect.do?topParam=100&kw=", //返回前100条
        showHeader: true,
        allowNoKeyword: true,
        searchFields: [ "stuId","name","phone"],
        getDataMethod: "url",
        autoSelect: false,
        showBtn: true,
        idField: "stuId",
        keyField: "name"
    }).on('onDataRequestSuccess', function (e, result) {
    	
    }).on('onSetSelectValue', function (e, keyword, data) {
        $('#stuId').val(data.stuId); //设置学生选择的stuId
        
    }).on('onUnsetSelectValue', function () {
    	
    });

}());
