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
 * ThStorageWordStatusDataFs
 * countFS    - (3a.1) - Integer countRecordsOnFileSystem - updated onWrite, 
 *                before write (Read, Write into old file name, 
 *                after write Files.move to newFileName
 *     - (3a.1) - Integer volumeNumber - update onWrite, before
 *                write = ifLimit ? update : none
 *
 * @author wladimirowichbiaran
 */
public class ThWordStatusDataFs {
    private final Long timeCreation;
    private final UUID objectLabel;
    /**
     * ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Long>>
     * <keyPointFlowDataFs, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Integer>> poolStatusDataFs;
    
    public ThWordStatusDataFs(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusDataFs = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Integer>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    private ConcurrentSkipListMap<Integer, Integer> getStatusDataFsForKeyPointFlow(final UUID keyPointFlowDataFs){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, Integer> getStatusDataFsFormPool;
        try{
            inputedVal = (UUID) keyPointFlowDataFs;
            getStatusDataFsFormPool = this.poolStatusDataFs.get(inputedVal);
            if( getStatusDataFsFormPool == null ){
                throw new IllegalStateException(ThWordStatusDataFs.class.getCanonicalName()
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
        ConcurrentSkipListMap<Integer, Integer> getRemovedStatusDataFsFormPool;
        try{
            inputedVal = (UUID) keyPointFlowDataFs;
            getRemovedStatusDataFsFormPool = (ConcurrentSkipListMap<Integer, Integer>) this.poolStatusDataFs.remove(inputedVal);
            if( getRemovedStatusDataFsFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Integer> itemOfPoint : getRemovedStatusDataFsFormPool.entrySet() ){
                Integer removedKey = itemOfPoint.getKey();
                Integer removedVal = getRemovedStatusDataFsFormPool.remove(removedKey);
                removedVal = null;
                removedKey = null;
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
     * <ul>
     * <li>0 - countRecordsOnFileSystem
     * <li>1 - volumeNumber
     * </ul>
     * @param keyPointFlowDataFsInputed
     * @param paramNumber
     * @return 
     * @throw IllegalStateException is keyPointFlowDataFsInputed not exist
     */
    protected Integer getValueForFlowPointByNumber(
            final UUID keyPointFlowDataFsInputed, 
            final Integer paramNumber){
        ConcurrentSkipListMap<Integer, Integer> getListValues;
        Integer returnedParamValue;
        UUID keyPointFlowDataFsFunc;
        try{
            keyPointFlowDataFsFunc = (UUID) keyPointFlowDataFsInputed;
            returnedParamValue = (Integer) paramNumber;
            if( isStatusDataFsNotExist(keyPointFlowDataFsFunc) ){
                throw new IllegalStateException(ThWordStatusDataFs.class.getCanonicalName()
                        + " not exist values for UUID "
                        + keyPointFlowDataFsFunc.toString()
                );
            }
            getListValues = this.poolStatusDataFs.get(keyPointFlowDataFsFunc);
            Integer paramCodeByNumber = getParamCodeByNumber(returnedParamValue);
            Integer getParamForReturn = getListValues.get(paramCodeByNumber);
            return new Integer(getParamForReturn.intValue());
        } finally {
            keyPointFlowDataFsFunc = null;
            getListValues = null;
        }
    }
    /**
     * 
     * @param keyPointFlowDataFsInputed 
     */
    protected void createStructureParamsDataFs(
                        final UUID keyPointFlowDataFsInputed){
        ConcurrentSkipListMap<Integer, Integer> countDataFs;
        UUID keyPointFlowDataFsFunc;
        try{
            keyPointFlowDataFsFunc = (UUID) keyPointFlowDataFsInputed;
            if( isStatusDataFsNotExist(keyPointFlowDataFsFunc) ){
                countDataFs = setInitParamDataFs();
                this.poolStatusDataFs.put(keyPointFlowDataFsFunc, countDataFs);
            }
        } finally {
            countDataFs = null;
            keyPointFlowDataFsFunc = null;
        }
    }
    /**
     * 
     * @return 
     */
    private ConcurrentSkipListMap<Integer, Integer> setInitParamDataFs(){
        ConcurrentSkipListMap<Integer, Integer> returnedHashMap;
        Integer paramCodeByNumber;
        Integer countParamsDataFsForSet;
        Integer idx;
        try {
            returnedHashMap = new ConcurrentSkipListMap<Integer, Integer>();
            countParamsDataFsForSet = getParamCount();
            for(idx = 0; idx < countParamsDataFsForSet; idx++ ){
                paramCodeByNumber = getParamCodeByNumber(idx);
                returnedHashMap.put(paramCodeByNumber, 0);
            }
            return returnedHashMap;
        } finally {
            idx = null;
            paramCodeByNumber = null;
            countParamsDataFsForSet = null;
            returnedHashMap = null;
        }
    }
    /**
     * <ul>
     * <li>0 - countRecordsOnFileSystem
     * <li>1 - volumeNumber
     * </ul> 
     * @param changedKeyPointFlowDataFs
     * @param paramNumber
     * @param changedVal 
     * 
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    protected void changeParamValByNumber(final UUID changedKeyPointFlowDataFs, final Integer paramNumber, final Integer changedVal){
        UUID changedKeyPointFlowDataFsFunc;
        Integer paramNumberFunc;
        Integer changedValFunc;
        Integer paramCodeByNumber;
        ConcurrentSkipListMap<Integer, Integer> fromCurrentFlow;
        try{
            changedKeyPointFlowDataFsFunc = (UUID) changedKeyPointFlowDataFs;
            validateCountParams(changedKeyPointFlowDataFsFunc);
            paramNumberFunc = (Integer) paramNumber;
            changedValFunc = (Integer) changedVal;
            fromCurrentFlow = (ConcurrentSkipListMap<Integer, Integer>) this.poolStatusDataFs.get(changedKeyPointFlowDataFsFunc);
            paramCodeByNumber = (Integer) getParamCodeByNumber(paramNumberFunc);
            fromCurrentFlow.put(paramCodeByNumber, changedValFunc);
            this.poolStatusDataFs.put(changedKeyPointFlowDataFsFunc, fromCurrentFlow);
        } finally {
            changedKeyPointFlowDataFsFunc = null;
            paramNumberFunc = null;
            changedValFunc = null;
            paramCodeByNumber = null;
            fromCurrentFlow = null;
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
    private Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = getParamNames();
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusDataFs.class.getCanonicalName() 
                                + " parameters of flow statusDataFs in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusDataFs.class.getCanonicalName() 
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
                throw new IllegalArgumentException(ThWordStatusDataFs.class.getCanonicalName() 
                                + " parameters of flow statusDataFs in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusDataFs.class.getCanonicalName() 
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
        UUID keyPointFlowDataFsFunc;
        ConcurrentSkipListMap<Integer, Integer> statusDataFsForKeyPointFlow;
        Integer sizeRec;
        Integer paramCount;
        Integer idxParam;
        Integer paramCodeByNumber;
        String paramNameByNumber;
        
        try {
            keyPointFlowDataFsFunc = (UUID) keyPointFlowDataFs;
            if( keyPointFlowDataFsFunc == null ){
                throw new NullPointerException(ThWordStatusDataFs.class.getCanonicalName() 
                        + " need point flow uuid, argument for validate is null");
            }
            if( !isStatusDataFsNotExist(keyPointFlowDataFsFunc) ){
                
                statusDataFsForKeyPointFlow = (ConcurrentSkipListMap<Integer, Integer>) getStatusDataFsForKeyPointFlow(keyPointFlowDataFsFunc);
                sizeRec = (Integer) statusDataFsForKeyPointFlow.size();
                paramCount = (Integer) getParamCount();
                if( sizeRec != paramCount ){
                    
                    throw new IllegalArgumentException(ThWordStatusDataFs.class.getCanonicalName() 
                            + " parameters of flow statusDataFs in Word is not valid, "
                            + "count records " + sizeRec + " not equal " + paramCount);
                }
                
                for(idxParam = 0; idxParam < paramCount; idxParam++ ){
                    
                    paramCodeByNumber = getParamCodeByNumber(idxParam);
                    if( !statusDataFsForKeyPointFlow.containsKey(paramCodeByNumber) ){
                        
                        paramNameByNumber = getParamNameByNumber(idxParam);
                        throw new IllegalArgumentException(ThWordStatusDataFs.class.getCanonicalName() 
                            + " parameter "
                            + " for name: " + paramNameByNumber
                            + " in inputed data for set into flow statusDataFs not exist");
                    }
                }
            }
            
        } finally {
            sizeRec = null;
            paramCount = null;
            idxParam = null;
            statusDataFsForKeyPointFlow = null;
            keyPointFlowDataFsFunc = null;
            paramCodeByNumber = null;
            paramNameByNumber = null;
        }
    }
}
