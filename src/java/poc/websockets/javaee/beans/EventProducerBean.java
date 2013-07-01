/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poc.websockets.javaee.beans;

import poc.websockets.javaee.types.TimeSnap;
import poc.websockets.javaee.types.WSocketTimerEvent;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Timer;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author 497248
 */
@Stateless
public class EventProducerBean {

    public static final Logger LOG = LoggerFactory.getLogger(EventProducerBean.class);
    
    @Inject
    @WSocketTimerEvent
    Event<TimeSnap> timeEvent;

    @Schedule(second="*/30", minute="*", hour="20-21", info="Publishing Time snaps")
    public void produceFrequentTimer(Timer t) {
        if(LOG.isInfoEnabled()){
            LOG.info("Call Timer #");
        }
        TimeSnap moment = new TimeSnap();
        timeEvent.fire(moment);
    }
}
