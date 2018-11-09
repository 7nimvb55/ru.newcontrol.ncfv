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
    private ConcurrentSkipListMap<String,Thread> currentWorkerList;
    private ArrayBlockingQueue<String> messagesQueueForLogging;
    private final Integer messagesQueueSize = 1000;

    public AppThManager() {
        this.currentWorkerList = new ConcurrentSkipListMap<>();
        this.messagesQueueForLogging = new ArrayBlockingQueue<String>(messagesQueueSize);
    }
    
    
    
    protected static void createNewWorkerGroup(){
        ThreadGroup groupForThreads = new ThreadGroup("ncfvThGroup");
        Thread thForExecutions = new Thread(groupForThreads, "addThread");
        
        
        
    }
    protected void doLogger(){
        String nameForWorker = AppMsgEnPrefixes.TH_NAME_LOG;
        Boolean existThread = Boolean.TRUE;
        try{
            Thread foundedThread = currentWorkerList.get(nameForWorker.hashCode());
        } catch(NullPointerException ex){
            currentWorkerList.put(nameForWorker, new Thread());
            existThread = Boolean.FALSE;
        }

    }
    
    protected void getAnyThread(String nameForWorker) throws CloneNotSupportedException{
        Boolean existThread = Boolean.TRUE;
        try{
            Thread foundedThread = currentWorkerList.get(nameForWorker.hashCode());
        } catch(NullPointerException ex){
            currentWorkerList.put(nameForWorker, new Thread());
            existThread = Boolean.FALSE;
        }
        if( !existThread ){
            throw new CloneNotSupportedException(AppMsgEnFiledForLog.MSG_EXIST_THREAD_KEY
            + nameForWorker);
        }
    }
    /**
     * create threads with called class.static functions in his logic and algoritms...
     */
}
