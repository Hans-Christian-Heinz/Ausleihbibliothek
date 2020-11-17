<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 11.11.20
  Time: 10:33
  Navigationsleiste; wird immer angezeigt.
--%>
<%@ page import="models.User" %>
<%@ page import="help.UserHelp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    User user = UserHelp.getUser(session);
    String prefix = (String) request.getAttribute("contextPath");
%>

<nav class="navbar navbar-expand-md navbar-light bg-white shadow-sm">
    <div class="container">
        <a class="navbar-brand" href="<%= prefix %>/home">
            Ausleihbibliothek
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Navigation umschalten">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <!-- Left Side Of Navbar -->
            <ul class="navbar-nav mr-auto">
                <% if (user != null && "admin".equals(user.getRole())) { %>
                    <li class="nav-item">
                        <a href="<%= prefix %>/admin" class="nav-link text-danger">
                            <span class="fa fa-cog mr-2" aria-hidden="true"></span>Adminbereich
                        </a>
                    </li>
                <% } %>
            </ul>

            <!-- Right Side Of Navbar -->
            <ul class="navbar-nav ml-auto">
                <!-- Authentication Links -->
                <% if (user == null) { %>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= prefix %>/login">Anmelden</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= prefix %>/register">Registrieren</a>
                    </li>
                <% } else { %>
                    <li class="nav-item dropdown">
                        <a id="booksDropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Bücher
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="booksDropdown">
                            <a class="dropdown-item" href="<%= prefix %>/books">Alle Bücher</a>
                            <a class="dropdown-item" href="<%= prefix %>/books/borrowed">Von Ihnen ausgeliehen</a>
                        </div>
                    </li>

                    <li class="nav-item dropdown">
                        <a id="navbarDropdown" class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <%= user.getUsername() %>
                        </a>
                        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                            <a class="dropdown-item" data-toggle="modal" href="#editProfileModal">Profil bearbeiten</a>
                            <a class="dropdown-item" data-toggle="modal" href="#changePwdModal">Passwort ändern</a>
                            <div class="dropdown-divider"></div>
                            <a class="dropdown-item" href="<%= prefix %>/logout">Abmelden</a>
                        </div>
                    </li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>

<% if(user != null) { %>
    <jsp:include page="modals/editProfile.jsp"/>
    <jsp:include page="modals/changePassword.jsp"/>
<% } %>