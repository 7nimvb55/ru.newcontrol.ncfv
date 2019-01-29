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
        //ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, TdataWord>> doWordForIndex = 
        //        ThWordLogicFilter.doWordForIndex(recordId, storagePath, inputedPath);
        //doWordForIndex.get(AppConstants.INDEX_DATA_TRANSFER_CODE_WORD);
        //doWordForIndex.get(AppConstants.INDEX_DATA_TRANSFER_CODE_LONG_WORD);
        while( !busJobForRead.isJobQueueEmpty() ){
            ThDirListStateJobReader jobForRead = busJobForRead.getJobForRead();
            if( !jobForRead.isBlankObject() ){
            if( !jobForRead.isReadedDataEmpty() ){
            if( jobForRead.isReaderJobDone() ){
                Path readedPath = jobForRead.getReadedPath();
                ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readedData = jobForRead.getReadedData();
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
                        }
                        for(Map.Entry<UUID, TdataWord> itemLongWord : getLongWord.entrySet()){
                            System.out.println("src: " + namePart.toString()
                            + " key: " + itemLongWord.getKey().toString()
                            + " str: " +  itemLongWord.getValue().strSubString
                            + " hex: " +  itemLongWord.getValue().hexSubString);
                        }
                        
                    }
                    
                    
                }
            }
            }
            }
        }
        
    }
    
}
