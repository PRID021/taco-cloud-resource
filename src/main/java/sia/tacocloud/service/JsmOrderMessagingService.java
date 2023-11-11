package sia.tacocloud.service;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import sia.tacocloud.models.TacoOrder;

@Service
public class JsmOrderMessagingService implements OrderMessagingService {
    private JmsTemplate template;
    private Destination orderQueue;

    public JsmOrderMessagingService(JmsTemplate jsm, Destination destination) {
        this.template = jsm;
        this.orderQueue = destination;
    }

    @Override
    public void sendOrder(TacoOrder order) {
        // template.send(new MessageCreator() {
        // @Override
        // public Message createMessage(Session session) throws JMSException {
        // return session.createObjectMessage(order);
        // }
        // });
        // template.send(session -> session.createObjectMessage(order));
        // template.send("tacocloud.order.queue",
        // session->session.createObjectMessage(order));
        // template.send(orderQueue, session->session.createObjectMessage(order));
        // template.convertAndSend(orderQueue, order, message -> {
        // message.setStringProperty("X_ORDER_SOURCE", "WEB");
        // return message;
        // });
        template.convertAndSend(orderQueue, order, this::addOrderSource);

    }

    private Message addOrderSource(Message message) throws JMSException {
        message.setStringProperty("X_ORDER_SOURCE", "WEB");
        return message;
    }

}
