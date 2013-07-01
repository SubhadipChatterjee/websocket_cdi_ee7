/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package poc.websockets.javaee.types;

import java.util.Date;

/**
 *
 * @author subhadip.chatterjee@tcs.com
 */
public class TimeSnap {
    private Date timestamp;
    
    public TimeSnap(){
        timestamp = new Date();
    }
    
    public Date getTimestamp(){
        return this.timestamp;
    }
}
