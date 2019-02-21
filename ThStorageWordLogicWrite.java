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

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThStorageWordLogicWrite {
    protected void doWriteToIndexStorageWord(ThStorageWordRule outerRuleStorageWord){
        ThIndexRule indexRule;
        ThIndexStatistic indexStatistic;
        AppFileStorageIndex currentIndexStorages;
        try{
    long counIterations = 0;
    /**
     * @todo
     * Rule
     * Statistic for this index system
     */
            indexRule = outerRuleStorageWord.getIndexRule();
            indexStatistic = indexRule.getIndexStatistic();
            indexStatistic.updateDataStorages();
            currentIndexStorages = outerRuleStorageWord.getIndexRule().getIndexState().currentIndexStorages();
            URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
            Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(
                    AppFileNamesConstants.FILE_INDEX_PREFIX_WORD); 
            try( FileSystem fsForReadData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){
        
        
        do{ 
            try{
            
            /**
             * @todo release some bus for each index system, 
             * got jobs and read write it in by queue system, workers whrere
             * opened storages
             * 
             * save data in limited file packets
             */
            ThStorageWordState wordState = outerRuleStorageWord.getStorageWordState();
            ThStorageWordBusWriter busJobForWrite = wordState.getBusJobForStorageWordRouterJobToWriter();
            
            
            ConcurrentHashMap<Integer, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> maxUsedBusesSet = busJobForWrite.getMaxUsedBusesSet();
            
            for(Map.Entry<Integer, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> busVal : maxUsedBusesSet.entrySet()){
                for(Map.Entry<UUID, ConcurrentHashMap<String, String>> forWriterJobUUID : busVal.getValue().entrySet()){
                    /**
                     * @todo get PointFlow for UUID
                     * call write func
                     * get keySet this level list, remove it from list, insert
                     * into function for write removed data 
                     */
                    
                    for(Map.Entry<String, String> itemsTagNames : forWriterJobUUID.getValue().entrySet()){
                        /**
                         * @todo to do function for return removed data from cache
                         */
                        Integer typeWordBusNumber = busVal.getKey();
                        String hexTagName = itemsTagNames.getKey();
                        String subStringValue = itemsTagNames.getValue();
                        checkDataForWrite(outerRuleStorageWord, typeWordBusNumber, hexTagName, subStringValue);
                        
                        
                        
                    }
                }
            } 
        
            } finally {
                
            }
        } while( outerRuleStorageWord.isRunnedStorageWordWorkRouter() );
        } catch(FileSystemNotFoundException ex){
            ex.printStackTrace();
        } catch(ProviderNotFoundException ex){
            ex.printStackTrace();
        } catch(IllegalArgumentException ex){
            ex.printStackTrace();
        } catch(SecurityException ex){
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
        } finally {
            
        }
        
    }
    private void checkDataForWrite(
            final ThStorageWordRule outerRuleStorageWord,
            final Integer typeWordBusNumberInputed,
            final String hexTagNameInputed,
            final String subStringValueInputed
    ){
        Integer typeWordBusNumberFunc;
        String hexTagNameFunc;
        String subStringValueFunc;
        
        ConcurrentHashMap<String, String> pollTypeWordTagFileNameData;
        
        try{
            typeWordBusNumberFunc = (Integer) typeWordBusNumberInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            subStringValueFunc = (String) subStringValueInputed;
            
            ThStorageWordState wordState = outerRuleStorageWord.getStorageWordState();
            ThStorageWordBusWriter busJobForWrite = wordState.getBusJobForStorageWordRouterJobToWriter();
            ThStorageWordStatistic storageWordStatistic = outerRuleStorageWord.getStorageWordStatistic();
            ThStorageWordCache storageWordCache = storageWordStatistic.getStorageWordCache();
            ThStorageWordStatusActivity storageWordStatusActivity = storageWordStatistic.getStorageWordStatusActivity();
            ThStorageWordStatusDataCache storageWordStatusDataCache = storageWordStatistic.getStorageWordStatusDataCache();
            ThStorageWordStatusDataFs storageWordStatusDataFs = storageWordStatistic.getStorageWordStatusDataFs();
            ThStorageWordStatusName storageWordStatusName = storageWordStatistic.getStorageWordStatusName();
            ThStorageWordStatusWorkers storageWordStatusWorkers = storageWordStatistic.getStorageWordStatusWorkers();
            
            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> typeWordTagFileNameFlowUuids = 
                    (ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>) storageWordStatistic.getTypeWordTagFileNameFlowUuids(
                    typeWordBusNumberFunc, 
                    hexTagNameFunc, 
                    subStringValueFunc);
                        
            for( Map.Entry<UUID, ConcurrentHashMap<Integer, UUID>> itemFlowMainUUID : typeWordTagFileNameFlowUuids.entrySet() ){

                int countThStorageWordStatusDataFs = 0;
                int countThStorageWordStatusName = 0;
                int countThStorageWordStatusActivity = 0;
                int countThStorageWordStatusDataCache = 0;
                int countThStorageWordStatusWorkers = 0;

                for(Map.Entry<Integer, UUID> itemsElements : itemFlowMainUUID.getValue().entrySet()){
                    switch ( itemsElements.getKey() ){
                        case 24537146: 
                            countThStorageWordStatusDataFs++;
                            continue; //ThStorageWordStatusDataFs
                        case -835430034: 
                            countThStorageWordStatusName++;
                            continue; //ThStorageWordStatusName
                        case -1339250574: 
                            countThStorageWordStatusActivity++;
                            continue; //ThStorageWordStatusActivity
                        case 838467829: 
                            countThStorageWordStatusDataCache++;
                            continue; //ThStorageWordStatusDataCache
                        case 842641138: 
                            countThStorageWordStatusWorkers++;
                            continue; //ThStorageWordStatusWorkers
                    }
                    new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters of data for write into file of StorageWord is not valid");
                }

                if( countThStorageWordStatusDataFs != 2 ){
                    new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters flowDataFs is not valid");
                }
                if( countThStorageWordStatusName != 2 ){
                    new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters flowName is not valid");
                    }
                if( countThStorageWordStatusActivity != 2 ){
                    new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters flowActivity is not valid");  
                }     
                if( countThStorageWordStatusDataCache != 2 ){
                    new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters flowDataCache is not valid");
                }
                if( countThStorageWordStatusWorkers != 5 ){
                    new IllegalArgumentException(ThStorageWordLogicWrite.class.getCanonicalName() 
                            + " parameters flowWorkers is not valid");
                }

                UUID keyMainFlow = itemFlowMainUUID.getKey();
                ConcurrentHashMap<Integer, UUID> flowPointsMap = itemFlowMainUUID.getValue();
                pollTypeWordTagFileNameData = null;
                try{
                    pollTypeWordTagFileNameData = storageWordCache.pollTypeWordTagFileNameData(typeWordBusNumberFunc, hexTagNameFunc, subStringValueFunc);
                } catch (NullPointerException exRetNull) {
                    System.err.println(exRetNull.getMessage());
                    exRetNull.printStackTrace();
                    continue;
                }
                /**
                 * @todo function for removed return cache data for write
                 */
                if( pollTypeWordTagFileNameData != null ){
                    dataFromCahePrint(pollTypeWordTagFileNameData);
                }
            }
        } finally {
            typeWordBusNumberFunc = null;
            hexTagNameFunc = null;
            subStringValueFunc = null;
            
            pollTypeWordTagFileNameData = null;
        }
        
    }
    private void dataFromCahePrint(final ConcurrentHashMap<String, String> inputedFromCacheData){
        ConcurrentHashMap<String, String> funcFromCacheData;
        try{
            if( inputedFromCacheData != null ){
                if( !inputedFromCacheData.isEmpty() ){
                    funcFromCacheData = (ConcurrentHashMap<String, String>) inputedFromCacheData;
                    Integer sizeOfReturnedPacket = (Integer) funcFromCacheData.size();
                    System.out.println("Returned from cache size" + sizeOfReturnedPacket);
                    for( Map.Entry<String, String> itemListFromCache : funcFromCacheData.entrySet() ){
                        System.out.println("-|-|-|-|-|-|-|-|-"
                                + "                        "
                                + "-|-|-|-|-|-|-|-|-"
                                + "                        "
                                + "Returned from cache tagName " + itemListFromCache.getKey()
                                + " subString " + itemListFromCache.getValue());
                    }
                }
            }
                
        } finally {
            funcFromCacheData = null;
        }
    }
   /* private void doWrite(){
        
        try{
            do{
                        if( !jobForWrite.isBlankObject() && !jobForWrite.isWritedDataEmpty() ){
                            //currentIndexStorages.updateMapForStorages();
                            String writerPath = jobForWrite.getFileNameForWrite();
                            
                            ConcurrentSkipListMap<UUID, TdataWord> writerData = 
                                    new ConcurrentSkipListMap<UUID, TdataWord>();
                            writerData.putAll(jobForWrite.getWriterData());
                            writerPath = indexStatistic.createNewNameForWriteWithAllAddRecords(
                                    AppFileNamesConstants.FILE_INDEX_PREFIX_WORD, 
                                    writerPath, 
                                    writerData);
                            
                            Path nowWritedFile = fsForReadData.getPath(writerPath);
                            
                            try(ObjectOutputStream oos = 
                                new ObjectOutputStream(Files.newOutputStream(nowWritedFile)))
                            {
                                oos.writeObject(writerData);
                                System.out.println(ThWordLogicWrite.class.getCanonicalName() 
                                        + " => => =>                                             => => => " 
                                        + nowWritedFile.toUri().toString() 
                                        + " writed size " + writerData.size());
                                indexStatistic.addToListSizeRemoveListProcess(
                                            AppFileNamesConstants.FILE_INDEX_PREFIX_WORD, 
                                            nowWritedFile);
                            } catch(Exception ex){
                                ex.printStackTrace();
                            }
                            writerData.clear();
                            writerData = null;
                            jobForWrite.cleanWriterData();
                            jobForWrite.setTrueWriterJobDone();
                        }
                        
                        jobForWrite = busJobForWrite.getJobForWrite();
                    } while( !busJobForWrite.isJobQueueEmpty() );
        } finally {
            
        }
    }*/
}
