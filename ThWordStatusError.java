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

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordStatusError {
    private final Long timeCreation;
    private final UUID objectLabel;
    /**
     * ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Long>>
     * <keyPointFlowError, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Integer>> poolStatusError;
    
    ThWordStatusError(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusError = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, Integer>>();
    }
    /**
     * 
     * @param keyPointFlowError
     * @return 
     * @throws IllegalStateException when UUID not exist
     */
    private ConcurrentSkipListMap<Integer, Integer> getStatusErrorForKeyPointFlow(final UUID keyPointFlowError){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, Integer> getStatusErrorFormPool;
        try{
            inputedVal = (UUID) keyPointFlowError;
            getStatusErrorFormPool = this.poolStatusError.get(inputedVal);
            if( getStatusErrorFormPool == null ){
                throw new IllegalStateException(ThWordStatusError.class.getCanonicalName()
                + " not exist record in list for "
                + inputedVal.toString() + " key point flow");
            }
            /**
             * @todo
             * update access time for point flow if in one point of all flow get
             * increment 
             */
            return getStatusErrorFormPool;
        } finally {
            inputedVal = null;
            getStatusErrorFormPool = null;
        }
    }
    /**
     * 
     * @param keyPointFlowError
     * @return true if found and delete data
     */
    protected Boolean removeStatusErrorForKeyPointFlow(final UUID keyPointFlowError){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, Integer> getRemovedStatusErrorFormPool;
        try{
            inputedVal = (UUID) keyPointFlowError;
            getRemovedStatusErrorFormPool = (ConcurrentSkipListMap<Integer, Integer>) this.poolStatusError.remove(inputedVal);
            if( getRemovedStatusErrorFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Integer> itemOfPoint : getRemovedStatusErrorFormPool.entrySet() ){
                Integer removedKey = itemOfPoint.getKey();
                Integer removedVal = getRemovedStatusErrorFormPool.remove(removedKey);
                removedVal = null;
                removedKey = null;
            }
            getRemovedStatusErrorFormPool = null;
            return Boolean.TRUE;
        } finally {
            inputedVal = null;
            getRemovedStatusErrorFormPool = null;
        }
    }
    /**
     * not exist UUID in flow
     * @param typeWordByDetectedCodePoint
     * @return true if key not exist (not contains in flow)
     */
    protected Boolean isStatusErrorNotExist(final UUID keyPointFlowError){
        UUID inputedVal;
        try{
            inputedVal = (UUID) keyPointFlowError;
            if( !this.poolStatusError.containsKey(inputedVal) ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            inputedVal = null;
        }
    }
    /**
     * <ul>
     * <li>0 - isErrorOnWrite
     * <li>1 - isErrorOnMove
     * <li>2 - isNullOnDataInCache
     * <li>3 - isErrorOnDataInCache
     * </ul>
     * @param keyPointFlowErrorInputed
     * @param paramNumber
     * @return 
     * @throw IllegalStateException is keyPointFlowErrorInputed not exist
     */
    protected Integer getValueForFlowPointByNumber(
            final UUID keyPointFlowDataFsInputed, 
            final Integer paramNumber){
        ConcurrentSkipListMap<Integer, Integer> getListValues;
        Integer returnedParamValue;
        UUID keyPointFlowErrorFunc;
        try{
            keyPointFlowErrorFunc = (UUID) keyPointFlowDataFsInputed;
            returnedParamValue = (Integer) paramNumber;
            if( isStatusErrorNotExist(keyPointFlowErrorFunc) ){
                throw new IllegalStateException(ThWordStatusDataFs.class.getCanonicalName()
                        + " not exist values for UUID "
                        + keyPointFlowErrorFunc.toString()
                );
            }
            getListValues = this.poolStatusError.get(keyPointFlowErrorFunc);
            return new Integer(getListValues.get(returnedParamValue));
        } finally {
            keyPointFlowErrorFunc = null;
            getListValues = null;
        }
    }
    /**
     * create new structure for UUID, and set all values to 0 (zero)
     * @param keyPointFlowErrorInputed
     * @see setInitParamError()
     */
    protected void createStructureParamsError(
                        final UUID keyPointFlowErrorInputed){
        ConcurrentSkipListMap<Integer, Integer> countError;
        UUID keyPointFlowErrorFunc;
        try{
            keyPointFlowErrorFunc = (UUID) keyPointFlowErrorInputed;
            if( isStatusErrorNotExist(keyPointFlowErrorFunc) ){
                countError = setInitParamError();
                this.poolStatusError.put(keyPointFlowErrorFunc, countError);
            }
        } finally {
            countError = null;
            keyPointFlowErrorFunc = null;
        }
    }
    /**
     * 
     * @return 
     */
    private ConcurrentSkipListMap<Integer, Integer> setInitParamError(){
        ConcurrentSkipListMap<Integer, Integer> returnedHashMap;
        Integer paramCodeByNumber;
        Integer countParamsErrorForSet;
        Integer idx;
        try {
            returnedHashMap = new ConcurrentSkipListMap<Integer, Integer>();
            countParamsErrorForSet = getParamCount();
            for(idx = 0; idx < countParamsErrorForSet; idx++ ){
                paramCodeByNumber = getParamCodeByNumber(idx);
                returnedHashMap.put(paramCodeByNumber, 0);
                idx++;
            }

            return returnedHashMap;
        } finally {
            idx = null;
            paramCodeByNumber = null;
            countParamsErrorForSet = null;
            returnedHashMap = null;
        }
    }
    /**
     * <ul>
     * <li>0 - isErrorOnWrite
     * <li>1 - isErrorOnMove
     * <li>2 - isNullOnDataInCache
     * <li>3 - isErrorOnDataInCache
     * </ul>
     * @param changedKeyPointFlowError
     * @param paramNumber
     * @param changedVal
     * 
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    protected void changeParamValByNumber(final UUID changedKeyPointFlowError, final Integer paramNumber, final Integer changedVal){
        UUID changedKeyPointFlowErrorFunc;
        Integer paramNumberFunc;
        Integer changedValFunc;
        Integer paramCodeByNumber;
        ConcurrentSkipListMap<Integer, Integer> fromCurrentFlow;
        try{
            changedKeyPointFlowErrorFunc = (UUID) changedKeyPointFlowError;
            validateCountParams(changedKeyPointFlowErrorFunc);
            paramNumberFunc = (Integer) paramNumber;
            changedValFunc = (Integer) changedVal;
            fromCurrentFlow = (ConcurrentSkipListMap<Integer, Integer>) this.poolStatusError.get(changedKeyPointFlowErrorFunc);
            paramCodeByNumber = (Integer) getParamCodeByNumber(paramNumberFunc);
            fromCurrentFlow.put(paramCodeByNumber, changedValFunc);
            this.poolStatusError.put(changedKeyPointFlowErrorFunc, fromCurrentFlow);
        } finally {
            changedKeyPointFlowErrorFunc = null;
            paramNumberFunc = null;
            changedValFunc = null;
            paramCodeByNumber = null;
            fromCurrentFlow = null;
        }
    }
    /**
     * <ul>
     * <li>0 - isErrorOnWrite
     * <li>1 - isErrorOnMove
     * <li>2 - isNullOnDataInCache
     * <li>3 - isErrorOnDataInCache
     * </ul>
     * @return 
     */
    private String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "isErrorOnWrite", 
                "isErrorOnMove", 
                "isNullOnDataInCache",
                "isErrorOnDataInCache"
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
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusError in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusError in StorageWord is not valid, "
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
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusError in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusError in StorageWord is not valid, "
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
     * create, set, validate -do- for change
     * change -do- for add
     * @param keyPointFlowError
     * 
     * @throw IllegalArgumentException if count of parameters or his
     * names not equal concept
     */
    
    protected void validateCountParams(final UUID keyPointFlowError){
        UUID keyPointFlowErrorFunc;
        ConcurrentSkipListMap<Integer, Integer> statusErrorForKeyPointFlow;
        Integer sizeRec;
        Integer paramCount;
        Integer idxParam;
        Integer paramCodeByNumber;
        String paramNameByNumber;
        
        try {
            keyPointFlowErrorFunc = (UUID) keyPointFlowError;
            if( keyPointFlowErrorFunc == null ){
                throw new NullPointerException(ThWordStatusError.class.getCanonicalName() 
                        + " need point flow uuid, argument for validate is null");
            }
            if( !isStatusErrorNotExist(keyPointFlowErrorFunc) ){
                
                statusErrorForKeyPointFlow = (ConcurrentSkipListMap<Integer, Integer>) getStatusErrorForKeyPointFlow(keyPointFlowErrorFunc);
                sizeRec = (Integer) statusErrorForKeyPointFlow.size();
                paramCount = (Integer) getParamCount();
                if( sizeRec != paramCount ){
                    
                    throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                            + " parameters of flow statusError in Word is not valid, "
                            + "count records " + sizeRec + " not equal " + paramCount);
                }
                
                for(idxParam = 0; idxParam < paramCount; idxParam++ ){
                    
                    paramCodeByNumber = getParamCodeByNumber(idxParam);
                    if( !statusErrorForKeyPointFlow.containsKey(paramCodeByNumber) ){
                        
                        paramNameByNumber = getParamNameByNumber(idxParam);
                        throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                            + " parameter "
                            + " for name: " + paramNameByNumber
                            + " in inputed data for set into flow statusError not exist");
                    }
                    idxParam++;
                }
            }
            
        } finally {
            sizeRec = null;
            paramCount = null;
            idxParam = null;
            statusErrorForKeyPointFlow = null;
            keyPointFlowErrorFunc = null;
            paramCodeByNumber = null;
            paramNameByNumber = null;
        }
    }
}
