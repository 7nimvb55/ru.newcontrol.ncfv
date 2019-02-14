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
    protected void doRouterForIndexStorageWord(ThStorageWordRule outerRuleStorageWord){
        ThIndexRule indexRule = outerRuleStorageWord.getIndexRule();
        ThIndexStatistic indexStatistic = indexRule.getIndexStatistic();
        ThStorageWordState storageWordState = outerRuleStorageWord.getStorageWordState();
        ThStorageWordStatistic storageWordStatistic = outerRuleStorageWord.getStorageWordStatistic();
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
                removeDataForCurrentTypeWordBus(storageWordStatistic, items.getKey(), busForTypeStorageWordRouter.remove(items.getKey()));
                
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
                removeDataForCurrentTypeWordBus(storageWordStatistic, items.getKey(), busForTypeStorageWordRouter.remove(items.getKey()));
                
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
            ThStorageWordStatistic storageWordStatistic,
            final Integer fromBusItemKey, 
            final ConcurrentHashMap<String, String> fromBusItemValue){
        Integer typeWord;
        ConcurrentHashMap<String, String> hexTagNameSubString;
        try {
            typeWord = fromBusItemKey;
            hexTagNameSubString = fromBusItemValue;
            for(Map.Entry<String, String> itemsHexTagSubStr : hexTagNameSubString.entrySet()){
                String recHexTagName = itemsHexTagSubStr.getKey();
                String recSubString = itemsHexTagSubStr.getValue();
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
                setFlagsToStatisticsList(storageWordStatistic,
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
        }
        
    }
    private static void setFlagsToStatisticsList(
            ThStorageWordStatistic storageWordStatistic,
            final Integer typeWordInputed, 
            final String tagNameInputed, 
            final String strSubStringInputed,
            final String buildTypeWordStoreSubDirictoriesInputed){
        ThStorageWordStatistic currentStorageWordStatistic;
        Integer typeWordFunc;
        String tagNameFunc;
        String strSubStringFunc;
        String buildTypeWordStoreSubDirictoriesFunc;
        
        ConcurrentHashMap<Integer, UUID> keysPointsFlow;
        
        String storageWordFileNameSrc;
        String namesFsFileNameDestMoveTo;
        try{
            currentStorageWordStatistic = storageWordStatistic;
            typeWordFunc = typeWordInputed;
            tagNameFunc = tagNameInputed;
            strSubStringFunc = strSubStringInputed;
            buildTypeWordStoreSubDirictoriesFunc = buildTypeWordStoreSubDirictoriesInputed;
            
            keysPointsFlow = new ConcurrentHashMap<Integer, UUID>();
            
            Integer countFsCountRecordsSrc = 0;
            Integer countFsCountRecordsDestMoveTo = 1;
            Integer countFsVolumeNumberSrc = 0;
            Integer countFsVolumeNumberDestMoveTo = 0;
            
            UUID keyFlowStatusDataFs = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusDataFs".hashCode(), keyFlowStatusDataFs);
            ThStorageWordStatusDataFs thStorageWordStatusDataFs = currentStorageWordStatistic.getStorageWordStatusDataFs();
            
            thStorageWordStatusDataFs.createStructureParamsCountFs(
                    keyFlowStatusDataFs,
                    countFsCountRecordsSrc, 
                    countFsVolumeNumberSrc);
            //this is a jobWrite UUID, to fix create jobListsStatus
            UUID randomUUID = UUID.randomUUID();
            
            storageWordFileNameSrc = new String()
                    .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                    .concat(randomUUID.toString().concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                    .concat(String.valueOf(countFsCountRecordsSrc))
                    .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                    .concat(String.valueOf(countFsVolumeNumberSrc));
            Path getNamesFsFileNameSrc = Paths.get(buildTypeWordStoreSubDirictoriesFunc, storageWordFileNameSrc);
            String namesFsFileNameSrc = getNamesFsFileNameSrc.toString();
            
            String storageWordFileNameDestMoveTo = new String()
                    .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                    .concat(randomUUID.toString().concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                    .concat(String.valueOf(countFsCountRecordsDestMoveTo))
                    .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                    .concat(String.valueOf(countFsVolumeNumberDestMoveTo));
            Path getNamesFsFileNameDestMoveTo = Paths.get(buildTypeWordStoreSubDirictoriesFunc, storageWordFileNameDestMoveTo);
            namesFsFileNameDestMoveTo = getNamesFsFileNameDestMoveTo.toString();
            
            UUID keyFlowStatusName = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusName".hashCode(), keyFlowStatusName);
            ThStorageWordStatusName thStorageWordStatusName = currentStorageWordStatistic.getStorageWordStatusName();
            
            thStorageWordStatusName.createStructureParamsNamesFs(
                    keyFlowStatusName,
                    namesFsFileNameSrc, 
                    namesFsFileNameDestMoveTo);
            Integer timeUSEIterationIncrement = 0;
            
            UUID keyFlowStatusActivity = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusActivity".hashCode(), keyFlowStatusActivity);
            ThStorageWordStatusActivity thStorageWordStatusActivity = currentStorageWordStatistic.getStorageWordStatusActivity();
            
            thStorageWordStatusActivity.createAddToListParamsTimeUse(
                    keyFlowStatusActivity, 
                    timeUSEIterationIncrement);
            Integer countTmpCurrentInCache = 0;
            
            Integer countTmpAddNeedToFileSystemLimit = AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT - countFsCountRecordsDestMoveTo;
            Integer countTmpIndexSystemLimitOnStorage = AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT;
            
            UUID keyFlowStatusDataCache = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusDataCache".hashCode(), keyFlowStatusDataCache);
            ThStorageWordStatusDataCache thStorageWordStatusDataCache = currentStorageWordStatistic.getStorageWordStatusDataCache();
            ThStorageWordCache thStorageWordCache = currentStorageWordStatistic.getStorageWordCache();
            /**
             * @todo
             * in index system StorageWord data fields if not save in UUID key
             * for distinct fields
             */
            thStorageWordCache.setDataIntoCacheFlow(keyFlowStatusDataCache, typeWordFunc, tagNameFunc, strSubStringFunc);
            
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
            
            
            
            
            
            UUID keyFlowStatusWorkers = UUID.randomUUID();
            keysPointsFlow.put("ThStorageWordStatusWorkers".hashCode(), keyFlowStatusWorkers);
            ThStorageWordStatusWorkers thStorageWordStatusWorkers = currentStorageWordStatistic.getStorageWordStatusWorkers();
            
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
                            keysPointsFlow);
            
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
            
        }
    }
}
