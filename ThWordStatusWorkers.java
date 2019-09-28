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
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * ThStorageWordStatusWorkers
 * <p>Boolean flagsProcessFlow:
 * <ol>
 * <li>    - (3a.5.0) - isWriteProcess - when this param init do it
 * <li>    - (3a.5.1) - isReadProcess - when this param init do it
 * <li>    - (3a.5.2) - isNeedReadData - when in flow data has write, this flag
 *                  set in TRUE for start reader job before write data, also
 *                  in StatusName, currentFileName set equal newFileName
 *                  in name generate functions add query for names equals and
 *                  flags about needRead isNeedReadData, writer process poll data
 *                  from ReadedCache, builder newFileName, build name with data from
 *                  ReaderCache when set isCachedReadedData to TRUE
 * <li>    - (3a.5.3) - isCachedData - when this param init do it
 * <li>    - (3a.5.4) - isCachedReadedData - when read process finished set
 *                  this flag, set to false, writer process poll data
 *                  from ReadedCache, builder newFileName, build name with data from
 *                  ReaderCache when set isCachedReadedData to TRUE
 * <li>    - (3a.5.5) - isCalculatedData
 * <li>    - (3a.5.6) - isUdatedDataInHashMap
 * <li>    - (3a.5.7) - isMoveFileReady - when move file process is finished,
 *                  set isNeedRead to TRUE, StatusName currentFileName set equal to
 *                  newFileName
 * <li>    - (3a.5.8) - isFlowInWriteBus when data insert into Bus for Writer
 * <li>    - (3a.5.9) - isFlowInReadBus when data insert into Bus for Reader
 * <li>    - (3a.5.10) - isNeedDeleteOldFile when flow write, read iteration end
 * <li>    - (3a.5.11) - isOldFileDeleted when writer delete file with old writed
 *                  and readed data
 * </ol>
 * @author wladimirowichbiaran
 */
