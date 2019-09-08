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
 * ThStorageWordStatusActivity
 * timeUSE    
 *     - (3a.3) - Long lastAccessNanotime - update onWrite, before 
 *                write
 *     - (3a.3) - Long countDataUseIterationsSummary - update onWrite, 
 *                before write, count++ sended jobWrite
 * @author wladimirowichbiaran
 */
public class ThWordStatusActivity {
    private final Long timeCreation;
    private final UUID objectLabel;
    /**
     * ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Long>>
     * <keyPointFlowActivity, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Long>> poolStatusActivity;
    
    ThWordStatusActivity(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusActivity = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Long>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    private ConcurrentSkipListMap<Integer, Long> getStatusActivityForKeyPointFlow(final UUID keyPointFlowActivity){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, Long> getStatusActivityFormPool;
        try{
            inputedVal = (UUID) keyPointFlowActivity;
            getStatusActivityFormPool = this.poolStatusActivity.get(inputedVal);
            if( getStatusActivityFormPool == null ){
                throw new IllegalStateException(ThWordStatusActivity.class.getCanonicalName()
                + " not exist record in list for "
                + inputedVal.toString() + " key point flow");
            }
            /**
             * @todo
             * update access time for point flow if in one point of all flow get
             * increment 
             */
            return getStatusActivityFormPool;
        } finally {
            inputedVal = null;
            getStatusActivityFormPool = null;
        }
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return true if found and delete data
     */
    protected Boolean removeStatusActivityForKeyPointFlow(final UUID keyPointFlowActivity){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, Long> getRemovedStatusActivityFormPool;
        try{
            inputedVal = (UUID) keyPointFlowActivity;
            getRemovedStatusActivityFormPool = (ConcurrentSkipListMap<Integer, Long>) this.poolStatusActivity.remove(inputedVal);
            if( getRemovedStatusActivityFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Long> itemOfPoint : getRemovedStatusActivityFormPool.entrySet() ){
                Integer removedKey = itemOfPoint.getKey();
                Long removedVal = getRemovedStatusActivityFormPool.remove(removedKey);
                removedVal = null;
                removedKey = null;
            }
            getRemovedStatusActivityFormPool = null;
            return Boolean.TRUE;
        } finally {
            inputedVal = null;
            getRemovedStatusActivityFormPool = null;
        }
    }
    /**
     * not exist bus
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected Boolean isStatusActivityNotExist(final UUID keyPointFlowActivity){
        UUID inputedVal;
        try{
            inputedVal = (UUID) keyPointFlowActivity;
            if( !this.poolStatusActivity.containsKey(inputedVal) ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            inputedVal = null;
        }
    }
    /**
     * 
     * @param keyPointFlowActivityInputed
     * @param paramNumber
     * @return 
     * @throw IllegalStateException is keyPointFlowActivityInputed not exist
     */
    protected Long getValueForFlowPointByNumber(
            final UUID keyPointFlowDataFsInputed, 
            final Integer paramNumber){
        ConcurrentSkipListMap<Integer, Long> getListValues;
        Integer returnedParamValue;
        UUID keyPointFlowActivityFunc;
        try{
            keyPointFlowActivityFunc = (UUID) keyPointFlowDataFsInputed;
            returnedParamValue = (Integer) paramNumber;
            if( isStatusActivityNotExist(keyPointFlowActivityFunc) ){
                throw new IllegalStateException(ThWordStatusDataFs.class.getCanonicalName()
                        + " not exist values for UUID "
                        + keyPointFlowActivityFunc.toString()
                );
            }
            getListValues = this.poolStatusActivity.get(keyPointFlowActivityFunc);
            Integer paramCodeByNumber = getParamCodeByNumber(returnedParamValue);
            Long getParamForReturn = getListValues.get(paramCodeByNumber);
            return new Long(getParamForReturn.longValue());
        } finally {
            keyPointFlowActivityFunc = null;
            getListValues = null;
        }
    }
    /**
     * <ul>
     * <li>0 - lastAccessNanotime
     * <li>1 - countDataUseIterationsSummary
     * </ul>
     * @param keyPointFlowActivityInputed 
     */
    protected void createStructureParamsActivity(
                        final UUID keyPointFlowActivityInputed){
        ConcurrentSkipListMap<Integer, Long> countActivity;
        UUID keyPointFlowActivityFunc;
        try{
            keyPointFlowActivityFunc = (UUID) keyPointFlowActivityInputed;
            if( isStatusActivityNotExist(keyPointFlowActivityFunc) ){
                countActivity = setInitParamActivity();
                this.poolStatusActivity.put(keyPointFlowActivityFunc, countActivity);
            }
        } finally {
            countActivity = null;
            keyPointFlowActivityFunc = null;
        }
    }
    /**
     * 
     * @return 
     */
    private ConcurrentSkipListMap<Integer, Long> setInitParamActivity(){
        ConcurrentSkipListMap<Integer, Long> returnedHashMap;
        Integer paramCodeByNumber;
        Integer countParamsActivityForSet;
        Integer idx;
        try {
            returnedHashMap = new ConcurrentSkipListMap<Integer, Long>();
            countParamsActivityForSet = getParamCount();
            for(idx = 0; idx < countParamsActivityForSet; idx++ ){
                paramCodeByNumber = getParamCodeByNumber(idx);
                returnedHashMap.put(paramCodeByNumber, 0L);
            }

            return returnedHashMap;
        } finally {
            idx = null;
            paramCodeByNumber = null;
            countParamsActivityForSet = null;
            returnedHashMap = null;
        }
    }
    /**
     * <ul>
     * <li>0 - lastAccessNanotime
     * <li>1 - countDataUseIterationsSummary
     * </ul> 
     * @param changedKeyPointFlowActivity
     * @param paramNumber
     * @param changedVal 
     * 
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    protected void changeParamValByNumber(final UUID changedKeyPointFlowActivity, final Integer paramNumber, final Long changedVal){
        UUID changedKeyPointFlowActivityFunc;
        Integer paramNumberFunc;
        Long changedValFunc;
        Integer paramCodeByNumber;
        ConcurrentSkipListMap<Integer, Long> fromCurrentFlow;
        try{
            changedKeyPointFlowActivityFunc = (UUID) changedKeyPointFlowActivity;
            validateCountParams(changedKeyPointFlowActivityFunc);
            paramNumberFunc = (Integer) paramNumber;
            changedValFunc = (Long) changedVal;
            fromCurrentFlow = (ConcurrentSkipListMap<Integer, Long>) this.poolStatusActivity.get(changedKeyPointFlowActivityFunc);
            paramCodeByNumber = (Integer) getParamCodeByNumber(paramNumberFunc);
            fromCurrentFlow.put(paramCodeByNumber, changedValFunc);
            this.poolStatusActivity.put(changedKeyPointFlowActivityFunc, fromCurrentFlow);
        } finally {
            changedKeyPointFlowActivityFunc = null;
            paramNumberFunc = null;
            changedValFunc = null;
            paramCodeByNumber = null;
            fromCurrentFlow = null;
        }
    }
    /**
     * <ul>
     * <li>0 - lastAccessNanotime
     * <li>1 - countDataUseIterationsSummary
     * </ul>
     * @return 
     */
    private String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "lastAccessNanotime",
                "countDataUseIterationsSummary"
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
                throw new IllegalArgumentException(ThWordStatusActivity.class.getCanonicalName() 
                                + " parameters of flow statusActivity in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusActivity.class.getCanonicalName() 
                                + " parameters of flow statusActivity in StorageWord is not valid, "
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
                throw new IllegalArgumentException(ThWordStatusActivity.class.getCanonicalName() 
                                + " parameters of flow statusActivity in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusActivity.class.getCanonicalName() 
                                + " parameters of flow statusActivity in StorageWord is not valid, "
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
     * @param keyPointFlowActivity
     * @throw IllegalArgumentException if count of parameters or his
     * names not equal concept
     */
    
    protected void validateCountParams(final UUID keyPointFlowActivity){
        UUID keyPointFlowActivityFunc;
        ConcurrentSkipListMap<Integer, Long> statusActivityForKeyPointFlow;
        Integer sizeRec;
        Integer paramCount;
        Integer idxParam;
        Integer paramCodeByNumber;
        String paramNameByNumber;
        
        try {
            keyPointFlowActivityFunc = (UUID) keyPointFlowActivity;
            if( keyPointFlowActivityFunc == null ){
                throw new NullPointerException(ThWordStatusActivity.class.getCanonicalName() 
                        + " need point flow uuid, argument for validate is null");
            }
            if( !isStatusActivityNotExist(keyPointFlowActivityFunc) ){
                
                statusActivityForKeyPointFlow = (ConcurrentSkipListMap<Integer, Long>) getStatusActivityForKeyPointFlow(keyPointFlowActivityFunc);
                sizeRec = (Integer) statusActivityForKeyPointFlow.size();
                paramCount = (Integer) getParamCount();
                if( sizeRec != paramCount ){
                    
                    throw new IllegalArgumentException(ThWordStatusActivity.class.getCanonicalName() 
                            + " parameters of flow statusActivity in Word is not valid, "
                            + "count records " + sizeRec + " not equal " + paramCount);
                }
                
                for(idxParam = 0; idxParam < paramCount; idxParam++ ){
                    
                    paramCodeByNumber = getParamCodeByNumber(idxParam);
                    if( !statusActivityForKeyPointFlow.containsKey(paramCodeByNumber) ){
                        
                        paramNameByNumber = getParamNameByNumber(idxParam);
                        throw new IllegalArgumentException(ThWordStatusActivity.class.getCanonicalName() 
                            + " parameter "
                            + " for name: " + paramNameByNumber
                            + " in inputed data for set into flow statusActivity not exist");
                    }
                }
            }
            
        } finally {
            sizeRec = null;
            paramCount = null;
            idxParam = null;
            statusActivityForKeyPointFlow = null;
            keyPointFlowActivityFunc = null;
            paramCodeByNumber = null;
            paramNameByNumber = null;
        }
    }
   
}
