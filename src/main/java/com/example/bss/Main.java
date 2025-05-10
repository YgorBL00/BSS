package com.example.bss;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.bss.ui.PainelBemVindo;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        PainelBemVindo painelBemVindo = new PainelBemVindo(stage);
        Scene scene = new Scene(painelBemVindo, 950, 650);
        stage.setScene(scene);
        stage.setTitle("App Câmara Fria");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
/* COISA A Fazer
1. Conseguir Trocar os valores pelo botao
2. Somar os Valores que nao ta somando
3. fazer o custo operacional ir no custo da camara
4. colocar um opçao de nova atualização
 */