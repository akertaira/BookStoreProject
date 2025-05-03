package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import network.ClientNetwork;

public class UserMainWindow {
    private Stage stage;
    private String username;
    private ClientNetwork clientNetwork;

    public UserMainWindow(Stage stage, String username, ClientNetwork clientNetwork) {
        this.stage = stage;
        this.username = username;
        this.clientNetwork = clientNetwork;
    }

    public void show() {
        // Кнопки главного меню
        Button browseRegionsButton = new Button("📚 Просмотреть книги по регионам");
        Button myPurchasesButton = new Button("🛒 Мои покупки");
        Button logoutButton = new Button("🔑 Выйти");

        // Стиль кнопок
        browseRegionsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15px; -fx-font-weight: bold;");
        myPurchasesButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15px; -fx-font-weight: bold;");
        logoutButton.setStyle("-fx-background-color: #FF5722; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 15px; -fx-font-weight: bold;");

        // Установка ширины кнопок
        browseRegionsButton.setPrefWidth(300);
        myPurchasesButton.setPrefWidth(300);
        logoutButton.setPrefWidth(300);

        // Обработчики событий для кнопок
        browseRegionsButton.setOnAction(e -> {
            RegionSelectionWindow regionSelectionWindow = new RegionSelectionWindow(stage, username, clientNetwork);
            regionSelectionWindow.show();
        });

        myPurchasesButton.setOnAction(e -> {
            PurchasedBooksWindow purchasesWindow = new PurchasedBooksWindow(stage, username, clientNetwork);
            purchasesWindow.show();
        });

        logoutButton.setOnAction(e -> {
            LoginWindow loginWindow = new LoginWindow(stage, clientNetwork);
            loginWindow.show();
        });

        // Контейнер для размещения элементов
        VBox root = new VBox(20, browseRegionsButton, myPurchasesButton, logoutButton);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 40px;");

        // Добавление заголовка
        Label title = new Label("Главное меню");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-padding: 20px;");

        // Добавление заголовка в верхнюю часть
        root.getChildren().add(0, title);

        // Создание сцены
        Scene scene = new Scene(root, 400, 300, Color.WHITE);
        stage.setTitle("Главное меню пользователя");
        stage.setScene(scene);
        stage.show();
    }
}
