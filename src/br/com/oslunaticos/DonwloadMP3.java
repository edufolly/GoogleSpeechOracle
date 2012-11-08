package br.com.oslunaticos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 *
 * @author Eduardo Folly
 */
public class DonwloadMP3 implements Runnable {

    private static final String userAgent = "Mozilla/5.0 (Windows NT 6.1) "
            + "AppleWebKit/535.11 (KHTML, like Gecko) "
            + "Chrome/17.0.963.56 "
            + "Safari/535.11";
    //--
    private Tarefa tarefa;
    private boolean debug;

    public DonwloadMP3(Tarefa tarefa, boolean debug) {
        this.tarefa = tarefa;
        this.debug = debug;
    }

    @Override
    public void run() {
        try {
            URL url = tarefa.getURL();
            if(debug) {
                System.out.println(url.toString());
            }
            URLConnection con = url.openConnection();
            con.setRequestProperty("User-Agent", userAgent);
            con.setReadTimeout(5000);
            con.setDoOutput(true);
            con.setAllowUserInteraction(false);

            InputStream in = con.getInputStream();

            File saida = new File("temp_" + tarefa.getId() + ".mp3");
            OutputStream out = new FileOutputStream(saida);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            tarefa.setMp3(saida);
            
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
