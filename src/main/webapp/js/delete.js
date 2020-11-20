$(document).ready(function() {
    const sendDelete = function (url) {
        $.ajax(url, {
            async: true,
            method: "DELETE",
            success: function(data, textStatus, jqXHR) {
                if (data) {
                    if (jqXHR.getResponseHeader("redirectTo")) {
                        window.location.href = jqXHR.getResponseHeader("redirectTo");
                    }
                    //window.location.href = prefix + redirect;
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                document.write(jqXHR.responseText);
                //todo ggf research the first parameter
                history.pushState({}, "", url);
            }
        });
    }

    $("a.delete-link").click(function(event) {
        event.preventDefault();
        //sendDelete($(this).attr('data-prefix'), $(this).attr('href'), $(this).attr('data-redirect'));
        sendDelete($(this).attr('href'));
    })
});