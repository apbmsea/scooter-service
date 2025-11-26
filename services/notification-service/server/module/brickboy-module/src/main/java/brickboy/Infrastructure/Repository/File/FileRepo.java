package brickboy.Infrastructure.Repository.File;

import brickboy.Infrastructure.Entity.v1.ImageA;
import brickboy.Infrastructure.Implimentation.v1.RepositoryImageImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;

@Service
@Transactional
public class FileRepo {
    @Autowired
    private final RepositoryImageImp repoImg;


    public FileRepo(RepositoryImageImp repoImg) {
        this.repoImg = repoImg;
    }


    public ImageA saveImage(ImageA image) {
        return repoImg.save(image);
    }
}
