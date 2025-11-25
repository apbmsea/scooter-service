package brickboy.Presetation.Controllers.v1;

import brickboy.Aplication.Domain.EntityDTO.EventAdminDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class ControlerAdminData {

    @GetMapping ("/test")
    public ResponseEntity<String> test(@RequestBody EventAdminDTO eventAdminDTO) {

        return ResponseEntity.ok("OK" + eventAdminDTO);
    }

}
