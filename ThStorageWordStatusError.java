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
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordStatusError {
    private final Long timeCreation;
    private final UUID objectLabel;
    /**
     * ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>
     * <keyPointFlowError, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> poolStatusError;
    
    ThStorageWordStatusError(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusError = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    protected ConcurrentHashMap<Integer, Integer> getStatusErrorForKeyPointFlow(final UUID keyPointFlowError){
        UUID inputedVal;
        ConcurrentHashMap<Integer, Integer> getStatusErrorFormPool;
        try{
            inputedVal = (UUID) keyPointFlowError;
            getStatusErrorFormPool = this.poolStatusError.get(inputedVal);
            if( getStatusErrorFormPool == null ){
                throw new IllegalStateException(ThStorageWordStatusError.class.getCanonicalName()
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
        ConcurrentHashMap<Integer, Integer> getRemovedStatusErrorFormPool;
        try{
            inputedVal = (UUID) keyPointFlowError;
            getRemovedStatusErrorFormPool = (ConcurrentHashMap<Integer, Integer>) this.poolStatusError.remove(inputedVal);
            if( getRemovedStatusErrorFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Integer> itemOfPoint : getRemovedStatusErrorFormPool.entrySet() ){
                Integer remove = getRemovedStatusErrorFormPool.remove(itemOfPoint.getKey());
                Integer [] remStrVal = {remove};
                remStrVal = null;
                Integer [] remIntKey = {itemOfPoint.getKey()};
                remIntKey = null;
            }
            getRemovedStatusErrorFormPool = null;
            return Boolean.TRUE;
        } finally {
            inputedVal = null;
            getRemovedStatusErrorFormPool = null;
        }
    }
    /**
     * not exist bus
     * @param typeWordByDetectedCodePoint
     * @return 
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
     * 
     * @param countRecords
     * @param volumeNumber
     * @return lvl(4, 3a.1) ready for put in list lvl(3)
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> createStructureParamsError(
                        final UUID keyPointFlowError){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> returnedParams;
        ConcurrentHashMap<Integer, Integer> countFs;
        UUID keyPointFlowErrorCountFs;
        try{
            keyPointFlowErrorCountFs = keyPointFlowError;
            countFs = setInParamError();
            returnedParams = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>>();
            this.poolStatusError.put(keyPointFlowErrorCountFs, countFs);
            returnedParams.put(keyPointFlowErrorCountFs , countFs);
            return returnedParams;
        } finally {
            returnedParams = null;
            countFs = null;
            keyPointFlowErrorCountFs = null;
        }
    }
    /**
     * @todo on init set to 0
     * @param countRecords
     * @param volumeNumber
     * @return lvl(3a.1)
     */
    protected ConcurrentHashMap<Integer, Integer> setInParamError(){
        ConcurrentHashMap<Integer, Integer> returnedHashMap;
        try {
            returnedHashMap = new ConcurrentHashMap<Integer, Integer>();
            String[] paramNames = getParamNames();
            int idx = 0;
            for(String itemError : paramNames){
                Integer paramCodeByNumber = getParamCodeByNumber(idx);
                returnedHashMap.put(paramCodeByNumber, 0);
                idx++;
            }

            return returnedHashMap;
        } finally {
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
    protected Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = getParamNames();
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThStorageWordStatusError.class.getCanonicalName() 
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
                throw new IllegalArgumentException(ThStorageWordStatusError.class.getCanonicalName() 
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
     * 
     * @param keyPointFlowError
     * 
     * @throw IllegalArgumentException if count of parameters or his
     * names not equal concept
     */
    
    protected void validateCountParams(final UUID keyPointFlowError){
        ConcurrentHashMap<Integer, Integer> statusErrorForKeyPointFlow;
        UUID keyPointFlowErrorFunc;
        String paramNameByNumber;
        try {
            keyPointFlowErrorFunc = (UUID) keyPointFlowError;
            if( !isStatusErrorNotExist(keyPointFlowErrorFunc) ){
                statusErrorForKeyPointFlow = getStatusErrorForKeyPointFlow(keyPointFlowErrorFunc);
                int sizeRec = statusErrorForKeyPointFlow.size();
                int paramCount = getParamCount();
                if( sizeRec != paramCount ){
                    new IllegalArgumentException(ThStorageWordStatusError.class.getCanonicalName() 
                            + " parameters of flow statusError in StorageWord is not valid, "
                            + "count records " + sizeRec + " not equal " + paramCount);
                }
                int idxParam = 0;
                while( idxParam < paramCount ){
                    int paramCodeByNumber = getParamCodeByNumber(idxParam);
                    if( statusErrorForKeyPointFlow.containsKey(paramCodeByNumber) ){
                        paramNameByNumber = getParamNameByNumber(idxParam);
                        new IllegalArgumentException(ThStorageWordStatusError.class.getCanonicalName() 
                            + " parameters of flow statusError in StorageWord is not valid, "
                            + "count records for " + paramNameByNumber + " not equal one");
                    }
                    idxParam++;
                }

            }
        } finally {
            statusErrorForKeyPointFlow = null;
            keyPointFlowErrorFunc = null;
            paramNameByNumber = null;
        }
    }
}
