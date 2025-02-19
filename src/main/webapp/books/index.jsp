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
<%@ page import="help.UserHelp" %>
<%@ page import="help.CSRFHelper" %>
<%@ page import="org.owasp.encoder.Encode" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    User user = UserHelp.getUser(session);
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
    List<DBModel> books = (List<DBModel>) request.getAttribute("books");
%>

<div class="card">
    <div class="card-header">
        <h3>Alle Bücher</h3>
    </div>
    <div class="card-body">
        <table class="table table-bordered  table-striped table-hover text-center">
            <tr>
                <th style="width: 30%">Name</th>
                <th style="width: 30%">Autor</th>
                <th style="width: 30%">Ausgeliehen von</th>
                <th style="width: 10%"></th>
            </tr>
            <% for (DBModel model : books) { %>
            <% Book book = (Book) model; %>
                <tr>
                    <td><%= Encode.forHtml(book.getName()) %></td>
                    <td><%= Encode.forHtml(book.getAuthor()) %></td>
                    <td><%= book.getRelValue("owner") == null ? "-" : Encode.forHtml(((User)book.getRelValue("owner")).getFullName()) %></td>
                    <td>
                        <form method="post" action="<%= prefix %>/books/borrow">
                            <input type="hidden" name="<%= CSRFHelper.CSRF_TOKEN %>" value="<%= CSRFHelper.getToken(session) %>"/>
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

<jsp:include page="/pagination.jsp">
    <jsp:param name="href" value="/books"/>
</jsp:include>