package brickboy.Presetation.Controllers.v1;

import brickboy.Aplication.Domain.EntityDTO.AdminEventDTO.EventAdminDTO;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.FileFrontDTOUpdate;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.FileFrontDtoSave;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.OutDTOImage;
import brickboy.Aplication.useCase.v1.GetAndSaveImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class ControlerAdminData {

    @Autowired
    private GetAndSaveImage getAndSaveImage;


    @PostMapping("/event/save")
    public ResponseEntity<EventAdminDTO> addEvent(@RequestBody EventAdminDTO dto) {
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    @PostMapping
    public ResponseEntity<EventAdminDTO> updateEvent(@RequestBody EventAdminDTO dto) {
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PostMapping
    public ResponseEntity<EventAdminDTO> deleteEvent(@RequestBody EventAdminDTO dto) {
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    @PostMapping("/files/upload")
    public ResponseEntity<?> handleFileUpload(@RequestBody FileFrontDtoSave dto) {
        if (dto.getFile().isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided!");
        }
        try {

            return ResponseEntity.ok(getAndSaveImage.GetAndSaveImage(dto));
        } catch (Exception e) {
            return ResponseEntity.ok("File invalid! or ERROR:" + e.getMessage());
        }

    }
@PostMapping("/filles/update")
    public ResponseEntity<?> handleFileUpdate(@RequestBody FileFrontDTOUpdate dto) {
    if (dto.getNewfile().isEmpty()) {
        return ResponseEntity.badRequest().body("No file provided!");
    }
    try {
        // return String patchToImg
        return ResponseEntity.ok(getAndSaveImage.UpdateAndSaveImage(dto));
    } catch (Exception e) {
        return ResponseEntity.ok("File invalid! or ERROR:" + e.getMessage());
    }
}


/*
        Добавить старит эвент
        стоп эвент
 */

}
