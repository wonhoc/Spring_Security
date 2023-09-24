var commonAjax = function (url, sMethod, fwdId, data, async, sCallback){

    if(async == null){
        async = false;
    }

    if(sCallback == null){
        sCallback = "sCallback";
    }

    $.ajax({
        type: sMethod,
        url: url,
        data: JSON.stringify(data),
        dataType : 'text',
        contentType: 'application/x-www-form-urlencoded',
        beforeSend(xhr) {
            xhr.setRequestHeader("Authorization", localStorage.getItem("Authorization"));
            xhr.setRequestHeader("Authorization-refresh", localStorage.getItem("Authorization-refresh"));
        },
        async: async,
        success: function (value, textStatus, request){


            window[sCallback](fwdId, value, textStatus, request);
        },
        error: function (error){
            console.log(error);
        }
    });

}

var fnNullCheck = function(str){

    if(new String(str).valueOf() == "undefined" || str == null) return true;

    var ch = new String(str);
    if(ch == null) return true;
    if(ch.toString().length == 0) return true;

    return false;
}
