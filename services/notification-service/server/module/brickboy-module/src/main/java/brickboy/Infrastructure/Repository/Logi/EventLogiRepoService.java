package brickboy.Infrastructure.Repository.Logi;

import brickboy.Infrastructure.Entity.v1.Logi.EventLogi;
import brickboy.Infrastructure.Implimentation.v1.RepositoryEventLogiImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class EventLogiRepoService {

    @Autowired
    private RepositoryEventLogiImp repositoryEventLogiImp;

    public EventLogi save(EventLogi eventLogi) {
        return repositoryEventLogiImp.save(eventLogi);
    }
}
