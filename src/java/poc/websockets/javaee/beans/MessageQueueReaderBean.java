/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poc.websockets.javaee.beans;

import poc.websockets.javaee.types.WSocketJmsMessage;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Message;
import javax.jms.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 497248
 */
@Named
@MessageDriven(mappedName = "jms/NewMessageQueue", activationConfig = {
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MessageQueueReaderBean implements MessageListener {
    
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    @WSocketJmsMessage
    Event<Message> jmsEvent;

    @Override
    public void onMessage(Message message) {
        if(log.isInfoEnabled()){
            log.info("[Reader] New message has arrived");
        }
        jmsEvent.fire(message);
    }
}
