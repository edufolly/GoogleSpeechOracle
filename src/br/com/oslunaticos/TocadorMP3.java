package br.com.oslunaticos;

import java.util.List;
import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Manager;
import javax.media.Player;

/**
 *
 * @author Eduardo Folly
 */
public class TocadorMP3 implements Runnable, ControllerListener {

    private List<Tarefa> tarefas;
    private int atual = 0;
    private Player player;
    private boolean debug;

    public TocadorMP3(List<Tarefa> tarefas, boolean debug) {
        this.tarefas = tarefas;
        this.debug = debug;
    }

    @Override
    public void run() {
        while (atual < tarefas.size()) {
            try {
                int tmp = atual;
                Tarefa t = tarefas.get(atual);
                player = Manager.createRealizedPlayer(t.getMp3().toURI().toURL());
                player.addControllerListener(this);
                player.start();
                if (debug) {
                    System.out.println(t.getMensagem());
                }
                long sleep = player.getDuration().getNanoseconds() / 1000000;
                Thread.sleep(sleep);
                while (atual == tmp) {
                    Thread.sleep(10);
                }
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }

    }

    @Override
    public void controllerUpdate(ControllerEvent ce) {
        if (ce.getSourceController().getTargetState() == Controller.Prefetched) {
            atual++;
        }
    }
}
