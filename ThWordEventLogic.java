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

import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <ol>
 * <li> when transfer data Event to Proc or Proc to Event set flag 
 *      transEvPr or transPrEv
 * <li> On fromFsDeleteDataEvent (markProcListDeleting)
 *  - current filename saved (curFNs)
 *  - poll data from Cache
 *  - if new move name (nMFN) after write equal curFNs than rename curFNs-UUID(prev)
 *  - write DataFromCache move to nMFN, delete curFNs-UUID(prev)
 * <li> On readReadyDataEvent (markProcListReading)
 *  - current filename saved (curFNs)
 *  - read from Fs data
 *  - poll data from ReadedCache
 *  - insert into Cache (insertIntoCacheEvent)
 *  - add curFNs name to list, move UUID into fromFsDelteDataEvent
 * <li> On writeDataFromCacheEvent (markProcListWriting)
 *  - poll data from Cache
 *  - if notexist (nMFN)
 *  - write data into FS
 *  - add nMFN to curFNs name to list, move UUID into readReadyDataEvent
 * <li> On insertIntoCacheEvent (markProcListInserting) 
 *                      Sources: fromOuterBus, fromReadedCache
 *  - poll data from OuterBus
 *  - poll data from ReadedCache
 *  - insert into Cache
 *  - check for markProcListDeleting, fromFsDelteDataEvent if need, do
 *  - check for markProcListReading, readReadyDataEvent if need, do
 *  - check for markProcListWriting, writeDataFromCacheEvent if need, do
 *  - if end for fromOuterBus source, do all clean fromReadedCache source, do cleanCacheEvent
 * <li> On cleanReadedCacheEvent (markProcListReadCacheCleaning)
 *  - do insertIntoCacheEvent for all data fromReadedCache
 * <li> On cleanCacheEvent, new Source cleanedCache
 *  - do cleanReadedCacheEvent (markProcListCacheCleaning)
 *  - poll all data from Cache
 *  - create Source pollFromCache
 *  - do insertIntoCacheEvent
 *  - while cleanedCache is not Empty
 * </ol>
 * @author wladimirowichbiaran
 */
public class ThWordEventLogic {
    private final Long timeCreation;
    private final UUID objectLabel;
    //Sources
    //private final ThStorageWordBusOutput busJobForWordRouter;
    private final ThWordState wordState;
    private final ThWordStatusMainFlow wordStatusMainFlow;
    private final ThWordCacheSk wordCache;
    private final ThWordCacheSk wordCacheReaded;
    private final ThWordEventIndex eventIndex;
    private final ThWordEventIndexFlow eventIndexFlow;
    
