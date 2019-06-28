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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordLogicRouter {
    /**
     * with events logic version
     * @param outerRuleWord 
     */
    protected void doRouterForIndexWord(final ThWordRule outerRuleWord){
        ThWordRule wordRuleFunc;
        ThIndexRule indexRuleFunc;
        ThIndexState indexState;
        ThStorageWordRule storageWordRuleFunc;
        ThStorageWordState storageWordState;
        ThStorageWordBusOutput busOutputForWordRouter;
        try {
            wordRuleFunc = (ThWordRule) outerRuleWord;
            indexRuleFunc = wordRuleFunc.getIndexRule();
            indexState = indexRuleFunc.getIndexState();
            storageWordRuleFunc = indexRuleFunc.getIndexState().getRuleStorageWord();
            storageWordState = storageWordRuleFunc.getStorageWordState();
            busOutputForWordRouter = storageWordState.getBusJobForWordWrite();
            do{
                outerBusIterator(wordRuleFunc, busOutputForWordRouter);
            } while( storageWordRuleFunc.isRunnedStorageWordWorkFilter() );
        } finally {
            wordRuleFunc = null;
            indexRuleFunc = null;
            indexState = null;
            storageWordRuleFunc = null;
            storageWordState = null;
            busOutputForWordRouter = null;
        }
    }
    protected void outerBusIterator(
            ThWordRule outerRuleWord, 
            ThStorageWordBusOutput busOutputForWordRouterInputed){
        ThStorageWordBusOutput busOutputForWordRouterFunc;
        LinkedTransferQueue<TdataWord> busOutputByTypeWord;
        Integer typeWordOfBusOutput;
        try {
            busOutputForWordRouterFunc = (ThStorageWordBusOutput) busOutputForWordRouterInputed;
            for( Map.Entry<Integer, LinkedTransferQueue<TdataWord>> itemsBusByTypeWord : busOutputForWordRouterFunc.getExistBusEntrySetForTypeWord() ){
                //make event indexes, main flow, set stop flags and insert data into cache
                typeWordOfBusOutput = itemsBusByTypeWord.getKey();
                busOutputByTypeWord = itemsBusByTypeWord.getValue();
                if( busOutputByTypeWord != null ){
                    if( !busOutputByTypeWord.isEmpty() ){
                        generateMainFlowForDataFromBusOutput(outerRuleWord, typeWordOfBusOutput, busOutputByTypeWord);
                    }
                }
            }
        } finally {
            busOutputForWordRouterFunc = null;
            busOutputByTypeWord = null;
            typeWordOfBusOutput = null;
        }
    }
    protected void generateMainFlowForDataFromBusOutput(
            ThWordRule outerRuleWord, 
            Integer typeWordOfBusOutputInputed, 
            LinkedTransferQueue<TdataWord> busOutputByTypeWordInputed){
        ThWordEventLogic eventLogic;
        Boolean labelTypeData;
        Integer typeWordOfBusOutputFunc;
        String hexTagName;
        String subString;
        LinkedTransferQueue<TdataWord> busOutputByTypeWordFunc;
        ConcurrentSkipListMap<UUID, TdataWord> pollFromBusOutputDataPacket;
        TdataWord pollDataItem;
        UUID itemKey;
        try {
            typeWordOfBusOutputFunc = (Integer) typeWordOfBusOutputInputed;
            busOutputByTypeWordFunc = (LinkedTransferQueue<TdataWord>) busOutputByTypeWordInputed;
            hexTagName = new String();
            eventLogic = (ThWordEventLogic) outerRuleWord.getWordState().getEventLogic();
            do {
                pollDataItem = busOutputByTypeWordFunc.poll();
                if( pollDataItem != null ){
                    hexTagName = pollDataItem.hexSubString;
                    subString = pollDataItem.strSubString;
                    itemKey = pollDataItem.randomUUID;
                    eventLogic.insertIntoCacheData(typeWordOfBusOutputFunc, hexTagName, subString, pollDataItem);
                }
            } while( !busOutputByTypeWordFunc.isEmpty() );
        } finally {
            eventLogic = null;
            labelTypeData = null;
            typeWordOfBusOutputFunc = null;
            busOutputByTypeWordFunc = null;
            pollFromBusOutputDataPacket = null;
            pollDataItem = null;
            itemKey = null;
            hexTagName = null;
            subString = null;
        }
    }
    protected void doNotReleasedRouterForIndexWord(final ThWordRule outerRuleWord){
        /**
         * @todo ThWordCache objects for cache, cacheReaded, inputBusFromFilter
         * Status set and read algoritm flags
         */
        final ThWordRule funcRuleWord;
        funcRuleWord = (ThWordRule) outerRuleWord;
        ThIndexRule indexRule;
        indexRule = funcRuleWord.getIndexRule();
        ThIndexStatistic indexStatistic = indexRule.getIndexStatistic();
        ThWordState wordState = funcRuleWord.getWordState();
        ThWordStatusMainFlow wordStatusMainFlow = funcRuleWord.getWordStatusMainFlow();
        ThStorageWordRule ruleStorageWord;
        ruleStorageWord = indexRule.getIndexState().getRuleStorageWord();
        ThStorageWordState storageWordState;
        storageWordState = ruleStorageWord.getStorageWordState();
        System.out.println("++++++++++++++++++++++++++++++start " + ThWordLogicRouter.class.getCanonicalName());
        ThStorageWordBusOutput busJobForWordRouter;
        busJobForWordRouter = storageWordState.getBusJobForWordWrite();
        do{
            Set<Map.Entry<Integer, LinkedTransferQueue<TdataWord>>> busForTypeWord = busJobForWordRouter.getExistBusEntrySetForTypeWord();
            for(Map.Entry<Integer, LinkedTransferQueue<TdataWord>> items : busForTypeWord){
                /**
                 * (1) - typeWord - directory in zipfs storage to string
                 */
                System.out.println("For bus typeWord " + items.getKey());
                //for(Map.Entry<String, String> itemsOfBus : items.getValue().entrySet()){
                    /**
                     * (2) - hexTagName
                     * (2a) - itemsOfBus.getKey() - .substring(0,3) - subDirectory into (1)
                     *          released in ThWordHelperFileSystem
                     * (3) - subString
                     * (3a) - items.getValue.remove(itemsOfBus.getKey()) - .length() - subDirectory into (2)
                     *          released in ThWordHelperFileSystem
                     * (4) - (2), (3) data into list of Word file, if exist flag in
                     * WordStatistic structure about existing in list data file than 
                     * do nothing (remove data, and not create jobs for read and write)
                     *          released in ThWordRouter, ThWordStatistic
                     *                  ThWordCache
                     */
                    /*System.out.println("For bus hexWord " 
                            + itemsOfBus.getKey() 
                            + " subString " 
                            + items.getValue().remove(itemsOfBus.getKey()));

                }*/
                /**
                 * @todo IllegalArgumentException catch
                 */
                
                try {
                    sendRemovedFromBusDataToFlow(funcRuleWord, items.getKey(), items.getValue());
                } catch(IllegalArgumentException exIllArg) {
                    System.err.println(exIllArg.getMessage());
                    exIllArg.printStackTrace();

                } catch(NullPointerException exNullReturn) {
                    System.err.println(exNullReturn.getMessage());
                    exNullReturn.printStackTrace();
                    continue;
                }
            }
        } while( ruleStorageWord.isRunnedStorageWordWorkFilter() );
        
        Set<Map.Entry<Integer, LinkedTransferQueue<TdataWord>>> busForTypeWord = busJobForWordRouter.getExistBusEntrySetForTypeWord();
        for(Map.Entry<Integer, LinkedTransferQueue<TdataWord>> items : busForTypeWord){
                System.out.println("From bus typeWord " + items.getKey());
                /*for(Map.Entry<String, String> itemsOfBus : items.getValue().entrySet()){
                    System.out.println("For bus hexWord " 
                            + itemsOfBus.getKey() 
                            + " subString " 
                            + items.getValue().remove(itemsOfBus.getKey()));

                }*/
                /**
                 * @todo IllegalArgumentException catch
                 */
                try {
                    sendRemovedFromBusDataToFlow(funcRuleWord, items.getKey(), items.getValue());
                } catch(IllegalArgumentException exIllArg) {
                    System.err.println(exIllArg.getMessage());
                    exIllArg.printStackTrace();

                } catch(NullPointerException exNullReturn) {
                    System.err.println(exNullReturn.getMessage());
                    exNullReturn.printStackTrace();
                    continue;
                }
        }
        /**
         * @todo procedure for read all caches data and write it
         */
        ThWordCacheSk wordCache = (ThWordCacheSk) wordStatusMainFlow.getWordCache();
        wordCache.cleanKeyForEmptyLists();
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<UUID, TdataWord>>>>> pollAllData = wordCache.pollAllData();
        /*ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> listTypTagSubStr = wordCache.pollListTypeTagSubStr();
        for(Map.Entry<Integer, ConcurrentHashMap<String, String>> itemList : listTypTagSubStr.entrySet()){
        try {
        removeDataForCurrentTypeWordBus(funcRuleWord,
        itemList.getKey(),
        listTypTagSubStr.remove(itemList.getKey()));
        } catch(IllegalArgumentException exIllArg) {
        System.err.println(exIllArg.getMessage());
        exIllArg.printStackTrace();
        } catch(NullPointerException exNullReturn) {
        System.err.println(exNullReturn.getMessage());
        exNullReturn.printStackTrace();
        continue;
        }
        }*/
        
        System.out.println("++++++++++++++++++++++++++++++stop " + ThWordLogicRouter.class.getCanonicalName());
    }
    private static void iteratorBusData(){
        
    }
    private static void sendRemovedFromBusDataToFlow(final ThWordRule outerRuleWord,
            final Integer keyTypeWordInputed, 
            final LinkedTransferQueue<TdataWord> valueBusOfTypeWordInputed){
        Integer keyTypeWordFunc;
        LinkedTransferQueue<TdataWord> valueBusOfTypeWordFunc;
        TdataWord poll;
        try {
            
            keyTypeWordFunc = (Integer) keyTypeWordInputed;
            valueBusOfTypeWordFunc = (LinkedTransferQueue<TdataWord>) valueBusOfTypeWordInputed;
            
            if (valueBusOfTypeWordFunc == null) {
                throw new NullPointerException("Bus for type of Word number not exist: " + String.valueOf(keyTypeWordFunc));
            }
            
            if (valueBusOfTypeWordFunc.isEmpty()) {
                throw new NullPointerException("Empty bus for type of Word number: " + String.valueOf(keyTypeWordFunc));
            }
            do{
                /**
                 * create new main flow
                 * set flags
                 * get data from cache readed
                 * change flag old flow
                 * insert data into cache from bus and cachereaded
                 * write data to storage
                 */
                
                poll = (TdataWord) valueBusOfTypeWordFunc.poll();
                if( poll != null){
                    if( ThWordHelper.isTdataWordValid(poll) ){
                        createNewFlow(outerRuleWord, poll);
                    } else {
                        System.out.println(ThWordLogicRouter.class.getCanonicalName() 
                        + " inputed not valid data for poll from bus object class " 
                        + TdataWord.class.getCanonicalName() 
                        + " object data " + poll.toString());
                        poll = null;
                    }
                }
            }
            while( !valueBusOfTypeWordFunc.isEmpty() );
            
        } finally {
            keyTypeWordFunc = null;
            valueBusOfTypeWordFunc = null;
            poll = null;
        }
    }
    private static void createNewFlow(final ThWordRule outerRuleWord,
            final TdataWord fromBusReadedData){
        ThWordStatusMainFlow wordStatusMainFlow;
        ThWordCacheSk wordCache;
        ThWordCacheSk wordCacheReaded;
        ConcurrentSkipListMap<UUID, TdataWord> pollDataByDataWord;
        UUID initMainFlowUUID;
        TdataWord dataFromBusFunc;
        Boolean isAllReadedDataAddIntoCache;
        try {
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            wordStatusMainFlow = (ThWordStatusMainFlow) outerRuleWord.getWordStatusMainFlow();
            
            wordCache = wordStatusMainFlow.getWordCache();
            wordCacheReaded = wordStatusMainFlow.getWordCacheReaded();
            initMainFlowUUID = wordStatusMainFlow.createInitMainFlow(dataFromBusFunc, outerRuleWord.getWordState().getEventIndexFlow());
            /**
             * DataCache - (0) currentInCache, (2) addNeedToFileSystemLimit
             * Name - (4) flowFileNamePrefix
             * Workers - (3) isCachedData
             */
            isAllReadedDataAddIntoCache = Boolean.FALSE;
            pollDataByDataWord = null;
            try {
                pollDataByDataWord = wordCacheReaded.pollDataByDataWord(dataFromBusFunc);
                
            } catch (IllegalArgumentException exIll) {
                System.err.println(exIll.getMessage());
            } catch (NullPointerException exNull) {
                System.err.println(exNull.getMessage());
            }
            if( pollDataByDataWord != null ){
                isAllReadedDataAddIntoCache = wordCache.addAllDataIntoCache(pollDataByDataWord);
                if( isAllReadedDataAddIntoCache ){
                    endReadFlow(outerRuleWord, dataFromBusFunc, initMainFlowUUID);
                }
            }
            
            wordCache.setDataIntoCacheFlow(dataFromBusFunc);
            changeInitFlowPoint(outerRuleWord, dataFromBusFunc, initMainFlowUUID);
            /**
             * check for exist data in storage
             * if exist create reader job
             * check reader ready job, create writer job
             * create flow for writer, if exist readed job change it params
             */
            
        }
        finally {
            wordStatusMainFlow = null;
            wordCache = null;
            wordCacheReaded = null;
            pollDataByDataWord = null;
            dataFromBusFunc = null;
            initMainFlowUUID = null;
        }
    }
    private static void intoWriteFlow(){
        
    }
    private static void intoReadFlow(){
        
    }
    private static void changeInitFlowPoint(final ThWordRule outerRuleWord,
            final TdataWord fromBusReadedData,
            final UUID newCreatedMainFlow){
        ThWordStatusMainFlow wordStatusMainFlow;
        ThWordBusFlowEvent wordFlowReaded;
        UUID createdMainFlow;
        ThWordCacheSk wordCache;
        ThWordCacheSk wordCacheReaded;
        ThWordState wordState;
        TdataWord dataFromBusFunc;
        Long nanoTime;
        String buildTypeWordStoreSubDirictoriesFunc;
        Integer sizeDataInCache;
        Integer sizeDataInCacheReaded;
        try {
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            wordState = (ThWordState) outerRuleWord.getWordState();
            wordStatusMainFlow = (ThWordStatusMainFlow) outerRuleWord.getWordStatusMainFlow();
            wordCache = wordStatusMainFlow.getWordCache();
            wordCacheReaded = wordStatusMainFlow.getWordCacheReaded();
            wordFlowReaded = (ThWordBusFlowEvent) wordState.getWordFlowReaded();
            createdMainFlow = (UUID) newCreatedMainFlow;
            
            if( createdMainFlow != null ){
                wordStatusMainFlow.validateInFlowAllPointsByDataWord(dataFromBusFunc, createdMainFlow);

                buildTypeWordStoreSubDirictoriesFunc = (String) ThWordHelper.buildTypeWordStoreSubDirictories(
                        dataFromBusFunc.typeWord,
                        dataFromBusFunc.hexSubString.substring(0, 3), 
                        dataFromBusFunc.lengthSubString);
                
                sizeDataInCache = wordCache.sizeDataInCache(dataFromBusFunc.typeWord, dataFromBusFunc.hexSubString, dataFromBusFunc.strSubString);
                sizeDataInCacheReaded = wordCacheReaded.sizeDataInCache(dataFromBusFunc.typeWord, dataFromBusFunc.hexSubString, dataFromBusFunc.strSubString);
                wordStatusMainFlow.changeParamForMainUuidByNumberName(dataFromBusFunc, createdMainFlow, 0, buildTypeWordStoreSubDirictoriesFunc);
                wordStatusMainFlow.changeParamForMainUuidByNumberName(dataFromBusFunc, createdMainFlow, 4, dataFromBusFunc.hexSubString);
                wordStatusMainFlow.changeParamForMainUuidByNumberName(dataFromBusFunc, createdMainFlow, 5, dataFromBusFunc.strSubString);
                nanoTime = System.nanoTime();
                wordStatusMainFlow.changeParamForMainUuidByNumberActivity(dataFromBusFunc, createdMainFlow, 0, nanoTime);
                wordStatusMainFlow.changeParamForMainUuidByNumberActivity(dataFromBusFunc, createdMainFlow, 0, 0L);
                
                wordStatusMainFlow.changeParamForMainUuidByNumberDataCache(dataFromBusFunc, createdMainFlow, 0, sizeDataInCache);
                wordStatusMainFlow.changeParamForMainUuidByNumberDataCache(dataFromBusFunc, createdMainFlow, 1, sizeDataInCache);
                
                wordStatusMainFlow.changeParamForMainUuidByNumberWorkers(dataFromBusFunc, createdMainFlow, 3, sizeDataInCache > 0);
                wordStatusMainFlow.changeParamForMainUuidByNumberWorkers(dataFromBusFunc, createdMainFlow, 4, sizeDataInCacheReaded > 0);
            }
        }
        finally {
            wordStatusMainFlow = null;
            wordFlowReaded = null;
            createdMainFlow = null;
            wordCache = null;
            wordCacheReaded = null;
            wordState = null;
            dataFromBusFunc = null;
            nanoTime = null;
            buildTypeWordStoreSubDirictoriesFunc = null;
            sizeDataInCache = null;
            sizeDataInCacheReaded = null;
        }
    }
    /**
     * read UUID from BusReaded flow,
     * read params, 
     * change and set init values for new main Flow UUID
     * remove from main flow UUID
     * @param outerRuleWord
     * @param fromBusReadedData 
     */
    private static void endReadFlow(final ThWordRule outerRuleWord,
            final TdataWord fromBusReadedData,
            final UUID newCreatedMainFlow){
        ThWordStatusMainFlow wordStatusMainFlow;
        ThWordCacheSk wordCache;
        ThWordCacheSk wordCacheReaded;
        ConcurrentSkipListMap<UUID, TdataWord> pollDataByDataWord;
        ConcurrentSkipListMap<String, ConcurrentSkipListMap<Long, UUID>> dataReadedFlowUuidsByDataWord;
        ThWordState wordState;
        ThWordBusFlowEvent wordFlowReaded;
        TdataWord dataFromBusFunc;
        UUID valueReadedUUID;
        UUID changedInitedFlow;
        Boolean isMoveFileReady;
        Boolean isNeedReadData;
        Boolean isWriteProcess;
        Boolean isCachedReadedData;
        Boolean isReadProcess;
        try {
            dataFromBusFunc = (TdataWord) fromBusReadedData;
            wordState = (ThWordState) outerRuleWord.getWordState();
            wordFlowReaded = wordState.getWordFlowReaded();
            wordStatusMainFlow = (ThWordStatusMainFlow) outerRuleWord.getWordStatusMainFlow();
            changedInitedFlow = (UUID) newCreatedMainFlow;
            /**
             * add getFuncByTdataWord
             * get UUID from ReadedFlowBus
             * future ... if UUID not exist set error in allIndexFlow about that
             * finish him, that readedUUID in main flow
             */
            try {
                LinkedTransferQueue<UUID> pollDataReadedFlowUuidsByDataWord = wordFlowReaded.pollDataReadedFlowUuidsByDataWord(dataFromBusFunc);
                do {
                        
                    //ifExistInMainFlowBus, change init newCreated param from readedUUIDparam = mainFlow(value)
                    //delete from main flow readedUUIDparam
                    valueReadedUUID = pollDataReadedFlowUuidsByDataWord.poll();
                    if( valueReadedUUID != null ){    
                        wordStatusMainFlow.changeParamForMainUuidByNumberWorkers(dataFromBusFunc, valueReadedUUID, 1, Boolean.FALSE);
                        isMoveFileReady = wordStatusMainFlow.getValueForMainUuidByNumberWorkers(dataFromBusFunc, valueReadedUUID, 7);
                        isNeedReadData = wordStatusMainFlow.getValueForMainUuidByNumberWorkers(dataFromBusFunc, valueReadedUUID, 2);
                        isWriteProcess = wordStatusMainFlow.getValueForMainUuidByNumberWorkers(dataFromBusFunc, valueReadedUUID, 0);
                        isCachedReadedData = wordStatusMainFlow.getValueForMainUuidByNumberWorkers(dataFromBusFunc, valueReadedUUID, 4);
                        isReadProcess = wordStatusMainFlow.getValueForMainUuidByNumberWorkers(dataFromBusFunc, valueReadedUUID, 1);
                        if(isMoveFileReady){
                            if(isNeedReadData){
                                if(isWriteProcess){
                                    if(!isCachedReadedData){
                                        if(!isReadProcess){
                                            changeFlowWithNeedRead(outerRuleWord, dataFromBusFunc, changedInitedFlow);
                                            /**
                                             * add to state event bus for
                                             * need read, readed, need write
                                             * need delete
                                             */
                                        }
                                    }else{
                                        if(isReadProcess){
                                            changeFlowWithNeedDelete(outerRuleWord, dataFromBusFunc, changedInitedFlow);
                                        }
                                    }
                                }
                            }
                        }
                    }    
                } while( !pollDataReadedFlowUuidsByDataWord.isEmpty() );
            } catch (IllegalArgumentException illExMessage) {
                System.out.println(illExMessage.getMessage());
            } catch (NullPointerException nullExMessage) {
                System.out.println(nullExMessage.getMessage());
            }
        } finally {
            isMoveFileReady = null;
            isNeedReadData = null;
            isWriteProcess = null;
            isCachedReadedData = null;
            isReadProcess = null;
            changedInitedFlow = null;
        }
    }
    private static void changeFlowWithNeedRead(
            final ThWordRule outerRuleWord,
            final TdataWord fromBusReadedData,
            final UUID changedInitedFlowInputed){
        ThWordStatusMainFlow wordStatusMainFlow;
        ThWordCacheSk wordCache;
        ThWordCacheSk wordCacheReaded;
        try {
            ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> busForTypeWord = 
            busJobForWordRouterJobToReader.getBusForTypeWord(typeWordFunc);
            ConcurrentHashMap<String, String> newReaderJob = new ConcurrentHashMap<String, String>();
            newReaderJob.put(tagNameFunc, strSubStringFunc);
            busForTypeWord.put(keyMainFlow, newReaderJob);
            isReadProcess = Boolean.TRUE;
            statusWorkersForKeyPointFlow.put(1836000367, isReadProcess);
            fileDataListPrefix = (String) statusNameForKeyPointFlow.get(-980152217);
        } finally {
            
        }
    }
    private static void changeFlowWithNeedDelete(
            final ThWordRule outerRuleWord,
            final TdataWord fromBusReadedData,
            final UUID changedInitedFlowInputed){
        ThWordStatusMainFlow wordStatusMainFlow;
        ThWordCacheSk wordCache;
        ThWordCacheSk wordCacheReaded;
        try {
            ConcurrentHashMap<String, String> pollTypeWordTagFileNameData = 
            thWordCacheReaded.pollTypeWordTagFileNameData(typeWordFunc, tagNameFunc, strSubStringFunc);
            Boolean addAllDataIntoCache = 
            thWordCache.addAllDataIntoCache(typeWordFunc, tagNameFunc, strSubStringFunc, pollTypeWordTagFileNameData);
            for( Map.Entry<String, ConcurrentHashMap<Long, UUID>> itemReadedMainFlow : typeWordTagFileNameReadedFlowUuids.entrySet() ) {
                if( itemReadedMainFlow.getValue().equals(keyMainFlow) ){
                    ConcurrentHashMap<Long, UUID> removeReadedFlowUUID = typeWordTagFileNameReadedFlowUuids.remove(itemReadedMainFlow.getKey());
                    removeReadedFlowUUID = null;
                }
            }
            //07 - isUdatedDataInHashMap - -2092233516
            statusWorkersForKeyPointFlow.put(-2091433802, addAllDataIntoCache);
            /**
             * calculate cached data
             * delete oldFlow, oldFile
             */
            //11 - isNeedDeleteOldFile - -1172779240
            statusWorkersForKeyPointFlow.put(-1172779240, Boolean.TRUE);
            String fileCurrentName = statusNameForKeyPointFlow.get(1517772480);
            statusNameForKeyPointFlow.put(2045325664, fileCurrentName);
            fileDataListPrefix = (String) statusNameForKeyPointFlow.get(-980152217);

            ConcurrentHashMap<Integer, Integer> statusDataFsForKeyPointFlow = thWordStatusDataFs.getStatusDataFsForKeyPointFlow(keyDataFs);
            volNumSettedInFlow = statusDataFsForKeyPointFlow.get(-1832815869);
        } finally {
            
        }
    }
    /**
     * 
     * @param storageWordStatistic
     * @param fromBusItemKey
     * @param fromBusItemValue 
     * @throws IllegalArgumentException
     */
    private static void removeDataForCurrentTypeWordBus(
            final ThWordRule outerRuleWord,
            final Integer fromBusItemKey, 
            final ConcurrentHashMap<String, String> fromBusItemValue){
        Integer typeWord;
        ConcurrentHashMap<String, String> hexTagNameSubString;
        ThWordRule funcRuleWord;
        try {
            funcRuleWord = (ThWordRule) outerRuleWord;
            typeWord = fromBusItemKey;
            hexTagNameSubString = fromBusItemValue;
            for(Map.Entry<String, String> itemsHexTagSubStr : hexTagNameSubString.entrySet()){
                String recHexTagName = (String) itemsHexTagSubStr.getKey();
                String recSubString = (String) itemsHexTagSubStr.getValue();
                int tagNamelength = (int) recHexTagName.length();
                int strSubStringlength = (int) recSubString.length();
                
                if( (strSubStringlength * 4) != tagNamelength ){
                    throw new IllegalArgumentException(ThWordLogicRouter.class.getCanonicalName() 
                            + " illegal length of inputed in index string, hexTagName: "
                            + recHexTagName + " lengthHex: " + recHexTagName.length()
                            + " strSubString: " + recSubString + " lengthStr: " + recSubString.length()
                            + " lengthHex == lengthStr * 4 ");
                }
                
                if( tagNamelength < 4 ){
                    throw new IllegalArgumentException(ThWordLogicRouter.class.getCanonicalName() 
                            + " illegal length of inputed in index string, hexTagName: "
                            + recHexTagName + " length: " + recHexTagName.length()
                            + " < 4 ");
                }
                /**
                 * (1) - generate directories names
                 * (2) - statistics flags
                 * add into statistics lists
                 * create job for write
                 * @todo in cache read and write into not limited list data files
                 */
                //(1)
                
                //(2)
                setFlagsToStatisticsList(funcRuleWord,
                        typeWord, 
                        recHexTagName, 
                        recSubString);
                
                
                
                String[] oldRecVal = {recHexTagName, recSubString};
                oldRecVal = null;
            }
            
        } finally {
            typeWord = null;
            hexTagNameSubString = null;
            funcRuleWord = null;
        }
        
    }
    private static void setFlagsToStatisticsList(
            final ThWordRule outerRuleWord,
            final Integer typeWordInputed, 
            final String tagNameInputed, 
            final String strSubStringInputed){
        ThWordRule funcRuleWord;
        ThWordStatusMainFlow currentWordStatistic;
        ThWordBusReadedFlow storageWordFlowReaded;
        ThWordState storageWordState;
        ThWordBusWriter busJobForWordRouterJobToWriter;
        ThWordBusReader busJobForWordRouterJobToReader;
        
        ThWordStatusName thWordStatusName;
        ThWordStatusActivity thWordStatusActivity;
        ThWordStatusDataCache thWordStatusDataCache;
        ThWordCacheHa thWordCache;
        ThWordCacheHaReaded thWordCacheReaded;
        ThWordStatusWorkers thWordStatusWorkers;
        ThWordStatusDataFs thWordStatusDataFs;
        
        Integer typeWordFunc;
        String tagNameFunc;
        String strSubStringFunc;
        String buildTypeWordStoreSubDirictoriesFunc;
        
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> typeWordTagFileNameMainFlowUuids;
        ConcurrentHashMap<String, ConcurrentHashMap<Long, UUID>> typeWordTagFileNameReadedFlowUuids;
        
        UUID mainFlowLabel;
        UUID keyFlowStatusDataFs;
        UUID keyFlowStatusName;
        UUID keyFlowStatusActivity;
        UUID keyFlowStatusDataCache;
        UUID keyFlowStatusWorkers;
        
        
        ConcurrentHashMap<Integer, UUID> keysPointsFlow;
        ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> mainFlowContent;
        String storageWordFileNameSrc;
        String namesFsFileNameDestMoveTo;
        
        Boolean isMainFlowExist;
        try{
            funcRuleWord = (ThWordRule) outerRuleWord;
            currentWordStatistic = (ThWordStatusMainFlow) funcRuleWord.getWordStatusMainFlow();
            storageWordFlowReaded = (ThWordBusReadedFlow) funcRuleWord.getWordState().getWordFlowReaded();
            
            storageWordState = (ThWordState) funcRuleWord.getWordState();
            busJobForWordRouterJobToWriter = (ThWordBusWriter) 
                    storageWordState.getBusJobForWordRouterJobToWriter();
            
            busJobForWordRouterJobToReader = (ThWordBusReader) 
                    storageWordState.getBusJobForWordRouterJobToReader();
            
            typeWordFunc = (Integer) typeWordInputed;
            tagNameFunc = (String) tagNameInputed;
            strSubStringFunc = (String) strSubStringInputed;
            
            Integer fromCacheCountFsCountRecordsSrc = 0;
            Integer fromCacheCountFsCountRecordsDestMoveTo = 1;
            Integer fromCacheCountFsVolumeNumberSrc = 0;
            Integer fromCacheCountFsVolumeNumberDestMoveTo = 0;
            
            /**
             * check current flow process
             * sendToCacheData | waitForReadQueue | waitForWriteQueue
             * isNeedReadData
             * isWriteInProcess
             * isReadInProcess
             *
             * 
             * isCachedReadedData
             *
             * newFileName equal currentFileName and isMoved then only writed
             * 
             */
            
            
            typeWordTagFileNameMainFlowUuids = 
                currentWordStatistic.getTypeWordTagFileNameFlowUuids(typeWordFunc, tagNameFunc, strSubStringFunc);
            
            /**
             * read from readed jobs UUIDs, get data from cache and write it
             */
            
            typeWordTagFileNameReadedFlowUuids = 
                storageWordFlowReaded.getTypeWordTagFileNameReadedFlowUuids(typeWordFunc, tagNameFunc, strSubStringFunc);
            
            if( typeWordTagFileNameMainFlowUuids == null ){
                throw new NullPointerException(ThWordLogicRouter.class.getCanonicalName() 
                            + " return null from " + ThWordStatusMainFlow.class.getCanonicalName()
                            + ".getTypeWordTagFileNameFlowUuids(typeWord, hexTagName, strSubString), for params values:"
                            + " typeWord: "
                            + String.valueOf(typeWordFunc) + ", hexTagName: "
                            + tagNameFunc + " lengthHex: " + tagNameFunc.length()
                            + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringFunc.length()
                            + " lengthStr * 4: " + strSubStringFunc.length());
            }
            if( typeWordTagFileNameReadedFlowUuids == null ){
                throw new NullPointerException(ThWordLogicRouter.class.getCanonicalName() 
                            + " return null from " + ThWordBusReadedFlow.class.getCanonicalName()
                            + ".getTypeWordTagFileNameReadedFlowUuids(typeWord, hexTagName, strSubString), for params values:"
                            + " typeWord: "
                            + String.valueOf(typeWordFunc) + ", hexTagName: "
                            + tagNameFunc + " lengthHex: " + tagNameFunc.length()
                            + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringFunc.length()
                            + " lengthStr * 4: " + strSubStringFunc.length());
            }
            
            isMainFlowExist = Boolean.FALSE;
            //this is a jobWrite UUID, to fix create jobListsStatus
            mainFlowLabel = UUID.randomUUID();
            
            Integer volNumSettedInFlow = 0;
            
            String fileDataListPrefix = mainFlowLabel.toString();
            
            thWordCache = currentWordStatistic.getWordCache();
            thWordCacheReaded = currentWordStatistic.getWordCacheReaded();
            
            thWordStatusDataFs = currentWordStatistic.getWordStatusDataFs();
            thWordStatusName = currentWordStatistic.getWordStatusName();
            thWordStatusActivity = currentWordStatistic.getWordStatusActivity();
            thWordStatusDataCache = currentWordStatistic.getWordStatusDataCache();
            thWordStatusWorkers = currentWordStatistic.getWordStatusWorkers();
            
            if( !typeWordTagFileNameMainFlowUuids.isEmpty() ){
                isMainFlowExist = Boolean.TRUE;
                //set not default values
                for( Map.Entry<UUID, ConcurrentHashMap<Integer, UUID>> itemMainFlow : typeWordTagFileNameMainFlowUuids.entrySet() ) {
                    UUID keyMainFlow = itemMainFlow.getKey();
                    if( itemMainFlow.getValue().size() == 5 ){
                        UUID keyDataFs = itemMainFlow.getValue().get("ThWordStatusDataFs".hashCode());
                        UUID keyName = itemMainFlow.getValue().get("ThWordStatusName".hashCode());
                        UUID keyActivity = itemMainFlow.getValue().get("ThWordStatusActivity".hashCode());
                        UUID keyDataCache = itemMainFlow.getValue().get("ThWordStatusDataCache".hashCode());
                        UUID keyWorkers = itemMainFlow.getValue().get("ThWordStatusWorkers".hashCode());

                        /**
                         * validate, catch, remove not valide
                         */
                        try{
                            thWordStatusDataFs.validateCountParams(keyDataFs);
                        } catch (IllegalArgumentException exDataFs) {
                            System.err.println(exDataFs.getMessage());
                            ConcurrentHashMap<Integer, UUID> removeNotValidDataFsFlowUUID = 
                                    typeWordTagFileNameMainFlowUuids.remove(keyMainFlow);
                            removeNotValidDataFsFlowUUID = null;
                            System.err.println("-----------------"
                                    + "||||||||||||||||||"
                                    + "-----------------"
                                    + "||||||||||||||||||"
                                    + "----------------- data in flow key not valid, removed, "
                                    + "reason not set ThWordStatusDataFs for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        try{
                            thWordStatusName.validateCountParams(keyName);
                        } catch (IllegalArgumentException exName) {
                            System.err.println(exName.getMessage());
                            ConcurrentHashMap<Integer, UUID> removeNotValidNameFlowUUID = 
                                    typeWordTagFileNameMainFlowUuids.remove(keyMainFlow);
                            removeNotValidNameFlowUUID = null;
                            System.err.println("-----------------"
                                    + "||||||||||||||||||"
                                    + "-----------------"
                                    + "||||||||||||||||||"
                                    + "----------------- data in flow key not valid, removed, "
                                    + "reason not set ThWordStatusName for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        try{
                            thWordStatusActivity.validateCountParams(keyActivity);
                        } catch (IllegalArgumentException exActiv) {
                            System.err.println(exActiv.getMessage());
                            ConcurrentHashMap<Integer, UUID> removeNotValidActivityFlowUUID = 
                                    typeWordTagFileNameMainFlowUuids.remove(keyMainFlow);
                            removeNotValidActivityFlowUUID = null;
                            System.err.println("-----------------"
                                    + "||||||||||||||||||"
                                    + "-----------------"
                                    + "||||||||||||||||||"
                                    + "----------------- data in flow key not valid, removed, "
                                    + "reason not set ThWordStatusActivity for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        try{
                            thWordStatusDataCache.validateCountParams(keyDataCache);
                        } catch (IllegalArgumentException exDataCache) {
                            System.err.println(exDataCache.getMessage());
                            ConcurrentHashMap<Integer, UUID> removeNotValidDataCacheFlowUUID = 
                                    typeWordTagFileNameMainFlowUuids.remove(keyMainFlow);
                            removeNotValidDataCacheFlowUUID = null;
                            System.err.println("-----------------"
                                    + "||||||||||||||||||"
                                    + "-----------------"
                                    + "||||||||||||||||||"
                                    + "----------------- data in flow key not valid, removed, "
                                    + "reason not set ThWordStatusDataCache for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        try{
                            thWordStatusWorkers.validateCountParams(keyWorkers);
                        } catch (IllegalArgumentException exWorkers) {
                            System.err.println(exWorkers.getMessage());
                            ConcurrentHashMap<Integer, UUID> removeNotValidWorkersFlowUUID = 
                                    typeWordTagFileNameMainFlowUuids.remove(keyMainFlow);
                            removeNotValidWorkersFlowUUID = null;
                            System.err.println("-----------------"
                                    + "||||||||||||||||||"
                                    + "-----------------"
                                    + "||||||||||||||||||"
                                    + "----------------- data in flow key not valid, removed, "
                                    + "reason not set ThWordStatusWorkers for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        ConcurrentHashMap<Integer, String> statusNameForKeyPointFlow = thWordStatusName.getStatusNameForKeyPointFlow(keyName);
                        /**
                         * list for writed, not readed UUID check with readed list
                         * list for writed and readed
                         * list for writed
                         */
                        ConcurrentHashMap<Integer, Boolean> statusWorkersForKeyPointFlow = thWordStatusWorkers.getStatusWorkersForKeyPointFlow(keyWorkers);
                        Boolean isWriteProcess = statusWorkersForKeyPointFlow.get(1640531930);
                        if( isWriteProcess ){
                            /**
                             * 
                             */
                        }
                        Boolean isReadProcess = statusWorkersForKeyPointFlow.get(1836000367);
                        if( isReadProcess ){
                            /**
                             * then isWrited, isNeedRead, isMoveFileReady for FileNames
                             */
                        }
                        Boolean isNeedReadData = statusWorkersForKeyPointFlow.get(-83825824);
                        if( isNeedReadData ){
                            /**
                             * set job for reader
                             */
                        }
                        Boolean isCachedData = statusWorkersForKeyPointFlow.get(-2091433802);
                        if( isCachedData ){
                            
                        }
                        Boolean isCachedReadedData = statusWorkersForKeyPointFlow.get(-660426229);
                        if( isCachedReadedData ){
                            /**
                             * poll for cacheReaded and insert into cache
                             * calculate flow data for new job write
                             * set job for new write
                             * set isNeedDeleteOldFile
                             * set deleteFileNameAfterWrite
                             */
                        }
                        Boolean isCalculatedData = statusWorkersForKeyPointFlow.get(1804093010);
                        Boolean isUdatedDataInHashMap = statusWorkersForKeyPointFlow.get(-2092233516);
                        //check before set reader job
                        Boolean isMoveFileReady = statusWorkersForKeyPointFlow.get(-1884096596);
                        if( isMoveFileReady ) {
                            /**
                             * check for isNeedRead, isReaded
                             */
                            if( isNeedReadData ) {
                                if( isWriteProcess ) {
                                    if( !isCachedReadedData ) {
                                        if( !isReadProcess ) {
                                            ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> busForTypeWord = 
                                                    busJobForWordRouterJobToReader.getBusForTypeWord(typeWordFunc);
                                            ConcurrentHashMap<String, String> newReaderJob = new ConcurrentHashMap<String, String>();
                                            newReaderJob.put(tagNameFunc, strSubStringFunc);
                                            busForTypeWord.put(keyMainFlow, newReaderJob);
                                            isReadProcess = Boolean.TRUE;
                                            statusWorkersForKeyPointFlow.put(1836000367, isReadProcess);
                                            fileDataListPrefix = (String) statusNameForKeyPointFlow.get(-980152217);
                                        }
                                    } else {
                                        if( isReadProcess ) {
                                            ConcurrentHashMap<String, String> pollTypeWordTagFileNameData = 
                                                    thWordCacheReaded.pollTypeWordTagFileNameData(typeWordFunc, tagNameFunc, strSubStringFunc);
                                            Boolean addAllDataIntoCache = 
                                                    thWordCache.addAllDataIntoCache(typeWordFunc, tagNameFunc, strSubStringFunc, pollTypeWordTagFileNameData);
                                            for( Map.Entry<String, ConcurrentHashMap<Long, UUID>> itemReadedMainFlow : typeWordTagFileNameReadedFlowUuids.entrySet() ) {
                                                if( itemReadedMainFlow.getValue().equals(keyMainFlow) ){
                                                    ConcurrentHashMap<Long, UUID> removeReadedFlowUUID = typeWordTagFileNameReadedFlowUuids.remove(itemReadedMainFlow.getKey());
                                                    removeReadedFlowUUID = null;
                                                }
                                            }
                                            //07 - isUdatedDataInHashMap - -2092233516
                                            statusWorkersForKeyPointFlow.put(-2091433802, addAllDataIntoCache);
                                            /**
                                             * calculate cached data
                                             * delete oldFlow, oldFile
                                             */
                                            //11 - isNeedDeleteOldFile - -1172779240
                                            statusWorkersForKeyPointFlow.put(-1172779240, Boolean.TRUE);
                                            String fileCurrentName = statusNameForKeyPointFlow.get(1517772480);
                                            statusNameForKeyPointFlow.put(2045325664, fileCurrentName);
                                            fileDataListPrefix = (String) statusNameForKeyPointFlow.get(-980152217);
                                            
                                            ConcurrentHashMap<Integer, Integer> statusDataFsForKeyPointFlow = thWordStatusDataFs.getStatusDataFsForKeyPointFlow(keyDataFs);
                                            volNumSettedInFlow = statusDataFsForKeyPointFlow.get(-1832815869);
                                            
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                }
                
                
                if( !typeWordTagFileNameReadedFlowUuids.isEmpty() ){
                    //exist readed data
                    /**
                     * add readed data from readed cache into datacache
                     * generate new names
                     * send data to write
                     * mark readed file name to delete
                     * impotant!!!
                     * when writer get data from data cache, calculate data size
                     * and check with limits and file name for move operation
                     * rebuild moveto name...
                     * 
                     * router send data into cache --- size N
                     * ----- may be router send data into cache --- size M
                     * writer read data from cache for write to fs --- size N
                     * ----- in file names file size not set into N+M...
                     * Word index System change data in HashMap by keyWords
                     * size not N+M, size is [arrayN]*[arrayM] --- sizeIs K
                     */
                    for( Map.Entry<String, ConcurrentHashMap<Long, UUID>> itemReadedMainFlow : typeWordTagFileNameReadedFlowUuids.entrySet() ) {
                    
                    }
                }
            }
                
            if( !typeWordTagFileNameReadedFlowUuids.isEmpty() ){
                if( !isMainFlowExist ){
                    /**
                     * set special params
                     */
                    typeWordTagFileNameReadedFlowUuids.clear();
                }
            }

            
            /**
             * @todo
             * in index system Word data fields if not save in UUID key
             * for distinct fields
             */
            
            Boolean isCachedData = Boolean.FALSE;
            
            isCachedData = thWordCache.setDataIntoCacheFlow(typeWordFunc, tagNameFunc, strSubStringFunc);
            
            Boolean isCachedReadedData = Boolean.FALSE;
            
            isCachedReadedData = thWordCacheReaded.isCacheReadedHasData(
                                                typeWordFunc, 
                                                tagNameFunc, 
                                                strSubStringFunc);
            /**
             * @todo if isCachedReadedData true, get from cache, add to list for
             * write
             */
            
            
            buildTypeWordStoreSubDirictoriesFunc = (String) ThWordHelper.buildTypeWordStoreSubDirictories(
                        typeWordFunc,
                        tagNameFunc.substring(0, 3), 
                        strSubStringFunc.length());
            
            /**
             * Read flags, isNeedReadData 
             *  - insert into DataCache
             * isReadInProcess read next readed UUID
             * isWriteInProcess read next need read UUID
             *  - get currentFileName, newFileName if equal, create job for Read
             * isCachedReadedData
             *  - get data from readed jobBus, add from DataCache
             * generate fileNames, write it
             */
            
            keysPointsFlow = new ConcurrentHashMap<Integer, UUID>();
            
            Integer countFsCountRecordsSrc = 0;
            Integer countFsCountRecordsDestMoveTo = (int) thWordCache.sizeDataInCache(typeWordFunc, tagNameFunc, strSubStringFunc);
            Integer countFsVolumeNumberSrc = volNumSettedInFlow;
            Integer countFsVolumeNumberDestMoveTo = volNumSettedInFlow;
            
            keyFlowStatusDataFs = UUID.randomUUID();
            keysPointsFlow.put("ThWordStatusDataFs".hashCode(), keyFlowStatusDataFs);
            
            
            thWordStatusDataFs.createStructureParamsCountFs(
                    keyFlowStatusDataFs,
                    countFsCountRecordsSrc, 
                    countFsVolumeNumberSrc);
            
            
            storageWordFileNameSrc = new String()
                    .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                    .concat(fileDataListPrefix.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                    .concat(String.valueOf(countFsCountRecordsSrc))
                    .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                    .concat(String.valueOf(countFsVolumeNumberSrc));
            /**
             * @todo isOnFileSystem exist data
             * put job for read, read for readed bus data
             */
            Path getNamesFsFileNameSrc = Paths.get(buildTypeWordStoreSubDirictoriesFunc, storageWordFileNameSrc);
            String namesFsFileNameSrc = getNamesFsFileNameSrc.toString();
            
            String storageWordFileNameDestMoveTo = new String()
                    .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                    .concat(fileDataListPrefix.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                    .concat(String.valueOf(countFsCountRecordsDestMoveTo))
                    .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                    .concat(String.valueOf(countFsVolumeNumberDestMoveTo));
            Path getNamesFsFileNameDestMoveTo = Paths.get(buildTypeWordStoreSubDirictoriesFunc, storageWordFileNameDestMoveTo);
            namesFsFileNameDestMoveTo = getNamesFsFileNameDestMoveTo.toString();
            
            keyFlowStatusName = UUID.randomUUID();
            keysPointsFlow.put("ThWordStatusName".hashCode(), keyFlowStatusName);
            
            
            thWordStatusName.createStructureParamsNamesFs(
                    keyFlowStatusName,
                    buildTypeWordStoreSubDirictoriesFunc,
                    namesFsFileNameSrc, 
                    namesFsFileNameDestMoveTo,
                    namesFsFileNameDestMoveTo,
                    fileDataListPrefix);
            Integer timeUSEIterationIncrement = 0;
            
            keyFlowStatusActivity = UUID.randomUUID();
            keysPointsFlow.put("ThWordStatusActivity".hashCode(), keyFlowStatusActivity);
            
            
            thWordStatusActivity.createAddToListParamsTimeUse(
                    keyFlowStatusActivity, 
                    timeUSEIterationIncrement);
            /**
             * get params from structures
             */
            Integer countTmpCurrentInCache = 0;
            Integer countTmpCurrentInCacheReaded = 0;
            
            Integer countTmpAddNeedToFileSystemLimit = AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT - countFsCountRecordsDestMoveTo;
            Integer countTmpIndexSystemLimitOnStorage = AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT;
            
            keyFlowStatusDataCache = UUID.randomUUID();
            keysPointsFlow.put("ThWordStatusDataCache".hashCode(), keyFlowStatusDataCache);
            
            
            thWordStatusDataCache.createStructureParamsCountTmp(
                    keyFlowStatusDataCache,
                    countTmpCurrentInCache, 
                    countTmpCurrentInCacheReaded,
                    countTmpAddNeedToFileSystemLimit, 
                    countTmpIndexSystemLimitOnStorage);
            
            Boolean isWriteProcess = Boolean.FALSE;
            Boolean isReadProcess = Boolean.FALSE;
            Boolean isNeedReadData = Boolean.FALSE;
            
            Boolean isCalculatedData = Boolean.FALSE;
            Boolean isUdatedDataInHashMap = Boolean.FALSE;
            Boolean isMoveFileReady = Boolean.FALSE;
            
            Boolean isFlowInWriteBus = Boolean.FALSE;
            Boolean isFlowInReadBus = Boolean.FALSE;
            Boolean isNeedDeleteOldFile = Boolean.FALSE;
            Boolean isOldFileDeleted = Boolean.FALSE;
            
            keyFlowStatusWorkers = UUID.randomUUID();
            keysPointsFlow.put("ThWordStatusWorkers".hashCode(), keyFlowStatusWorkers);
            
            mainFlowContent = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>();
            mainFlowContent.put(mainFlowLabel, keysPointsFlow);
            thWordStatusWorkers.createStructureParamsFlagsProc(
                    keyFlowStatusWorkers,
                    isWriteProcess, 
                    isReadProcess, 
                    isNeedReadData,
                    isCachedData,
                    isCachedReadedData,  
                    isCalculatedData, 
                    isUdatedDataInHashMap, 
                    isMoveFileReady,
                    isFlowInWriteBus,
                    isFlowInReadBus,
                    isNeedDeleteOldFile,
                    isOldFileDeleted);
            
            currentWordStatistic.setParamsPointsFlow(
                            typeWordFunc, 
                            tagNameFunc, 
                            strSubStringFunc,
                            mainFlowContent);
            
            ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> busForTypeWord 
                    = busJobForWordRouterJobToWriter.getBusForTypeWord(typeWordFunc);
            
            ConcurrentHashMap<String, String> dataForOutput = new ConcurrentHashMap<String, String>();
            dataForOutput.put(tagNameFunc, strSubStringFunc);
            /**
             * ... so part for job data hash..., readed hash, restructure cash on parts readed and write
             */
            busForTypeWord.put(mainFlowLabel, dataForOutput);
            /**
             * isWriteProcess = TRUE;
             */
            //thWordCache.printCacheData();
            
        } catch(IllegalArgumentException exIllArg) {
            System.err.println(exIllArg.getMessage());
            exIllArg.printStackTrace();
            
        } finally {
            currentWordStatistic = null;
            storageWordFlowReaded = null;
            typeWordFunc =  null;
            tagNameFunc =  null;
            strSubStringFunc =  null;
            buildTypeWordStoreSubDirictoriesFunc =  null;
            
            funcRuleWord = null;
            currentWordStatistic = null;
            storageWordState = null;
            busJobForWordRouterJobToWriter = null;
            
            thWordStatusName = null;
            thWordStatusActivity = null;
            thWordStatusDataCache = null;
            thWordCache = null;
            thWordStatusWorkers = null;
            thWordStatusDataFs = null;
            
            typeWordTagFileNameMainFlowUuids = null;
            typeWordTagFileNameReadedFlowUuids = null;
            
            mainFlowLabel = null;
            keyFlowStatusDataFs = null;
            keyFlowStatusName = null;
            keyFlowStatusActivity = null;
            keyFlowStatusDataCache = null;
            keyFlowStatusWorkers = null;
            
        }
    }
}
