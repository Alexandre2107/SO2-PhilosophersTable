package Janta_MinhaImplementacao;


import java.util.concurrent.Semaphore;
public class JantarDosFilosofos {
    public static void main(String[] args) {
        int numFilosofos = 5;
        Semaphore[] garfos = new Semaphore[numFilosofos];

        for (int i = 0; i < numFilosofos; i++) {
            garfos[i] = new Semaphore(1);
        }

        Filosofo[] filosofos = new Filosofo[numFilosofos];

        for (int i = 0; i < numFilosofos; i++) {
            filosofos[i] = new Filosofo(i, garfos);
            new Thread(filosofos[i]).start();
        }

        // Execute por 180 segundos
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Interrompe os filósofos
        for (Filosofo filosofo : filosofos) {
            filosofo.parar();
        }

        // Exiba os resultados
        for (int i = 0; i < numFilosofos; i++) {
            Filosofo filosofo = filosofos[i];
            System.out.println("Filósofo " + i + ": Comeu " + filosofo.getVezesComeu() + " vezes. Média de espera: " +
                    filosofo.getTempoEsperaMedio() + " ms. Máximo tempo de espera: " + filosofo.getTempoMaxEspera() + " ms.");
        }
    }
}
