package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;
import network.ClientNetwork;

import java.util.List;

public class PurchasedBooksWindow {

    private Stage stage;
    private String username;
    private ClientNetwork clientNetwork;
    private Runnable onBackAction;

    public PurchasedBooksWindow(Stage stage, String username, ClientNetwork clientNetwork, Runnable onBackAction) {
        this.stage = stage;
        this.username = username;
        this.clientNetwork = clientNetwork;
        this.onBackAction = onBackAction;
    }

    public void show() {
        stage.setTitle("📚 Мои покупки");

        ListView<String> listView = new ListView<>();
        List<Book> books = clientNetwork.getPurchasedBooks(username);
        for (Book book : books) {
            listView.getItems().add(book.getDetails());
        }

        // Стиль окна
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #f4f4f4; -fx-font-family: 'Arial';");

        // Заголовок
        Label title = new Label("Список ваших покупок:");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        // Список покупок
        listView.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-font-size: 14px;");

        Button backButton = util.NavigationHelper.createBackButton(stage, onBackAction);

        // Добавление элементов на панель
        vbox.getChildren().addAll(title, listView, backButton);

        // Настройка сцены
        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
