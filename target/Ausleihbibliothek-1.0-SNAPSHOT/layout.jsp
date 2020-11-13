<%@ page import="java.util.Map" %>
<%@ page import="models.User" %><%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 11.11.20
  Time: 10:27
  Layout: Wird immer angezeigt; veriabler Inhalt: requestScope.tpl
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    User user = (User) session.getAttribute("user");
%>

<html>
<head>
    <title>Ausleihbibliothek</title>

    <!-- bootstrap -->
    <!-- CSS -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/css/bootstrap.min.css" integrity="sha384-TX8t27EcRE3e/ihU7zmQxVncDAy5uIKz4rEkgIXeMed4M0jlfIDPvg6uqKI2xXr2" crossorigin="anonymous">
    <!-- jQuery and JS bundle w/ Popper.js -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ho+j7jyWK8fNQe+A12Hb8AhRq26LrZ/JpcUGGOn+Y7RsweNrtN/tE3MoK7ZeZDyx" crossorigin="anonymous"></script>
</head>
<body class="bg-light">
<jsp:include page="navigation.jsp"/>
<div class="container my-4">
    <div class="row">
        <div class="col-md-12 bg-white p-0">
            <jsp:include page="${requestScope.tpl}"/>
        </div>
    </div>
</div>

<% if (user != null && errors.containsKey("editProfile")) { %>
    <script type="text/javascript">
        $(window).on('load',function(){
            $('#editProfileModal').modal('show');
        });
    </script>
<% } %>

</body>
</html>
