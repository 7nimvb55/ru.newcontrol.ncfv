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
    private AppListOfObjects outerObectsForApp;
    
    private final Integer messagesQueueSize = 1000;

    public AppThManager(AppListOfObjects obectsForApp) {
        this.outerObectsForApp = obectsForApp;
        this.currentWorkerList = obectsForApp.getWorkerList();
        this.messagesQueueForLogging = obectsForApp.getLoggingQueue();
        
    }
    protected String getPrefixInfo(){
        String prefixStr = AppMsgEnFiledForLog.FIELD_START
                + AppFileOperationsSimple.getNowTimeStringWithMS()
                + AppMsgEnFiledForLog.FIELD_STOP
                + AppMsgEnFiledForLog.INFO;
        return prefixStr;
    }
    
    protected void putLogInfoMessage(String strToLog){
        if( !strToLog.isEmpty() ){
            String prefixStr = getPrefixInfo()
                + strToLog;
        
            messagesQueueForLogging.add(prefixStr);
        }
        if( !messagesQueueForLogging.isEmpty()){
            if( messagesQueueForLogging.size() > 100 ){
                doLogger();
            }
        }
        System.out.println("for log ready " + messagesQueueForLogging.size());
    }
    
    protected static void createNewWorkerGroup(){
        ThreadGroup groupForThreads = new ThreadGroup("ncfvThGroup");
        Thread thForExecutions = new Thread(groupForThreads, "addThread");
        
        
        
    }
    protected void doLogger(){
        Thread foundedThread;
        Boolean existThread = Boolean.TRUE;
        try{
            foundedThread = this.outerObectsForApp.getLogger();
            foundedThread.start();
        } catch(NullPointerException ex){

            System.out.println("null for init logger " + ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
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
