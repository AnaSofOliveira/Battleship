package com.isel.battleship;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.Set;

public class XMLUtil {

    public static final Document documentFromFile(String inputFile) {
        try {
            return readDocument(new FileInputStream(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final Document readDocument(InputStream input) {
        Document xmlDoc = null;
        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // enable XInclude processing
            factory.setNamespaceAware( true );
            factory.setXIncludeAware( true );

            DocumentBuilder parser = factory.newDocumentBuilder();
            xmlDoc = parser.parse( input );

        } catch (Exception e) {
            System.err
                    .println("Error: Unable to read XML from stream!\n\t" + e);
            e.printStackTrace();
        }
        return xmlDoc;
    }

    public static Document jogar() {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element tag_protocolo = document.createElement("protocolo");
            Element tag_pedido = document.createElement("pedido");
            tag_pedido.setAttribute("tipo", "joga");

            tag_protocolo.appendChild(tag_pedido);
            document.appendChild(tag_protocolo);

            return document;
        }catch (ParserConfigurationException e) {
            System.out.println("ParserConfigurationException in jogar() from XMLUtil");
            return null;
        }
    }

    public static coordinate obtemCoordenasTiro(Document document) {

        Element tag_protocolo = document.getDocumentElement();

        Element tag_tiro = (Element) tag_protocolo.getElementsByTagName("tiro").item(0);

        int linha = Integer.parseInt(tag_tiro.getElementsByTagName("linha").item(0).getTextContent());
        int coluna = Integer.parseInt(tag_tiro.getElementsByTagName("coluna").item(0).getTextContent());

        return new coordinate(coluna-1, linha-1);
    }

    public static Document atualizaResultadoTiro(Document document, boolean resultado, Utilizador atacante) {

        // Criar um novo documento
        // Criar elmento pedido com tipo "atualiza"
        // Adicionar informação de reposta com resultado do tiro

        Element tag_protocolo = document.getDocumentElement();

        Element tag_pedido = (Element) tag_protocolo.getElementsByTagName("pedido").item(0);
        tag_pedido.setAttribute("tipo", "atualiza");
        Element tag_resposta = (Element) tag_pedido.getElementsByTagName("resposta").item(0);

        Element tag_atacante = document.createElement("atacante");
        tag_atacante.setTextContent(atacante.getNomeUtilizador());
        tag_resposta.appendChild(tag_atacante);

        tag_resposta.setAttribute("resultado", ((resultado) ? "Acertou" : "Falhou"));

        return document;
    }

    public static Document terminaJogo(Utilizador vencedor) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element tag_protocolo = document.createElement("protocolo");
            Element tag_pedido = document.createElement("pedido");
            tag_pedido.setAttribute("tipo", "termina");



            if(vencedor == null) {
                Element tag_estado = document.createElement("estado");
                tag_estado.setTextContent("Empatado");
                tag_pedido.appendChild(tag_estado);

            }else{
                Element tag_estado = document.createElement("estado");
                tag_estado.setTextContent("Ganho");

                Element tag_vencedor = document.createElement("vencedor");
                tag_vencedor.setTextContent(vencedor.getNomeUtilizador());

                tag_pedido.appendChild(tag_estado);
                tag_pedido.appendChild(tag_vencedor);
            }

            tag_protocolo.appendChild(tag_pedido);
            document.appendChild(tag_protocolo);

            return document;
        }catch (ParserConfigurationException e) {
            System.out.println("ParserConfigurationException in jogar() from XMLUtil");
            return null;
        }

    }

