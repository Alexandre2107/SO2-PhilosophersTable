package Jantar_Dysktstra;

/**
 *
 * @author Alexandre Rodrigues
 */
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Filosofo extends Thread {

    private final Estados estados;
    private final int idFilosofo;
    private boolean comeu;
    private int n;
    private boolean rodar;

    public Filosofo(int id, Estados estados) {
        this.idFilosofo = id;
        this.estados = estados;
        this.rodar = true;
    }

    public void parar() {
        this.rodar = false;
    }

    @Override
    public void run() {
        while (rodar) {
            pensar(idFilosofo);
            verificarSePodeComer(idFilosofo);
            comer(idFilosofo);
        }
        printFinal(idFilosofo);
    }

    public synchronized void pensar(int i) {
        Random gerador = new Random();
        int tempo = gerador.nextInt(500);
        try {
            Thread.sleep(tempo);
        } catch (InterruptedException E) {
            System.out.println("Erro");
        }
        if (comeu == false) {
            estados.setMediaDeEspera(i, estados.getMediaDeEspera(i) + tempo);
            estados.setTempoSemComer(i, estados.getTempoSemComer(i) + tempo);
        }
        if (comeu == true) {
            estados.setTempoSemComer(i, tempo);
        }
        if (estados.getTempoSemComer(i) > estados.getTempoMaximoSemComer(i)) {
            estados.setTempoMaximoSemComer(i, estados.getTempoSemComer(i));
        }
        estados.setEstado(i, 1);
    }

    public synchronized void verificarSePodeComer(int i) {
        if (estados.getEstado(i) == 1 && estados.getEstado((i + 4) % 5) != 2 && estados.getEstado((i + 1) % 5) != 2) {
            estados.setEstado(i, 2);
        }
        if (estados.getEstado(i) != 2) {
            comeu = false;
        }
    }

    public synchronized void comer(int i) {
        if (estados.getEstado(i) == 2) {
            try {
                Random gerador = new Random();
                int tempo = gerador.nextInt(500);
                Thread.sleep(tempo);
                estados.setContador(i, estados.getContador(i) + 1);
                estados.setEstado(i, 0);
                estados.setMediaDeEspera(i, estados.getMediaDeEspera(i) + tempo);
                n++;
                comeu = true;
                estados.setTempoSemComer(i, 0);
            } catch (InterruptedException ex) {
                Logger.getLogger(Filosofo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public int getN() {
      return n;
    }

    public synchronized void printFinal(int i) {
        System.out.println("Implementacao do Livro: \nFilosofo : " + i);
        System.out.println("Tempo sem Comer: " + estados.getTempoSemComer(i));
        System.out.println("Media de Espera: " + (estados.getMediaDeEspera(i) / n));
        System.out.println("Tempo Maximo de Espera: " + estados.getTempoMaximoSemComer(i));
        System.out.println("Vezes que Comeu : " + estados.getContador(i));
        System.out.println();
    }
}

