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

import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordLogicFilter {
    protected void doFilterForIndexStorageWord(ThStorageWordRule outerRuleStorageWord){
        //bus from FileListBusToNext throw NullPointerException
        ThIndexRule indexRule = outerRuleStorageWord.getIndexRule();
        ThIndexState indexState = indexRule.getIndexState();
        ThFileListRule ruleFileList = indexState.getRuleFileList();
        ThFileListState fileListState = ruleFileList.getFileListState();
        ThFileListBusToNext busJobForFileListToNext = fileListState.getBusJobForFileListToNext();
        /**
         * funcReadedPath - 1506682974
         * funcNamePart - -589260798
         */
        do{
        while( !busJobForFileListToNext.isJobQueueEmpty() ){
            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, ?>> jobForWrite = busJobForFileListToNext.getJobForWrite();
            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> resortInputedStructure = resortInputedStructure(jobForWrite);
            
        }
        } while( ruleFileList.isRunnedFileListWorkBuild() );



    }
    private static ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> resortInputedStructure(
            final ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, ?>> inputedStructure){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> ouputStructure;

        try{
            ouputStructure = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>>();
            for(Entry<UUID, ConcurrentHashMap<Integer, ?>> itemFromStructure : inputedStructure.entrySet()){
                UUID keyInputedData = (UUID) itemFromStructure.getKey();
                ConcurrentHashMap<Integer, String> jobData = getJobData(itemFromStructure.getValue());
                ouputStructure.put(keyInputedData, jobData);
                System.out.println("transfered UUID: " + keyInputedData.toString());
            }
            return ouputStructure;
        } finally {
            ouputStructure = null;
        }
    }
    private static ConcurrentHashMap<Integer, String> getJobData(
            final ConcurrentHashMap<Integer, ?> inputedData){
        String funcReadedPath;
        String funcNamePart;
        ConcurrentHashMap<Integer, String> forDataOutput;
        try{
            String getReadedPath = (String) inputedData.get(1506682974);
            forDataOutput = new ConcurrentHashMap<Integer, String>();
            if( !getReadedPath.isEmpty() ){
                funcReadedPath = new String(getReadedPath.toCharArray());
                forDataOutput.put(1506682974, funcReadedPath);
            } else {
                funcReadedPath = new String("N/A");
            }
            
            String getNamePart = (String) inputedData.get(-589260798);
            if( !getNamePart.isEmpty() ){
                funcNamePart = new String(getNamePart.toCharArray());
                forDataOutput.put(-589260798, funcNamePart);
            } else {
                funcNamePart = new String("N/A");
            }
            System.out.println("transfered funcReadedPath:  " + funcReadedPath
                    + " funcNamePart: " + funcNamePart);
            return forDataOutput;
        } finally {
            funcReadedPath = null;
            funcNamePart = null;
            forDataOutput = null;
        }
        
    }
}
