$('.message a').click(function () {
    $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
});


$(document).ready(function () {

    $("form").click(login)

});

function login() {

    console.log('login');
    $.ajax({
        type: 'POST',
        url: '/login',
        data: $('#loginform').serialize(),
        cache: false,
        // dataType: "json",
        crossDomain: false,
        success: function (data) {
            console.log(data);
            var response = jQuery.parseJSON(data);
            if (response.success === true) {
                console.log("Authentication Success!");
                window.location("/api/quizzes");
            } else {
                console.log("Unable to login");
            }
        },
        error: function (data) {
            console.log("Login failure");
        }
    });
}