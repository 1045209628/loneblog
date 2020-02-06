var comment_paginator = {
    listDOM: null,
    pageDOM: null,
    setValue: function (opt) {
        this.listDOM = opt.listDOM;
        this.pageDOM = opt.pageDOM;
        this.articleId = opt.articleId;
        return this;
    },
    initCommentList: function (url) {
        this.getCommentList(url, 1);
    },
    getCommentList: function (url, pageNum) {
        var _this = this;
        var page = {
            page: pageNum
        }
        this.listDOM.html('正在加载...');
        this.pageDOM.empty();
        $.get(url, page, function (result) {
            if (result.status == 200) {
                var comments = result.data.list;
                _this.buildCommentList(comments);
                _this.buildCommentPageItem(result.data, url);
            } else if (result.status == 400) {
                _this.buildNullCommentList('服务器出错了!');
                console.warn(result.data);
            } else if (result.status == 500) {
                _this.buildNullCommentList('服务器出现异常!');
                throw new Error(result.data);
            }
        });
    },
    getReplyList: function (commentItem, cid, pageNum) {
        var _this = this;
        var page = {
            page: pageNum
        }
        var url = '/comment/' + cid + '/replies';
        var $commentItem = $(commentItem);
        $commentItem.find('.reply_list').first().html('正在加载...');
        $commentItem.find('.pagewrapper').empty();
        $.get(url, page, function (result) {
            if (result.status == 200) {
                var comment = {
                    replyInfo: result.data,
                    id: cid
                }
                _this.buildReplyList(commentItem, comment, false);
            } else if (result.status == 400) {
                _this.buildNullCommentList('服务器出错了!');
                console.warn(result.data);
            } else if (result.status == 500) {
                _this.buildNullCommentList('服务器出现异常!');
                throw new Error(result.data);
            }
        });
    },
    buildCommentList: function (comments) {
        var _this = this;
        this.listDOM.empty();
        $.each(comments, function (index, element) {
            var comment = comments[index];
            _this.buildCommentItem(comment);
        });
        this.pageDOM.show();
    },
    buildCommentItem: function (comment) {
        var _this = this;
        var html = '<div class="comment_item" data-comment-id=' + comment.id + ' >' +
            '<div class="user_avatar">' +
            '<a href="#">' +
            '<img class="imgCir" src="https://apic.douyucdn.cn/upload/avatar_v3/201910/d8fd40047f5643faaf7f38942d8dbae5_middle.jpg"/>' +
            '</a>' +
            '</div>' +
            '<div class="com_container">' +
            '<div class="username" data-user-id=' + comment.userId + ' >' + comment.username + '</div>' +
            '<p>' + comment.content + '</p>' +
            '<div class="statitic">' +
            '<span class="mr-1">' + new Date(comment.createTime).toLocaleString() + '</span>' +
            '<span class="reply_btn" onclick="addCommentSend(this);">回复</span>' +
            '</div>' +
            '<div class="reply_list">' +
            '</div>' +
            '<ul class="pagewrapper"></ul>' +
            '</div></div>';
        var commentDOM = $(html);

        //构造回复列表
        _this.buildReplyList(commentDOM, comment, true);

        /*commentDOM.hover(function () {
            commentDOM.toggleClass('selected');
        }, function () {
            commentDOM.toggleClass('selected');
        }).appendTo(this.listDOM);*/
        this.listDOM.append(commentDOM);
    },
    buildReplyList: function (parent, comment, isOrigin) {
        var _this = this;
        var $parent = $(parent);
        var reply_list = $parent.find('.reply_list').first();
        var reply_page = $parent.find('.pagewrapper').first();
        var replies = comment.replyInfo.list;
        var url = '/comment/' + comment.id + '/replies';
        reply_list.empty();
        $.each(replies, function (index, element) {
            var reply = replies[index];
            _this.buildReplyItem(reply_list, reply);
        });
        if (isOrigin) {
            if (comment.replyInfo.total > comment.replyInfo.pageSize) {
                var html = '<div class="view_more">' +
                    '<span>共<span style="font-weight: bold;">' + comment.replyInfo.total + '</span>条回复,</span>' +
                    '<span class="reply_btn ml-1" onclick="loadReplyList(this);" >点击查看</span></div>';
                $(html).appendTo(reply_list);
            }
            reply_page.hide();
        }
        else {
            reply_page.show();
            _this.buildReplyPageItem($parent, comment);
        }
    },
    buildReplyItem: function (parent, reply) {
        var html = '<div class="reply_item" data-comment-id=' + reply.id + ' >' +
            '<div class="user_avatar">' +
            '<a href="#">' +
            '<img class="imgCir" src="https://apic.douyucdn.cn/upload/avatar_v3/201910/d8fd40047f5643faaf7f38942d8dbae5_middle.jpg"/>' +
            '</a>' +
            '</div>' +
            '<div class="com_container">' +
            '<p>' +
            '<span class="username" data-user-id=' + reply.userId + ' >' + reply.username + '</span>' +
            '<span class="mx-2">回复</span>' +
            '<span class="username" data-replyuser-id=' + reply.replyUserId + ' >' + reply.replyname + '</span><span class="mx-1">:</span>' +
            '<span>' + reply.content + '</span>' +
            '</p>' +
            '<div class="statitic">' +
            '<span class="mr-1">' + new Date(reply.createTime).toLocaleString() + '</span>' +
            '<span class="reply_btn" onclick="addCommentSend(this);">回复</span>' +
            '</div></div></div>';
        $(parent).append($(html));
    },
    buildNullCommentList: function (alertmsg) {
        var html = '<h3 class="text-center my-1" style="color: gray;">目前没有评论</h3>';
        this.listDOM.html(html);
        this.pageDOM.hide();
        if (alertmsg != null)
            alert(alertmsg);
    },
    buildReplyPageItem: function (parent, comment) {
        var _this = this;
        var pageInfo = comment.replyInfo;
        $(parent).find('.pagewrapper').pagination({
            cssStyle: 'light-theme',
            prevText: '<span aria-hidden="true">&laquo;</span>',
            nextText: '<span aria-hidden="true">&raquo;</span>',
            itemsOnPage: pageInfo.pageSize,
            items: pageInfo.total,
            currentPage: pageInfo.pageNum,
            hrefTextPrefix: '#list',
            onPageClick: function (pageNum, event) {
                _this.getReplyList($(parent), comment.id, pageNum);
            }
        })
    },
    buildCommentPageItem: function (pageInfo, url) {
        var _this = this;
        this.pageDOM.pagination({
            cssStyle: 'light-theme',
            prevText: '<span aria-hidden="true">&laquo;</span>',
            nextText: '<span aria-hidden="true">&raquo;</span>',
            itemsOnPage: pageInfo.pageSize,
            items: pageInfo.total,
            currentPage: pageInfo.pageNum,
            hrefTextPrefix: '#list',
            onPageClick: function (pageNum, event) {
                //getCommentList(url,pageNum);
                _this.getCommentList(url, pageNum);
            }
        })
    }

}

