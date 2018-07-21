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
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author wladimirowichbiaran
 * @param <E>
 */
public class NcThExecPool implements ExecutorService {
    private static String typeThread;
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAXIMUM_POOL_SIZE = 4;
    private static final int QUEUE_LENGTH = 100;
    private Path dirForScanToList;
    private final BlockingQueue<Runnable> workQueue;
    private final ExecutorService execSuper;
    private final ConcurrentSkipListMap<UUID, Callable> callableQueue;
    private final ConcurrentSkipListMap<UUID, Future> futureQueue;
    
    public NcThExecPool(NcThExStatus jobParam) {
        Thread.currentThread().checkAccess();
        this.typeThread = "[EXECPOOL]";
        NcAppHelper.outCreateObjectMessage(this.typeThread, this.getClass());
        this.callableQueue = new ConcurrentSkipListMap<UUID, Callable>();
        this.futureQueue = new ConcurrentSkipListMap<UUID, Future>();
        this.workQueue = new ArrayBlockingQueue<>(this.QUEUE_LENGTH);
        this.execSuper =
                new ThreadPoolExecutor(this.CORE_POOL_SIZE,
                        this.MAXIMUM_POOL_SIZE,
                        0L,
                        TimeUnit.MILLISECONDS,
                        this.workQueue);
        
    }
    
    /**
     * @deprecated
     */
    public NcThExecPool() {
        Thread.currentThread().checkAccess();
        this.typeThread = "[EXECPOOL]";
        NcAppHelper.outCreateObjectMessage(this.typeThread, this.getClass());
        this.callableQueue = new ConcurrentSkipListMap<UUID, Callable>();
        this.futureQueue = new ConcurrentSkipListMap<UUID, Future>();
        this.workQueue = new ArrayBlockingQueue<>(this.QUEUE_LENGTH);
        this.execSuper =
                new ThreadPoolExecutor(this.CORE_POOL_SIZE,
                        this.MAXIMUM_POOL_SIZE,
                        0L,
                        TimeUnit.MILLISECONDS,
                        this.workQueue);
        
    }
    /**
     * @deprecated
     * @param forReadList
     * @throws IOException 
     */
    public NcThExecPool(Path forReadList) throws IOException {
        Thread.currentThread().checkAccess();
        this.typeThread = "[EXECPOOL]";
        NcAppHelper.outCreateObjectMessage(this.typeThread, this.getClass());
        
        this.dirForScanToList = null;
        Path pathToStart = Paths.get(forReadList.toString());
        try{
            pathToStart = NcFsIdxOperationDirs.checkScanPath(pathToStart);
        } catch (IOException ex) {
            String strAddMsg = NcStrLogMsgField.MSG_ERROR.getStr()
                    + " wrong path for scan "
                    + forReadList.toString();
            NcAppHelper.outMessage(strAddMsg);
            NcAppHelper.logException(NcThExecPool.class.getCanonicalName(), ex);
            throw new IOException(strAddMsg, ex);
        }
        this.dirForScanToList = pathToStart;
        this.callableQueue = new ConcurrentSkipListMap<UUID, Callable>();
        this.futureQueue = new ConcurrentSkipListMap<UUID, Future>();
        this.workQueue = new ArrayBlockingQueue<>(this.QUEUE_LENGTH);
        this.execSuper =
                new ThreadPoolExecutor(this.CORE_POOL_SIZE,
                        this.MAXIMUM_POOL_SIZE,
                        0L,
                        TimeUnit.MILLISECONDS,
                        this.workQueue);
        
    }
    /**
     * @deprecated 
     * @param countThreadPool 
     */
    public NcThExecPool(int countThreadPool) {
        Thread.currentThread().checkAccess();
        this.typeThread = "[EXECPOOL]";
        NcAppHelper.outCreateObjectMessage(this.typeThread, this.getClass());
        this.callableQueue = new ConcurrentSkipListMap<UUID, Callable>();
        this.futureQueue = new ConcurrentSkipListMap<UUID, Future>();
        this.workQueue = new ArrayBlockingQueue<>(this.QUEUE_LENGTH);
        this.execSuper =
                new ThreadPoolExecutor(this.CORE_POOL_SIZE,
                        this.MAXIMUM_POOL_SIZE,
                        0L,
                        TimeUnit.MILLISECONDS,
                        this.workQueue);
    }
    protected void execScanDirToIndex() throws Exception{
        
    }
    /**
     * @deprecated
     * @throws Exception 
     */
    protected void autoExecRouter() throws Exception{
        
        
        NcThExRouter thRouter = new NcThExRouter();
        System.out.println("NcThExecPool.autoExecRouter NcThExRouter");
        
        
        thRouter.setExecPool(this);
        thRouter.setThreadParams(new NcThExInfo());
        thRouter.setDirForScan(this.dirForScanToList);
        System.out.println("NcThExecPool in NcThExRouter.setScanDir");
        
        System.out.println("NcThExecPool.submit");
        Future submitRouter = this.submit(thRouter);
        
        
        int countWait = 0;

        while( !submitRouter.isDone() ){
            System.out.println("NcThExecPool.while");
            if( submitRouter.isCancelled() ){
                 NcAppHelper.outMessage("[BUTTONTHREAD] is canceled in main thread " + countWait);
            }
            NcAppHelper.outMessage("[BUTTONTHREAD] not for done " + countWait);
            countWait++;
            if( countWait == 30 ){
                NcAppHelper.outMessage("[BUTTONTHREAD] call for thread.setNeedSleep(30) " + countWait);
                thRouter.setNeedSleep(30L);
            }
            if( countWait == 70 ){
                NcAppHelper.outMessage("[BUTTONTHREAD] call for thread.setNeedSleep(0) " + countWait);
                thRouter.setNeedSleep(0L);
            }
            if( countWait > 100 ){
                NcAppHelper.outMessage("[BUTTONTHREAD] call for thread.finishHim " + countWait);
                thRouter.finishHim();
            }
            if( countWait > 130 ){
                NcAppHelper.outMessage("[BUTTONTHREAD] call for thread.finishHimByInterrupted " + countWait);

                try {
                    thRouter.finishHimByInterrupted();
                } catch (InterruptedException ex) {
                    NcAppHelper.logException(NcThExecPool.class.getCanonicalName(), ex);
                    String classInfoToString = NcAppHelper.getClassInfoToString(NcThExecPool.class);
                    throw new Exception( NcStrLogMsgField.ERROR.getStr()
                            + classInfoToString
                            + NcStrLogMsgField.MSG_INFO.getStr()
                            + " part of start make index is Interrupted ", ex);
                            }
            }
        }
    }
    /**
     * @deprecated
     * @return 
     */
    protected int ncGetQueueSize(){
        return this.workQueue.size();
    }
    /**
     * @deprecated
     * @return 
     */
    protected ConcurrentSkipListMap<UUID, Callable> ncGetCallableQueue(){
        return this.callableQueue;
    }
    /**
     * @deprecated
     * @return 
     */
    protected ConcurrentSkipListMap<UUID, Future> ncGetFutureQueue(){
        return this.futureQueue;
    }

