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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @todo logging for all createt objects and his states
 * @todo need correct clean for free resurses
 * @author wladimirowichbiaran
 */
public class AppObjectsList {
    private ConcurrentSkipListMap<String, Runnable> currentWorkerList;
    private ArrayBlockingQueue<String> messagesQueueForLogging;
    
    private final Integer messagesQueueSize = 1000;

    public AppObjectsList() {
        this.currentWorkerList = new ConcurrentSkipListMap<>();
        this.messagesQueueForLogging = new ArrayBlockingQueue<String>(messagesQueueSize);
        currentWorkerList.put(AppMsgEnPrefixes.TH_NAME_LOG, new AppLoggerToTextRunnable(messagesQueueForLogging));
    }
    protected ConcurrentSkipListMap<String, Runnable> getWorkerList(){
        return currentWorkerList;
    }
    protected ArrayBlockingQueue<String> getLoggingQueue(){
        return messagesQueueForLogging;
    }
    protected Runnable getLogger(){
        Runnable getForReturn = currentWorkerList.get(AppMsgEnPrefixes.TH_NAME_LOG);
        
        if( getForReturn == null ){
            currentWorkerList.put(AppMsgEnPrefixes.TH_NAME_LOG, new AppLoggerToTextRunnable(messagesQueueForLogging));
            getForReturn = currentWorkerList.get(AppMsgEnPrefixes.TH_NAME_LOG);
        }
        return getForReturn;
    }
    protected String getPrefixInfo(){
        String prefixStr = AppMsgEnFiledForLog.FIELD_START
                + AppFileOperationsSimple.getNowTimeStringWithMS()
                + AppMsgEnFiledForLog.FIELD_STOP
                + AppMsgEnFiledForLog.INFO;
        return prefixStr;
    }
    protected String getPrefixState(){
        String prefixStr = AppMsgEnFiledForLog.FIELD_START
                + AppFileOperationsSimple.getNowTimeStringWithMS()
                + AppMsgEnFiledForLog.FIELD_STOP
                + AppMsgEnFiledForLog.STATE;
        return prefixStr;
    }
    protected String getPrefixWarning(){
        String prefixStr = AppMsgEnFiledForLog.FIELD_START
                + AppFileOperationsSimple.getNowTimeStringWithMS()
                + AppMsgEnFiledForLog.FIELD_STOP
                + AppMsgEnFiledForLog.WARINING;
        return prefixStr;
    }
    protected String getPrefixError(){
        String prefixStr = AppMsgEnFiledForLog.FIELD_START
                + AppFileOperationsSimple.getNowTimeStringWithMS()
                + AppMsgEnFiledForLog.FIELD_STOP
                + AppMsgEnFiledForLog.ERROR;
        return prefixStr;
    }
    
