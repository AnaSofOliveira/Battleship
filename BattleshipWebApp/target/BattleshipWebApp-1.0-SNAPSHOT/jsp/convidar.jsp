<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: anaso
  Date: 18/09/2022
  Time: 19:31
  To change this template use File | Settings | File Templates.
--%>
<jsp:include page="includes/header.jsp"/>
<div id="background">
    <div id="usersContainer">
        <%
            Object utilizadores = (List<String>) session.getAttribute("utilizadores");
            List<String> u;
            if(utilizadores instanceof List){
                u = (List<String>) utilizadores;
            }

        %>

        <p>Utilizadores</p>
        <p><% System.out.println(utilizadores); %></p>

    </div>
</div>
<jsp:include page="includes/footer.jsp" />