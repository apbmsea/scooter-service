package brickboy.Infrastructure.Repository.v1;


import brickboy.Infrastructure.Entity.v1.EventDataAdmin;
import brickboy.Infrastructure.Implimentation.v1.RepositpryEventListImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class EventRepoService {
    @Autowired
    public RepositpryEventListImp repoELImp;

    //    Metod
    public EventDataAdmin save(EventDataAdmin eventDataAdmin) {
        return repoELImp.save(eventDataAdmin);
    }


    public Page<EventDataAdmin> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repoELImp.findAll(pageable);
    }

    public void delete(UUID id) {
        try {
            repoELImp.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventDataAdmin findById(UUID id) {
        return repoELImp.findById(id).get();
    }
    public EventDataAdmin update(EventDataAdmin eventDataAdmin, UUID id) {
        findById(id);
        delete(id);
        return save(eventDataAdmin);
    }
}