    @Override
    public void shutdown() {
        shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        List<Runnable> shutdownNow = execSuper.shutdownNow();
        return shutdownNow;
    }

    @Override
    public boolean isShutdown() {
        return execSuper.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return execSuper.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return execSuper.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        //String threadInfoToString = NcAppHelper.getThreadInfoToString(Thread.currentThread());
        //String classInfoToString = NcAppHelper.getClassInfoToString(NcThExecPool.class);
        //Future<T> ncSubmit = null;
        //try{
            //Thread currentThread = Thread.currentThread();
            //currentThread.checkAccess();
            Class<? extends Callable> aClass = task.getClass();
            String strClassInfo = NcAppHelper.getClassInfoToString(aClass);
            //String strThreadInfo = NcAppHelper.getThreadInfoToString(currentThread);
            NcAppHelper.outMessage(
                //strThreadInfo
                //+ 
                        strClassInfo
                + NcStrLogMsgField.MSG_INFO.getStr()
                + NcStrLogMsgField.START.getStr());

                //ncSubmit = execSuper.submit(task);
                //if( ncSubmit == null ){
                    //throw new NullPointerException(classInfoToString);
                //}
            //this.callableQueue.put(UUID.randomUUID(), task);
            //this.futureQueue.put(UUID.randomUUID(), ncSubmit);
        /*} catch(Exception ex){
            String strMsg = NcStrLogMsgField.ERROR.getStr()
                + threadInfoToString + classInfoToString
                + NcStrLogMsgField.MSG_INFO.getStr()
                + " in submit exception ";
            NcAppHelper.logException(strMsg, ex);
        }*/
        return execSuper.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return execSuper.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return execSuper.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return execSuper.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return execSuper.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return execSuper.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return execSuper.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        execSuper.execute(command);
    }

}
