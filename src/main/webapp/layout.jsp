<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>
<%@ page import="help.UserHelp" %>
<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 11.11.20
  Time: 10:27
  Layout: Wird immer angezeigt; veriabler Inhalt: requestScope.tpl
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    User user = UserHelp.getUser(session);
    String uri = (String) session.getAttribute("uri");
    String prefix = (String) request.getAttribute("contextPath");
%>

<html>
<head>
    <title>Ausleihbibliothek</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- bootstrap -->
    <!-- CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <!-- jQuery and JS bundle w/ Popper.js -->
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"
            integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0="
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
    <script src="<%= prefix %>/js/popover.js" type="text/javascript"></script>
    <script src="<%= prefix %>/js/delete.js" type="text/javascript"></script>
</head>
<body class="bg-light">
<% if (uri.startsWith(prefix + "/admin")) { %>
    <jsp:include page="navigation_admin.jsp"/>
<% } else { %>
    <jsp:include page="navigation.jsp"/>
<% } %>
<div class="container my-4">
    <div class="row">
        <div class="col-md-12 bg-white p-0">
            <jsp:include page="${requestScope.tpl}"/>
        </div>
    </div>
</div>

<%-- Wenn in einem Formular in einem modalen Fenster Validierungsfehler auftreten, zeige das modale fenster unmittelbar an --%>
<% if (user != null && errors.containsKey("modal")) { %>
    <script type="text/javascript">
        $(window).on('load',function(){
            $('#<%= errors.get("modal") %>').modal('show');
        });
    </script>
<% } %>

</body>
</html>
