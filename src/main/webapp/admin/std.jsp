<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 13.11.20
  Time: 09:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>
<%@ page import="help.UserHelp" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = UserHelp.getUser(session);
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
%>

<div class="card">
    <div class="card-header">Wilkommen, <%= Encode.forHtml(user.getFullName()) %>.</div>

    <div class="card-body">
        <p>
            Sie haben den Adminbereich der Applikation erreicht..
        </p>
    </div>
</div>