    public ThWordEventLogic(final ThWordRule ruleWordInputed) {
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        //this.busJobForWordRouter = ruleWordInputed.getIndexRule().getIndexState().getRuleStorageWord().getStorageWordState().getBusJobForWordWrite();
        this.wordState = ruleWordInputed.getWordState();
        this.eventIndexFlow = ruleWordInputed.getWordState().getEventIndexFlow();
        this.wordStatusMainFlow = ruleWordInputed.getWordStatusMainFlow();
        this.wordCacheReaded = ruleWordInputed.getWordStatusMainFlow().getWordCacheReaded();
        this.wordCache = ruleWordInputed.getWordStatusMainFlow().getWordCache();
        this.eventIndex = ruleWordInputed.getWordState().getEventIndex();
    }
    /**
     * 
     * @param inputedMainFlowUuid
     * @return 
     */
    protected Integer getSizeDataInCacheByMainFlowUuid( final UUID inputedMainFlowUuid ){
        UUID functionMainFlowUuid;
        String hexTagNameByMainFlowUuid;
        String subStringByMainFlowUuid;
        Integer typeWordByMainFlowUuid;
        Integer countRecordsForReturn;
        try {
            functionMainFlowUuid = (UUID) inputedMainFlowUuid;
            hexTagNameByMainFlowUuid = this.eventIndex.getHexTagNameByMainFlowUuid(functionMainFlowUuid);
            typeWordByMainFlowUuid = this.eventIndex.getTypeWordByMainFlowUuid(functionMainFlowUuid);
            subStringByMainFlowUuid = this.eventIndex.getSubStringByMainFlowUuid(functionMainFlowUuid);
            countRecordsForReturn = this.wordCache.sizeDataInCache(typeWordByMainFlowUuid, hexTagNameByMainFlowUuid, subStringByMainFlowUuid);
            return new Integer(countRecordsForReturn);
        } finally {
            hexTagNameByMainFlowUuid = null;
            typeWordByMainFlowUuid = null;
            countRecordsForReturn = null;
            subStringByMainFlowUuid = null;
            ThWordHelper.utilizeStringValues(new String[]{hexTagNameByMainFlowUuid, subStringByMainFlowUuid});
        }
    }
    /**
     * 
     * @param typeWordOfBusOutput
     * @param hexTagNameFromBusOutput
     * @param subStringFromBusOutput
     * @param pollFromBusOutputDataPacket 
     */
    protected void insertIntoCacheData(Integer typeWordOfBusOutput, 
            String hexTagNameFromBusOutput, 
            String subStringFromBusOutput, 
            TdataWord pollFromBusOutputDataPacket){
        UUID createInitMainFlow;
        Boolean setDataIntoCacheFlow;
        Boolean removeAllFlowStatusByUUID;
        ThWordBusFlowEvent eventDoBusByNumber;
        ThWordBusFlowEvent eventReadyBusByNumber;
        String buildTypeWordStoreSubDirictories = new String();
        Integer valueForMainUuidByNumberDataCache;
        Integer valueLimitForMainUuidByNumberDataCache;
        Integer sizeDataInCacheByMainFlowUuid;
        Integer forLimitOnFsNeedAppendSize;
        Integer valueForSetInDataCache;
        Integer sizeBusFlowUuids;
        String exMessage = new String();
        try {
            
            if( typeWordOfBusOutput == null ){
                throw new IllegalArgumentException(ThWordEventLogic.class.getCanonicalName() + " typeWord is null");
            }
            if( hexTagNameFromBusOutput.isEmpty() || hexTagNameFromBusOutput == null ){
                throw new IllegalArgumentException(ThWordEventLogic.class.getCanonicalName() + " not set hexTagName");
            }
            if( subStringFromBusOutput.isEmpty() || subStringFromBusOutput == null ){
                throw new IllegalArgumentException(ThWordEventLogic.class.getCanonicalName() + " not set subString");
            }
            if( pollFromBusOutputDataPacket == null ){
                throw new IllegalArgumentException(ThWordEventLogic.class.getCanonicalName() + " data from output bus for insert into cache is null");
            }
            //start before insert into cache
            //@todo if size in cache > 0, get last uuid
            //if exist data in cache readed insert all existing readed data into cache
            Boolean isNeedCreateNewUuid = insertFromReadedToCache(typeWordOfBusOutput, hexTagNameFromBusOutput, subStringFromBusOutput);
            //@todo incrementSize value for uuid
            if( isNeedCreateNewUuid ){
                createInitMainFlow = this.wordStatusMainFlow.createInitMainFlow(pollFromBusOutputDataPacket, this.eventIndexFlow);
            }
            else {
                createInitMainFlow = getExistingUuidWithReadedValues(typeWordOfBusOutput, hexTagNameFromBusOutput, subStringFromBusOutput);
                if( createInitMainFlow == null ){
                    createInitMainFlow = this.wordStatusMainFlow.createInitMainFlow(pollFromBusOutputDataPacket, this.eventIndexFlow);
                }
            }
            eventDoBusByNumber = this.wordState.getEventDoBusByNumber(3);
            eventDoBusByNumber.addToListOfFlowEventUuids(typeWordOfBusOutput, hexTagNameFromBusOutput, subStringFromBusOutput, createInitMainFlow);
            //end before insert into cache
            setDataIntoCacheFlow = Boolean.FALSE;
            
            try {
                setDataIntoCacheFlow = this.wordCache.setDataIntoCacheFlow(pollFromBusOutputDataPacket);
                //start after insert into cache
                //@todo 2 set in init
                this.wordStatusMainFlow.incrementRecordsCountCache(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput, createInitMainFlow);
                //set inital flags to workers
                this.wordStatusMainFlow.changeInWorkers(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput, createInitMainFlow, 3, setDataIntoCacheFlow);
                //set inital flags to name
                buildTypeWordStoreSubDirictories = ThWordHelper.buildTypeWordStoreSubDirictories(typeWordOfBusOutput, hexTagNameFromBusOutput, subStringFromBusOutput.length());
                this.wordStatusMainFlow.changeInName(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput, createInitMainFlow, 0, buildTypeWordStoreSubDirictories);
                this.wordStatusMainFlow.changeInName(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput, createInitMainFlow, 4, hexTagNameFromBusOutput);
                valueForMainUuidByNumberDataCache = this.wordStatusMainFlow.getValueForMainUuidByNumberDataCache(pollFromBusOutputDataPacket, createInitMainFlow, 2);
                valueLimitForMainUuidByNumberDataCache = this.wordStatusMainFlow.getValueForMainUuidByNumberDataCache(pollFromBusOutputDataPacket, createInitMainFlow, 3);
                //set inital flags to cache
                sizeDataInCacheByMainFlowUuid = this.getSizeDataInCacheByMainFlowUuid(createInitMainFlow);
                forLimitOnFsNeedAppendSize = valueForMainUuidByNumberDataCache - sizeDataInCacheByMainFlowUuid;
                valueForSetInDataCache = 0;
                if( forLimitOnFsNeedAppendSize < 0 ){
                    do {
                        //increment cached volumes count
                        this.wordStatusMainFlow.incrementVolumeNumberDataCache(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput, createInitMainFlow);
                        forLimitOnFsNeedAppendSize = valueLimitForMainUuidByNumberDataCache + forLimitOnFsNeedAppendSize;
                    } while( forLimitOnFsNeedAppendSize < 0 );
                }
                if( forLimitOnFsNeedAppendSize == 0 ){
                    //increment cached volumes
                    this.wordStatusMainFlow.incrementVolumeNumberDataCache(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput, createInitMainFlow);
                    valueForSetInDataCache = valueLimitForMainUuidByNumberDataCache;
                }    
                if( forLimitOnFsNeedAppendSize > 0 ){
                    valueForSetInDataCache = forLimitOnFsNeedAppendSize;
                }
                this.wordStatusMainFlow.changeInDataCache(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput, createInitMainFlow, 2, valueForSetInDataCache);
                //set flow flags
            } catch(IllegalArgumentException exArg){
                exMessage = exArg.getMessage();
            }
            if( setDataIntoCacheFlow ){
                this.eventIndex.putMainFlowUuidTypeWord(createInitMainFlow, typeWordOfBusOutput);
                this.eventIndex.putMainFlowUuidHexTagName(createInitMainFlow, hexTagNameFromBusOutput);
                this.eventIndex.putMainFlowUuidSubString(createInitMainFlow, subStringFromBusOutput);
                this.eventIndex.changeFlowStatusProcDeletingEvent(typeWordOfBusOutput, hexTagNameFromBusOutput, createInitMainFlow);
                eventReadyBusByNumber = this.wordState.getEventReadyBusByNumber(3);
                eventReadyBusByNumber.addToListOfFlowEventUuids(typeWordOfBusOutput, hexTagNameFromBusOutput, subStringFromBusOutput, createInitMainFlow);
                eventDoBusByNumber.removeMainFlowUuid(createInitMainFlow, this.eventIndex);
                this.wordState.getBusEventShort().addUuidToShortEvent(2, 3, createInitMainFlow);
                ThWordBusFlowEvent eventReadyBusByNumberWrite = this.wordState.getEventReadyBusByNumber(2);
                sizeBusFlowUuids = eventReadyBusByNumberWrite.sizeBusFlowUuids(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput);
                if( sizeBusFlowUuids == 0 ){
                    this.wordState.getBusEventShortNextStep().addUuidToShortEvent(0, 2, createInitMainFlow);
                } else {
                    this.wordState.getBusEventShortNextStep().addUuidToShortEvent(0, 1, createInitMainFlow);
                }
            } else {
                removeAllFlowStatusByUUID = this.wordStatusMainFlow.removeAllFlowStatusByUUID(createInitMainFlow);
                if( !removeAllFlowStatusByUUID ){
                    throw new IllegalArgumentException(ThWordEventLogic.class.getCanonicalName() + " data from output bus is not set "
                            + " into cache, reason: " + exMessage);
                }
            }
            //end after insert into cache
        } finally {
            createInitMainFlow = null;
            setDataIntoCacheFlow = null;
            removeAllFlowStatusByUUID = null;
            eventDoBusByNumber = null;
            eventReadyBusByNumber = null;
            valueForMainUuidByNumberDataCache = null;
            valueLimitForMainUuidByNumberDataCache = null;
            sizeDataInCacheByMainFlowUuid = null;
            forLimitOnFsNeedAppendSize = null;
            valueForSetInDataCache = null;
            sizeBusFlowUuids = null;
            ThWordHelper.utilizeStringValues(new String[] {
                exMessage,
                buildTypeWordStoreSubDirictories
            });
        }
    }
    /**
     * @todo algoritm need change for not del, not move, not write parts
     * @param fsForWriteData
     * @param pollNextUuid 
     * @throws IllegalStateException 
     * <ul>
     * <li>when from cache return empty or null value for write
     * <li>when Main Flow UUID is writed
     * <li>when Storage directory not exist and not created, exceptions on create
     * </ul>
     */
    protected void writeDataToStorage(FileSystem fsForWriteData, UUID pollNextUuid){
        ConcurrentSkipListMap<UUID, TdataWord> writedFromCacheData;
        Map.Entry<UUID, TdataWord> pollFirstEntry;
        Boolean localIsLimitForWrite;
        Boolean localPrevDataWrited; 
        Boolean localPrevDataMoved;
        Boolean localIsUuidFinished;
        try {
            localIsUuidFinished = Boolean.TRUE; 
            localPrevDataWrited = Boolean.FALSE; 
            localPrevDataMoved = Boolean.FALSE;
            String hexTagNameByMainFlowUuid = this.eventIndex.getHexTagNameByMainFlowUuid(pollNextUuid);
            String subStringByMainFlowUuid = this.eventIndex.getSubStringByMainFlowUuid(pollNextUuid);
            Integer typeWordByMainFlowUuid = this.eventIndex.getTypeWordByMainFlowUuid(pollNextUuid);
            Boolean valueForMainFlowUuidByNumberWorkers = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberWorkers(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 0);
            if( valueForMainFlowUuidByNumberWorkers ){
                throw new IllegalStateException(ThWordEventLogic.class.getCanonicalName() + " Main Flow UUID: "
                    + pollNextUuid.toString() + " now writed in other worker");
            }
            ConcurrentSkipListMap<UUID, TdataWord> pollTypeWordTagFileNameData = null;
            try{
                pollTypeWordTagFileNameData = this.wordCache.pollTypeWordTagFileNameData(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid);
            } catch (NullPointerException exRetNull) {
                System.err.println(exRetNull.getMessage());
                exRetNull.printStackTrace();
            } catch (IllegalArgumentException exRetNull) {
                    System.err.println(exRetNull.getMessage());
                    exRetNull.printStackTrace();
            }
            if( pollTypeWordTagFileNameData.isEmpty() || pollTypeWordTagFileNameData == null ){
                String strExReturn = pollTypeWordTagFileNameData.isEmpty() ? " received empty value for write from cache" : " from cache returned null for write to storage";
                throw new IllegalStateException(ThWordEventLogic.class.getCanonicalName() + strExReturn);
            }
            String storageDirectoryName = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 0);
            Path storageTypeWordWritedFile = fsForWriteData.getPath(storageDirectoryName);
            if( !ThWordHelper.createDirIfNotExist(storageTypeWordWritedFile) ){
                String strExReturn = "Storage directory not created " + storageTypeWordWritedFile.toUri().toString();
                throw new IllegalStateException(ThWordEventLogic.class.getCanonicalName() + strExReturn);
            }
            Boolean isDataToVol = Boolean.FALSE;
            if( pollTypeWordTagFileNameData.size() > AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT ){
                isDataToVol = Boolean.TRUE;
            }
            Integer datafsVolumeNumber = 
                this.wordStatusMainFlow.getValueForMainFlowUuidByNumberDataFs(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 1);
            Integer localSrcSize = 0;
            Integer localDestSize = 0;
            localDestSize = pollTypeWordTagFileNameData.size();
            String prefixFileName = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 4);

