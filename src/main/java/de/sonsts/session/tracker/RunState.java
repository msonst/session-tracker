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
