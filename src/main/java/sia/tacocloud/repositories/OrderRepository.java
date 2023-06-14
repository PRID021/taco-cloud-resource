package sia.tacocloud.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import sia.tacocloud.models.TacoOrder;
import sia.tacocloud.models.TacoUser;

import java.util.Date;
import java.util.List;

public interface  OrderRepository extends CrudRepository<TacoOrder, String> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);
    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);
    Object findByUserOrderByPlacedAtDesc(TacoUser user, Pageable pageable);
}
