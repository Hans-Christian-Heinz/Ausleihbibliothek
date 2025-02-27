<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 11.11.20
  Time: 10:51
  Formular zum Abmelden
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
    <div class="card-header">Registrieren</div>
    <div class="card-body">
        <form method="POST" action="<%= prefix %>/register">
            <input type="hidden" name="<%= CSRFHelper.CSRF_TOKEN %>" value="<%= CSRFHelper.getToken(session) %>"/>

            <div class="form-group row">
                <label for="username" class="col-md-4 col-form-label text-md-right">Benutzername</label>
                <div class="col-md-6">
                    <input class="form-control <%= errors.containsKey("username") ? "is-invalid" : "" %>"
                           name="username" required autocomplete="on" autofocus id="username" type="text"
                           value="<%= Encode.forHtml(old.getOrDefault("username", "")) %>">
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
                <label for="name" class="col-md-4 col-form-label text-md-right">Name</label>
                <div class="col-md-6">
                    <input class="form-control <%= errors.containsKey("name") ? "is-invalid" : "" %>"
                           name="name" required autocomplete="on" autofocus id="name" type="text"
                           value="<%= Encode.forHtml(old.getOrDefault("name", "")) %>">
                    <%
                        if(errors.containsKey("name")) {
                    %>
                    <span class="invalid-feedback" role="alert">
                        <strong><%= Encode.forHtml(errors.get("name")) %></strong>
                    </span>
                    <%
                        }
                    %>
                </div>
            </div>

            <div class="form-group row">
                <label for="vorname" class="col-md-4 col-form-label text-md-right">Vorname</label>
                <div class="col-md-6">
                    <input class="form-control <%= errors.containsKey("vorname") ? "is-invalid" : "" %>"
                           name="vorname" required autocomplete="on" autofocus id="vorname" type="text"
                           value="<%= Encode.forHtml(old.getOrDefault("vorname", "")) %>">
                    <%
                        if(errors.containsKey("vorname")) {
                    %>
                    <span class="invalid-feedback" role="alert">
                        <strong><%= Encode.forHtml(errors.get("vorname")) %></strong>
                    </span>
                    <%
                        }
                    %>
                </div>
            </div>

            <div class="form-group row">
                <label for="password" class="col-md-4 col-form-label text-md-right">Passwort</label>

                <div class="col-md-6">
                    <input class="form-control <%= errors.containsKey("password") ? "is-invalid" : "" %>"
                           name="password" id="password" type="password" required>
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

                <div class="form-group row">
                    <label for="password_repeat" class="col-md-4 col-form-label text-md-right">Passwort wiederholen</label>

                    <div class="col-md-6">
                        <input class="form-control <%= errors.containsKey("password_repeat") ? "is-invalid" : "" %>"
                               name="password_repeat" id="password_repeat" type="password" required>
                        <%
                            if(errors.containsKey("password_repeat")) {
                        %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= Encode.forHtml(errors.get("password_repeat")) %></strong>
                        </span>
                        <%
                            }
                        %>
                    </div>
                </div>

            <div class="form-group row mb-0">
                <div class="col-md-8 offset-md-4">
                    <button type="submit" class="btn btn-primary">Registrieren</button>
                </div>
            </div>
        </form>
    </div>
</div>
