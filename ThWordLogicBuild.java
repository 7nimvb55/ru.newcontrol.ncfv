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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Release logic for data from DirList to word index format
 * @author wladimirowichbiaran
 */
public class ThWordLogicBuild {
    protected void doWordIndex(final ThWordRule ruleWordOuter){
        
        //ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> doWordForIndex = 
        //        ThWordLogicFilter.doWordForIndex(recordId, storagePath, inputedPath);
        //doWordForIndex.get(AppConstants.INDEX_DATA_TRANSFER_CODE_WORD);
        //doWordForIndex.get(AppConstants.INDEX_DATA_TRANSFER_CODE_LONG_WORD);
        
        ThDirListBusReaded busJobForRead = ruleWordOuter.getIndexRule().getIndexState().getBusJobForRead();
        //for send data into word writer
        ThWordBusWrited busJobForWrite = ruleWordOuter.getWordState().getBusJobForWordWrite();
        System.out.println(ThWordLogicBuild.class.getCanonicalName() 
                + " do it ++busJobForRead"
                        + busJobForRead.toString() + "++"
                        + busJobForRead.getQueueSize() + "++++++busJobForWrite++"
                        //+ busJobForWrite.toString() + "++"
                        //+ busJobForWrite.getQueueSize() 
                + "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        while( !busJobForRead.isJobQueueEmpty() ){
            ThDirListStateJobReader jobForRead = busJobForRead.getJobForRead();
            if( !jobForRead.isBlankObject() ){
                if( !jobForRead.isReadedDataEmpty() ){
                    if( jobForRead.isReaderJobDone() ){
                        Path readedPath = jobForRead.getReadedPath();
                        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readedData = jobForRead.getReadedData();
                        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> packetDataForWord = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> packetDataForWordLong = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                        int iterations = 0;
                        System.out.println(ThWordLogicBuild.class.getCanonicalName() + " read from job size " + readedData.size());
                        
                        for( Map.Entry<UUID, TdataDirListFsObjAttr> recordItem : readedData.entrySet() ){
                            String shortDataToString = recordItem.getValue().file;
                            Path dirListReaded = Paths.get(shortDataToString);
                            for (int i = 0; i < dirListReaded.getNameCount(); i++) {
                                /**
                                 * @todo release index by depth dirictories, file extentions, data time map
                                 * create job for bus index jobs workers
                                 * do for word index in runnables workers,
                                 * sort results to packets by queue system
                                 * 
                                 * for dirlist reader need release jobforneed read data
                                 * before end of release current packet
                                 */
                                Path namePart = dirListReaded.getName(i);
                                //outOfMemory exception
                                ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> doWordForIndex = 
                                    ThWordLogicFilter.doWordForIndex(recordItem.getKey(), readedPath.toString(), namePart.toString());
                                ConcurrentSkipListMap<UUID, TdataWord> getWord = doWordForIndex.get(AppConstants.INDEX_DATA_TRANSFER_CODE_WORD);
                                ConcurrentSkipListMap<UUID, TdataWord> getLongWord = doWordForIndex.get(AppConstants.INDEX_DATA_TRANSFER_CODE_LONG_WORD);
                                for(Map.Entry<UUID, TdataWord> itemWord : getWord.entrySet()){
                                    System.out.println("src: " + namePart.toString()
                                    + " key: " + itemWord.getKey().toString()
                                    + " str: " +  itemWord.getValue().strSubString
                                    + " hex: " +  itemWord.getValue().hexSubString);
                                    ConcurrentSkipListMap<UUID, TdataWord> tmpForPut = new ConcurrentSkipListMap<UUID, TdataWord>();
                                    tmpForPut.put(itemWord.getKey(), itemWord.getValue());
                                    ConcurrentSkipListMap<UUID, TdataWord> getPrevVal = packetDataForWord.remove(itemWord.getValue().hexSubString);
                                    if( getPrevVal != null){
                                        getPrevVal.putAll(tmpForPut);
                                        packetDataForWord.put(itemWord.getValue().hexSubString, getPrevVal);
                                    } else {
                                        packetDataForWord.put(itemWord.getValue().hexSubString, tmpForPut);
                                    }
                                    
                                    
                                    //ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemWord.getValue().hexSubString);
                                    //thWordStateJobWriter.putWritedData(getWord);
                                    
                                    //busJobForWrite.addWriterJob(thWordStateJobWriter);
                                }
                                for(Map.Entry<UUID, TdataWord> itemLongWord : getLongWord.entrySet()){
                                    System.out.println("src: " + namePart.toString()
                                    + " key: " + itemLongWord.getKey().toString()
                                    + " str: " +  itemLongWord.getValue().strSubString
                                    + " hex: " +  itemLongWord.getValue().hexSubString);
                                    ConcurrentSkipListMap<UUID, TdataWord> tmpForPutLong = new ConcurrentSkipListMap<UUID, TdataWord>();
                                    tmpForPutLong.put(itemLongWord.getKey(), itemLongWord.getValue());
                                    ConcurrentSkipListMap<UUID, TdataWord> getPrevValLong = packetDataForWordLong.remove(itemLongWord.getValue().hexSubString);
                                    if( getPrevValLong != null){
                                        getPrevValLong.putAll(tmpForPutLong);
                                        packetDataForWordLong.put(itemLongWord.getValue().hexSubString, getPrevValLong);
                                    } else {
                                        packetDataForWordLong.put(itemLongWord.getValue().hexSubString, tmpForPutLong);
                                    }
                                }

                            }
                            //if( iterations > 1000){
                                for(Map.Entry<String, ConcurrentSkipListMap<UUID, TdataWord>> itemWord : packetDataForWord.entrySet()){
                                    ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemWord.getKey());
                                    thWordStateJobWriter.putWritedData(itemWord.getValue());
                                    
                                    busJobForWrite.addWriterJob(thWordStateJobWriter);
                                }
                                for(Map.Entry<String, ConcurrentSkipListMap<UUID, TdataWord>> itemLongWord : packetDataForWordLong.entrySet()){
                                    ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemLongWord.getKey(), Boolean.TRUE);
                                    thWordStateJobWriter.putWritedData(itemLongWord.getValue());
                                    
                                    busJobForWrite.addWriterJob(thWordStateJobWriter);
                                }
                                iterations = 0;
                                packetDataForWord = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                                packetDataForWordLong = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                            //}
                            iterations++;
                            /*if( busJobForRead.isJobQueueEmpty() ){
                                for(Map.Entry<String, ConcurrentSkipListMap<UUID, TdataWord>> itemWord : packetDataForWord.entrySet()){
                                    ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemWord.getKey());
                                    thWordStateJobWriter.putWritedData(itemWord.getValue());
                                    
                                    busJobForWrite.addWriterJob(thWordStateJobWriter);
                                }
                                for(Map.Entry<String, ConcurrentSkipListMap<UUID, TdataWord>> itemLongWord : packetDataForWordLong.entrySet()){
                                    ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemLongWord.getKey(), Boolean.TRUE);
                                    thWordStateJobWriter.putWritedData(itemLongWord.getValue());
                                    
                                    busJobForWrite.addWriterJob(thWordStateJobWriter);
                                }
                                packetDataForWord = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                                packetDataForWordLong = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                            }
                            if( busJobForRead.isJobQueueEmpty() ){
                                for(Map.Entry<String, ConcurrentSkipListMap<UUID, TdataWord>> itemWord : packetDataForWord.entrySet()){
                                    ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemWord.getKey());
                                    thWordStateJobWriter.putWritedData(itemWord.getValue());
                                    
                                    busJobForWrite.addWriterJob(thWordStateJobWriter);
                                }
                                for(Map.Entry<String, ConcurrentSkipListMap<UUID, TdataWord>> itemLongWord : packetDataForWordLong.entrySet()){
                                    ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemLongWord.getKey(), Boolean.TRUE);
                                    thWordStateJobWriter.putWritedData(itemLongWord.getValue());
                                    
                                    busJobForWrite.addWriterJob(thWordStateJobWriter);
                                }
                                packetDataForWord = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                                packetDataForWordLong = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                            }*/
                        }
                        jobForRead.cleanReadedData();
                        jobForRead.setTrueReaderJobDone();
                    }
                }
            }
            
            
        }
    }
}
