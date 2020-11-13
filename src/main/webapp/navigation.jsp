<%@ page import="models.User" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 11.11.20
  Time: 10:33
  Navigationsleiste; wird immer angezeigt.
--%>

<%
    User user = (User) session.getAttribute("user");
%>

<nav class="navbar navbar-expand-md navbar-light bg-white shadow-sm">
    <div class="container">
        <a class="navbar-brand" href="home">
            Ausleihbibliothek
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Navigation umschalten">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <!-- Left Side Of Navbar -->
            <ul class="navbar-nav mr-auto">

            </ul>

            <!-- Right Side Of Navbar -->
            <ul class="navbar-nav ml-auto">
                <!-- Authentication Links -->
                <% if (user == null) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="login">Anmelden</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="register">Registrieren</a>
                    </li>
                <% } else { %>
                    <li class="nav-item dropdown">
                        <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <%= user.getUsername() %>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" data-toggle="modal" href="#editProfileModal">Profil bearbeiten</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="logout">Abmelden</a>
                        </div>
                    </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>

<%
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
%>

<% if(user != null) { %>
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

                <form class="form" id="profile_form" action="profile/edit" method="POST">
                    <button type="submit" class="btn btn-primary">Profil speichern</button>
                </form>
            </div>
        </div>
    </div>
</div>
<% } %>