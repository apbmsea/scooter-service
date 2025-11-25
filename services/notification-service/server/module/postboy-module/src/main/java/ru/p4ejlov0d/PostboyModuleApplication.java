package ru.p4ejlov0d;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>Класс для запуска spring-приложения</p>
 * <p>Содержит один метод {@link PostboyModuleApplication#main(String[])} который запускает приложение</p>
 *
 * @author p4eJlov0d
 */
@SpringBootApplication
public class PostboyModuleApplication {
    /**
     * <p>Метод запускает spring-приложение и spring-контекст</p>
     *
     * @param args данные, передаваемые при запуске программы
     */
    public static void main(String[] args) {
        SpringApplication.run(PostboyModuleApplication.class, args);
    }

}
