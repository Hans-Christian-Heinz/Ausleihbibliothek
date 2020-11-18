<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 11.11.20
  Time: 10:50
  Formular zum Anmelden
--%>
<%@ page import="java.util.Map" %>
<%@ page import="help.CSRFHelper" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
%>

<div class="card">
    <div class="card-header">Login</div>

    <div class="card-body">
        <form method="POST" action="<%= prefix %>/login">
            <input type="hidden" name="<%= CSRFHelper.CSRF_TOKEN %>" value="<%= CSRFHelper.getToken(session) %>"/>

            <div class="form-group row">
                <label for="username" class="col-md-4 col-form-label text-md-right">Benutzername</label>

                <div class="col-md-6">
                    <input id="username" type="text" class="form-control <%= errors.containsKey("username") ? "is-invalid" : "" %>"
                           name="username" required autocomplete="on" autofocus value="<%= Encode.forHtml(old.getOrDefault("username", "")) %>">
                    <%
                        if(errors.containsKey("username")) {
                    %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= Encode.forHtml(errors.get("username")) %></strong>
                        </span>
                    <%
                        }
                    %>
                </div>
            </div>

            <div class="form-group row">
                <label for="password" class="col-md-4 col-form-label text-md-right">Passwort</label>

                <div class="col-md-6">
                    <input id="password" type="password" class="form-control <%= errors.containsKey("password") ? "is-invalid" : "" %>"
                           name="password" required autocomplete="current-password">
                    <%
                        if(errors.containsKey("password")) {
                    %>
                    <span class="invalid-feedback" role="alert">
                            <strong><%= Encode.forHtml(errors.get("password")) %></strong>
                        </span>
                    <%
                        }
                    %>
                </div>
            </div>

            <div class="form-group row mb-0">
                <div class="col-md-8 offset-md-4">
                    <button type="submit" class="btn btn-primary">Anmelden</button>
                </div>
            </div>
        </form>
    </div>
</div>