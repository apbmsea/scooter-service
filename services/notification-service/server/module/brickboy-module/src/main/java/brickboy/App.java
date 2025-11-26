package brickboy;


import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Dotenv dotenv = Dotenv.load();
        SpringApplication.run(App.class, args);
        System.out.println("Buy World!");
    }
}