package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseTest {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5433/BookStore";
        String user = "postgres";
        String password = "a200525d";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            if (connection != null) {
                System.out.println("✅ Подключение к базе данных успешно установлено!");
            } else {
                System.out.println("❌ Не удалось установить подключение.");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Ошибка подключения:");
            e.printStackTrace();
        }
    }
}
