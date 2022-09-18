<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Battleship</title>
    <link rel="stylesheet" type="text/css" href="../css/login.css">
</head>
<body>
<div id="background">
    <h1>Battleship Game - Login</h1>
    <div id="loginContainer">
        <div id="inputContainer">

            <form id="loginForm" action="/BattleshipWebApp_war_exploded/server" method="post">
                <h2>Login to your account</h2>
                <input type="hidden" name="tipo" value="login" />
                <p>
                    <label for="loginNickname">Username</label>
                    <input id="loginNickname" name="loginNickname" type="text" required>
                </p>
                <p>
                    <label for="loginPassword">Password</label>
                    <input id="loginPassword" name="loginPassword" type="password" required>
                </p>

                <button type="submit" name="loginButton">Login</button>
            </form>

        </div>

    </div>
</div>

</body>
</html>