            writedFromCacheData = new ConcurrentSkipListMap<UUID, TdataWord>();
            do {
                pollFirstEntry = pollTypeWordTagFileNameData.pollFirstEntry();
                if( pollFirstEntry != null ){
                    writedFromCacheData.put(pollFirstEntry.getKey(), pollFirstEntry.getValue());
                }
                localDestSize = writedFromCacheData.size();
                localIsLimitForWrite = ( localDestSize == AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT );
                if( localIsLimitForWrite || pollTypeWordTagFileNameData.isEmpty() ){
                    String localSrcFileName = ThWordHelper.fileNameBuilder(prefixFileName, localSrcSize, datafsVolumeNumber);
                    Path localNowWritedFile = fsForWriteData.getPath(storageTypeWordWritedFile.toString(), localSrcFileName);
                    Boolean localIsWritedToStorage = ThWordHelper.writeDataToStorage(localNowWritedFile, writedFromCacheData);
                    if( !localIsWritedToStorage ){
                        //if not write data
                        localIsUuidFinished = Boolean.FALSE;
                    }
                    this.wordStatusMainFlow.changeInWorkers(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 0, Boolean.TRUE);
                    
                    Boolean workersIsNeedDeleteOldFile = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberWorkers(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 10);
                    if( workersIsNeedDeleteOldFile ){
                        String nameForDeleteFileName = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 3);
                        Path localDeleteOldFileName = fsForWriteData.getPath(storageTypeWordWritedFile.toString(), nameForDeleteFileName);
                        Boolean isFileDeleted = ThWordHelper.deleteFileFromStorage(localDeleteOldFileName);
                        if( !isFileDeleted ){
                            //if not delete file
                            localIsUuidFinished = Boolean.FALSE;
                        }
                        this.wordStatusMainFlow.changeInWorkers(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 11, Boolean.TRUE);
                    } else {
                        localIsUuidFinished = Boolean.FALSE;
                    }
                    
                    String newFileName = ThWordHelper.fileNameBuilder(prefixFileName, localDestSize, datafsVolumeNumber);
                    Path localFileNameForMove = fsForWriteData.getPath(storageTypeWordWritedFile.toString(), newFileName);
                    localPrevDataMoved = ThWordHelper.moveAfterWrite(localNowWritedFile, localFileNameForMove);
                    if( !localPrevDataMoved ){
                        //if not move file
                        localIsUuidFinished = Boolean.FALSE;
                        this.wordStatusMainFlow.changeInName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 1, localSrcFileName);
                    } else {
                        this.wordStatusMainFlow.changeInName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 1, newFileName);
                    }
                    this.wordStatusMainFlow.changeInWorkers(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 2, Boolean.TRUE);
                    this.wordStatusMainFlow.changeInName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 2, newFileName);
                    if( pollTypeWordTagFileNameData.isEmpty() ){
                        this.wordStatusMainFlow.changeInWorkers(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 7, Boolean.TRUE);
                    }
                    if( localPrevDataWrited && localPrevDataMoved ){ 
                        if( localIsLimitForWrite ){
                            datafsVolumeNumber++;
                        }
                        writedFromCacheData = ThWordHelper.doUtilizationDataInitNew(writedFromCacheData);
                    }
                }
            } while( !pollTypeWordTagFileNameData.isEmpty() );
            //utilize writed data
            if( localIsUuidFinished ){
                //recode for delete from events
                Boolean removeAllFlowStatusByUUID = this.wordStatusMainFlow.removeAllFlowStatusByUUID(pollNextUuid);
                removeAllFlowStatusByUUID = null;
            } else {
                //code change events state for init read
                this.wordState.getBusEventShortNextStep().addUuidToShortEvent(0, 1, pollNextUuid);
                this.wordState.getBusEventShort().addUuidToShortEvent(2, 2, pollNextUuid);
            }
            
        } finally {
            
        }
    }
    protected void readDataFromStorage(FileSystem fsForReadData, UUID pollNextUuid){
        try {
            String hexTagNameByMainFlowUuid = this.eventIndex.getHexTagNameByMainFlowUuid(pollNextUuid);
            String subStringByMainFlowUuid = this.eventIndex.getSubStringByMainFlowUuid(pollNextUuid);
            Integer typeWordByMainFlowUuid = this.eventIndex.getTypeWordByMainFlowUuid(pollNextUuid);
            Boolean valueForMainFlowUuidByNumberWorkers = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberWorkers(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 0);
            String storageDirectoryName = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 0);
            String currentFileName = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 1);
            String newFileName = this.wordStatusMainFlow.getValueForMainFlowUuidByNumberName(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 2);
            if( currentFileName.equalsIgnoreCase(newFileName) ){
                Path forReadFileName = fsForReadData.getPath(storageDirectoryName, currentFileName);
                ConcurrentSkipListMap<UUID, TdataWord> readedFromStorageData = new ConcurrentSkipListMap<UUID, TdataWord>();
                if( Files.exists(forReadFileName) ){
                    readedFromStorageData.putAll(ThWordHelper.readFromFile(forReadFileName));
                    //insert into cache readed, after that insert into cache
                    Boolean isReadedDataValide = Boolean.TRUE;
                    for( Map.Entry<UUID, TdataWord> forValidate : readedFromStorageData.entrySet() ){
                        if( !ThWordHelper.isTdataWordValid(forValidate.getValue()) ){
                            isReadedDataValide = Boolean.FALSE;
                        }
                    }
                    if( isReadedDataValide && (!readedFromStorageData.isEmpty()) ){
                        Boolean isCacheReaded = this.wordCacheReaded.addAllDataIntoCache(readedFromStorageData);
                        this.wordStatusMainFlow.changeInWorkers(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 4, isCacheReaded);
                        Integer sizeReadedData = readedFromStorageData.size();
                        this.wordStatusMainFlow.changeInDataCache(typeWordByMainFlowUuid, subStringByMainFlowUuid, hexTagNameByMainFlowUuid, pollNextUuid, 1, sizeReadedData);
                        this.wordState.getBusEventShortNextStep().addUuidToShortEvent(0, 3, pollNextUuid);
                        this.wordState.getBusEventShort().addUuidToShortEvent(2, 1, pollNextUuid);
                    }
                    //change main flow params
                }
            }
        } finally {
            
        }
    }
    protected Boolean insertFromReadedToCache(Integer typeWordOfBusOutput, 
            String hexTagNameFromBusOutput, 
            String subStringFromBusOutput){
        //
        ConcurrentSkipListMap<UUID, TdataWord> pollTypeWordTagFileNameData = this.wordCacheReaded.pollTypeWordTagFileNameData(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput);
        if( pollTypeWordTagFileNameData != null ){
            if( !pollTypeWordTagFileNameData.isEmpty() ){
                this.wordCache.addAllDataIntoCache(pollTypeWordTagFileNameData);
                UUID pollNextUuid = this.wordState.getBusEventShortNextStep().pollNextUuid(0, 3);
                this.wordState.getBusEventShort().addUuidToShortEvent(2, 3, pollNextUuid);
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
    private UUID getExistingUuidWithReadedValues(Integer typeWordOfBusOutput, 
            String hexTagNameFromBusOutput, 
            String subStringFromBusOutput){
        UUID pollNextUuid = this.wordState.getBusEventShort().pollNextUuid(2, 3);
        String hexTagNameByMainFlowUuid = this.eventIndex.getHexTagNameByMainFlowUuid(pollNextUuid);
        String subStringByMainFlowUuid = this.eventIndex.getSubStringByMainFlowUuid(pollNextUuid);
        Integer typeWordByMainFlowUuid = this.eventIndex.getTypeWordByMainFlowUuid(pollNextUuid);
        if( typeWordByMainFlowUuid.compareTo(typeWordOfBusOutput) == 0 ){
            if( hexTagNameByMainFlowUuid.contentEquals(hexTagNameFromBusOutput) ){
                if( subStringByMainFlowUuid.contentEquals(subStringFromBusOutput) ){
                    return pollNextUuid;
                }
            }
        }
        this.wordState.getBusEventShort().addUuidToShortEvent(2, 3, pollNextUuid);
        return null;
    }
    protected void deleteOldDataFromStorage(Integer typeWordOfBusOutput, 
            String hexTagNameFromBusOutput, 
            String subStringFromBusOutput, 
            TdataWord pollFromBusOutputDataPacket){
        try {
            
        } finally {
            
        }
    }
    protected void cleanReadedCache(Integer typeWordOfBusOutput, 
            String hexTagNameFromBusOutput, 
            String subStringFromBusOutput, 
            TdataWord pollFromBusOutputDataPacket){
        try {
            
        } finally {
            
        }
    }
    protected void cleanCache(Integer typeWordOfBusOutput, 
            String hexTagNameFromBusOutput, 
            String subStringFromBusOutput, 
            TdataWord pollFromBusOutputDataPacket){
        try {
            
        } finally {
            
        }
    }
    /**
     * if true than from OuterBus not poll data for insert into cache
     * true when writer run poll data from cache in local variable for write
     * when data saved in local variable this procedure return false
     * @return 
     */
    protected Boolean isStateChangedCacheWrite(){
        return Boolean.FALSE;
    }
    /**
     * when data exist in storage need read data before new write it
     */
    protected Boolean isNeedReadDataFromFs(){
        return Boolean.TRUE;
    }
    /**
     * if true than data from cache readed insert into cache
     */
    protected Boolean isStateChangedReadedCache(){
        return Boolean.FALSE;
    }
    
}
