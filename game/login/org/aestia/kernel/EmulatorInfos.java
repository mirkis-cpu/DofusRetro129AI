// 

// 

package org.aestia.kernel;

public enum EmulatorInfos
{
    RELEASE("RELEASE", 0, 1.01), 
    SOFT_NAME("SOFT_NAME", 1, "Aestia Login v" + EmulatorInfos.RELEASE.value), 
    HARD_NAME("HARD_NAME", 2, EmulatorInfos.SOFT_NAME + " pour dofus " + Main.config.getConfigFile().getString("network.version"));
    
    private String string;
    private double value;
    
    private EmulatorInfos(final String s2, final int n, final String s) {
        this.string = s;
    }
    
    private EmulatorInfos(final String s, final int n, final double d) {
        this.value = d;
    }
    
    public static String uptime() {
        long uptime = System.currentTimeMillis() - Main.config.startTime;
        final int jour = (int)(uptime / 86400000L);
        uptime %= 86400000L;
        final int hour = (int)(uptime / 3600000L);
        uptime %= 3600000L;
        final int min = (int)(uptime / 60000L);
        uptime %= 60000L;
        final int sec = (int)(uptime / 1000L);
        return String.valueOf(jour) + "j " + hour + "h " + min + "m " + sec + "s";
    }
    
    @Override
    public String toString() {
        return this.string;
    }
}
