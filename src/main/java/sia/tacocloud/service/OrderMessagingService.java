
 package sia.tacocloud.service;

import sia.tacocloud.models.TacoOrder;

public interface OrderMessagingService {
    public void sendOrder(TacoOrder order);
    
}