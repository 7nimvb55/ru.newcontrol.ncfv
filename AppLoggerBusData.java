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

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerBusData {
    private ConcurrentSkipListMap<UUID, ArrayBlockingQueue<String>> writerList;
    private ConcurrentSkipListMap<UUID, ArrayBlockingQueue<String>> readerList;
    
    private UUID lastKey;
    AppLoggerBusData(){
        this.writerList = new ConcurrentSkipListMap<UUID, ArrayBlockingQueue<String>>();
        this.readerList = new ConcurrentSkipListMap<UUID, ArrayBlockingQueue<String>>();
    }
    protected ArrayBlockingQueue<String> getByKey(String keyForGet){
        return writerList.get(keyForGet);
    }
    protected UUID addArrayAndGetKey(ArrayList<String> elementForAdd){
        ArrayBlockingQueue<String> forRecord = new ArrayBlockingQueue<String>(elementForAdd.size());
        for(String elToReturn : elementForAdd){
            forRecord.add(new String(elToReturn));
        }
        UUID strKey = UUID.randomUUID();
        this.writerList.put(strKey, forRecord);
        this.lastKey = strKey;
        return strKey;
    }
    //@todo create writer job and add to list by job uuid
    protected void addWriterJobDataKey(UUID strKey, ArrayBlockingQueue<String> elementForAdd){
        ReentrantLock forAddWriterJobDatalck = new ReentrantLock();
        forAddWriterJobDatalck.lock();
        try{
            this.writerList.put(strKey, elementForAdd);
            this.lastKey = strKey;
        } finally {
            forAddWriterJobDatalck.unlock();
        }
    }
    //@todo release from reader job data add to readerlist bus
    protected void addReaderJobDataKey(UUID strKey, ArrayBlockingQueue<String> elementForAdd){
        ReentrantLock forAddReaderJobDatalck = new ReentrantLock();
        forAddReaderJobDatalck.lock();
        try{
            this.readerList.put(strKey, elementForAdd);
            this.lastKey = strKey;
        } finally {
            forAddReaderJobDatalck.unlock();
        }
    }
    protected UUID addAndGetKey(ArrayBlockingQueue<String> elementForAdd){
        UUID strKey = UUID.randomUUID();
        this.writerList.put(strKey, elementForAdd);
        this.lastKey = strKey;
        return strKey;
    }
    protected UUID newAndGetKey(Integer queueSize){
        UUID strKey = UUID.randomUUID();
        ArrayBlockingQueue<String> queueElements = new ArrayBlockingQueue<String>(queueSize);
        this.writerList.put(strKey, queueElements);
        this.lastKey = strKey;
        return strKey;
    }
    protected UUID getLastKey(){
        return this.lastKey;
    }
    protected ConcurrentSkipListMap<UUID, ArrayBlockingQueue<String>> getReaderBus(){
        return this.readerList;
    }
    protected ConcurrentSkipListMap<UUID, ArrayBlockingQueue<String>> getWritedBus(){
        return this.writerList;
    }
}
