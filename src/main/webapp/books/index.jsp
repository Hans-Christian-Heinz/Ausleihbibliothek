<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 16.11.20
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>
<%@ page import="models.DBModel" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Book" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    User user = (User) session.getAttribute("user");
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
    List<DBModel> books = (List<DBModel>) request.getAttribute("books");
%>

<div class="card">
    <div class="card-header">
        <h3>Alle Bücher</h3>
    </div>
    <div class="card-body">
        <table class="table table-bordered table-hover text-center">
            <tr>
                <th>Name</th>
                <th>Autor</th>
                <th>Ausgeliehen von</th>
                <th></th>
            </tr>
            <% for (DBModel model : books) { %>
            <% Book book = (Book) model; %>
                <tr>
                    <td><%= book.getName() %></td>
                    <td><%= book.getAuthor() %></td>
                    <td><%= book.getRelValue("owner") == null ? "-" : ((User)book.getRelValue("owner")).getFullName() %></td>
                    <td>
                        <form method="post" action="<%= prefix %>/books/borrow">
                            <input type="hidden" name="id" value="<%= book.getId() %>"/>
                            <input type="hidden" name="user_id" value="<%= book.getAusgeliehenVon() == null ? "0" : book.getAusgeliehenVon() %>"/>
                            <% if (book.getAusgeliehenVon() == null) { %>
                                <input type="submit" class="btn btn-sm btn-outline-primary" value="Ausleihen"/>
                            <% } else if (book.getAusgeliehenVon().equals(user.getId())) { %>
                                <input type="submit" class="btn btn-sm btn-outline-primary" value="Zurückgeben"/>
                            <% } %>
                        </form>
                    </td>
                </tr>
            <% } %>
        </table>
    </div>
</div>