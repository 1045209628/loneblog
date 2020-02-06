
var paginator = {
    listDOM: null,
    pagesDOM: null,
    setDOM: function (opt) {
        this.listDOM = opt.listDOM;
        this.pagesDOM = opt.pagesDOM;
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
        this.pagesDOM.empty();
        $.get(url, page, function (result) {
            if (result.status == 200) {
                var articles = result.data.list;
                if (articles.length == 0) {
                    _this.setNullCommentList(null);
                    return;
                }
                _this.setCommentList(articles);
                _this.setPageItem(result.data, url);

            } else if (result.status == 400) {
                _this.setNullCommentList('服务器出错了!');
                console.warn(result.data);
            } else if (result.status == 500) {
                _this.setNullCommentList('服务器出现异常:\n');
                throw new Error(result.data);
            }
        });
    },
    setCommentList: function (comments) {
        var _this = this;
        this.listDOM.empty();
        $.each(comments, function (index, element) {
            var comment = comments[index];
            _this.setCommentItem(comment);
        })
        _this.pagesDOM.show();
    },
    setCommentItem: function (comment) {
        var html = '<div class="comment">' +
            '<div style="position: relative; margin-right: 40px;">' +
            '<h5 style="color:gray">' + comment.blogName + '</h5>' +
            '<div>' +
            (comment.replyname == null ? "" : '<span>回复 <span>' + comment.replyname + '</span>:</span>&nbsp;')
            + comment.content +
            '</div></div></div>';
        var commentDOM = $(html);
        if (sessionUserId != null && sessionUserId == userId) {
            this.appenCloseBtn(commentDOM, comment);
        }
        commentDOM.hover(function () {
            commentDOM.toggleClass('selected');
        }, function () {
            commentDOM.toggleClass('selected');
        }).appendTo(this.listDOM);
        //listDOM.append(html);
    },
    setNullCommentList: function (alertmsg) {
        var html = '<h1 class="text-center my-1" style="color: gray;">目前没有评论</h1>';
        this.listDOM.html(html);
        this.pagesDOM.hide();
        if (alertmsg != null)
            alert(alertmsg);
    },
    setPageItem: function (pageInfo, url) {
        var _this = this;
        this.pagesDOM.pagination({
            cssStyle: 'dark-theme',
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
    },
    appenCloseBtn: function (dom, data) {
        var $dom = $(dom);
        var html = '<div class="close_btn" title="删除">' +
            '<a href="javascript:deleteCommnet(' + data.id + ');"><i class="zi zi_times"></i></a>' +
            '</div>';
        dom.append($(html));
    }

}



function deleteCommnet(id) {
    var isDelete = window.confirm("您确定要删除吗");
    if (isDelete) {
        $.ajax({
            url:"/comment/"+id,
            type: 'delete',
            success: function (result) {
                if (result.status == 200) {
                    alert("删除成功");
                    paginator.initCommentList("/comment/user/" + userId);
                }
                else {
                    console.warn("出现错误");
                }
            },
            error: function (xhr, status, errors) {
                alert("发送失败");
                console.error(errors);
            }
        });
    }
    else return;
};