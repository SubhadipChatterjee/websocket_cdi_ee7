package poc.websockets.javaee.endpoints;

import poc.websockets.javaee.beans.MessageQueueFeederBean;
import poc.websockets.javaee.types.TimeSnap;
import poc.websockets.javaee.types.WSocketJmsMessage;
import poc.websockets.javaee.types.WSocketTimerEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author subhadip.chatterjee@tcs.com
 */
@ServerEndpoint("/share")
public class TweetItSocket implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(TweetItSocket.class);
    private MessageQueueFeederBean senderBean;
    private static final Set<Session> peers =
            Collections.synchronizedSet(new HashSet<Session>());

    @Inject
    public TweetItSocket(MessageQueueFeederBean sb) {
        this.senderBean = sb;
    }
    
    @OnOpen
    public void onOpen(final Session peer) {
        try {
            peer.getBasicRemote().sendText("[Socket] Session opened");
            peers.add(peer);
            if (LOG.isInfoEnabled()) {
                LOG.info("New Client Session opened. ID: " + peer.getId());
            }
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
    }

    @OnMessage
    // Once Handshaking is completed, socket is open for data exchange
    public void onMessage(String message, final Session peer) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Received Message");
        }
        try {
            peer.getBasicRemote().sendText("[Socket] Cliet has sent: " + message);
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
        if (senderBean != null) {
            senderBean.sendMessage(message);
        }
    }

    // Broadcasting the Time snaps
    public void onTimeEvent(@Observes @WSocketTimerEvent TimeSnap time) {
        if (LOG.isInfoEnabled()) {
            LOG.info("****BROADCASTING begins****");
        }
        try {
            for (Session each : peers) {
                each.getBasicRemote().sendText("[Time Event] " + time.getTimestamp());
            }
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("****BROADCASTING ends****");
        }
    }

    // Broadcasting the JMS messages
    public void onJMSMessages(@Observes @WSocketJmsMessage Message msg) {
        if (LOG.isInfoEnabled()) {
            LOG.info("****BROADCASTING begins****");
        }
        try {
            for (Session each : peers) {
                each.getBasicRemote().sendText("[Queue] " + msg.getBody(String.class));
            }
        } catch (IOException | JMSException e) {
            LOG.error(e.getMessage());
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("****BROADCASTING ends****");
        }
    }

    @OnClose
    public void onClose(final Session peer) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Client Session closed. id: " + peer.getId());
        }
        try {
            peers.remove(peer);
            peer.close();
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
