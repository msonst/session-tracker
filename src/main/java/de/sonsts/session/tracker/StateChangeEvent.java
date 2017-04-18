package de.sonsts.session.tracker;

public class StateChangeEvent
{
    private long mTimestamp;
    private RunState mRunState;
    
    public StateChangeEvent(long timestamp)
    {
        init(timestamp, RunState.INVALID);
    }
    
    public StateChangeEvent(RunState runstate)
    {
        init(System.currentTimeMillis(), runstate);
    }
    
    public StateChangeEvent()
    {
        init(System.currentTimeMillis(), RunState.INVALID);
    }
    
    public StateChangeEvent(long timestamp, RunState runstate)
    {
        init(timestamp, runstate);
    }
    
    private void init(long timestamp, RunState runState)
    {
        setTimestamp(timestamp);
        setRunState(runState);
    }
    
    public RunState getRunState()
    {
        return mRunState;
    }
    
    public void setRunState(RunState runState)
    {
        mRunState = runState;
    }
    
    public void setTimestamp(long timestamp)
    {
        mTimestamp = timestamp;
    }
    
    public long getTimestamp()
    {
        return mTimestamp;
    }
    
    @Override
    public String toString()
    {
        return "StateChangeEvent [Timestamp=" + mTimestamp + ", RunState=" + mRunState + "]";
    }
}
