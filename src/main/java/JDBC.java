import java.sql.*;

public class JDBC {

    private static final String URL = "jdbc:postgresql://localhost:5432/rashen-db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static void main(String[] args) {
        connection();
    }

    public static void connection() {
        try (Connection c = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement st = c.createStatement();
            st.execute("DROP SCHEMA IF EXISTS rashen_schema CASCADE;");
            st.execute("CREATE SCHEMA rashen_schema;");
            st.execute("""
                    CREATE TABLE IF NOT EXISTS rashen_schema.books (
                        id          bigserial PRIMARY KEY,
                        name        varchar(100) NOT NULL,
                        author      varchar(100) NOT NULL);""");
            st.execute("""
                    INSERT INTO rashen_schema.books (name, author) VALUES
                        ('Война и мир', 'Лев Толстой'),
                        ('Анна Каренина', 'Лев Толстой'),
                        ('Детство', 'Лев Толстой'),
                        ('Капитанская дочка', 'Александр Пушкин'),
                        ('Евгений Онегин', 'Александр Пушкин'),
                        ('Сказки', 'Александр Пушкин'),
                        ('Бородино', 'Михаил Лермонтов'),
                        ('Мцыри', 'Михаил Лермонтов'),
                        ('Демон', 'Михаил Лермонтов'),
                        ('Герой нашего времени', 'Михаил Лермонтов');""");
            String query = "SELECT * FROM rashen_schema.books WHERE author = 'Лев Толстой'";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String nameBook = rs.getString("name");
                String author = rs.getString("author");
                System.out.println("Книгу \"" + nameBook + "\" написал " + author);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
