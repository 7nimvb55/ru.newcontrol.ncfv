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

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThExRouter <V>
        implements Callable<ConcurrentSkipListMap<UUID, String>> {
    private static String typeThread = "[ROUTER]";
    /*private ConcurrentSkipListMap<UUID, Callable> ncGetCallableQueue;
    private ConcurrentSkipListMap<UUID, Future> ncGetFutureQueue;
    
    private boolean boolDone;
    private boolean FAIR_QUEUE;
    private int LENGTH_QUEUE;
    private BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> pipeDirList;
    private int ncGetQueueSize;
    private NcThExRouter router;
    private NcThExDirTreeWalk dirWalker;
    private NcThExListAttrScanDir dirListScanner;
    private NcThExListPack resultPacker;*/
    private ThreadLocal<NcThExecPool> executorPool = new ThreadLocal<NcThExecPool>();
    private ThreadLocal<Boolean> isDone = new ThreadLocal<Boolean>();
    private ThreadLocal<Boolean> needFinish = new ThreadLocal<Boolean>();
    private ThreadLocal<Long> needSleepInTime = new ThreadLocal<Long>();
    private ThreadLocal<NcThExInfo> timeToSleep = new ThreadLocal<NcThExInfo>();
    private ThreadLocal<NcThExInfo> threadParams = new ThreadLocal<NcThExInfo>();
    /*public NcThExRouter() {
        
        this.isDone.set(Boolean.FALSE);
        this.needFinish.set(Boolean.FALSE);
    }*/
    
    
    /*public NcThExRouter(NcThExecPool executorPool) throws Exception {
        this.isDone.set(Boolean.FALSE);
        this.needFinish.set(Boolean.FALSE);
        this.FAIR_QUEUE = Boolean.TRUE;
        this.LENGTH_QUEUE = 1000;
        this.pipeDirList = new ArrayBlockingQueue(this.LENGTH_QUEUE, this.FAIR_QUEUE);
        try{
            Thread.currentThread().checkAccess();
            this.typeThread = "[ROUTER]";
            NcAppHelper.outCreateObjectMessage(this.typeThread, this.getClass());
            //this.ncGetCallableQueue = executorPool.ncGetCallableQueue();
            //this.ncGetFutureQueue = executorPool.ncGetFutureQueue();
            //this.ncGetQueueSize = executorPool.ncGetQueueSize();
            //this.router = null;
            this.dirWalker = null;
            this.dirListScanner = null;
            this.resultPacker = null;
        } catch (Exception ex) {
            NcAppHelper.logException(NcThExRouter.class.getCanonicalName(), ex);
            String threadInfoToString = NcAppHelper.getThreadInfoToString(Thread.currentThread());
            String classInfoToString = NcAppHelper.getClassInfoToString(NcThExRouter.class);
            throw new Exception(NcStrLogMsgField.ERROR.getStr()
                + threadInfoToString + classInfoToString
                + NcStrLogMsgField.MSG_INFO.getStr()
                + " create router for make index ", ex);
        }
    }*/
    protected void setThreadParams(NcThExInfo inParam){
        if( inParam != null ){
            this.threadParams.set(inParam);
        }
        
    }
    protected void setDirForScan(Path inputDirForScan){
        if( inputDirForScan != null){
            this.threadParams.get().setDirForScan(inputDirForScan);
        }
    }
    protected void setExecPool(NcThExecPool inputPool){
        System.out.println("setExecPool");
        String classInfoToString = NcAppHelper.getClassInfoToString(inputPool.getClass());
        NcAppHelper.outMessage(typeThread + "[ATTEMPTTOADD]"
            + classInfoToString + " toString "
            + inputPool.toString());
        executorPool.set(inputPool);
    }
    private boolean isSetExecPool() throws IllegalThreadStateException {
        if( executorPool.get() == null ){
            NcAppHelper.outMessage(typeThread
                + " executorPool is null");
            return false;
        }
        if( NcThExecPool.class.isInstance(executorPool.get()) ){
            NcAppHelper.outMessage(typeThread
                + " executorPool is set "
                + executorPool.get().getClass().getCanonicalName());
            return true;
        }
        NcThExecPool getInstance = executorPool.get();
        NcAppHelper.outMessage(typeThread
                + " executorPool is set "
                + getInstance.getClass().getCanonicalName());
        return false;
    }
    protected void finishHimByInterrupted() throws InterruptedException{
        throw new InterruptedException("Interrupted by "
                + NcThExRouter.class.getCanonicalName()
                + ".finishHimByInterrupted() in [THREAD]"
                + Thread.currentThread().getName());
    }
    private void initThread(){
        this.timeToSleep.set(new NcThExInfo());
        setNeedSleep(0L);
        this.executorPool.set(null);
        this.isDone.set(Boolean.FALSE);
        this.needFinish.set(Boolean.FALSE);
    }
    protected boolean isDone(){
        return isDone.get();
    }
    
    protected void finishHim(){
        this.needFinish.set(Boolean.TRUE);
    }
    
    @Override
    public ConcurrentSkipListMap<UUID, String> call() throws Exception {
        System.out.println("NcThExRouter.call");
        
        System.out.println("NcThExRouter in NcThExInfo.new");
        Path dirForScan = null;
        System.out.println("NcThExRouter.call: before while: Set dir for scan: null");
        int getterIterationCount = 0;
        while( dirForScan != null ){
            dirForScan = this.threadParams.get().getDirForScan();
            if( getterIterationCount > 35){
                break;
            }
            getterIterationCount++;
        }
        System.out.println("NcThExRouter.call: after while: Set dir for scan: " + dirForScan.toString());
        String strInCallThreadInfo = NcAppHelper.getThreadInfoToString(Thread.currentThread());
        NcAppHelper.outMessage(typeThread + "start, first operator for " + strInCallThreadInfo);
        initThread();
        NcAppHelper.outMessage(typeThread + "init Thread param");
        isSetExecPool();
        int countInteration = 0;
        
        try{
            NcAppHelper.outMessage(typeThread
                + " start thread code countInteration = " + countInteration);
            while( !needFinish.get() ){
                strInCallThreadInfo = NcAppHelper.getThreadInfoToString(Thread.currentThread());
                NcAppHelper.outMessage(strInCallThreadInfo + " " 
                + typeThread
                + " in cicle countInteration = " + countInteration
                //+ " executorPool " + executorPool.get().getClass().getCanonicalName()
                + " needSleepInTime " + String.valueOf(getNeedSleepTime()));
                isSetExecPool();
                needSleep();
                
                countInteration++;
                if( countInteration > 150 ){
                    needFinish.set(Boolean.TRUE);
                }
            }
            NcAppHelper.outMessage(typeThread
                + " finish thread code countInteration = " + countInteration);
        } catch (Exception ex) {
            isDone.set(Boolean.TRUE);
            NcAppHelper.logException(NcThExRouter.class.getCanonicalName(), ex);

            String strThreadInfo = NcAppHelper.getThreadInfoToString(Thread.currentThread());
            throw new IOException(
                strThreadInfo
                + NcStrLogMsgField.MSG_INFO.getStr()
                + "Thread interrupted, reason "
                + NcStrLogMsgField.EXCEPTION_MSG.getStr()
                + ex.getMessage());
        }
        isDone.set(Boolean.TRUE);
        return new ConcurrentSkipListMap<UUID, String>();
    }
    protected void setScanDir(Path scanDir){
        System.out.println("NcThExRouter.setScanDir(" + scanDir.toString() + ")");
        if( scanDir != null ){
            this.threadParams.get().setDirForScan(scanDir);
        }
    }
    protected void setNeedSleep(long sleepTime){
        NcThExInfo getData = timeToSleep.get();
        getData.setSleepTime(sleepTime);
        timeToSleep.set(getData);
        /*if( sleepTime > 0 ){
            needSleepInTime.set(sleepTime);
        }
        if( sleepTime == 0 ){
            needSleepInTime.set(0L);
        }*/
    }
    private long getNeedSleepTime(){
        NcThExInfo getData = timeToSleep.get();
        return getData.getSleepTime();
    }
    private void needSleep() throws Exception{
        //long sleepTime = needSleepInTime.get();
        NcThExInfo getData = timeToSleep.get();
        long sleepTime = getData.getSleepTime();
        if( sleepTime > 0 ){
            try {
                String strThreadInfo = NcAppHelper.getThreadInfoToString(Thread.currentThread());
                NcAppHelper.outMessage(
                    strThreadInfo
                    + NcStrLogMsgField.MSG_WARNING.getStr()
                    + "Go to sleep time, "
                    + sleepTime + " ms");
                Thread.sleep(sleepTime);
                NcAppHelper.outMessage(
                    strThreadInfo
                    + NcStrLogMsgField.MSG_WARNING.getStr()
                    + "Wake up, after sleep time, "
                    + sleepTime + " ms");
            } catch (InterruptedException ex) {
                NcAppHelper.logException(NcThExRouter.class.getCanonicalName(), ex);
                
                String strThreadInfo = NcAppHelper.getThreadInfoToString(Thread.currentThread());
                throw new Exception(
                    strThreadInfo
                    + NcStrLogMsgField.MSG_INFO.getStr()
                    + "Thread interrupted, reason "
                    + NcStrLogMsgField.EXCEPTION_MSG.getStr()
                    + ex.getMessage());
            }
        }
    }
    /*protected boolean startDirWalk(Path pathForScan){
        if( pathForScan == null){
            return false;
        }
        try {
            this.dirWalker = new NcThExDirTreeWalk(pathForScan, this.pipeDirList);
        } catch (IOException ex) {
            NcAppHelper.logException(NcThExRouter.class.getCanonicalName(), ex);
            return false;
        }
        return true;
    }
    protected NcThExDirTreeWalk getDirWalker() throws IllegalThreadStateException{
        if( this.dirWalker != null ){
            return this.dirWalker;
        }
        throw new IllegalThreadStateException("null for [TREEWALKER] not create or lost");
    }
    protected boolean startListScaner(NcThExDirTreeWalk createdWalker){
        if( createdWalker == null){
            return false;
        }
        //try {
            this.dirListScanner = new NcThExListAttrScanDir(createdWalker);
        //} catch (IOException ex) {
            //NcAppHelper.logException(NcThExRouter.class.getCanonicalName(), ex);
            //return false;
        //}
        return true;
    }
    protected boolean startListPack(
            ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> scanResult,
            NcSwGUIComponentStatus lComp){
        if( scanResult.size() < 101 ){
            return false;
        }
        this.resultPacker = new NcThExListPack(scanResult, lComp);
        return true;
    }*/
}
