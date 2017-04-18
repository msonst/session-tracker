package de.sonsts.session.tracker;

import java.util.HashMap;

import com.sun.jna.platform.win32.Wtsapi32;

public enum RunState
{
    SESSION_LOCK(Wtsapi32.WTS_SESSION_LOCK), SESSION_UNLOCK(Wtsapi32.WTS_SESSION_UNLOCK), INVALID(Integer.MIN_VALUE);
    
    private int mWtsId;
    private static final HashMap<Integer, RunState> MAP = new HashMap<>();
    
    static
    {
        for (RunState runState : values())
        {
            MAP.put(runState.getValue(), runState);
        }
    }
    
    private RunState(int wtsId)
    {
        mWtsId = wtsId;
    }
    
    public static RunState valueOf(int wtsId)
    {
        RunState runState = MAP.get(wtsId);
        return (null != runState) ? runState : INVALID;
    }
    
    public int getValue()
    {
        return mWtsId;
    }
}
