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
/**
 * Developed based on the publications found on the Internet at
 * http://www.skipy.ru/technics/gui_sync.html
 * Thanks and best regards to author of publishing
 * 
 * @author wladimirowichbiaran
 */
public class NcThProcLoader implements Runnable, NcThProcLoaderInterface {
    private boolean executed = false;
    private NcThProcGUICallbackInterface proxyInstGuiCB;
    private boolean canceled = false;
    /**
     * Not used
     */
    protected NcThProcLoader(){
        
    }
    /**
     * Not used
     */
    @Override
    public void run() {
        
        
    }
    /**
     * Not used
     */
    @Override
    public synchronized void execute() {
        if( executed ){
            throw new IllegalStateException("Process is run");
        }
        executed = true;
        long nowTime = System.nanoTime();
        Thread t = new Thread(this, "NcThProcAt-" + nowTime);
        t.start();
    }
    /**
     * Not used
     */
    @Override
    public synchronized void cancel() {
        canceled = true;
    }
    /**
     * Not used
     * @return 
     */
    public synchronized boolean isCanceled(){
        return canceled;
    }
    /**
     * Not used
     */
    @Override
    public synchronized void state() {
        
    }
    /**
     * Not used
     */
    @Override
    public synchronized void error() {
        
    }
    /**
     * Not used
     */
    @Override
    public synchronized void stats() {
        
    }
    
}
