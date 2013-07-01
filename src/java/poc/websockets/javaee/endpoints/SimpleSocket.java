package poc.websockets.javaee.endpoints;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 497248
 */
@ServerEndpoint("/chat")
public class SimpleSocket implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleSocket.class);
    private static final Set<Session> peers =
            Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(final Session peer) {
        try {
            peer.getBasicRemote().sendText("[Socket] Session opened");
            peers.add(peer);
            if (LOG.isInfoEnabled()) {
                LOG.info("New Client Session opened. id: " + peer.getId());
            }
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message, final Session peer) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Received Message");
        }
        try {
            for (Session each : peers) {
                each.getBasicRemote().sendText("[Socket] " + message);
            }
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
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
