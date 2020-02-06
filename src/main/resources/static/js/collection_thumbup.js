function diggClick(digg) {
    var behaiver = digg;
    var btn = behaiver.children('.behaiver_btn');
    var zi = behaiver.find('i');
    var addNum = behaiver.children('.add_num');
    var text = behaiver.children('.behaiver_text');
    var count = parseInt(text.text());
    //console.log(addNum);
    if (articleId == -1 || articleId == null) {
        //console.log("articleId is null");
        throw error("articleId is null");
    }
    if (userId == -1 || userId == null) {
        userId = undefined;
    }
    if (btn.data('used') == false) {//未点击状态
        $.post('/thumbup/save', { userId, articleId }, function (result) {
            if (result.status == 200) {
                zi.removeClass('zi_digg').addClass('zi_thumbsup');
                addNum.html("<em class='add_animation'>+1</em>").show();
                count++;
                text.text(count + "点赞");
                btn.data('used', true);
            } else {
                alert("服务器发生异常：\n");
                if (result.status == 500)
                    throw new Error(result.msg);
            }
        });
    }
    else { //已点击状态
        $.post('/thumbup/cancel', { userId, articleId }, function (result) {
            if (result.status == 200) {
                zi.addClass('zi_digg').removeClass('zi_thumbsup');
                addNum.html("<em class='add_animation'>-1</em>").show();
                count--;
                text.text(count + "点赞");
                btn.data('used', false);
            } else {
                alert("服务器发生异常：\n" + result.msg);
            }
        });

    }
}

function collectionClick(collection) {
    var behaiver = collection;
    var btn = behaiver.children('.behaiver_btn');
    var zi = behaiver.find('i');
    var addNum = behaiver.children('.add_num');
    var text = behaiver.children('.behaiver_text');
    var count = parseInt(text.text());
    //console.log(addNum);
    if (articleId == -1 || articleId == null) {
        //console.log("articleId is null");
        throw error("articleId is null");
    }
    if (userId == -1 || userId == null) {
        userId = undefined;
    }
    if (btn.data('used') == false) {//未点击状态
        $.post('/collection/save', { userId, articleId }, function (result) {
            if (result.status == 200) {
                zi.removeClass('zi_starLine').addClass('zi_star');
                addNum.html("<em class='add_animation'>+1</em>").show();
                count++;
                text.text(count + "收藏");
                btn.data('used', true);
            } else {
                alert("服务器发生异常：\n");
                if (result.status == 500)
                    throw new Error(result.msg);
            }
        });
    }
    else { //已点击状态
        $.post('/collection/cancel', { userId, articleId }, function (result) {
            if (result.status == 200) {
                zi.addClass('zi_starLine').removeClass('zi_star');
                addNum.html("<em class='add_animation'>-1</em>").show();
                count--;
                text.text(count + "收藏");
                btn.data('used', false);
            } else {
                alert("服务器发生异常：\n" + result.msg);
            }
        });

    }
}

$('#digg').click(function () {
    var $this = $(this);
    $.get("/passport/check", function (result) {
        if (result.status == 200) {
            diggClick($this);
        }
        else if (result.status == 400) {
            location.href = '/passport/login/page';
        } else {
            alert("服务器发生异常");
            location.href = "/home";
        }
    });
});
$('#collection').click(function () {
    var $this = $(this);
    $.get("/passport/check", function (result) {
        if (result.status == 200) {
            collectionClick($this);
        }
        else if (result.status == 400) {
            location.href = '/passport/login/page';
        } else {
            alert("服务器发生异常");
            location.href = "/home";
        }
    });
});

function setCollection() {
    if (userId == null || userId == -1) return;
    var behaiver = $('#collection');
    var btn = behaiver.children('.behaiver_btn');
    var zi = behaiver.find('i');
    $.get("/collection/" + userId + "/" + articleId, function (result) {
        if (result.status == 200) {
            btn.data('used',true);
            zi.removeClass('zi_starLine').addClass('zi_star');
        }
        else if (result.status == 400) {

        }
        else {
            console.log(result.data);
        }
    });
}

function setDigg() {
    if (userId == null || userId == -1) return;
    var behaiver = $('#digg');
    var btn = behaiver.children('.behaiver_btn');
    var zi = behaiver.find('i');
    $.get("/thumbup/" + userId + "/" + articleId, function (result) {
        if (result.status == 200) {
            btn.data('used',true);
            zi.removeClass('zi_digg').addClass('zi_thumbsup');
        }
        else if (result.status == 400) {

        }
        else {
            console.log(result.data);
        }
    });
}
