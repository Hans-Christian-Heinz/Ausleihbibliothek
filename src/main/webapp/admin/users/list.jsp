<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 13.11.20
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    User user = (User) session.getAttribute("user");
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
%>

<div class="card">
    <div class="card-header">
        <h3>Benutzerliste</h3>
    </div>
    <div class="card-body">
        TODO
    </div>
</div>