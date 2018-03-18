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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 * @param <E>
 */
public class NcThExecPool implements ExecutorService {
    private static final int CORE_POOL_SIZE = 4;
    private static final int MAXIMUM_POOL_SIZE = 4;
    private static final int QUEUE_LENGTH = 100;
    private final BlockingQueue<Runnable> workQueue;
    private final ExecutorService execSuper;
    
    public NcThExecPool() {
        Thread.currentThread().checkAccess();
        this.workQueue = new ArrayBlockingQueue<>(this.QUEUE_LENGTH);
        this.execSuper =
                new ThreadPoolExecutor(this.CORE_POOL_SIZE,
                        this.MAXIMUM_POOL_SIZE,
                        0L,
                        TimeUnit.MILLISECONDS,
                        this.workQueue);
    }
    
    public NcThExecPool(int countThreadPool) {
        Thread.currentThread().checkAccess();
        this.workQueue = new ArrayBlockingQueue<>(this.QUEUE_LENGTH);
        this.execSuper =
                new ThreadPoolExecutor(this.CORE_POOL_SIZE,
                        this.MAXIMUM_POOL_SIZE,
                        0L,
                        TimeUnit.MILLISECONDS,
                        this.workQueue);
    }

    @Override
    public void shutdown() {
        execSuper.shutdown();
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
        //SecurityManager securityManager = System.getSecurityManager();
        //ThreadGroup threadGroup = securityManager.getThreadGroup();
        //threadGroup.checkAccess();
        Class<? extends Callable> aClass = task.getClass();
        
        NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
        + NcStrLogMsgField.START.getStr()
        + NcStrLogMsgField.THREAD.getStr()
        + NcStrLogMsgField.ID.getStr()
        + String.valueOf(Thread.currentThread().getId())
        + NcStrLogMsgField.PRIORITY.getStr()        
        + String.valueOf(Thread.currentThread().getPriority())
        + NcStrLogMsgField.NAME.getStr()
        + Thread.currentThread().getName()
        + NcStrLogMsgField.CANONICALNAME.getStr()
        + aClass.getCanonicalName()
        + NcStrLogMsgField.GENERICSTRING.getStr()
        + aClass.toGenericString());
        
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
