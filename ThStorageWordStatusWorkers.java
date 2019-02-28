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
 *     - (3a.5) - Boolean isCachedData - when this param init do it
 *     - (3a.5) - Boolean isCalculatedData
 *     - (3a.5) - Boolean isUdatedDataInHashMap
 * @author wladimirowichbiaran
 */
public class ThStorageWordStatusWorkers {
    /**
     * ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>
     * <keyPointFlowWorkers, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Boolean>> poolStatusWorkers;
    
    ThStorageWordStatusWorkers(){
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
     * @param isCachedData
     * @param isCalculatedData
     * @param isUdatedDataInHashMap
     * @param isMoveFileReady
     * @return lvl(4, 3a.5) ready for put in list lvl(3)
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Boolean>> createStructureParamsFlagsProc(
                        final UUID keyPointFlowWorkers,
                        final Boolean isWriteProcess,
                        final Boolean isReadProcess,
                        final Boolean isCachedData,
                        final Boolean isCalculatedData,
                        final Boolean isUdatedDataInHashMap,
                        final Boolean isMoveFileReady){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Boolean>> returnedParams;
        ConcurrentHashMap<Integer, Boolean> flagsProc;
        UUID keyPointFlowWorkersFlagsProc;
        try{
            keyPointFlowWorkersFlagsProc = keyPointFlowWorkers;
            flagsProc = setInParamFlagsProc(
                        isWriteProcess,
                        isReadProcess,
                        isCachedData,
                        isCalculatedData,
                        isUdatedDataInHashMap,
                        isMoveFileReady);
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
     * @param isCachedData
     * @param isCalculatedData
     * @param isUdatedDataInHashMap
     * @param isMoveFileReady
     * @return lvl(3a.5)
     */
    protected ConcurrentHashMap<Integer, Boolean> setInParamFlagsProc(
                        final Boolean isWriteProcess,
                        final Boolean isReadProcess,
                        final Boolean isCachedData,
                        final Boolean isCalculatedData,
                        final Boolean isUdatedDataInHashMap,
                        final Boolean isMoveFileReady){
        ConcurrentHashMap<Integer, Boolean> returnedHashMap;
        Boolean funcWriteProcess;
        Boolean funcReadProcess;
        Boolean funcCachedData;
        Boolean funcCalculatedData;
        Boolean funcUdatedDataInHashMap;
        Boolean funcMoveFileReady;
        try {
            funcWriteProcess = isWriteProcess;
            funcReadProcess = isReadProcess;
            funcCachedData = isCachedData;
            funcCalculatedData = isCalculatedData;
            funcUdatedDataInHashMap = isUdatedDataInHashMap;
            funcMoveFileReady = isMoveFileReady;
            returnedHashMap = new ConcurrentHashMap<Integer, Boolean>();
            //isWriteProcess - 1640531930
            returnedHashMap.put(1640531930, funcWriteProcess);
            //isReadProcess - 1836000367
            returnedHashMap.put(1836000367, funcReadProcess);
            //isCachedData - -2091433802
            returnedHashMap.put(-2091433802, funcCachedData);
            //isCalculatedData - 1804093010
            returnedHashMap.put(1804093010, funcCalculatedData);
            //isUdatedDataInHashMap - -2092233516
            returnedHashMap.put(-2092233516, funcUdatedDataInHashMap);
            //isMoveFileReady - -1884096596
            returnedHashMap.put(-1884096596, funcMoveFileReady);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            funcWriteProcess = null;
            funcReadProcess = null;
            funcCachedData = null;
            funcCalculatedData = null;
            funcUdatedDataInHashMap = null;
            funcMoveFileReady = null;
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
        Integer countThStorageWordStatusWorkersStorageCachedData;
        Integer countThStorageWordStatusWorkersStorageCalculatedData;
        Integer countThStorageWordStatusWorkersStorageUdatedDataInHashMap;
        Integer countThStorageWordStatusWorkersStorageMoveFileReady;
        Integer countSummaryOfParameters;
        try {
            keyPointFlowWorkersFunc = (UUID) keyPointFlowWorkers;
            if( !isStatusWorkersNotExist(keyPointFlowWorkersFunc) ){
                statusWorkersForKeyPointFlow = getStatusWorkersForKeyPointFlow(keyPointFlowWorkersFunc);
                countSummaryOfParameters = 0;
                
                countThStorageWordStatusWorkersStorageWriteProcess = 0;
                countThStorageWordStatusWorkersStorageReadProcess = 0;
                countThStorageWordStatusWorkersStorageCachedData = 0;
                
                countThStorageWordStatusWorkersStorageCalculatedData = 0;
                countThStorageWordStatusWorkersStorageUdatedDataInHashMap = 0;
                countThStorageWordStatusWorkersStorageMoveFileReady = 0;
                
                for(Map.Entry<Integer, Boolean> itemOfLong: statusWorkersForKeyPointFlow.entrySet()){
                    countSummaryOfParameters++;
                    switch ( itemOfLong.getKey() ) {
                        case 1640531930:
                            countThStorageWordStatusWorkersStorageWriteProcess++;
                            continue;
                        case 1836000367:
                            countThStorageWordStatusWorkersStorageReadProcess++;
                            continue;
                        case -2091433802:
                            countThStorageWordStatusWorkersStorageCachedData++;
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
                    }
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, has more values");
                }
                if( countSummaryOfParameters != 6 ){
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
                if( countThStorageWordStatusWorkersStorageCachedData != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in StorageWord is not valid, "
                            + "count records for CachedData not equal one");
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
            }
        } finally {
            statusWorkersForKeyPointFlow = null;
            keyPointFlowWorkersFunc = null;
            
            countThStorageWordStatusWorkersStorageWriteProcess = null;
            countThStorageWordStatusWorkersStorageReadProcess = null;
            countThStorageWordStatusWorkersStorageCachedData = null;
            
            countThStorageWordStatusWorkersStorageCalculatedData = null;
            countThStorageWordStatusWorkersStorageUdatedDataInHashMap = null;
            countThStorageWordStatusWorkersStorageMoveFileReady = null;
            
            countSummaryOfParameters = null;
        }
    }
}
