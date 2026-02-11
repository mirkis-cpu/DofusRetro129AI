// 

// 

package org.aestia.object;

import java.io.Serializable;

public class Player implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int id;
    private int server;
    private int groupe;
    
    public Player(final int id, final int server, final int groupe) {
        this.id = id;
        this.server = server;
        this.groupe = groupe;
    }
    
    public int getGroupe() {
        return this.groupe;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getServer() {
        return this.server;
    }
    
    public void setServer(final int server) {
        this.server = server;
    }
}
