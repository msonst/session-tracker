/*
 * Copyright (c) 2017, Michael Sonst, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
