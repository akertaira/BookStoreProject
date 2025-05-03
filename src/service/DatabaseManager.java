package service;

import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DatabaseManager {

    private static final String URL = "jdbc:postgresql://localhost:5433/BookStore";
    private static final String USER = "postgres";
    private static final String PASSWORD = "a200525d";

    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Подключение к базе данных установлено");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Добавление книги
    public void insertBook(Book book) {
        String sql = "INSERT INTO books (title, author, region, price, type, numberOfPages, downloadLink, coverUrl) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getRegion());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setString(5, book.getType());

            if (book instanceof PrintedBook pb) {
                pstmt.setInt(6, pb.getNumberOfPages());
                pstmt.setNull(7, Types.VARCHAR);
            } else if (book instanceof EBook eb) {
                pstmt.setNull(6, Types.INTEGER);
                pstmt.setString(7, eb.getDownloadLink());
            } else {
                pstmt.setNull(6, Types.INTEGER);
                pstmt.setNull(7, Types.VARCHAR);
            }

            pstmt.setString(8, book.getCoverUrl());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> getBooksByRegion(String region) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE region = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, region);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book mapBook(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String region = rs.getString("region");
        double price = rs.getDouble("price");
        String coverUrl = rs.getString("coverUrl");

        if (type.equals("PrintedBook")) {
            int pages = rs.getInt("numberOfPages");
            return new PrintedBook(title, author, region, price, pages, coverUrl);
        } else if (type.equals("EBook")) {
            String link = rs.getString("downloadLink");
            return new EBook(title, author, region, price, link, coverUrl);
        } else {
            return null;
        }
    }

    public int getUserId(String name) {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT id FROM users WHERE name = ?")) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void addPurchase(String username, String bookTitle) {
        int userId = getUserId(username);
        int bookId = getBookIdByTitle(bookTitle);
        if (userId != -1 && bookId != -1) {
            try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO purchases (user_id, book_id) VALUES (?, ?)")) {
                pstmt.setInt(1, userId);
                pstmt.setInt(2, bookId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private int getBookIdByTitle(String title) {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT id FROM books WHERE title = ?")) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public User getUserByCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("role")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean createUser(String username, String password) {
        // Проверка, существует ли пользователь
        try (PreparedStatement checkStmt = connection.prepareStatement(
                "SELECT * FROM users WHERE name = ?")) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) return false; // пользователь уже есть
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Вставка нового пользователя
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO users (name, email, role, password) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, username + "@mail.com"); // фиктивный email
            stmt.setString(3, "user");
            stmt.setString(4, password); // ← СЮДА передаётся пароль
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    public List<Book> getPurchasedBooksByUsername(String username) {
        List<Book> books = new ArrayList<>();
        String sql = """
        SELECT b.* FROM books b
        JOIN purchases p ON b.id = p.book_id
        JOIN users u ON p.user_id = u.id
        WHERE u.name = ?
        """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                books.add(mapBook(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public boolean purchaseBook(String username, Book book) {
        int userId = getUserId(username);
        int bookId = getBookIdByTitle(book.getTitle());
        if (userId == -1 || bookId == -1) return false;

        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO purchases (user_id, book_id) VALUES (?, ?)")) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBookByTitle(String title) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM books WHERE title = ?")) {
            stmt.setString(1, title);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Подключение к БД закрыто");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
