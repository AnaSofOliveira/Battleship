package com.isel.battleship;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.print.Doc;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class XMLUtil {

    public static final Document documentFromString(String strXML) {
        Document xmlDoc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            xmlDoc = builder.parse(new InputSource(new StringReader(strXML)));
        } catch (Exception e) {
            System.err
                    .println("Error: Unable to read XML from string!\n\t" + e);
            e.printStackTrace();
        }
        return xmlDoc;
    }

    public static Document login(String user, String pass, String src) {
        Document document =  null;

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            Element tag_protocolo = document.createElement("protocolo");
            Element tag_pedido = document.createElement("pedido");
            tag_pedido.setAttribute("tipo", "login");
            Element tag_utilizador = document.createElement("nome_utilizador");
            tag_utilizador.setTextContent(user);
            Element tag_password = document.createElement("password");
            tag_password.setTextContent(pass);
            Element tag_foto = document.createElement("foto");
            tag_foto.setAttribute("src", src);
            tag_pedido.appendChild(tag_utilizador);
            tag_pedido.appendChild(tag_password);
            tag_pedido.appendChild(tag_foto);
            tag_protocolo.appendChild(tag_pedido);
            document.appendChild(tag_protocolo);

        } catch (ParserConfigurationException e) {
            System.err.println("Não foi possível criar pedido Login.");
        }
        return document;
    }

    public static boolean loginValido(Document document) {
        Element root = document.getDocumentElement();
        Element tag_resposta = (Element) root.getElementsByTagName("resposta").item(0);

        Element tag_codigo = (Element) tag_resposta.getElementsByTagName("codigo").item(0);
        String codigo = tag_codigo.getTextContent();

        if(codigo.equals("Autorizado")){
            return true;
        }return false;

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

    public static Document listaUtilizadores() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element tag_protocolo = document.createElement("protocolo");
            Element tag_pedido = document.createElement("pedido");
            tag_pedido.setAttribute("tipo", "listaUtilizadores");
            tag_protocolo.appendChild(tag_pedido);
            document.appendChild(tag_protocolo);
            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document convida(String from, String to) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element tag_protocolo = document.createElement("protocolo");
            Element tag_pedido = document.createElement("pedido");
            tag_pedido.setAttribute("tipo", "convite");

            Element tag_anfitriao = document.createElement("anfitriao");
            Element tag_user_anf = document.createElement("nome_utilizador");
            tag_user_anf.setTextContent(from);

            Element tag_convidado = document.createElement("convidado");
            Element tag_user_conv = document.createElement("nome_utilizador");
            tag_user_conv.setTextContent(to);

            tag_anfitriao.appendChild(tag_user_anf);
            tag_convidado.appendChild(tag_user_conv);

            tag_pedido.appendChild(tag_anfitriao);
            tag_pedido.appendChild(tag_convidado);
            tag_protocolo.appendChild(tag_pedido);
            document.appendChild(tag_protocolo);
            return document;

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Document respondeConvite(Document document, String resposta) {
        Element convidado_tag = (Element)document.getElementsByTagName("convidado").item(0);

        Element tag_protocolo = document.getDocumentElement();
        Element tag_pedido = (Element) tag_protocolo.getElementsByTagName("pedido").item(0);

        Element tag_resposta = document.createElement("resposta");
        Element tag_estado = document.createElement("estado");
        tag_estado.setTextContent(resposta);

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


    public static ArrayList<Map> obtemInfoNavios(Document document) {
        ArrayList<Map> info_navios = new ArrayList<>();
        Map<String, String> info_navio = new HashMap<String, String>();

        System.out.println("XPTO: " + documentToString(document));

        Element tag_protocolo = document.getDocumentElement();

        Element tag_navios = (Element) tag_protocolo.getElementsByTagName("navios").item(0);
        NodeList navios = tag_navios.getChildNodes();

        for( int index = 0; index < navios.getLength(); index++){
            Element navio = (Element) navios.item(index);

            String tipo_navio = navio.getTagName();
            String tamanho_navio = navio.getAttribute("tamanho");
            String quantidade = navio.getAttribute("quantidade");

            info_navio.put("tipo", tipo_navio);
            info_navio.put("tamanho", tamanho_navio);
            info_navio.put("quantidade", quantidade);

            info_navios.add(Map.copyOf(info_navio));

        }



        return info_navios;

    }

    public static Document setup_game(Document document, ArrayList<Navio> navios_a_jogo) {

        Element tag_protocolo = document.getDocumentElement();
        Element tag_pedido = (Element) tag_protocolo.getElementsByTagName("pedido").item(0);

        Element tag_resposta = document.createElement("resposta");
        Element tag_navios = document.createElement("navios");

        for(Navio navio: navios_a_jogo){
            String nome_navio = navio.getNome();
            int tamanho = navio.getTamanho();
            Coordenada posIni = navio.getLocalizacao_inicial();
            Coordenada posFin = navio.getLocalizacao_final();

            Element tag_navio = document.createElement(nome_navio);
            tag_navio.setAttribute("tamanho", String.valueOf(tamanho));

            Element tag_posIni = document.createElement("posIni");
            Element tag_linhaIni = document.createElement("linha");
            tag_linhaIni.setTextContent(String.valueOf(posIni.getY()));

            Element tag_colunaIni = document.createElement("coluna");
            tag_colunaIni.setTextContent(String.valueOf(posIni.getX()));

            Element tag_posFin = document.createElement("posFin");
            Element tag_linhaFin = document.createElement("linha");
            tag_linhaFin.setTextContent(String.valueOf(posFin.getY()));

            Element tag_colunaFin = document.createElement("coluna");
            tag_colunaFin.setTextContent(String.valueOf(posFin.getX()));

            tag_posIni.appendChild(tag_linhaIni);
            tag_posIni.appendChild(tag_colunaIni);

            tag_posFin.appendChild(tag_linhaFin);
            tag_posFin.appendChild(tag_colunaFin);

            tag_navio.appendChild(tag_posIni);
            tag_navio.appendChild(tag_posFin);

            tag_navios.appendChild(tag_navio);
        }

        tag_resposta.appendChild(tag_navios);
        tag_pedido.appendChild(tag_resposta);
        return document;

    }

    public static Document tiro(Document document, Coordenada coord){

        Element tag_protocolo = document.getDocumentElement();
        Element tag_pedido = (Element) tag_protocolo.getElementsByTagName("pedido").item(0);

        Element tag_resposta = document.createElement("resposta");
        Element tag_tiro = document.createElement("tiro");
        Element tag_linha = document.createElement("linha");
        Element tag_coluna = document.createElement("coluna");

        tag_linha.setTextContent(String.valueOf(coord.getY()));
        tag_coluna.setTextContent(String.valueOf(coord.getX()));

        tag_tiro.appendChild(tag_linha);
        tag_tiro.appendChild(tag_coluna);

        tag_resposta.appendChild(tag_tiro);

        tag_pedido.appendChild(tag_resposta);

        return document;
    }

    public static Coordenada getCoordenadasTiro(Document document) {

        Element tag_protocolo = document.getDocumentElement();

        Element tag_tiro = (Element) tag_protocolo.getElementsByTagName("tiro").item(0);

        int linha = Integer.parseInt(tag_tiro.getElementsByTagName("linha").item(0).getTextContent());
        int coluna = Integer.parseInt(tag_tiro.getElementsByTagName("coluna").item(0).getTextContent());

        return new Coordenada(coluna-1, linha-1);
    }

    public static String getNomeAtacante(Document document) {

        Element tag_protocolo = document.getDocumentElement();

        Element tag_resposta = (Element) tag_protocolo.getElementsByTagName("resposta").item(0);

        String atacante = tag_resposta.getElementsByTagName("atacante").item(0).getTextContent();

        return atacante;
    }

    public static boolean getResultadoTiro(Document document) {
        Element tag_protocolo = document.getDocumentElement();

        Element tag_resposta = (Element) tag_protocolo.getElementsByTagName("resposta").item(0);

        boolean resultado = (tag_resposta.getAttribute("resultado")).equals("Acertou") ? true : false;

        return resultado;
    }
}
