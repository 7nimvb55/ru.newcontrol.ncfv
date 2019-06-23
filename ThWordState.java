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
 * How work
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
    
    public ThWordState() {
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        setFalseWordRouterJobToWriter();
        setFalseWordRouterJobToReader();
        /**
         * ThWordFlowReaded
         */
        setFalseWordFlowReaded();
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
     * On fromFsDelteDataEvent (markProcListDeleting)
     *  - current filename saved (curFNs)
     *  - poll data from Cache
     *  - if new move name (nMFN) after write equal curFNs than rename curFNs-UUID(prev)
     *  - write DataFromCache move to nMFN, delete curFNs-UUID(prev)
     * On readReadyDataEvent (markProcListReading)
     *  - current filename saved (curFNs)
     *  - read from Fs data
     *  - poll data from ReadedCache
     *  - insert into Cache (insertIntoCacheEvent)
     *  - add curFNs name to list, move UUID into fromFsDelteDataEvent
     * On writeDataFromCacheEvent (markProcListWriting)
     *  - poll data from Cache
     *  - if notexist (nMFN)
     *  - write data into FS
     *  - add nMFN to curFNs name to list, move UUID into readReadyDataEvent
     * On insertIntoCacheEvent (markProcListInserting) 
     *                      Sources: fromOuterBus, fromReadedCache
     *  - poll data from OuterBus
     *  - poll data from ReadedCache
     *  - insert into Cache
     *  - check for markProcListDeleting, fromFsDelteDataEvent if need, do
     *  - check for markProcListReading, readReadyDataEvent if need, do
     *  - check for markProcListWriting, writeDataFromCacheEvent if need, do
     *  - if end for fromOuterBus source, do all clean fromReadedCache source, do cleanCacheEvent
     * On cleanCacheEvent, new Source cleanedCache
     *  - do insertIntoCacheEvent
     */
    private String[] getEventNames(){
        String[] returnedListEventNames;
        try {
            returnedListEventNames = new String[] {
                "",
                
            };
            return returnedListEventNames;
        } finally {
            returnedListEventNames = null;
        }
    }
    private Integer getEventCount(){
        String[] eventNamesArray;
        try {
            eventNamesArray = getEventNames();
            return new Integer(eventNamesArray.length);
        } finally {
            eventNamesArray = null;
        }
    }
    private Integer getEventCodeByNumber(int numParam){
        String[] eventNamesArray;
        Integer codeForEventName;
        try {
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + " negative index sended, 0 (zero) > " + numParam);
            }
            eventNamesArray = getEventNames();
            if( numParam > (eventNamesArray.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusName.class.getCanonicalName() 
                                + " parameters of flow statusName in StorageWord is not valid, "
                                + "count parameters: " 
                                + eventNamesArray.length 
                                + ", need for return " + numParam);
            } 
            codeForEventName = eventNamesArray[numParam]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return codeForEventName;
        } finally {
            eventNamesArray = null;
            codeForEventName = null;
        }
    }
    
}
