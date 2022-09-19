<jsp:include page="includes/header.jsp" />
<div id="background">
    <div id="optionsContainer">
        <h1>Escolha uma das opções abaixo:</h1>
        <form action="/BattleshipWebApp_war_exploded/server" method="post">
            <input type="hidden" name="tipo" value="convite" />
            <button type="submit">Convidar jogador</button>
        </form>
        <form action="/BattleshipWebApp_war_exploded/server" method="post">
            <input type="hidden" name="tipo" value="terminar" />
            <button type="submit">Sair</button>
        </form>
    </div>
</div>
<jsp:include page="includes/footer.jsp" />
