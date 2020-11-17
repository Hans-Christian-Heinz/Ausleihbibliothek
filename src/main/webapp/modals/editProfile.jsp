<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 13.11.20
  Time: 08:15
  Modales Fenster zum Bearbeiten von Benutzerprofilen.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    User user = (User) session.getAttribute("user");
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
%>

<div class="modal fade" id="editProfileModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Profil verwalten</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group row">
                    <input type="hidden" name="user_id" value="<%= user.getId() %>" form="profile_form"/>

                    <label for="username" class="col-md-4 col-form-label text-md-right">Benutzername</label>
                    <div class="col-md-6">
                        <input class="form-control <%= errors.containsKey("username") ? "is-invalid" : "" %>"
                               name="username" required autocomplete="on" autofocus id="username" type="text"
                               value="<%= old.getOrDefault("username", user.getUsername()) %>" form="profile_form">
                        <%
                            if(errors.containsKey("username")) {
                        %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= errors.get("username") %></strong>
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
                               value="<%= old.getOrDefault("name", user.getName()) %>" form="profile_form">
                        <%
                            if(errors.containsKey("name")) {
                        %>
                        <span class="invalid-feedback" role="alert">
                        <strong><%= errors.get("name") %></strong>
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
                               value="<%= old.getOrDefault("vorname", user.getVorname()) %>" form="profile_form">
                        <%
                            if(errors.containsKey("vorname")) {
                        %>
                        <span class="invalid-feedback" role="alert">
                        <strong><%= errors.get("vorname") %></strong>
                    </span>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-link text-secondary" data-dismiss="modal">Abbrechen</button>

                <form class="form" id="profile_form" action="<%= prefix %>/profile/edit" method="POST">
                    <button type="submit" class="btn btn-primary">Profil speichern</button>
                </form>
            </div>
        </div>
    </div>
</div>