public class ThWordStatusWorkers {
    /**
     * ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Long>>
     * <keyPointFlowWorkers, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private final Long timeCreation;
    private final UUID objectLabel;
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Boolean>> poolStatusWorkers;
    
    public ThWordStatusWorkers(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusWorkers = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Boolean>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    private ConcurrentSkipListMap<Integer, Boolean> getStatusWorkersForKeyPointFlow(final UUID keyPointFlowWorkers){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, Boolean> getStatusWorkersFormPool;
        try{
            inputedVal = (UUID) keyPointFlowWorkers;
            getStatusWorkersFormPool = this.poolStatusWorkers.get(inputedVal);
            if( getStatusWorkersFormPool == null ){
                throw new IllegalStateException(ThWordStatusWorkers.class.getCanonicalName()
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
        ConcurrentSkipListMap<Integer, Boolean> getRemovedStatusWorkersFormPool;
        try{
            inputedVal = (UUID) keyPointFlowWorkers;
            getRemovedStatusWorkersFormPool = (ConcurrentSkipListMap<Integer, Boolean>) this.poolStatusWorkers.remove(inputedVal);
            if( getRemovedStatusWorkersFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Boolean> itemOfPoint : getRemovedStatusWorkersFormPool.entrySet() ){
                Integer removedKey = itemOfPoint.getKey();
                Boolean removedVal = getRemovedStatusWorkersFormPool.remove(removedKey);
                removedVal = null;
                removedKey = null;
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
     * @param keyPointFlowWorkersInputed
     * @param paramNumber
     * @return 
     * @throw IllegalStateException is keyPointFlowWorkersInputed not exist
     */
    protected Boolean getValueForFlowPointByNumber(
            final UUID keyPointFlowDataFsInputed, 
            final Integer paramNumber){
        ConcurrentSkipListMap<Integer, Boolean> getListValues;
        Integer returnedParamValue;
        UUID keyPointFlowWorkersFunc;
        try{
            keyPointFlowWorkersFunc = (UUID) keyPointFlowDataFsInputed;
            returnedParamValue = (Integer) paramNumber;
            if( isStatusWorkersNotExist(keyPointFlowWorkersFunc) ){
                throw new IllegalStateException(ThWordStatusDataFs.class.getCanonicalName()
                        + " not exist values for UUID "
                        + keyPointFlowWorkersFunc.toString()
                );
            }
            getListValues = this.poolStatusWorkers.get(keyPointFlowWorkersFunc);
            Integer paramCodeByNumber = getParamCodeByNumber(returnedParamValue);
            Boolean getParamForReturn = getListValues.get(paramCodeByNumber);
            return new Boolean(getParamForReturn.booleanValue());
        } finally {
            keyPointFlowWorkersFunc = null;
            getListValues = null;
        }
    }
    /**
     * create new structure for UUID, and set all values to Boolean.FALSE
     * @param keyPointFlowWorkersInputed 
     * @see setInitParamWorkers()
     */
    protected void createStructureParamsWorkers(
                        final UUID keyPointFlowWorkersInputed){
        ConcurrentSkipListMap<Integer, Boolean> countWorkers;
        UUID keyPointFlowWorkersFunc;
        try{
            keyPointFlowWorkersFunc = (UUID) keyPointFlowWorkersInputed;
            if( isStatusWorkersNotExist(keyPointFlowWorkersFunc) ){
                countWorkers = setInitParamWorkers();
                this.poolStatusWorkers.put(keyPointFlowWorkersFunc, countWorkers);
            }
        } finally {
            countWorkers = null;
            keyPointFlowWorkersFunc = null;
        }
    }
    /**
     * Create, set all values to Boolean.FALSE
     * @return ConcurrentSkipListMap<Integer, Boolean>
     *                          <ParamCode, Value>
     * @see ThWordStatusWorkers#getParamCodeByNumber(int) 
     */
    private ConcurrentSkipListMap<Integer, Boolean> setInitParamWorkers(){
        ConcurrentSkipListMap<Integer, Boolean> returnedHashMap;
        Integer paramCodeByNumber;
        Integer countParamsWorkersForSet;
        Integer idx;
        try {
            returnedHashMap = new ConcurrentSkipListMap<Integer, Boolean>();
            countParamsWorkersForSet = getParamCount();
            for(idx = 0; idx < countParamsWorkersForSet; idx++ ){
                paramCodeByNumber = getParamCodeByNumber(idx);
                returnedHashMap.put(paramCodeByNumber, Boolean.FALSE);
            }

            return returnedHashMap;
        } finally {
            idx = null;
            paramCodeByNumber = null;
            countParamsWorkersForSet = null;
            returnedHashMap = null;
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
     * @param changedKeyPointFlowWorkers
     * @param paramNumber
     * @param changedVal 
     * 
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    protected void changeParamValByNumber(final UUID changedKeyPointFlowWorkers, final Integer paramNumber, final Boolean changedVal){
        UUID changedKeyPointFlowWorkersFunc;
        Integer paramNumberFunc;
        Boolean changedValFunc;
        Integer paramCodeByNumber;
        ConcurrentSkipListMap<Integer, Boolean> fromCurrentFlow;
        try{
            changedKeyPointFlowWorkersFunc = (UUID) changedKeyPointFlowWorkers;
            validateCountParams(changedKeyPointFlowWorkersFunc);
            paramNumberFunc = (Integer) paramNumber;
            changedValFunc = (Boolean) changedVal;
            fromCurrentFlow = (ConcurrentSkipListMap<Integer, Boolean>) this.poolStatusWorkers.get(changedKeyPointFlowWorkersFunc);
            paramCodeByNumber = (Integer) getParamCodeByNumber(paramNumberFunc);
            fromCurrentFlow.put(paramCodeByNumber, changedValFunc);
            this.poolStatusWorkers.put(changedKeyPointFlowWorkersFunc, fromCurrentFlow);
        } finally {
            changedKeyPointFlowWorkersFunc = null;
            paramNumberFunc = null;
            changedValFunc = null;
            paramCodeByNumber = null;
            fromCurrentFlow = null;
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
    private Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = getParamNames();
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusWorkers.class.getCanonicalName() 
                                + " parameters of flow statusWorkers in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusWorkers.class.getCanonicalName() 
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
    private int getParamCount(){
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
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusWorkers.class.getCanonicalName() 
                                + " parameters of flow statusWorkers in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusWorkers.class.getCanonicalName() 
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
        UUID keyPointFlowWorkersFunc;
        ConcurrentSkipListMap<Integer, Boolean> statusWorkersForKeyPointFlow;
        Integer sizeRec;
        Integer paramCount;
        Integer idxParam;
        Integer paramCodeByNumber;
        String paramNameByNumber;
        
        try {
            keyPointFlowWorkersFunc = (UUID) keyPointFlowWorkers;
            if( keyPointFlowWorkersFunc == null ){
                throw new NullPointerException(ThWordStatusWorkers.class.getCanonicalName() 
                        + " need point flow uuid, argument for validate is null");
            }
            if( !isStatusWorkersNotExist(keyPointFlowWorkersFunc) ){
                
                statusWorkersForKeyPointFlow = (ConcurrentSkipListMap<Integer, Boolean>) getStatusWorkersForKeyPointFlow(keyPointFlowWorkersFunc);
                sizeRec = (Integer) statusWorkersForKeyPointFlow.size();
                paramCount = (Integer) getParamCount();
                if( sizeRec != paramCount ){
                    
                    throw new IllegalArgumentException(ThWordStatusWorkers.class.getCanonicalName() 
                            + " parameters of flow statusWorkers in Word is not valid, "
                            + "count records " + sizeRec + " not equal " + paramCount);
                }
                
                for(idxParam = 0; idxParam < paramCount; idxParam++ ){
                    
                    paramCodeByNumber = getParamCodeByNumber(idxParam);
                    if( !statusWorkersForKeyPointFlow.containsKey(paramCodeByNumber) ){
                        
                        paramNameByNumber = getParamNameByNumber(idxParam);
                        throw new IllegalArgumentException(ThWordStatusWorkers.class.getCanonicalName() 
                            + " parameter "
                            + " for name: " + paramNameByNumber
                            + " in inputed data for set into flow statusWorkers not exist");
                    }
                }
            }
            
        } finally {
            sizeRec = null;
            paramCount = null;
            idxParam = null;
            statusWorkersForKeyPointFlow = null;
            keyPointFlowWorkersFunc = null;
            paramCodeByNumber = null;
            paramNameByNumber = null;
        }
    }
}
