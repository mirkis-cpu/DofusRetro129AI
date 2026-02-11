// 

// 

package org.aestia.exchange;

import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import org.apache.mina.core.session.IdleStatus;
import org.aestia.object.Server;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.service.IoHandler;

public class ExchangeHandler implements IoHandler
{
    public void exceptionCaught(final IoSession arg0, final Throwable arg1) throws Exception {
        System.out.println("eSession " + arg0.getId() + " exception : " + arg1.getCause() + " : " + arg1.getMessage());
    }
    
    public void messageReceived(final IoSession arg0, final Object arg1) throws Exception {
        final String string = new String(((IoBuffer)arg1).array());
        System.out.println("eSession " + arg0.getId() + " < " + string);
        if (string.startsWith("MS")) 
            Server.get(Integer.parseInt(string.substring(2).split("\\|")[0])).send(arg1);
        else if (arg0.getAttribute((Object)"client") instanceof ExchangeClient) {
            final String message = this.bufferToString(arg1);
            final ExchangeClient client = (ExchangeClient)arg0.getAttribute((Object)"client");
            client.parse(message);
        }
    }
    
    public void messageSent(final IoSession arg0, final Object arg1) throws Exception {
        final String message = this.bufferToString(arg1);
        System.out.println("eSession " + arg0.getId() + " > " + message);
    }
    
    public void sessionClosed(final IoSession arg0) throws Exception {
        System.out.println("eSession " + arg0.getId() + " closed");
        if (arg0.getAttribute((Object)"client") instanceof ExchangeClient) {
            final ExchangeClient client = (ExchangeClient)arg0.getAttribute((Object)"client");
            client.getServer().setState(0);
        }
    }
    
    public void sessionCreated(final IoSession arg0) throws Exception {
        System.out.println("eSession " + arg0.getId() + " created");
        arg0.setAttribute((Object)"client", (Object)new ExchangeClient(arg0.getId(), arg0));
        final IoBuffer ioBuffer = IoBuffer.allocate(2048);
        ioBuffer.put("SK?".getBytes());
        ioBuffer.flip();
        arg0.write((Object)ioBuffer);
    }
    
    public void sessionIdle(final IoSession arg0, final IdleStatus arg1) throws Exception {
        System.out.println("eSession " + arg0.getId() + " idle");
    }
    
    public void sessionOpened(final IoSession arg0) throws Exception {
    }
    
    public String bufferToString(final Object o) {
        final IoBuffer buffer = IoBuffer.allocate(2048);
        buffer.put((IoBuffer)o);
        buffer.flip();
        final CharsetDecoder cd = Charset.forName("UTF-8").newDecoder();
        try {
            return buffer.getString(cd);
        }
        catch (CharacterCodingException ex) {
            return "undefined";
        }
    }
    
    public void inputClosed(final IoSession arg0) throws Exception {
        arg0.close(true);
    }
    
}
