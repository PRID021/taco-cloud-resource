package sia.tacocloud.data;

import org.springframework.data.repository.CrudRepository;

import sia.tacocloud.models.TacoOrder;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public interface  OrderRepository extends CrudRepository<TacoOrder, UUID> {
    List<TacoOrder> findByDeliveryZip(String deliveryZip);
    /*
     * [verb] + [subject] + [By] + [predicate]
     * [read/find/get] <--> [verb] : used to dedicated query action
     * [subject] : an optional subject to specify the query target, spring data almost ignore it.
     * [By] : signify begin of properties match
     * [predicate] : can be multiple option e.x 'Between','Equals'..., spring use to implies operation
     * 
     */
    List<TacoOrder> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);
}
