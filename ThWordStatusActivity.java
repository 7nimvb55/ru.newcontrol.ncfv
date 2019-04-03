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
     * ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>
     * <keyPointFlowActivity, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>> poolStatusActivity;
    
    ThWordStatusActivity(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.poolStatusActivity = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    protected ConcurrentHashMap<Integer, Long> getStatusActivityForKeyPointFlow(final UUID keyPointFlowActivity){
        UUID inputedVal;
        ConcurrentHashMap<Integer, Long> getStatusActivityFormPool;
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
        ConcurrentHashMap<Integer, Long> getRemovedStatusActivityFormPool;
        try{
            inputedVal = (UUID) keyPointFlowActivity;
            getRemovedStatusActivityFormPool = (ConcurrentHashMap<Integer, Long>) this.poolStatusActivity.remove(inputedVal);
            if( getRemovedStatusActivityFormPool == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, Long> itemOfPoint : getRemovedStatusActivityFormPool.entrySet() ){
                Long remove = getRemovedStatusActivityFormPool.remove(itemOfPoint.getKey());
                Long [] remStrVal = {remove};
                remStrVal = null;
                Integer [] remIntKey = {itemOfPoint.getKey()};
                remIntKey = null;
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
     * create new record for keyPointFlowActivity, add to list
     * ThStorageWordStatistic
     * timeUSE    
     *     - (3a.3) - Long lastAccessNanotime - update onWrite, before 
     *                write
     *     - (3a.3) - Long countDataUseIterationsSummary - update onWrite, 
     *                before write, count++ sended jobWrite
     * @param keyPointFlowActivity
     * @param countDataUseIterationsSummary
     * @return
     * ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>
     * <keyPointFlowActivity, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>> createAddToListParamsTimeUse(
            final UUID keyPointFlowActivity,
            final long countDataUseIterationsSummary){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>> returnedParams;
        ConcurrentHashMap<Integer, Long> timeUse;
        UUID keyPointFlowActivityTimeUse;
        try{
            keyPointFlowActivityTimeUse = keyPointFlowActivity;
            timeUse = setInParamTimeUse(countDataUseIterationsSummary);
            
            returnedParams = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>();
            this.poolStatusActivity.put(keyPointFlowActivityTimeUse, timeUse);
            returnedParams.put(keyPointFlowActivityTimeUse, timeUse);
            return returnedParams;
        } finally {
            returnedParams = null;
            timeUse = null;
            keyPointFlowActivityTimeUse = null;
        }
    }
    /**
     * used in createStructureParamsTimeUse(
     *       final UUID keyPointFlowActivity,
     *       final long countDataUseIterationsSummary)
     * 
     * @param countDataUseIterationsSummary
     * @return ThStorageWordStatistic lvl(3a.3)
     * ConcurrentHashMap<Integer, Long>
     *      <lastAccessNanotime.hashCode(), Long Value>
     *      <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    protected ConcurrentHashMap<Integer, Long> setInParamTimeUse(
            final long countDataUseIterationsSummary){
        ConcurrentHashMap<Integer, Long> returnedHashMap;
        Long countIterations;
        try {
            returnedHashMap = new ConcurrentHashMap<Integer, Long>();
            countIterations = countDataUseIterationsSummary;
            if( countIterations < 0L ){
                countIterations = 0L;
            }
            //lastAccessNanotime - -1553995461
            long nanoTime = System.nanoTime();
            returnedHashMap.put(-1553995461, nanoTime);
            //countDataUseIterationsSummary - 1445275074
            returnedHashMap.put(1445275074, countIterations);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            countIterations = null;
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
    protected Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = getParamNames();
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
        ConcurrentHashMap<Integer, Long> statusActivityForKeyPointFlow;
        UUID keyPointFlowActivityFunc;
        Integer countThStorageWordStatusActivityLastAccessNanotime;
        Integer countThStorageWordStatusActivityDataUseIterationsSummary;
        Integer countSummaryOfParameters;
        try {
            keyPointFlowActivityFunc = (UUID) keyPointFlowActivity;
            if( !isStatusActivityNotExist(keyPointFlowActivityFunc) ){
                statusActivityForKeyPointFlow = getStatusActivityForKeyPointFlow(keyPointFlowActivityFunc);
                countSummaryOfParameters = 0;
                countThStorageWordStatusActivityLastAccessNanotime = 0;
                countThStorageWordStatusActivityDataUseIterationsSummary = 0;
                for(Map.Entry<Integer, Long> itemOfLong: statusActivityForKeyPointFlow.entrySet()){
                    countSummaryOfParameters++;
                    switch ( itemOfLong.getKey() ) {
                        case -1553995461:
                            countThStorageWordStatusActivityLastAccessNanotime++;
                            continue;
                        case 1445275074:
                            countThStorageWordStatusActivityDataUseIterationsSummary++;
                            continue;
                    }
                    throw new IllegalArgumentException(ThWordStatusActivity.class.getCanonicalName() 
                            + " parameters of flow statusActivity in StorageWord is not valid, has more values");
                }
                if( countSummaryOfParameters != 2 ){
                    throw new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters of flow statusActivity in StorageWord is not valid, "
                            + "count records not equal two");
                }
                if( countThStorageWordStatusActivityLastAccessNanotime != 1 ){
                    throw new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters of flow statusActivity in StorageWord is not valid, "
                            + "count records for lastAccessNanotime not equal one");
                }
                if( countThStorageWordStatusActivityDataUseIterationsSummary != 1 ){
                    throw new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters of flow statusActivity in StorageWord is not valid, "
                            + "count records for countDataUseIterationsSummary not equal one");
                }
            }
        } finally {
            statusActivityForKeyPointFlow = null;
            keyPointFlowActivityFunc = null;
            countThStorageWordStatusActivityLastAccessNanotime = null;
            countThStorageWordStatusActivityDataUseIterationsSummary = null;
            countSummaryOfParameters = null;
        }
    }
   
}
