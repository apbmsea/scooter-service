package one_module.Presentation;

import one_module.Aplication.DTO.addsBufer.BatchDataDTOadds;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
public class MainController {

    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED)
    public void eventList(@RequestBody BatchDataDTOadds listAdds) {

    }


    //  @PostMapping("/userscontactdata")
    //   @ResponseStatus(HttpStatus.CREATED)
    //public void usersContactData(@RequestBody B listAdds){}
}