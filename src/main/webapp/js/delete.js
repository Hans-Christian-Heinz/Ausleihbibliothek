const sendDelete = function (prefix, url, redirect) {
    $.ajax(prefix + url, {
        async: true,
        method: "DELETE",
        success: function(data) {
            if (data) {
                window.location.href = prefix + redirect;
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            document.write(jqXHR.responseText);
            //todo ggf research the first parameter
            history.pushState({}, "", prefix + url);
        }
    });
}

$(document).ready(function() {
    $("a#deleteSelf").click(function(event) {
        event.preventDefault();
        sendDelete($(this).attr('data-prefix'), $(this).attr('href'), $(this).attr('data-redirect'));
    })
});