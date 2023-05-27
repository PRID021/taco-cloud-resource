package sia.tacocloud.repositories;

import org.springframework.data.repository.CrudRepository;

import sia.tacocloud.models.TacoOrder;

import java.util.Date;
import java.util.List;


public interface  OrderRepository extends CrudRepository<TacoOrder, String> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);
    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);
}
