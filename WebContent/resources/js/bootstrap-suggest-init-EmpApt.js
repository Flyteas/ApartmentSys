(function() {
	/* 公寓选择 */
    $("#aptSelect").bsSuggest({
        url: "ApartmentSelect.do?topParam=100&kw=", //返回前100条
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
        $('#aptId').val(data.id); //设置公寓选择的ID
        
    }).on('onUnsetSelectValue', function () {
    	
    });

}());
