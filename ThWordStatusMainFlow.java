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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;


/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordStatusMainFlow {
    /**
     * ConcurrentSkipListMap<Integer, Integer> (<hashFieldCode, Value>)
     * hashFieldCode:
     * - Size
     * - VolumeNum
     * Search by tagFileName current VolumeNum and get his size
     * data for record size summ with writed size and compared with limit for
     * index type if need accumulate data to limit size, send it into cache
     * data structure, while volume not have limited size or time limit in nanos
     * 
     * and control to sizes for cache lists
     * 
     * This structure also for distinct word index need...
     * 
     * ConcurrentSkipListMap<Integer,     - (1) - Strorage hash value 
     * * * * * - (1) In release only for storage of Word index not apply
     * > ConcurrentSkipListMap<Integer,   - (2) - Type of word index hash value
     *   ConcurrentSkipListMap<String,    - (2a) - tagFileName.substring(0,3)
     *     ConcurrentSkipListMap<Integer, - (2b) - subString.length                            
     *     ConcurrentSkipListMap<String,  - (3) - tagFileName with hex view
     * > >   ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>
     *          <UUID MainFlow, Field.hashCode(), valueUUID>
     *          <ThWordStatusDataFs.hashCode(), recordUUID>
     *          <ThWordStatusName.hashCode(), recordUUID>
     *          <ThWordStatusActivity.hashCode(), recordUUID>
     *          <ThWordStatusDataCache.hashCode(), recordUUID>
     *          <ThWordStatusWorkers.hashCode(), recordUUID>>
     * -------------------------------------------------------------------------
     * ThWordStatusDataFs
     * countFS    - (3a.1) - Integer countRecordsOnFileSystem - updated onWrite, 
     *                before write (Read, Write into old file name, 
     *                after write Files.move to newFileName
     *     - (3a.1) - Integer volumeNumber - update onWrite, before
     *                write = ifLimit ? update : none
     * -------------------------------------------------------------------------
     * ThWordStatusName
     * namesFS    - (3a.2) - String currentFileName - full file name where read 
     *                from data
     *     - (3a.2) - String newFileName - full file name for Files.move 
     *                operation after write created when readJobDataSize
     * -------------------------------------------------------------------------
     * ThWordStatusActivity
     * timeUSE    - (3a.3) - Long lastAccessNanotime - update onWrite, before 
     *                write
     *     - (3a.3) - Long countDataUseIterationsSummary - update onWrite, 
     *                before write, count++ sended jobWrite
     * -------------------------------------------------------------------------
     * ThWordStatusDataCache
     * countTMP   - (3a.4) - Integer currentInCache - records count, need when 
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
     *     - (3a.4) - Integer addNeedToFileSystemLimit - exist in data file
     *                records size => indexSystemLimitOnStorage - sizeFormFileName
     *                @todo when data read need calculate name and readed data size
     *     - (3a.4) - Integer indexSystemLimitOnStorage - limit from constants
     * -------------------------------------------------------------------------
     * ThWordStatusWorkers
     * flagsProc  - (3a.5) - Boolean isWriteProcess - when this param init do it
     *     - (3a.5) - Boolean isReadProcess - when this param init do it
     *     - (3a.5) - Boolean isCachedData - when this param init do it
     *     - (3a.5) - Boolean isCalculatedData
     *     - (3a.5) - Boolean isUdatedDataInHashMap
     */
    private final Long timeCreation;
    private final UUID objectLabel;
    
    private ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                            ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>>> fileStoragesMap;
    
    private final ThWordCacheSk thWordCache;
    private final ThWordCacheSk thWordCacheReaded;
    
    private final ThWordStatusActivity thWordStatusActivity;
    private final ThWordStatusDataCache thWordStatusDataCache;
    private final ThWordStatusDataFs thWordStatusDataFs;
    private final ThWordStatusError thWordStatusError;
    private final ThWordStatusName thWordStatusName;
    private final ThWordStatusWorkers thWordStatusWorkers;
    /**
     * @todo StatusError
     */
    
    public ThWordStatusMainFlow() {
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        
        this.fileStoragesMap = createNewListStoragesMapEmpty();
        
        this.thWordCache = new ThWordCacheSk();
        this.thWordCacheReaded = new ThWordCacheSk();
        this.thWordStatusActivity = new ThWordStatusActivity();
        this.thWordStatusDataCache = new ThWordStatusDataCache();
        this.thWordStatusDataFs = new ThWordStatusDataFs();
        this.thWordStatusError = new ThWordStatusError();
        this.thWordStatusName = new ThWordStatusName();
        this.thWordStatusWorkers = new ThWordStatusWorkers();
        
        
    }
    /**
     * remove from MainFlow UUID, found statusUUIDs in flows and remove it
     * 
     * @todo in next iterations of releases
     *       create objects list HashMap<integer, object>
     *                          <paramCodeByNumber, object>
     *       for optimize remove operation, get from list object by code,
     *       call his remove method with UUID param
     * 
     * @param inputMainFlowUUID
     * @return true if found and delete data
     */
    protected Boolean removeAllFlowStatusByUUID(UUID inputMainFlowUUID){
        ConcurrentSkipListMap<Integer, UUID> removeMainFlowRec;
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>> valueItemLvlTypeWord;
        ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>> valueItemLvlTagFileNameLetter;
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>> valueItemLvlSubStrLength;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> valueItemLvlTagFileName;
        UUID keyMainFlow;
        Integer countParam;
        Integer paramCodeByNumber;
        Integer keyRemovedItem;
        UUID valueRemovedItem;
        UUID removedStatusUUID;
        try {
            if( this.fileStoragesMap == null ){
                return Boolean.FALSE;
            }
            for( Map.Entry<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>>> itemLvlTypeWord : this.fileStoragesMap.entrySet() ){
                valueItemLvlTypeWord = itemLvlTypeWord.getValue();
                for( Map.Entry<String, ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>> itemLvlTagFileNameLetter : valueItemLvlTypeWord.entrySet() ){
                    valueItemLvlTagFileNameLetter = itemLvlTagFileNameLetter.getValue();
                    for( Map.Entry<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>> itemLvlSubStrLength : valueItemLvlTagFileNameLetter.entrySet() ){
                        valueItemLvlSubStrLength = itemLvlSubStrLength.getValue();
                        for( Map.Entry<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>> itemLvlTagFileName : valueItemLvlSubStrLength.entrySet() ){
                            valueItemLvlTagFileName = itemLvlTagFileName.getValue();
                            for( Map.Entry<UUID, ConcurrentSkipListMap<Integer, UUID>> itemMainFlowUUID : valueItemLvlTagFileName.entrySet() ){
                                keyMainFlow = (UUID) itemMainFlowUUID.getKey();
                                removeMainFlowRec = valueItemLvlTagFileName.remove(keyMainFlow);
                                countParam = getParamCount();
                                for( int idx = 0 ; idx < countParam ; idx++ ){
                                    paramCodeByNumber = getParamCodeByNumber(idx);
                                    removedStatusUUID = removeMainFlowRec.remove(paramCodeByNumber);
                                    if( this.thWordStatusActivity.removeStatusActivityForKeyPointFlow(removedStatusUUID) ){
                                        removedStatusUUID = null;
                                        continue;
                                    }
                                    if( this.thWordStatusDataCache.removeStatusDataCacheForKeyPointFlow(removedStatusUUID) ){
                                        removedStatusUUID = null;
                                        continue;
                                    }
                                    if( this.thWordStatusDataFs.removeStatusDataFsForKeyPointFlow(removedStatusUUID) ){
                                        removedStatusUUID = null;
                                        continue;
                                    }
                                    if( this.thWordStatusError.removeStatusErrorForKeyPointFlow(removedStatusUUID) ){
                                        removedStatusUUID = null;
                                        continue;
                                    }
                                    if( this.thWordStatusName.removeStatusNameForKeyPointFlow(removedStatusUUID) ){
                                        removedStatusUUID = null;
                                        continue;
                                    }
                                    if( this.thWordStatusWorkers.removeStatusWorkersForKeyPointFlow(removedStatusUUID) ){
                                        removedStatusUUID = null;
                                        continue;
                                    }
                                    paramCodeByNumber = null;
                                }
                                removeMainFlowRec = null;
                                keyMainFlow = null;
                            }
                        }
                    }
                }
            }
            return Boolean.TRUE;
        } finally {
            valueItemLvlTypeWord = null;
            valueItemLvlTagFileNameLetter = null;
            valueItemLvlSubStrLength = null;
            valueItemLvlTagFileName = null;
            keyRemovedItem = null;
            valueRemovedItem = null;
            removeMainFlowRec = null;
            removedStatusUUID = null;
            keyMainFlow = null;
        }
    }
    protected ThWordCacheSk getWordCache(){
        return this.thWordCache;
    }
    protected ThWordCacheSk getWordCacheReaded(){
        return this.thWordCacheReaded;
    }
    private ThWordStatusActivity getWordStatusActivity(){
        return this.thWordStatusActivity;
    }
    private ThWordStatusDataCache getWordStatusDataCache(){
        return this.thWordStatusDataCache;
    }
    private ThWordStatusDataFs getWordStatusDataFs(){
        return this.thWordStatusDataFs;
    }
    private ThWordStatusError getWordStatusError(){
        return this.thWordStatusError;
    }
    private ThWordStatusName getWordStatusName(){
        return this.thWordStatusName;
    }
    private ThWordStatusWorkers getWordStatusWorkers(){
        return this.thWordStatusWorkers;
    }
    /**
     * 
     * @param typeWordInputed
     * @param tagNameInputed
     * @param mainFlowUuid
     * @param numberParameter
     * <ul>
     * <li>0 - countRecordsOnFileSystem
     * <li>1 - volumeNumber
     * </ul>
     * @return 
     */
    protected Integer getValueForValideUuuidByTypeWordHexTagNameNumberDataFs(
            final Integer typeWordInputed,
            final String tagNameInputed,
            final UUID mainFlowUuid,
            final Integer numberParameter
    ){
        Integer typeWordInputedFunc;
        UUID mainFlowUuidFunc;
        String tagNameInputedFunc;
        Integer numberParameterFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByTypeWordHexTagName;
        ThWordStatusDataFs wordStatusDataFs;
        UUID valueUUIDDataFs;
        Integer valueForFlowPointByNumber;
        try {
            typeWordInputedFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameInputedFunc = (String) tagNameInputed;
            numberParameterFunc = (Integer) numberParameter;
            valideInFlowAllPointsByTypeWordHexTagName(typeWordInputedFunc, mainFlowUuidFunc, tagNameInputedFunc);
            flowUuidsByTypeWordHexTagName = getFlowUuidsByTypeWordHexTagName(typeWordInputedFunc, tagNameInputedFunc, mainFlowUuidFunc);
            wordStatusDataFs = this.getWordStatusDataFs();
            valueUUIDDataFs = flowUuidsByTypeWordHexTagName.get(this.getParamCodeByNumber(0));
            wordStatusDataFs.validateCountParams(valueUUIDDataFs);
            valueForFlowPointByNumber = wordStatusDataFs.getValueForFlowPointByNumber(valueUUIDDataFs, numberParameterFunc);
            return valueForFlowPointByNumber;
        } finally {
            typeWordInputedFunc = null;
            mainFlowUuidFunc = null;
            tagNameInputedFunc = null;
            numberParameterFunc = null;
            flowUuidsByTypeWordHexTagName = null;
            
            wordStatusDataFs = null;
            valueUUIDDataFs = null;
            valueForFlowPointByNumber = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param tagNameInputed
     * @param mainFlowUuid
     * @param numberParameter
     * <ul>
     * <li>0 - storageDirectoryName
     * <li>1 - currentFileName
     * <li>2 - newFileName
     * <li>3 - deletedFileName
     * <li>4 - flowFileNamePrefix
     * </ul>
     * @return 
     */
    protected String getValueForValideUuuidByTypeWordHexTagNameNumberName(
            final Integer typeWordInputed,
            final String tagNameInputed,
            final UUID mainFlowUuid,
            final Integer numberParameter
    ){
        Integer typeWordInputedFunc;
        UUID mainFlowUuidFunc;
        String tagNameInputedFunc;
        Integer numberParameterFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByTypeWordHexTagName;
        UUID valueUUIDName;
        ThWordStatusName wordStatusName;
        String valueForFlowPointByNumber;
        try {
            typeWordInputedFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameInputedFunc = (String) tagNameInputed;
            numberParameterFunc = (Integer) numberParameter;
            valideInFlowAllPointsByTypeWordHexTagName(typeWordInputedFunc, mainFlowUuidFunc, tagNameInputedFunc);
            flowUuidsByTypeWordHexTagName = getFlowUuidsByTypeWordHexTagName(typeWordInputedFunc, tagNameInputedFunc, mainFlowUuidFunc);
            wordStatusName = this.getWordStatusName();
            valueUUIDName = flowUuidsByTypeWordHexTagName.get(this.getParamCodeByNumber(1));
            wordStatusName.validateCountParams(valueUUIDName);
            valueForFlowPointByNumber = wordStatusName.getValueForFlowPointByNumber(valueUUIDName, numberParameterFunc);
            return valueForFlowPointByNumber;
        } finally {
            typeWordInputedFunc = null;
            mainFlowUuidFunc = null;
            tagNameInputedFunc = null;
            numberParameterFunc = null;
            flowUuidsByTypeWordHexTagName = null;
            
            wordStatusName = null;
            valueUUIDName = null;
            valueForFlowPointByNumber = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param tagNameInputed
     * @param mainFlowUuid
     * @param numberParameter
     * <ul>
     * <li>0 - lastAccessNanotime
     * <li>1 - countDataUseIterationsSummary
     * </ul>
     * @return 
     */
    protected Long getValueForValideUuuidByTypeWordHexTagNameNumberActivity(
            final Integer typeWordInputed,
            final String tagNameInputed,
            final UUID mainFlowUuid,
            final Integer numberParameter
    ){
        Integer typeWordInputedFunc;
        UUID mainFlowUuidFunc;
        String tagNameInputedFunc;
        Integer numberParameterFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByTypeWordHexTagName;

        ThWordStatusActivity wordStatusActivity;
        UUID valueUUIDActivity;
        Long valueForFlowPointByNumber;
        try {
            typeWordInputedFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameInputedFunc = (String) tagNameInputed;
            numberParameterFunc = (Integer) numberParameter;
            valideInFlowAllPointsByTypeWordHexTagName(typeWordInputedFunc, mainFlowUuidFunc, tagNameInputedFunc);
            flowUuidsByTypeWordHexTagName = getFlowUuidsByTypeWordHexTagName(typeWordInputedFunc, tagNameInputedFunc, mainFlowUuidFunc);
            wordStatusActivity = this.getWordStatusActivity();
            valueUUIDActivity = flowUuidsByTypeWordHexTagName.get(this.getParamCodeByNumber(2));
            wordStatusActivity.validateCountParams(valueUUIDActivity);
            valueForFlowPointByNumber = wordStatusActivity.getValueForFlowPointByNumber(valueUUIDActivity, numberParameterFunc);
            return valueForFlowPointByNumber;
        } finally {
            typeWordInputedFunc = null;
            mainFlowUuidFunc = null;
            tagNameInputedFunc = null;
            numberParameterFunc = null;
            flowUuidsByTypeWordHexTagName = null;
            wordStatusActivity = null;
            valueUUIDActivity = null;
            valueForFlowPointByNumber = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param tagNameInputed
     * @param mainFlowUuid
     * @param numberParameter
     * <ul>
     * <li>0 - currentInCache
     * <li>1 - currentInCacheReaded
     * <li>2 - addNeedToFileSystemLimit
     * <li>3 - indexSystemLimitOnStorage
     * </ul>
     * @return 
     */
    protected Integer getValueForValideUuuidByTypeWordHexTagNameNumberDataCache(
            final Integer typeWordInputed,
            final String tagNameInputed,
            final UUID mainFlowUuid,
            final Integer numberParameter
    ){
        Integer typeWordInputedFunc;
        UUID mainFlowUuidFunc;
        String tagNameInputedFunc;
        Integer numberParameterFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByTypeWordHexTagName;
    ThWordStatusDataCache wordStatusDataCache;
    UUID valueUUIDDataCache;
        Integer valueForFlowPointByNumber;
        try {
            typeWordInputedFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameInputedFunc = (String) tagNameInputed;
            numberParameterFunc = (Integer) numberParameter;
            valideInFlowAllPointsByTypeWordHexTagName(typeWordInputedFunc, mainFlowUuidFunc, tagNameInputedFunc);
            flowUuidsByTypeWordHexTagName = getFlowUuidsByTypeWordHexTagName(typeWordInputedFunc, tagNameInputedFunc, mainFlowUuidFunc);
            wordStatusDataCache = this.getWordStatusDataCache();
            valueUUIDDataCache = flowUuidsByTypeWordHexTagName.get(this.getParamCodeByNumber(3));
            wordStatusDataCache.validateCountParams(valueUUIDDataCache);
            valueForFlowPointByNumber = wordStatusDataCache.getValueForFlowPointByNumber(valueUUIDDataCache, numberParameterFunc);
            return valueForFlowPointByNumber;
        } finally {
            typeWordInputedFunc = null;
            mainFlowUuidFunc = null;
            tagNameInputedFunc = null;
            numberParameterFunc = null;
            flowUuidsByTypeWordHexTagName = null;
            wordStatusDataCache = null;
            valueUUIDDataCache = null;
            valueForFlowPointByNumber = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param tagNameInputed
     * @param mainFlowUuid
     * @param numberParameter
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
    protected Boolean getValueForValideUuuidByTypeWordHexTagNameNumberWorkers(
            final Integer typeWordInputed,
            final String tagNameInputed,
            final UUID mainFlowUuid,
            final Integer numberParameter
    ){
        Integer typeWordInputedFunc;
        UUID mainFlowUuidFunc;
        String tagNameInputedFunc;
        Integer numberParameterFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByTypeWordHexTagName;
        ThWordStatusWorkers wordStatusWorkers;
        UUID valueUUIDWorkers;
        Boolean valueForFlowPointByNumber;
        try {
            typeWordInputedFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameInputedFunc = (String) tagNameInputed;
            numberParameterFunc = (Integer) numberParameter;
            valideInFlowAllPointsByTypeWordHexTagName(typeWordInputedFunc, mainFlowUuidFunc, tagNameInputedFunc);
            flowUuidsByTypeWordHexTagName = getFlowUuidsByTypeWordHexTagName(typeWordInputedFunc, tagNameInputedFunc, mainFlowUuidFunc);
            wordStatusWorkers = this.getWordStatusWorkers();
            valueUUIDWorkers = flowUuidsByTypeWordHexTagName.get(this.getParamCodeByNumber(4));
            wordStatusWorkers.validateCountParams(valueUUIDWorkers);
            valueForFlowPointByNumber = wordStatusWorkers.getValueForFlowPointByNumber(valueUUIDWorkers, numberParameterFunc);
            return valueForFlowPointByNumber;
        } finally {
            typeWordInputedFunc = null;
            mainFlowUuidFunc = null;
            tagNameInputedFunc = null;
            numberParameterFunc = null;
            flowUuidsByTypeWordHexTagName = null;
            wordStatusWorkers = null;
            valueUUIDWorkers = null;
            valueForFlowPointByNumber = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param tagNameInputed
     * @param mainFlowUuid
     * @param numberParameter
     * <ul>
     * <li>0 - isErrorOnWrite
     * <li>1 - isErrorOnMove
     * <li>2 - isNullOnDataInCache
     * <li>3 - isErrorOnDataInCache
     * </ul>
     * @return 
     */
    protected Integer getValueForValideUuuidByTypeWordHexTagNameNumberError(
            final Integer typeWordInputed,
            final String tagNameInputed,
            final UUID mainFlowUuid,
            final Integer numberParameter
    ){
        Integer typeWordInputedFunc;
        UUID mainFlowUuidFunc;
        String tagNameInputedFunc;
        Integer numberParameterFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByTypeWordHexTagName;
        ThWordStatusError wordStatusError;
        UUID valueUUIDError;
        Integer valueForFlowPointByNumber;
        try {
            typeWordInputedFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameInputedFunc = (String) tagNameInputed;
            numberParameterFunc = (Integer) numberParameter;
            valideInFlowAllPointsByTypeWordHexTagName(typeWordInputedFunc, mainFlowUuidFunc, tagNameInputedFunc);
            flowUuidsByTypeWordHexTagName = getFlowUuidsByTypeWordHexTagName(typeWordInputedFunc, tagNameInputedFunc, mainFlowUuidFunc);
            wordStatusError = this.getWordStatusError();
            valueUUIDError = flowUuidsByTypeWordHexTagName.get(this.getParamCodeByNumber(5));
            wordStatusError.validateCountParams(valueUUIDError);
            valueForFlowPointByNumber = wordStatusError.getValueForFlowPointByNumber(valueUUIDError, numberParameterFunc);
            return valueForFlowPointByNumber;
        } finally {
            typeWordInputedFunc = null;
            mainFlowUuidFunc = null;
            tagNameInputedFunc = null;
            numberParameterFunc = null;
            flowUuidsByTypeWordHexTagName = null;
            wordStatusError = null;
            valueUUIDError = null;
            valueForFlowPointByNumber = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForReturnValue
     * @param numberParam
     * <ul>
     * <li>0 - countRecordsOnFileSystem
     * <li>1 - volumeNumber
     * </ul>
     * @return 
     * @throw NullPointerException is mainFlowUuidForReturnValue null
     */
    protected Integer getValueForMainUuidByNumberDataFs(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForReturnValue,
            Integer numberParam){
        TdataWord dataFromBusFunc;
        UUID returnValueMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusDataFs wordStatusDataFs;
        UUID valueUUIDDataFs;
        Integer numberParamFunc;

        try {
            numberParamFunc = (Integer) numberParam;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            returnValueMainFlow = (UUID) mainFlowUuidForReturnValue;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( returnValueMainFlow == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName()
                        + " not exist values for UUID "
                        + returnValueMainFlow.toString()
                );
            }    
            createdFlowParams = flowUuidsByDataWord.get(returnValueMainFlow);
            wordStatusDataFs = this.getWordStatusDataFs();
            valueUUIDDataFs = createdFlowParams.get(this.getParamCodeByNumber(0));
            return wordStatusDataFs.getValueForFlowPointByNumber(valueUUIDDataFs, numberParamFunc);
        } finally {
            dataFromBusFunc = null;
            returnValueMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusDataFs = null;
            valueUUIDDataFs = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForReturnValue
     * @param numberParam
     * <ul>
     * <li>0 - lastAccessNanotime
     * <li>1 - countDataUseIterationsSummary
     * </ul>
     * @return 
     * @throw NullPointerException is mainFlowUuidForReturnValue null
     */
    protected Long getValueForMainUuidByNumberActivity(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForReturnValue,
            Integer numberParam){
        TdataWord dataFromBusFunc;
        UUID returnValueMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusActivity wordStatusActivity;
        UUID valueUUIDActivity;
        Integer numberParamFunc;

        try {
            numberParamFunc = (Integer) numberParam;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            returnValueMainFlow = (UUID) mainFlowUuidForReturnValue;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( returnValueMainFlow == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName()
                        + " not exist values for UUID "
                        + returnValueMainFlow.toString()
                );
            }    
            createdFlowParams = flowUuidsByDataWord.get(returnValueMainFlow);
            wordStatusActivity = this.getWordStatusActivity();
            valueUUIDActivity = createdFlowParams.get(this.getParamCodeByNumber(2));
            return wordStatusActivity.getValueForFlowPointByNumber(valueUUIDActivity, numberParamFunc);
        } finally {
            dataFromBusFunc = null;
            returnValueMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusActivity = null;
            valueUUIDActivity = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForReturnValue
     * @param numberParam
     * <ul>
     * <li>0 - currentInCache
     * <li>1 - currentInCacheReaded
     * <li>2 - addNeedToFileSystemLimit
     * <li>3 - indexSystemLimitOnStorage
     * </ul>
     * @return 
     * @throw NullPointerException is mainFlowUuidForReturnValue null
     */
    protected Integer getValueForMainUuidByNumberDataCache(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForReturnValue,
            Integer numberParam){
        TdataWord dataFromBusFunc;
        UUID returnValueMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusDataCache wordStatusDataCache;
        UUID valueUUIDDataCache;
        Integer numberParamFunc;

        try {
            numberParamFunc = (Integer) numberParam;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            returnValueMainFlow = (UUID) mainFlowUuidForReturnValue;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( returnValueMainFlow == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName()
                        + " not exist values for UUID "
                        + returnValueMainFlow.toString()
                );
            }    
            createdFlowParams = flowUuidsByDataWord.get(returnValueMainFlow);
            wordStatusDataCache = this.getWordStatusDataCache();
            valueUUIDDataCache = createdFlowParams.get(this.getParamCodeByNumber(3));
            return wordStatusDataCache.getValueForFlowPointByNumber(valueUUIDDataCache, numberParamFunc);
        } finally {
            dataFromBusFunc = null;
            returnValueMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusDataCache = null;
            valueUUIDDataCache = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForReturnValue
     * @param numberParam
     * <ul>
     * <li>0 - isErrorOnWrite
     * <li>1 - isErrorOnMove
     * <li>2 - isNullOnDataInCache
     * <li>3 - isErrorOnDataInCache
     * </ul>
     * @return 
     * @throw NullPointerException is mainFlowUuidForReturnValue null
     */
    protected Integer getValueForMainUuidByNumberError(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForReturnValue,
            Integer numberParam){
        TdataWord dataFromBusFunc;
        UUID returnValueMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusError wordStatusError;
        UUID valueUUIDError;
        Integer numberParamFunc;

        try {
            numberParamFunc = (Integer) numberParam;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            returnValueMainFlow = (UUID) mainFlowUuidForReturnValue;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( returnValueMainFlow == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName()
                        + " not exist values for UUID "
                        + returnValueMainFlow.toString()
                );
            }    
            createdFlowParams = flowUuidsByDataWord.get(returnValueMainFlow);
            wordStatusError = this.getWordStatusError();
            valueUUIDError = createdFlowParams.get(this.getParamCodeByNumber(5));
            return wordStatusError.getValueForFlowPointByNumber(valueUUIDError, numberParamFunc);
        } finally {
            dataFromBusFunc = null;
            returnValueMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusError = null;
            valueUUIDError = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForReturnValue
     * @param numberParam
     * <ul>
     * <li>0 - storageDirectoryName
     * <li>1 - currentFileName
     * <li>2 - newFileName
     * <li>3 - deletedFileName
     * <li>4 - flowFileNamePrefix
     * </ul>
     * @return 
     * @throw NullPointerException is mainFlowUuidForReturnValue null
     */
    protected String getValueForMainUuidByNumberName(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForReturnValue,
            Integer numberParam){
        TdataWord dataFromBusFunc;
        UUID returnValueMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusName wordStatusName;
        UUID valueUUIDName;
        Integer numberParamFunc;

        try {
            numberParamFunc = (Integer) numberParam;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            returnValueMainFlow = (UUID) mainFlowUuidForReturnValue;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( returnValueMainFlow == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName()
                        + " not exist values for UUID "
                        + returnValueMainFlow.toString()
                );
            }    
            createdFlowParams = flowUuidsByDataWord.get(returnValueMainFlow);
            wordStatusName = this.getWordStatusName();
            valueUUIDName = createdFlowParams.get(this.getParamCodeByNumber(1));
            return wordStatusName.getValueForFlowPointByNumber(valueUUIDName, numberParamFunc);
        } finally {
            dataFromBusFunc = null;
            returnValueMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusName = null;
            valueUUIDName = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForReturnValue
     * @param numberParam
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
     * @throw NullPointerException is mainFlowUuidForReturnValue null
     */
    protected Boolean getValueForMainUuidByNumberWorkers(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForReturnValue,
            Integer numberParam){
        TdataWord dataFromBusFunc;
        UUID returnValueMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusWorkers wordStatusWorkers;
        UUID valueUUIDWorkers;
        Integer numberParamFunc;

        try {
            numberParamFunc = (Integer) numberParam;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            returnValueMainFlow = (UUID) mainFlowUuidForReturnValue;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( returnValueMainFlow == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName()
                        + " not exist values for UUID "
                        + returnValueMainFlow.toString()
                );
            }    
            createdFlowParams = flowUuidsByDataWord.get(returnValueMainFlow);
            wordStatusWorkers = this.getWordStatusWorkers();
            valueUUIDWorkers = createdFlowParams.get(this.getParamCodeByNumber(4));
            return wordStatusWorkers.getValueForFlowPointByNumber(valueUUIDWorkers, numberParamFunc);
        } finally {
            dataFromBusFunc = null;
            returnValueMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusWorkers = null;
            valueUUIDWorkers = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param hexTagNameInputed
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - countRecordsOnFileSystem
     * <li>1 - volumeNumber
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByHexTagNameNumberDataFs(
            final Integer typeWordInputed,
            final String hexTagNameInputed,
            final UUID mainFlowUuidForChange,
            final Integer numberParam,
            final Integer changedValue){
        
        UUID changedMainFlowFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByDataWord;
        ThWordStatusDataFs wordStatusDataFs;
        UUID valueUUIDDataFs;
        Integer typeWordFunc;
        String hexTagNameFunc;
        Integer numberParamFunc;
        Integer changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Integer) changedValue;
            typeWordFunc = (Integer) typeWordInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            changedMainFlowFunc = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = getFlowUuidsByTypeWordHexTagName(typeWordFunc, hexTagNameFunc, changedMainFlowFunc);
            wordStatusDataFs = this.getWordStatusDataFs();
            valueUUIDDataFs = flowUuidsByDataWord.get(this.getParamCodeByNumber(0));
            wordStatusDataFs.changeParamValByNumber(valueUUIDDataFs, numberParamFunc, changedValueFunc);
        } finally {
            changedMainFlowFunc = null;
            flowUuidsByDataWord = null;
            wordStatusDataFs = null;
            valueUUIDDataFs = null;
            typeWordFunc = null;
            hexTagNameFunc = null;
            numberParamFunc = null;
            changedValueFunc = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param hexTagNameInputed
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - storageDirectoryName
     * <li>1 - currentFileName
     * <li>2 - newFileName
     * <li>3 - deletedFileName
     * <li>4 - flowFileNamePrefix
     * <li>5 - secretStr
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByHexTagNameNumberName(
            final Integer typeWordInputed,
            final String hexTagNameInputed,
            final UUID mainFlowUuidForChange,
            final Integer numberParam,
            final String changedValue){
        
        UUID changedMainFlowFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByDataWord;
        ThWordStatusName wordStatusName;
        UUID valueUUIDName;
        Integer typeWordFunc;
        String hexTagNameFunc;
        Integer numberParamFunc;
        String changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (String) changedValue;
            typeWordFunc = (Integer) typeWordInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            changedMainFlowFunc = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = getFlowUuidsByTypeWordHexTagName(typeWordFunc, hexTagNameFunc, changedMainFlowFunc);
            wordStatusName = this.getWordStatusName();
            valueUUIDName = flowUuidsByDataWord.get(this.getParamCodeByNumber(1));
            wordStatusName.changeParamValByNumber(valueUUIDName, numberParamFunc, changedValueFunc);
        } finally {
            changedMainFlowFunc = null;
            flowUuidsByDataWord = null;
            wordStatusName = null;
            valueUUIDName = null;
            typeWordFunc = null;
            hexTagNameFunc = null;
            numberParamFunc = null;
            changedValueFunc = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param hexTagNameInputed
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - lastAccessNanotime
     * <li>1 - countDataUseIterationsSummary
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByHexTagNameNumberActivity(
            final Integer typeWordInputed,
            final String hexTagNameInputed,
            final UUID mainFlowUuidForChange,
            final Integer numberParam,
            final Long changedValue){
        
        UUID changedMainFlowFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByDataWord;
        ThWordStatusActivity wordStatusActivity;
        UUID valueUUIDActivity;
        Integer typeWordFunc;
        String hexTagNameFunc;
        Integer numberParamFunc;
        Long changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Long) changedValue;
            typeWordFunc = (Integer) typeWordInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            changedMainFlowFunc = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = getFlowUuidsByTypeWordHexTagName(typeWordFunc, hexTagNameFunc, changedMainFlowFunc);
            wordStatusActivity = this.getWordStatusActivity();
            valueUUIDActivity = flowUuidsByDataWord.get(this.getParamCodeByNumber(2));
            wordStatusActivity.changeParamValByNumber(valueUUIDActivity, numberParamFunc, changedValueFunc);
        } finally {
            changedMainFlowFunc = null;
            flowUuidsByDataWord = null;
            wordStatusActivity = null;
            valueUUIDActivity = null;
            typeWordFunc = null;
            hexTagNameFunc = null;
            numberParamFunc = null;
            changedValueFunc = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param hexTagNameInputed
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - currentInCache
     * <li>1 - currentInCacheReaded
     * <li>2 - addNeedToFileSystemLimit
     * <li>3 - indexSystemLimitOnStorage
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByHexTagNameNumberDataCache(
            final Integer typeWordInputed,
            final String hexTagNameInputed,
            final UUID mainFlowUuidForChange,
            final Integer numberParam,
            final Integer changedValue){
        
        UUID changedMainFlowFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByDataWord;
        ThWordStatusDataCache wordStatusDataCache;
        UUID valueUUIDDataCache;
        Integer typeWordFunc;
        String hexTagNameFunc;
        Integer numberParamFunc;
        Integer changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Integer) changedValue;
            typeWordFunc = (Integer) typeWordInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            changedMainFlowFunc = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = getFlowUuidsByTypeWordHexTagName(typeWordFunc, hexTagNameFunc, changedMainFlowFunc);
            wordStatusDataCache = this.getWordStatusDataCache();
            valueUUIDDataCache = flowUuidsByDataWord.get(this.getParamCodeByNumber(3));
            wordStatusDataCache.changeParamValByNumber(valueUUIDDataCache, numberParamFunc, changedValueFunc);
        } finally {
            changedMainFlowFunc = null;
            flowUuidsByDataWord = null;
            wordStatusDataCache = null;
            valueUUIDDataCache = null;
            typeWordFunc = null;
            hexTagNameFunc = null;
            numberParamFunc = null;
            changedValueFunc = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param hexTagNameInputed
     * @param mainFlowUuidForChange
     * @param numberParam
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
     * @param changedValue 
     */
    protected void changeParamForMainUuidByHexTagNameNumberWorkers(
            final Integer typeWordInputed,
            final String hexTagNameInputed,
            final UUID mainFlowUuidForChange,
            final Integer numberParam,
            final Boolean changedValue){
        
        UUID changedMainFlowFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByDataWord;
        ThWordStatusWorkers wordStatusWorkers;
        UUID valueUUIDWorkers;
        Integer typeWordFunc;
        String hexTagNameFunc;
        Integer numberParamFunc;
        Boolean changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Boolean) changedValue;
            typeWordFunc = (Integer) typeWordInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            changedMainFlowFunc = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = getFlowUuidsByTypeWordHexTagName(typeWordFunc, hexTagNameFunc, changedMainFlowFunc);
            wordStatusWorkers = this.getWordStatusWorkers();
            valueUUIDWorkers = flowUuidsByDataWord.get(this.getParamCodeByNumber(4));
            wordStatusWorkers.changeParamValByNumber(valueUUIDWorkers, numberParamFunc, changedValueFunc);
        } finally {
            changedMainFlowFunc = null;
            flowUuidsByDataWord = null;
            wordStatusWorkers = null;
            valueUUIDWorkers = null;
            typeWordFunc = null;
            hexTagNameFunc = null;
            numberParamFunc = null;
            changedValueFunc = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param hexTagNameInputed
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - isErrorOnWrite
     * <li>1 - isErrorOnMove
     * <li>2 - isNullOnDataInCache
     * <li>3 - isErrorOnDataInCache
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByHexTagNameNumberError(
            final Integer typeWordInputed,
            final String hexTagNameInputed,
            final UUID mainFlowUuidForChange,
            final Integer numberParam,
            final Integer changedValue){
        
        UUID changedMainFlowFunc;
        ConcurrentSkipListMap<Integer, UUID> flowUuidsByDataWord;
        ThWordStatusError wordStatusError;
        UUID valueUUIDError;
        Integer typeWordFunc;
        String hexTagNameFunc;
        Integer numberParamFunc;
        Integer changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Integer) changedValue;
            typeWordFunc = (Integer) typeWordInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            changedMainFlowFunc = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = getFlowUuidsByTypeWordHexTagName(typeWordFunc, hexTagNameFunc, changedMainFlowFunc);
            wordStatusError = (ThWordStatusError) getWordStatusError();
            valueUUIDError = flowUuidsByDataWord.get(this.getParamCodeByNumber(5));
            wordStatusError.changeParamValByNumber(valueUUIDError, numberParamFunc, changedValueFunc);
        } finally {
            changedMainFlowFunc = null;
            flowUuidsByDataWord = null;
            wordStatusError = null;
            valueUUIDError = null;
            typeWordFunc = null;
            hexTagNameFunc = null;
            numberParamFunc = null;
            changedValueFunc = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - countRecordsOnFileSystem
     * <li>1 - volumeNumber
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByNumberDataFs(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForChange,
            Integer numberParam,
            Integer changedValue){
        TdataWord dataFromBusFunc;
        UUID createdMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusDataFs wordStatusDataFs;
        UUID valueUUIDDataFs;
        Integer numberParamFunc;
        Integer changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Integer) changedValue;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            createdMainFlow = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( createdMainFlow != null ){
                createdFlowParams = flowUuidsByDataWord.get(createdMainFlow);
                wordStatusDataFs = this.getWordStatusDataFs();
                valueUUIDDataFs = createdFlowParams.get(this.getParamCodeByNumber(0));
                wordStatusDataFs.changeParamValByNumber(valueUUIDDataFs, numberParamFunc, changedValueFunc);
            }
        } finally {
            dataFromBusFunc = null;
            createdMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusDataFs = null;
            valueUUIDDataFs = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - storageDirectoryName
     * <li>1 - currentFileName
     * <li>2 - newFileName
     * <li>3 - deletedFileName
     * <li>4 - flowFileNamePrefix
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByNumberName(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForChange,
            Integer numberParam,
            String changedValue){
        TdataWord dataFromBusFunc;
        UUID createdMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusName wordStatusName;
        UUID valueUUIDName;
        Integer numberParamFunc;
        String changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (String) changedValue;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            createdMainFlow = (UUID) mainFlowUuidForChange;
            
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( createdMainFlow != null ){
                createdFlowParams = flowUuidsByDataWord.get(createdMainFlow);
                wordStatusName = this.getWordStatusName();
                valueUUIDName = createdFlowParams.get(this.getParamCodeByNumber(1));
                wordStatusName.changeParamValByNumber(valueUUIDName,  numberParamFunc, changedValueFunc);
            }
        } finally {
            dataFromBusFunc = null;
            createdMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusName = null;
            valueUUIDName = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - lastAccessNanotime
     * <li>1 - countDataUseIterationsSummary
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByNumberActivity(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForChange,
            Integer numberParam,
            Long changedValue){
        TdataWord dataFromBusFunc;
        UUID createdMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusActivity wordStatusActivity;
        UUID valueUUIDActivity;
        Integer numberParamFunc;
        Long changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Long) changedValue;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            createdMainFlow = (UUID) mainFlowUuidForChange;
            
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( createdMainFlow != null ){
                createdFlowParams = flowUuidsByDataWord.get(createdMainFlow);
                wordStatusActivity = this.getWordStatusActivity();
                valueUUIDActivity = createdFlowParams.get(this.getParamCodeByNumber(2));
                wordStatusActivity.changeParamValByNumber(valueUUIDActivity,  numberParamFunc, changedValueFunc);
            }
        } finally {
            dataFromBusFunc = null;
            createdMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusActivity = null;
            valueUUIDActivity = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - currentInCache
     * <li>1 - currentInCacheReaded
     * <li>2 - addNeedToFileSystemLimit
     * <li>3 - indexSystemLimitOnStorage
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByNumberDataCache(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForChange,
            Integer numberParam,
            Integer changedValue){
        TdataWord dataFromBusFunc;
        UUID createdMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusDataCache wordStatusDataCache;
        UUID valueUUIDDataCache;
        Integer numberParamFunc;
        Integer changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Integer) changedValue;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            createdMainFlow = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( createdMainFlow != null ){
                createdFlowParams = flowUuidsByDataWord.get(createdMainFlow);
                wordStatusDataCache = this.getWordStatusDataCache();
                valueUUIDDataCache = createdFlowParams.get(this.getParamCodeByNumber(3));
                wordStatusDataCache.changeParamValByNumber(valueUUIDDataCache,  numberParamFunc, changedValueFunc);
            }
        } finally {
            dataFromBusFunc = null;
            createdMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusDataCache = null;
            valueUUIDDataCache = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForChange
     * @param numberParam
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
     * @param changedValue 
     */
    protected void changeParamForMainUuidByNumberWorkers(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForChange,
            Integer numberParam,
            Boolean changedValue){
        TdataWord dataFromBusFunc;
        UUID createdMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusWorkers wordStatusWorkers;
        UUID valueUUIDWorkers;
        Integer numberParamFunc;
        Boolean changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Boolean) changedValue;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            createdMainFlow = (UUID) mainFlowUuidForChange;
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( createdMainFlow != null ){
                createdFlowParams = flowUuidsByDataWord.get(createdMainFlow);
                wordStatusWorkers = this.getWordStatusWorkers();
                valueUUIDWorkers = createdFlowParams.get(this.getParamCodeByNumber(4));
                wordStatusWorkers.changeParamValByNumber(valueUUIDWorkers,  numberParamFunc, changedValueFunc);
            }
        } finally {
            dataFromBusFunc = null;
            createdMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusWorkers = null;
            valueUUIDWorkers = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForChange
     * @param numberParam
     * <ul>
     * <li>0 - isErrorOnWrite
     * <li>1 - isErrorOnMove
     * <li>2 - isNullOnDataInCache
     * <li>3 - isErrorOnDataInCache
     * </ul>
     * @param changedValue 
     */
    protected void changeParamForMainUuidByNumberError(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForChange,
            Integer numberParam,
            Integer changedValue){
        TdataWord dataFromBusFunc;
        UUID createdMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        ThWordStatusError wordStatusError;
        UUID valueUUIDError;
        Integer numberParamFunc;
        Integer changedValueFunc;
        try {
            numberParamFunc = (Integer) numberParam;
            changedValueFunc = (Integer) changedValue;
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            createdMainFlow = (UUID) mainFlowUuidForChange;
            
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( createdMainFlow != null ){
                createdFlowParams = flowUuidsByDataWord.get(createdMainFlow);
                wordStatusError = this.getWordStatusError();
                valueUUIDError = createdFlowParams.get(this.getParamCodeByNumber(5));
                wordStatusError.changeParamValByNumber(valueUUIDError, numberParamFunc, changedValueFunc);
            }
        } finally {
            dataFromBusFunc = null;
            createdMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;
            wordStatusError = null;
            valueUUIDError = null;
        }
    }
    /**
     * 
     * @param dataInputed
     * @return 
     */
    protected UUID createInitMainFlow(final TdataWord dataInputed){
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> initMainFlow;
        ConcurrentSkipListMap<Integer, UUID> initValues;
        UUID generatedMainFlowUUID;
        UUID keyFlowStatusDataFs;
        UUID keyFlowStatusName;
        UUID keyFlowStatusActivity;
        UUID keyFlowStatusDataCache;
        UUID keyFlowStatusWorkers;
        UUID keyFlowStatusError;
        
        try {
            generatedMainFlowUUID = UUID.randomUUID();
            keyFlowStatusDataFs = UUID.randomUUID();
            getWordStatusDataFs().createStructureParamsDataFs(keyFlowStatusDataFs);
            keyFlowStatusName = UUID.randomUUID();
            getWordStatusName().createStructureParamsName(keyFlowStatusName);
            keyFlowStatusActivity = UUID.randomUUID();
            getWordStatusActivity().createStructureParamsActivity(keyFlowStatusActivity);
            keyFlowStatusDataCache = UUID.randomUUID();
            getWordStatusDataCache().createStructureParamsDataCache(keyFlowStatusDataCache);
            keyFlowStatusWorkers = UUID.randomUUID();
            getWordStatusWorkers().createStructureParamsWorkers(keyFlowStatusWorkers);
            keyFlowStatusError = UUID.randomUUID();
            getWordStatusError().createStructureParamsError(keyFlowStatusError);
            initValues = new ConcurrentSkipListMap<Integer, UUID>();
            
            initValues.put(getParamCodeByNumber(0), keyFlowStatusDataFs);
            initValues.put(getParamCodeByNumber(1), keyFlowStatusName);
            initValues.put(getParamCodeByNumber(2), keyFlowStatusActivity);
            initValues.put(getParamCodeByNumber(3), keyFlowStatusDataCache);
            initValues.put(getParamCodeByNumber(4), keyFlowStatusWorkers);
            initValues.put(getParamCodeByNumber(5), keyFlowStatusError);
            initMainFlow = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>();
            initMainFlow.put(generatedMainFlowUUID, initValues);
            setParamFlowUuidsByDataWord(dataInputed, initMainFlow);
            return generatedMainFlowUUID;
        } finally {
            generatedMainFlowUUID = null;
            keyFlowStatusActivity = null;
            keyFlowStatusDataCache = null;
            keyFlowStatusDataFs = null;
            keyFlowStatusError = null;
            keyFlowStatusName = null;
            keyFlowStatusWorkers = null;
        }
    }
    /**
     * 
     * @param dataInputed
     * @param checkForExistUuid
     * @return 
     */
    protected Boolean isUuidExistInFlowByDataWord(final TdataWord dataInputed,
            UUID checkForExistUuid){
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> dataTypeWordTagNameSubStr;
        
        TdataWord dataInputedFunc;
        UUID checkForExistUuidFunc;
        
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        try {
            dataInputedFunc = (TdataWord) dataInputed;
            checkForExistUuidFunc = (UUID) checkForExistUuid;
            
            tagNameFunc = dataInputedFunc.hexSubString;
            strSubStringFunc = dataInputedFunc.strSubString;
            typeWordFunc = dataInputedFunc.typeWord;
            
            dataTypeWordTagNameSubStr = getTypeWordTagFileNameFlowUuids(typeWordFunc, strSubStringFunc, tagNameFunc);
            
            if( !dataTypeWordTagNameSubStr.containsKey(checkForExistUuidFunc) ){
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } finally {
            dataTypeWordTagNameSubStr = null;
            dataInputedFunc = null;
            checkForExistUuidFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
        }
    }
    /**
     * 
     * @param typeWordInputed
     * @param tagNameInputed
     * @param strSubStringInputed
     * @param checkForExistUuid
     * @return 
     */
    protected Boolean isUuidExistInFlow(final Integer typeWordInputed, 
            final String tagNameInputed, 
            final String strSubStringInputed,
            UUID checkForExistUuid){
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> dataTypeWordTagNameSubStr;
        UUID checkForExistUuidFunc;
        
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        try {
            checkForExistUuidFunc = (UUID) checkForExistUuid;
            
            tagNameFunc = (String) tagNameInputed;
            strSubStringFunc = (String) strSubStringInputed;
            typeWordFunc = (Integer) typeWordInputed;
            
            dataTypeWordTagNameSubStr = getTypeWordTagFileNameFlowUuids(typeWordFunc, strSubStringFunc, tagNameFunc);
            
            if( !dataTypeWordTagNameSubStr.containsKey(checkForExistUuidFunc) ){
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        } finally {
            dataTypeWordTagNameSubStr = null;
            checkForExistUuidFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
        }
    }
    /**
     * 
     * @param dataInputed
     * @return 
     */
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> getFlowUuidsByDataWord(final TdataWord dataInputed){
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> dataTypeWordTagNameSubStr;
        TdataWord dataFunc;
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        Boolean tdataWordValid;
        try {
            dataFunc = (TdataWord) dataInputed;
            
            tdataWordValid = ThWordHelper.isTdataWordValid(dataFunc);
            if( !tdataWordValid ){
                throw new IllegalArgumentException(ThWordBusReadedFlow.class.getCanonicalName() 
                        + " not valid data for get from cache object class " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            tagNameFunc = dataFunc.hexSubString;
            strSubStringFunc = dataFunc.strSubString;
            typeWordFunc = dataFunc.typeWord;
            
            dataTypeWordTagNameSubStr = getTypeWordTagFileNameFlowUuids(typeWordFunc, strSubStringFunc, tagNameFunc);
            if( dataTypeWordTagNameSubStr == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " not have UUIDs in MainFlow for key type " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            if( dataTypeWordTagNameSubStr.isEmpty() ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " not have UUIDs in MainFlow for key type " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            return dataTypeWordTagNameSubStr;
        }
        finally {
            dataFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
            dataTypeWordTagNameSubStr = null;
            tdataWordValid = null;
        }
    }
    /**
     * <p><h2>this use in router
     * <p>lvl (2a) init params for new itemIndex
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @return lvl (3a)
     * @throws IllegalArgumentException
     */
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> getTypeWordTagFileNameFlowUuids(
            final Integer typeWordInputed,
            final String strSubStringInputed,
            final String tagNameInputed
            
    ){
        
        //(3)
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> tagFileNameParams;
        //(1)
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>> getListByTypeWord;
        //(2a)
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>> getListByTagNameCode;
        //(2b)
        ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>> getListBySubStrLength;
        Integer strSubStringlength;
        Integer tagNamelength;
        
        Integer typeWordFunc;
        String tagNameFunc;
        String strSubStringFunc;
        String substringTagNameLetterFunc;
        try{
            typeWordFunc = (Integer) typeWordInputed;
            tagNameFunc = (String) tagNameInputed;
            strSubStringFunc = (String) strSubStringInputed;
            
            strSubStringlength = (Integer) strSubStringFunc.length();
            tagNamelength = (Integer) tagNameFunc.length();
            
            if( (strSubStringlength * 4) != tagNamelength ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " lengthHex: " + tagNameFunc.length()
                        + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringFunc.length()
                        + " lengthHex == lengthStr * 4 ");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNameFunc.length()
                        + " < 4 ");
            }
            
            getListByTypeWord = getListByType(typeWordFunc);
            
            substringTagNameLetterFunc = tagNameFunc.substring(0, 3);
            getListByTagNameCode = getListByTypeWord.get(substringTagNameLetterFunc);
            if( getListByTagNameCode == null ){
                getListByTagNameCode = new ConcurrentSkipListMap<Integer, 
                                                ConcurrentSkipListMap<String, 
                                                        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>();
                getListByTypeWord.put(substringTagNameLetterFunc, getListByTagNameCode);
                
            }
            getListBySubStrLength = getListByTagNameCode.get(strSubStringlength);
            if( getListBySubStrLength == null ){
                getListBySubStrLength = new ConcurrentSkipListMap<String, 
                                                ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>();
                getListByTagNameCode.put(strSubStringlength, getListBySubStrLength);
            }
            tagFileNameParams = getTagFileNameParams(getListBySubStrLength, tagNameFunc);
            return tagFileNameParams;
        } finally {
            getListByTypeWord = null;
            tagFileNameParams = null;
            getListByTagNameCode = null;
            getListBySubStrLength = null;
            strSubStringlength = null;
            tagNamelength = null;
            
            typeWordFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            substringTagNameLetterFunc = null;
        }
    }
    private ConcurrentSkipListMap<Integer, 
                    UUID> getFlowUuidsByTypeWordHexTagName(
            final Integer typeWordInputed,
            final String tagNameInputed,
            final UUID mainFlowUuid ){
        
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsFunc;
        ConcurrentSkipListMap<Integer, UUID> flowParamsFunc;
        ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, 
                    ConcurrentSkipListMap<Integer, UUID>>>>> listTypeWordData;
        ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, 
                    ConcurrentSkipListMap<Integer, UUID>>>> listTagNameLetter;
        ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, 
                    ConcurrentSkipListMap<Integer, 
                    UUID>>> listTagName;
        
        Integer typeWordFunc;
        UUID mainFlowUuidFunc;
        String tagNameFunc;
        String tagNameLetter;
        Integer tagNamelength;
        Integer calculatedSubString;
        try {
            typeWordFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameFunc = (String) tagNameInputed;
            tagNamelength = (Integer) tagNameFunc.length();
            if( mainFlowUuidFunc == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " Main Flow UUID is null");
            }
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNameFunc.length()
                        + " < 4 ");
            }
            calculatedSubString = tagNamelength / 4;
            tagNameLetter = tagNameFunc.substring(0, 3);
            
            listTypeWordData = this.fileStoragesMap.get(typeWordFunc);
            if( listTypeWordData == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for typeWord: "
                        + typeWordFunc);
            }
            listTagNameLetter = listTypeWordData.get(tagNameLetter);
            if( listTagNameLetter == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for tagNameLetter: "
                        + tagNameLetter);
            }
            listTagName = listTagNameLetter.get(calculatedSubString);
            if( listTagName == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for subStringLength: "
                        + calculatedSubString);
            }
            flowUuidsFunc = listTagName.get(tagNameFunc);
            
            flowParamsFunc = flowUuidsFunc.get(mainFlowUuidFunc);
            if(flowParamsFunc == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for Main Flow UUID: "
                        + mainFlowUuidFunc);
            }
            this.validateCountParams(flowParamsFunc);
            return flowParamsFunc;
        } finally {
            flowUuidsFunc = null;
            flowParamsFunc = null;
            listTypeWordData = null;
            listTagNameLetter = null;
            listTagName = null;

            typeWordFunc = null;
            mainFlowUuidFunc = null;
            tagNameFunc = null;
            tagNamelength = null;
            calculatedSubString = null;
            tagNameLetter = null;
        }
    }
    /**
     * create default or get from lists
     * @param getListByTypeWord
     * @param tagName
     * @return lvl (3)
     */
    private ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> getTagFileNameParams(
            final ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>> getListByTypeWordInputed,
            final String tagNameInputed){
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> getListByTagFileName;
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>> getListByTypeWordFunc;
        String tagNameFunc;
        try{
            tagNameFunc = (String) tagNameInputed;
            getListByTypeWordFunc = (ConcurrentSkipListMap<String, ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>) getListByTypeWordInputed;
            getListByTagFileName = getListByTypeWordFunc.get(tagNameFunc);
            if( getListByTagFileName == null ){
                getListByTagFileName = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>();
                getListByTypeWordFunc.put(tagNameFunc, getListByTagFileName);
            }
            return getListByTagFileName;
        } finally {
            getListByTagFileName = null;
            getListByTypeWordFunc = null;
            tagNameFunc = null;
        }
    }
    /**
     * 
     * @param dataInputed
     * @param mainFlowContentInputed 
     */
    private void setParamFlowUuidsByDataWord(final TdataWord dataInputed,
            final ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> mainFlowContentInputed){
        TdataWord dataFunc;
        String tagNameFunc;
        String strSubStringFunc;
        Integer typeWordFunc;
        Boolean tdataWordValid;
        try {
            dataFunc = (TdataWord) dataInputed;
            
            tdataWordValid = ThWordHelper.isTdataWordValid(dataFunc);
            if( !tdataWordValid ){
                throw new IllegalArgumentException(ThWordBusReadedFlow.class.getCanonicalName() 
                        + " not valid data for get from cache object class " + TdataWord.class.getCanonicalName() 
                        + " object data " + dataFunc.toString());
            }
            tagNameFunc = dataFunc.hexSubString;
            strSubStringFunc = dataFunc.strSubString;
            typeWordFunc = dataFunc.typeWord;
            
            setParamsPointsFlow(typeWordFunc, strSubStringFunc, tagNameFunc, mainFlowContentInputed);
        }
        finally {
            dataFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
            typeWordFunc = null;
            tdataWordValid = null;
        }
    }
    /**
     * @todo check for inputed params
     * @param typeWord
     * @param tagName
     * @param strSubString
     * @param keysPointsFlow ConcurrentSkipListMap<Integer, UUID>
     *          <ThWordStatusDataFs.hashCode(), recordUUID>
     *          <ThWordStatusName.hashCode(), recordUUID>
     *          <ThWordStatusActivity.hashCode(), recordUUID>
     *          <ThWordStatusDataCache.hashCode(), recordUUID>
     *          <ThWordStatusWorkers.hashCode(), recordUUID>
     * @throws IllegalArgumentException when count params or name not valid
     */
    private void setParamsPointsFlow(
            final Integer typeWordOuter,
            final String strSubStringOuter,
            final String tagNameOuter, 
            final ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> mainFlowContentInputed){
        Integer typeWordInner;
        String tagNameInner;
        String strSubStringInner;
        
        Boolean existInListOfParams;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> mainFlowContentFunc;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> typeWordTagFileNameFlowUuids;
        try {
            typeWordInner = (Integer) typeWordOuter;
            tagNameInner = (String) tagNameOuter;
            strSubStringInner = (String) strSubStringOuter;
            mainFlowContentFunc = new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>();
            for(Map.Entry<UUID, ConcurrentSkipListMap<Integer, UUID>> itemsContent : mainFlowContentInputed.entrySet()){
                validateCountParams(itemsContent.getValue());
                mainFlowContentFunc.put(itemsContent.getKey(), itemsContent.getValue());
            }
            
            typeWordTagFileNameFlowUuids = (ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>) getTypeWordTagFileNameFlowUuids(
                    typeWordInner,
                    strSubStringInner,
                    tagNameInner
                    );
            
            typeWordTagFileNameFlowUuids.putAll((ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>) mainFlowContentFunc);
        } finally {
            typeWordTagFileNameFlowUuids = null;
            existInListOfParams = null;
            //delete it null if in creation of structure has problem...need test
            mainFlowContentFunc = null;
            typeWordInner = null;
            tagNameInner = null;
            strSubStringInner = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForCheck 
     */
    protected void validateInFlowAllPointsByDataWord(final TdataWord fromBusReadedData,
            final UUID mainFlowUuidForCheck){
        TdataWord dataFromBusFunc;
        UUID createdMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        
        ThWordStatusDataFs wordStatusDataFs;
        ThWordStatusName wordStatusName;
        ThWordStatusActivity wordStatusActivity;
        ThWordStatusDataCache wordStatusDataCache;
        ThWordStatusWorkers wordStatusWorkers;
        ThWordStatusError wordStatusError;
        
        UUID valueUUIDDataFs;
        UUID valueUUIDName;
        UUID valueUUIDActivity;
        UUID valueUUIDDataCache;
        UUID valueUUIDWorkers;
        UUID valueUUIDError;
        try {
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            createdMainFlow = (UUID) mainFlowUuidForCheck;
            
            flowUuidsByDataWord = this.getFlowUuidsByDataWord(dataFromBusFunc);
            if( createdMainFlow != null ){
                createdFlowParams = flowUuidsByDataWord.get(createdMainFlow);
                this.validateCountParams(createdFlowParams);

                wordStatusDataFs = this.getWordStatusDataFs();
                wordStatusName = this.getWordStatusName();
                wordStatusActivity = this.getWordStatusActivity();
                wordStatusDataCache = this.getWordStatusDataCache();
                wordStatusWorkers = this.getWordStatusWorkers();
                wordStatusError = this.getWordStatusError();
                
                valueUUIDDataFs = createdFlowParams.get(this.getParamCodeByNumber(0));
                valueUUIDName = createdFlowParams.get(this.getParamCodeByNumber(1));
                valueUUIDActivity = createdFlowParams.get(this.getParamCodeByNumber(2));
                valueUUIDDataCache = createdFlowParams.get(this.getParamCodeByNumber(3));
                valueUUIDWorkers = createdFlowParams.get(this.getParamCodeByNumber(4));
                valueUUIDError = createdFlowParams.get(this.getParamCodeByNumber(5));
                
                wordStatusDataFs.validateCountParams(valueUUIDDataFs);
                wordStatusName.validateCountParams(valueUUIDName);
                wordStatusActivity.validateCountParams(valueUUIDActivity);
                wordStatusDataCache.validateCountParams(valueUUIDDataCache);
                wordStatusWorkers.validateCountParams(valueUUIDWorkers);
                wordStatusError.validateCountParams(valueUUIDError);
            }
        } finally {
            dataFromBusFunc = null;
            createdMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;

            wordStatusDataFs = null;
            wordStatusName = null;
            wordStatusActivity = null;
            wordStatusDataCache = null;
            wordStatusWorkers = null;
            wordStatusError = null;

            valueUUIDDataFs = null;
            valueUUIDName = null;
            valueUUIDActivity = null;
            valueUUIDDataCache = null;
            valueUUIDWorkers = null;
            valueUUIDError = null;
        }
    }
    /**
     * 
     * @param fromBusReadedData
     * @param mainFlowUuidForCheck 
     */
    protected void validateInFlowAllPoints(
            final Integer typeWordInputed,
            final String strSubStringInputed,
            final String tagNameInputed, 
            final UUID mainFlowUuidForCheck){
        TdataWord dataFromBusFunc;
        UUID createdMainFlow;
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsByDataWord;
        ConcurrentSkipListMap<Integer, UUID> createdFlowParams;
        
        ThWordStatusDataFs wordStatusDataFs;
        ThWordStatusName wordStatusName;
        ThWordStatusActivity wordStatusActivity;
        ThWordStatusDataCache wordStatusDataCache;
        ThWordStatusWorkers wordStatusWorkers;
        ThWordStatusError wordStatusError;
        
        UUID valueUUIDDataFs;
        UUID valueUUIDName;
        UUID valueUUIDActivity;
        UUID valueUUIDDataCache;
        UUID valueUUIDWorkers;
        UUID valueUUIDError;
        
        Integer typeWordFunc;
        String tagNameFunc;
        String strSubStringFunc;
        try{
            typeWordFunc = (Integer) typeWordInputed;
            tagNameFunc = (String) tagNameInputed;
            strSubStringFunc = (String) strSubStringInputed;
            
            createdMainFlow = (UUID) mainFlowUuidForCheck;
            
            flowUuidsByDataWord = this.getTypeWordTagFileNameFlowUuids(typeWordFunc, strSubStringFunc, tagNameFunc);
            if( createdMainFlow != null ){
                createdFlowParams = flowUuidsByDataWord.get(createdMainFlow);
                this.validateCountParams(createdFlowParams);

                wordStatusDataFs = this.getWordStatusDataFs();
                wordStatusName = this.getWordStatusName();
                wordStatusActivity = this.getWordStatusActivity();
                wordStatusDataCache = this.getWordStatusDataCache();
                wordStatusWorkers = this.getWordStatusWorkers();
                wordStatusError = this.getWordStatusError();
                
                valueUUIDDataFs = createdFlowParams.get(this.getParamCodeByNumber(0));
                valueUUIDName = createdFlowParams.get(this.getParamCodeByNumber(1));
                valueUUIDActivity = createdFlowParams.get(this.getParamCodeByNumber(2));
                valueUUIDDataCache = createdFlowParams.get(this.getParamCodeByNumber(3));
                valueUUIDWorkers = createdFlowParams.get(this.getParamCodeByNumber(4));
                valueUUIDError = createdFlowParams.get(this.getParamCodeByNumber(5));
                
                wordStatusDataFs.validateCountParams(valueUUIDDataFs);
                wordStatusName.validateCountParams(valueUUIDName);
                wordStatusActivity.validateCountParams(valueUUIDActivity);
                wordStatusDataCache.validateCountParams(valueUUIDDataCache);
                wordStatusWorkers.validateCountParams(valueUUIDWorkers);
                wordStatusError.validateCountParams(valueUUIDError);
            }
        } finally {
            dataFromBusFunc = null;
            createdMainFlow = null;
            flowUuidsByDataWord = null;
            createdFlowParams = null;

            wordStatusDataFs = null;
            wordStatusName = null;
            wordStatusActivity = null;
            wordStatusDataCache = null;
            wordStatusWorkers = null;
            wordStatusError = null;

            valueUUIDDataFs = null;
            valueUUIDName = null;
            valueUUIDActivity = null;
            valueUUIDDataCache = null;
            valueUUIDWorkers = null;
            valueUUIDError = null;
            
            typeWordFunc = null;
            tagNameFunc = null;
            strSubStringFunc = null;
        }
    }
    protected void valideInFlowAllPointsByTypeWordHexTagName(
            final Integer typeWordInputed,
            final UUID mainFlowUuid,
            final String tagNameInputed ){
        
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>> flowUuidsFunc;
        ConcurrentSkipListMap<Integer, UUID> flowParamsFunc;
        ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, 
                    ConcurrentSkipListMap<Integer, UUID>>>>> listTypeWordData;
        ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, 
                    ConcurrentSkipListMap<Integer, UUID>>>> listTagNameLetter;
        ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<UUID, 
                    ConcurrentSkipListMap<Integer, 
                    UUID>>> listTagName;
        ThWordStatusDataFs wordStatusDataFs;
        ThWordStatusName wordStatusName;
        ThWordStatusActivity wordStatusActivity;
        ThWordStatusDataCache wordStatusDataCache;
        ThWordStatusWorkers wordStatusWorkers;
        ThWordStatusError wordStatusError;
        
        UUID valueUUIDDataFs;
        UUID valueUUIDName;
        UUID valueUUIDActivity;
        UUID valueUUIDDataCache;
        UUID valueUUIDWorkers;
        UUID valueUUIDError;
        
        Integer typeWordFunc;
        UUID mainFlowUuidFunc;
        String tagNameFunc;
        String tagNameLetter;
        Integer tagNamelength;
        Integer calculatedSubString;
        try {
            typeWordFunc = (Integer) typeWordInputed;
            mainFlowUuidFunc = (UUID) mainFlowUuid;
            tagNameFunc = (String) tagNameInputed;
            tagNamelength = tagNameFunc.length();
            
            if( tagNamelength < 4 ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal length of inputed in index string, hexTagName: "
                        + tagNameFunc + " length: " + tagNameFunc.length()
                        + " < 4 ");
            }
            calculatedSubString = tagNamelength / 4;
            tagNameLetter = tagNameFunc.substring(0, 3);
            
            listTypeWordData = this.fileStoragesMap.get(typeWordFunc);
            if( listTypeWordData == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for typeWord: "
                        + typeWordFunc);
            }
            listTagNameLetter = listTypeWordData.get(tagNameLetter);
            if( listTagNameLetter == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for tagNameLetter: "
                        + tagNameLetter);
            }
            listTagName = listTagNameLetter.get(calculatedSubString);
            if( listTagName == null ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " illegal key value for subStringLength: "
                        + calculatedSubString);
            }
            flowUuidsFunc = listTagName.get(tagNameFunc);
            if( mainFlowUuidFunc != null ){
                flowParamsFunc = flowUuidsFunc.get(mainFlowUuidFunc);
                this.validateCountParams(flowParamsFunc);

                wordStatusDataFs = this.getWordStatusDataFs();
                wordStatusName = this.getWordStatusName();
                wordStatusActivity = this.getWordStatusActivity();
                wordStatusDataCache = this.getWordStatusDataCache();
                wordStatusWorkers = this.getWordStatusWorkers();
                wordStatusError = this.getWordStatusError();
                
                valueUUIDDataFs = flowParamsFunc.get(this.getParamCodeByNumber(0));
                valueUUIDName = flowParamsFunc.get(this.getParamCodeByNumber(1));
                valueUUIDActivity = flowParamsFunc.get(this.getParamCodeByNumber(2));
                valueUUIDDataCache = flowParamsFunc.get(this.getParamCodeByNumber(3));
                valueUUIDWorkers = flowParamsFunc.get(this.getParamCodeByNumber(4));
                valueUUIDError = flowParamsFunc.get(this.getParamCodeByNumber(5));
                
                wordStatusDataFs.validateCountParams(valueUUIDDataFs);
                wordStatusName.validateCountParams(valueUUIDName);
                wordStatusActivity.validateCountParams(valueUUIDActivity);
                wordStatusDataCache.validateCountParams(valueUUIDDataCache);
                wordStatusWorkers.validateCountParams(valueUUIDWorkers);
                wordStatusError.validateCountParams(valueUUIDError);
            }
        } finally {
            flowUuidsFunc = null;
            flowParamsFunc = null;
            listTypeWordData = null;
            listTagNameLetter = null;
            listTagName = null;
            
            wordStatusDataFs = null;
            wordStatusName = null;
            wordStatusActivity = null;
            wordStatusDataCache = null;
            wordStatusWorkers = null;
            wordStatusError = null;

            valueUUIDDataFs = null;
            valueUUIDName = null;
            valueUUIDActivity = null;
            valueUUIDDataCache = null;
            valueUUIDWorkers = null;
            valueUUIDError = null;
            
            typeWordFunc = null;
            mainFlowUuidFunc = null;
            tagNameFunc = null;
            tagNamelength = null;
            calculatedSubString = null;
            tagNameLetter = null;
        }
    }
    /**
     * 
     * @param checkedMainFlowContentInputed 
     * @throws IllegalArgumentException when count params or name not valid
     */
    private void validateCountParams(final ConcurrentSkipListMap<Integer, UUID> checkedMainFlowContentInputed){
        Integer paramCodeByNumber;
        String paramNameByNumber;
        ConcurrentSkipListMap<Integer, UUID> checkedMainFlowContentFunc;
        try {
            checkedMainFlowContentFunc = (ConcurrentSkipListMap<Integer, UUID>) checkedMainFlowContentInputed;
            if( checkedMainFlowContentFunc == null ){
                throw new NullPointerException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " need structure of parameters, argument for validate is null");
            }
            if( checkedMainFlowContentFunc.isEmpty() ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                        + " parameters of data for set into cache is empty");
            }
                int countParam = getParamCount();
                int countForSet = checkedMainFlowContentFunc.size();
                if( countForSet != countParam ){
                    throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                            + " parameters of data for set into cache not valid, "
                            + " base count: " + countParam
                            + ", send for set count: " + countParam);
                }
                
                for( int idx = 0 ; idx < countParam ; idx++ ){
                    paramCodeByNumber = getParamCodeByNumber(idx);
                    if ( !checkedMainFlowContentFunc.containsKey(paramCodeByNumber) ){
                        
                        paramNameByNumber = getParamNameByNumber(idx);
                        throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                                + " parameter "
                                + " for name: " + paramNameByNumber
                                + " in inputed data for set into StatusMain Flow not exist");
                    }
                }
        } finally {
            paramCodeByNumber = null;
            paramNameByNumber = null;
            checkedMainFlowContentFunc = null;
        }
    }
    /**
     * <ul>
     * <li>0 - ThWordStatusDataFs
     * <li>1 - ThWordStatusName
     * <li>2 - ThWordStatusActivity
     * <li>3 - ThWordStatusDataCache
     * <li>4 - ThWordStatusWorkers
     * <li>5 - ThWordStatusError
     * </ul>
     * @return 
     */
    private String[] getParamNames(){
        String[] namesForReturn;
        try {
            namesForReturn = new String[] {
                "ThWordStatusDataFs", 
                "ThWordStatusName", 
                "ThWordStatusActivity",
                "ThWordStatusDataCache",
                "ThWordStatusWorkers",
                "ThWordStatusError"
            };
            return namesForReturn;
        } finally {
            namesForReturn = null;
        }
    }
    /**
     * <ul>
     * <li>0 - ThWordStatusDataFs
     * <li>1 - ThWordStatusName
     * <li>2 - ThWordStatusActivity
     * <li>3 - ThWordStatusDataCache
     * <li>4 - ThWordStatusWorkers
     * <li>5 - ThWordStatusError
     * </ul>
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
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + " 0 (zero) > , need for return " + numParam + "count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
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
    private Integer getParamCount(){
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
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + " 0 (zero) > , need for return " + numParam + "count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
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
     * @return 
     */
    private ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, 
                            ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>>> createNewListStoragesMapEmpty(){
        return new ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                            ConcurrentSkipListMap<Integer, 
                                ConcurrentSkipListMap<String, 
                                    ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>>>();
    }
    /**
     * 
     * @param typeWordOuter
     * @return 
     */
    private ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>> getListByType(final int typeWordOuter){
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>> forListReturn;
        try{
            forListReturn = this.fileStoragesMap.get(typeWordOuter);
            if( forListReturn == null ){
                forListReturn = new ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<Integer, UUID>>>>>();
                this.fileStoragesMap.put(typeWordOuter, forListReturn);
            }
            return forListReturn;
        } finally {
            forListReturn = null;
        }
    }
    

}
