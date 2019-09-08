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
 * Contains method and values for control word index process
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
    private ConcurrentSkipListMap<Integer, ThWordBusFlowEvent> eventsBusReadyList;
    private ConcurrentSkipListMap<Integer, ThWordBusFlowEvent> eventsBusWaitList;
    private ConcurrentSkipListMap<Integer, ThWordBusFlowEvent> eventsBusDoList;
    private ThWordEventLogic eventsLogic;
    private ThWordEventIndex eventsIndex;
    private ThWordStatusMainFlow mainFlow;
    private ThWordBusEventShort eventShort;
    private ThWordBusEventShort eventShortNextStep;
    private ThWordEventIndexFlow eventIndexFlow;
    
    public ThWordState(ThWordRule ruleWordInputed) {
        this.mainFlow = (ThWordStatusMainFlow) ruleWordInputed.getWordStatusMainFlow();
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.eventIndexFlow = new ThWordEventIndexFlow();
        this.eventShort = new ThWordBusEventShort();
        this.eventShortNextStep = new ThWordBusEventShort();
        newInstanceOfReadyListBus(ruleWordInputed);
        newInstanceOfWaitListBus(ruleWordInputed);
        newInstanceOfDoListBus(ruleWordInputed);
        //newInstanceEventIndex(ruleWordInputed);
        //newInstanceEventLogic(ruleWordInputed);
        
        setFalseWordRouterJobToWriter();
        setFalseWordRouterJobToReader();
        /**
         * ThWordFlowReaded
         */
        setFalseWordFlowReaded();
    }
    protected ThWordEventIndexFlow getEventIndexFlow(){
        return this.eventIndexFlow;
    }
    /**
     * Object for add Uuid current event short state by buses types: wait, do, ready
     * used with getBusEventShortNextStep()
     * @return 
     */
    protected ThWordBusEventShort getBusEventShort(){
        return this.eventShort;
    }
    /**
     * Object for add Uuid next step event short by buses types: wait, do, ready
     * for example:
     * insert data into cache, create Uuid, set params, insert created uuid in
     * BusEventShot in part 2 (ready), 3(insertCache) also eventLogic make insert
     * uuid in BusEventShot next step in part 0 (wait), 2 (writeToStorage)
     * if not need read data from storage before write else
     * uuid in BusEventShot next step in part 0 (wait), 1 (ReadDataFromStorage)
     * (when early data writed...)
     * 
     * when writer from BusEventShot get next job uuid, compare with next step
     * if uuid next step not found in need position of table, result [-1,-1]
     * than value not runned in logic writer
     * 
     * @todo queue not empty, for check and not create new uuids when insert 
     * data into cache
     * 
     * 
     * 
     * @return 
     */
    protected ThWordBusEventShort getBusEventShortNextStep(){
        return this.eventShortNextStep;
    }
    protected void newInstanceEventLogic(final ThWordRule ruleInputed){
        if( this.eventsLogic == null ){
            this.eventsLogic = new ThWordEventLogic(ruleInputed);
        }
    }
    protected void newInstanceEventIndex(final ThWordRule ruleInputed){
        if( this.eventsIndex == null ){
            this.eventsIndex = new ThWordEventIndex(ruleInputed);
        }
    }
    /**
     * Create Buses for list objects of Ready Events
     * @param ruleInputed 
     */
    private void newInstanceOfReadyListBus(final ThWordRule ruleInputed){
        Integer eventReadyCount;
        Integer idxReady;
        Integer currentEventReadyCode;
        ThWordBusFlowEvent newEventReadyBus;
        ThWordRule ruleFunc;
        ThWordStatusMainFlow wordStatusMainFlow;
        try {
            ruleFunc = (ThWordRule) ruleInputed;
            wordStatusMainFlow = ruleFunc.getWordStatusMainFlow();
            this.eventsBusReadyList = new ConcurrentSkipListMap<Integer, ThWordBusFlowEvent>();
            eventReadyCount = getEventReadyCount();
            for( idxReady = 0; idxReady < eventReadyCount ; idxReady++ ){
                currentEventReadyCode = getEventReadyCodeByNumber(idxReady);
                newEventReadyBus = new ThWordBusFlowEvent(wordStatusMainFlow);
                this.eventsBusReadyList.put(currentEventReadyCode, newEventReadyBus);
            }
        } finally {
            eventReadyCount = null;
            idxReady = null;
            currentEventReadyCode = null;
            newEventReadyBus = null;
            ruleFunc = null;
            wordStatusMainFlow = null;
        }
    }
    /**
     * Create Buses for list objects of Ready Events
     * @param ruleInputed 
     */
    private void newInstanceOfWaitListBus(final ThWordRule ruleInputed){
        Integer eventWaitCount;
        Integer idxWait;
        Integer currentEventWaitCode;
        ThWordBusFlowEvent newEventWaitBus;
        ThWordRule ruleFunc;
        ThWordStatusMainFlow wordStatusMainFlow;
        try {
            ruleFunc = (ThWordRule) ruleInputed;
            wordStatusMainFlow = ruleFunc.getWordStatusMainFlow();
            this.eventsBusWaitList = new ConcurrentSkipListMap<Integer, ThWordBusFlowEvent>();
            eventWaitCount = getEventWaitCount();
            for( idxWait = 0; idxWait < eventWaitCount ; idxWait++ ){
                currentEventWaitCode = getEventWaitCodeByNumber(idxWait);
                newEventWaitBus = new ThWordBusFlowEvent(wordStatusMainFlow);
                this.eventsBusWaitList.put(currentEventWaitCode, newEventWaitBus);
            }
        } finally {
            eventWaitCount = null;
            idxWait = null;
            currentEventWaitCode = null;
            newEventWaitBus = null;
            ruleFunc = null;
            wordStatusMainFlow = null;
        }
    }
    /**
     * Create Buses for list objects of Ready Events
     * @param ruleInputed 
     */
    private void newInstanceOfDoListBus(final ThWordRule ruleInputed){
        Integer eventDoCount;
        Integer idxDo;
        Integer currentEventDoCode;
        ThWordBusFlowEvent newEventDoBus;
        ThWordRule ruleFunc;
        ThWordStatusMainFlow wordStatusMainFlow;
        try {
            ruleFunc = (ThWordRule) ruleInputed;
            wordStatusMainFlow = ruleFunc.getWordStatusMainFlow();
            this.eventsBusDoList = new ConcurrentSkipListMap<Integer, ThWordBusFlowEvent>();
            eventDoCount = getEventDoCount();
            for( idxDo = 0; idxDo < eventDoCount ; idxDo++ ){
                currentEventDoCode = getEventDoCodeByNumber(idxDo);
                newEventDoBus = new ThWordBusFlowEvent(wordStatusMainFlow);
                this.eventsBusDoList.put(currentEventDoCode, newEventDoBus);
            }
        } finally {
            eventDoCount = null;
            idxDo = null;
            currentEventDoCode = null;
            newEventDoBus = null;
            ruleFunc = null;
            wordStatusMainFlow = null;
        }
    }
    protected ThWordEventIndex getEventIndex(){
        return this.eventsIndex;
    }
    protected ThWordEventLogic getEventLogic(){
        return this.eventsLogic;
    }
    /**
     * 
     */
    protected void destructorOfListReadyBus(){
        Integer keyRemovedEventReadyBus;
        ThWordBusFlowEvent removedEventReadyBus;
        try {
            for( Map.Entry<Integer, ThWordBusFlowEvent> destoyedItem : this.eventsBusReadyList.entrySet() ){
                destoyedItem.getValue().destructorBusFlowEvent();
                keyRemovedEventReadyBus = destoyedItem.getKey();
                removedEventReadyBus = this.eventsBusReadyList.remove(keyRemovedEventReadyBus);
                removedEventReadyBus = null;
                keyRemovedEventReadyBus = null;
            }
            this.eventsBusReadyList = null;
        } finally {
            keyRemovedEventReadyBus = null;
            removedEventReadyBus = null;
        }
    }
    /**
     * 
     */
    protected void destructorOfListWaitBus(){
        Integer keyRemovedEventWaitBus;
        ThWordBusFlowEvent removedEventWaitBus;
        try {
            for( Map.Entry<Integer, ThWordBusFlowEvent> destoyedItem : this.eventsBusWaitList.entrySet() ){
                destoyedItem.getValue().destructorBusFlowEvent();
                keyRemovedEventWaitBus = destoyedItem.getKey();
                removedEventWaitBus = this.eventsBusWaitList.remove(keyRemovedEventWaitBus);
                removedEventWaitBus = null;
                keyRemovedEventWaitBus = null;
            }
            this.eventsBusWaitList = null;
        } finally {
            keyRemovedEventWaitBus = null;
            removedEventWaitBus = null;
        }
    }
    /**
     * 
     */
    protected void destructorOfListDoBus(){
        Integer keyRemovedEventDoBus;
        ThWordBusFlowEvent removedEventDoBus;
        try {
            for( Map.Entry<Integer, ThWordBusFlowEvent> destoyedItem : this.eventsBusDoList.entrySet() ){
                destoyedItem.getValue().destructorBusFlowEvent();
                keyRemovedEventDoBus = destoyedItem.getKey();
                removedEventDoBus = this.eventsBusDoList.remove(keyRemovedEventDoBus);
                removedEventDoBus = null;
                keyRemovedEventDoBus = null;
            }
            this.eventsBusDoList = null;
        } finally {
            keyRemovedEventDoBus = null;
            removedEventDoBus = null;
        }
    }
    /**
     * <ul>
     * <li>  0  - eventDeleteOldDataFromStorage
     * <li>  1  - eventReadDataFromStorage
     * <li>  2  - eventWriteDataToStorage
     * <li>  3  - eventInsertIntoCache
     * <li>  4  - eventCleanReadedCache
     * <li>  5  - eventCleanCache
     * </ul>
     * @param numEventNameInputed
     * @return 
     */
    protected ThWordBusFlowEvent getEventReadyBusByNumber(final Integer numEventNameInputed){
        Integer numEventReadyNameFunc;
        Integer eventReadyCodeByNumber;
        ThWordBusFlowEvent returnedEventReadyBus;
        try {
            numEventReadyNameFunc = (Integer) numEventNameInputed;
            eventReadyCodeByNumber = getEventReadyCodeByNumber(numEventReadyNameFunc);
            returnedEventReadyBus = (ThWordBusFlowEvent) this.eventsBusReadyList.get(eventReadyCodeByNumber);
            if( returnedEventReadyBus == null  ){
                returnedEventReadyBus = new ThWordBusFlowEvent(this.mainFlow);
                this.eventsBusReadyList.put(eventReadyCodeByNumber, returnedEventReadyBus);
            }
            return returnedEventReadyBus;
        } finally {
            numEventReadyNameFunc = null;
            eventReadyCodeByNumber = null;
            returnedEventReadyBus = null;
        }
    }
    /**
     * <ul>
     * <li>  0  - waitDeleteOldDataFromStorage
     * <li>  1  - waitReadDataFromStorage
     * <li>  2  - waitWriteDataToStorage
     * <li>  3  - waitInsertIntoCache
     * <li>  4  - waitCleanReadedCache
     * <li>  5  - waitCleanCache
     * </ul>
     * @param numEventNameInputed
     * @return 
     */
    protected ThWordBusFlowEvent getEventWaitBusByNumber(final Integer numEventNameInputed){
        Integer numEventWaitNameFunc;
        Integer eventWaitCodeByNumber;
        ThWordBusFlowEvent returnedEventWaitBus;
        try {
            numEventWaitNameFunc = (Integer) numEventNameInputed;
            eventWaitCodeByNumber = getEventWaitCodeByNumber(numEventWaitNameFunc);
            returnedEventWaitBus = (ThWordBusFlowEvent) this.eventsBusWaitList.get(eventWaitCodeByNumber);
            if( returnedEventWaitBus == null  ){
                returnedEventWaitBus = new ThWordBusFlowEvent(this.mainFlow);
                this.eventsBusWaitList.put(eventWaitCodeByNumber, returnedEventWaitBus);
            }
            return returnedEventWaitBus;
        } finally {
            numEventWaitNameFunc = null;
            eventWaitCodeByNumber = null;
            returnedEventWaitBus = null;
        }
    }
    /**
     * <ul>
     * <li>  0  - doDeleteOldDataFromStorage
     * <li>  1  - doReadDataFromStorage
     * <li>  2  - doWriteDataToStorage
     * <li>  3  - doInsertIntoCache
     * <li>  4  - doCleanReadedCache
     * <li>  5  - doCleanCache
     * </ul>
     * @param numEventNameInputed
     * @return 
     */
    protected ThWordBusFlowEvent getEventDoBusByNumber(final Integer numEventNameInputed){
        Integer numEventDoNameFunc;
        Integer eventDoCodeByNumber;
        ThWordBusFlowEvent returnedEventDoBus;
        try {
            numEventDoNameFunc = (Integer) numEventNameInputed;
            eventDoCodeByNumber = getEventDoCodeByNumber(numEventDoNameFunc);
            returnedEventDoBus = null;
            try {
                returnedEventDoBus = (ThWordBusFlowEvent) this.eventsBusDoList.get(eventDoCodeByNumber);
            } catch(ClassCastException exClassCast) {
                System.err.println(exClassCast.getMessage());
                exClassCast.getStackTrace();
            } catch(NullPointerException exNullPoint) {
                System.err.println(exNullPoint.getMessage());
                exNullPoint.getStackTrace();
            }
            //ClassCastException - if the specified key cannot be compared with the keys currently in the map NullPointerException
            if( returnedEventDoBus == null  ){
                returnedEventDoBus = new ThWordBusFlowEvent(this.mainFlow);
                this.eventsBusDoList.put(eventDoCodeByNumber, returnedEventDoBus);
            }
            return returnedEventDoBus;
        } finally {
            numEventDoNameFunc = null;
            eventDoCodeByNumber = null;
            returnedEventDoBus = null;
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
     * <ul>
     * <li>  0  - eventDeleteOldDataFromStorage
     * <li>  1  - eventReadDataFromStorage
     * <li>  2  - eventWriteDataToStorage
     * <li>  3  - eventInsertIntoCache
     * <li>  4  - eventCleanReadedCache
     * <li>  5  - eventCleanCache
     * </ul>
     * @return 
     */
    private String[] getEventReadyNames(){
        String[] returnedListEventReadyNames;
        try {
            returnedListEventReadyNames = new String[] {
                "eventDeleteOldDataFromStorage",
                "eventReadDataFromStorage",
                "eventWriteDataToStorage",
                "eventInsertIntoCache",
                "eventCleanReadedCache",
                "eventCleanCache",
            };
            return returnedListEventReadyNames;
        } finally {
            returnedListEventReadyNames = null;
        }
    }
    /**
     * <ul>
     * <li>  0  - waitDeleteOldDataFromStorage
     * <li>  1  - waitReadDataFromStorage
     * <li>  2  - waitWriteDataToStorage
     * <li>  3  - waitInsertIntoCache
     * <li>  4  - waitCleanReadedCache
     * <li>  5  - waitCleanCache
     * </ul>
     * @return 
     */
    private String[] getEventWaitNames(){
        String[] returnedListEventWaitNames;
        try {
            returnedListEventWaitNames = new String[] {
                "waitDeleteOldDataFromStorage",
                "waitReadDataFromStorage",
                "waitWriteDataToStorage",
                "waitInsertIntoCache",
                "waitCleanReadedCache",
                "waitCleanCache",
            };
            return returnedListEventWaitNames;
        } finally {
            returnedListEventWaitNames = null;
        }
    }
    /**
     * <ul>
     * <li>  0  - doDeleteOldDataFromStorage
     * <li>  1  - doReadDataFromStorage
     * <li>  2  - doWriteDataToStorage
     * <li>  3  - doInsertIntoCache
     * <li>  4  - doCleanReadedCache
     * <li>  5  - doCleanCache
     * </ul>
     * @return 
     */
    private String[] getEventDoNames(){
        String[] returnedListEventDoNames;
        try {
            returnedListEventDoNames = new String[] {
                "doDeleteOldDataFromStorage",
                "doReadDataFromStorage",
                "doWriteDataToStorage",
                "doInsertIntoCache",
                "doCleanReadedCache",
                "doCleanCache",
            };
            return returnedListEventDoNames;
        } finally {
            returnedListEventDoNames = null;
        }
    }
    /**
     * 
     * @return count records of String array returned by
     * @see getEventNames()
     */
    private Integer getEventReadyCount(){
        String[] eventNamesArray;
        try {
            eventNamesArray = getEventReadyNames();
            return new Integer(eventNamesArray.length);
        } finally {
            eventNamesArray = null;
        }
    }
    /**
     * 
     * @return 
     */
    private Integer getEventWaitCount(){
        String[] eventNamesArray;
        try {
            eventNamesArray = getEventWaitNames();
            return new Integer(eventNamesArray.length);
        } finally {
            eventNamesArray = null;
        }
    }
    /**
     * 
     * @return 
     */
    private Integer getEventDoCount(){
        String[] eventNamesArray;
        try {
            eventNamesArray = getEventDoNames();
            return new Integer(eventNamesArray.length);
        } finally {
            eventNamesArray = null;
        }
    }
    /**
     * event type flow points with indexing
     * process Start(s), End(e) part of logic algoritm
     * t...(01...09)
     * UUID01, UUID02, UUID03, UUID04
     * <ul>                                     DataFromBus                 DataReaded          SetOldFileName      delFN-UUID      moveFN
     * <li> 0  - eventDeleteOldDataFromStorage                                                                      del-UUID01andOldFileFromFs
     * <li> 1  - waitDeleteOldDataFromStorage                                                                       (08e)UUID01
     * <li> 2  - doDeleteOldDataFromStorage                                                     (05s)UUID01
     *          - when algoritm poll UUID from 1 insert into 2 when end do insert into 0
     *          - writer delete file after write before move
     * <li> 3  - eventReadDataFromStorage                                   (04e)UUID01
     *          - poll from cacheReaded insert into cache
     * <li> 4  - waitReadDataFromStorage
     * <li> 5  - doReadDataFromStorage                                      (03s)UUID01
     *          - reader read, insert into cacheReaded
     *          - set UUID flag in (1)
     * <li> 6  - eventWriteDataToStorage       (01e)UUID01, (07e)UUID02                                                            (02e)UUID01, (09e)UUID02
     *          - set UUID flag in (4)
     * <li> 7  - waitWriteDataToStorage
     * <li> 8  - doWriteDataToStorage          (00s)UUID01, (06e)UUID02
     *          - writer, poll from cache existing data packet
     *          - get storage path
     *          - if need write limited files with incremental volume number
     *          - build new file name with him volume number
     *          - write data to thisFile
     *          - build file name for move movedFile, 
     *          - if equal oldFile (from 1 rename it, change rename in 1)
     *          - move thisFile to movedFile
     *          - delete oldFile (from 1 or renamedOldFile)
     *          - delete UUID00 contined oldFile (from 1)
     * <li> 9  - eventInsertIntoCache
     *          - if not UUID01 in (1 || 8) than new UUIDnextNum
     *          - else if UUID01 in (7) nothing 
     * <li> 10 - waitInsertIntoCache
     * <li> 11 - doInsertIntoCache             (new)UUID01
     * <li> 12 - eventCleanReadedCache         (set)UUID01
     * <li> 13 - waitCleanReadedCache
     * <li> 14 - doCleanReadedCache
     * <li> 15 - eventCleanCache
     * <li> 16 - waitCleanCache
     * <li> 17 - doCleanCache
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
    private Integer getEventReadyCodeByNumber(final Integer numEventReadyNameInputed){
        String[] eventNamesArray;
        Integer codeForEventReadyName;
        Integer numEventReadyNameFunc;
        try {
            numEventReadyNameFunc = (Integer) numEventReadyNameInputed;
            if( numEventReadyNameFunc < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numEventReadyNameFunc);
            }
            eventNamesArray = getEventReadyNames();
            if( numEventReadyNameFunc > (eventNamesArray.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + "count parameters: " 
                                + eventNamesArray.length 
                                + ", need for return " + numEventReadyNameFunc);
            } 
            codeForEventReadyName = eventNamesArray[numEventReadyNameFunc]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return new Integer(codeForEventReadyName);
        } finally {
            eventNamesArray = null;
            codeForEventReadyName = null;
            numEventReadyNameFunc = null;
        }
    }
    /**
     * 
     * @param numEventWaitNameInputed
     * @return 
     */
    private Integer getEventWaitCodeByNumber(final Integer numEventWaitNameInputed){
        String[] eventNamesArray;
        Integer codeForEventWaitName;
        Integer numEventWaitNameFunc;
        try {
            numEventWaitNameFunc = (Integer) numEventWaitNameInputed;
            if( numEventWaitNameFunc < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numEventWaitNameFunc);
            }
            eventNamesArray = getEventWaitNames();
            if( numEventWaitNameFunc > (eventNamesArray.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + "count parameters: " 
                                + eventNamesArray.length 
                                + ", need for return " + numEventWaitNameFunc);
            } 
            codeForEventWaitName = eventNamesArray[numEventWaitNameFunc]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return new Integer(codeForEventWaitName);
        } finally {
            eventNamesArray = null;
            codeForEventWaitName = null;
            numEventWaitNameFunc = null;
        }
    }
    /**
     * 
     * @param numEventDoNameInputed
     * @return 
     */
    private Integer getEventDoCodeByNumber(final Integer numEventDoNameInputed){
        String[] eventNamesArray;
        Integer codeForEventDoName;
        Integer numEventDoNameFunc;
        String strEventName;
        try {
            numEventDoNameFunc = (Integer) numEventDoNameInputed;
            if( numEventDoNameFunc < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numEventDoNameFunc);
            }
            eventNamesArray = getEventDoNames();
            if( numEventDoNameFunc > (eventNamesArray.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + "count parameters: " 
                                + eventNamesArray.length 
                                + ", need for return " + numEventDoNameFunc);
            } 
            strEventName = eventNamesArray[numEventDoNameFunc];
            codeForEventDoName = strEventName
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return new Integer(codeForEventDoName);
        } finally {
            eventNamesArray = null;
            codeForEventDoName = null;
            numEventDoNameFunc = null;
        }
    }
    /**
     *  
     * @param numEventNameInputed
     * @return String name
     * @see getEventNames()
     */
    private String getEventReadyNameByNumber(final Integer numEventReadyNameInputed){
        String[] eventNames;
        Integer numEventReadyNameFunc;
        try {
            numEventReadyNameFunc = (Integer) numEventReadyNameInputed;
            if( numEventReadyNameFunc < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " eventeters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numEventReadyNameFunc);
            }
            eventNames = getEventReadyNames();
            if( numEventReadyNameFunc > (eventNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " eventeters of flow statusName in StorageWord is not valid, "
                                + "count eventeters: " 
                                + eventNames.length 
                                + ", need for return " + numEventReadyNameFunc);
            } 
            return new String(eventNames[numEventReadyNameFunc]);
        } finally {
            eventNames = null;
            numEventReadyNameFunc = null;
        }
    }
    /**
     * 
     * @param numEventWaitNameInputed
     * @return 
     */
    private String getEventWaitNameByNumber(final Integer numEventWaitNameInputed){
        String[] eventNames;
        Integer numEventWaitNameFunc;
        try {
            numEventWaitNameFunc = (Integer) numEventWaitNameInputed;
            if( numEventWaitNameFunc < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " eventeters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numEventWaitNameFunc);
            }
            eventNames = getEventWaitNames();
            if( numEventWaitNameFunc > (eventNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " eventeters of flow statusName in StorageWord is not valid, "
                                + "count eventeters: " 
                                + eventNames.length 
                                + ", need for return " + numEventWaitNameFunc);
            } 
            return new String(eventNames[numEventWaitNameFunc]);
        } finally {
            eventNames = null;
            numEventWaitNameFunc = null;
        }
    }
    /**
     * 
     * @param numEventDoNameInputed
     * @return 
     */
    private String getEventDoNameByNumber(final Integer numEventDoNameInputed){
        String[] eventNames;
        Integer numEventDoNameFunc;
        try {
            numEventDoNameFunc = (Integer) numEventDoNameInputed;
            if( numEventDoNameFunc < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " eventeters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numEventDoNameFunc);
            }
            eventNames = getEventDoNames();
            if( numEventDoNameFunc > (eventNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " eventeters of flow statusName in StorageWord is not valid, "
                                + "count eventeters: " 
                                + eventNames.length 
                                + ", need for return " + numEventDoNameFunc);
            } 
            return new String(eventNames[numEventDoNameFunc]);
        } finally {
            eventNames = null;
            numEventDoNameFunc = null;
        }
    }
}
