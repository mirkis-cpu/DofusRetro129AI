// 

// 

package org.aestia.kernel;

class Instance
{
    private Runnable runnable;
    private Long time;
    
    public Instance(final Runnable runnable, final Long time) {
        this.runnable = runnable;
        this.time = time;
    }
    
    public Runnable getRunnable() {
        return this.runnable;
    }
    
    public Long getTime() {
        return this.time;
    }
}
