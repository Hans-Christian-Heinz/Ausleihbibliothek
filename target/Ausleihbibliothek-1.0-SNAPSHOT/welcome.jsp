<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 12.11.20
  Time: 14:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="models.User" %>
<%@ page import="help.UserHelp" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = UserHelp.getUser(session);
%>

<div class="card">
    <div class="card-header">Wilkommen, <%= Encode.forHtml(user.getFullName()) %>.</div>

    <div class="card-body">
        <p>
            Sie wurden erfolgreich in der Applikation angemeldet.
        </p>
    </div>
</div>
