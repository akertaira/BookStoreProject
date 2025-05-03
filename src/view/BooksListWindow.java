package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Book;

import java.util.List;

public class BooksListWindow {

    private Stage stage;
    private List<Book> books;
    private Runnable onBackAction;

    public BooksListWindow(List<Book> books,  Runnable onBackAction) {
        this.stage = new Stage();
        this.books = books;
        this.onBackAction = onBackAction;
    }

    public void show() {
        stage.setTitle("📚 Книги региона");

        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();

        // Добавляем книги в ListView
        for (Book book : books) {
            items.add(book.getDetails() + " — " + book.getPrice() + " ₸");
        }
        listView.setItems(items);

        // Стилизация элементов списка

        listView.setStyle("-fx-background-color: #ffffff; -fx-border-color: #d3d3d3; -fx-border-width: 1px; -fx-font-size: 14px;");

        Button backButton = util.NavigationHelper.createBackButton(stage, onBackAction);

        VBox vbox = new VBox(15, listView, backButton);
        vbox.setStyle("-fx-padding: 20px; -fx-background-color: #f4f4f4;");
        Scene scene = new Scene(vbox, 400, 300);

        // Подключаем стили
        scene.getStylesheets().add(getClass().getResource("/resources/css/bookslist.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }
}
