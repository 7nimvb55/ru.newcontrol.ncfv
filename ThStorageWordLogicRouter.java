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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * From ThStorageWordBusOutput get data, convert int typeWord to String for 
 directory, create or add to path, get from bus data, read heximal value,  
 first four bytes, create or add to sub path, calculate length for subString 
 value, generate name for list file in format wl-(UUID)-(Size)-(Volume Number)
 
 ThStorageWordStatusMainFlow - search directories and list file names
 ThStorageWordCache - temp storages for data
 ThStorageWordHelperFileSystem - static functions for create, move, scan 
 directories and list files
 * 
 * @author wladimirowichbiaran
 */
public class ThStorageWordLogicRouter {
    protected void doRouterForIndexStorageWord(final ThStorageWordRule outerRuleStorageWord){
        final ThStorageWordRule funcRuleStorageWord = (ThStorageWordRule) outerRuleStorageWord;
        ThIndexRule indexRule = funcRuleStorageWord.getIndexRule();
        ThIndexStatistic indexStatistic = indexRule.getIndexStatistic();
        ThStorageWordState storageWordState = funcRuleStorageWord.getStorageWordState();
        ThStorageWordStatusMainFlow storageWordStatistic = funcRuleStorageWord.getStorageWordStatistic();
        
        System.out.println("++++++++++++++++++++++++++++++start " + ThStorageWordLogicRouter.class.getCanonicalName());
        ThStorageWordBusInput busJobForStorageWordRouter = storageWordState.getBusJobForStorageWordRouterJob();
        do{
            ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> busForTypeStorageWordRouter = busJobForStorageWordRouter.getMaxUsedBusesSet();
            for(Map.Entry<Integer, ConcurrentHashMap<String, String>> items : busForTypeStorageWordRouter.entrySet()){
                /**
                 * (1) - typeWord - directory in zipfs storage to string
                 */
                System.out.println("For bus typeWord " + items.getKey());
                //for(Map.Entry<String, String> itemsOfBus : items.getValue().entrySet()){
                    /**
                     * (2) - hexTagName
                     * (2a) - itemsOfBus.getKey() - .substring(0,3) - subDirectory into (1)
                     *          released in ThStorageWordHelperFileSystem
                     * (3) - subString
                     * (3a) - items.getValue.remove(itemsOfBus.getKey()) - .length() - subDirectory into (2)
                     *          released in ThStorageWordHelperFileSystem
                     * (4) - (2), (3) data into list of StorageWord file, if exist flag in
                     * StorageWordStatistic structure about existing in list data file than 
                     * do nothing (remove data, and not create jobs for read and write)
                     *          released in ThStorageWordRouter, ThStorageWordStatistic
                     *                  ThStorageWordCache
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
                    removeDataForCurrentTypeWordBus(funcRuleStorageWord, 
                        items.getKey(), 
                        busForTypeStorageWordRouter.remove(items.getKey()));
                } catch(IllegalArgumentException exIllArg) {
                    System.err.println(exIllArg.getMessage());
                    exIllArg.printStackTrace();

                } catch(NullPointerException exNullReturn) {
                    System.err.println(exNullReturn.getMessage());
                    exNullReturn.printStackTrace();
                    continue;
                }
            }
        } while( outerRuleStorageWord.isRunnedStorageWordWorkFilter() );
        
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> busForTypeStorageWordRouter = 
                busJobForStorageWordRouter.getMaxUsedBusesSet();
        for(Map.Entry<Integer, ConcurrentHashMap<String, String>> items : busForTypeStorageWordRouter.entrySet()){
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
                    removeDataForCurrentTypeWordBus(funcRuleStorageWord, 
                        items.getKey(), 
                        busForTypeStorageWordRouter.remove(items.getKey()));
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
        ThStorageWordCache storageWordCache = (ThStorageWordCache) storageWordStatistic.getStorageWordCache();
        
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> listTypTagSubStr = storageWordCache.getListTypTagSubStr();
        
        for(Map.Entry<Integer, ConcurrentHashMap<String, String>> itemList : listTypTagSubStr.entrySet()){
            try {
                    removeDataForCurrentTypeWordBus(funcRuleStorageWord, 
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
        }
        
        System.out.println("++++++++++++++++++++++++++++++stop " + ThStorageWordLogicRouter.class.getCanonicalName());
    }
    /**
     * 
     * @param storageWordStatistic
     * @param fromBusItemKey
     * @param fromBusItemValue 
     * @throws IllegalArgumentException
     */
    private static void removeDataForCurrentTypeWordBus(
            final ThStorageWordRule outerRuleStorageWord,
            final Integer fromBusItemKey, 
            final ConcurrentHashMap<String, String> fromBusItemValue){
        Integer typeWord;
        ConcurrentHashMap<String, String> hexTagNameSubString;
        ThStorageWordRule funcRuleStorageWord;
        try {
            funcRuleStorageWord = (ThStorageWordRule) outerRuleStorageWord;
            typeWord = fromBusItemKey;
            hexTagNameSubString = fromBusItemValue;
            for(Map.Entry<String, String> itemsHexTagSubStr : hexTagNameSubString.entrySet()){
                String recHexTagName = (String) itemsHexTagSubStr.getKey();
                String recSubString = (String) itemsHexTagSubStr.getValue();
                int tagNamelength = (int) recHexTagName.length();
                int strSubStringlength = (int) recSubString.length();
                
                if( (strSubStringlength * 4) != tagNamelength ){
                    throw new IllegalArgumentException(ThStorageWordLogicRouter.class.getCanonicalName() 
                            + " illegal length of inputed in index string, hexTagName: "
                            + recHexTagName + " lengthHex: " + recHexTagName.length()
                            + " strSubString: " + recSubString + " lengthStr: " + recSubString.length()
                            + " lengthHex == lengthStr * 4 ");
                }
                
                if( tagNamelength < 4 ){
                    throw new IllegalArgumentException(ThStorageWordLogicRouter.class.getCanonicalName() 
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
                setFlagsToStatisticsList(funcRuleStorageWord,
                        typeWord, 
                        recHexTagName, 
                        recSubString);
                
                
                
                String[] oldRecVal = {recHexTagName, recSubString};
                oldRecVal = null;
            }
            
        } finally {
            typeWord = null;
            hexTagNameSubString = null;
            funcRuleStorageWord = null;
        }
        
    }
    private static void setFlagsToStatisticsList(
            final ThStorageWordRule outerRuleStorageWord,
            final Integer typeWordInputed, 
            final String tagNameInputed, 
            final String strSubStringInputed){
        ThStorageWordRule funcRuleStorageWord;
        ThStorageWordStatusMainFlow currentStorageWordStatistic;
        ThStorageWordFlowReaded storageWordFlowReaded;
        ThStorageWordState storageWordState;
        ThStorageWordBusWriter busJobForStorageWordRouterJobToWriter;
        ThStorageWordBusReader busJobForStorageWordRouterJobToReader;
        
        ThStorageWordStatusName thStorageWordStatusName;
        ThStorageWordStatusActivity thStorageWordStatusActivity;
        ThStorageWordStatusDataCache thStorageWordStatusDataCache;
        ThStorageWordCache thStorageWordCache;
        ThStorageWordCacheReaded thStorageWordCacheReaded;
        ThStorageWordStatusWorkers thStorageWordStatusWorkers;
        ThStorageWordStatusDataFs thStorageWordStatusDataFs;
        
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
            funcRuleStorageWord = (ThStorageWordRule) outerRuleStorageWord;
            currentStorageWordStatistic = (ThStorageWordStatusMainFlow) funcRuleStorageWord.getStorageWordStatistic();
            storageWordFlowReaded = (ThStorageWordFlowReaded) funcRuleStorageWord.getStorageWordFlowReaded();
            
            storageWordState = (ThStorageWordState) funcRuleStorageWord.getStorageWordState();
            busJobForStorageWordRouterJobToWriter = (ThStorageWordBusWriter) 
                    storageWordState.getBusJobForStorageWordRouterJobToWriter();
            
            busJobForStorageWordRouterJobToReader = (ThStorageWordBusReader) 
                    storageWordState.getBusJobForStorageWordRouterJobToReader();
            
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
                currentStorageWordStatistic.getTypeWordTagFileNameFlowUuids(typeWordFunc, tagNameFunc, strSubStringFunc);
            
            /**
             * read from readed jobs UUIDs, get data from cache and write it
             */
            
            typeWordTagFileNameReadedFlowUuids = 
                storageWordFlowReaded.getTypeWordTagFileNameReadedFlowUuids(typeWordFunc, tagNameFunc, strSubStringFunc);
            
            if( typeWordTagFileNameMainFlowUuids == null ){
                throw new NullPointerException(ThStorageWordLogicRouter.class.getCanonicalName() 
                            + " return null from " + ThStorageWordStatusMainFlow.class.getCanonicalName()
                            + ".getTypeWordTagFileNameFlowUuids(typeWord, hexTagName, strSubString), for params values:"
                            + " typeWord: "
                            + String.valueOf(typeWordFunc) + ", hexTagName: "
                            + tagNameFunc + " lengthHex: " + tagNameFunc.length()
                            + " strSubString: " + strSubStringFunc + " lengthStr: " + strSubStringFunc.length()
                            + " lengthStr * 4: " + strSubStringFunc.length());
            }
            if( typeWordTagFileNameReadedFlowUuids == null ){
                throw new NullPointerException(ThStorageWordLogicRouter.class.getCanonicalName() 
                            + " return null from " + ThStorageWordFlowReaded.class.getCanonicalName()
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
            
            thStorageWordCache = currentStorageWordStatistic.getStorageWordCache();
            thStorageWordCacheReaded = currentStorageWordStatistic.getStorageWordCacheReaded();
            
            thStorageWordStatusDataFs = currentStorageWordStatistic.getStorageWordStatusDataFs();
            thStorageWordStatusName = currentStorageWordStatistic.getStorageWordStatusName();
            thStorageWordStatusActivity = currentStorageWordStatistic.getStorageWordStatusActivity();
            thStorageWordStatusDataCache = currentStorageWordStatistic.getStorageWordStatusDataCache();
            thStorageWordStatusWorkers = currentStorageWordStatistic.getStorageWordStatusWorkers();
            
            if( !typeWordTagFileNameMainFlowUuids.isEmpty() ){
                isMainFlowExist = Boolean.TRUE;
                //set not default values
                for( Map.Entry<UUID, ConcurrentHashMap<Integer, UUID>> itemMainFlow : typeWordTagFileNameMainFlowUuids.entrySet() ) {
                    UUID keyMainFlow = itemMainFlow.getKey();
                    if( itemMainFlow.getValue().size() == 5 ){
                        UUID keyDataFs = itemMainFlow.getValue().get("ThStorageWordStatusDataFs".hashCode());
                        UUID keyName = itemMainFlow.getValue().get("ThStorageWordStatusName".hashCode());
                        UUID keyActivity = itemMainFlow.getValue().get("ThStorageWordStatusActivity".hashCode());
                        UUID keyDataCache = itemMainFlow.getValue().get("ThStorageWordStatusDataCache".hashCode());
                        UUID keyWorkers = itemMainFlow.getValue().get("ThStorageWordStatusWorkers".hashCode());

                        /**
                         * validate, catch, remove not valide
                         */
                        try{
                            thStorageWordStatusDataFs.validateCountParams(keyDataFs);
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
                                    + "reason not set ThStorageWordStatusDataFs for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        try{
                            thStorageWordStatusName.validateCountParams(keyName);
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
                                    + "reason not set ThStorageWordStatusName for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        try{
                            thStorageWordStatusActivity.validateCountParams(keyActivity);
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
                                    + "reason not set ThStorageWordStatusActivity for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        try{
                            thStorageWordStatusDataCache.validateCountParams(keyDataCache);
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
                                    + "reason not set ThStorageWordStatusDataCache for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        try{
                            thStorageWordStatusWorkers.validateCountParams(keyWorkers);
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
                                    + "reason not set ThStorageWordStatusWorkers for UUID "
                                    + keyMainFlow.toString());
                            continue;
                        }
                        ConcurrentHashMap<Integer, String> statusNameForKeyPointFlow = thStorageWordStatusName.getStatusNameForKeyPointFlow(keyName);
                        /**
                         * list for writed, not readed UUID check with readed list
                         * list for writed and readed
                         * list for writed
                         */
                        ConcurrentHashMap<Integer, Boolean> statusWorkersForKeyPointFlow = thStorageWordStatusWorkers.getStatusWorkersForKeyPointFlow(keyWorkers);
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
                                                    busJobForStorageWordRouterJobToReader.getBusForTypeWord(typeWordFunc);
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
                                                    thStorageWordCacheReaded.pollTypeWordTagFileNameData(typeWordFunc, tagNameFunc, strSubStringFunc);
                                            Boolean addAllDataIntoCache = 
                                                    thStorageWordCache.addAllDataIntoCache(typeWordFunc, tagNameFunc, strSubStringFunc, pollTypeWordTagFileNameData);
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
                                            
                                            ConcurrentHashMap<Integer, Integer> statusDataFsForKeyPointFlow = thStorageWordStatusDataFs.getStatusDataFsForKeyPointFlow(keyDataFs);
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
                     * StorageWord index System change data in HashMap by keyWords
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
             * in index system StorageWord data fields if not save in UUID key
             * for distinct fields
             */
            
            Boolean isCachedData = Boolean.FALSE;
            
            isCachedData = thStorageWordCache.setDataIntoCacheFlow(typeWordFunc, tagNameFunc, strSubStringFunc);
            
            Boolean isCachedReadedData = Boolean.FALSE;
            
            isCachedReadedData = thStorageWordCacheReaded.isCacheReadedHasData(
                                                typeWordFunc, 
                                                tagNameFunc, 
                                                strSubStringFunc);
            /**
             * @todo if isCachedReadedData true, get from cache, add to list for
             * write
             */
            
            
            buildTypeWordStoreSubDirictoriesFunc = (String) ThStorageWordHelperFileSystem.buildTypeWordStoreSubDirictories(
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
            Integer countFsCountRecordsDestMoveTo = (int) thStorageWordCache.sizeDataInCache(typeWordFunc, tagNameFunc, strSubStringFunc);
            Integer countFsVolumeNumberSrc = volNumSettedInFlow;
            Integer countFsVolumeNumberDestMoveTo = volNumSettedInFlow;
            
            keyFlowStatusDataFs = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusDataFs".hashCode(), keyFlowStatusDataFs);
            
            
            thStorageWordStatusDataFs.createStructureParamsCountFs(
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
            keysPointsFlow.put("ThStorageWordStatusName".hashCode(), keyFlowStatusName);
            
            
            thStorageWordStatusName.createStructureParamsNamesFs(
                    keyFlowStatusName,
                    buildTypeWordStoreSubDirictoriesFunc,
                    namesFsFileNameSrc, 
                    namesFsFileNameDestMoveTo,
                    namesFsFileNameDestMoveTo,
                    fileDataListPrefix);
            Integer timeUSEIterationIncrement = 0;
            
            keyFlowStatusActivity = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusActivity".hashCode(), keyFlowStatusActivity);
            
            
            thStorageWordStatusActivity.createAddToListParamsTimeUse(
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
            keysPointsFlow.put("ThStorageWordStatusDataCache".hashCode(), keyFlowStatusDataCache);
            
            
            thStorageWordStatusDataCache.createStructureParamsCountTmp(
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
            keysPointsFlow.put("ThStorageWordStatusWorkers".hashCode(), keyFlowStatusWorkers);
            
            mainFlowContent = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>();
            mainFlowContent.put(mainFlowLabel, keysPointsFlow);
            thStorageWordStatusWorkers.createStructureParamsFlagsProc(
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
            
            currentStorageWordStatistic.setParamsPointsFlow(
                            typeWordFunc, 
                            tagNameFunc, 
                            strSubStringFunc,
                            mainFlowContent);
            
            ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>> busForTypeWord 
                    = busJobForStorageWordRouterJobToWriter.getBusForTypeWord(typeWordFunc);
            
            ConcurrentHashMap<String, String> dataForOutput = new ConcurrentHashMap<String, String>();
            dataForOutput.put(tagNameFunc, strSubStringFunc);
            /**
             * ... so part for job data hash..., readed hash, restructure cash on parts readed and write
             */
            busForTypeWord.put(mainFlowLabel, dataForOutput);
            /**
             * isWriteProcess = TRUE;
             */
            //thStorageWordCache.printCacheData();
            
        } catch(IllegalArgumentException exIllArg) {
            System.err.println(exIllArg.getMessage());
            exIllArg.printStackTrace();
            
        } finally {
            currentStorageWordStatistic = null;
            storageWordFlowReaded = null;
            typeWordFunc =  null;
            tagNameFunc =  null;
            strSubStringFunc =  null;
            buildTypeWordStoreSubDirictoriesFunc =  null;
            
            funcRuleStorageWord = null;
            currentStorageWordStatistic = null;
            storageWordState = null;
            busJobForStorageWordRouterJobToWriter = null;
            
            thStorageWordStatusName = null;
            thStorageWordStatusActivity = null;
            thStorageWordStatusDataCache = null;
            thStorageWordCache = null;
            thStorageWordStatusWorkers = null;
            thStorageWordStatusDataFs = null;
            
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
