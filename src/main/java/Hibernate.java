import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Hibernate {

    private static final String URL = "jdbc:postgresql://localhost:5432/rashen-db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        ArrayList<Book> books = new ArrayList<>(List.of(
                new Book("Война и мир", "Лев Толстой"),
                new Book("Анна Каренинар", "Лев Толстой"),
                new Book("Детство", "Лев Толстой"),
                new Book("Капитанская дочка", "Александр Пушкин"),
                new Book("Евгений Онегин", "Александр Пушкин"),
                new Book("Сказки", "Александр Пушкин"),
                new Book("Бородино", "Михаил Лермонтов"),
                new Book("Мцыри", "Михаил Лермонтов"),
                new Book("Демон", "Михаил Лермонтов"),
                new Book("Герой нашего времени", "Михаил Лермонтов")
        ));
        createSchema();
        createSessionFactory();
        addListBooksToSQL(books);
        printBookByAuthor("Александр Пушкин");
    }

    public static void createSessionFactory() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public static void createSchema() {
        try (Connection c = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement st = c.createStatement();
            st.execute("DROP SCHEMA IF EXISTS rashen_schema2 CASCADE;");
            st.execute("CREATE SCHEMA rashen_schema2;");
            st.execute("""
                    CREATE TABLE IF NOT EXISTS rashen_schema2.books (
                        id          bigserial PRIMARY KEY,
                        name        varchar(100) NOT NULL,
                        author      varchar(100) NOT NULL);""");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addListBooksToSQL(List<Book> listBooks) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            for (Book b : listBooks) {
                session.persist(b);
            }
            session.getTransaction().commit();
        }
    }

    public static void printBookByAuthor(String author) {
        try (Session session = sessionFactory.openSession()) {
            List<Book> books = session.createQuery(String.format("from Book where author = '%s'", author), Book.class)
                    .getResultList();
            System.out.println(books);
        }
    }
}