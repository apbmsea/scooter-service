package brickboy.Infrastructure.Repository.File;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileManager {


    Dotenv dotenv = Dotenv.load();
    private String mainFolder = dotenv.get("MAIN_FOLDER");

    private String SavItor(MultipartFile photo) {
        String filename = photo.getOriginalFilename();
        String fullPath = mainFolder + "/" + filename;
        File targetFile = new File(fullPath);
        File directory = new File(mainFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            // Копируем файл из MultipartFile в нужную директорию
            photo.transferTo(targetFile);
        } catch (IOException e) {
            // Сообщаем об ошибке
            throw new RuntimeException("Ошибка при записи файла: " + e.getMessage(), e);
        }

        // Возвращаем полное имя файла
        return fullPath;
    }

    public String FileSave(MultipartFile photo) {
        return SavItor(photo);
    }

    //   public FileUdate()

    //   public FileDelite()

    //   public FileGet()

}
