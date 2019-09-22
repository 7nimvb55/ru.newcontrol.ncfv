/*
 * Copyright 2019 wladimirowichbiaran.
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

import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * get typedcode name from static functions
 * - create runners
 * - create and start threads
 * - utilize threads after use
 * @author wladimirowichbiaran
 */
public class AdibThread {
    private final Long timeCreation;
    private final UUID objectLabel;
    private final ConcurrentSkipListMap<Integer, Thread> workerListCreated;
    private final ConcurrentSkipListMap<Integer, Thread> workerListRunned;
    private final ConcurrentSkipListMap<Integer, Thread> workerListFinished;
    private final ConcurrentSkipListMap<Integer, Runnable> runnerTypedList;
    AdibThread(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.workerListCreated = new ConcurrentSkipListMap<Integer, Thread>();
        this.workerListRunned = new ConcurrentSkipListMap<Integer, Thread>();
        this.workerListFinished = new ConcurrentSkipListMap<Integer, Thread>();
        this.runnerTypedList = new ConcurrentSkipListMap<Integer, Runnable>();
    }
    /**
     * create new thread object for typed runner and add it into workerListCreated
     */
    private void createWorker(){
        try {
            
        } finally {
            
        }
    }
    /**
     * poll thread object from workerListCreated, add it into workerListRunned
     */
    private void startNextWorker(){
        
    }
    /**
     * poll thread object from workerListRunned,  add it into workerListFinished
     */
    private void markWorkerFinished(){
        
    }
}
