<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 13.11.20
  Time: 09:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
%>

<nav class="navbar navbar-expand-md navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="<%= prefix %>/admin">Ausleihbibliothek <span class="text-danger fa fa-cog mx-2"></span><span class="text-danger">Adminbereich</span></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Navigation umschalten">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <!-- Left Side Of Navbar -->
            <ul class="navbar-nav mr-auto">

            </ul>

            <!-- Right Side Of Navbar -->
            <ul class="navbar-nav ml-auto">
                <li class="nav-item">
                    <a href="<%= prefix %>/home" class="nav-link"><span class="fa fa-user-circle-o mr-2"></span>Zurück zur Benutzeransicht</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<nav class="navbar navbar-expand-md navbar-light bg-white shadow-sm">
    <div class="container">
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#secondNavbar" aria-controls="secondNavbar" aria-expanded="false" aria-label="Navigation umschalten">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="secondNavbar">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item">
                    <a href="<%= prefix %>/admin/books" class="nav-link pl-0">Bücherliste</a>
                </li>
                <li class="nav-item">
                    <a href="<%= prefix %>/admin/users" class="nav-link">Benutzer verwalten</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
