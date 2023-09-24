$(document).on("click", "#detailUser", function(){

    let user = {};
    user = JSON.parse(localStorage.getItem("user"));
    let userId = user.code;

    var url = "/user/retrieveUser/" + userId;    //url
    var data= {};                               //data
    var async = false;                         //비동기 여부(기본값 false)
    var sCallback = "sCallback";               //콜백이름(기본값 sCallback)

    commonAjax(url, data, async, sCallback);

});

var sCallback = function(url, data){

}

