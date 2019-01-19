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
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThDirListBusReaded {
    private ConcurrentSkipListMap<UUID, ThDirListStateJobReader> forReadQueue;
    
    protected ThDirListBusReaded(){
        this.forReadQueue = new ConcurrentSkipListMap<UUID, ThDirListStateJobReader>();
        
    }
    protected void addReaderJob(final ThDirListStateJobReader jobForDone){
        if( !jobForDone.isBlankObject() ){
            this.forReadQueue.put(jobForDone.getID(), jobForDone);
        }
    }
    protected ThDirListStateJobReader getJobForRead(){
        Map.Entry<UUID, ThDirListStateJobReader> pollFirstEntry = this.forReadQueue.pollFirstEntry();
        if( pollFirstEntry != null ){
                return pollFirstEntry.getValue();
        }
        return new ThDirListStateJobReader();
    }
    protected Boolean isJobQueueEmpty(){
        if( this.forReadQueue.isEmpty() ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected Integer getQueueSize(){
        return (int) this.forReadQueue.size();
    }
    protected void cleanQueue(){
        this.forReadQueue = new ConcurrentSkipListMap<UUID, ThDirListStateJobReader>();
    }
    
}
