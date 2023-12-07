package Janta_MinhaImplementacao;

import java.util.concurrent.Semaphore;

class Filosofo implements Runnable {
    private final int id;
    private final Semaphore[] garfos;
    private int vezesComeu = 0;
    private long tempoEsperaTotal = 0;
    private long tempoMaxEspera = 0;
    private volatile boolean running = true;

    public Filosofo(int id, Semaphore[] garfos) {
        this.id = id;
        this.garfos = garfos;
    }

    private void pensar() throws InterruptedException {
        Thread.sleep((long) (Math.random() * 400 + 100)); // Entre 0,1 e 0,5 segundos
    }

    private void comer() throws InterruptedException {
        Thread.sleep((long) (Math.random() * 400 + 100)); // Entre 0,1 e 0,5 segundos
    }

    @Override
    public void run() {
        try {
            while (running) {
                pensar();
                long inicioEspera = System.currentTimeMillis();
                garfos[id].acquire(); // Pega o garfo à esquerda
                garfos[(id + 1) % garfos.length].acquire(); // Pega o garfo à direita
                long espera = System.currentTimeMillis() - inicioEspera;
                tempoEsperaTotal += espera;
                tempoMaxEspera = Math.max(tempoMaxEspera, espera);

                System.out.println("Filósofo " + id + " está comendo.");

                comer();

                garfos[id].release(); // Solta o garfo à esquerda
                garfos[(id + 1) % garfos.length].release(); // Solta o garfo à direita

                vezesComeu++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public int getVezesComeu() {
        return vezesComeu;
    }

    public long getTempoEsperaMedio() {
        return vezesComeu == 0 ? 0 : tempoEsperaTotal / vezesComeu;
    }

    public long getTempoMaxEspera() {
        return tempoMaxEspera;
    }

    public void parar() {
        running = false;
    }
}