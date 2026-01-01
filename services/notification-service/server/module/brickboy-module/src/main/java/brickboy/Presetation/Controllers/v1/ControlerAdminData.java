package brickboy.Presetation.Controllers.v1;

import brickboy.Aplication.Domain.EntityDTO.AdminEventDTO.AdminEventDtoSave;
import brickboy.Aplication.Domain.EntityDTO.EventDTO.FrontStartEvent;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.FileFrontDTOUpdate;
import brickboy.Aplication.Domain.EntityDTO.ImageDTO.FileFrontDtoSave;
import brickboy.Aplication.useCase.v1.UseCaseAdminEvent;
import brickboy.Aplication.useCase.v1.UseCaseImage;
import brickboy.Aplication.useCase.v1.UseCaseProdusser;
import brickboy.Infrastructure.Entity.v1.EventDataAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1")
public class ControlerAdminData {

    @Autowired
    private UseCaseImage useCaseImage;
    @Autowired
    private UseCaseAdminEvent useCaseAdminEvent;
    @Autowired
    private UseCaseProdusser useCaseProdusser;

    @PostMapping("/event/save")
    public ResponseEntity<AdminEventDtoSave> addEvent(@RequestBody AdminEventDtoSave dto) {
        useCaseAdminEvent.newAdminEvent(dto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/event/get")
    public Page<EventDataAdmin> findAllEvents(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return useCaseAdminEvent.findAllEvents(page, size);
    }

    @PostMapping
    public /* ResponseEntity<EventDataAdmin> */  ResponseEntity<String> updateEvent(@RequestBody AdminEventDtoSave dto) {
        return ResponseEntity.ok("OR");
    }
//
//    @PostMapping
//    public ResponseEntity<EventAdminDTO> deleteEvent(@RequestBody EventAdminDTO dto) {
//        return new ResponseEntity<>(dto, HttpStatus.CREATED);
//    }


    @PostMapping(value = "/files/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute FileFrontDtoSave dto) {
        if (dto.getFile().isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided!");
        }
        try {
            useCaseImage.GetAndSaveImage(dto); // Логика делегирована сервису
            return ResponseEntity.ok("ok");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing the uploaded file: " + e.getMessage());
        }
    }


    @PostMapping("/filles/update")
    public ResponseEntity<?> handleFileUpdate(@RequestBody FileFrontDTOUpdate dto) {
        if (dto.getNewfile().isEmpty()) {
            return ResponseEntity.badRequest().body("No file provided!");
        }
        try {
            // return String patchToImg
            return ResponseEntity.ok(useCaseImage.UpdateAndSaveImage(dto));
        } catch (Exception e) {
            return ResponseEntity.ok("File invalid! or ERROR:" + e.getMessage());
        }
    }

    @PostMapping("/start")
    public ResponseEntity<String> startEvent(@RequestBody FrontStartEvent dto) {
        useCaseProdusser.produserPublic(dto);
        return ResponseEntity.ok("OR");
    }


//  @PostMapping("/template/save")
//   public ResponseEntity<String>saveTemplate(@RequestBody ){
//      return ResponseEntity.ok("OK");
//  }

//    @PostMapping("/teplate/get")


}
