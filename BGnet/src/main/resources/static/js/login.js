$(document).ready(function () {
    document.getElementById('btn_login').onclick = function (e) {
        let uname = document.getElementById('username').value
        let pwd = document.getElementById('password').value
        $.ajax({
            url : "/api/login",
            data : {username : uname, password: pwd},
            dataType : 'json',
            method : "post",
            success: function(data) {
                result = jQuery.parseJSON(data)
                console.log(result)
                if(result['type'] == 1) {
                    alert("Username NOT VALID")
                    $("#username").val("")
                    $("#password").val("")
                } else if(result['type'] == 2) {
                    alert("Password NOT VALID")
                    $("#username").val("")
                    $("#password").val("")
                } else
                    alert("OK")
            }
        })
    }
});