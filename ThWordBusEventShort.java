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

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordBusEventShort {
    /**
     * Global list for queue uuids where set flags for logic stop, do, ready do
     * {@code <vectorCodeName, <mainFlowUUID>>}
     */
    private final Long timeCreation;
    private final UUID objectLabel;
    private ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> waitEventsBus;
    private ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> doEventsBus;
    private ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> readyEventsBus;
    
    ThWordBusEventShort(){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.waitEventsBus = new ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>>();
        this.doEventsBus = new ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>>();
        this.readyEventsBus = new ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>>();
        initEmptyLists();
    }
    /**
     * 
     */
    private void initEmptyLists(){
        Integer eventCodeByNumber;
        try {
            for( Integer idxPrefix = 0; idxPrefix < getEventPrefixNamesCount(); idxPrefix++ ){
                for(Integer idxEvent = 0; idxEvent < getEventNamesCount(); idxEvent++ ){
                    eventCodeByNumber = getEventCodeByNumber(idxPrefix, idxEvent);
                    if( idxPrefix == 0 ){
                        this.waitEventsBus.put(eventCodeByNumber, new LinkedTransferQueue<UUID>());
                    }
                    if( idxPrefix == 1 ){
                        this.doEventsBus.put(eventCodeByNumber, new LinkedTransferQueue<UUID>());
                    }
                    if( idxPrefix == 2 ){
                        this.readyEventsBus.put(eventCodeByNumber, new LinkedTransferQueue<UUID>());
                    }
                }
            }
        } finally {
            eventCodeByNumber = null;
        }
    }
    /**
     * default return bus for index 0 - wait
     * @param codeTypeEvent
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @return 
     */
    private ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> choiceTypeBusEvent(Integer codeTypeEvent){
        if( codeTypeEvent == 1 ){
            return this.doEventsBus;
        }
        if( codeTypeEvent == 2 ){
            return this.readyEventsBus;
        }
        return this.waitEventsBus;
    }
    /**
     * {@code [prefixNumber]*[numEventReadyNameInputed]+sufFix=indexName}
     * @param codeTypeEvent
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @param codeEventName
     * <ul>
     * <li>  0  - DeleteOldDataFromStorage
     * <li>  1  - ReadDataFromStorage
     * <li>  2  - WriteDataToStorage
     * <li>  3  - InsertIntoCache
     * <li>  4  - CleanReadedCache
     * <li>  5  - CleanCache
     * </ul>
     * @param listEvents 
     */
    protected void setShortEventFromBus(Integer codeTypeEvent, Integer codeEventName, LinkedTransferQueue<UUID> listEvents){
        //if settable value == null not set
        //if constainKey where keyCodeEventName not set
        ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> choiceTypeBusEvent;
        Integer eventCodeByNumber;
        UUID next;
        LinkedTransferQueue<UUID> getAllCurrentBusValues;
        try {
            if( listEvents != null ){
                if( !listEvents.isEmpty() ){
                    choiceTypeBusEvent = choiceTypeBusEvent(codeTypeEvent);
                    eventCodeByNumber = getEventCodeByNumber(codeTypeEvent, codeEventName);
                    getAllCurrentBusValues = choiceTypeBusEvent.get(eventCodeByNumber);
                    for( Iterator<UUID> iterator = listEvents.iterator(); iterator.hasNext(); ){
                        next = iterator.next();
                        if( !isExistUuid(codeTypeEvent, codeEventName, next) ){
                            getAllCurrentBusValues.add(next);
                        }
                    }
                }
            }
        } finally {
            choiceTypeBusEvent = null;
            eventCodeByNumber = null;
            next = null;
            getAllCurrentBusValues = null;
        }
    }
    /**
     * {@code [prefixNumber]*[numEventReadyNameInputed]+sufFix=indexName}
     * @param codeTypeEvent
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @param codeEventName
     * <ul>
     * <li>  0  - DeleteOldDataFromStorage
     * <li>  1  - ReadDataFromStorage
     * <li>  2  - WriteDataToStorage
     * <li>  3  - InsertIntoCache
     * <li>  4  - CleanReadedCache
     * <li>  5  - CleanCache
     * </ul>
     * @param addedMainFlowUuid 
     */
    protected void addUuidToShortEvent(Integer codeTypeEvent, Integer codeEventName, UUID addedMainFlowUuid){
        ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> choiceTypeBusEvent;
        Integer eventCodeByNumber;
        LinkedTransferQueue<UUID> getAllCurrentBusValues;
        try {
            if( addedMainFlowUuid != null ){
                choiceTypeBusEvent = choiceTypeBusEvent(codeTypeEvent);
                eventCodeByNumber = getEventCodeByNumber(codeTypeEvent, codeEventName);
                getAllCurrentBusValues = choiceTypeBusEvent.get(eventCodeByNumber);
                if( !isExistUuid(codeTypeEvent, codeEventName, addedMainFlowUuid) ){
                    getAllCurrentBusValues.add(addedMainFlowUuid);
                }
            }
        } finally {
            choiceTypeBusEvent = null;
            eventCodeByNumber = null;
            getAllCurrentBusValues = null;
        }
    }
    /**
     * {@code [prefixNumber]*[numEventReadyNameInputed]+sufFix=indexName}
     * @param codeTypeEvent
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @param codeEventName
     * <ul>
     * <li>  0  - DeleteOldDataFromStorage
     * <li>  1  - ReadDataFromStorage
     * <li>  2  - WriteDataToStorage
     * <li>  3  - InsertIntoCache
     * <li>  4  - CleanReadedCache
     * <li>  5  - CleanCache
     * </ul>
     */
    protected UUID pollNextUuid(Integer codeTypeEvent, Integer codeEventName){
        ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> choiceTypeBusEvent;
        Integer eventCodeByNumber;
        LinkedTransferQueue<UUID> getAllCurrentBusValues;
        UUID poll;
        try {
            choiceTypeBusEvent = choiceTypeBusEvent(codeTypeEvent);
            eventCodeByNumber = getEventCodeByNumber(codeTypeEvent, codeEventName);
            getAllCurrentBusValues = choiceTypeBusEvent.get(eventCodeByNumber);
            poll = getAllCurrentBusValues.poll();
            return poll;
        } finally {
            choiceTypeBusEvent = null;
            eventCodeByNumber = null;
            getAllCurrentBusValues = null;
            poll = null;
        }
    }
    /**
     * table for next steps for Uuids
     * @param codeTypeEvent
     * @param codeEventName 
     */
    private void moveUuidToNextLogicPoint(Integer codeTypeEvent, Integer codeEventName){
        
    }
    /**
     * found where Uuids and return state numeric value, algoritm may use
     * numeric state value for compare with his number...
     * {@code [prefixNumber]*[numEventReadyNameInputed]+sufFix=indexName}=value
     * 
     * if algoritm in do write his value = codeTypeEvent(1)+codeEventName(2)
     * resulted code name may be equal with logic check point
     */
    protected LinkedTransferQueue<Integer[]> foundUuidInList(UUID checkedOnExist){
        if( checkedOnExist == null ){
            return new LinkedTransferQueue<Integer[]>();
        }
        LinkedTransferQueue<Integer[]> returnedFoundedNodes = new LinkedTransferQueue<Integer[]>();
        try {
            for( Integer idxPrefix = 0; idxPrefix < getEventPrefixNamesCount(); idxPrefix++ ){
                    for(Integer idxEvent = 0; idxEvent < getEventNamesCount(); idxEvent++ ){
                        if( isExistUuid(idxPrefix, idxEvent, checkedOnExist) ){
                            returnedFoundedNodes.add(new Integer[]{idxPrefix, idxEvent});
                        }
                    }
            }
            return returnedFoundedNodes;
        }
        finally {
            returnedFoundedNodes = null;
        }
    }
    /**
     * {@code [prefixNumber]*[numEventReadyNameInputed]+sufFix=indexName}
     * @param codeTypeEvent
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @param codeEventName
     * <ul>
     * <li>  0  - DeleteOldDataFromStorage
     * <li>  1  - ReadDataFromStorage
     * <li>  2  - WriteDataToStorage
     * <li>  3  - InsertIntoCache
     * <li>  4  - CleanReadedCache
     * <li>  5  - CleanCache
     * </ul>
     * @param checkedOnExist 
     */
    protected Boolean isExistUuid(Integer codeTypeEvent, Integer codeEventName, UUID checkedOnExist){
        ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> choiceTypeBusEvent;
        Integer eventCodeByNumber;
        UUID next;
        LinkedTransferQueue<UUID> getAllCurrentBusValues;
        try {
            choiceTypeBusEvent = choiceTypeBusEvent(codeTypeEvent);
            eventCodeByNumber = getEventCodeByNumber(codeTypeEvent, codeEventName);
            getAllCurrentBusValues = choiceTypeBusEvent.get(eventCodeByNumber);
            for( Iterator<UUID> iterator = getAllCurrentBusValues.iterator(); iterator.hasNext(); ){
                next = iterator.next();
                if( next.equals(checkedOnExist) ){
                    return Boolean.TRUE;
                }
            }
            return Boolean.FALSE;
        } finally {
            choiceTypeBusEvent = null;
            eventCodeByNumber = null;
            next = null;
            getAllCurrentBusValues = null;
        }
    }
    /**
     * {@code [prefixNumber]*[numEventReadyNameInputed]+sufFix=indexName}
     * @param codeTypeEvent
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @param codeEventName
     * <ul>
     * <li>  0  - DeleteOldDataFromStorage
     * <li>  1  - ReadDataFromStorage
     * <li>  2  - WriteDataToStorage
     * <li>  3  - InsertIntoCache
     * <li>  4  - CleanReadedCache
     * <li>  5  - CleanCache
     * </ul> 
     * @param removedMainFlowUuid 
     */
    protected Boolean removeUuid(Integer codeTypeEvent, Integer codeEventName, UUID removedMainFlowUuid){
        ConcurrentSkipListMap<Integer, LinkedTransferQueue<UUID>> choiceTypeBusEvent;
        Integer eventCodeByNumber;
        LinkedTransferQueue<UUID> getAllCurrentBusValues;
        Boolean removeResult;
        try {
            choiceTypeBusEvent = choiceTypeBusEvent(codeTypeEvent);
            eventCodeByNumber = getEventCodeByNumber(codeTypeEvent, codeEventName);
            getAllCurrentBusValues = choiceTypeBusEvent.get(eventCodeByNumber);
            
            removeResult = getAllCurrentBusValues.remove(removedMainFlowUuid);
            if( removeResult ){
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        } finally {
            choiceTypeBusEvent = null;
            eventCodeByNumber = null;
            getAllCurrentBusValues = null;
            removeResult = null;
        }
    }
    /**
     * 
     * @return 
     */
    private String[] getEventNames(){
        String[] returnedListEventReadyNames;
        try {
            returnedListEventReadyNames = new String[] {
                "DeleteOldDataFromStorage",
                "ReadDataFromStorage",
                "WriteDataToStorage",
                "InsertIntoCache",
                "CleanReadedCache",
                "CleanCache",
            };
            return returnedListEventReadyNames;
        } finally {
            returnedListEventReadyNames = null;
        }
    }
    /**
     * @return
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     */
    private String[] getPrefixEventNames(){
        return new String[]{
            "wait",
            "do",
            "ready",
        };
    }
    /**
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @param prefixNum
     * @return 
     */
    private String prefixEventName(Integer prefixNum){
        String[] oneOfReturnedPrefix;
        oneOfReturnedPrefix = getPrefixEventNames();
        try {
            return oneOfReturnedPrefix[prefixNum];
        }finally {
            ThWordHelper.utilizeStringValues(oneOfReturnedPrefix);
        }
    }
    /**
     * {@code [prefixNumber]*[numEventReadyNameInputed]+sufFix=indexName}
     * @param prefixNumber
     * <ul>
     * <li> 0 - wait
     * <li> 1 - do
     * <li> 2 - ready
     * </ul>
     * @param numEventReadyNameInputed
     * <ul>
     * <li>  0  - DeleteOldDataFromStorage
     * <li>  1  - ReadDataFromStorage
     * <li>  2  - WriteDataToStorage
     * <li>  3  - InsertIntoCache
     * <li>  4  - CleanReadedCache
     * <li>  5  - CleanCache
     * </ul>
     * @return 
     */
    private Integer getEventCodeByNumber(final Integer prefixNumber, final Integer numEventReadyNameInputed){
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
            eventNamesArray = getEventNames();
            if( numEventReadyNameFunc > (eventNamesArray.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + "count parameters: " 
                                + eventNamesArray.length 
                                + ", need for return " + numEventReadyNameFunc);
            } 
            codeForEventReadyName = prefixEventName(prefixNumber).concat(eventNamesArray[numEventReadyNameFunc]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString())).hashCode();
            return new Integer(codeForEventReadyName);
        } finally {
            eventNamesArray = null;
            codeForEventReadyName = null;
            numEventReadyNameFunc = null;
        }
    }
    /**
     * 
     * @return 
     */
    private Integer getEventNamesCount(){
        String[] eventNamesArray;
        try {
            eventNamesArray = getEventNames();
            return new Integer(eventNamesArray.length);
        } finally {
            eventNamesArray = null;
        }
    }
    /**
     * 
     * @return 
     */
    private Integer getEventPrefixNamesCount(){
        String[] eventNamesArray;
        try {
            eventNamesArray = getPrefixEventNames();
            return new Integer(eventNamesArray.length);
        } finally {
            eventNamesArray = null;
        }
    }
    /**
     * rules logic:
     * states see in state descr: event... wait... do...
     * setStopFlag(int, UUID) into wait only
     * moveFlagInTo(int, UUID) from wait to do after finished 
     *  - into event or next logic level
     * deleteFinishedFlag(int, UUID)
     * - getEvent... from state
     */
}
