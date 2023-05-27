package sia.tacocloud.repositories;

import org.springframework.data.repository.CrudRepository;

import sia.tacocloud.models.TacoUser;

public interface UserRepository extends CrudRepository<TacoUser, Long>{
    TacoUser findByUsername(String username);
}
