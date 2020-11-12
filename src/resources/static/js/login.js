$('.message a').click(function () {
    $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
});


$(document).ready(function() {

    $("#loginform").click(login)

});

function login() {

    $.ajax({
        type: 'POST',
        url: '/login',
        data: $('#loginform').serialize(),
        cache: false,
        dataType: "json",
        crossDomain: false,
        success: function (data) {
            var response = jQuery.parseJSON(data);
            if (response.success === true) {
                console.info("Authentication Success!");
                window.location("/api/quizzes");
            }
            else {
                console.error("Unable to login");
            }
        },
        error: function (data) {
            console.error("Login failure");
        }
    });
}