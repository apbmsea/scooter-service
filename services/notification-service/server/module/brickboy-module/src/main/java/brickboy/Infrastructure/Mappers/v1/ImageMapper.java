package brickboy.Infrastructure.Mappers.v1;

import brickboy.Aplication.Domain.EntityDTO.ImageDTO.ImageDTOEntity;
import brickboy.Infrastructure.Entity.v1.ImageA;

import java.awt.*;
import java.util.UUID;

public class ImageMapper {

    public ImageA map(ImageDTOEntity imageDTO) {
        ImageA imageA = new ImageA();
        imageA.setId(UUID.randomUUID());
        imageA.setFilename(imageDTO.getFilename());
        imageA.setPatchToImage(imageDTO.getPatchTOImge());
        imageA.setCreator(imageDTO.getCreatorId());
        return imageA;
    }
}
