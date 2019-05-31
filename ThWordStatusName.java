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
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, String>> poolStatusName;
    
    ThWordStatusName(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusName = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, String>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    private ConcurrentSkipListMap<Integer, String> getStatusNameForKeyPointFlow(final UUID keyPointFlowName){
        UUID inputedVal;
        ConcurrentSkipListMap<Integer, String> getStatusNameFormPool;
        try{
            inputedVal = (UUID) keyPointFlowName;
            getStatusNameFormPool = (ConcurrentSkipListMap<Integer, String>) this.poolStatusName.get(inputedVal);
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
        ConcurrentSkipListMap<Integer, String> getRemovedStatusNameFormPool;
        try{
            inputedVal = (UUID) keyPointFlowName;
            getRemovedStatusNameFormPool = (ConcurrentSkipListMap<Integer, String>) this.poolStatusName.remove(inputedVal);
            if( getRemovedStatusNameFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, String> itemOfPoint : getRemovedStatusNameFormPool.entrySet() ){
                Integer removedKey = itemOfPoint.getKey();
                String removedVal = getRemovedStatusNameFormPool.remove(removedKey);
                removedVal = null;
                removedKey = null;
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
     * @param keyPointFlowNameInputed
     * @param paramNumber
     * @return 
     * @throw IllegalStateException is keyPointFlowNameInputed not exist
     */
    protected String getValueForFlowPointByNumber(
            final UUID keyPointFlowDataFsInputed, 
            final Integer paramNumber){
        ConcurrentSkipListMap<Integer, String> getListValues;
        Integer returnedParamValue;
        UUID keyPointFlowNameFunc;
        try{
            keyPointFlowNameFunc = (UUID) keyPointFlowDataFsInputed;
            returnedParamValue = (Integer) paramNumber;
            if( isStatusNameNotExist(keyPointFlowNameFunc) ){
                throw new IllegalStateException(ThWordStatusDataFs.class.getCanonicalName()
                        + " not exist values for UUID "
                        + keyPointFlowNameFunc.toString()
                );
            }
            getListValues = this.poolStatusName.get(keyPointFlowNameFunc);
            return new String(getListValues.get(returnedParamValue));
        } finally {
            keyPointFlowNameFunc = null;
            getListValues = null;
        }
    }
    /**
     * 
     * @param keyPointFlowNameInputed 
     */
    protected void createStructureParamsName(
                        final UUID keyPointFlowNameInputed){
        ConcurrentSkipListMap<Integer, String> countName;
        UUID keyPointFlowNameFunc;
        try{
            keyPointFlowNameFunc = (UUID) keyPointFlowNameInputed;
            if( isStatusNameNotExist(keyPointFlowNameFunc) ){
                countName = setInitParamName();
                this.poolStatusName.put(keyPointFlowNameFunc, countName);
            }
        } finally {
            countName = null;
            keyPointFlowNameFunc = null;
        }
    }
    /**
     * Create, set all values to Boolean.FALSE
     * @return ConcurrentSkipListMap<Integer, String>
     *                          <ParamCode, Value>
     * @see ThWordStatusName#getParamCodeByNumber(int) 
     */
    private ConcurrentSkipListMap<Integer, String> setInitParamName(){
        ConcurrentSkipListMap<Integer, String> returnedHashMap;
        Integer paramCodeByNumber;
        Integer countParamsNameForSet;
        Integer idx;
        try {
            returnedHashMap = new ConcurrentSkipListMap<Integer, String>();
            countParamsNameForSet = getParamCount();
            for(idx = 0; idx < countParamsNameForSet; idx++ ){
                paramCodeByNumber = getParamCodeByNumber(idx);
                returnedHashMap.put(paramCodeByNumber, "undefined");
                idx++;
            }

            return returnedHashMap;
        } finally {
            idx = null;
            paramCodeByNumber = null;
            countParamsNameForSet = null;
            returnedHashMap = null;
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
     * @param changedKeyPointFlowName
     * @param paramNumber
     * @param changedVal 
     * 
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds
     */
    protected void changeParamValByNumber(final UUID changedKeyPointFlowName, final Integer paramNumber, final String changedVal){
        UUID changedKeyPointFlowNameFunc;
        Integer paramNumberFunc;
        String changedValFunc;
        Integer paramCodeByNumber;
        ConcurrentSkipListMap<Integer, String> fromCurrentFlow;
        try{
            changedKeyPointFlowNameFunc = (UUID) changedKeyPointFlowName;
            validateCountParams(changedKeyPointFlowNameFunc);
            paramNumberFunc = (Integer) paramNumber;
            changedValFunc = (String) changedVal;
            fromCurrentFlow = (ConcurrentSkipListMap<Integer, String>) this.poolStatusName.get(changedKeyPointFlowNameFunc);
            paramCodeByNumber = (Integer) getParamCodeByNumber(paramNumberFunc);
            fromCurrentFlow.put(paramCodeByNumber, changedValFunc);
            this.poolStatusName.put(changedKeyPointFlowNameFunc, fromCurrentFlow);
        } finally {
            changedKeyPointFlowNameFunc = null;
            paramNumberFunc = null;
            changedValFunc = null;
            paramCodeByNumber = null;
            fromCurrentFlow = null;
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
    private Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = getParamNames();
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
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
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam + ", count parameters: " 
                                + paramNames.length);
            }
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
        UUID keyPointFlowNameFunc;
        ConcurrentSkipListMap<Integer, String> statusNameForKeyPointFlow;
        Integer sizeRec;
        Integer paramCount;
        Integer idxParam;
        Integer paramCodeByNumber;
        String paramNameByNumber;
        
        try {
            keyPointFlowNameFunc = (UUID) keyPointFlowName;
            if( !isStatusNameNotExist(keyPointFlowNameFunc) ){
                
                statusNameForKeyPointFlow = (ConcurrentSkipListMap<Integer, String>) getStatusNameForKeyPointFlow(keyPointFlowNameFunc);
                sizeRec = (Integer) statusNameForKeyPointFlow.size();
                paramCount = (Integer) getParamCount();
                if( sizeRec != paramCount ){
                    
                    throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameters of flow statusName in Word is not valid, "
                            + "count records " + sizeRec + " not equal " + paramCount);
                }
                
                for(idxParam = 0; idxParam < paramCount; idxParam++ ){
                    
                    paramCodeByNumber = getParamCodeByNumber(idxParam);
                    if( !statusNameForKeyPointFlow.containsKey(paramCodeByNumber) ){
                        
                        paramNameByNumber = getParamNameByNumber(idxParam);
                        throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                            + " parameter "
                            + " for name: " + paramNameByNumber
                            + " in inputed data for set into flow statusName not exist");
                    }
                    idxParam++;
                }
            }
            
        } finally {
            sizeRec = null;
            paramCount = null;
            idxParam = null;
            statusNameForKeyPointFlow = null;
            keyPointFlowNameFunc = null;
            paramCodeByNumber = null;
            paramNameByNumber = null;
        }
    }
}
