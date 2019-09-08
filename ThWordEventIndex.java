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
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * <ol>
 * <li> when transfer data Event to Proc or Proc to Event set flag 
 *      transEvPr or transPrEv
 * <li> Bus where index name start with markProcList is Queue for UUID of main
 *      flow point who need do it part of algoritm logic
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
 * 
 * this class for transfer UUIDs 
 *  from markProcListDeleting to fromFsDeleteDataEvent when process end
 *  from fromFsDeleteDataEvent to markProcListReading when ThWordLogicRouter
 *      read data from bus for this data type and cached his
 *      or read data from cacheReaded for this data type and cached his
 *  from markProcListReading to readReadyDataEvent when process end
 * @author wladimirowichbiaran
 */
public class ThWordEventIndex {
    private final Long timeCreation;
    private final UUID objectLabel;
    private ThWordStatusMainFlow wordStatusMainFlow;
    private ThWordState wordState;
    private ConcurrentSkipListMap<UUID, String> idxMainFlowHexTagName;
    private ConcurrentSkipListMap<UUID, String> idxMainFlowSubString;
    private ConcurrentSkipListMap<UUID, Integer> idxMainFlowTypeWord;
    private ConcurrentSkipListMap<UUID, Integer> idxMainFlowEventBusNumber;
    
