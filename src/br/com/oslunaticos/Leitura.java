package br.com.oslunaticos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Eduardo Folly
 */
public class Leitura implements Runnable {

    private static final boolean debug = true;
    //--
    private Controladora leitura;
    private String texto;
    private String idioma;

    public Leitura(Controladora leitura, String texto, String idioma) {
        this.leitura = leitura;
        this.texto = texto;
        this.idioma = idioma;
    }

    @Override
    public void run() {
        try {
            List<String> partes = dividir(texto);
            List<Tarefa> tarefas = new ArrayList();

            int total = partes.size();
            int i = 1;

            for (String s : partes) {
                Tarefa t = new Tarefa(i, total, s, idioma);
                if (debug) {
                    System.out.println(t.toString());
                }
                tarefas.add(t);
                i++;
            }

            int numPool = tarefas.size();

            ExecutorService tpes = Executors.newFixedThreadPool(numPool);

            DonwloadMP3[] downloads = new DonwloadMP3[numPool];
            for (int x = 0; x < tarefas.size(); x++) {
                Tarefa t = tarefas.get(x);
                downloads[x] = new DonwloadMP3(t, debug);
                tpes.execute(downloads[x]);
            }

            tpes.shutdown();
            while (!tpes.isTerminated()) {
                tpes.awaitTermination(500, TimeUnit.MILLISECONDS);
            }

            for (Tarefa t : tarefas) {
                if (t.getMp3() == null) {
                    throw new NoMP3FoundException();
                }
                if (!t.getMp3().isFile()) {
                    throw new NoMP3FoundException();
                }
            }

            TocadorMP3 tocador = new TocadorMP3(tarefas, debug);
            tocador.run();

            leitura.concluido();
            if (debug) {
                System.out.println("CONCLUÍDO");
            }
        } catch (Exception ex) {
            leitura.erro(ex);
        }
    }

    private List<String> dividir(String txt) throws StringDivisionException {
        List<String> partes = new ArrayList();

        while (txt.length() > 100) {
            int pos = (txt.length() > 100) ? 100 : txt.length();
            String tempTexto = txt.substring(0, pos);
            int i;
            for (i = pos - 1; i >= 0; i--) {
                char c = tempTexto.charAt(i);
                if (c == '.' || c == ',' || c == '!' || c == '?'
                        || c == ';' || c == ':') {
                    break;
                }
            }
            if (i == 0) {
                throw new StringDivisionException();
            }
            i++;
            partes.add(txt.substring(0, i));
            txt = txt.substring(i).trim();
        }

        partes.add(txt);
        return partes;
    }
}
