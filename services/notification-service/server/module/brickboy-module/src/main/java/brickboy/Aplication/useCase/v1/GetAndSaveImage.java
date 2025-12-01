package brickboy.Aplication.useCase.v1;

import brickboy.Aplication.Domain.EntityDTO.ImageDTO.FileFrontDTOUpdate;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.FileFrontDtoSave;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.ImageDTOEntity;
import brickboy.Infrastructure.Entity.v1.ImageA;
import brickboy.Infrastructure.Mappers.v1.ImageMapper;
import brickboy.Infrastructure.Repository.File.FileManager;
import brickboy.Infrastructure.Repository.File.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAndSaveImage {
    @Autowired
    private FileRepo fileRepo;
    @Autowired
    private final FileManager fileManager;
    @Autowired
    private ImageMapper imageMapper;

    public GetAndSaveImage(FileManager fileManager) {
        this.fileManager = fileManager;
    }


    public String GetAndSaveImage(FileFrontDtoSave fileFrontDtoSave) {

        if (!fileFrontDtoSave.getFile().getOriginalFilename().toLowerCase().endsWith(".jpg") && !fileFrontDtoSave.getFile().getOriginalFilename().toLowerCase().endsWith(".jpeg") && !fileFrontDtoSave.getFile().getOriginalFilename().toLowerCase().endsWith(".png")) {
            throw new IllegalArgumentException("Неверный тип файла");
        }
        String filename = fileFrontDtoSave.getFile().getOriginalFilename();
        String patch = fileManager.FileSave(fileFrontDtoSave.getFile());


        ImageDTOEntity iDTOE = new ImageDTOEntity();
        iDTOE.setFilename(filename);
        iDTOE.setPatchTOImge(patch);
        iDTOE.setCreatorId(fileFrontDtoSave.getCreatorId());


        ImageA imageA = imageMapper.map(iDTOE);
        return fileRepo.saveImage(imageA);
    }

    public String UpdateAndSaveImage(FileFrontDTOUpdate fileFrontDTOUpdate) {
        if (!fileFrontDTOUpdate.getNewfile().getOriginalFilename().toLowerCase().endsWith(".jpg") && !fileFrontDTOUpdate.getNewfile().getOriginalFilename().toLowerCase().endsWith(".jpeg") && !fileFrontDTOUpdate.getNewfile().getOriginalFilename().toLowerCase().endsWith(".png")) {
            throw new IllegalArgumentException("Неверный тип файла");
        }

        ImageA imageA = fileRepo.getImage(fileFrontDTOUpdate.getIdOld());
        String oldFilename = imageA.getFilename();
        String oldPatch = imageA.getPatchToImage();


        fileManager.FileDelete(oldFilename);
        fileRepo.deleteImage(fileFrontDTOUpdate.getIdOld());


        String filename = fileFrontDTOUpdate.getNewfile().getOriginalFilename();
        String patch = fileManager.FileSave(fileFrontDTOUpdate.getNewfile());

        ImageDTOEntity iDTOE = new ImageDTOEntity();
        iDTOE.setFilename(filename);
        iDTOE.setPatchTOImge(patch);
        iDTOE.setCreatorId(fileFrontDTOUpdate.getCreatorid());

        ImageA imageAs = imageMapper.map(iDTOE);
        return fileRepo.saveImage(imageAs);

    }


}
