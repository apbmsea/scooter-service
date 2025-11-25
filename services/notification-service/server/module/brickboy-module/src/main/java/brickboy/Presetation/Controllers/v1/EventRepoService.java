package brickboy.Presetation.Controllers.v1;


import brickboy.Aplication.Domain.EntityDTO.patchDTO;
import brickboy.Infrastructure.Entity.v1.EventListA;
import brickboy.Infrastructure.Implimentation.v1.RepositpryEventListImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EventRepoService {
    @Autowired
    public final RepositpryEventListImp repoELImp;


    public EventRepoService(RepositpryEventListImp repoELImp) {
        this.repoELImp = repoELImp;
    }

public EventListA save(EventListA eventListA) {
        return repoELImp.save(eventListA);
}
}

