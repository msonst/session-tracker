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

package de.sonsts.utils.impl;

public class OperatingSystem
{
    // OS flags
    public static final int OS_OTHER = 0;
    public static final int OS_WINDOWS = 1;
    public static final int OS_MAC = 2;
    public static final int OS_UNIX = 3;
    
    public static int getOs()
    {
        int retVal = 0;
        
        if (System.getProperty("os.name").toLowerCase().contains("win"))
        {
            retVal = OS_WINDOWS;
        }
        else if (System.getProperty("os.name").toLowerCase().contains("mac"))
        {
            retVal = OS_MAC;
        }
        else if (System.getProperty("os.name").toLowerCase().contains("nix"))
        {
            retVal = OS_UNIX;
        }
        else
        {
            retVal = OS_OTHER;
        }
        
        return retVal;
    }
}
