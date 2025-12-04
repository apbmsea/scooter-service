package brickboy.Infrastructure.Repository.Logi;

import brickboy.Infrastructure.Entity.v1.EventDataAdmin;
import brickboy.Infrastructure.Entity.v1.Logi.EventDataLogi;
import brickboy.Infrastructure.Implimentation.v1.RepositoryEventDataLogiIml;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EventDataLogiRepoService {
    @Autowired
    private RepositoryEventDataLogiIml repositoryEventDataLogiIml;

    public EventDataAdmin SaveLogEvent(EventDataLogi event) {
        return repositoryEventDataLogiIml.save(event);
    }


}
