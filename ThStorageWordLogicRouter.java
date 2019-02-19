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
 * directory, create or add to path, get from bus data, read heximal value,  
 * first four bytes, create or add to sub path, calculate length for subString 
 * value, generate name for list file in format wl-(UUID)-(Size)-(Volume Number)
 * 
 * ThStorageWordStatistic - search directories and list file names
 * ThStorageWordCache - temp storages for data
 * ThStorageWordHelperFileSystem - static functions for create, move, scan 
 * directories and list files
 * 
 * @author wladimirowichbiaran
 */
public class ThStorageWordLogicRouter {
    protected void doRouterForIndexStorageWord(final ThStorageWordRule outerRuleStorageWord){
        final ThStorageWordRule funcRuleStorageWord = (ThStorageWordRule) outerRuleStorageWord;
        ThIndexRule indexRule = funcRuleStorageWord.getIndexRule();
        ThIndexStatistic indexStatistic = indexRule.getIndexStatistic();
        ThStorageWordState storageWordState = funcRuleStorageWord.getStorageWordState();
        ThStorageWordStatistic storageWordStatistic = funcRuleStorageWord.getStorageWordStatistic();
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
                removeDataForCurrentTypeWordBus(funcRuleStorageWord, items.getKey(), busForTypeStorageWordRouter.remove(items.getKey()));
                
            }
        } while( outerRuleStorageWord.isRunnedStorageWordWorkFilter() );
        
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> busForTypeStorageWordRouter = busJobForStorageWordRouter.getMaxUsedBusesSet();
            for(Map.Entry<Integer, ConcurrentHashMap<String, String>> items : busForTypeStorageWordRouter.entrySet()){
                System.out.println("From bus typeWord " + items.getKey());
                /*for(Map.Entry<String, String> itemsOfBus : items.getValue().entrySet()){
                    System.out.println("For bus hexWord " 
                            + itemsOfBus.getKey() 
                            + " subString " 
                            + items.getValue().remove(itemsOfBus.getKey()));

                }*/
                removeDataForCurrentTypeWordBus(funcRuleStorageWord, items.getKey(), busForTypeStorageWordRouter.remove(items.getKey()));
                
        }
        System.out.println("++++++++++++++++++++++++++++++stop " + ThStorageWordLogicRouter.class.getCanonicalName());
    }
    /**
     * 
     * @param storageWordStatistic
     * @param fromBusItemKey
     * @param fromBusItemValue 
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
                String buildTypeWordStoreSubDirictories = ThStorageWordHelperFileSystem.buildTypeWordStoreSubDirictories(
                        typeWord,
                        recHexTagName.substring(0, 3), 
                        recSubString.length());
                //(2)
                setFlagsToStatisticsList(funcRuleStorageWord,
                        typeWord, 
                        recHexTagName, 
                        recSubString, 
                        buildTypeWordStoreSubDirictories);
                
                
                
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
            final String strSubStringInputed,
            final String buildTypeWordStoreSubDirictoriesInputed){
        ThStorageWordRule funcRuleStorageWord;
        ThStorageWordStatistic currentStorageWordStatistic;
        ThStorageWordState storageWordState;
        ThStorageWordBusWriter busJobForStorageWordRouterJobToWriter;
        
        ThStorageWordStatusName thStorageWordStatusName;
        ThStorageWordStatusActivity thStorageWordStatusActivity;
        ThStorageWordStatusDataCache thStorageWordStatusDataCache;
        ThStorageWordCache thStorageWordCache;
        ThStorageWordStatusWorkers thStorageWordStatusWorkers;
        ThStorageWordStatusDataFs thStorageWordStatusDataFs;
        
        Integer typeWordFunc;
        String tagNameFunc;
        String strSubStringFunc;
        String buildTypeWordStoreSubDirictoriesFunc;
        
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
        try{
            funcRuleStorageWord = (ThStorageWordRule) outerRuleStorageWord;
            currentStorageWordStatistic = (ThStorageWordStatistic) funcRuleStorageWord.getStorageWordStatistic();
            storageWordState = (ThStorageWordState) funcRuleStorageWord.getStorageWordState();
            busJobForStorageWordRouterJobToWriter = (ThStorageWordBusWriter) storageWordState.getBusJobForStorageWordRouterJobToWriter();
            typeWordFunc = typeWordInputed;
            tagNameFunc = tagNameInputed;
            strSubStringFunc = strSubStringInputed;
            buildTypeWordStoreSubDirictoriesFunc = buildTypeWordStoreSubDirictoriesInputed;
            
            keysPointsFlow = new ConcurrentHashMap<Integer, UUID>();
            
            Integer countFsCountRecordsSrc = 0;
            Integer countFsCountRecordsDestMoveTo = 1;
            Integer countFsVolumeNumberSrc = 0;
            Integer countFsVolumeNumberDestMoveTo = 0;
            
            keyFlowStatusDataFs = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusDataFs".hashCode(), keyFlowStatusDataFs);
            thStorageWordStatusDataFs = currentStorageWordStatistic.getStorageWordStatusDataFs();
            
            thStorageWordStatusDataFs.createStructureParamsCountFs(
                    keyFlowStatusDataFs,
                    countFsCountRecordsSrc, 
                    countFsVolumeNumberSrc);
            //this is a jobWrite UUID, to fix create jobListsStatus
            mainFlowLabel = UUID.randomUUID();
            
            storageWordFileNameSrc = new String()
                    .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                    .concat(mainFlowLabel.toString().concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
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
                    .concat(mainFlowLabel.toString().concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                    .concat(String.valueOf(countFsCountRecordsDestMoveTo))
                    .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                    .concat(String.valueOf(countFsVolumeNumberDestMoveTo));
            Path getNamesFsFileNameDestMoveTo = Paths.get(buildTypeWordStoreSubDirictoriesFunc, storageWordFileNameDestMoveTo);
            namesFsFileNameDestMoveTo = getNamesFsFileNameDestMoveTo.toString();
            
            keyFlowStatusName = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusName".hashCode(), keyFlowStatusName);
            thStorageWordStatusName = currentStorageWordStatistic.getStorageWordStatusName();
            
            thStorageWordStatusName.createStructureParamsNamesFs(
                    keyFlowStatusName,
                    namesFsFileNameSrc, 
                    namesFsFileNameDestMoveTo);
            Integer timeUSEIterationIncrement = 0;
            
            keyFlowStatusActivity = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusActivity".hashCode(), keyFlowStatusActivity);
            thStorageWordStatusActivity = currentStorageWordStatistic.getStorageWordStatusActivity();
            
            thStorageWordStatusActivity.createAddToListParamsTimeUse(
                    keyFlowStatusActivity, 
                    timeUSEIterationIncrement);
            Integer countTmpCurrentInCache = 0;
            
            Integer countTmpAddNeedToFileSystemLimit = AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT - countFsCountRecordsDestMoveTo;
            Integer countTmpIndexSystemLimitOnStorage = AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT;
            
            keyFlowStatusDataCache = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusDataCache".hashCode(), keyFlowStatusDataCache);
            thStorageWordStatusDataCache = currentStorageWordStatistic.getStorageWordStatusDataCache();
            thStorageWordCache = currentStorageWordStatistic.getStorageWordCache();
            /**
             * @todo
             * in index system StorageWord data fields if not save in UUID key
             * for distinct fields
             */
            thStorageWordCache.setDataIntoCacheFlow(typeWordFunc, tagNameFunc, strSubStringFunc);
            
            thStorageWordStatusDataCache.createStructureParamsCountTmp(
                    keyFlowStatusDataCache,
                    countTmpCurrentInCache, 
                    countTmpAddNeedToFileSystemLimit, 
                    countTmpIndexSystemLimitOnStorage);
            
            Boolean isWriteProcess = Boolean.FALSE;
            Boolean isReadProcess = Boolean.FALSE;
            Boolean isCachedData = Boolean.FALSE;
            Boolean isCalculatedData = Boolean.FALSE;
            Boolean isUdatedDataInHashMap = Boolean.FALSE;
            Boolean isMoveFileReady = Boolean.FALSE;
            
            keyFlowStatusWorkers = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusWorkers".hashCode(), keyFlowStatusWorkers);
            thStorageWordStatusWorkers = currentStorageWordStatistic.getStorageWordStatusWorkers();
            mainFlowContent = new ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>();
            mainFlowContent.put(mainFlowLabel, keysPointsFlow);
            thStorageWordStatusWorkers.createStructureParamsFlagsProc(
                    keyFlowStatusWorkers,
                    isWriteProcess, 
                    isReadProcess, 
                    isCachedData, 
                    isCalculatedData, 
                    isUdatedDataInHashMap, 
                    isMoveFileReady);
            
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
            
            thStorageWordCache.printCacheData();
            
        } catch(IllegalArgumentException exIllArg) {
            System.err.println(exIllArg.getMessage());
            exIllArg.printStackTrace();
            
        } finally {
            currentStorageWordStatistic = null;
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
            
            mainFlowLabel = null;
            keyFlowStatusDataFs = null;
            keyFlowStatusName = null;
            keyFlowStatusActivity = null;
            keyFlowStatusDataCache = null;
            keyFlowStatusWorkers = null;
            
        }
    }
    
}
