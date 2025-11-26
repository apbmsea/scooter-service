package brickboy.Infrastructure.Mappers.v1;

import brickboy.Aplication.Domain.EntityDTO.ImageDTO.patchDTO;
import brickboy.Infrastructure.Entity.v1.ImageA;

public class MapperDtoPath {
    public patchDTO map(ImageA image) {
        patchDTO patchDTO = new patchDTO();
        patchDTO.setId(image.getId());
        patchDTO.setPatch(image.getPatchToImage());
        return patchDTO;
    }
}
