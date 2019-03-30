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
 * ThStorageWordStatusWorkers
 * flagsProc  - (3a.5) - Boolean isWriteProcess - when this param init do it
 *     - (3a.5) - Boolean isReadProcess - when this param init do it
 *     - (3a.5) - Boolean isNeedReadData - when in flow data has write, this flag
 *                  set in TRUE for start reader job before write data, also
 *                  in StatusName, currentFileName set equal newFileName
 *                  in name generate functions add query for names equals and
 *                  flags about needRead isNeedReadData, writer process poll data
 *                  from ReadedCache, builder newFileName, build name with data from
 *                  ReaderCache when set isCachedReadedData to TRUE
 *     - (3a.5) - Boolean isCachedData - when this param init do it
 *     - (3a.5) - Boolean isCachedReadedData - when read process finished set
 *                  this flag, set to false, writer process poll data
 *                  from ReadedCache, builder newFileName, build name with data from
 *                  ReaderCache when set isCachedReadedData to TRUE
 *     - (3a.5) - Boolean isCalculatedData
 *     - (3a.5) - Boolean isUdatedDataInHashMap
 *     - (3a.5) - Boolean isMoveFileReady - when move file process is finished,
 *                  set isNeedRead to TRUE, StatusName currentFileName set equal to
 *                  newFileName
 *     - (3a.5) - Boolean isFlowInWriteBus when data insert into Bus for Writer
 *     - (3a.5) - Boolean isFlowInReadBus when data insert into Bus for Reader
 *     - (3a.5) - Boolean isNeedDeleteOldFile when flow write, read iteration end
 *     - (3a.5) - Boolean isOldFileDeleted  when writer delete file woth old writed
 *                  and readed data
 * @author wladimirowichbiaran
 */