    public ThWordEventIndex(ThWordRule ruleInputed) {
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.wordStatusMainFlow = ruleInputed.getWordStatusMainFlow();
        this.wordState = ruleInputed.getWordState();
        this.idxMainFlowHexTagName = new ConcurrentSkipListMap<UUID, String>();
        this.idxMainFlowSubString = new ConcurrentSkipListMap<UUID, String>();
        this.idxMainFlowTypeWord = new ConcurrentSkipListMap<UUID, Integer>();
    }
    /**
     * 
     * @param mainFlowUUID
     * @return value in index
     * @throws IllegalArgumentException if index not have value
     */
    protected String getHexTagNameByMainFlowUuid(UUID mainFlowUUID){
        String getHexTagName;
        try {
            getHexTagName = this.idxMainFlowHexTagName.get(mainFlowUUID);
            if( getHexTagName == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " returned HexTagName is null");
            }
            return getHexTagName;
        } finally {
            getHexTagName = null;
        }
    }
    /**
     * 
     * @param mainFlowUUID
     * @return value in index
     * @throws IllegalArgumentException if index not have value
     */
    protected Integer getTypeWordByMainFlowUuid(UUID mainFlowUUID){
        Integer getTypeWord;
        try {
            getTypeWord = this.idxMainFlowTypeWord.get(mainFlowUUID);
            if( getTypeWord == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " returned TypeWord is null");
            }
            return getTypeWord;
        } finally {
            getTypeWord = null;
        }
    }
    /**
     * 
     * @param mainFlowUUID
     * @return value in index
     * @throws IllegalArgumentException if index not have value
     */
    protected String getSubStringByMainFlowUuid(UUID mainFlowUUID){
        String getSubString;
        try {
            getSubString = this.idxMainFlowSubString.get(mainFlowUUID);
            if( getSubString == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " returned SubString is null");
            }
            return getSubString;
        } finally {
            getSubString = null;
        }
    }
    /**
     * 
     * @param inputedMainFlowUUID
     * @param inputedTypeWord 
     * @throws NullPointerException for null or empty arguments
     * @throws IllegalArgumentException if in index exist value
     */
    protected void putMainFlowUuidTypeWord(UUID inputedMainFlowUUID, Integer inputedTypeWord){
        Integer funcTypeWord;
        UUID funcMainFlowUUID;
        Integer pervValue;
        try {
            funcTypeWord = (Integer) inputedTypeWord;
            funcMainFlowUUID = (UUID) inputedMainFlowUUID;
            if( funcTypeWord == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " inputed TypeWord is null");
            }
            if( funcMainFlowUUID == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " inputed MainFlowUUID is null");
            }
            pervValue = this.idxMainFlowTypeWord.put(funcMainFlowUUID, funcTypeWord);
            if( pervValue != null ){
                this.idxMainFlowTypeWord.put(funcMainFlowUUID, pervValue);
                throw new IllegalArgumentException(ThWordEventIndex.class.getCanonicalName() 
                        + " value: " + String.valueOf(pervValue)
                        + " for UUID: " + funcMainFlowUUID.toString()
                        + " exist in index, new value: " + String.valueOf(funcTypeWord)
                        + " not has been apply");
            }
        } finally {
            funcTypeWord = null;
            funcMainFlowUUID = null;
            pervValue = null;
        }
    }
    /**
     * 
     * @param inputedMainFlowUUID
     * @param inputedHexTagName 
     * @throws NullPointerException for null or empty arguments
     * @throws IllegalArgumentException if in index exist value
     */
    protected void putMainFlowUuidHexTagName(UUID inputedMainFlowUUID, String inputedHexTagName){
        String funcHexTagName;
        UUID funcMainFlowUUID;
        String pervValue;
        try {
            funcHexTagName = (String) inputedHexTagName;
            funcMainFlowUUID = (UUID) inputedMainFlowUUID;
            if( funcHexTagName == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " inputed HexTagName is null");
            }
            if( funcHexTagName.isEmpty() ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " inputed HexTagName is empty");
            }
            if( funcMainFlowUUID == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " inputed MainFlowUUID is null");
            }
            pervValue = this.idxMainFlowHexTagName.put(funcMainFlowUUID, funcHexTagName);
            if( pervValue != null ){
                this.idxMainFlowHexTagName.put(funcMainFlowUUID, pervValue);
                throw new IllegalArgumentException(ThWordEventIndex.class.getCanonicalName() 
                        + " value: " + String.valueOf(pervValue)
                        + " for UUID: " + funcMainFlowUUID.toString()
                        + " exist in index, new value: " + String.valueOf(funcHexTagName)
                        + " not has been apply");
            }
        } finally {
            funcHexTagName = null;
            funcMainFlowUUID = null;
            pervValue = null;
        }
    }
    /**
     * 
     * @param inputedMainFlowUUID
     * @param inputedSubString 
     * @throws NullPointerException for null or empty arguments
     * @throws IllegalArgumentException if in index exist value
     */
    protected void putMainFlowUuidSubString(UUID inputedMainFlowUUID, String inputedSubString){
        String funcSubString;
        UUID funcMainFlowUUID;
        String pervValue;
        try {
            funcSubString = (String) inputedSubString;
            funcMainFlowUUID = (UUID) inputedMainFlowUUID;
            if( funcSubString == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " inputed SubString is null");
            }
            if( funcSubString.isEmpty() ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " inputed SubString is empty");
            }
            if( funcMainFlowUUID == null ){
                throw new NullPointerException(ThWordEventIndex.class.getCanonicalName() + " inputed MainFlowUUID is null");
            }
            pervValue = this.idxMainFlowSubString.put(funcMainFlowUUID, funcSubString);
            if( pervValue != null ){
                this.idxMainFlowSubString.put(funcMainFlowUUID, pervValue);
                throw new IllegalArgumentException(ThWordEventIndex.class.getCanonicalName() 
                        + " value: " + String.valueOf(pervValue)
                        + " for UUID: " + funcMainFlowUUID.toString()
                        + " exist in index, new value: " + String.valueOf(funcSubString)
                        + " not has been apply");
            }
        } finally {
            funcSubString = null;
            funcMainFlowUUID = null;
            pervValue = null;
        }
    }
    /**
     * change in flow Workers states in EventLogic methods
     * @param typeWordInputed
     * @param tagNameInputed
     * @param checkForExistUuidInputed
     * @return 
     */
    protected Boolean changeFlowStatusProcDeletingEvent(final Integer typeWordInputed, 
            final String tagNameInputed,
            final UUID checkForExistUuidInputed){
        Integer typeWordFunc;
        String tagNameFunc;
        UUID checkForExistUuidFunc;
        Boolean uuidExistInFlowByTypeWordHexTagName;
        ThWordBusFlowEvent eventBusProcDeleting;
        ThWordBusFlowEvent eventBusEventDeleting;
        Boolean existUUIDByTypeWordHexTagName;
        try {
            typeWordFunc = (Integer) typeWordInputed;
            tagNameFunc = (String) tagNameInputed;
            checkForExistUuidFunc = (UUID) checkForExistUuidInputed;
            uuidExistInFlowByTypeWordHexTagName = this.wordStatusMainFlow.isUuidExistInFlowByTypeWordHexTagName(typeWordFunc, tagNameFunc, checkForExistUuidFunc);
            if( uuidExistInFlowByTypeWordHexTagName ){
                eventBusProcDeleting = this.wordState.getEventDoBusByNumber(0);
                eventBusEventDeleting = this.wordState.getEventReadyBusByNumber(0);
                existUUIDByTypeWordHexTagName = eventBusProcDeleting.isExistUUIDByTypeWordHexTagName(typeWordFunc, checkForExistUuidFunc, tagNameFunc);
                existUUIDByTypeWordHexTagName = eventBusEventDeleting.isExistUUIDByTypeWordHexTagName(typeWordFunc, checkForExistUuidFunc, tagNameFunc);
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            uuidExistInFlowByTypeWordHexTagName = null;
            typeWordFunc = null;
            tagNameFunc = null;
            checkForExistUuidFunc = null;
            eventBusProcDeleting = null;
            eventBusEventDeleting = null;
            existUUIDByTypeWordHexTagName = null;
        }
    }
    protected Boolean appendDataToCache(){
        return Boolean.FALSE;
    }
}
