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
    private final ThStorageWordBusOutput busJobForWordRouter;
    private final ThWordCacheSk wordCache;
    private final ThWordCacheSk wordCacheReaded;
    private final ThWordEventIndex eventIndex;
    public ThWordEventLogic(final ThWordRule ruleWordInputed) {
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.busJobForWordRouter = ruleWordInputed.getIndexRule().getIndexState().getRuleStorageWord().getStorageWordState().getBusJobForWordWrite();
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
}
