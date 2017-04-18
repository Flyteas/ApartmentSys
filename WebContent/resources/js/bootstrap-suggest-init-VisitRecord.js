(function() {
	/* 公寓楼选择 */
    $("#apartmentSelect").bsSuggest({
        url: "ApartmentSelect.do?topParam=100&kw=",//返回前100条
        showHeader: true,
        allowNoKeyword: true,
        searchFields: [ "id","name","address"],
        getDataMethod: "url",
        autoSelect: false,
        showBtn: true,
        idField: "id",
        keyField: "name"
    }).on('onDataRequestSuccess', function (e, result) {
    	
    }).on('onSetSelectValue', function (e, keyword, data) {
        $('#aptId').val(data.id); //设置公寓选择的aptId
        
    }).on('onUnsetSelectValue', function () {
    	
    });
    
    /* 房间选择 */
    $("#roomSelect").bsSuggest({
        url: "RoomSelect.do?topParam=100&kw=", //返回前100条
        showHeader: true,
        allowNoKeyword: true,
        searchFields: [ "id","name"],
        getDataMethod: "url",
        autoSelect: false,
        showBtn: true,
        idField: "id",
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
    	 $('#roomId').val(data.id); //设置房间选择的aptId
    	 
    }).on('onUnsetSelectValue', function () {
    	
    });

}());
