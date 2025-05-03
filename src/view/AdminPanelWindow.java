package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Book;
import model.PrintedBook;
import model.EBook;
import service.DatabaseManager;

public class AdminPanelWindow {

    private final Stage stage;
    private final DatabaseManager dbManager = new DatabaseManager();

    public AdminPanelWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        stage.setTitle("Админ-панель: Добавление книги");

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);
        root.getStyleClass().add("auth-pane");

        Label titleLabel = new Label("Добавить новую книгу");
        titleLabel.getStyleClass().add("title-label");

        TextField titleField = new TextField();
        titleField.setPromptText("Название книги");
        titleField.getStyleClass().add("input-field");

        TextField authorField = new TextField();
        authorField.setPromptText("Автор");
        authorField.getStyleClass().add("input-field");

        TextField regionField = new TextField();
        regionField.setPromptText("Регион");
        regionField.getStyleClass().add("input-field");

        TextField priceField = new TextField();
        priceField.setPromptText("Цена");
        priceField.getStyleClass().add("input-field");

        TextField pagesField = new TextField();
        pagesField.setPromptText("Количество страниц (для печатной книги)");
        pagesField.getStyleClass().add("input-field");

        TextField linkField = new TextField();
        linkField.setPromptText("Ссылка на eBook");
        linkField.getStyleClass().add("input-field");

        TextField coverField = new TextField();
        coverField.setPromptText("URL обложки");
        coverField.getStyleClass().add("input-field");

        Button addPrintedBook = new Button("Добавить печатную книгу");
        addPrintedBook.getStyleClass().add("action-button");

        Button addEBook = new Button("Добавить электронную книгу");
        addEBook.getStyleClass().add("secondary-button");

        TextArea statusArea = new TextArea();
        statusArea.setEditable(false);
        statusArea.setPrefHeight(150);
        statusArea.getStyleClass().add("input-field");
        statusArea.setPromptText("Статус добавления книг будет отображаться здесь...");

        HBox buttonBox = new HBox(15, addPrintedBook, addEBook);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(
                titleLabel,
                titleField,
                authorField,
                regionField,
                priceField,
                pagesField,
                linkField,
                coverField,
                buttonBox,
                statusArea
        );

        addPrintedBook.setOnAction(e -> {
            try {
                Book book = new PrintedBook(
                        titleField.getText(),
                        authorField.getText(),
                        regionField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(pagesField.getText()),
                        coverField.getText()
                );
                dbManager.insertBook(book);
                statusArea.appendText("✅ Печатная книга добавлена: " + book.getTitle() + "\n");
            } catch (Exception ex) {
                statusArea.appendText("❌ Ошибка при добавлении печатной книги.\n");
            }
        });

        addEBook.setOnAction(e -> {
            try {
                Book book = new EBook(
                        titleField.getText(),
                        authorField.getText(),
                        regionField.getText(),
                        Double.parseDouble(priceField.getText()),
                        linkField.getText(),
                        coverField.getText()
                );
                dbManager.insertBook(book);
                statusArea.appendText("✅ Электронная книга добавлена: " + book.getTitle() + "\n");
            } catch (Exception ex) {
                statusArea.appendText("❌ Ошибка при добавлении электронной книги.\n");
            }
        });

        Scene scene = new Scene(root, 500, 750);
        scene.getStylesheets().add(getClass().getResource("/resources/css/style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
