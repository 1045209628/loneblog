function uploadPicture() {
    var files = $('#picture')[0].files;
    if (files.length > 1) {
        alert('只能选择一张图片');
    }
    var pic = files[0];
    console.log(pic);
    var formData = new FormData();
    formData.append("picture", pic);
    formData.append("type", 1);
    formData.append("useId", sessionUserId);
    console.log(formData);
    if (pic) {
        $.ajax({
            url: "/picture/upload",
            processData: false,
            data: formData,
            type: "post",
            contentType: false,
            success: function (result) {
                if (result.status == 200) {
                    $('img').attr('src', result.url);
                    alert("上传成功");
                }
                else {
                    alert('上传失败');
                }
            },
            error: function () {
                alert("上传失败");
            }
        })
    }
    else {
        alert('未选择图片');
    }
};

function showImg(dom) {
    $parent = $(dom).parents(".avatarWrapper");
    var reader = new FileReader();
    var picture = dom.files[0];
    if (dom.files.length > 1) {
        alert("只能选择一张图片");
    }
    if (picture) {
        reader.readAsDataURL(picture);
        $parent.find(".uploadSpan").hide();
        reader.onload = function (e) {
            $('#upload_avatar')[0].src = this.result;
            $("#upload_avatar").show();
        }


    }
    else {
        console.log("读取文件失败");
    }
}

function userbarScroll() {

    var barOffTop = $("#userbar").offset().top;//和顶部的距离
    var fixedTop = $('.navbar').outerHeight(true);
    var margin = parseInt($('#userbar').css('margin-top'));
    var originWidth = $("#userbar").outerWidth();
    //console.log(prewidth);
    var scTop = 0;//初始化垂直滚动的距离
    $(document).scroll(function () {
        scTop = $(this).scrollTop();//获取到滚动条拉动的距离
        //console.log(scTop);//查看滚动时，垂直方向上，滚动条滚动的距离
        if (scTop >= barOffTop - fixedTop - margin) {
            //核心部分：当滚动条拉动的距离大于等于导航栏距离顶部的距离时，添加指定的样式
            $("#userbar").css({
                position: "fixed",
                top: fixedTop,
                width: originWidth
            });
        } else {
            $("#userbar").css({
                position: "",
                top: "",
            });
        }

    });
}

function uploadUserDesc() {
    var textarea = $('textarea').first();
    $.ajax({
        type: "put",
        data: {
            description: textarea.val().trim()
        },
        url: "/user/" + userId,
        success: function (result) {
            if (result.status == 200) {
                alert("修改成功");
            }
            else {
                console.warn("错误或异常");
            }
            location.href = '/user/setting';
        },
        error: function (xhr, status, errors) {
            alert("发送失败");
            console.error(errors);
        }


    });

}