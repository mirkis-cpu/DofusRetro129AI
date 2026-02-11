// 

// 

package org.aestia.login;

import org.aestia.login.packet.PacketHandler;
import org.aestia.object.Account;
import org.apache.mina.core.session.IoSession;

public class LoginClient
{
    private long id;
    private IoSession ioSession;
    private String key;
    private Status status;
    private Account account;
    
    public LoginClient(final IoSession ioSession, final String key) {
        this.setStatus(Status.WAIT_VERSION);
        this.setId(ioSession.getId());
        this.setIoSession(ioSession);
        this.setKey(key);
        this.send("HC" + this.getKey());
    }
    
    public void send(final Object object) {
        this.ioSession.write(object);
    }
    
    public void parser(final String packet) {
        PacketHandler.parser(this, packet);
    }
    
    public void kick() {
    	this.ioSession.removeAttribute("client");
        this.ioSession.close(true);
    }
    
    public long getId() {
        return this.id;
    }
    
    private void setId(final long l) {
        this.id = l;
    }
    
    public IoSession getIoSession() {
        return this.ioSession;
    }
    
    private void setIoSession(final IoSession ioSession) {
        this.ioSession = ioSession;
    }
    
    public String getKey() {
        return this.key;
    }
    
    private void setKey(final String key) {
        this.key = key;
    }
    
    public Status getStatus() {
        return this.status;
    }
    
    public void setStatus(final Status status) {
        this.status = status;
    }
    
    public Account getAccount() {
        return this.account;
    }
    
    public void setAccount(final Account account) {
        this.account = account;
    }
    
    public enum Status
    {
        WAIT_VERSION("WAIT_VERSION", 0), 
        WAIT_PASSWORD("WAIT_PASSWORD", 1), 
        WAIT_ACCOUNT("WAIT_ACCOUNT", 2), 
        WAIT_NICKNAME("WAIT_NICKNAME", 3), 
        SERVER("SERVER", 4);
        
        private Status(final String s, final int n) {
        }
    }
}
