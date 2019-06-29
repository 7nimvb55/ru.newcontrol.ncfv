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
        Integer typeWordByMainFlowUuid;
        Integer countRecordsForReturn;
        try {
            functionMainFlowUuid = (UUID) inputedMainFlowUuid;
            hexTagNameByMainFlowUuid = this.eventIndex.getHexTagNameByMainFlowUuid(functionMainFlowUuid);
            typeWordByMainFlowUuid = this.eventIndex.getTypeWordByMainFlowUuid(functionMainFlowUuid);
            countRecordsForReturn = this.wordCache.sizeDataInCacheByTypeWordHexTagName(typeWordByMainFlowUuid, hexTagNameByMainFlowUuid);
            return new Integer(countRecordsForReturn);
        } finally {
            hexTagNameByMainFlowUuid = null;
            typeWordByMainFlowUuid = null;
            countRecordsForReturn = null;
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
        String exMessage;
        try {
            exMessage = new String();
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
            createInitMainFlow = this.wordStatusMainFlow.createInitMainFlow(pollFromBusOutputDataPacket, this.eventIndexFlow);
            eventDoBusByNumber = this.wordState.getEventDoBusByNumber(3);
            eventDoBusByNumber.addToListOfFlowEventUuids(typeWordOfBusOutput, hexTagNameFromBusOutput, subStringFromBusOutput, createInitMainFlow);
            setDataIntoCacheFlow = Boolean.FALSE;
            
            try {
                setDataIntoCacheFlow = wordCache.setDataIntoCacheFlow(pollFromBusOutputDataPacket);
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
                Integer sizeBusFlowUuids = eventReadyBusByNumberWrite.sizeBusFlowUuids(typeWordOfBusOutput, subStringFromBusOutput, hexTagNameFromBusOutput);
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
        } finally {
            createInitMainFlow = null;
            setDataIntoCacheFlow = null;
            removeAllFlowStatusByUUID = null;
            eventDoBusByNumber = null;
            eventReadyBusByNumber = null;
            exMessage = null;
            ThWordHelper.utilizeStringValues(new String[] {exMessage});
        }
    }
    protected void writeDataToStorage(Integer typeWordOfBusOutput, 
            String hexTagNameFromBusOutput, 
            String subStringFromBusOutput, 
            TdataWord pollFromBusOutputDataPacket){
        try {
            
        } finally {
            
        }
    }
    protected void readDataFromStorage(Integer typeWordOfBusOutput, 
            String hexTagNameFromBusOutput, 
            String subStringFromBusOutput, 
            TdataWord pollFromBusOutputDataPacket){
        try {
            
        } finally {
            
        }
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
