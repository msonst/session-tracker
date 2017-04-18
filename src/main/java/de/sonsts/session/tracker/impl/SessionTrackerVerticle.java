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

package de.sonsts.session.tracker.impl;

import org.apache.log4j.Logger;

import com.sun.jna.WString;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.MSG;
import com.sun.jna.platform.win32.WinUser.WNDCLASSEX;
import com.sun.jna.platform.win32.WinUser.WindowProc;
import com.sun.jna.platform.win32.Wtsapi32;

import de.sonsts.session.tracker.RunState;
import de.sonsts.session.tracker.StateChangeEvent;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class SessionTrackerVerticle extends AbstractVerticle implements WindowProc
{
    private static final Logger LOGGER = Logger.getLogger(SessionTrackerVerticle.class);
    
    private String mAddress;
    private long mUpdateCycleMs = 0;
    private RunState mState = RunState.INVALID;
    
    public SessionTrackerVerticle(String address)
    {
        super();
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("ENTER address=" + address);
        }
        
        mAddress = address;
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("LEAVE");
        }
    }
    
    public SessionTrackerVerticle(String address, long updateCycleMs)
    {
        this(address);
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("ENTER address=" + address + ", updateCycleMs=" + updateCycleMs);
        }
        
        mUpdateCycleMs = updateCycleMs;
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("LEAVE");
        }
    }
    
    public LRESULT callback(HWND hwnd, int uMsg, WPARAM wParam, LPARAM lParam)
    {
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("ENTER hwnd=" + hwnd + ", uMsg=" + uMsg + ", wParam=" + wParam + ", lParam=" + lParam);
        }
        
        LRESULT retVal = null;
        
        switch (uMsg)
        {
            case WinUser.WM_DESTROY:
            {
                User32.INSTANCE.PostQuitMessage(0);
                retVal = new LRESULT(0);
                break;
            }
            case WinUser.WM_SESSION_CHANGE:
            {
                onSessionChange(wParam, lParam);
                retVal = new LRESULT(0);
                break;
            }
            default:
                retVal = User32.INSTANCE.DefWindowProc(hwnd, uMsg, wParam, lParam);
        }
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("LEAVE retVal=" + retVal);
        }
        
        return retVal;
    }
    
    public int getLastError()
    {
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("ENTER");
        }
        
        int retVal = Kernel32.INSTANCE.GetLastError();
        
        if (retVal != 0)
        {
            System.out.println("error: " + retVal);
        }
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("send - LEAVE retVal=" + retVal);
        }
        
        return retVal;
    }
    
    protected void send(StateChangeEvent event)
    {
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("ENTER event=" + event);
        }
        
        if (null != vertx)
        {
            vertx.eventBus().publish(mAddress, new JsonObject(Json.encodePrettily(event)));
        }
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("LEAVE");
        }
    }
    
    private void onSessionChange(WPARAM wParam, LPARAM lParam)
    {
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("ENTER wParam=" + wParam + ", lParam=" + lParam);
        }
        
        mState = RunState.valueOf(wParam.intValue());
        send(new StateChangeEvent(System.currentTimeMillis(), mState));
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("LEAVE");
        }
    }
    
    protected void evaluate()
    {
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("ENTER");
        }
        
        final WString windowClass = new WString(this.getClass().getSimpleName());
        final HMODULE hInst = Kernel32.INSTANCE.GetModuleHandle("");
        
        WNDCLASSEX wClass = new WNDCLASSEX();
        wClass.hInstance = hInst;
        wClass.lpfnWndProc = SessionTrackerVerticle.this;
        wClass.lpszClassName = windowClass;
        
        User32.INSTANCE.RegisterClassEx(wClass);
        getLastError();
        
        final HWND hWnd = User32.INSTANCE.CreateWindowEx(User32.WS_EX_TOPMOST, windowClass,
            "'Helper window to track session state", 0, 0, 0, 0, 0, null, null, hInst, null);
        
        getLastError();
        
        Wtsapi32.INSTANCE.WTSRegisterSessionNotification(hWnd, Wtsapi32.NOTIFY_FOR_THIS_SESSION);
        
        MSG msg = new MSG();
        while (0 != User32.INSTANCE.GetMessage(msg, hWnd, 0, 0))
        {
            User32.INSTANCE.TranslateMessage(msg);
            User32.INSTANCE.DispatchMessage(msg);
        }
        
        Wtsapi32.INSTANCE.WTSUnRegisterSessionNotification(hWnd);
        User32.INSTANCE.UnregisterClass(windowClass, hInst);
        User32.INSTANCE.DestroyWindow(hWnd);
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("LEAVE");
        }
    }
    
    @Override
    public void start(Future<Void> startFuture)
    {
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("ENTER");
        }
        
        if (mUpdateCycleMs > 0)
        {
            vertx.setPeriodic(mUpdateCycleMs, t -> send(new StateChangeEvent(t, mState)));
        }
        
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                evaluate();
            }
        }).start();
        
        if (LOGGER.isTraceEnabled())
        {
            LOGGER.trace("LEAVE");
        }
    }
}
