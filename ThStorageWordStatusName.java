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
import java.util.concurrent.ConcurrentHashMap;

/**
 * ThStorageWordStatusName
 * namesFS    - (3a.2) - String currentFileName - full file name where read 
 *                from data
 *     - (3a.2) - String newFileName - full file name for Files.move 
 *                operation after write created when readJobDataSize
 *      - (3a.2) - String storageDirectoryName - full directory name
 *                in storage for data files save
 * @author wladimirowichbiaran
 */
public class ThStorageWordStatusName {

    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> poolStatusName;
    
    ThStorageWordStatusName(){
        this.poolStatusName = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    protected ConcurrentHashMap<Integer, String> getStatusNameForKeyPointFlow(final UUID keyPointFlowName){
        UUID inputedVal;
        ConcurrentHashMap<Integer, String> getStatusNameFormPool;
        try{
            inputedVal = (UUID) keyPointFlowName;
            getStatusNameFormPool = this.poolStatusName.get(inputedVal);
            if( getStatusNameFormPool == null ){
                throw new IllegalStateException(ThStorageWordStatusName.class.getCanonicalName()
                + " not exist record in list for "
                + inputedVal.toString() + " key point flow");
            }
            /**
             * @todo
             * update access time for point flow if in one point of all flow get
             * increment 
             */
            return getStatusNameFormPool;
        } finally {
            inputedVal = null;
            getStatusNameFormPool = null;
        }
    }

    /**
     * not exist bus
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected Boolean isStatusNameNotExist(final UUID keyPointFlowName){
        UUID inputedVal;
        try{
            inputedVal = (UUID) keyPointFlowName;
            if( !this.poolStatusName.containsKey(inputedVal) ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            inputedVal = null;
        }
    }
    /**
     * 
     * @param srcFileName
     * @param destFileName
     * @return lvl(4, 3a.2) ready for put in list lvl(3)
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> createStructureParamsNamesFs(
                        final UUID keyPointFlowName,
                        final String directoryName,
                        final String srcFileName,
                        final String destFileName){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> returnedParams;
        ConcurrentHashMap<Integer, String> namesFS;
        UUID keyPointFlowNamesFs;
        try{
            keyPointFlowNamesFs = keyPointFlowName;
            namesFS = setInParamNamesFS(
                        directoryName,
                        srcFileName,
                        destFileName);
            returnedParams = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>>();
            this.poolStatusName.put(keyPointFlowNamesFs, namesFS);
            returnedParams.put(keyPointFlowNamesFs , namesFS);
            return returnedParams;
        } finally {
            returnedParams = null;
            namesFS = null;
            keyPointFlowNamesFs = null;
        }
    }
    /**
     * 
     * @param srcFileName
     * @param destFileName
     * @return lvl(3a.2)
     */
    protected ConcurrentHashMap<Integer, String> setInParamNamesFS(
                        final String directoryName,
                        final String srcFileName,
                        final String destFileName){
        ConcurrentHashMap<Integer, String> returnedHashMap;
        String directoryFuncName;
        String srcFuncFileName;
        String destFuncFileName;
        try {
            directoryFuncName = (String) directoryName;
            srcFuncFileName = (String) srcFileName;
            destFuncFileName = (String) destFileName;
            if( directoryFuncName.isEmpty() ){
                directoryFuncName = "undefinedSrcName-0-0";
            }
            if( srcFuncFileName.isEmpty() ){
                srcFuncFileName = "undefinedSrcName-0-0"; // getDafaultNames with current Size and Volume Number
            }
            if( destFuncFileName.isEmpty() ){
                destFuncFileName = "undefinedDestName-0-0";
            }
            returnedHashMap = new ConcurrentHashMap<Integer, String>();
            //storageDirectoryName - 1962941405
            returnedHashMap.put(1962941405, directoryFuncName);
            //currentFileName - 1517772480
            returnedHashMap.put(1517772480, srcFuncFileName);
            //newFileName - 521024487
            returnedHashMap.put(521024487, destFuncFileName);
            
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            directoryFuncName = null;
            srcFuncFileName = null;
            destFuncFileName = null;
        }
    }
    
}
