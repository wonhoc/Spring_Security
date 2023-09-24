$(document).on("click", "#joinBtn", function(event){

    var url = "/common/sign-up";       //url
    var data= consultData();            //data
    var sMethod = "post"                //method
    var fwdId = "joinUser";             // fwdId
    var async = false;                  //비동기 여부(기본값 false)
    var sCallback = "sCallback" ;       //콜백이름(기본값 sCallback)

    event.preventDefault();

    commonAjax(url, sMethod, fwdId, data, async, sCallback);
});

var sCallback = function (fwdId, data){
    var url = "/common/loginForm";
    var sMethod = "get";
    var fwdId = "logInForm";
    var data;

    location.href = "/common/loginForm";
}

var consultData = function (){
    var cData = {
        email:       $('#email').val(),
        password:    $('#password').val(),
        nickname:    $('#nickname').val(),
        phoneNumber: $('#phoneNumber').val(),
        loginMethod: "default"
    };

    return cData;
}
