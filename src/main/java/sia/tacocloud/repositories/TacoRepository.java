package sia.tacocloud.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


import sia.tacocloud.models.Taco;

public interface TacoRepository extends CrudRepository<Taco, Long> {

    Page<Taco> findAll(Pageable pageRequest);

}
