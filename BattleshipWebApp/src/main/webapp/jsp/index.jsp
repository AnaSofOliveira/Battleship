<%
    session = request.getSession(false);
    String userIsLogged = (String) session.getAttribute("userIsLogged");

    if(userIsLogged != null){
        response.sendRedirect("home.jsp");
    }else{
        response.sendRedirect("login.jsp");
    }

%>