    public boolean writeDocument(Document document, final String output) {
        try {
            writeDocument(document, new FileOutputStream(output));
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static final void writeDocument(final Document input, final OutputStream output) {
        /* implementação da escrita da arvore num ficheiro recorrendo ao XSLT */
        try {
            DOMSource domSource = new DOMSource(input);
            StreamResult resultStream = new StreamResult(output);
            TransformerFactory transformFactory = TransformerFactory.newInstance();

            // transformação vazia

            Transformer transformer = transformFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            if (input.getXmlEncoding() != null)
                transformer.setOutputProperty(OutputKeys.ENCODING, input.getXmlEncoding());
            else
                transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            try {
                transformer.transform(domSource, resultStream);
            } catch (javax.xml.transform.TransformerException e) {

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validar(Document document, String xsdFile) {
        Schema schema = null;
        try {
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);
            File xsd=new File(xsdFile);
            schema = factory.newSchema(xsd);
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(document));
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found" + e.getMessage());
            return false;
        }catch(SAXException e) {
            System.out.println("SAX exception" + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
            return false;
        }
    }

    public static final String documentToString(Document xmlDoc) {
        Writer out = new StringWriter();
        try {
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1"); // "UTF-8"
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(new DOMSource(xmlDoc), new StreamResult(out));
        } catch (Exception e) {
            System.out.println("Error: Unable to write XML to string!\n\t" + e);
            e.printStackTrace();
        }
        return out.toString();
    }




    public static ArrayList<Utilizador> carregaUtilizadores(String XML_location, String XSD_location) {
        Document XML_utilizadores = XMLUtil.documentFromFile(XML_location);

        ArrayList<Utilizador> utilizadores = new ArrayList<Utilizador>();

        if(XMLUtil.validar(XML_utilizadores, XSD_location)) {
            Element root = XML_utilizadores.getDocumentElement();
            NodeList tag_utilizadores = root.getElementsByTagName("utilizador");
            for (int index_user = 0; index_user < tag_utilizadores.getLength(); index_user++) {
                Element tag_utilizador = (Element) tag_utilizadores.item(index_user);

                String nome_utilizador = tag_utilizador.getElementsByTagName("nome_utilizador").item(0).getTextContent();
                String password = tag_utilizador.getElementsByTagName("password").item(0).getTextContent();
                Element foto = (Element) tag_utilizador.getElementsByTagName("foto").item(0);
                String foto_src = foto.getAttribute("src");

                Utilizador utilizador = new Utilizador(nome_utilizador, password, foto_src);
                utilizadores.add(utilizador);
            }return utilizadores;
        }else {
            System.out.println("Erro na leitura do XML Utilizadores.");
            return null;
        }
    }

    public static Document listaUtilizadores(Document document, Set<Utilizador> utilizadores_online) {
        Element root = document.getDocumentElement();
        Element tag_pedido = (Element) root.getElementsByTagName("pedido").item(0);

        Element tag_resposta = document.createElement("resposta");
        Element tag_utlizadores = document.createElement("utilizadores");

        for (Utilizador utilizador: utilizadores_online) {
            Element tag_utilizador = document.createElement("utilizador");
            Element tag_nome = document.createElement("nome_utilizador");
            tag_nome.setTextContent(utilizador.getNomeUtilizador());
            tag_utilizador.appendChild(tag_nome);
            tag_utlizadores.appendChild(tag_utilizador);
        }

        tag_resposta.appendChild(tag_utlizadores);
        tag_pedido.appendChild(tag_resposta);
        return document;
    }

    public static String obtemTipoDoPedido(Document document) throws NullPointerException{
        Element root = document.getDocumentElement();
        Element tag_pedido = (Element) root.getElementsByTagName("pedido").item(0);
        String tipo = tag_pedido.getAttribute("tipo");

        return tipo;
    }

    public static Document resposta_login(Document document, String codigo, String motivo) {
        try {
            Element root = document.getDocumentElement();
            Element tag_pedido = (Element) root.getElementsByTagName("pedido").item(0);

            Element tag_resposta = document.createElement("resposta");

            Element code = document.createElement("codigo");
            code.setTextContent(codigo);
            Element message = document.createElement("mensagem");
            message.setTextContent(motivo);

            tag_resposta.appendChild(code);
            tag_resposta.appendChild(message);
            tag_pedido.appendChild(tag_resposta);

            return document;
        } catch (DOMException e) {
            System.err.println("Não foi possível criar resposta ao pedido login.");
            e.printStackTrace();
            return null;
        }
    }

    public static Document estado_convite(Document document, String mensagem) {
        Element root = document.getDocumentElement();
        Element tag_pedido = (Element) root.getElementsByTagName("pedido").item(0);

        Element tag_resposta = document.createElement("resposta");
        Element tag_estado = document.createElement("estado");
        tag_estado.setTextContent(mensagem);

        tag_resposta.appendChild(tag_estado);
        tag_pedido.appendChild(tag_resposta);

        return document;
    }

    public static String getNomeConvidado(Document document){
        Element tag_convidado = (Element)document.getElementsByTagName("convidado").item(0);
        String nome_convidado = tag_convidado.getTextContent();
        nome_convidado = nome_convidado.replaceAll("\\s", "");

        return nome_convidado;
    }

    public static String getNomeAnfitriao(Document document){
        Element tag_anfitriao = (Element)document.getElementsByTagName("anfitriao").item(0);
        String nome_anfitriao = tag_anfitriao.getTextContent();
        nome_anfitriao = nome_anfitriao.replaceAll("\\s", "");

        return nome_anfitriao;
    }

    public static Document setup_game(String nome_utilizador){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element tag_protocolo = document.createElement("protocolo");
            Element tag_pedido = document.createElement("pedido");
            tag_pedido.setAttribute("tipo", "setup_game");
            tag_pedido.setAttribute("jogador", nome_utilizador);

            Element tag_navios = document.createElement("navios");

            Element tag_portaAvioes = document.createElement("portaAvioes");
            tag_portaAvioes.setAttribute("tamanho", "5");
            tag_portaAvioes.setAttribute("quantidade", "1");

            Element tag_tanque = document.createElement("tanque");
            tag_tanque.setAttribute("tamanho", "4");
            tag_tanque.setAttribute("quantidade", "2");

            Element tag_contratorpedeiros = document.createElement("contratorpedeiros");
            tag_contratorpedeiros.setAttribute("tamanho", "3");
            tag_contratorpedeiros.setAttribute("quantidade", "3");

            Element tag_submarino = document.createElement("submarino");
            tag_submarino.setAttribute("tamanho", "2");
            tag_submarino.setAttribute("quantidade", "4");

            tag_navios.appendChild(tag_portaAvioes);
            tag_navios.appendChild(tag_tanque);
            tag_navios.appendChild(tag_contratorpedeiros);
            tag_navios.appendChild(tag_submarino);

            tag_pedido.appendChild(tag_navios);

            tag_protocolo.appendChild(tag_pedido);
            document.appendChild(tag_protocolo);

            return document;

        } catch (ParserConfigurationException e) {
            System.out.println("Não foi possível criar o pedido de iniciação do jogo.");
            System.out.println("ParserConfigurationException from setup_game() in XMLUtil.");
            return null;
        }
    }

    public static String obtemUtilizadorDaResposta(Document document) {
        //Element tag_protocolo = document.getDocumentElement();

        Element tag_pedido = (Element) document.getElementsByTagName("pedido").item(0);
        String nome_utilizador = tag_pedido.getAttribute("jogador");

        return  nome_utilizador;
    }

    public static ArrayList<Navio> obtemNavios(Document document) {
        System.out.println("** Recebi no obtemNavios() do XMLUtil: " + documentToString(document));

        ArrayList<Navio> navios = new ArrayList<>();
        Element tag_protocolo = document.getDocumentElement();

        Element tag_navios = (Element) tag_protocolo.getElementsByTagName("navios").item(1);

        NodeList tags_navio = tag_navios.getChildNodes();

        for( int index = 0; index < tags_navio.getLength(); index++){
            Element tag_nome_navio = (Element) tags_navio.item(index);

            String nome_navio = tag_nome_navio.getTagName();


            Element tag_posIni = (Element) tag_nome_navio.getElementsByTagName("posIni").item(0);
            int posIniX = Integer.parseInt(tag_posIni.getElementsByTagName("coluna").item(0).getTextContent());
            int posIniY = Integer.parseInt(tag_posIni.getElementsByTagName("linha").item(0).getTextContent());

            Element tag_posFin = (Element) tag_nome_navio.getElementsByTagName("posFin").item(0);
            int posFinX = Integer.parseInt(tag_posFin.getElementsByTagName("coluna").item(0).getTextContent());
            int posFinY = Integer.parseInt(tag_posFin.getElementsByTagName("linha").item(0).getTextContent());

            coordinate posIni = new coordinate(posIniX, posIniY);
            coordinate posFin = new coordinate(posFinX, posFinY);
            char sigla = nome_navio.toUpperCase().charAt(0);

            Navio navio = new Navio(sigla, posIni, posFin);
            navios.add(navio);
        }

        System.out.println("*** Navios: " + navios);
        return navios;
    }
}
