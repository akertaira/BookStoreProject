package util;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class NavigationHelper {
    public static Button createBackButton(Stage stage, Runnable onBackAction) {
        Button backButton = new Button("⬅ Назад");
        backButton.setPrefWidth(150);
        backButton.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14px; " +
                        "-fx-cursor: hand;"
        );

        backButton.setOnAction(e -> {
            if (onBackAction != null) {
                onBackAction.run();
            }
        });

        return backButton;
    }
}