function loadReplyList(dom) {
    var $this = $(dom);
    var comment_item = $this.parents('.comment_item');
    var cid = comment_item.data('comment-id');
    comment_paginator.getReplyList(comment_item, cid, 1);
}

function addCommentSend(dom) {

    var $this = $(dom);
    var list = $('#comment_list');
    var comment_item = $this.parents('.comment_item');
    var container = comment_item.children('.com_container');
    var send = $('.comment_send').clone(true).first();
    var user = $this.parentsUntil(".reply_item,.commtent_item").find('[data-user-id]').first();
    var username = user.text();
    var root = comment_item.data('comment-id');
    var ruid = user.data('user-id');
    var rid = $this.parents('.reply_item').data('comment-id') || root;//reply不存在即回复comment
    send.find('textarea').attr('placeholder', '回复  ' + username)
        .data('reply-id', rid)
        .data('reply-user-id', ruid);
    var preid = list.find('.comment_send').find('textarea').data('reply-id');

    //list只能存在一个回复框

    list.find('.comment_send').remove();
    //list不存在或回复不同对象时添加
    if (rid !== preid) {
        container.append(send);
    }

}

function SendComment(DOM) {
    $this = $(DOM);
    var comment_item = $this.parents('.comment_item');
    var textarea = $this.parentsUntil('.comment_send').find('textarea');

    var cid = comment_item.data("comment-id");
    var replyId = textarea.data('reply-id');
    var replyUserId = textarea.data('reply-user-id');
    var content = textarea.val().trim();

    if (content.length == 0) {
        alert("你还没有评论");
        return;
    }
    var comment = {
        articleId: articleId,
        content: content,
        replyId: replyId,
        replyUserId: replyUserId,
        rootComment: cid
    }

    $.ajax({
        url: "/comment/save",
        type: "post",
        data: JSON.stringify(comment),
        contentType: "application/json",
        success: function (result, textStatus, jqXHR) {
            if (result.status == 200) {
                textarea.text('').val("");
                initComment();
            }
            else if (result.status == 400) {
                alert("发送错误");
                console.warn(result.data);
            }
            else if (result.status == 500) {
                alert("发送异常");
                console.error(result);
            }
        },
        error: function (msg) {
            alert("发送失败");
            console.error(msg);
        }
    });

}

function getCommentCount() {
    $.get('/comment/count/' + articleId, function (result) {
        if (result.status == 200) {
            $('#comment-count').text(result.data);
        }
        else throw new Error(result.data);
    });
}

function initComment() {
    comment_paginator.setValue({
        listDOM: $('#comment_list'), pageDOM: $('#pagewrapper')
    }).initCommentList("/comment/blog/" + articleId);
    getCommentCount();
}