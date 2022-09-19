![](RackMultipart20220919-1-5lke8i_html_87fc0c4501c3065f.jpg)

## Licenciatura em Engenharia Informática e de Multimédia

##
# Infraestruturas Computacionais Distribuídas

## Trabalho Prático Final

## Data Entrega: 19 de setembro de 2022
## Engenheiro: Diogo Remédios
 
## Alunos: 
## Ana Oliveira 39275
## Tiago Carvalho 37726

# Semestre Verão 21/22

| Data Entrega: | 19 de setembro de 2022 |
| --- | --- |
| Engenheiro: | Diogo Remédios |
| --- | --- |
| Alunos: |
| Ana Oliveira | 39275 |
| Tiago Carvalho | 37726 |

 | Semestre Verão 21/22 |


Índice

[Introdução 3](#_Toc114500741)

[Desenvolvimento 4](#_Toc114500742)

[Arquitetura Cliente-Servidor 4](#_Toc114500743)

[Mensagens para Login 8](#_Toc114500744)

[Mensagens para listar utilizadores 9](#_Toc114500745)

[Mensagens para convidar utilizadores 10](#_Toc114500746)

[Mensagens para iniciar o jogo 12](#_Toc114500747)

[Mensagens para ataque 14](#_Toc114500748)

[Mensagem para término do jogo 15](#_Toc114500749)

[Arquitetura Web 16](#_Toc114500750)

[Visão Cliente 17](#_Toc114500751)

[Aplicação Stand-alone 17](#_Toc114500752)

[Login 17](#_Toc114500753)

[Enviar Convite 17](#_Toc114500754)

[Aceitar Convite 18](#_Toc114500755)

[Inserir 18](#_Toc114500756)

[Jogadas 19](#_Toc114500757)

[Terminar jogo 20](#_Toc114500758)

[Múltiplos jogadores 21](#_Toc114500759)

[Aplicação Web 22](#_Toc114500760)

[Login 22](#_Toc114500761)

[Home Page 22](#_Toc114500762)

[Conclusões 23](#_Toc114500763)

Índice de Figuras

[Figura 1 - Arquitetura Cliente - Servidor 4](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500654)

[Figura 2 - UML Servidor 5](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500655)

[Figura 3 - UML Cliente 6](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500656)

[Figura 4 - Estrutura Pedido XML 7](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500657)

[Figura 5 - Estrutura Resposta XML 7](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500658)

[Figura 6 - Exemplo pedido login 8](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500659)

[Figura 7 - Exemplo resposta login com sucesso 8](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500660)

[Figura 8 - Exemplo resposta login de jogador login já efetuado 8](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500661)

[Figura 9 - Exemplo resposta login com password incorreta 9](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500662)

[Figura 10 - Exemplo pedido listar utilizadores 9](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500663)

[Figura 11 - Exemplo resposta listar utilizadores 9](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500664)

[Figura 12 - Exemplo pedido convite 10](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500665)

[Figura 13 - Exemplo resposta convite enviado 10](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500666)

[Figura 14 - Exemplo resposta convite recusado 11](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500667)

[Figura 15 - Exemplo resposta convite aceite 11](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500668)

[Figura 16 - Exemplo pedido para configurar jogo 12](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500669)

[Figura 17 - Exemplo resposta para configurar jogo 13](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500670)

[Figura 18 - Exemplo pedido para jogar 14](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500671)

[Figura 19 - Exemplo resposta para jogar 14](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500672)

[Figura 20 - Exemplo pedido para terminar jogo 15](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500673)

[Figura 21 - App - Iniciar sessão 17](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500674)

[Figura 22 - App - Convidar 17](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500675)

[Figura 23 - App - Inserir navios (início) 18](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500676)

[Figura 24 - App - Inserir navios (final) 19](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500677)

[Figura 25 - App - Jogada/ Tiros 19](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500678)

[Figura 26 - App - Jogo terminado 20](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500679)

[Figura 27 - App - 2 Jogos a decorrer 21](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500680)

[Figura 28 - Web App Login 22](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500681)

[Figura 29 - Web App Home Page 22](/C:/Users/anaso/Documents/IECD/Trabalhos%20Pr%C3%A1ticos/Battleship/ICD%20Relat%C3%B3rio%20-%20Conte%C3%BAdo.docx#_Toc114500682)

#


# Introdução

No âmbito do desenvolvimento da componente prática da disciplina de Infraestruturas Computacionais Distribuídas, foi proposto o desenvolvimento, ao longo do semestre, do jogo multijogador da Batalha Naval, onde os jogadores tentam afundar a armada do adversário.

No início do jogo, cada jogador posiciona os seus navios, representados por um grupo de quadrados em linha reta que não podem ser contíguos a outro navio.

Cada jogador dispõe dos seguintes navios:

- 1 porta-aviões (cinco quadrados);
- 2 navios-tanque (quatro quadrados);
- 3 contratorpedeiros (três quadrados);
- 4 submarinos (dois quadrados).

Cada jogador possui uma grelha que apresenta a disposição dos seus navios.

Em cada turno, um jogador indica um quadrado da grelha do adversário. Se houver um navio nessa posição é identificado tiro certeiro, caso contrário é identificado tiro na água. O jogo termina quando um jogador adivinhar todas as posições de todos os navios do adversário.

Os jogadores farão um auto-registo indicando o nickname, password e foto. Na componente de multijogador, cada jogador poderá convidar outros jogadores previamente "logados" no sistema que não estejam a participar noutras partidas, e iniciar de imediato uma nova partida após o convite ter sido aceite.

O jogo proposto deverá ser capaz de estabelecer a comunicação entre todos os clientes no sistema, bem como manter a informação estritamente necessária sobre os jogadores e os jogos em curso. Assim, será necessário implementar um sistema com base numa arquitetura cliente-servidor e definir cuidadosamente as mensagens trocadas entre as diversas componentes do sistema.

No decorrer deste documento iremos apresentar as diversas abordagens e tomadas de decisões necessárias de forma a garantir o cumprimento dos requisitos.

# Desenvolvimento

O sistema proposto, conforme indicado anteriormente, deve ser capaz de estabelecer a comunicação entre diversos jogadores que se autenticam no mesmo. Assim, é necessário manter a informação sobre todos os jogadores autenticados, bem como sobre os jogos em curso. Para isso, iniciámos o processo de desenvolvimento pela construção da arquitetura cliente-servidor do nosso sistema.

## Arquitetura Cliente-Servidor

Cada cliente solicita recursos ao servidor e recebe uma resposta de acordo com o pedido efetuado. Neste sistema existem múltiplos clientes a efetuar pedidos em indeterminados instantes de tempo, pelo que, de forma que os tempos de resposta ao servidor fossem minimizados e nenhum pedido ficasse a aguardar a resposta ao pedido do cliente anterior, a arquitetura cliente-servidor foi desenvolvida com base no modelo concorrente. Assim, todos os pedidos são atendidos de forma concorrente, ou seja, em paralelo.

No contexto do trabalho desenvolvido, tanto os clientes como o servidor irão ficar alojados na mesma máquina, pelo que ambos partilham o mesmo IP, porém com portos diferentes. O servidor ficará alojado no IP correspondente ao localhost e no porto 5025. Os clientes ficarão igualmente alojados no IP do localhost, porém noutro porto disponível na máquina (cada cliente terá obrigatoriamente um porto diferente).

Para estabelece a comunicação entre os clientes e o servidor recorremos ao protocolo de transporte TCP de forma que todas as mensagens fossem entregues mantendo a ordem de envio das mesmas.

![Shape1](RackMultipart20220919-1-ajmesn_html_e47667524bd292b2.gif)

Nas imagens abaixo são apresentados os diagramas UML do projeto Servidor e Cliente implementados.

![Shape2](RackMultipart20220919-1-ajmesn_html_65c806b6feba5dde.gif)

![Shape3](RackMultipart20220919-1-ajmesn_html_4c30e76b25d0e658.gif)

Esta arquitetura opera com base num modelo de pedido-resposta, pelo que as mensagens trocadas para realizar os pedidos e repostas devem ser cuidadas e bem definidas. Assim, definimos as mensagens trocadas entre o cliente e o servidor. Todas as mensagens foram definidas em XML e obedecem às regras de validação criadas em XSD.

O protocolo de comunicação criado obedece à seguinte estrutura:

![Shape4](RackMultipart20220919-1-ajmesn_html_1642e493e0b72d60.gif)

![Shape5](RackMultipart20220919-1-ajmesn_html_8f10c4b421933528.gif)

## ![Shape6](RackMultipart20220919-1-ajmesn_html_6274405897ac51e0.gif) Mensagens para Login

Os dados dos utilizadores anteriormente "logados" são armazenados no ficheiro _utilizadores.xml_. Assim, após o auto-registo do jogador é possível verificar se a autenticação é válida ou não. Desta forma, podem ser produzidas várias respostas para o login. Adicionalmente, o servidor apenas permite uma sessão por jogador, pelo que se o jogador se tentar autenticar com uma sessão ativa não conseguirá fazê-lo.

![Shape7](RackMultipart20220919-1-ajmesn_html_c0f22c8f223fe9b4.gif)

![Shape8](RackMultipart20220919-1-ajmesn_html_15ddf7679399be10.gif)

![Shape9](RackMultipart20220919-1-ajmesn_html_72a3c105112b9c84.gif)

##


## Mensagens para listar utilizadores

Os jogadores podem listar os utilizadores disponíveis para iniciar um novo jogo.

![Shape10](RackMultipart20220919-1-ajmesn_html_758a69d1fb2ef46e.gif)

![Shape11](RackMultipart20220919-1-ajmesn_html_342be523eaee8223.gif)

## Mensagens para convidar utilizadores

A mensagem para o convite apenas é enviada após o jogador indicar o nickname do jogador que pretende convidar para iniciar um novo jogo. Assim, inicialmente é enviada a mensagem para listar os utilizadores, apresentada acima, e, posteriormente, é enviada uma mensagem de convite para o jogador indicado.

![Shape12](RackMultipart20220919-1-ajmesn_html_6d7a3b400e2356f9.gif)

O servidor reencaminha o pedido com o convite para o jogador convidado. Após ter enviado esta mensagem, o servidor responde, com a seguinte mensagem, ao jogador que efetuou o convite sinalizando que o mesmo foi enviado.

![Shape13](RackMultipart20220919-1-ajmesn_html_ea9cbcf1226c31b1.gif)

O jogador convidado recebe a seguinte mensagem, podendo aceitar ou recusar o convite. Caso o jogador convidado recuse o convite, envia a seguinte mensagem para o servidor, propagando-a até ao jogador que iniciou o convite.

![Shape14](RackMultipart20220919-1-ajmesn_html_51fc64c8f83d1e6a.gif)

Caso o jogador convidado aceite o convite, o servidor, ao receber a mensagem abaixo, envia uma nova mensagem para ambos os jogadores iniciarem o jogo.

![Shape15](RackMultipart20220919-1-ajmesn_html_43a7254fa0ab8179.gif)

## Mensagens para iniciar o jogo

O pedido para iniciar o jogo é proveniente do servidor indicando quais os navios necessários, a quantidade de determinado tipo de navio e o tamanho que cada um ocupa no tabuleiro.

![Shape16](RackMultipart20220919-1-ajmesn_html_b3b723b2df3c6a51.gif)

O jogador indica a posição inicial e a posição final de cada navio respondendo da seguinte forma ao servidor:

![Shape17](RackMultipart20220919-1-ajmesn_html_26338543c7c74a72.gif)

## Mensagens para ataque

Após estarem configurados os tabuleiros de ambos os jogadores, estes iniciam o jogo propriamente dito, atacando estrategicamente os navios do adversário. Para isso, cada jogador deve indicar a posição a atacar enviando a seguinte mensagem ao servidor.

![Shape18](RackMultipart20220919-1-ajmesn_html_8b49f2b979ee73ce.gif)

![Shape19](RackMultipart20220919-1-ajmesn_html_aadb8990463dfa0f.gif)

## Mensagem para término do jogo

Quando o servidor deteta que um dos tabuleiros dos jogadores não tem mais navios por afundar, envia a mensagem abaixo para ambos os jogadores, indicado que o jogo terminou e anunciando o vencedor.

Caso um dos jogadores abandone o jogo, esta mensagem também é enviada para o outro jogador, anunciando-o vencedor.

![Shape21](RackMultipart20220919-1-ajmesn_html_8de89439e8209616.gif)

_Figura 20 - Exemplo pedido para terminar jogo_

\<protocolo\>

\<pedido tipo="termina"\>

\<estado\>Ganho\</estado\>

\<vencedor\>Ana\</vencedor\>

\</pedido\>

\</protocolo\>

# Arquitetura Web

Nesta fase pretendíamos, mantendo os objetivos anteriormente indicados, escalar o nosso sistema, de forma que o jogador pudesse jogar através de uma aplicação ou através de qualquer browser com acesso à internet. Assim, desenvolvemos um novo cliente recorrendo a um servidor Web. Este servidor Web opera como um cliente do nosso servidor anteriormente implementado, pelo que este apenas opera como intermediário (proxy) entre o browser e o nosso servidor anteriormente desenvolvido.

Para implementar a visão do cliente no browser e as operações a enviar pelo servidor Web foi necessário recorrer a Servlets e JSP. A Servlet Servidor é responsável por interpretar a operação a realizar do lado do cliente web, enviar a mensagem ao servidor local e aguardar a resposta. Após isto, e mediante o tipo da resposta, o browser apresenta a página correspondente, codificada em JSPs.

# Visão Cliente

## Aplicação Stand-alone

### Login

![Shape22](RackMultipart20220919-1-ajmesn_html_36e26e713877f1a2.gif)

### Enviar Convite

![Shape23](RackMultipart20220919-1-ajmesn_html_117d0c03255e59bd.gif)

### Aceitar Convite

![](RackMultipart20220919-1-ajmesn_html_b66b80b2c7612d9.png)

### Inserir

![](RackMultipart20220919-1-ajmesn_html_573fc066da0ee9f1.png)

_Figura 23 - App - Inserir navios (início)_

![Shape25](RackMultipart20220919-1-ajmesn_html_3247b57e481c6168.gif)

### Jogadas

![Shape26](RackMultipart20220919-1-ajmesn_html_a3dd10c9069ca9ce.gif)

### Terminar jogo

![Shape27](RackMultipart20220919-1-ajmesn_html_a2fcd7ee1b0fb35f.gif)

### ![Shape28](RackMultipart20220919-1-ajmesn_html_899c0d126f5df06d.gif) Múltiplos jogadores

## Aplicação Web

### Login

![Shape29](RackMultipart20220919-1-ajmesn_html_c4c7ce3a4c1a7270.gif)

### Home Page

![Shape30](RackMultipart20220919-1-ajmesn_html_488ab3435a4a05e5.gif)

# Conclusões

Durante o desenvolvimento do trabalho proposto foi possível abordar diversas temáticas, tais como:

- Comunicação entre sockets, usando linguagem JAVA;
- Comunicação em rede com base no protocolo de transporte TCP;
- Desenvolvimento de sistema com arquitetura Cliente-Servidor;
- Desenvolvimento de sistema com arquitetura Web.

No geral, o objetivo da primeira componente do trabalho foi conseguido com sucesso. Existem, no entanto, alguns aspetos a melhorar, nomeadamente:

- Validação do protocolo através de XSD;
- Encriptação e validação das passwords dos jogadores;
- Otimizar a estrutura do protocolo XML usado;
- Implementar interface gráfica para os utilizadores.

Em relação à segunda componente do trabalho, esta não foi concluída com sucesso sendo apenas possível efetuar o login recorrendo ao protocolo de comunicação definido na primeira componente deste trabalho. Assim, existem os seguintes aspetos a implementar e melhorar:

- Implementação de ecrãs de convite e jogo;
- Otimização da interface de utilizador;
- Implementação de sistema por sessões para identificação dos jogadores no servidor Web.

Por fim, concluímos que os objetivos das duas componentes do trabalho prático foram parcialmente concluídos. No entanto, foi realizada a abordagem a todos os conteúdos teóricos lecionados na componente prática, tendo faltando apenas a abordagem às sessões na arquitetura Web.