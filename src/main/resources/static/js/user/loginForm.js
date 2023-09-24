$(document).on("click", ".joinUser", function(){

    location.href= "/joinUserForm";
});

$(document).on("click", ".loginBtn", function(){

    var url = "/login";         //url
    var data= consultData();    //data
    var async = false;          //비동기 여부(기본값 false)

    $.ajax({
        type: "post",
        url: url,
        data: JSON.stringify(data),
        dataType : 'json',
        contentType: 'application/json;charset=utf-8',
        async: async,
        success: function (data, textStatus, request, xhr){
            if(data.success){

                localStorage.setItem("Authorization", request.getResponseHeader("Authorization"));
                localStorage.setItem("Authorization-refresh", request.getResponseHeader("Authorization-refresh"));

                var url = '/user/main';
                var sMethod = 'post';
                var fwdId = 'main';
                var sCallbcak = "fnCallback"

                console.log("asdasddsa");

                commonAjax(url, sMethod, fwdId, null, true, sCallbcak);

            }else{
                location.href = "loginForm";
            }
        },
        error: function (error){
            console.log(error);
        }
    });

});

var consultData = function(){
    var data = {
        email :  $('#email').val(),
        password:   $('#password').val()
    };

    return data;
}

var fnCallback = function (url, data){

}

$(document).on("click", "#joinBtn", function(){

    location.href = "/common/joinUserForm";
});