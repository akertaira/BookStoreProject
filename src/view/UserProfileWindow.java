package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.User;

public class UserProfileWindow {

    private Stage stage;
    private User user;
    private Runnable onBackAction;

    public UserProfileWindow(Stage stage, User user, Runnable onBackAction) {
        this.stage = stage;
        this.user = user;
        this.onBackAction = onBackAction;
    }

    public void show() {
        stage.setTitle("Профиль пользователя");

        // Заголовок профиля
        Label titleLabel = new Label("Профиль пользователя");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-padding: 10px 0;");

        // Метки с информацией
        Label nameLabel = new Label("Имя: " + user.getName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: #555555;");

        Label emailLabel = new Label("Email: " + user.getEmail());
        emailLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: normal; -fx-text-fill: #555555;");
        Button backButton = util.NavigationHelper.createBackButton(stage, onBackAction);

        // Основной контейнер с отступами
        VBox layout = new VBox(15, titleLabel, nameLabel, emailLabel, backButton);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f5f5f5; -fx-border-radius: 10px; -fx-background-radius: 10px;");


        // Создание сцены
        Scene scene = new Scene(layout, 350, 200, Color.WHITE);
        stage.setScene(scene);
        stage.show();
    }
}
