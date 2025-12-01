package brickboy.Infrastructure.Repository.File;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Component
public class FileManager {

    private final Dotenv dotenv = Dotenv.load();

    private final String mainFolder = dotenv.get("MAIN_FOLDER");

    /**
     * Сохраняет файл на сервер
     *
     * @param photo Файл для загрузки
     * @return Полный путь к файлу
     */
    private String saveFile(MultipartFile photo) throws IOException {
        String filename = photo.getOriginalFilename();
        String fullPath = mainFolder + "/" + filename;
        File targetFile = new File(fullPath);
        File directory = new File(mainFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        photo.transferTo(targetFile);
        return fullPath;
    }

    /**
     * Обертка публичного метода для сохранения файла
     *
     * @param photo Загружаемый файл
     * @return Полный путь к файлу
     */
    public String FileSave(MultipartFile photo) {
        try {
            return saveFile(photo);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла: " + e.getMessage(), e);
        }
    }

    /**
     * Удаляет файл с сервера
     *
     * @param fileName Имя удаляемого файла
     */
    public void FileDelete(String fileName) {
        String fullPath = mainFolder + "/" + fileName;
        File file = new File(fullPath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                throw new RuntimeException("Не удалось удалить файл: " + fileName);
            }
        } else {
            throw new RuntimeException("Файл не найден: " + fileName);
        }
    }

    /**
     * Получает файл по имени
     *
     * @param fileName Имя запрашиваемого файла
     * @return Объект типа File
     */
    public File FileGet(String fileName) {
        String fullPath = mainFolder + "/" + fileName;
        File file = new File(fullPath);
        if (!file.exists()) {
            throw new RuntimeException("Файл не найден: " + fileName);
        }
        return file;
    }
}