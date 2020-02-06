function initCss() {
    $('.navbar').addClass('fixed-top');
    $('body').css("padding-top", "7rem");
}

function logout() {
    $.post("/passport/logout", function (result) {
        if (result.status == 200) {
            location.href = '/home';
        }
        else if (result.status == 400) {
            confirm(result.msg);
            location.href="/home";
        } else if (result.status == 500) {
            alert("服务器异常");
        }
    });
}

$.ajaxSetup({
    complete: function (xhr, status) {
        if ("REDIRECT" == xhr.getResponseHeader("REDIRECT")) { //若HEADER中含有REDIRECT说明后端想重定向，
            var win = window;
            while (win != win.top) {
                win = win.top;
            }
            win.location.href = xhr.getResponseHeader("Location");//将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
        }
    }
});

function loginModal() {
    $("#login-modal").modal('show');
}