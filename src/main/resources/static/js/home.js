function initPage() {
    paginator.setDOM({
        listDOM:$('#list'),pagesDOM:$('#pagewrapper')
    }).initBlogList("/blog/new");
    initCss();
    $('#square').click(function () {
        paginator.initBlogList("/blog/new");
    });
    $('#hot').click(function () {
        paginator.initBlogList("/blog/hot");
    });
    $('#new').click(function () {
        paginator.initBlogList("/blog/new");
    });
}

function loginModal() {
    $("#login-modal").modal('show');
}