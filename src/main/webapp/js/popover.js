/*
 * Popover: Dropdown zur Seitenauswahl
 */

$(document).ready(function() {
    const pagPopover = $('span.paginationPopover');
    const wbTpl = '' +
        '<div class="popover" style="min-width: 10em;" role="tooltip">' +
        '<div class="arrow"></div>' +
        '<div class="popover-body"></div>' +
        '</div>';

    pagPopover.popover({
        placement: "bottom",
        content: popoverContent,
        html: true,
        sanitize: false,
        template: wbTpl
    })
    .on("mouseenter", function() {
        let _this = this;
        $(this).popover("show");
        $("select.paginationSelect").change(goToPage);
        $(".popover").on("mouseleave", function() {
            $(_this).popover('hide');
        });
    }).on("mouseleave", function() {
        let _this = this;
        setTimeout(function() {
            if (!$(".popover:hover").length) {
                $(_this).popover("hide");
            }
        }, 300);
    });

    $('input[type=number]#perPage').change(changePerPage);

    function replaceQueryString(name, value) {
        const url = window.location.href;
        //query-String
        let qs;
        //replace or add query string name=value
        const index = url.indexOf("?");
        if (index !== -1) {
            qs = url.substr(index);
            const cpIndex = qs.indexOf(name + "=");
            if (cpIndex !== -1) {
                const cpEnd = qs.indexOf("&", cpIndex);
                let cpStr;
                if (cpEnd !== -1) {
                    cpStr = qs.substring(cpIndex, cpEnd);
                }
                else {
                    cpStr = qs.substr(cpIndex);
                }
                qs = qs.replace(cpStr, name + "=" + value);
            }
            else {
                qs += "&" + name + "=" + value;
            }

            window.location.href = url.substr(0, index) + qs;
        }
        else {
            qs = "?" + name + "=" + value;

            window.location.href = url + qs;
        }
    }

    function changePerPage(e) {
        const perPage = $(e.target).val();
        replaceQueryString("perPage", perPage);
    }

    function goToPage(e) {
        const page = $(e.target).val();
        replaceQueryString("currentPage", page);
    }

    function popoverContent() {
        const pages = JSON.parse($(this).attr("data-pages"));
        const currentPage = $(this).attr("data-cp");

        let options = '';
        pages.forEach(function(page) {
            options += '<option value="' + page + '"';
            if (Number(page) === Number(currentPage)) {
                options += ' selected';
            }
            options += '>' + page + '</option>';
        });

        /*
        <li class="nav-item dropdown">
                        <a id="booksDropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Bücher
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="booksDropdown">
                            <a class="dropdown-item" href="<%= prefix %>/books">Alle Bücher</a>
                            <a class="dropdown-item" href="<%= prefix %>/books/borrowed">Von Ihnen ausgeliehen</a>
                        </div>
                    </li>
         */

        return '' +
            '<div class="d-flex justify-content-between">' +
            '<label for="selectPag">Seite</label>' +
            '<select class="form-control paginationSelect" data-cp="' + currentPage + '" style="width: 4em;" id="selectPag">' +
            options +
            "</select>" +
            "</div>";
    }
});