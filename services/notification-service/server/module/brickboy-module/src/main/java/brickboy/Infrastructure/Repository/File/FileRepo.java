package brickboy.Infrastructure.Repository.File;

import brickboy.Infrastructure.Entity.v1.ImageA;
import brickboy.Infrastructure.Implimentation.v1.RepositoryImageImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.util.UUID;

@Service
@Transactional
public class FileRepo {
    @Autowired
    private final RepositoryImageImp repoImg;


    public FileRepo(RepositoryImageImp repoImg) {
        this.repoImg = repoImg;
    }

    // Metod
    public String saveImage(ImageA image) {
        ImageA imageA = repoImg.save(image);
        return imageA.getPatchToImage();
    }

    public ImageA getImage(UUID id) {
        return repoImg.getReferenceById(id);
    }

    public void deleteImage(UUID id) {
        repoImg.deleteById(id);
    }
}
