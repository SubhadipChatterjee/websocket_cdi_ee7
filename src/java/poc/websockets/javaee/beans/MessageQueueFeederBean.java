/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poc.websockets.javaee.beans;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author subhadip.chatterjee@tcs.com
 */
@Named
@LocalBean
@Stateless
public class MessageQueueFeederBean {
    private final Logger log = LoggerFactory.getLogger(getClass());
    
    @Inject
    @JMSConnectionFactory("jms/NewMessageQueueFactory")
    private JMSContext jmsContext;
    
    @Resource(mappedName="jms/NewMessageQueue")
    private Queue messageQueue;

    public void sendMessage(String message) {
        if(log.isInfoEnabled()){
            log.info("[Feeder] Sending message to Queue");
        }
        jmsContext.createProducer().send(messageQueue, message);
    }
}
