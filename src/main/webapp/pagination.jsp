<%@ page import="javax.json.*" %>
<%@ page import="java.util.*" %><%--
  Created by IntelliJ IDEA.
  User: h.heinz
  Date: 17.11.20
  Time: 15:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    String prefix = (String) request.getAttribute("contextPath");
    String href = request.getParameter("href");
    int totalPages = (int) request.getAttribute("totalPages");
    int currentPage = (int) request.getAttribute("currentPage");
    int perPage = (int) request.getAttribute("perPage");
    int upper = Math.min(totalPages, currentPage + 2);
    int lower = Math.max(0, currentPage - 1);
    int[] pages = new int[totalPages];
    for (int i = 1; i <= totalPages; i++) {
        pages[i - 1] = i;
    }
%>

<div class="mt-3 pr-1 text-right">
    <div class="mr-5" style="display: inline-block">
        <label for="perPage">Datensätze pro Seite:</label>
        <!-- EventHandler: im Skript popover.js -->
        <input id="perPage" type="number" class="form-control border-0" step="1" min="1" max="99" style="width: 4em; display: inline-block;" value="<%= perPage %>"/>
    </div>

    <% if (currentPage > 1) { %>
        <a style="display: inline-block" href="<%= prefix + href %>?currentPage=1&perPage=<%= perPage %>">1</a>
        <% if(lower > 1) { %>
            <span class="text-primary paginationPopover" data-pages="<%= Arrays.toString(pages) %>" data-cp="<%= currentPage + 1 %>">...</span>
        <% } %>
    <% } %>
    <% for(int i = lower; i < upper; i++) { %>
        <% if (i == currentPage) { %>
            <span style="color: #007bff; text-decoration: underline #007bff;"><%= i + 1 %></span>
        <% } else { %>
            <a style="display: inline-block;" href="<%= prefix + href %>?currentPage=<%= i + 1 %>&perPage=<%= perPage %>"><%= i + 1 %></a>
        <% } %>
    <% } %>
    <% if (currentPage < totalPages - 2) { %>
        <% if (upper < totalPages - 1) { %>
            <span class="text-primary paginationPopover" data-pages="<%= Arrays.toString(pages) %>" data-cp="<%= currentPage + 1 %>">...</span>
        <% } %>
        <a style="display: inline-block" href="<%= prefix + href %>?currentPage=<%= totalPages %>&perPage=<%= perPage %>"><%= totalPages %></a>
    <% } %>
</div>