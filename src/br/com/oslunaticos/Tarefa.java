package br.com.oslunaticos;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author Eduardo Folly
 */
public class Tarefa {

    private static final String urlBase = "http://translate.google.com/translate_tts?";
    private static final String encode = "UTF-8";
    //--
    private String idioma;
    private int id;
    private int total;
    private String mensagem;
    private File mp3;

    public Tarefa() {
    }

    public Tarefa(int id, int total, String mensagem, String idioma) {
        this.id = id;
        this.total = total;
        this.mensagem = mensagem;
        this.idioma = idioma;
    }

    public int getId() {
        return id;
    }

    public int getTotal() {
        return total;
    }

    public String getMensagem() {
        return mensagem;
    }

    public URL getURL() throws UnsupportedEncodingException, MalformedURLException {
        String msgEncoded = URLEncoder.encode(mensagem, encode);

        String url = "";
        url += urlBase;
        url += "ie=" + encode + "&";
        url += "q=" + msgEncoded + "&";
        url += "tl=" + idioma + "&";
        url += "total=" + total + "&";
        url += "idx=" + id + "&";
        url += "textlen=" + mensagem.length() + "&";
        url += "prev:input";

        return new URL(url);
    }

    @Override
    public String toString() {
        return id + "/" + total + ": " + mensagem + "(" + mensagem.length() + ") " + idioma;
    }

    public File getMp3() {
        return mp3;
    }

    public void setMp3(File mp3) {
        this.mp3 = mp3;
    }
}