    protected void putLogMessageInfo(String strToLog){
        if( !strToLog.isEmpty() ){
            String prefixStr = getPrefixInfo()
                + strToLog;
            messagesQueueForLogging.add(prefixStr);
        }
        if( !messagesQueueForLogging.isEmpty()){
            if( messagesQueueForLogging.size() > AppConstants.LIMIT_MESSAGES_FOR_LOG_IN_QUEUE_COUNT ){
                doLogger();
            }
        }
    }
    protected void putLogMessageState(String strToLog){
        if( !strToLog.isEmpty() ){
            String prefixStr = getPrefixState()
                + strToLog;
            messagesQueueForLogging.add(prefixStr);
        }
        if( !messagesQueueForLogging.isEmpty()){
            if( messagesQueueForLogging.size() > AppConstants.LIMIT_MESSAGES_FOR_LOG_IN_QUEUE_COUNT ){
                doLogger();
            }
        }
    }
    protected void putLogMessageWarning(String strToLog){
        if( !strToLog.isEmpty() ){
            String prefixStr = getPrefixWarning()
                + strToLog;
            messagesQueueForLogging.add(prefixStr);
        }
        if( !messagesQueueForLogging.isEmpty()){
            if( messagesQueueForLogging.size() > AppConstants.LIMIT_MESSAGES_FOR_LOG_IN_QUEUE_COUNT ){
                doLogger();
            }
        }
    }
    protected void putLogMessageError(String strToLog){
        if( !strToLog.isEmpty() ){
            String prefixStr = getPrefixError()
                + strToLog;
            messagesQueueForLogging.add(prefixStr);
        }
        if( !messagesQueueForLogging.isEmpty()){
            if( messagesQueueForLogging.size() > AppConstants.LIMIT_MESSAGES_FOR_LOG_IN_QUEUE_COUNT ){
                doLogger();
            }
        }
    }
    protected void doLogger(){
        Thread foundedThread;
        Boolean existThread = Boolean.TRUE;
        try{
            foundedThread = new Thread(this.getLogger());
            
            ThreadGroup testTG = new ThreadGroup("TestForLogger");
            
            AppThExtendsBaseThread appThExtendsBaseThread = new AppThExtendsBaseThread(3);
            System.out.println(" * * * First create Thread from Runnable and start");
            Thread forTest = new Thread(testTG,appThExtendsBaseThread,"testThread-" + AppFileOperationsSimple.getNowTimeStringWithMS());
            System.out.println("[NAME]" + forTest.getName() 
                    + "[toString]" + forTest.toString()
                    + "[getId]" + String.valueOf(forTest.getId())
                    + "[getPriority]" + String.valueOf(forTest.getPriority())
                    + "[getState][name]" + String.valueOf(forTest.getState().name())
                    + "[getState][ordinal]" + String.valueOf(forTest.getState().ordinal())
                    + "[isDaemon]" + String.valueOf(forTest.isDaemon())
                    + "[isAlive]" + String.valueOf(forTest.isAlive())
                    + "[isInterrupted]" + String.valueOf(forTest.isInterrupted())
            );
            System.out.println("[NcAppHelper.getThreadInfoToString]" + NcAppHelper.getThreadInfoToString(forTest));
            forTest.start();
            try{
                forTest.join();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
            System.out.println(" * * * Second create Thread from Runnable and start");
            
            forTest = new Thread(testTG,appThExtendsBaseThread,"testThread" + AppFileOperationsSimple.getNowTimeStringWithMS());
            System.out.println("" + forTest.getName());
            forTest.start();
            try{
                forTest.join();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
            System.out.println(" * * * Thrid create Thread from Runnable and start");
            forTest = new Thread(testTG,appThExtendsBaseThread,"testThread" + AppFileOperationsSimple.getNowTimeStringWithMS());
            System.out.println("" + forTest.getName());
            forTest.start();
            try{
                forTest.join();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
            //For use object first time whith his State.NEW = 0 used Thread.start();
            /*System.out.println("[ThreadGrpupName]" + foundedThread.getThreadGroup().getName()
                    + "[ThreadGrpupActiveCount]" + foundedThread.getThreadGroup().activeCount()
                    + "[ThreadGrpupActiveGroupCount]" + foundedThread.getThreadGroup().activeGroupCount()
                    + "[State]" + foundedThread.getState().name()
                    + "[ORDINAL]" + foundedThread.getState().ordinal() 
                    + "[ThreadName]" + foundedThread.getName()
                    + "[ThreadToString]" + foundedThread.toString()
                    + "[ThreadID]" + foundedThread.getId()
                    + "[ThreadHash]" + foundedThread.hashCode()
                    );
            if( foundedThread.getState().ordinal() != 0 ){
                foundedThread = new Thread(foundedThread);
            }
            System.out.println("[ThreadGrpupName]" + foundedThread.getThreadGroup().getName()
                    + "[ThreadGrpupActiveCount]" + foundedThread.getThreadGroup().activeCount()
                    + "[ThreadGrpupActiveGroupCount]" + foundedThread.getThreadGroup().activeGroupCount()
                    + "[State]" + foundedThread.getState().name()
                    + "[ORDINAL]" + foundedThread.getState().ordinal() 
                    + "[ThreadName]" + foundedThread.getName()
                    + "[ThreadToString]" + foundedThread.toString()
                    + "[ThreadID]" + foundedThread.getId()
                    + "[ThreadHash]" + foundedThread.hashCode()
                    );*/
            //System.out.println("[State]" + foundedThread.getState().name() + "[ThreadName]" + foundedThread.getName());
            //foundedThread.setName("Logger" + AppFileOperationsSimple.getNowTimeStringWithMS());
            //System.out.println("[State]" + foundedThread.getState().name() + "[ThreadName]" + foundedThread.getName());
            //ThreadGroup nowLogGroup = new ThreadGroup("Logger" + AppFileOperationsSimple.getNowTimeStringWithMS());
            
            //try{
                //foundedThread.start();
            //} catch (IllegalStateException ex){
            //    System.out.println("[State]" + foundedThread.getState().name() + " [ERRORMESSAGE] " + ex.getMessage());
            //    ex.printStackTrace();
            //}
            
            //@todo create method restart thread...
            foundedThread.start();
            try{
                foundedThread.join();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
        } catch(NullPointerException ex){
            System.out.println("[CRITICALERROR]NullPointerException for init logger " + ex.getMessage());
            ex.printStackTrace();
            System.exit(0);
        }
    }
    protected Runnable addAnyThread(Runnable workerForAdd, String nameForKey) {
        String nameForWorker = nameForKey;
        Runnable  foundedThread = currentWorkerList.get(nameForWorker);
        if( foundedThread == null ){
            currentWorkerList.put(nameForWorker, workerForAdd);
            foundedThread = currentWorkerList.get(nameForWorker);
        }
        if( AppConstants.LOG_LEVEL_CURRENT > AppConstants.LOG_LEVEL_SILENT ){
            putLogMessageState("[ADDOBJECTTOLIST]" + nameForWorker);
            if( AppConstants.LOG_LEVEL_CURRENT > AppConstants.LOG_LEVEL_USE ){
                if( foundedThread != null ){
                    String classInfoToString = AppObjectsInfoHelperClasses.getClassInfoToString(foundedThread.getClass());
                    putLogMessageState("[CLASS]" + classInfoToString);
                }
            }
        }
        return foundedThread;
    }
    protected Runnable getThreadByKey(String nameForWorker){
        Runnable foundedThread = currentWorkerList.get(nameForWorker);
        return foundedThread;
    }
}
