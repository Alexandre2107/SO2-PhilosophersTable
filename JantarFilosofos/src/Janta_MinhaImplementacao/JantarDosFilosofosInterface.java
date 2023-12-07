package Janta_MinhaImplementacao;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.concurrent.Semaphore;

public class JantarDosFilosofosInterface extends Application {

  private static final int NUM_FILOSOFOS = 5;

  private Semaphore[] garfos;
  private Filosofo[] filosofos;
  private Pane root;
  private Canvas canvas;
  private GraphicsContext gc;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Jantar dos Filósofos - Minha Implementação");

    root = new Pane();
    canvas = new Canvas(600, 400);
    gc = canvas.getGraphicsContext2D();
    root.getChildren().add(canvas);

    Scene scene = new Scene(root, 600, 400);
    primaryStage.setScene(scene);
    primaryStage.show();

    initialize();

    new Thread(this::runSimulation).start();
  }

  private void initialize() {
    garfos = new Semaphore[NUM_FILOSOFOS];
    filosofos = new Filosofo[NUM_FILOSOFOS];

    for (int i = 0; i < NUM_FILOSOFOS; i++) {
      garfos[i] = new Semaphore(1);
    }

    for (int i = 0; i < NUM_FILOSOFOS; i++) {
      filosofos[i] = new Filosofo(i, garfos);
    }
  }

  private void runSimulation() {
    for (Filosofo filosofo : filosofos) {
      new Thread(filosofo).start();
    }

    long startTime = System.currentTimeMillis();

    while (System.currentTimeMillis() - startTime < 10000) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      updateGUI();
    }

    for (Filosofo filosofo : filosofos) {
      filosofo.parar();
    }

    // Exiba os resultados
    for (int i = 0; i < NUM_FILOSOFOS; i++) {
      Filosofo filosofo = filosofos[i];
      System.out.println("Filósofo " + i + ": Comeu " + filosofo.getVezesComeu() + " vezes. Média de espera: " +
          filosofo.getTempoEsperaMedio() + " ms. Máximo tempo de espera: " + filosofo.getTempoMaxEspera() + " ms.");
    }
  }

  private void updateGUI() {
    Platform.runLater(() -> {
      gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

      // Desenha a mesa
      gc.strokeOval(50, 50, 500, 300);

      // Desenha os filósofos
      for (int i = 0; i < NUM_FILOSOFOS; i++) {
        double angle = Math.toRadians((360.0 / NUM_FILOSOFOS) * i);
        double x = 300 + 200 * Math.cos(angle);
        double y = 200 + 150 * Math.sin(angle);

        gc.setFill(Color.BLUE);
        gc.fillOval(x - 20, y - 20, 40, 40);

        gc.setStroke(Color.BLACK);
        gc.strokeText("Filósofo " + i, x - 20, y - 30);
        gc.strokeText("Comeu: " + filosofos[i].getVezesComeu(), x + 20, y + 10);
        gc.strokeText("Média: " + filosofos[i].getTempoEsperaMedio() + " ms", x + 20, y + 30);
        gc.strokeText("Máx: " + filosofos[i].getTempoMaxEspera() + " ms", x + 20, y + 50);
      }
    });
  }
}
