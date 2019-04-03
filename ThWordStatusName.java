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
import java.util.concurrent.ConcurrentHashMap;

/**
 * ThStorageWordStatusName
 * namesFS    - (3a.2) - String currentFileName - full file name where read 
 *                from data
 *                  when flow read in storage data this names set equal newFileName
 *                  after move process, in StatusWorkers set flag isNeedRead
 *     - (3a.2) - String newFileName - full file name for Files.move 
 *                operation after write created when readJobDataSize
 *     - (3a.2) - String storageDirectoryName - full directory name
 *                in storage for data files save
 *     - (3a.2) - String deletedFileName name of file data from prev iteration of
 *                  write, read flow
 *     - (3a.2) - String flowFileNamePrefix name prefix for add in the writer before
 *                  write to storage and after read from DataCahe, and calculate
 *                  readed from cache data size, if need readed data limited by
 *                  vol size for index storages system, in the flow system add last
 *                  vol file name if it not limited
 * 
 * @author wladimirowichbiaran
 */
public class ThWordStatusName {
    private final Long timeCreation;
    private final UUID objectLabel;
    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> poolStatusName;
    
    ThWordStatusName(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
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
            getStatusNameFormPool = (ConcurrentHashMap<Integer, String>) this.poolStatusName.get(inputedVal);
            if( getStatusNameFormPool == null ){
                throw new IllegalStateException(ThWordStatusName.class.getCanonicalName()
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
     * 
     * @param keyPointFlowName
     * @return true if found and delete data
     */
    protected Boolean removeStatusNameForKeyPointFlow(final UUID keyPointFlowName){
        UUID inputedVal;
        ConcurrentHashMap<Integer, String> getRemovedStatusNameFormPool;
        try{
            inputedVal = (UUID) keyPointFlowName;
            getRemovedStatusNameFormPool = (ConcurrentHashMap<Integer, String>) this.poolStatusName.remove(inputedVal);
            if( getRemovedStatusNameFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, String> itemOfPoint : getRemovedStatusNameFormPool.entrySet() ){
                String remove = getRemovedStatusNameFormPool.remove(itemOfPoint.getKey());
                String [] remStrVal = {remove};
                remStrVal = null;
                Integer [] remIntKey = {itemOfPoint.getKey()};
                remIntKey = null;
            }
            getRemovedStatusNameFormPool = null;
            return Boolean.TRUE;
        } finally {
            inputedVal = null;
            getRemovedStatusNameFormPool = null;
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
     * @param keyPointFlowName
     * @param directoryName
     * @param srcFileName
     * @param destFileName
     * @param deletedFileName
     * @param flowFileNamePrefix
     * @return lvl(4, 3a.2) ready for put in list lvl(3)
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> createStructureParamsNamesFs(
                        final UUID keyPointFlowName,
                        final String directoryName,
                        final String srcFileName,
                        final String destFileName,
                        final String deletedFileName,
                        final String flowFileNamePrefix){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, String>> returnedParams;
        ConcurrentHashMap<Integer, String> namesFS;
        UUID keyPointFlowNamesFs;
        try{
            keyPointFlowNamesFs = keyPointFlowName;
            namesFS = setInParamNamesFS(
                        directoryName,
                        srcFileName,
                        destFileName,
                        deletedFileName,
                        flowFileNamePrefix);
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
     * @param directoryName
     * @param srcFileName
     * @param destFileName
     * @param deletedFileName
     * @param flowFileNamePrefix
     * @return lvl(3a.2)
     */
    protected ConcurrentHashMap<Integer, String> setInParamNamesFS(
                        final String directoryName,
                        final String srcFileName,
                        final String destFileName,
                        final String deletedFileName,
                        final String flowFileNamePrefix
                        ){
        ConcurrentHashMap<Integer, String> returnedHashMap;
        String directoryFuncName;
        String srcFuncFileName;
        String destFuncFileName;
        String deletedFuncFileName;
        String flowFuncFileNamePrefix;
        try {
            directoryFuncName = (String) directoryName;
            srcFuncFileName = (String) srcFileName;
            destFuncFileName = (String) destFileName;
            deletedFuncFileName = (String) deletedFileName;
            flowFuncFileNamePrefix = (String) flowFileNamePrefix;
            if( directoryFuncName.isEmpty() ){
                directoryFuncName = "undefinedSrcName-0-0";
            }
            if( srcFuncFileName.isEmpty() ){
                srcFuncFileName = "undefinedSrcName-0-0"; // getDafaultNames with current Size and Volume Number
            }
            if( destFuncFileName.isEmpty() ){
                destFuncFileName = "undefinedDestName-0-0";
            }
            if( deletedFuncFileName.isEmpty() ){
                deletedFuncFileName = "undefDelName-0-0";
            }
            if( flowFuncFileNamePrefix.isEmpty() ){
                flowFuncFileNamePrefix = "undefFlowPrefName-0-0";
            }
            returnedHashMap = new ConcurrentHashMap<Integer, String>();
            //storageDirectoryName - 1962941405
            returnedHashMap.put(1962941405, directoryFuncName);
            //currentFileName - 1517772480
            returnedHashMap.put(1517772480, srcFuncFileName);
            //newFileName - 521024487
            returnedHashMap.put(521024487, destFuncFileName);
            //deletedFileName - 2045325664
            returnedHashMap.put(2045325664, deletedFuncFileName);
            //flowFileNamePrefix - -980152217
            returnedHashMap.put(-980152217, flowFuncFileNamePrefix);
            
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            directoryFuncName = null;
            srcFuncFileName = null;
            destFuncFileName = null;
            deletedFuncFileName = null;
            flowFuncFileNamePrefix = null;
        }
    }
    /**
     * <ul>
     * <li>0 - storageDirectoryName
     * <li>1 - currentFileName
     * <li>2 - newFileName
     * <li>3 - deletedFileName
     * <li>4 - flowFileNamePrefix
     * </ul>
     * @return 
     */
    private String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "storageDirectoryName",
                "currentFileName",
                "newFileName",
                "deletedFileName",
                "flowFileNamePrefix"
            };
            return namesForReturn;
        } finally {
            namesForReturn = null;
        }
    }
    /**
     * Return code of parameter by his number, calculeted from some fileds
     * @param numParam
     * @return hashCode for Parameter by his number
     * @see getParamNames()
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    protected Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = getParamNames();
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + "count parameters: " 
                                + paramNames.length 
                                + ", need for return " + numParam);
            } 
            int codeForParameter = paramNames[numParam]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return codeForParameter;
        } finally {
            paramNames = null;
        }
    }
    /**
     * Count records (array.length) returned from {@link #getParamNames }
     * @return 
     */
    protected int getParamCount(){
        String[] paramNames;
        try {
            paramNames = getParamNames();
            return paramNames.length;
        } finally {
            paramNames = null;
        }
    }
    /**
     * 
     * @param numParam
     * @return name of param by his number
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    private String getParamNameByNumber(int numParam){
        String[] paramNames;
        String paramName;
        try {
            paramNames = getParamNames();
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + "count parameters: " 
                                + paramNames.length 
                                + ", need for return " + numParam);
            } 
            paramName = new String(paramNames[numParam]);
            return paramName;
        } finally {
            paramNames = null;
            paramName = null;
        }
    }
    /**
     * 
     * @param keyPointFlowName
     * @throw IllegalArgumentException if count of parameters or his
     * names not equal concept
     */
    
    protected void validateCountParams(final UUID keyPointFlowName){
        ConcurrentHashMap<Integer, String> statusNameForKeyPointFlow;
        UUID keyPointFlowNameFunc;
        Integer countThStorageWordStatusNameStorageDirectoryName;
        Integer countThStorageWordStatusNameCurrentFileName;
        Integer countThStorageWordStatusNameNewFileName;
        Integer countThStorageWordStatusNameDeletedFileName;
        Integer countThStorageWordStatusNameFlowFileNamePrefix;
        Integer countSummaryOfParameters;
        try {
            keyPointFlowNameFunc = (UUID) keyPointFlowName;
            if( !isStatusNameNotExist(keyPointFlowNameFunc) ){
                statusNameForKeyPointFlow = getStatusNameForKeyPointFlow(keyPointFlowNameFunc);
                countSummaryOfParameters = 0;
                countThStorageWordStatusNameStorageDirectoryName = 0;
                countThStorageWordStatusNameCurrentFileName = 0;
                countThStorageWordStatusNameNewFileName = 0;
                countThStorageWordStatusNameDeletedFileName = 0;
                countThStorageWordStatusNameFlowFileNamePrefix = 0;
                for(Map.Entry<Integer, String> itemOfLong: statusNameForKeyPointFlow.entrySet()){
                    countSummaryOfParameters++;
                    switch ( itemOfLong.getKey() ) {
                        case 1962941405:
                            countThStorageWordStatusNameStorageDirectoryName++;
                            continue;
                        case 1517772480:
                            countThStorageWordStatusNameCurrentFileName++;
                            continue;
                        case 521024487:
                            countThStorageWordStatusNameNewFileName++;
                            continue;
                        case 2045325664:
                            countThStorageWordStatusNameDeletedFileName++;
                            continue;
                        case -980152217:
                            countThStorageWordStatusNameFlowFileNamePrefix++;
                            continue;
                    }
                    throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameters of flow statusName in StorageWord is not valid, has more values");
                }
                if( countSummaryOfParameters != 5 ){
                    throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameters of flow statusName in StorageWord is not valid, "
                            + "count records not equal three");
                }
                if( countThStorageWordStatusNameStorageDirectoryName != 1 ){
                    throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameters of flow statusName in StorageWord is not valid, "
                            + "count records for StorageDirectoryName not equal one");
                }
                if( countThStorageWordStatusNameCurrentFileName != 1 ){
                    throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameters of flow statusName in StorageWord is not valid, "
                            + "count records for CurrentFileName not equal one");
                }
                if( countThStorageWordStatusNameNewFileName != 1 ){
                    throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameters of flow statusName in StorageWord is not valid, "
                            + "count records for IndexSystemLimitOnStorage not equal one");
                }
                if( countThStorageWordStatusNameDeletedFileName != 1 ){
                    throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameters of flow statusName in StorageWord is not valid, "
                            + "count records for DeletedFileName not equal one");
                }
                if( countThStorageWordStatusNameFlowFileNamePrefix != 1 ){
                    throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameters of flow statusName in StorageWord is not valid, "
                            + "count records for FileNamePrefix not equal one");
                }
            }
        } finally {
            statusNameForKeyPointFlow = null;
            keyPointFlowNameFunc = null;
            countThStorageWordStatusNameStorageDirectoryName = null;
            countThStorageWordStatusNameCurrentFileName = null;
            countThStorageWordStatusNameNewFileName = null;
            countThStorageWordStatusNameDeletedFileName = null;
            countThStorageWordStatusNameFlowFileNamePrefix = null;
            countSummaryOfParameters = null;
        }
    }
}
