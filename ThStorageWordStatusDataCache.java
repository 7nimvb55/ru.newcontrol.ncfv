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
 * ThStorageWordStatusDataCache
 * countTMP   - (3a.4) - Integer currentInCache 
 *  - records count, need when 
 *                get job for write for example:
 *                fromJobToWriteDataSize + countRecordsOnFileSystem + 
 *                currentInCache = resultNowData < indexSystemLimitOnStorage
 *                => readFormFileSystem -> summaryReadedCacheFromJob ->
 *                setNew VolumeNumber -> setNew in countRecordsOnFileSystem
 *                -> sendJobForWriter about writeMove -> setNew NewFileName
 *                -> writeToFileSystem in CurrentFileName -> moveTo 
 *                NewFileName (data from sendedJobForWriter)
 *                = resultNowData > indexSystemLimitOnStorage => 
 *                I. - readFromFileSystem, summ...
 *                II. - resultNowData - indexSystemLimitOnStorage -> toCache
 *                III. - sendJobForWriter, update HashMap data ...
 *                = resultNowData == indexSystemLimitOnStorage =>
 *                I. - readFromFileSystem...
 *                II. - resultNowData
 *                III. - sendJobForWriter, update HashMap data ...
 *     - (3a.4) - Integer currentInCacheReaded set when data readed from
 *                  storage index data file
 *     - (3a.4) - Integer addNeedToFileSystemLimit 
 * - exist in data file
 *                records size => indexSystemLimitOnStorage - sizeFormFileName
 *                @todo when data read need calculate name and readed data size
 *     - (3a.4) - Integer indexSystemLimitOnStorage 
 * - limit from constants
 * 
 * @author wladimirowichbiaran
 */
public class ThStorageWordStatusDataCache {
    /**
     * ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Long>>
     * <keyPointFlowDataCache, <lastAccessNanotime.hashCode(), Long Value>>
     *                        <countDataUseIterationsSummary.hashCode(), Long Value>
     */
    private ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> poolStatusDataCache;
    
    ThStorageWordStatusDataCache(){
        this.poolStatusDataCache = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>>();
    }
    /**
     * 
     * @param keyPointFlowActivity
     * @return 
     * @throws IllegalStateException
     */
    protected ConcurrentHashMap<Integer, Integer> getStatusDataCacheForKeyPointFlow(final UUID keyPointFlowDataCache){
        UUID inputedVal;
        ConcurrentHashMap<Integer, Integer> getStatusDataCacheFormPool;
        try{
            inputedVal = (UUID) keyPointFlowDataCache;
            getStatusDataCacheFormPool = this.poolStatusDataCache.get(inputedVal);
            if( getStatusDataCacheFormPool == null ){
                throw new IllegalStateException(ThStorageWordStatusDataCache.class.getCanonicalName()
                + " not exist record in list for "
                + inputedVal.toString() + " key point flow");
            }
            /**
             * @todo
             * update access time for point flow if in one point of all flow get
             * increment 
             */
            return getStatusDataCacheFormPool;
        } finally {
            inputedVal = null;
            getStatusDataCacheFormPool = null;
        }
    }

