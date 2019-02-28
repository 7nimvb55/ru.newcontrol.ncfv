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
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.nio.file.StandardCopyOption;
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
    protected void doWriteToIndexStorageWord(final ThStorageWordRule outerRuleStorageWord){
        ThIndexRule indexRule;
        ThIndexStatistic indexStatistic;
        ThStorageWordRule funcRuleStorageWord;
        AppFileStorageIndex currentIndexStorages;
        try{
            long counIterations = 0;
            /**
             * @todo
             * Rule
             * Statistic for this index system
             */
            funcRuleStorageWord = (ThStorageWordRule) outerRuleStorageWord;
            
            indexRule = funcRuleStorageWord.getIndexRule();
            indexStatistic = indexRule.getIndexStatistic();
            indexStatistic.updateDataStorages();
            currentIndexStorages = funcRuleStorageWord.getIndexRule().getIndexState().currentIndexStorages();
            URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD);
            Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(
                    AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD); 
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
                ThStorageWordState wordState = (ThStorageWordState) funcRuleStorageWord.getStorageWordState();
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
                        UUID mainFlowLabel = forWriterJobUUID.getKey();

                        for(Map.Entry<String, String> itemsTagNames : forWriterJobUUID.getValue().entrySet()){
                            /**
                             * @todo to do function for return removed data from cache
                             */
                            Integer typeWordBusNumber = busVal.getKey();
                            String hexTagName = itemsTagNames.getKey();
                            String subStringValue = itemsTagNames.getValue();

                            ThStorageWordStatistic storageWordStatistic = (ThStorageWordStatistic) outerRuleStorageWord.getStorageWordStatistic();

                            ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>> typeWordTagFileNameFlowUuids = 
                                (ConcurrentHashMap<UUID, ConcurrentHashMap<Integer, UUID>>) storageWordStatistic.getTypeWordTagFileNameFlowUuids(
                                typeWordBusNumber, 
                                hexTagName, 
                                subStringValue);

                            ConcurrentHashMap<Integer, UUID> flowPointsUUID = 
                                    (ConcurrentHashMap<Integer, UUID>) typeWordTagFileNameFlowUuids.get(mainFlowLabel);
                            int countKeysByNamesExist = 0;
                            if( flowPointsUUID != null ) {
                                if( !flowPointsUUID.isEmpty() ){
                                    if( flowPointsUUID.size() == 5 ){
                                        if( !flowPointsUUID.containsKey("ThStorageWordStatusDataFs".hashCode()) ){
                                            ConcurrentHashMap<String, String> remove = busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusDataFs for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        if( !flowPointsUUID.containsKey("ThStorageWordStatusName".hashCode()) ){
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusName for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        if( !flowPointsUUID.containsKey("ThStorageWordStatusActivity".hashCode()) ){
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusActivity for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        if( !flowPointsUUID.containsKey("ThStorageWordStatusDataCache".hashCode()) ){
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusDataCache for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        if( !flowPointsUUID.containsKey("ThStorageWordStatusWorkers".hashCode()) ){
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusWorkers for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        /**
                                         * validate values, write to file
                                         */
                                        ThStorageWordCache storageWordCache = 
                                                (ThStorageWordCache) storageWordStatistic.getStorageWordCache();
                                        ThStorageWordStatusActivity storageWordStatusActivity = 
                                                (ThStorageWordStatusActivity) storageWordStatistic.getStorageWordStatusActivity();
                                        
                                        UUID getKeyActivity = (UUID) flowPointsUUID.get("ThStorageWordStatusActivity".hashCode());
                                        try{
                                            storageWordStatusActivity.validateCountParams(getKeyActivity);
                                        } catch (IllegalArgumentException exActiv) {
                                            exActiv.getMessage();
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusActivity for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        ThStorageWordStatusDataCache storageWordStatusDataCache = 
                                                (ThStorageWordStatusDataCache) storageWordStatistic.getStorageWordStatusDataCache();
                                        
                                        UUID getKeyDataCache = (UUID) flowPointsUUID.get("ThStorageWordStatusDataCache".hashCode());
                                        try{
                                            storageWordStatusDataCache.validateCountParams(getKeyDataCache);
                                        } catch (IllegalArgumentException exActiv) {
                                            exActiv.getMessage();
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusDataCache for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        
                                        ThStorageWordStatusDataFs storageWordStatusDataFs = 
                                                (ThStorageWordStatusDataFs) storageWordStatistic.getStorageWordStatusDataFs();
                                        
                                        UUID getKeyDataFs = (UUID) flowPointsUUID.get("ThStorageWordStatusDataFs".hashCode());
                                        try{
                                            storageWordStatusDataFs.validateCountParams(getKeyDataFs);
                                        } catch (IllegalArgumentException exActiv) {
                                            exActiv.getMessage();
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusDataFs for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        
                                        
                                        ThStorageWordStatusName storageWordStatusName = 
                                                (ThStorageWordStatusName) storageWordStatistic.getStorageWordStatusName();
                                        
                                        UUID getKeyName = (UUID) flowPointsUUID.get("ThStorageWordStatusName".hashCode());
                                        try{
                                            storageWordStatusName.validateCountParams(getKeyName);
                                        } catch (IllegalArgumentException exName) {
                                            exName.getMessage();
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusName for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        
                                        
                                        ThStorageWordStatusWorkers storageWordStatusWorkers = 
                                                (ThStorageWordStatusWorkers) storageWordStatistic.getStorageWordStatusWorkers();
                                        
                                        UUID getKeyWorkers = (UUID) flowPointsUUID.get("ThStorageWordStatusWorkers".hashCode());
                                        try{
                                            storageWordStatusWorkers.validateCountParams(getKeyWorkers);
                                        } catch (IllegalArgumentException exWorkers) {
                                            exWorkers.getMessage();
                                            busVal.getValue().remove(mainFlowLabel);
                                            System.err.println("-----------------"
                                                    + "||||||||||||||||||"
                                                    + "-----------------"
                                                    + "||||||||||||||||||"
                                                    + "----------------- data in flow key not valid, removed, "
                                                    + "reason not set ThStorageWordStatusWorkers for UUID "
                                                    + mainFlowLabel.toString());
                                            continue;
                                        }
                                        
                                        ConcurrentHashMap<Integer, String> statusNameForKeyPointFlow = storageWordStatusName.getStatusNameForKeyPointFlow(getKeyName);
                                        ConcurrentHashMap<Integer, Boolean> statusWorkersForKeyPointFlow = storageWordStatusWorkers.getStatusWorkersForKeyPointFlow(getKeyWorkers);
                                        Boolean getIsWriteInProcess = statusWorkersForKeyPointFlow.get(1640531930);
                                        if( getIsWriteInProcess ){
                                            continue;
                                        }
                                        //isWriteProcess - 1640531930
                                        
                                        
                                        //storageDirectoryName - 1962941405
                                        String storageDirectoryName = statusNameForKeyPointFlow.get(1962941405);
                                        //currentFileName - 1517772480
                                        String currentFileName = statusNameForKeyPointFlow.get(1517772480);
                                        //newFileName - 521024487
                                        String newFileName = statusNameForKeyPointFlow.get(521024487);
                                        
                                        
                                        ConcurrentHashMap<String, String> pollTypeWordTagFileNameData = null;
                                        try{
                                            pollTypeWordTagFileNameData = storageWordCache.pollTypeWordTagFileNameData(typeWordBusNumber, hexTagName, subStringValue);
                                        } catch (NullPointerException exRetNull) {
                                            System.err.println(exRetNull.getMessage());
                                            exRetNull.printStackTrace();
                                            continue;
                                        }
                                        /**
                                         * @todo function for removed return cache data for write
                                         */
                                        if( pollTypeWordTagFileNameData == null ){
                                            continue;
                                        }
                                        Path storageTypeWordWritedFile = fsForReadData.getPath(storageDirectoryName);
                                        if( Files.notExists(storageTypeWordWritedFile, LinkOption.NOFOLLOW_LINKS) ){
                                            try{
                                                Files.createDirectories(storageTypeWordWritedFile);
                                            } catch (FileAlreadyExistsException exAlreadyExist) {
                                                exAlreadyExist.printStackTrace();
                                            } catch (SecurityException exSecurity) {
                                                exSecurity.printStackTrace();
                                            } catch (UnsupportedOperationException exUnSupp) {
                                                exUnSupp.printStackTrace();
                                            }
                                            
                                        }
                                        /**
                                         * need additional flags 
                                         * isErrorOnWrite, 
                                         * isErrorOnMove, 
                                         * isNullOnDataInCache,
                                         * isErrorOnDataInCache,
                                         */
                                        Path nowWritedFile = fsForReadData.getPath(currentFileName);
                                        
                                        try(ObjectOutputStream oos = 
                                            new ObjectOutputStream(Files.newOutputStream(nowWritedFile)))
                                        {
                                            oos.writeObject(pollTypeWordTagFileNameData);
                                            System.out.println(ThWordLogicWrite.class.getCanonicalName() 
                                                    + " => => =>                                             => => => " 
                                                    + nowWritedFile.toUri().toString() 
                                                    + " writed size " + pollTypeWordTagFileNameData.size());
                                            statusWorkersForKeyPointFlow.put(1640531930, Boolean.TRUE);
                                        } catch(Exception ex){
                                            ex.printStackTrace();
                                        }
                                        
                                        //isMoveFileReady - -1884096596
                                        Boolean getIsMoveReady = statusWorkersForKeyPointFlow.get(-1884096596);
                                        if( getIsMoveReady ){
                                            continue;
                                        }
                                        
                                        Path moveToFile = fsForReadData.getPath(newFileName);
                                        try{
                                            Files.move(nowWritedFile, moveToFile, StandardCopyOption.ATOMIC_MOVE);
                                            statusWorkersForKeyPointFlow.put(-1884096596, Boolean.TRUE);
                                        } catch(SecurityException exSecurity) {
                                            System.err.println(exSecurity.getMessage());
                                            exSecurity.printStackTrace();
                                        } catch(AtomicMoveNotSupportedException exAtomic) {
                                            System.err.println(exAtomic.getMessage());
                                            exAtomic.printStackTrace();
                                        } catch(FileAlreadyExistsException exAlreadyExists) {
                                            System.err.println(exAlreadyExists.getMessage());
                                            exAlreadyExists.printStackTrace();
                                        } catch(UnsupportedOperationException exUnsupported) {
                                            System.err.println(exUnsupported.getMessage());
                                            exUnsupported.printStackTrace();
                                        }
                                        
                                    }
                                }
                                
                            }

                            try{
                                /*checkDataForWrite(funcRuleStorageWord, typeWordBusNumber, hexTagName, subStringValue);*/
                            } catch(IllegalArgumentException exArg) {
                                System.err.println(exArg.getMessage());
                                exArg.printStackTrace();
                            }
                        }
                    }
                } 

                } finally {
                    
                }
            } while( outerRuleStorageWord.isRunnedStorageWordWorkRouter() );
        } catch(FileSystemNotFoundException ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } catch(ProviderNotFoundException ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } catch(IllegalArgumentException ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } catch(SecurityException ex){
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
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
                if( countThStorageWordStatusName != 3 ){
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
                
                UUID getUUIDStatusDataCache = flowPointsMap.get(838467829);
                
                if( !storageWordStatusWorkers.isStatusWorkersNotExist(keyMainFlow) ){
                    ConcurrentHashMap<Integer, Boolean> statusWorkersForKeyPointFlow = storageWordStatusWorkers.getStatusWorkersForKeyPointFlow(keyMainFlow);
                    Boolean isDataSendIntoCahe = statusWorkersForKeyPointFlow.get(-2091433802);
                    if( isDataSendIntoCahe ){
                        
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
