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
 * 
 * @author wladimirowichbiaran
 */
public class ThWordState {
    private final Long timeCreation;
    private final UUID objectLabel;
    /**
     * ThWordBusWriter
     */
    private ThWordBusFlowEvent busWordRouterJobToWriter;
    private Boolean isSetWordRouterJobToWriter;
    /**
     * ThWordBusReader
     */
    private ThWordBusFlowEvent busWordRouterJobToReader;
    private Boolean isSetWordRouterJobToReader;
    /**
     * ThWordBusReadedFlow thWordFlowRead
     * @todo Bus into State
     */
    private ThWordBusFlowEvent thWordFlowRead;
    private Boolean isSetWordFlowReaded;
    private ConcurrentSkipListMap<Integer, ThWordBusFlowEvent> eventsBusObjectList;
    private ThWordEventLogic eventsLogic;
    private ThWordEventIndex eventsIndex;
    private ThWordStatusMainFlow mainFlow;
    public ThWordState(ThWordRule ruleWordInputed) {
        this.mainFlow = (ThWordStatusMainFlow) ruleWordInputed.getWordStatusMainFlow();
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        newInstanceOfListBus(ruleWordInputed);
        newInstanceEventIndex(ruleWordInputed);
        newInstanceEventLogic(ruleWordInputed);
        
        setFalseWordRouterJobToWriter();
        setFalseWordRouterJobToReader();
        /**
         * ThWordFlowReaded
         */
        setFalseWordFlowReaded();
    }
    private void newInstanceEventLogic(final ThWordRule ruleInputed){
        this.eventsLogic = new ThWordEventLogic(ruleInputed);
    }
    private void newInstanceEventIndex(final ThWordRule ruleInputed){
        this.eventsIndex = new ThWordEventIndex(ruleInputed);
    }
    /**
     * Create Buses of Events on create object
     * @param ruleInputed 
     */
    private void newInstanceOfListBus(final ThWordRule ruleInputed){
        Integer eventCount;
        Integer idx;
        Integer currentEventCode;
        ThWordBusFlowEvent newEventBus;
        ThWordRule ruleFunc;
        ThWordStatusMainFlow wordStatusMainFlow;
        try {
            ruleFunc = (ThWordRule) ruleInputed;
            wordStatusMainFlow = ruleFunc.getWordStatusMainFlow();
            this.eventsBusObjectList = new ConcurrentSkipListMap<Integer, ThWordBusFlowEvent>();
            eventCount = getEventCount();
            for( idx = 0; idx < eventCount ; idx++ ){
                currentEventCode = getEventCodeByNumber(idx);
                newEventBus = new ThWordBusFlowEvent(wordStatusMainFlow);
                this.eventsBusObjectList.put(currentEventCode, newEventBus);
            }
        } finally {
            eventCount = null;
            idx = null;
            currentEventCode = null;
            newEventBus = null;
            ruleFunc = null;
            wordStatusMainFlow = null;
        }
    }
    protected ThWordEventIndex getEventIndex(){
        return this.eventsIndex;
    }
    /**
     * 
     */
    protected void destructorOfListBus(){
        Integer keyRemovedEventBus;
        ThWordBusFlowEvent removedEventBus;
        try {
            for( Map.Entry<Integer, ThWordBusFlowEvent> destoyedItem : this.eventsBusObjectList.entrySet() ){
                destoyedItem.getValue().destructorBusFlowEvent();
                keyRemovedEventBus = destoyedItem.getKey();
                removedEventBus = this.eventsBusObjectList.remove(keyRemovedEventBus);
                removedEventBus = null;
                keyRemovedEventBus = null;
            }
            this.eventsBusObjectList = null;
        } finally {
            keyRemovedEventBus = null;
            removedEventBus = null;
        }
    }
    /**
     * <ul>
     * <li> 0 - fromFsDeleteDataEvent
     * <li> 1 - markProcListDeleting
     * <li> 2 - readReadyDataEvent
     * <li> 3 - markProcListReading
     * <li> 4 - writeDataFromCacheEvent
     * <li> 5 - markProcListWriting
     * <li> 6 - insertIntoCacheEvent
     * <li> 7 - markProcListInserting
     * <li> 8 - cleanReadedCacheEvent
     * <li> 9 - markProcListReadCacheCleaning
     * <li> 10 - cleanCacheEvent
     * <li> 11 - markProcListCacheCleaning
     * </ul>
     * @param numEventNameInputed
     * @return 
     */
    protected ThWordBusFlowEvent getEventBusByNumber(final Integer numEventNameInputed){
        Integer numEventNameFunc;
        Integer eventCodeByNumber;
        ThWordBusFlowEvent returnedEventBus;
        try {
            numEventNameFunc = (Integer) numEventNameInputed;
            eventCodeByNumber = getEventCodeByNumber(numEventNameFunc);
            returnedEventBus = (ThWordBusFlowEvent) this.eventsBusObjectList.get(eventCodeByNumber);
            if( returnedEventBus == null  ){
                returnedEventBus = new ThWordBusFlowEvent(this.mainFlow);
                this.eventsBusObjectList.put(eventCodeByNumber, returnedEventBus);
            }
            return returnedEventBus;
        } finally {
            numEventNameFunc = null;
            eventCodeByNumber = null;
            returnedEventBus = null;
        }
    }
    /**
     * 
     * @return 
     * @throws #java.lang.IllegalArgumentException
     */
    protected ThWordBusFlowEvent getBusJobForWordRouterJobToWriter(){
        if( !this.isWordRouterJobToWriter() ){
            throw new IllegalArgumentException("Bus jobs for output not set in " + ThWordState.class.getCanonicalName());
        }
        return (ThWordBusFlowEvent) this.busWordRouterJobToWriter;
    }
    protected void setBusJobForWordRouterJobToWriter(final ThWordBusFlowEvent busWordRouterJobToWriterOuter){
        this.busWordRouterJobToWriter = (ThWordBusFlowEvent) busWordRouterJobToWriterOuter;
        setTrueWordRouterJobToWriter();
    }
    protected void setTrueWordRouterJobToWriter(){
        this.isSetWordRouterJobToWriter = Boolean.TRUE;
    }
    protected void setFalseWordRouterJobToWriter(){
        this.isSetWordRouterJobToWriter = Boolean.FALSE;
    }
    protected Boolean isWordRouterJobToWriter(){
        if( this.isSetWordRouterJobToWriter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    /**
     * 
     * @return 
     * @throws #java.lang.IllegalArgumentException
     */
    protected ThWordBusFlowEvent getBusJobForWordRouterJobToReader(){
        if( !this.isWordRouterJobToReader() ){
            throw new IllegalArgumentException("Bus jobs for output not set in " + ThWordState.class.getCanonicalName());
        }
        return (ThWordBusFlowEvent) this.busWordRouterJobToReader;
    }
    protected void setBusJobForWordRouterJobToReader(final ThWordBusFlowEvent busWordRouterJobToReaderOuter){
        this.busWordRouterJobToReader = (ThWordBusFlowEvent) busWordRouterJobToReaderOuter;
        setTrueWordRouterJobToReader();
    }
    protected void setTrueWordRouterJobToReader(){
        this.isSetWordRouterJobToReader = Boolean.TRUE;
    }
    protected void setFalseWordRouterJobToReader(){
        this.isSetWordRouterJobToReader = Boolean.FALSE;
    }
    protected Boolean isWordRouterJobToReader(){
        if( this.isSetWordRouterJobToReader ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
     /**
     * ThWordBusReadedFlow
     * @return 
     */
    protected ThWordBusFlowEvent getWordFlowReaded(){
        if( !this.isWordFlowReaded() ){
            throw new IllegalArgumentException(ThWordBusReadedFlow.class.getCanonicalName() + " object not set in " + ThWordRule.class.getCanonicalName());
        }
        return this.thWordFlowRead;
    }
    protected void setWordFlowReaded(final ThWordBusFlowEvent stateWordOuter){
        this.thWordFlowRead = stateWordOuter;
        setTrueWordFlowReaded();
    }
    protected void setTrueWordFlowReaded(){
        this.isSetWordFlowReaded = Boolean.TRUE;
    }
    protected void setFalseWordFlowReaded(){
        this.isSetWordFlowReaded = Boolean.FALSE;
    }
    protected Boolean isWordFlowReaded(){
        if( this.isSetWordFlowReaded ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
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
     */
    private String[] getEventNames(){
        String[] returnedListEventNames;
        try {
            returnedListEventNames = new String[] {
                "fromFsDeleteDataEvent",
                "markProcListDeleting",
                "readReadyDataEvent",
                "markProcListReading",
                "writeDataFromCacheEvent",
                "markProcListWriting",
                "insertIntoCacheEvent",
                "markProcListInserting",
                "cleanReadedCacheEvent",
                "markProcListReadCacheCleaning",
                "cleanCacheEvent",
                "markProcListCacheCleaning"
            };
            return returnedListEventNames;
        } finally {
            returnedListEventNames = null;
        }
    }
    /**
     * 
     * @return count records of String array returned by
     * @see getEventNames()
     */
    private Integer getEventCount(){
        String[] eventNamesArray;
        try {
            eventNamesArray = getEventNames();
            return new Integer(eventNamesArray.length);
        } finally {
            eventNamesArray = null;
        }
    }
    /**
     * <ul>
     * <li> 0 - fromFsDeleteDataEvent
     * <li> 1 - markProcListDeleting
     * <li> 2 - readReadyDataEvent
     * <li> 3 - markProcListReading
     * <li> 4 - writeDataFromCacheEvent
     * <li> 5 - markProcListWriting
     * <li> 6 - insertIntoCacheEvent
     * <li> 7 - markProcListInserting
     * <li> 8 - cleanReadedCacheEvent
     * <li> 9 - markProcListReadCacheCleaning
     * <li> 10 - cleanCacheEvent
     * <li> 11 - markProcListCacheCleaning
     * </ul>
     * @param numEventNameInputed
     * @return 
     * @throws IllegalArgumentException
     * <ul>
     * <li> when numEventNameInputed < 0 (Zero)
     * <li> when numEventNameInputed > count event names
     * </ul>
     * @see getEventCount()
     * @see getEventNameByNumber()
     */
    private Integer getEventCodeByNumber(final Integer numEventNameInputed){
        String[] eventNamesArray;
        Integer codeForEventName;
        Integer numEventNameFunc;
        try {
            numEventNameFunc = (Integer) numEventNameInputed;
            if( numEventNameFunc < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numEventNameFunc);
            }
            eventNamesArray = getEventNames();
            if( numEventNameFunc > (eventNamesArray.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + "count parameters: " 
                                + eventNamesArray.length 
                                + ", need for return " + numEventNameFunc);
            } 
            codeForEventName = eventNamesArray[numEventNameFunc]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return new Integer(codeForEventName);
        } finally {
            eventNamesArray = null;
            codeForEventName = null;
            numEventNameFunc = null;
        }
    }
    /**
     *  
     * @param numEventNameInputed
     * @return String name
     * @see getEventNames()
     */
    private String getEventNameByNumber(final Integer numEventNameInputed){
        String[] eventNames;
        Integer numEventNameFunc;
        try {
            numEventNameFunc = (Integer) numEventNameInputed;
            if( numEventNameFunc < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " eventeters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numEventNameFunc);
            }
            eventNames = getEventNames();
            if( numEventNameFunc > (eventNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " eventeters of flow statusName in StorageWord is not valid, "
                                + "count eventeters: " 
                                + eventNames.length 
                                + ", need for return " + numEventNameFunc);
            } 
            return new String(eventNames[numEventNameFunc]);
        } finally {
            eventNames = null;
            numEventNameFunc = null;
        }
    }
}
