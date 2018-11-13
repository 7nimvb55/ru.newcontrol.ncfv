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

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 *
 * @author wladimirowichbiaran
 */
public class AppThManager {
    private ConcurrentSkipListMap<String,Runnable> currentWorkerList;
    private ArrayBlockingQueue<String> messagesQueueForLogging;
    private AppObjectsList outerObectsForApp;

    public AppThManager(AppObjectsList obectsForApp) {
        this.outerObectsForApp = obectsForApp;
        this.currentWorkerList = obectsForApp.getWorkerList();
        this.messagesQueueForLogging = obectsForApp.getLoggingQueue();
        
    }
    protected AppObjectsList getListOfObjects(){
        return this.outerObectsForApp;
    }
    
    protected static void createNewWorkerGroup(){
        ThreadGroup groupForThreads = new ThreadGroup("ncfvThGroup");
        Thread thForExecutions = new Thread(groupForThreads, "addThread");
    }
    
    /**
     * create threads with called class.static functions in his logic and algoritms...
     */
}