    /**
     * not exist bus
     * @param typeWordByDetectedCodePoint
     * @return 
     */
    protected Boolean isStatusDataCacheNotExist(final UUID keyPointFlowDataCache){
        UUID inputedVal;
        try{
            inputedVal = (UUID) keyPointFlowDataCache;
            if( !this.poolStatusDataCache.containsKey(inputedVal) ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            inputedVal = null;
        }
    }
    /**
     * 
     * @param currentInCache
     * @param addNeedToFileSystemLimit
     * @param indexSystemLimitOnStorage
     * @return lvl(4, 3a.4) ready for put in list lvl(3)
     */
    protected ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> createStructureParamsCountTmp(
                        final UUID keyPointFlowDataCache,
                        final Integer currentInCache,
                        final Integer currentInCacheReaded,
                        final Integer addNeedToFileSystemLimit,
                        final Integer indexSystemLimitOnStorage){
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>> returnedParams;
        ConcurrentHashMap<Integer, Integer> countTmp;
        UUID keyPointFlowDataCacheCountTmp;
        try{
            keyPointFlowDataCacheCountTmp = keyPointFlowDataCache;
            countTmp = setInParamCountTMP(
                        currentInCache,
                        currentInCacheReaded,
                        addNeedToFileSystemLimit,
                        indexSystemLimitOnStorage);
            returnedParams = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, Integer>>();
            this.poolStatusDataCache.put(keyPointFlowDataCacheCountTmp, countTmp);
            returnedParams.put(keyPointFlowDataCacheCountTmp , countTmp);
            return returnedParams;
        } finally {
            returnedParams = null;
            countTmp = null;
            keyPointFlowDataCacheCountTmp = null;
        }
    }
    /**
     * 
     * @param currentInCache
     * @param currentInCacheReaded
     * @param addNeedToFileSystemLimit
     * @param indexSystemLimitOnStorage
     * @return lvl(3a.4)
     */
    protected ConcurrentHashMap<Integer, Integer> setInParamCountTMP(
                        final Integer currentInCache,
                        final Integer currentInCacheReaded,
                        final Integer addNeedToFileSystemLimit,
                        final Integer indexSystemLimitOnStorage){
        ConcurrentHashMap<Integer, Integer> returnedHashMap;
        Integer defaultInCache;
        Integer defaultInCacheReaded;
        Integer defaultNeedToFileSystemLimit;
        Integer defaultIndexSystemLimitOnStorage;
        try {
            defaultInCache = currentInCache;
            if( defaultInCache < 0 ){
                defaultInCache = 0;
            }
            defaultInCacheReaded = currentInCacheReaded;
            if( defaultInCacheReaded < 0 ){
                defaultInCacheReaded = 0;
            }
            defaultNeedToFileSystemLimit = addNeedToFileSystemLimit;
            if( defaultNeedToFileSystemLimit < 0 ){
                defaultNeedToFileSystemLimit = 0;
            }
            defaultIndexSystemLimitOnStorage = indexSystemLimitOnStorage;
            if( defaultIndexSystemLimitOnStorage < 0 ){
                defaultIndexSystemLimitOnStorage = 0;
            }
            returnedHashMap = new ConcurrentHashMap<Integer, Integer>();
            //currentInCache - 322802084
            returnedHashMap.put(322802084, defaultInCache);
            //currentInCacheReaded - -835384455
            returnedHashMap.put(-835384455, defaultInCacheReaded);
            //addNeedToFileSystemLimit - 1443203998
            returnedHashMap.put(1443203998, defaultNeedToFileSystemLimit);
            //indexSystemLimitOnStorage - 585177634
            returnedHashMap.put(585177634, defaultIndexSystemLimitOnStorage);
            return returnedHashMap;
        } finally {
            returnedHashMap = null;
            defaultInCache = null;
            defaultInCacheReaded = null;
            defaultNeedToFileSystemLimit = null;
            defaultIndexSystemLimitOnStorage = null;
        }
    }
    /**
     * 
     * @param keyPointFlowDataCache
     * @throw IllegalArgumentException if count of parameters or his
     * names not equal concept
     */
    
    protected void validateCountParams(final UUID keyPointFlowDataCache){
        ConcurrentHashMap<Integer, Integer> statusDataCacheForKeyPointFlow;
        UUID keyPointFlowDataCacheFunc;
        Integer countThStorageWordStatusDataCacheCurrentInCache;
        Integer countThStorageWordStatusDataCacheCurrentInCacheReaded;
        Integer countThStorageWordStatusAddNeedToFileSystemLimit;
        Integer countThStorageWordStatusIndexSystemLimitOnStorage;
        Integer countSummaryOfParameters;
        try {
            keyPointFlowDataCacheFunc = (UUID) keyPointFlowDataCache;
            if( !isStatusDataCacheNotExist(keyPointFlowDataCacheFunc) ){
                statusDataCacheForKeyPointFlow = getStatusDataCacheForKeyPointFlow(keyPointFlowDataCacheFunc);
                countSummaryOfParameters = 0;
                countThStorageWordStatusDataCacheCurrentInCache = 0;
                countThStorageWordStatusDataCacheCurrentInCacheReaded = 0;
                countThStorageWordStatusAddNeedToFileSystemLimit = 0;
                countThStorageWordStatusIndexSystemLimitOnStorage = 0;
                for(Map.Entry<Integer, Integer> itemOfLong: statusDataCacheForKeyPointFlow.entrySet()){
                    countSummaryOfParameters++;
                    switch ( itemOfLong.getKey() ) {
                        case 322802084:
                            countThStorageWordStatusDataCacheCurrentInCache++;
                            continue;
                        case -835384455:
                            countThStorageWordStatusDataCacheCurrentInCacheReaded++;
                            continue;    
                        case 1443203998:
                            countThStorageWordStatusAddNeedToFileSystemLimit++;
                            continue;
                        case 585177634:
                            countThStorageWordStatusIndexSystemLimitOnStorage++;
                            continue;
                    }
                    new IllegalArgumentException(ThStorageWordStatusDataCache.class.getCanonicalName() 
                            + " parameters of flow statusDataCache in StorageWord is not valid, has more values");
                }
                if( countSummaryOfParameters != 4 ){
                    new IllegalArgumentException(ThStorageWordStatusDataCache.class.getCanonicalName() 
                            + " parameters of flow statusDataCache in StorageWord is not valid, "
                            + "count records not equal three");
                }
                if( countThStorageWordStatusDataCacheCurrentInCache != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusDataCache.class.getCanonicalName() 
                            + " parameters of flow statusDataCache in StorageWord is not valid, "
                            + "count records for CurrentInCache not equal one");
                }
                if( countThStorageWordStatusDataCacheCurrentInCacheReaded != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusDataCache.class.getCanonicalName() 
                            + " parameters of flow statusDataCache in StorageWord is not valid, "
                            + "count records for CurrentInCacheReaded not equal one");
                }
                if( countThStorageWordStatusAddNeedToFileSystemLimit != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusDataCache.class.getCanonicalName() 
                            + " parameters of flow statusDataCache in StorageWord is not valid, "
                            + "count records for AddNeedToFileSystemLimit not equal one");
                }
                if( countThStorageWordStatusIndexSystemLimitOnStorage != 1 ){
                    new IllegalArgumentException(ThStorageWordStatusDataCache.class.getCanonicalName() 
                            + " parameters of flow statusDataCache in StorageWord is not valid, "
                            + "count records for IndexSystemLimitOnStorage not equal one");
                }
            }
        } finally {
            statusDataCacheForKeyPointFlow = null;
            keyPointFlowDataCacheFunc = null;
            countThStorageWordStatusDataCacheCurrentInCache = null;
            countThStorageWordStatusDataCacheCurrentInCacheReaded = null;
            countThStorageWordStatusAddNeedToFileSystemLimit = null;
            countThStorageWordStatusIndexSystemLimitOnStorage = null;
            countSummaryOfParameters = null;
        }
    }
}
