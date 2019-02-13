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
public class ThStorageWordStatusActivity {

    /**
     * ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>
     * <keyPointFlowActivity, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>> poolStatusActivity;
    
    ThStorageWordStatusActivity(){
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
                throw new IllegalStateException(ThStorageWordStatusActivity.class.getCanonicalName()
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
    
}
