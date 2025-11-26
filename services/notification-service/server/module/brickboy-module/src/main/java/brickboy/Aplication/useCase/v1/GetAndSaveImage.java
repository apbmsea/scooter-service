package brickboy.Aplication.useCase.v1;

import brickboy.Aplication.Domain.EntityDTO.ImageDTO.FileFrontDto;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.ImageDTOEntity;
import brickboy.Infrastructure.Entity.v1.ImageA;
import brickboy.Infrastructure.Mappers.v1.ImageMapper;
import brickboy.Infrastructure.Repository.File.FileManager;
import brickboy.Infrastructure.Repository.File.FileRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GetAndSaveImage {
    /*
    здесь должно пройзойти следущее

    Применяем проверку на jpeg img png

    мы получаем файл
    пилим его на
    название

    проименяем мапер
    сохраняем в базу мета данные
    само изображение пишем на диск через файловый менеджер



     */
    private FileRepo fileRepo;

    private final FileManager fileManager;

    public GetAndSaveImage(FileManager fileManager) {
        this.fileManager = fileManager;
    }


    public ImageA GetAndSaveImage(FileFrontDto fileFrontDto) {

        if (!fileFrontDto.getFile().getOriginalFilename().toLowerCase().endsWith(".jpg")
                && !fileFrontDto.getFile().getOriginalFilename().toLowerCase().endsWith(".jpeg")
                && !fileFrontDto.getFile().getOriginalFilename().toLowerCase().endsWith(".png")) {
            throw new IllegalArgumentException("Неверный тип файла");
        }
        String filename = fileFrontDto.getFile().getOriginalFilename();
        String patch = fileManager.FileSave(fileFrontDto.getFile());


        ImageDTOEntity iDTOE = new ImageDTOEntity();
        iDTOE.setFilename(filename);
        iDTOE.setPatchTOImge(patch);
        iDTOE.setCreatorId(fileFrontDto.getCreatorId());

        ImageMapper imageMapper = new ImageMapper();
        ImageA imageA = imageMapper.map(iDTOE);
        return fileRepo.saveImage(imageA);
    }
}
