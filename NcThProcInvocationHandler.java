/*
 * Copyright 2018 wladimirowichbiaran.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.newcontrol.ncfv;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;

/**
 * Developed based on the publications found on the Internet at
 * http://www.skipy.ru/technics/gui_sync.html
 * Thanks and best regards to author of publishing
 * 
 * @author wladimirowichbiaran
 */
public class NcThProcInvocationHandler implements InvocationHandler {
    private Object invocationResult = null;
    private NcThProcGUICallback ui;
    
    public NcThProcInvocationHandler(NcThProcGUICallback ui){
        this.ui = ui;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        NcThProcTypeDetect typeProc = method.getAnnotation(NcThProcTypeDetect.class);
        if( typeProc != null ){
            if( SwingUtilities.isEventDispatchThread() ){
                invocationResult = method.invoke(ui, args);
            }
            else{
                Runnable shellThread = new Runnable(){
                    @Override
                    public void run(){
                        try{
                            invocationResult = method.invoke(ui, args);
                        }
                        catch(Exception ex){
                            throw new RuntimeException(ex);
                        }
                    }
                };
                if( NcThProcType.ASYNC.equals(typeProc.value()) ){
                    SwingUtilities.invokeLater(shellThread);
                }
                else{
                    SwingUtilities.invokeAndWait(shellThread);
                }
            }
        }
        else{
            invocationResult = method.invoke(ui, args);
        }
        return invocationResult;
    }
    
}
