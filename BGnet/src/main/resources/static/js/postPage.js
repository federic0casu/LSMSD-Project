function requestPostPage(pageNumber){
    $.ajax({
        url : "/api/getPost",
        data : {page : pageNumber},
        method : "get",
        parameters : 0,
        success : function(data) {
            let posts = $.parseJSON(data);
            if (posts != null) {
                let post = 0
                $('#game').text(posts[post].game)
                for (post in posts) {
                    let html = '<div id="post-' + posts[post].id + '" class="post"><div class="w3-container w3-card w3-white w3-round w3-margin-left w3-margin-right"><br>'
                    html += '<img src="img/avatar.png" alt="Avatar" class="w3-left w3-circle w3-margin-right" style="width:60px">'
                    html += '<span class="w3-right w3-opacity"><i class="fa fa-calendar"></i>' + posts[post].date.slice(0, 10) + '</span>'
                    html += '<span class="w3-right w3-opacity w3-margin-right"><i class="fa fa-comment"></i>' + posts[post].comments + '</span>'
                    html += '<span class="w3-right w3-opacity w3-margin-right"><i class="fa fa-thumbs-up"></i>' + posts[post].likes + '</span>'
                    html += ('<h4 id="' + posts[post].author + '" class="author">' + posts[post].author + '</h4><br><hr class="w3-clear">')
                    html += ('<p>' + posts[post].text + '</p>')
                    html += '<p id="_id" style="display: none;">' + posts[post].id + '</p>'
                    html += '<button type="button" class="logged like w3-button w3-theme-d1 w3-margin-bottom" id="like-post-' + posts[post].id + '"><i id="like-post-' + posts[post].id + '" class="fa fa-thumbs-' + ((posts[post].hasLiked) ? 'down' : 'up') + '"></i>' + ((posts[post].hasLiked) ? ' Unlike' : ' Like') + '</button>'
                    html += '<button type="button" class="view-comments-' + post + ' view-comments w3-button w3-theme-d2 w3-margin-bottom" id="' + posts[post].id + '"><i id="' + posts[post].id + '" class="fa fa-comment"></i> View comments</button>'
                    html += '<button type="button" class="admin delete w3-button w3-theme-d2 w3-margin-bottom" id="deletepost-' + posts[post].id + '"><i class="fa fa-comment"></i> Delete Post</button>'
                    html += '</div><br>'
                    $('#containerPosts').append(html)
                }
                $('h4.author').bind('click', function(event) {
                    searchForAPerson(event.target.id)
                })
                $('button.like').bind('click', function(event) {
                    $.ajax({
                        url: "/api/likePost",
                        method: "get",
                        data: {post: event.target.id.slice(10), game: posts[0].game},
                        success: function (data) {
                            data = JSON.parse(data)
                            if(data == 0) {
                                alert("You must be logged to like a post!")
                                window.location.href = "http://localhost:8080/login"
                            }
                            else if(data == -1) {
                                alert("Something goes wrong!")
                            }
                            else
                                window.location.href = "http://localhost:8080/postPage"
                        }
                    })
                })
                $('button.view-comments').bind('click', function(event) {
                    window.location.href = "http://localhost:8080/commentPage?post=" + event.target.id
                })
                $("#game").bind('click', function(event){
                    window.location.href = "http://localhost:8080/gamePage"
                })
                $(".delete").bind('click', function(event){
                    if(confirm("Do you really want to delete this post?")){
                        $.ajax({
                            url: "/api/deletePost",
                            method: "get",
                            data: {id: event.target.id.slice(11)},
                            success: function(data){
                                alert("Post deleted!")
                                window.location.href = "http://localhost:8080/postPage"
                            }
                        })
                    }
                })
                $('#postButton').bind('click', function (event) {
                    $('#postButton').prop("disabled", true)
                    html = "<div class=\"w3-container w3-card w3-white w3-round w3-margin-left w3-margin-right\">"
                    html += "<br>"
                    html += "<textarea id=\"post-text\" placeholder=\"Say Something:\" rows=\"5\" cols=\"99\"></textarea>"
                    html += "<br>"
                    html += "<button type=\"button\" id=\"submit-" + data.gameName + "\" class=\"w3-button w3-theme-d2 w3-margin-bottom\"><i class=\"fa fa-comment\"></i>Submit</button></button>"
                    html += "</div><br>"
                    $("#containerPosts").prepend(html)
                    $('#submit-' + data.gameName).bind('click', function(event) {
                        $.ajax({
                            url: "api/addPost",
                            data: {game: $("#game").text(), text: $('textarea#post-text').val()},
                            method : "get",
                            success: function (data) {
                                if(data == false)
                                    alert("Something went wrong")
                                else {
                                    window.location.href = "http://localhost:8080/gamePage"
                                }
                            }
                        })
                    })
                })
                $(".admin").hide()
                checkAdmin()
                checkLogged()
            }
        }
    })
}
$(document).ready(function (){
    loadNumberOfPages();
    requestPostPage(0);
})
function precPage() {
    let page = parseInt($('#page').text()) - 1
    if(page <= 0)
        return
    $('#containerPosts').empty()
    requestPostPage(page - 1);
    $('#page').text(page)
    $('#succPage').prop("disabled", false)
}
function succPage() {
    let page = parseInt($('#page').text()) + 1
    $('#containerPosts').empty()
    requestPostPage(page - 1)
    $('#page').text(page)
    if($('#page').text() == $("#howManyPages").attr("class")){
        $("#succPage").prop("disabled", true)
    }
}

function loadNumberOfPages(){
    $.ajax({
        url: "/api/getPages",
        success: function(data){
            $("#howManyPages").attr("class", data)
        }
    })
}

function likePost(){;}

function addPost(){;}

function backToThePage(){;
}

