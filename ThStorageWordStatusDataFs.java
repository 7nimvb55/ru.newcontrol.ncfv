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
 * ThStorageWordStatusDataFs
 * countFS    - (3a.1) - Integer countRecordsOnFileSystem - updated onWrite, 
 *                before write (Read, Write into old file name, 
 *                after write Files.move to newFileName
 *     - (3a.1) - Integer volumeNumber - update onWrite, before
 *                write = ifLimit ? update : none
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordStatusDataFs {
    private final Long timeCreation;
    private final UUID objectLabel;
    /**
     * ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>
     * <keyPointFlowDataFs, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> poolStatusDataFs;
    
    ThStorageWordStatusDataFs(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusDataFs = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    protected ConcurrentHashMap<Integer, Integer> getStatusDataFsForKeyPointFlow(final UUID keyPointFlowDataFs){
        UUID inputedVal;
        ConcurrentHashMap<Integer, Integer> getStatusDataFsFormPool;
        try{
            inputedVal = (UUID) keyPointFlowDataFs;
            getStatusDataFsFormPool = this.poolStatusDataFs.get(inputedVal);
            if( getStatusDataFsFormPool == null ){
                throw new IllegalStateException(ThStorageWordStatusDataFs.class.getCanonicalName()
                + " not exist record in list for "
                + inputedVal.toString() + " key point flow");
            }
            /**
             * @todo
             * update access time for point flow if in one point of all flow get
             * increment 
             */
            return getStatusDataFsFormPool;
        } finally {
            inputedVal = null;
            getStatusDataFsFormPool = null;
        }
    }
    /**
     * 
     * @param keyPointFlowDataFs
     * @return true if found and delete data
     */
    protected Boolean removeStatusDataFsForKeyPointFlow(final UUID keyPointFlowDataFs){
        UUID inputedVal;
        ConcurrentHashMap<Integer, Integer> getRemovedStatusDataFsFormPool;
        try{
            inputedVal = (UUID) keyPointFlowDataFs;
            getRemovedStatusDataFsFormPool = (ConcurrentHashMap<Integer, Integer>) this.poolStatusDataFs.remove(inputedVal);
            if( getRemovedStatusDataFsFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Integer> itemOfPoint : getRemovedStatusDataFsFormPool.entrySet() ){
                Integer remove = getRemovedStatusDataFsFormPool.remove(itemOfPoint.getKey());
                Integer [] remStrVal = {remove};
                remStrVal = null;
                Integer [] remIntKey = {itemOfPoint.getKey()};
                remIntKey = null;
            }
            getRemovedStatusDataFsFormPool = null;
            return Boolean.TRUE;
        } finally {
            inputedVal = null;
            getRemovedStatusDataFsFormPool = null;
        }
    }
    /**
     * not exist bus
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected Boolean isStatusDataFsNotExist(final UUID keyPointFlowDataFs){
        UUID inputedVal;
        try{
            inputedVal = (UUID) keyPointFlowDataFs;
            if( !this.poolStatusDataFs.containsKey(inputedVal) ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            inputedVal = null;
        }
    }
    /**
     * 
     * @param countRecords
     * @param volumeNumber
     * @return lvl(4, 3a.1) ready for put in list lvl(3)
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> createStructureParamsCountFs(
                        final UUID keyPointFlowDataFs,
                        final Integer countRecords,
                        final Integer volumeNumber){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> returnedParams;
        ConcurrentHashMap<Integer, Integer> countFs;
        UUID keyPointFlowDataFsCountFs;
        try{
            keyPointFlowDataFsCountFs = keyPointFlowDataFs;
            countFs = setInParamCountFS(countRecords,
                        volumeNumber);
            returnedParams = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>>();
            this.poolStatusDataFs.put(keyPointFlowDataFsCountFs, countFs);
            returnedParams.put(keyPointFlowDataFsCountFs , countFs);
            return returnedParams;
        } finally {
            returnedParams = null;
            countFs = null;
            keyPointFlowDataFsCountFs = null;
        }
    }
    /**
     * 
     * @param countRecords
     * @param volumeNumber
     * @return lvl(3a.1)
     */
    protected ConcurrentHashMap<Integer, Integer> setInParamCountFS(
                        final Integer countRecords,
                        final Integer volumeNumber){
        ConcurrentHashMap<Integer, Integer> returnedHashMap;
        Integer funcCountRecords;
        Integer funcVolumeNumber;
        try {
            funcCountRecords = countRecords;
            if( funcCountRecords < 0 ){
                funcCountRecords = 0;
            }
            funcVolumeNumber = volumeNumber;
            if( funcVolumeNumber < 0 ){
                funcVolumeNumber = 0;
            }
            returnedHashMap = new ConcurrentHashMap<Integer, Integer>();
            //countRecordsOnFileSystem.hashCode() - -2011092003
            returnedHashMap.put(-2011092003, funcCountRecords);
            //volumeNumber.hashCode() - -1832815869
            returnedHashMap.put(-1832815869, funcVolumeNumber);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            funcCountRecords = null;
            funcVolumeNumber = null;
        }
    }
    /**
     * <ul>
     * <li>0 - countRecordsOnFileSystem
     * <li>1 - volumeNumber
     * </ul>
     * @return 
     */
    private String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "countRecordsOnFileSystem",
                "volumeNumber"
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
                throw new IllegalArgumentException(ThStorageWordStatusDataFs.class.getCanonicalName() 
                                + " parameters of flow statusDataFs in StorageWord is not valid, "
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
                throw new IllegalArgumentException(ThStorageWordStatusDataFs.class.getCanonicalName() 
                                + " parameters of flow statusDataFs in StorageWord is not valid, "
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
     * @param keyPointFlowDataFs
     * 
     * @throw IllegalArgumentException if count of parameters or his
     * names not equal concept
     */
    
    protected void validateCountParams(final UUID keyPointFlowDataFs){
        ConcurrentHashMap<Integer, Integer> statusDataFsForKeyPointFlow;
        UUID keyPointFlowDataFsFunc;
        Integer countThStorageWordStatusDataFsCountRecordsOnFileSystem;
        Integer countThStorageWordStatusDataFsVolumeNumber;
        Integer countSummaryOfParameters;
        try {
            keyPointFlowDataFsFunc = (UUID) keyPointFlowDataFs;
            if( !isStatusDataFsNotExist(keyPointFlowDataFsFunc) ){
                statusDataFsForKeyPointFlow = getStatusDataFsForKeyPointFlow(keyPointFlowDataFsFunc);
                countSummaryOfParameters = 0;
                countThStorageWordStatusDataFsCountRecordsOnFileSystem = 0;
                countThStorageWordStatusDataFsVolumeNumber = 0;
                for(Map.Entry<Integer, Integer> itemOfLong: statusDataFsForKeyPointFlow.entrySet()){
                    countSummaryOfParameters++;
                    switch ( itemOfLong.getKey() ) {
                        case -2011092003:
                            countThStorageWordStatusDataFsCountRecordsOnFileSystem++;
                            continue;
                        case -1832815869:
                            countThStorageWordStatusDataFsVolumeNumber++;
                            continue;
                    }
                    new IllegalArgumentException(ThStorageWordStatusDataFs.class.getCanonicalName() 
                            + " parameters of flow statusDataFs in StorageWord is not valid, has more values");
                }
                if( countSummaryOfParameters != 2 ){
                    new IllegalArgumentException(ThStorageWordStatusDataFs.class.getCanonicalName() 
                            + " parameters of flow statusDataFs in StorageWord is not valid, "
                            + "count records not equal two");
                }
                if( countThStorageWordStatusDataFsCountRecordsOnFileSystem != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusDataFs.class.getCanonicalName() 
                            + " parameters of flow statusDataFs in StorageWord is not valid, "
                            + "count records for CountRecordsOnFileSystem not equal one");
                }
                if( countThStorageWordStatusDataFsVolumeNumber != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusDataFs.class.getCanonicalName() 
                            + " parameters of flow statusDataFs in StorageWord is not valid, "
                            + "count records for VolumeNumber not equal one");
                }
            }
        } finally {
            statusDataFsForKeyPointFlow = null;
            keyPointFlowDataFsFunc = null;
            countThStorageWordStatusDataFsCountRecordsOnFileSystem = null;
            countThStorageWordStatusDataFsVolumeNumber = null;
            countSummaryOfParameters = null;
        }
    }
}
