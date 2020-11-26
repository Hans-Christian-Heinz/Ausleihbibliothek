<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 16.11.20
  Time: 13:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="java.util.Map" %>
<%@ page import="models.User" %>
<%@ page import="java.util.List" %>
<%@ page import="models.DBModel" %>
<%@ page import="models.Book" %>
<%@ page import="help.CSRFHelper" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
    List<DBModel> books = (List<DBModel>) request.getAttribute("books");
%>

<div class="card">
    <div class="card-header">
        <h3>Alle Bücher</h3>
    </div>
    <div class="card-body">
        <table class="table table-bordered table-striped table-hover text-center">
            <tr>
                <th style="width: 25%"><label for="name">Name</label></th>
                <th style="width: 25%"><label for="author">Autor</label></th>
                <th style="width: 25%">Ausgeliehen von</th>
                <th style="width: 12.5%">Bearbeiten</th>
                <th style="width: 12.5%">Löschen</th>
            </tr>
            <% for (DBModel model : books) { %>
                <% Book book = (Book) model; %>
                <tr>
                    <td class="text-left"><%= book.getName() %></td>
                    <td class="text-left"><%= book.getAuthor() %></td>
                    <td class="text-left"><%= book.getRelValue("owner") == null ? "-" : Encode.forHtml(((User)book.getRelValue("owner")).getFullName()) %></td>
                    <td>
                        <a href="#editBookModal<%= model.getId()%>" data-toggle="modal" class="btn btn-sm btn-secondary">Bearbeiten</a>
                    </td>
                    <td>
                        <a href="#deleteBookModal<%= model.getId() %>" data-toggle="modal" class="btn btn-sm btn-outline-danger">Löschen</a>
                    </td>
                </tr>
            <% } %>

            <tr>
                <td>
                    <input type="text" class="form-control <%= errors.containsKey("name") ? "is-invalid" : "" %>" name="name"
                           form="formAddBook" id="name" required placeholder="Name" value="<%= Encode.forHtml(old.getOrDefault("name", "")) %>"/>
                    <%
                        if(errors.containsKey("name")) {
                    %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= Encode.forHtml(errors.get("name")) %></strong>
                        </span>
                    <%
                        }
                    %>
                </td>
                <td>
                    <input type="text" class="form-control <%= errors.containsKey("author") ? "is-invalid" : "" %>"
                           name="author" form="formAddBook" id="author" required placeholder="Autor" value="<%= Encode.forHtml(old.getOrDefault("author", "")) %>"/>
                    <%
                        if(errors.containsKey("author")) {
                    %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= Encode.forHtml(errors.get("author")) %></strong>
                        </span>
                    <%
                        }
                    %>
                </td>
                <td></td>
                <td>
                    <form method="post" action="<%= prefix %>/admin/books" id="formAddBook">
                        <input type="hidden" name="<%= CSRFHelper.CSRF_TOKEN %>" value="<%= CSRFHelper.getToken(session) %>"/>
                        <input type="submit" class="btn btn-primary" value="Hinzufügen"/>
                    </form>
                </td>
            </tr>
        </table>
    </div>
</div>

<jsp:include page="/pagination.jsp">
    <jsp:param name="href" value="/admin/books"/>
</jsp:include>

<% for (DBModel model : books) { %>
<jsp:include page="/modals/deleteBook.jsp">
    <jsp:param name="bid" value="<%= model.getId() %>"/>
    <jsp:param name="name" value="<%= ((Book)model).getName() %>"/>
</jsp:include>
<jsp:include page="/modals/editBook.jsp">
    <jsp:param name="bid" value="<%= model.getId() %>"/>
    <jsp:param name="name" value="<%= ((Book)model).getName() %>"/>
    <jsp:param name="author" value="<%= ((Book)model).getAuthor() %>"/>
</jsp:include>
<% } %>