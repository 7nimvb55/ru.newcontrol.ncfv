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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NewControl Thread Make Index Factory Exec Pool (NcThMifExecPool
 *
 * @author wladimirowichbiaran
 */
public class NcThMifExecPool extends ThreadPoolExecutor {

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 5;
    private static final int QUEUE_LENGTH = 100;
    private static final BlockingQueue<Runnable> ARRAY_WORK_QUEUE = new ArrayBlockingQueue<>(QUEUE_LENGTH);
    
    public NcThMifExecPool(){
        super(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                ARRAY_WORK_QUEUE);
    }
    public NcThMifExecPool(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue){
        super(corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue);
    }
    
    
}
