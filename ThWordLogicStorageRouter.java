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
 *
 * @author wladimirowichbiaran
 */
public class ThWordLogicStorageRouter {
    protected void doRouteFilteredDataForWordStorage(final ThWordRule ruleWordOuter){
        
    
        try{
            ThWordState wordState = ruleWordOuter.getWordState();
            
            //recode for chice busWithData By TypeOfWord
            
            ThWordBusWrited busJobForWrite = wordState.getBusJobForWordWrite();
            if( !busJobForWrite.isJobQueueEmpty() ){
                ThWordStateJobWriter jobForWrite = busJobForWrite.getJobForWrite();
                do {
                    if( !jobForWrite.isBlankObject() && !jobForWrite.isWritedDataEmpty() ){
                        processRouteLogic(jobForWrite);
                    }
                } while( !busJobForWrite.isJobQueueEmpty() );
            }
        }finally {
        
        }
    }
    private static void processRouteLogic(final ThWordStateJobWriter fromFilterJob){
        ThWordStateJobWriter currentJob;
        String writerPath;
        ConcurrentSkipListMap<UUID, TdataWord> writerData;
        try{
            currentJob = fromFilterJob;
            writerPath = currentJob.getFileNameForWrite();
            writerData = new ConcurrentSkipListMap<UUID, TdataWord>();                
            writerData.putAll(currentJob.getWriterData());
            
            //Path and Data ready for record
            
        }finally {
            currentJob = null;
            writerPath = null;
            writerData = null;
        }
    }
}
