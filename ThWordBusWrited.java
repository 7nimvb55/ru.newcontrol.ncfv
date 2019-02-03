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
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordBusWrited {
    private ConcurrentSkipListMap<UUID, ThWordStateJobWriter> forWriteQueue;
    
    protected ThWordBusWrited(){
        this.forWriteQueue = new ConcurrentSkipListMap<UUID, ThWordStateJobWriter>();
        
    }
    protected void addWriterJob(final ThWordStateJobWriter jobForDone){
        if( !jobForDone.isBlankObject() ){
            this.forWriteQueue.put(jobForDone.getID(), jobForDone);
        }
    }
    protected ThWordStateJobWriter getJobForWrite(){
        Map.Entry<UUID, ThWordStateJobWriter> pollFirstEntry = this.forWriteQueue.pollFirstEntry();
        if( pollFirstEntry != null ){
                return pollFirstEntry.getValue();
        }
        return new ThWordStateJobWriter();
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
        this.forWriteQueue = new ConcurrentSkipListMap<UUID, ThWordStateJobWriter>();
    }
    /**
     * Read in bus job UUIDs where set flag jobDone, write it in quue structure,
     * after that delete from job bus UUID from wroted queue
     */
    protected void shrinkJobDoneItems(){
        
        ThreadLocal<ArrayBlockingQueue<UUID>> thListJobDone = new ThreadLocal<ArrayBlockingQueue<UUID>>();
        try{
            ArrayBlockingQueue<UUID> listOfDoneJob = new ArrayBlockingQueue<UUID>((int) this.forWriteQueue.size());
            thListJobDone.set(listOfDoneJob);
            Boolean notHaveDoneJob = Boolean.FALSE;
            for( Map.Entry<UUID, ThWordStateJobWriter> itemJob : this.forWriteQueue.entrySet() ){
                if( itemJob.getValue().isWriterJobDone() ){
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
