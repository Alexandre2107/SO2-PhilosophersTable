package Jantar_Dysktstra;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JantarDosFilosofosInterface extends Application {
    private Estados estados;
    private Filosofo[] filosofos;
    private Pane pane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        estados = new Estados();
        filosofos = new Filosofo[5];
        pane = new Pane();

        for (int i = 0; i < 5; i++) {
            double angle = i * 360 / 5.0;
            double x = 350 + 250 * Math.cos(Math.toRadians(angle));
            double y = 350 + 250 * Math.sin(Math.toRadians(angle));

            Circle filosofoCircle = new Circle(x, y, 20, Color.RED);
            Text filosofoText = new Text(x + 20, y, "Filosofo " + (i + 1));
            Line garfoLine = new Line(x, y, x + 20, y + 20);
            pane.getChildren().addAll(filosofoCircle, filosofoText, garfoLine);

            filosofos[i] = new Filosofo(i, estados);
            filosofos[i].setName("Filosofo " + (i + 1));
            filosofos[i].start();
        }

        Scene scene = new Scene(pane, 300, 300);
        primaryStage.setTitle("Jantar dos Filósofos - Dykstra");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> encerrarPrograma());
        primaryStage.show();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(10000);
                   for (int i = 0; i < 5; i++) {
                    filosofos[i].parar();;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(this::atualizarInterface);
            }
        }).start();
    }

    private void atualizarInterface() {
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            Platform.runLater(() -> {
                Text filosofoText = (Text) pane.getChildren().get(finalI * 3 + 1);

                filosofoText.setText(filosofos[finalI].getName() + "\n" +
                        "Tempo sem comer: " + estados.getTempoSemComer(finalI) + " ms\n" +
                        "Média de espera: "
                        + (filosofos[finalI].getN() == 0 ? "N/A"
                                : (estados.getMediaDeEspera(finalI) / filosofos[finalI].getN()))
                        + " ms\n" +
                        "Tempo máximo sem comer: " + estados.getTempoMaximoSemComer(finalI) + " ms\n" +
                        "Vezes que comeu: " + estados.getContador(finalI));
            });
        }
        Platform.runLater(this::atualizarInterface);
    }

    private void encerrarPrograma() {
        Platform.exit();
        System.exit(0);
    }
}
