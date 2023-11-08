
 package sia.tacocloud.service;

import sia.tacocloud.models.TacoOrder;

public interface OtherMessagingService {
    public void sendOrder(TacoOrder order);
    
}