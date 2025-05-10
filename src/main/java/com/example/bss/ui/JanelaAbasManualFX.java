package com.example.bss.ui;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class JanelaAbasManualFX extends TabPane {

    public JanelaAbasManualFX(Stage stage) {

        PainelMaterialFX painelMateriais = new PainelMaterialFX();
        PainelCalculoPaineisFX painel1 = new PainelCalculoPaineisFX(painelMateriais);
        PainelCargaTermicaFX painel2 = new PainelCargaTermicaFX();
        painel2.setPainelMaterialFX(painelMateriais);

        Tab aba1 = new Tab("Cálculo de Painéis", painel1);
        Tab aba2 = new Tab("Carga Térmica", painel2);
        Tab aba3 = new Tab("Matérias", painelMateriais);
        //Tab aba4 = new Tab("Ordem Serviço", new javafx.scene.control.Label("Conteúdo da Aba 4"));

        aba1.setClosable(false);
        aba2.setClosable(false);
        aba3.setClosable(false);
        //aba4.setClosable(false);

        this.getTabs().addAll(aba1, aba2, aba3);

        this.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            if (newTab == aba2 && aba2.getContent() instanceof PainelCargaTermicaFX) {
                ((PainelCargaTermicaFX) aba2.getContent()).atualizarDados();
            }
        });
    }
}