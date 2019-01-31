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
 *
 * @author wladimirowichbiaran
 */
public class ThIndexWord extends Thread{
    private ThIndexRule ruleThIndex;
    ThIndexWord(ThIndexRule outerRule){
        super(UUID.randomUUID().toString());
        this.ruleThIndex = outerRule;
        //Thread.currentThread().setName(UUID.randomUUID().toString());
    }
    
    @Override
    public void run(){
        System.out.println(ThIndexWord.class.getCanonicalName() 
                + " do it +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        ThDirListBusReaded busJobForRead = this.ruleThIndex.getIndexState().getBusJobForRead();
        System.out.println(busJobForRead.getQueueSize().toString()
                + " do it +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        ThWordBusWrited thWordBusWordWrited = new ThWordBusWrited();
        ThWordBusWrited thWordBusLongWordWrited = new ThWordBusWrited();
        
        ThWordState thWordState = new ThWordState();
        
        thWordState.setBusJobForWordWrite(thWordBusWordWrited);
        thWordState.setBusJobForLongWordWrite(thWordBusLongWordWrited);
        
        ThWordRule thWordRule = new ThWordRule(this.ruleThIndex);
        
        ThWordWorkBuild thWordWorkBuild = new ThWordWorkBuild(thWordRule);
        ThWordWorkWrite thWordWorkWrite = new ThWordWorkWrite(thWordRule);
        ThLongWordWorkWrite thLongWordWorkWrite = new ThLongWordWorkWrite(thWordRule);
        
        thWordRule.setWordState(thWordState);
        thWordRule.setWordWorkBuild(thWordWorkBuild);
        thWordRule.setWordWorkWriter(thWordWorkWrite);
        thWordRule.setLongWordWorkWriter(thLongWordWorkWrite);
        thWordRule.runBuildWordWorkers();
        //uncomment
        //processWordIndex(thWordRule);
        
        thWordRule.runWriteToWord();
        thWordRule.runWriteToLongWord();
    }
    private void processWordIndex(ThWordRule ruleWordOuter){
        ThDirListBusReaded busJobForRead = this.ruleThIndex.getIndexState().getBusJobForRead();
        //for send data into word writer
        ThWordBusWrited busJobForWordWrite = ruleWordOuter.getWordState().getBusJobForWordWrite();
        ThWordBusWrited busJobForLongWordWrite = ruleWordOuter.getWordState().getBusJobForLongWordWrite();
        //LongWord
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
                //if( !jobForRead.isReadedDataEmpty() ){
                    //if( jobForRead.isReaderJobDone() ){
                        Path readedPath = jobForRead.getReadedPath();
                        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readedData = jobForRead.getReadedData();
                        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> packetDataForWord = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> packetDataForWordLong = 
                                new ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>>();
                        int iterations = 0;
                        System.out.println(" read from job size " + readedData.size());
                        for( Map.Entry<UUID, TdataDirListFsObjAttr> recordItem : readedData.entrySet() ){
                            String shortDataToString = recordItem.getValue().file;
                            Path dirListReaded = Paths.get(shortDataToString);
                            for (int i = 0; i < dirListReaded.getNameCount(); i++) {
                                Path namePart = dirListReaded.getName(i);
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
                            
                            for(Map.Entry<String, ConcurrentSkipListMap<UUID, TdataWord>> itemWord : packetDataForWord.entrySet()){
                                ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemWord.getKey());
                                thWordStateJobWriter.putWritedData(itemWord.getValue());

                                busJobForWordWrite.addWriterJob(thWordStateJobWriter);
                            }
                            for(Map.Entry<String, ConcurrentSkipListMap<UUID, TdataWord>> itemLongWord : packetDataForWordLong.entrySet()){
                                ThWordStateJobWriter thWordStateJobWriter = new ThWordStateJobWriter(itemLongWord.getKey(), Boolean.TRUE);
                                thWordStateJobWriter.putWritedData(itemLongWord.getValue());

                                busJobForLongWordWrite.addWriterJob(thWordStateJobWriter);
                            }
                            iterations = 0;
                            
                            iterations++;
                        }
                    //}
                //}
            }
        }
    }
    
}
