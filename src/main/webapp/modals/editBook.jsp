<%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 26.11.20
  Time: 13:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="help.CSRFHelper" %>
<%@ page import="org.owasp.encoder.Encode" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%

    String prefix = (String) request.getAttribute("contextPath");
    Map<String, String> errors = (Map<String, String>) session.getAttribute("errors");
    Map<String, String> old = (Map<String, String>) session.getAttribute("old");
%>

<div class="modal fade" id="editBookModal<%= request.getParameter("bid") %>" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Buch bearbeiten</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="form-group row">
                    <label for="name" class="col-md-4 col-form-label text-md-right">Name</label>
                    <div class="col-md-6">
                        <input class="form-control <%= errors.containsKey("name") ? "is-invalid" : "" %>"
                               name="name" required autocomplete="on" autofocus id="name" type="text"
                               value="<%= Encode.forHtml(old.getOrDefault("name", request.getParameter("name"))) %>" form="edit_book_<%= request.getParameter("bid") %>">
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
                    <label for="author" class="col-md-4 col-form-label text-md-right">Autor</label>
                    <div class="col-md-6">
                        <input class="form-control <%= errors.containsKey("author") ? "is-invalid" : "" %>"
                               name="author" required autocomplete="on" autofocus id="author" type="text"
                               value="<%= Encode.forHtml(old.getOrDefault("author", request.getParameter("author"))) %>" form="edit_book_<%= request.getParameter("bid") %>">
                        <%
                            if(errors.containsKey("author")) {
                        %>
                        <span class="invalid-feedback" role="alert">
                            <strong><%= Encode.forHtml(errors.get("author")) %></strong>
                        </span>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-link text-secondary" data-dismiss="modal">Abbrechen</button>

                <form id="edit_book_<%= request.getParameter("bid") %>" method="post" action="<%= prefix %>/admin/books/edit">
                    <input type="hidden" name="<%= CSRFHelper.CSRF_TOKEN %>" value="<%= CSRFHelper.getToken(session) %>"/>
                    <input type="hidden" name="id" value="<%= request.getParameter("bid") %>"/>
                    <button type="submit" class="btn btn-primary">Änderungen speichern</button>
                </form>
            </div>
        </div>
    </div>
</div>