public class ThStorageWordStatusWorkers {
    /**
     * ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>
     * <keyPointFlowWorkers, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private final Long timeCreation;
    private final UUID objectLabel;
    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Boolean>> poolStatusWorkers;
    
    ThStorageWordStatusWorkers(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusWorkers = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Boolean>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    protected ConcurrentHashMap<Integer, Boolean> getStatusWorkersForKeyPointFlow(final UUID keyPointFlowWorkers){
        UUID inputedVal;
        ConcurrentHashMap<Integer, Boolean> getStatusWorkersFormPool;
        try{
            inputedVal = (UUID) keyPointFlowWorkers;
            getStatusWorkersFormPool = this.poolStatusWorkers.get(inputedVal);
            if( getStatusWorkersFormPool == null ){
                throw new IllegalStateException(ThStorageWordStatusWorkers.class.getCanonicalName()
                + " not exist record in list for "
                + inputedVal.toString() + " key point flow");
            }
            /**
             * @todo
             * update access time for point flow if in one point of all flow get
             * increment 
             */
            return getStatusWorkersFormPool;
        } finally {
            inputedVal = null;
            getStatusWorkersFormPool = null;
        }
    }
    /**
     * 
     * @param keyPointFlowDataFs
     * @return true if found and delete data
     */
    protected Boolean removeStatusWorkersForKeyPointFlow(final UUID keyPointFlowWorkers){
        UUID inputedVal;
        ConcurrentHashMap<Integer, Boolean> getRemovedStatusWorkersFormPool;
        try{
            inputedVal = (UUID) keyPointFlowWorkers;
            getRemovedStatusWorkersFormPool = (ConcurrentHashMap<Integer, Boolean>) this.poolStatusWorkers.remove(inputedVal);
            if( getRemovedStatusWorkersFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Boolean> itemOfPoint : getRemovedStatusWorkersFormPool.entrySet() ){
                Boolean remove = getRemovedStatusWorkersFormPool.remove(itemOfPoint.getKey());
                Boolean [] remStrVal = {remove};
                remStrVal = null;
                Integer [] remIntKey = {itemOfPoint.getKey()};
                remIntKey = null;
            }
            getRemovedStatusWorkersFormPool = null;
            return Boolean.TRUE;
        } finally {
            inputedVal = null;
            getRemovedStatusWorkersFormPool = null;
        }
    }
    /**
     * not exist bus
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected Boolean isStatusWorkersNotExist(final UUID keyPointFlowWorkers){
        UUID inputedVal;
        try{
            inputedVal = (UUID) keyPointFlowWorkers;
            if( !this.poolStatusWorkers.containsKey(inputedVal) ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            inputedVal = null;
        }
    }
    /**
     * 
     * @param isWriteProcess
     * @param isReadProcess
     * @param isNeedReadData
     * @param isCachedData
     * @param isCachedReadedData
     * @param isCalculatedData
     * @param isUdatedDataInHashMap
     * @param isMoveFileReady
     * @return lvl(4, 3a.5) ready for put in list lvl(3)
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Boolean>> createStructureParamsFlagsProc(
                        final UUID keyPointFlowWorkers,
                        final Boolean isWriteProcess,
                        final Boolean isReadProcess,
                        final Boolean isNeedReadData,
                        final Boolean isCachedData,
                        final Boolean isCachedReadedData,
                        final Boolean isCalculatedData,
                        final Boolean isUdatedDataInHashMap,
                        final Boolean isMoveFileReady,
                        final Boolean isFlowInWriteBus,
                        final Boolean isFlowInReadBus,
                        final Boolean isNeedDeleteOldFile,
                        final Boolean isOldFileDeleted){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Boolean>> returnedParams;
        ConcurrentHashMap<Integer, Boolean> flagsProc;
        UUID keyPointFlowWorkersFlagsProc;
        try{
            keyPointFlowWorkersFlagsProc = keyPointFlowWorkers;
            flagsProc = setInParamFlagsProc(
                        isWriteProcess,
                        isReadProcess,
                        isNeedReadData,
                        isCachedData,
                        isCachedReadedData,
                        isCalculatedData,
                        isUdatedDataInHashMap,
                        isMoveFileReady,
                        isFlowInWriteBus,
                        isFlowInReadBus,
                        isNeedDeleteOldFile,
                        isOldFileDeleted);
            returnedParams = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Boolean>>();
            this.poolStatusWorkers.put(keyPointFlowWorkersFlagsProc, flagsProc);
            returnedParams.put(keyPointFlowWorkersFlagsProc , flagsProc);
            return returnedParams;
        } finally {
            returnedParams = null;
            flagsProc = null;
        }
    }
    /**
     * 
     * @param isWriteProcess
     * @param isReadProcess
     * @param isNeedReadData
     * @param isCachedData
     * @param isCachedReadedData
     * @param isCalculatedData
     * @param isUdatedDataInHashMap
     * @param isMoveFileReady
     * @param isFlowInWriteBus
     * @param isFlowInReadBus
     * @param isNeedDeleteOldFile
     * @param isOldFileDeleted
     * @return lvl(4, 3a.5) ready for put in list lvl(3)
     */
    protected ConcurrentHashMap<Integer, Boolean> setInParamFlagsProc(
                        final Boolean isWriteProcess,
                        final Boolean isReadProcess,
                        final Boolean isNeedReadData,
                        final Boolean isCachedData,
                        final Boolean isCachedReadedData,
                        final Boolean isCalculatedData,
                        final Boolean isUdatedDataInHashMap,
                        final Boolean isMoveFileReady,
                        final Boolean isFlowInWriteBus,
                        final Boolean isFlowInReadBus,
                        final Boolean isNeedDeleteOldFile,
                        final Boolean isOldFileDeleted
    ){
        ConcurrentHashMap<Integer, Boolean> returnedHashMap;
        Boolean funcWriteProcess;
        Boolean funcReadProcess;
        Boolean funcNeedReadData;
        Boolean funcCachedData;
        Boolean funcCachedReadedData;
        Boolean funcCalculatedData;
        Boolean funcUdatedDataInHashMap;
        Boolean funcMoveFileReady;
        Boolean funcFlowInWriteBus;
        Boolean funcFlowInReadBus;
        Boolean funcNeedDeleteOldFile;
        Boolean funcOldFileDeleted;
        try {
            funcWriteProcess = (Boolean) isWriteProcess;
            funcReadProcess = (Boolean) isReadProcess;
            funcNeedReadData = (Boolean) isNeedReadData;
            funcCachedData = (Boolean) isCachedData;
            funcCachedReadedData = (Boolean) isCachedReadedData;
            funcCalculatedData = (Boolean) isCalculatedData;
            funcUdatedDataInHashMap = (Boolean) isUdatedDataInHashMap;
            funcMoveFileReady = (Boolean) isMoveFileReady;
            funcFlowInWriteBus = (Boolean) isFlowInWriteBus;
            funcFlowInReadBus = (Boolean) isFlowInReadBus;
            funcNeedDeleteOldFile = (Boolean) isNeedDeleteOldFile;
            funcOldFileDeleted = (Boolean) isOldFileDeleted;
            returnedHashMap = new ConcurrentHashMap<Integer, Boolean>();
            //01 - isWriteProcess - 1640531930
            returnedHashMap.put(1640531930, funcWriteProcess);
            //02 - isReadProcess - 1836000367
            returnedHashMap.put(1836000367, funcReadProcess);
            //03 - isNeedReadData - -83825824
            returnedHashMap.put(-83825824, funcNeedReadData);
            //04 - isCachedData - -2091433802
            returnedHashMap.put(-2091433802, funcCachedData);
            //05 - isCachedReadedData - -660426229
            returnedHashMap.put(-660426229, funcCachedReadedData);
            //06 - isCalculatedData - 1804093010
            returnedHashMap.put(1804093010, funcCalculatedData);
            //07 - isUdatedDataInHashMap - -2092233516
            returnedHashMap.put(-2092233516, funcUdatedDataInHashMap);
            //08 - isMoveFileReady - -1884096596
            returnedHashMap.put(-1884096596, funcMoveFileReady);
            //09 - isFlowInWriteBus - -993366530
            returnedHashMap.put(-993366530, funcFlowInWriteBus);
            //10 - isFlowInReadBus - 1939180941
            returnedHashMap.put(1939180941, funcFlowInReadBus);
            //11 - isNeedDeleteOldFile - -1172779240
            returnedHashMap.put(-1172779240, funcNeedDeleteOldFile);
            //12 - isOldFileDeleted - 1736565280
            returnedHashMap.put(1736565280, funcOldFileDeleted);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            funcWriteProcess = null;
            funcReadProcess = null;
            funcNeedReadData = null;
            funcCachedData = null;
            funcCachedReadedData = null;
            funcCalculatedData = null;
            funcUdatedDataInHashMap = null;
            funcMoveFileReady = null;
            funcFlowInWriteBus = null;
            funcFlowInReadBus = null;
            funcNeedDeleteOldFile = null;
            funcOldFileDeleted = null;
        }
    }
    /**
     * <ul>
     * <li>0 - isWriteProcess
     * <li>1 - isReadProcess
     * <li>2 - isNeedReadData
     * <li>3 - isCachedData
     * <li>4 - isCachedReadedData
     * <li>5 - isCalculatedData
     * <li>6 - isUdatedDataInHashMap
     * <li>7 - isMoveFileReady
     * <li>8 - isFlowInWriteBus
     * <li>9 - isFlowInReadBus
     * <li>10 - isNeedDeleteOldFile
     * <li>11 - isOldFileDeleted
     * </ul>
     * @return 
     */
    private String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "isWriteProcess",
                "isReadProcess",
                "isNeedReadData",
                "isCachedData",
                "isCachedReadedData",
                "isCalculatedData",
                "isUdatedDataInHashMap",
                "isMoveFileReady",
                "isFlowInWriteBus",
                "isFlowInReadBus",
                "isNeedDeleteOldFile",
                "isOldFileDeleted"
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
                throw new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                                + " parameters of flow statusWorkers in StorageWord is not valid, "
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
                throw new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                                + " parameters of flow statusWorkers in StorageWord is not valid, "
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
     * @param keyPointFlowWorkers
     * @throw IllegalArgumentException if count of parameters or his
     * names not equal concept
     */
    
    protected void validateCountParams(final UUID keyPointFlowWorkers){
        ConcurrentHashMap<Integer, Boolean> statusWorkersForKeyPointFlow;
        UUID keyPointFlowWorkersFunc;
        Integer countThStorageWordStatusWorkersStorageWriteProcess;
        Integer countThStorageWordStatusWorkersStorageReadProcess;
        Integer countThStorageWordStatusWorkersStorageNeedReadData;
        Integer countThStorageWordStatusWorkersStorageCachedData;
        Integer countThStorageWordStatusWorkersStorageCachedReadedData;
        Integer countThStorageWordStatusWorkersStorageCalculatedData;
        Integer countThStorageWordStatusWorkersStorageUdatedDataInHashMap;
        Integer countThStorageWordStatusWorkersStorageMoveFileReady;
        Integer countThStorageWordStatusWorkersStorageFlowInWriteBus;
        Integer countThStorageWordStatusWorkersStorageFlowInReadBus;
        Integer countThStorageWordStatusWorkersStorageNeedDeleteOldFile;
        Integer countThStorageWordStatusWorkersStorageOldFileDeleted;
        Integer countSummaryOfParameters;
        try {
            keyPointFlowWorkersFunc = (UUID) keyPointFlowWorkers;
            if( !isStatusWorkersNotExist(keyPointFlowWorkersFunc) ){
                statusWorkersForKeyPointFlow = getStatusWorkersForKeyPointFlow(keyPointFlowWorkersFunc);
                countSummaryOfParameters = 0;
                
                countThStorageWordStatusWorkersStorageWriteProcess = 0;
                countThStorageWordStatusWorkersStorageReadProcess = 0;
                countThStorageWordStatusWorkersStorageNeedReadData = 0;
                countThStorageWordStatusWorkersStorageCachedData = 0;
                countThStorageWordStatusWorkersStorageCachedReadedData = 0;
                
                countThStorageWordStatusWorkersStorageCalculatedData = 0;
                countThStorageWordStatusWorkersStorageUdatedDataInHashMap = 0;
                countThStorageWordStatusWorkersStorageMoveFileReady = 0;
                
                countThStorageWordStatusWorkersStorageFlowInWriteBus = 0;
                countThStorageWordStatusWorkersStorageFlowInReadBus = 0;
                countThStorageWordStatusWorkersStorageNeedDeleteOldFile = 0;
                countThStorageWordStatusWorkersStorageOldFileDeleted = 0;
                
                for(Map.Entry<Integer, Boolean> itemOfLong: statusWorkersForKeyPointFlow.entrySet()){
                    countSummaryOfParameters++;
                    switch ( itemOfLong.getKey() ) {
                        case 1640531930:
                            countThStorageWordStatusWorkersStorageWriteProcess++;
                            continue;
                        case 1836000367:
                            countThStorageWordStatusWorkersStorageReadProcess++;
                            continue;
                        case -83825824:
                            countThStorageWordStatusWorkersStorageNeedReadData++;
                            continue;
                        case -2091433802:
                            countThStorageWordStatusWorkersStorageCachedData++;
                            continue;
                        case -660426229:
                            countThStorageWordStatusWorkersStorageCachedReadedData++;
                            continue;    
                        case 1804093010:
                            countThStorageWordStatusWorkersStorageCalculatedData++;
                            continue;
                        case -2092233516:
                            countThStorageWordStatusWorkersStorageUdatedDataInHashMap++;
                            continue;
                        case -1884096596:
                            countThStorageWordStatusWorkersStorageMoveFileReady++;
                            continue;
                        case -993366530:
                            countThStorageWordStatusWorkersStorageFlowInWriteBus++;
                            continue;
                        case 1939180941:
                            countThStorageWordStatusWorkersStorageFlowInReadBus++;
                            continue;
                        case -1172779240:
                            countThStorageWordStatusWorkersStorageNeedDeleteOldFile++;
                            continue;
                        case 1736565280:
                            countThStorageWordStatusWorkersStorageOldFileDeleted++;
                            continue;
                    }
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, has more values");
                }
                if( countSummaryOfParameters != 12 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records not equal six");
                }
                if( countThStorageWordStatusWorkersStorageWriteProcess != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for WriteProcess not equal one");
                }
                if( countThStorageWordStatusWorkersStorageReadProcess != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for ReadProcess not equal one");
                }
                if( countThStorageWordStatusWorkersStorageNeedReadData != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for NeedReadData not equal one");
                }
                if( countThStorageWordStatusWorkersStorageCachedData != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for CachedData not equal one");
                }
                if( countThStorageWordStatusWorkersStorageCachedReadedData != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for CachedReadedData not equal one");
                }
                if( countThStorageWordStatusWorkersStorageCalculatedData != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for CalculatedData not equal one");
                }
                if( countThStorageWordStatusWorkersStorageUdatedDataInHashMap != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for UdatedDataInHashMap not equal one");
                }
                if( countThStorageWordStatusWorkersStorageMoveFileReady != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for MoveFileReady not equal one");
                }
                
                if( countThStorageWordStatusWorkersStorageFlowInWriteBus != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for FlowInWriteBus not equal one");
                }
                if( countThStorageWordStatusWorkersStorageFlowInReadBus != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for FlowInReadBus not equal one");
                }
                if( countThStorageWordStatusWorkersStorageNeedDeleteOldFile != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for NeedDeleteOldFile not equal one");
                }
                if( countThStorageWordStatusWorkersStorageOldFileDeleted != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for OldFileDeleted not equal one");
                }
            }
        } finally {
            statusWorkersForKeyPointFlow = null;
            keyPointFlowWorkersFunc = null;
            
            countThStorageWordStatusWorkersStorageWriteProcess = null;
            countThStorageWordStatusWorkersStorageReadProcess = null;
            countThStorageWordStatusWorkersStorageNeedReadData = null;
            countThStorageWordStatusWorkersStorageCachedData = null;
            countThStorageWordStatusWorkersStorageCachedReadedData = null;
            
            countThStorageWordStatusWorkersStorageCalculatedData = null;
            countThStorageWordStatusWorkersStorageUdatedDataInHashMap = null;
            countThStorageWordStatusWorkersStorageMoveFileReady = null;
            
            countThStorageWordStatusWorkersStorageFlowInWriteBus = null;
            countThStorageWordStatusWorkersStorageFlowInReadBus = null;
            countThStorageWordStatusWorkersStorageNeedDeleteOldFile = null;
            countThStorageWordStatusWorkersStorageOldFileDeleted = null;
            
            countSummaryOfParameters = null;
        }
    }
}
