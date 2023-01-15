

package dev.madcat.rebirth.util;

public class TimerUtils
{
    private final long current;
    private long time;
    
    public TimerUtils() {
        this.current = System.currentTimeMillis();
        this.time = this.getCurrentTime();
    }
    
    protected final long getCurrentTime() {
        return System.currentTimeMillis();
    }
    
    public final long getTime() {
        return this.time;
    }
    
    protected final void setTime(final long l2) {
        this.time = l2;
    }
    
    public boolean passed(final long ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }
    
    public boolean passed(final double ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }
    
    public long convertToNS(final long time) {
        return time * 1000000L;
    }
    
    public void setMs(final long ms) {
        this.time = System.nanoTime() - this.convertToNS(ms);
    }
    
    public boolean tickAndReset(final long ms) {
        if (System.currentTimeMillis() - this.time >= ms) {
            this.reset();
            return true;
        }
        return false;
    }
    
    public void reset() {
        this.time = System.currentTimeMillis();
    }
    
    public boolean hasReached(final long var1) {
        return System.currentTimeMillis() - this.current >= var1;
    }
    
    public boolean hasReached(final long var1, final boolean var3) {
        if (var3) {
            this.reset();
        }
        return System.currentTimeMillis() - this.current >= var1;
    }
    
    public boolean passedS(final double s) {
        return this.passedMs((long)s * 1000L);
    }
    
    public boolean passedDms(final double dms) {
        return this.passedMs((long)dms * 10L);
    }
    
    public boolean passedDs(final double ds) {
        return this.passedMs((long)ds * 100L);
    }
    
    public boolean passedMs(final long ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }
    
    public long timePassed(final long n) {
        return System.currentTimeMillis() - n;
    }
    
    public long getPassedTimeMs() {
        return System.currentTimeMillis() - this.time;
    }
    
    public final boolean passedTicks(final int ticks) {
        return this.passed(ticks * 50);
    }
    
    public void resetTimeSkipTo(final long p_MS) {
        this.time = System.currentTimeMillis() + p_MS;
    }
    
    public boolean passed(final float ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }
    
    public boolean passed(final int ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }
}
