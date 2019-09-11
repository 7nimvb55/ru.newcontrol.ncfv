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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdilLogicWrite {
    private String iterationStartTime;
    AdilLogicWrite(){
        iterationStartTime = AppFileOperationsSimple.getNowTimeStringWithMS();
    }
    /**
     * 
     * @param ruleAdil 
     */
    protected void doWriteLinesIntoLog(AdilRule ruleAdil){
        AdilState adilState = ruleAdil.getAdilState();
        ConcurrentSkipListMap<String, LinkedTransferQueue<String>> pollBusData = adilState.pollBusData();
        if( pollBusData != null ){
            Path storageLogIterationDir = AdilStorage.getStorageLogIterationDir();
            if( storageLogIterationDir != null ){
                for(Map.Entry<String, LinkedTransferQueue<String>> itemBusName : pollBusData.entrySet()){
                    if( !itemBusName.getValue().isEmpty() ){
                        String keyBusName = itemBusName.getKey();
                        Path getFileForWrite = null;
                        try {
                            getFileForWrite = Paths.get(storageLogIterationDir.toString(), keyBusName);
                        } catch(InvalidPathException exInvPath){

                        }
                        LinkedTransferQueue<String> valueBusLines = pollBusData.remove(keyBusName);
                        try(BufferedWriter bw = new BufferedWriter(new FileWriter(getFileForWrite.toString())))
                        {
                            for( String itemStr : valueBusLines ){
                                String text = itemStr.toString();
                                bw.write(text);
                                bw.newLine();
                            }
                            bw.flush();
                        }
                        catch(IOException ex){
                            ex.printStackTrace();
                        }
                    }
                }
            }
            
        }
    }
}
