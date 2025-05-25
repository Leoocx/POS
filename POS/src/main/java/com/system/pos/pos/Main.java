package com.system.pos.pos;

import com.system.pos.pos.controller.LoginController;
import com.system.pos.pos.view.LoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private BorderPane root;
    private TreeView<String> treeMenu;
    private Label contentArea;

    @Override
    public void start(Stage primaryStage) {
        exibirTelaLogin(primaryStage);
    }

    private void exibirTelaLogin(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/system/pos/pos/Login.fxml"));
            Parent loginRoot = loader.load();

            Scene loginScene = new Scene(loginRoot);
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login");
            primaryStage.show();

            // injeta a referência ao Stage
            LoginView controller = loader.getController();

            controller.setOnLoginSucesso(() -> iniciarTelaPrincipal(primaryStage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void iniciarTelaPrincipal(Stage primaryStage) {
        root = new BorderPane();

        HBox toolbar = createToolbar();
        VBox sideMenu = createSideMenu();
        contentArea = new Label("\uD83D\uDCBB Bem-vindo ");
        contentArea.setFont(new Font("Arial", 18));
        contentArea.setPadding(new Insets(20));

        root.setTop(toolbar);
        root.setLeft(sideMenu);
        root.setCenter(contentArea);

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sistema PDV");
    }

    private HBox createToolbar() {
        HBox toolbar = new HBox(10);
        toolbar.setPadding(new Insets(10));
        toolbar.setStyle("-fx-background-color: #0078D7;");
        toolbar.setAlignment(Pos.CENTER_LEFT);

        String[] labels = {"Cadastros", "Estoque", "Compra", "Produção", "Vendas", "Financeiro", "Fiscal"};
        for (String label : labels) {
            Button btn = new Button(label);
            btn.setStyle("-fx-background-color: white; -fx-border-radius: 4; -fx-background-radius: 4;");
            btn.setOnAction(e -> contentArea.setText("Você clicou em: " + label));
            toolbar.getChildren().add(btn);
        }
        return toolbar;
    }

    private VBox createSideMenu() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(10);
        vbox.setStyle("-fx-background-color: #f0f0f0;");

        Label searchLabel = new Label("Pesquisar Menus:");
        TextField searchField = new TextField();

        treeMenu = new TreeView<>();
        treeMenu.setRoot(createMenuTree());
        treeMenu.setShowRoot(false);

        treeMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            TreeItem<String> item = treeMenu.getSelectionModel().getSelectedItem();
            if (item != null && item.isLeaf()) {
                String viewName = item.getValue();
                loadFXML(viewName);
            }
        });


        vbox.getChildren().addAll(searchLabel, searchField, treeMenu);
        return vbox;
    }

    private TreeItem<String> createMenuTree() {
        TreeItem<String> rootItem = new TreeItem<>("Root");

        TreeItem<String> cadastros = new TreeItem<>("Cadastros");
        cadastros.getChildren().addAll(
                new TreeItem<>("Clientes"),
                new TreeItem<>("Fornecedores"),
                new TreeItem<>("Produtos")
        );

        TreeItem<String> estoque = new TreeItem<>("Estoque");
        estoque.getChildren().addAll(
                new TreeItem<>("Entrada"),
                new TreeItem<>("Saída")
        );

        TreeItem<String> vendas = new TreeItem<>("Venda");
        TreeItem<String> pdv = new TreeItem<>("PDV");
        TreeItem<String> ecf = new TreeItem<>("ECF");
        TreeItem<String> relatorios = new TreeItem<>("Relatorios");
        TreeItem<String> bi = new TreeItem<>("Business Intelligence");
        vendas.getChildren().addAll(pdv, ecf, relatorios, bi);

        TreeItem<String> financeiro = new TreeItem<>("Financeiro");
        financeiro.getChildren().addAll(
          new TreeItem<>("Contas")
        );

        TreeItem<String> fiscal = new TreeItem<>("Fiscal");
        fiscal.getChildren().addAll(
                new TreeItem<>("Notas Fiscais"),
                new TreeItem<>("Apuração de Impostos")
        );

        rootItem.getChildren().addAll(cadastros, estoque, vendas, financeiro, fiscal);
        return rootItem;
    }

    private void loadFXML(String name) {
        try {
            // Remove espaços e coloca nome padrão (ex: Clientes → Clientes.fxml)
            String fileName = name.replaceAll(" ", "") + ".fxml";
            String path = "/com/system/pos/pos/" + fileName; // caminho + nome padrão (ex: /com/system/pos/pos/Clientes.fxml )

            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent view = loader.load();
            root.setCenter(view);
        } catch (IOException e) {
            contentArea.setText("Erro ao carregar tela: " + name);
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }
}
