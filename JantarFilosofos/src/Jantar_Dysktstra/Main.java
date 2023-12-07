package Jantar_Dysktstra;

/**
 *
 * @author Alexandre Rodrigues
 */
public class Main {

    public boolean[] garfos = new boolean[5];

    public static void main(String[] args) throws InterruptedException {
        Estados estados = new Estados();
        Filosofo f0 = new Filosofo(0, estados);
        Filosofo f1 = new Filosofo(1, estados);
        Filosofo f2 = new Filosofo(2, estados);
        Filosofo f3 = new Filosofo(3, estados);
        Filosofo f4 = new Filosofo(4, estados);
        f0.setName("Filosofo1");
        f1.setName("Filosofo2");
        f2.setName("Filosofo3");
        f3.setName("Filosofo4");
        f4.setName("Filosofo5");
        f0.start();
        f1.start();
        f2.start();
        f3.start();
        f4.start();
        Thread.sleep(10000);
        f0.parar();
        f1.parar();
        f2.parar();
        f3.parar();
        f4.parar();
    }
}
