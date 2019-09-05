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
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordLogicRead {
    protected void doReadFromIndexWord(final ThWordRule outerRuleWord){
        ThIndexRule indexRule;
        ThIndexStatistic indexStatistic;
        ThWordRule funcRuleWord;
        AppFileStorageIndex currentIndexStorages;
        UUID pollNextUuid;
        URI byPrefixGetUri;
        Map<String, String> byPrefixGetMap;
        ThWordEventLogic eventLogic;
        try {
            funcRuleWord = (ThWordRule) outerRuleWord;
            
            indexRule = funcRuleWord.getIndexRule();
            indexStatistic = indexRule.getIndexStatistic();
            indexStatistic.updateDataStorages();
            currentIndexStorages = funcRuleWord.getIndexRule().getIndexState().currentIndexStorages();
            byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
            byPrefixGetMap = currentIndexStorages.byPrefixGetMap( 
                    AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
            try( FileSystem fsForReadData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){
                do {
                    pollNextUuid = outerRuleWord.getWordState().getBusEventShort().pollNextUuid(2, 2);
                    if( checkStateForUuidOnDoRead(outerRuleWord, pollNextUuid) ){
                        //move uuid in bus event shot
                        //move uuid in statebuseventlocal
                        //do read data
                        //move uuid into wait read
                        eventLogic = (ThWordEventLogic) outerRuleWord.getWordState().getEventLogic();
                        try {
                            eventLogic.readDataFromStorage(fsForReadData, pollNextUuid);
                        } catch(IllegalStateException exIllState) {
                            System.err.println(exIllState.getMessage());
                            exIllState.printStackTrace();
                        }
                    } else {
                        //not founded in nextStep ready write uuids go into...
                    }
                } while( funcRuleWord.isRunnedWordWorkRouter() );
                //need read all cached data after end for all read jobs
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
            pollNextUuid = null;
            indexRule = null;
            indexStatistic = null;
            funcRuleWord = null;
            currentIndexStorages = null;
            byPrefixGetUri = null;
            byPrefixGetMap = null;
            eventLogic = null;
        }
    }
    protected Boolean checkStateForUuidOnDoRead(ThWordRule outerRuleWord, UUID checkedReturnedUuid){
        LinkedTransferQueue<Integer[]> foundedNodes;
        try {
            if( checkedReturnedUuid != null ){
                foundedNodes = outerRuleWord.getWordState().getBusEventShortNextStep().foundUuidInList(checkedReturnedUuid);
                while( !foundedNodes.isEmpty() ) {
                    Integer[] foundUuidInList = foundedNodes.poll();
                    if( foundUuidInList[0] == -1 || foundUuidInList[1] == -1 ){
                        continue;
                    }
                    if( foundUuidInList[0] == 0 || foundUuidInList[1] == 1 ){
                        return Boolean.TRUE;
                    }
                }
            }
            return Boolean.FALSE;
        }
        finally {
            foundedNodes = null;
        }
    }
    
    protected void doNotReleasedReadFromIndexWord(final ThWordRule outerRuleWord){
        ThIndexRule indexRule;
        ThIndexStatistic indexStatistic;
        ThWordRule funcRuleWord;
        AppFileStorageIndex currentIndexStorages;
        
        URI byPrefixGetUri;
        Map<String, String> byPrefixGetMap;
        try {
            funcRuleWord = (ThWordRule) outerRuleWord;
            
            indexRule = funcRuleWord.getIndexRule();
            indexStatistic = indexRule.getIndexStatistic();
            indexStatistic.updateDataStorages();
            currentIndexStorages = funcRuleWord.getIndexRule().getIndexState().currentIndexStorages();
            byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD);
            byPrefixGetMap = currentIndexStorages.byPrefixGetMap( 
                    AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD);
            try( FileSystem fsForWriteData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){
                do {
                    iterationBusData(funcRuleWord, fsForWriteData);
                } while( funcRuleWord.isRunnedWordWorkRouter() );
                //need read all jobs and send data to cache
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
            indexRule = null;
            indexStatistic = null;
            funcRuleWord = null;
            currentIndexStorages = null;
            
            byPrefixGetUri = null;
            byPrefixGetMap = null;
        }
    }
    private void iterationBusData(final ThWordRule outerRuleWord, final FileSystem fsForWriteData){
        ThWordRule funcRuleWord;
        FileSystem fsForWriteDataFunc;
        ThWordState wordState;
        ThWordStatusMainFlow wordStatusMainFlow;
        ThWordBusFlowEvent busJobForRead;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>>> pollAllBusData;
        try {
            funcRuleWord = (ThWordRule) outerRuleWord;
            fsForWriteDataFunc = (FileSystem) fsForWriteData;
            wordState = (ThWordState) funcRuleWord.getWordState();
            busJobForRead = (ThWordBusFlowEvent) wordState.getBusJobForWordRouterJobToReader();
            wordStatusMainFlow = (ThWordStatusMainFlow) outerRuleWord.getWordStatusMainFlow();
            //wordCache = wordStatusMainFlow.getWordCache();
            pollAllBusData = busJobForRead.pollAllBusData();
            /**
             * poll data  from cache by typeWord, hexTagName
             */
            /**
             * process returned from bus data
             * 1. validate UUID
             * 2. move cache and cacheReaded in state object
             * 3. code in main flow object procedure for return data from cache by UUID
             * 4. add state functional in main flow object
             * 5. code and add list objects structure and validate procedures
             * ConcurrentSkipListMap<Object, System.identityHashCode(Object x)>
             */
            walkReaderJobBus(funcRuleWord, fsForWriteDataFunc, pollAllBusData);
            busJobForRead.deleteBusPacketData(pollAllBusData);
        } finally {
            funcRuleWord = null;
            fsForWriteDataFunc = null;
            wordState = null;
            wordStatusMainFlow = null;
            busJobForRead = null;
            pollAllBusData = null;
        }
    }
    private void walkReaderJobBus(final ThWordRule outerRuleWordInputed, 
            final FileSystem fsForReadDataInputed, 
            ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>>> pollFromBusDataInputed){
        ThWordRule outerRuleWordFunc;
        FileSystem fsForReadDataFunc;
        ThWordStatusMainFlow wordStatusMainFlow;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>>> pollFromBusDataFunc;
        ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>> valueItemLvlTypeWord;
        ConcurrentSkipListMap<Integer, ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>>> valueItemLvlTagFileNameLetter;
        ConcurrentSkipListMap<String, LinkedTransferQueue<UUID>> valueItemLvlSubStrLength;
        LinkedTransferQueue<UUID> valueItemLvlTagFileName;
        UUID pollUuidAboutReadJob;
        Integer keyTypeWordList;
        String keyHexTagNameList;
        try {
            outerRuleWordFunc = (ThWordRule) outerRuleWordInputed;
            fsForReadDataFunc = (FileSystem) fsForReadDataInputed;
            pollFromBusDataFunc = pollFromBusDataInputed;
            for( Map.Entry<Integer, 
                    ConcurrentSkipListMap<String, 
                    ConcurrentSkipListMap<Integer, 
                    ConcurrentSkipListMap<String, 
                    LinkedTransferQueue<UUID>>>>> itemLvlTypeWord : pollFromBusDataFunc.entrySet() ){
                valueItemLvlTypeWord = itemLvlTypeWord.getValue();
                for( Map.Entry<String, 
                        ConcurrentSkipListMap<Integer, 
                        ConcurrentSkipListMap<String, 
                        LinkedTransferQueue<UUID>>>> itemLvlTagFileNameLetter : valueItemLvlTypeWord.entrySet() ){
                    valueItemLvlTagFileNameLetter = itemLvlTagFileNameLetter.getValue();
                    for( Map.Entry<Integer, 
                            ConcurrentSkipListMap<String, 
                            LinkedTransferQueue<UUID>>> itemLvlSubStrLength : valueItemLvlTagFileNameLetter.entrySet() ){
                        keyTypeWordList = itemLvlSubStrLength.getKey();
                        valueItemLvlSubStrLength = itemLvlSubStrLength.getValue();
                        for( Map.Entry<String, 
                                LinkedTransferQueue<UUID>> itemLvlTagFileName : valueItemLvlSubStrLength.entrySet() ){
                            keyHexTagNameList = itemLvlTagFileName.getKey();
                            valueItemLvlTagFileName = itemLvlTagFileName.getValue();
                            do {
                                pollUuidAboutReadJob = valueItemLvlTagFileName.poll();
                                if( pollUuidAboutReadJob != null ){
                                    readToMainFlowCacheData(outerRuleWordFunc, fsForReadDataFunc, keyTypeWordList, keyHexTagNameList, pollUuidAboutReadJob);
                                }
                            } while( !valueItemLvlTagFileName.isEmpty() );
                        }
                    }
                }
            }
        } finally {
            outerRuleWordFunc = null;
            fsForReadDataFunc = null;
            wordStatusMainFlow = null;
            pollFromBusDataFunc = null;
            valueItemLvlTypeWord = null;
            valueItemLvlTagFileNameLetter = null;
            valueItemLvlSubStrLength = null;
            valueItemLvlTagFileName = null;
            pollUuidAboutReadJob = null;
            keyTypeWordList = null;
            keyHexTagNameList = null;
        }
    }
    private void readToMainFlowCacheData(final ThWordRule outerRuleWordInputed, 
            final FileSystem fsForWriteDataInputed,
            final Integer typeWordInputed,
            final String hexTagNameInputed,
            final UUID readerBusUuidInputed){
        ThWordRule outerRuleWordFunc;
        FileSystem fsForWriteDataFunc;
        ThWordStatusMainFlow wordStatusMainFlowFunc;
        ThWordCacheSk wordCache;
        Integer typeWordFunc;
        String hexTagNameFunc;
        UUID readerBusUuidFunc;
        String storageDirectory;
        String currentFile;
        String newFile;
        Boolean workersIsMoveReady;
        String nameStorageDirectoryName;
        String namePrefixFileName;
        Path nowWritedFile;
        Path moveToFile;
        String localSrcFileName;
        String localDestFileName;
        Integer localSrcSize;
        Integer localDestSize;
        Integer datafsVolumeNumber;
        ConcurrentSkipListMap<UUID, TdataWord> pollTypeWordTagFileNameData;
        ConcurrentSkipListMap<UUID, TdataWord> writedFromCacheData;
        Map.Entry<UUID, TdataWord> pollFirstEntry;
        Boolean localIsLimitForWrite;
        Boolean localPrevDataWrited; 
        Boolean localPrevDataMoved;
        Path storageTypeWordWritedDirectory;
        ThWordCacheSk wordCacheReaded;
        Boolean isCachedReadedData;
        ThWordState wordState;
        ThWordBusFlowEvent wordFlowReaded;
        Path forReadFileName;
        ConcurrentSkipListMap<UUID, TdataWord> readedFromStorageData;
        try {
            outerRuleWordFunc = (ThWordRule) outerRuleWordInputed;
            wordStatusMainFlowFunc = (ThWordStatusMainFlow) outerRuleWordFunc.getWordStatusMainFlow();
            wordCache = wordStatusMainFlowFunc.getWordCache();
            
            fsForWriteDataFunc = (FileSystem) fsForWriteDataInputed;
            typeWordFunc = (Integer) typeWordInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            readerBusUuidFunc = (UUID) readerBusUuidInputed;
            localPrevDataWrited = Boolean.FALSE; 
            localPrevDataMoved = Boolean.FALSE;
            /**
             * 1. mainFlowParams get values for Uuid
             * 2. poll data from cache
             * 3. generate names, voul numbers, count records for write
             * 4. write data
             * 5. set flags in main flow
             * 6. change params in main flow
             */
            
            storageDirectory = wordStatusMainFlowFunc.getValueForValideUuuidByTypeWordHexTagNameNumberName(typeWordFunc, hexTagNameFunc, readerBusUuidFunc,  0);
            currentFile = wordStatusMainFlowFunc.getValueForValideUuuidByTypeWordHexTagNameNumberName(typeWordFunc, hexTagNameFunc, readerBusUuidFunc,  1);
            newFile = wordStatusMainFlowFunc.getValueForValideUuuidByTypeWordHexTagNameNumberName(typeWordFunc, hexTagNameFunc, readerBusUuidFunc,  2);
            if( currentFile.equalsIgnoreCase(newFile) ){
                
                forReadFileName = fsForWriteDataFunc.getPath(storageDirectory, currentFile);
                readedFromStorageData = new ConcurrentSkipListMap<UUID, TdataWord>();
                if( Files.exists(forReadFileName) ){
                    try(ObjectInputStream ois =
                        new ObjectInputStream(Files.newInputStream(forReadFileName)))
                    {
                        readedFromStorageData.putAll((ConcurrentSkipListMap<UUID, TdataWord>) ois.readObject());
                    } catch(ClassNotFoundException exCnf){
                        System.err.println(exCnf.getMessage());
                        exCnf.printStackTrace();
                    } catch(InvalidClassException exIce){
                        System.err.println(exIce.getMessage());
                        exIce.printStackTrace();
                    } catch(StreamCorruptedException exSce){
                        System.err.println(exSce.getMessage());
                        exSce.printStackTrace();
                    } catch(OptionalDataException exOde){
                        System.err.println(exOde.getMessage());
                        exOde.printStackTrace();
                    } catch(IOException exIo){
                        System.err.println(exIo.getMessage());
                        exIo.printStackTrace();
                    }
                    wordCacheReaded = wordStatusMainFlowFunc.getWordCacheReaded();
                    isCachedReadedData = Boolean.FALSE;
                    isCachedReadedData = wordCacheReaded.addAllDataIntoCache(readedFromStorageData);
                    wordStatusMainFlowFunc.changeParamForMainUuidByHexTagNameNumberWorkers(typeWordFunc, hexTagNameFunc, readerBusUuidFunc, 4, isCachedReadedData);
                    wordStatusMainFlowFunc.changeParamForMainUuidByHexTagNameNumberDataCache(typeWordFunc, hexTagNameFunc, readerBusUuidFunc, 1, readedFromStorageData.size());
                    wordState = outerRuleWordFunc.getWordState();
                    wordFlowReaded = wordState.getWordFlowReaded();
                    wordFlowReaded.addToListOfFlowEventUuidByTypeWordHexTagName(typeWordFunc, readerBusUuidFunc, hexTagNameFunc);
                    utilizeTdataWord(readedFromStorageData);
                }
            }
            
        } finally {
            outerRuleWordFunc = null;
            fsForWriteDataFunc = null;
            wordStatusMainFlowFunc = null;
            wordCache = null;
            typeWordFunc = null;
            hexTagNameFunc = null;
            readerBusUuidFunc = null;
            workersIsMoveReady = null;
            nameStorageDirectoryName = null;
            namePrefixFileName = null;
            nowWritedFile = null;
            moveToFile = null;
            localSrcFileName = null;
            localDestFileName = null;
            localSrcSize = null;
            localDestSize = null;
            datafsVolumeNumber = null;
            pollTypeWordTagFileNameData = null;
            writedFromCacheData = null;
            pollFirstEntry = null;
            localIsLimitForWrite = null;
            localPrevDataWrited = null; 
            localPrevDataMoved = null;
            storageTypeWordWritedDirectory = null;
            wordCacheReaded = null;
            isCachedReadedData = null;
            wordState = null;
            wordFlowReaded = null;
            forReadFileName = null;
            readedFromStorageData = null;
        }
    }
    private static ConcurrentSkipListMap<UUID, TdataWord> doUtilizationDataInitNew(ConcurrentSkipListMap<UUID, TdataWord> prevData){
        utilizeTdataWord(prevData);
        return new ConcurrentSkipListMap<UUID, TdataWord>();
    }
    private static void utilizeTdataWord(ConcurrentSkipListMap<UUID, TdataWord> forUtilizationData){
        UUID keyForDelete;
        TdataWord removedData;
        try {
            for( Map.Entry<UUID, TdataWord> deletingItem : forUtilizationData.entrySet() ){
                keyForDelete = deletingItem.getKey();
                removedData = forUtilizationData.remove(keyForDelete);
                removedData.dirListFile = null;
                removedData.hexSubString = null;
                removedData.hexSubStringHash = null;
                removedData.lengthSubString = null;
                removedData.positionSubString = null;
                removedData.randomUUID = null;
                removedData.recordHash = null;
                removedData.recordTime = null;
                removedData.recordUUID = null;
                removedData.strSubString = null;
                removedData.strSubStringHash = null;
                removedData.typeWord = null;
                removedData = null;
                keyForDelete = null;
            }
            forUtilizationData = null;
        } finally {
            keyForDelete = null;
            removedData = null;
        }
    }
    /**
     * 
     * @param namePrefixFileNameFromFlowInputed
     * @param recordsCountInputed
     * @param volumeNumberInputed
     * @return 
     */
    private static String fileNameBuilder(
            final String namePrefixFileNameFromFlowInputed,
            final Integer recordsCountInputed,
            final Integer volumeNumberInputed){
        String namePrefixFunc;
        Integer recordsCountFunc;
        Integer volumeNumberFunc;
        String buildedFileName;
        try {
            namePrefixFunc = new String(namePrefixFileNameFromFlowInputed);
            recordsCountFunc = (Integer) recordsCountInputed;
            volumeNumberFunc = (Integer) volumeNumberInputed;
            buildedFileName = new String()
                .concat(AppFileNamesConstants.SZFS_WORD_FILE_PREFIX)
                .concat(namePrefixFunc.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                .concat(String.valueOf(recordsCountFunc))
                .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                .concat(String.valueOf(volumeNumberFunc));
            return buildedFileName;
        } finally {
            namePrefixFunc = null;
            recordsCountFunc = null;
            volumeNumberFunc = null;
            buildedFileName = null;
        }
    }
    protected void doOldReadFromIndexWord(ThWordRule outerRuleWord){
        ThIndexRule indexRule;
        ThIndexStatistic indexStatistic;
        ThWordRule funcRuleWord;
        AppFileStorageIndex currentIndexStorages;
        try{
            long counIterations = 0;
            /**
             * @todo
             * Rule
             * Statistic for this index system
             */
            funcRuleWord = (ThWordRule) outerRuleWord;
            ThWordBusReadedFlow storageWordFlowReaded = funcRuleWord.getWordState().getWordFlowReaded();
            indexRule = funcRuleWord.getIndexRule();
            indexStatistic = indexRule.getIndexStatistic();
            indexStatistic.updateDataStorages();
            currentIndexStorages = funcRuleWord.getIndexRule().getIndexState().currentIndexStorages();
            URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD);
            Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(
                    AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD); 
            try( FileSystem fsForReadData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){
        
        
            do{ 
                try{
                    ThWordState wordState = (ThWordState) funcRuleWord.getWordState();
                    ThWordBusReader busJobForWordRouterJobToReader = 
                            (ThWordBusReader) wordState.getBusJobForWordRouterJobToReader();
                    
                    ConcurrentHashMap<Integer, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> 
                            maxUsedBusesSet = busJobForWordRouterJobToReader.getMaxUsedBusesSet();
                    
                    for(Map.Entry<Integer, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> busVal : maxUsedBusesSet.entrySet()){
                        for(Map.Entry<UUID, ConcurrentHashMap<String, String>> forReaderJobUUID : busVal.getValue().entrySet()){
                            /**
                             * @todo get PointFlow for UUID
                             * call write func
                             * get keySet this level list, remove it from list, insert
                             * into function for write removed data 
                             */
                            UUID mainFlowLabel = forReaderJobUUID.getKey();

                            for(Map.Entry<String, String> itemsTagNames : forReaderJobUUID.getValue().entrySet()){
                                /**
                                 * @todo to do function for return removed data from cache
                                 */
                                Integer typeWordBusNumber = busVal.getKey();
                                String hexTagName = itemsTagNames.getKey();
                                String subStringValue = itemsTagNames.getValue();

                                ThWordStatusMainFlow storageWordStatistic = (ThWordStatusMainFlow) outerRuleWord.getWordStatusMainFlow();

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
                                            if( !flowPointsUUID.containsKey("ThWordStatusDataFs".hashCode()) ){
                                                ConcurrentHashMap<Integer, UUID> removeBadDataFsUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadDataFsUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusDataFs for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }
                                            if( !flowPointsUUID.containsKey("ThWordStatusName".hashCode()) ){
                                                ConcurrentHashMap<Integer, UUID> removeBadNameUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadNameUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusName for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }
                                            if( !flowPointsUUID.containsKey("ThWordStatusActivity".hashCode()) ){
                                                ConcurrentHashMap<Integer, UUID> removeBadActivityUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadActivityUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusActivity for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }
                                            if( !flowPointsUUID.containsKey("ThWordStatusDataCache".hashCode()) ){
                                                ConcurrentHashMap<Integer, UUID> removeBadDataCacheUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadDataCacheUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusDataCache for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }
                                            if( !flowPointsUUID.containsKey("ThWordStatusWorkers".hashCode()) ){
                                                ConcurrentHashMap<Integer, UUID> removeBadWorkersUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadWorkersUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusWorkers for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }
                                            /**
                                             * validate values, read from file
                                             */
                                            ThWordCacheHa storageWordCache = 
                                                    (ThWordCacheHa) storageWordStatistic.getWordCache();
                                            ThWordStatusActivity storageWordStatusActivity = 
                                                    (ThWordStatusActivity) storageWordStatistic.getWordStatusActivity();

                                            UUID getKeyActivity = (UUID) flowPointsUUID.get("ThWordStatusActivity".hashCode());
                                            try{
                                                storageWordStatusActivity.validateCountParams(getKeyActivity);
                                            } catch (IllegalArgumentException exActiv) {
                                                System.err.println(exActiv.getMessage());
                                                ConcurrentHashMap<Integer, UUID> removeBadActivityUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadActivityUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusActivity for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }
                                            ThWordStatusDataCache storageWordStatusDataCache = 
                                                    (ThWordStatusDataCache) storageWordStatistic.getWordStatusDataCache();

                                            UUID getKeyDataCache = (UUID) flowPointsUUID.get("ThWordStatusDataCache".hashCode());
                                            try{
                                                storageWordStatusDataCache.validateCountParams(getKeyDataCache);
                                            } catch (IllegalArgumentException exDataCache) {
                                                System.err.println(exDataCache.getMessage());
                                                ConcurrentHashMap<Integer, UUID> removeBadDataCacheUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadDataCacheUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusDataCache for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }

                                            ThWordStatusDataFs storageWordStatusDataFs = 
                                                    (ThWordStatusDataFs) storageWordStatistic.getWordStatusDataFs();

                                            UUID getKeyDataFs = (UUID) flowPointsUUID.get("ThWordStatusDataFs".hashCode());
                                            try{
                                                storageWordStatusDataFs.validateCountParams(getKeyDataFs);
                                            } catch (IllegalArgumentException exDataFs) {
                                                System.err.println(exDataFs.getMessage());
                                                ConcurrentHashMap<Integer, UUID> removeBadDataFsUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadDataFsUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusDataFs for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }


                                            ThWordStatusName storageWordStatusName = 
                                                    (ThWordStatusName) storageWordStatistic.getWordStatusName();

                                            UUID getKeyName = (UUID) flowPointsUUID.get("ThWordStatusName".hashCode());
                                            try{
                                                storageWordStatusName.validateCountParams(getKeyName);
                                            } catch (IllegalArgumentException exName) {
                                                System.err.println(exName.getMessage());
                                                ConcurrentHashMap<Integer, UUID> removeBadNameUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadNameUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusName for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }


                                            ThWordStatusWorkers storageWordStatusWorkers = 
                                                    (ThWordStatusWorkers) storageWordStatistic.getWordStatusWorkers();

                                            UUID getKeyWorkers = (UUID) flowPointsUUID.get("ThWordStatusWorkers".hashCode());
                                            try{
                                                storageWordStatusWorkers.validateCountParams(getKeyWorkers);
                                            } catch (IllegalArgumentException exWorkers) {
                                                System.err.println(exWorkers.getMessage());
                                                ConcurrentHashMap<Integer, UUID> removeBadWorkersUUID = typeWordTagFileNameFlowUuids.remove(mainFlowLabel);
                                                removeBadWorkersUUID = null;
                                                System.err.println("-----------------"
                                                        + "||||||||||||||||||"
                                                        + "-----------------"
                                                        + "||||||||||||||||||"
                                                        + "----------------- data in flow key not valid, removed, "
                                                        + "reason not set ThWordStatusWorkers for UUID "
                                                        + mainFlowLabel.toString());
                                                continue;
                                            }

                                            ConcurrentHashMap<Integer, String> statusNameForKeyPointFlow = 
                                                    storageWordStatusName.getStatusNameForKeyPointFlow(getKeyName);
                                            ConcurrentHashMap<Integer, Integer> statusDataCacheForKeyPointFlow = 
                                                    storageWordStatusDataCache.getStatusDataCacheForKeyPointFlow(getKeyDataCache);
                                            ConcurrentHashMap<Integer, Boolean> statusWorkersForKeyPointFlow = 
                                                    storageWordStatusWorkers.getStatusWorkersForKeyPointFlow(getKeyWorkers);

                                            //isWriteProcess - 1640531930

                                            //Boolean getIsWriteInProcess = statusWorkersForKeyPointFlow.get(1640531930);
                                            //if( getIsWriteInProcess ){
                                            //    continue;
                                            //}

                                            //storageDirectoryName - 1962941405
                                            String storageDirectoryName = (String) statusNameForKeyPointFlow.get(1962941405);
                                            //currentFileName - 1517772480
                                            String currentFileName = (String) statusNameForKeyPointFlow.get(1517772480);
                                            //newFileName - 521024487
                                            String newFileName = statusNameForKeyPointFlow.get(521024487);

                                            if( !currentFileName.equalsIgnoreCase(newFileName) ){
                                                continue;
                                            }

                                            Path forReadFileName = fsForReadData.getPath(storageDirectoryName, currentFileName);

                                            ConcurrentHashMap<String, String> readedFormData = 
                                                    new ConcurrentHashMap<String, String>();

                                            if( Files.exists(forReadFileName) ){
                                                try(ObjectInputStream ois =
                                                    new ObjectInputStream(Files.newInputStream(forReadFileName)))
                                                {
                                                    readedFormData.putAll((ConcurrentHashMap<String, String>) ois.readObject());

                                                    ThWordCacheHaReaded thWordCacheReaded = storageWordStatistic.getWordCacheReaded();

                                                    Boolean isCachedReadedData = Boolean.FALSE;

                                                    isCachedReadedData = thWordCacheReaded.addAllDataIntoCacheReaded(
                                                            typeWordBusNumber, 
                                                            hexTagName, 
                                                            subStringValue, 
                                                            readedFormData);
                                                    //isCachedReadedData - -660426229
                                                    statusWorkersForKeyPointFlow.put(-660426229, isCachedReadedData);
                                                    //currentInCacheReaded - -835384455
                                                    statusDataCacheForKeyPointFlow.put(-835384455, readedFormData.size());
                                                    storageWordFlowReaded.addToListOfReadedFlowUuids(typeWordBusNumber, 
                                                            hexTagName, 
                                                            subStringValue, 
                                                            mainFlowLabel);
                                                    ConcurrentHashMap<String, String> remove = busVal.getValue().remove(mainFlowLabel);
                                                    remove = null;
                                                } catch(ClassNotFoundException exCnf){
                                                    System.err.println(exCnf.getMessage());
                                                    exCnf.printStackTrace();
                                                } catch(InvalidClassException exIce){
                                                    System.err.println(exIce.getMessage());
                                                    exIce.printStackTrace();
                                                } catch(StreamCorruptedException exSce){
                                                    System.err.println(exSce.getMessage());
                                                    exSce.printStackTrace();
                                                } catch(OptionalDataException exOde){
                                                    System.err.println(exOde.getMessage());
                                                    exOde.printStackTrace();
                                                } catch(IOException exIo){
                                                    System.err.println(exIo.getMessage());
                                                    exIo.printStackTrace();
                                                }
                                            }
                                        }
                                    }

                                }
                                try{
                                    /*checkDataForWrite(funcRuleWord, typeWordBusNumber, hexTagName, subStringValue);*/
                                } catch(IllegalArgumentException exArg) {
                                    System.err.println(exArg.getMessage());
                                    exArg.printStackTrace();
                                }
                            }
                        }
                }
                    /**
                     * for this typeWord, hexTagName, subString get from flowPoint need to read file name
                     * read it and insert data into cacheReaded, set flow point flag as readed, in write
                     * read flag readed and rewrite existing file, or write in new file name after that
                     * delete existing file
                     */
                    
                    /*ConcurrentSkipListMap<UUID, TdataWord> readedFormData =
                    new ConcurrentSkipListMap<UUID, TdataWord>();
                    if( Files.exists(nowWritedFile) ){
                    try(ObjectInputStream ois =
                    new ObjectInputStream(Files.newInputStream(nowWritedFile)))
                    {
                    readedFormData.putAll((ConcurrentSkipListMap<UUID, TdataWord>) ois.readObject());
                    } catch(Exception ex){
                    ex.printStackTrace();
                    }
                    try{
                    Path mvOldDir = fsForReadData.getPath(AppFileNamesConstants.DIR_INDEX_OLD_DATA);
                    if( Files.notExists(mvOldDir) ){
                    Files.createDirectories(mvOldDir);
                    }
                    Path forNewMove = fsForReadData.getPath(AppFileNamesConstants.DIR_INDEX_OLD_DATA
                    ,writerPath + "-"
                    + AppFileOperationsSimple.getNowTimeStringWithMS()
                    + "-"
                    + String.valueOf(counIterations));
                    Files.move(nowWritedFile, forNewMove);
                    } catch(UnsupportedOperationException ex){
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                    } catch(FileAlreadyExistsException ex){
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                    } catch(DirectoryNotEmptyException ex){
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                    } catch(AtomicMoveNotSupportedException ex){
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                    } catch(IOException ex){
                    System.err.println(ex.getMessage());
                    ex.printStackTrace();
                    }
                    }
                    ConcurrentSkipListMap<UUID, TdataWord> readyForWriteData =
                    new ConcurrentSkipListMap<UUID, TdataWord>();
                    readyForWriteData.putAll(readedFormData);
                    readyForWriteData.putAll(writerData);*/
        } finally {
                    
                }
            } while( outerRuleWord.isRunnedWordWorkRouter() );
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
}
