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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <p> @todo when add data into bus, need save sended hash of objects and keys 
 * in the *statistic or some *sendedDataControls sub object, not in bus object,
 * when read data from bus, need calculate readed hash of objects, keys in
 * *readedDataControls and build reports about calc sended, readed hashes
 * 
 * @author wladimirowichbiaran
 */
public class ThFileListBusToNext {
    private ConcurrentSkipListMap<UUID, ConcurrentHashMap<Integer, ?>> forWriteQueue;
    /**
     * @todo when add data into bus, need save sended hash of objects and keys 
     * in the *statistic or some *sendedDataControls sub object, not in bus object,
     * when read data from bus, need calculate readed hash of objects, keys in
     * *readedDataControls and build reports about calc sended, readed hashes
     */
    protected ThFileListBusToNext(){
        this.forWriteQueue = new ConcurrentSkipListMap<UUID, ConcurrentHashMap<Integer, ?>>();
        
    }
    /**
     * Bus for data transfer format example:
     * ThFileListLogicBuild get data from ThDirListStateJobReader in special
     * class (1) - TdataDirListFsObjAttr, for next technology stadies of make 
     * index, get for need fields of data and save it in next structure:
     * 
     * ConcurrentSkipListMap<UUID, ConcurrentHashMap<Integer, ?>>
     *                      <recordDirList, <fieldsCodes, fieldsValue>>
     * 
     * this function used 
     * {@link java.util.concurrent.ConcurrentSkipListMap#put(java.lang.Object, java.lang.Object) }
     * 
     * @param keyDirListRecord
     * @param dataForFilter 
     * @throws NullPointerException when add null in key, data object
     * @see 
     * java.util.concurrent.ConcurrentSkipListMap#put(java.lang.Object, java.lang.Object)
     * 
     */
    
    protected void addWriterJob(final UUID keyDirListRecord, final ConcurrentHashMap<Integer, ?> dataForFilter){
        ConcurrentHashMap<Integer, ?> dataForAdd;
        UUID keyDirListForTransfer;
        try{
            keyDirListForTransfer = keyDirListRecord;
            if( keyDirListForTransfer == null ){
                throw new NullPointerException(ThFileListBusToNext.class.getCanonicalName()
                    + " add to bus null key value");
            }
            dataForAdd = dataForFilter;
            if( dataForAdd == null ){
                throw new NullPointerException(ThFileListBusToNext.class.getCanonicalName()
                    + " add to bus null object value");
            }
            if( !dataForAdd.isEmpty() ){
                this.forWriteQueue.put(keyDirListForTransfer, dataForAdd);
            }
        } finally {
            dataForAdd = null;
            keyDirListForTransfer = null;
        }
    }
    /**
     * job data for index make process
     * @return 
     * @throws NullPointerException when return null or empty object
     * @see ThFileListBusToNext 
     * @todo
     */
    protected ConcurrentHashMap <UUID, ConcurrentHashMap<Integer, ?>> getJobForWrite(){
        ConcurrentHashMap <UUID, ConcurrentHashMap<Integer, ?>> dataJobReturn;
        try{
            Map.Entry<UUID, ConcurrentHashMap<Integer, ?>> pollFirstEntry = this.forWriteQueue.pollFirstEntry();
            if( pollFirstEntry != null ){
                if( !pollFirstEntry.getValue().isEmpty() ){
                    dataJobReturn = new ConcurrentHashMap <UUID, ConcurrentHashMap<Integer, ?>>();
                    dataJobReturn.put(pollFirstEntry.getKey(), pollFirstEntry.getValue());
                    return dataJobReturn;
                }
                throw new NullPointerException(ThFileListBusToNext.class.getCanonicalName()
                    + " in bus empty object");
            }
            throw new NullPointerException(ThFileListBusToNext.class.getCanonicalName()
                    + " in bus null object");
        } finally {
            dataJobReturn = null;
        }
    }
    protected Boolean isJobQueueEmpty(){
        if( this.forWriteQueue.isEmpty() ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected Integer getQueueSize(){
        return (int) this.forWriteQueue.size();
    }
    protected void cleanQueue(){
        this.forWriteQueue = null;
        this.forWriteQueue = new ConcurrentSkipListMap<UUID, ConcurrentHashMap<Integer, ?>>();
    }
    /**
     * Read in bus job UUIDs where set flag jobDone, write it in quue structure,
     * after that delete from job bus UUID from wroted queue
     */
    protected void shrinkEmptyItems(){
        
        ThreadLocal<ArrayBlockingQueue<UUID>> thListJobDone = new ThreadLocal<ArrayBlockingQueue<UUID>>();
        try{
            ArrayBlockingQueue<UUID> listOfDoneJob = new ArrayBlockingQueue<UUID>((int) this.forWriteQueue.size());
            thListJobDone.set(listOfDoneJob);
            Boolean notHaveDoneJob = Boolean.FALSE;
            for( Map.Entry<UUID, ConcurrentHashMap<Integer, ?>> itemJob : this.forWriteQueue.entrySet() ){
                if( itemJob.getValue().isEmpty() ){
                    thListJobDone.get().add(itemJob.getKey());
                }
            }
            try{
                if( !thListJobDone.get().isEmpty() ){
                    do{
                        UUID poll = thListJobDone.get().poll();
                        if( poll != null){
                            this.forWriteQueue.remove(poll);
                            UUID ceilingKey = this.forWriteQueue.ceilingKey(poll);
                            if( ceilingKey == null ){
                                thListJobDone.get().add(poll);
                            }
                        }

                    }while( !thListJobDone.get().isEmpty() );
                }
            } catch ( ClassCastException ex ){
                ex.printStackTrace();
            } catch ( NullPointerException ex ){
                ex.printStackTrace();
            }
        } finally {
            thListJobDone.remove();
        }
    }
}
