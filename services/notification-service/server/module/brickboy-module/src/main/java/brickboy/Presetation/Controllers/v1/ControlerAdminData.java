package brickboy.Presetation.Controllers.v1;

import brickboy.Aplication.Domain.EntityDTO.AdminEventDTO.EventAdminDTO;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.FileFrontDto;
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


    @PostMapping
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
    public ResponseEntity<?> handleFileUpload(@RequestBody FileFrontDto dto) {
        if (dto.getFile().isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided!");
        }
        try {
            getAndSaveImage.GetAndSaveImage(dto);
        } catch (Exception e) {}

//            применяю юзкей

        return ResponseEntity.ok("File uploaded successfully!");
    }

}
