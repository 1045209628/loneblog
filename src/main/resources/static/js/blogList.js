
var paginator = {
    listDOM: null,
    pagesDOM: null,
    manager: false,
    setDOM: function (opt) {
        this.listDOM = opt.listDOM;
        this.pagesDOM = opt.pagesDOM;
        this.manager = opt.manager;
        return this;
    },
    initBlogList: function (url) {
        this.getBlogList(url, 1);
    },
    getBlogList: function (url, pageNum) {
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
                    _this.setNullBlogList(null);
                    return;
                }
                _this.setBlogList(articles);
                _this.setPageItem(result.data, url);
            } else if (result.status == 400) {
                _this.setNullBlogList('服务器出错了!');
                console.warn(result.data);
            } else if (result.status == 500) {
                _this.setNullBlogList('服务器出现异常:\n');
                throw new Error(result.data);
            }
        });
    },
    setBlogList: function (articles) {
        var _this = this;
        this.listDOM.empty();
        $.each(articles, function (index, element) {
            var article = articles[index];
            _this.setBlogItem(article);
        })
        _this.pagesDOM.show();
    },
    setBlogItem: function (article) {
        var html = '<div class="article" data-article-id=' + article.id + ' >' +
            '<h3 class="title">' +
            '<a href="/blog/' + article.id + '" title="' + article.title + '" >' + article.title + '</a>' +
            '</h3>' +
            '<div class="content">' + article.summary + '</div>' +
            '<div class="staticlist d-flex justify-content-between"><div>' + new Date(article.createTime).toLocaleDateString() + ' · <a href="/user/' + article.userId + '">' + article.username + '</a> · ' +
            article.clicks + ' 浏览 · ' + article.commentCount + ' 评论 · ' + article.collectionCount + ' 收藏' +
            '</div>' +
            '</div>' +
            '</div>';
        var blogDOM = $(html);
        if (this.manager) {
            var managerDOM = '<div>' +
                '<a href="/user/edit/'+article.id+'" style="color:blue;">编辑</a>&nbsp;&nbsp;' +
                //'<a href="/edit/'+article.id+'" style="color:red;">删除</a>' +
                '</div>';
            blogDOM.find('.staticlist').append(managerDOM);
        }
        blogDOM.hover(function () {
            blogDOM.toggleClass('selected');
        }, function () {
            blogDOM.toggleClass('selected');
        }).appendTo(this.listDOM);
        //listDOM.append(html);
    },
    setNullBlogList: function (alertmsg) {
        var html = '<h1 class="text-center my-1" style="color: gray;">目前没有博客</h1>';
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
                //getBlogList(url,pageNum);
                _this.getBlogList(url, pageNum);
            }
        })
    }

}





