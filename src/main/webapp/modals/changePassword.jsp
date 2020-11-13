<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 13.11.20
  Time: 08:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>

<%
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
%>

<div class="modal fade" id="changePwdModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Passwort ändern</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group row">
                    <label for="password_old" class="col-md-4 col-form-label text-md-right">Altes Passwort</label>

                    <div class="col-md-6">
                        <input class="form-control <%= errors.containsKey("password_old") ? "is-invalid" : "" %>"
                               name="password_old" id="password_old" type="password" required form="change_pwd_form">
                        <%
                            if(errors.containsKey("password_old")) {
                        %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= errors.get("password_old") %></strong>
                        </span>
                        <%
                            }
                        %>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="password" class="col-md-4 col-form-label text-md-right">Neues Passwort</label>

                    <div class="col-md-6">
                        <input class="form-control <%= errors.containsKey("password") ? "is-invalid" : "" %>"
                               name="password" id="password" type="password" required form="change_pwd_form">
                        <%
                            if(errors.containsKey("password")) {
                        %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= errors.get("password") %></strong>
                        </span>
                        <%
                            }
                        %>
                    </div>
                </div>

                <div class="form-group row">
                    <label for="password_repeat" class="col-md-4 col-form-label text-md-right">Neues Passwort wiederholen</label>

                    <div class="col-md-6">
                        <input class="form-control <%= errors.containsKey("password_repeat") ? "is-invalid" : "" %>"
                               name="password_repeat" id="password_repeat" type="password" required form="change_pwd_form">
                        <%
                            if(errors.containsKey("password_repeat")) {
                        %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= errors.get("password_repeat") %></strong>
                        </span>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-link text-secondary" data-dismiss="modal">Abbrechen</button>

                <form class="form" id="change_pwd_form" action="profile/pwd" method="POST">
                    <button type="submit" class="btn btn-primary">Passwort ändern</button>
                </form>
            </div>
        </div>
    </div>
</div>