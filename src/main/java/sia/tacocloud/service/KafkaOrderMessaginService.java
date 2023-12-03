package sia.tacocloud.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import sia.tacocloud.models.TacoOrder;

@Service
public class KafkaOrderMessaginService implements OrderMessagingService{
    private KafkaTemplate<String, TacoOrder> kafkaTemplate;

    
    public KafkaOrderMessaginService(KafkaTemplate<String,TacoOrder> kafkaTemplate ) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @Override
    public void sendOrder(TacoOrder order) {
      kafkaTemplate.sendDefault(order);
    }
    
}
