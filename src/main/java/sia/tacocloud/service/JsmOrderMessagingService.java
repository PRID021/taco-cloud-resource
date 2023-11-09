package sia.tacocloud.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import sia.tacocloud.models.TacoOrder;

@Service
public class JsmOrderMessagingService implements OrderMessagingService {
    private JmsTemplate template;

    public JsmOrderMessagingService(JmsTemplate jsm){
        this.template = jsm;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        template.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(order);
            }
        });
    }
    
}
