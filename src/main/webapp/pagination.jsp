<%--
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
%>

<div>
    <% for(int i = 0; i < totalPages; i++) { %>
        <a style="display: inline-block" href="<%= prefix + href %>?currentPage=<%= i + 1 %>"><%= i + 1 %></a>
    <% } %>
</div>