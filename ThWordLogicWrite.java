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
import java.nio.file.DirectoryNotEmptyException;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordLogicWrite {
    protected void doWriteToIndexWord(final ThWordRule outerRuleWord){
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
        }
    }
    private void iterationBusData(final ThWordRule outerRuleWord, final FileSystem fsForWriteData){
        ThWordRule funcRuleWord;
        FileSystem fsForWriteDataFunc;
        ThWordState wordState;
        ThWordStatusMainFlow wordStatusMainFlow;
        ThWordBusFlowEvent busJobForWrite;
        ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>>> pollAllBusData;
        try {
            funcRuleWord = (ThWordRule) outerRuleWord;
            fsForWriteDataFunc = (FileSystem) fsForWriteData;
            wordState = (ThWordState) funcRuleWord.getWordState();
            busJobForWrite = (ThWordBusFlowEvent) wordState.getBusJobForWordRouterJobToWriter();
            wordStatusMainFlow = (ThWordStatusMainFlow) outerRuleWord.getWordStatusMainFlow();
            //wordCache = wordStatusMainFlow.getWordCache();
            pollAllBusData = busJobForWrite.pollAllBusData();
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
            walkWriterJobBus(funcRuleWord, fsForWriteDataFunc, pollAllBusData);
            busJobForWrite.deleteBusPacketData(pollAllBusData);


            
        } finally {
            funcRuleWord = null;
            fsForWriteDataFunc = null;
            wordState = null;
            wordStatusMainFlow = null;
            busJobForWrite = null;
            pollAllBusData = null;
        }
    }
    private void walkWriterJobBus(final ThWordRule outerRuleWordInputed, 
            final FileSystem fsForWriteDataInputed, 
            ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                ConcurrentSkipListMap<Integer, 
                ConcurrentSkipListMap<String, 
                LinkedTransferQueue<UUID>>>>> pollFromBusDataInputed){
        ThWordRule outerRuleWordFunc;
        FileSystem fsForWriteDataFunc;
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
        UUID pollUuidAboutWriteJob;
        try {
            outerRuleWordFunc = (ThWordRule) outerRuleWordInputed;
            fsForWriteDataFunc = (FileSystem) fsForWriteDataInputed;
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
                        valueItemLvlSubStrLength = itemLvlSubStrLength.getValue();
                        for( Map.Entry<String, 
                                LinkedTransferQueue<UUID>> itemLvlTagFileName : valueItemLvlSubStrLength.entrySet() ){
                            valueItemLvlTagFileName = itemLvlTagFileName.getValue();
                            do {
                                pollUuidAboutWriteJob = valueItemLvlTagFileName.poll();
                                if( pollUuidAboutWriteJob != null ){
                                    getMainFlowCacheData(outerRuleWordFunc, fsForWriteDataFunc, pollUuidAboutWriteJob);
                                }
                            } while( !valueItemLvlTagFileName.isEmpty() );
                        }
                    }
                }
            }
        } finally {
            outerRuleWordFunc = null;
            fsForWriteDataFunc = null;
            wordStatusMainFlow = null;
            pollFromBusDataFunc = null;
            valueItemLvlTypeWord = null;
            valueItemLvlTagFileNameLetter = null;
            valueItemLvlSubStrLength = null;
            valueItemLvlTagFileName = null;
            pollUuidAboutWriteJob = null;
        }
    }
    private void getMainFlowCacheData(final ThWordRule outerRuleWordInputed, 
            final FileSystem fsForWriteDataInputed,
            final Integer typeWordInputed,
            final String hexTagNameInputed,
            final UUID writerBusUuidInputed){
        ThWordRule outerRuleWordFunc;
        FileSystem fsForWriteDataFunc;
        ThWordStatusMainFlow wordStatusMainFlowFunc;
        ThWordCacheSk wordCache;
        Integer typeWordFunc;
        String hexTagNameFunc;
        UUID writerBusUuidFunc;
        Boolean workersIsWriteProcess;
        Boolean workersIsMoveReady;
        String nameStorageDirectoryName;
        String namePrefixFileName;
        Path nowWritedFile;
        Path moveToFile;
        String localSrcFileName;
        String localDestFileName;
        Integer localSrcSize;
        Integer localDestSize;
        Integer localWritedSize;
        Integer datafsVolumeNumber;
        ConcurrentSkipListMap<UUID, TdataWord> pollTypeWordTagFileNameData;
        ConcurrentSkipListMap<UUID, TdataWord> writedFromCacheData;
        Map.Entry<UUID, TdataWord> pollFirstEntry;
        Boolean localIsDataToVol;
        Boolean localIsLimitForWrite;
        try {
            outerRuleWordFunc = (ThWordRule) outerRuleWordInputed;
            wordStatusMainFlowFunc = (ThWordStatusMainFlow) outerRuleWordFunc.getWordStatusMainFlow();
            wordCache = wordStatusMainFlowFunc.getWordCache();
            
            fsForWriteDataFunc = (FileSystem) fsForWriteDataInputed;
            typeWordFunc = (Integer) typeWordInputed;
            hexTagNameFunc = (String) hexTagNameInputed;
            writerBusUuidFunc = (UUID) writerBusUuidInputed;
            /**
             * 1. mainFlowParams get values for Uuid
             * 2. poll data from cache
             * 3. generate names, voul numbers, count records for write
             * 4. write data
             * 5. set flags in main flow
             * 6. change params in main flow
             */
            
            workersIsWriteProcess = wordStatusMainFlowFunc.getValueForValideUuuidByTypeWordHexTagNameNumberWorkers(typeWordFunc, hexTagNameFunc, writerBusUuidFunc,  0);
            if( !workersIsWriteProcess ){
                pollTypeWordTagFileNameData = null;
                try{
                    pollTypeWordTagFileNameData = wordCache.pollDataByTypeWordHexTagName(typeWordFunc, hexTagNameFunc);
                } catch (NullPointerException exRetNull) {
                    System.err.println(exRetNull.getMessage());
                    exRetNull.printStackTrace();
                } catch (IllegalArgumentException exRetNull) {
                    System.err.println(exRetNull.getMessage());
                    exRetNull.printStackTrace();
                }
                if( pollTypeWordTagFileNameData != null ){
                    if( !pollTypeWordTagFileNameData.isEmpty() ){
                        nameStorageDirectoryName = wordStatusMainFlowFunc.getValueForValideUuuidByTypeWordHexTagNameNumberName(typeWordFunc, hexTagNameFunc, writerBusUuidFunc,  0);
                        // into helper class function
                        Path storageTypeWordWritedDirectory = fsForWriteDataFunc.getPath(nameStorageDirectoryName);
                        if( Files.notExists(storageTypeWordWritedDirectory, LinkOption.NOFOLLOW_LINKS) ){
                            try{
                                Files.createDirectories(storageTypeWordWritedDirectory);
                            } catch (FileAlreadyExistsException exAlreadyExist) {
                                exAlreadyExist.printStackTrace();
                            } catch (SecurityException exSecurity) {
                                exSecurity.printStackTrace();
                            } catch (UnsupportedOperationException exUnSupp) {
                                exUnSupp.printStackTrace();
                            } catch (IOException exIO) {
                                exIO.printStackTrace();
                            }
                        }
                        // end into
                        localSrcSize = 0;
                        datafsVolumeNumber = wordStatusMainFlowFunc.getValueForValideUuuidByTypeWordHexTagNameNumberDataFs(typeWordFunc, hexTagNameFunc, writerBusUuidFunc,  1);
                        namePrefixFileName = wordStatusMainFlowFunc.getValueForValideUuuidByTypeWordHexTagNameNumberName(typeWordFunc, hexTagNameFunc, writerBusUuidFunc,  4);
                        
                
                
                        localIsDataToVol = Boolean.FALSE;
                        localDestSize = 0;
                        do {
                            if( pollTypeWordTagFileNameData.size() > AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT){
                                writedFromCacheData = new ConcurrentSkipListMap<UUID, TdataWord>();
                                do {
                                    pollFirstEntry = pollTypeWordTagFileNameData.pollFirstEntry();
                                    if( pollFirstEntry != null ){
                                        writedFromCacheData.put(pollFirstEntry.getKey(), pollFirstEntry.getValue());
                                    }
                                    localDestSize = writedFromCacheData.size();
                                    localIsLimitForWrite = localDestSize == AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT;
                                    if( localIsLimitForWrite ){
                                        localSrcFileName = new String()
                                            .concat(AppFileNamesConstants.SZFS_WORD_FILE_PREFIX)
                                            .concat(namePrefixFileName.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                                            .concat(String.valueOf(localSrcSize))
                                            .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                                            .concat(String.valueOf(datafsVolumeNumber));
                                        //---section for next code release --- write to
                                        
                                        nowWritedFile = fsForWriteDataFunc.getPath(storageTypeWordWritedDirectory.toString(), localSrcFileName);

                                        try( ObjectOutputStream oos = 
                                            new ObjectOutputStream(Files.newOutputStream(nowWritedFile)) )
                                        {
                                            oos.writeObject(writedFromCacheData);
                                            System.out.println(ThWordLogicWrite.class.getCanonicalName() 
                                                    + " => => =>                                             => => => " 
                                                    + nowWritedFile.toUri().toString() 
                                                    + " writed size " + localDestSize);
                                            wordStatusMainFlowFunc.changeParamForMainUuidByHexTagNameNumberWorkers(typeWordFunc, hexTagNameFunc, writerBusUuidFunc, 0, Boolean.TRUE);
                                        } catch(Exception ex){
                                            ex.printStackTrace();
                                        }
                                        //isMoveFileReady - -1884096596
                                        
                                        workersIsMoveReady = wordStatusMainFlowFunc.getValueForValideUuuidByTypeWordHexTagNameNumberWorkers(typeWordFunc, hexTagNameFunc, writerBusUuidFunc, 7);
                                        if( workersIsMoveReady ){
                                            continue;
                                        }
                                        /**
                                         * build names for write and move operations
                                         */
                                        localDestFileName = new String()
                                            .concat(AppFileNamesConstants.SZFS_WORD_FILE_PREFIX)
                                            .concat(namePrefixFileName.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                                            .concat(String.valueOf(localDestSize))
                                            .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                                            .concat(String.valueOf(datafsVolumeNumber));
                                        //---section for next code release --- move to
                                        
                                        moveToFile = fsForWriteDataFunc.getPath(storageTypeWordWritedDirectory.toString(), localDestFileName);
                                        try{
                                            Files.move(nowWritedFile, moveToFile, StandardCopyOption.ATOMIC_MOVE);
                                            wordStatusMainFlowFunc.changeParamForMainUuidByHexTagNameNumberWorkers(typeWordFunc, hexTagNameFunc, writerBusUuidFunc, 7, Boolean.TRUE);
                                            wordStatusMainFlowFunc.changeParamForMainUuidByHexTagNameNumberWorkers(typeWordFunc, hexTagNameFunc, writerBusUuidFunc, 2, Boolean.TRUE);
                                            wordStatusMainFlowFunc.changeParamForMainUuidByHexTagNameNumberName(typeWordFunc, hexTagNameFunc, writerBusUuidFunc, 1, localDestFileName);
                                            //after delete oldFile
                                            ConcurrentHashMap<String, String> remove = busVal.getValue().remove(mainFlowLabel);
                                            remove = null;
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
                                        } catch(IOException exIO) {
                                            System.err.println(exIO.getMessage());
                                            exIO.printStackTrace();
                                        }

                                        datafsVolumeNumber++;
                                        //delete data from to write writedFromCacheData
                                        writedFromCacheData = new ConcurrentSkipListMap<UUID, TdataWord>();
                                    }
                                    localIsDataToVol = pollTypeWordTagFileNameData.size() > AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT;

                                } while( localIsDataToVol );
                            }
                            localDestFileName = new String()
                                .concat(AppFileNamesConstants.SZFS_WORD_FILE_PREFIX)
                                .concat(namePrefixFileName.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                                .concat(String.valueOf(localDestSize))
                                .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                                .concat(String.valueOf(datafsVolumeNumber));
                            //---section for next code release --- write to
                            Path nowWritedFile = fsForWriteDataFunc.getPath(storageTypeWordWritedDirectory.toString(), localDestFileName);

                            try(ObjectOutputStream oos = 
                                new ObjectOutputStream(Files.newOutputStream(nowWritedFile)))
                            {
                                oos.writeObject(pollTypeWordTagFileNameData);
                                System.out.println(ThWordLogicWrite.class.getCanonicalName() 
                                        + " => => =>                                             => => => " 
                                        + nowWritedFile.toUri().toString() 
                                        + " writed size " + pollTypeWordTagFileNameData.size());
                                //isWriteInProcess - 1640531930
                                statusWorkersForKeyPointFlow.put(1640531930, Boolean.TRUE);
                                wordStatusMainFlowFunc.changeValueForValideUuuidByTypeWordHexTagNameNumberWorkers(typeWordFunc, hexTagNameFunc, writerBusUuidFunc,  0);
                            } catch(Exception ex){
                                ex.printStackTrace();
                            }
                            //isMoveFileReady - -1884096596
                            Boolean getIsMoveReady = statusWorkersForKeyPointFlow.get(-1884096596);
                            if( getIsMoveReady ){
                                continue;
                            }
                            //---section for next code release --- move to
                            Path moveToFile = fsForWriteData.getPath(storageDirectoryName, newFileName);
                            try{
                                Files.move(nowWritedFile, moveToFile, StandardCopyOption.ATOMIC_MOVE);
                                statusWorkersForKeyPointFlow.put(-1884096596, Boolean.TRUE);
                                statusWorkersForKeyPointFlow.put(-83825824, Boolean.TRUE);
                                statusNameForKeyPointFlow.put(1517772480, newFileName);
                                //after delete oldFile
                                ConcurrentHashMap<String, String> remove = busVal.getValue().remove(mainFlowLabel);
                                remove = null;
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
                            //---section for next code release --- delete data from to write pollTypeWordTagFileNameData
                            //---section for next code release --- set params to flow
                        } while ( !pollTypeWordTagFileNameData.isEmpty() );
                    }
                }
            }
            
        } finally {
            
        }
    }
    protected void doOldWriteToIndexWord(final ThWordRule outerRuleWord){
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
            
            indexRule = funcRuleWord.getIndexRule();
            indexStatistic = indexRule.getIndexStatistic();
            indexStatistic.updateDataStorages();
            currentIndexStorages = funcRuleWord.getIndexRule().getIndexState().currentIndexStorages();
            URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD);
            Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(
                    AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD); 
            try( FileSystem fsForWriteData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){
        
        
            do { 
                try {

                /**
                 * @todo release some bus for each index system, 
                 * got jobs and read write it in by queue system, workers whrere
                 * opened storages
                 * 
                 * save data in limited file packets
                 */
                ThWordState wordState = (ThWordState) funcRuleWord.getWordState();
                ThWordBusWriter busJobForWrite = wordState.getBusJobForWordRouterJobToWriter();


                ConcurrentHashMap<Integer, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> maxUsedBusesSet = busJobForWrite.getMaxUsedBusesSet();

                for( Map.Entry<Integer, ConcurrentHashMap<UUID, ConcurrentHashMap<String, String>>> busVal : maxUsedBusesSet.entrySet() ){
                    for( Map.Entry<UUID, ConcurrentHashMap<String, String>> forWriterJobUUID : busVal.getValue().entrySet() ){
                        /**
                         * @todo get PointFlow for UUID
                         * call write func
                         * get keySet this level list, remove it from list, insert
                         * into function for write removed data 
                         */
                        UUID mainFlowLabel = forWriterJobUUID.getKey();

                        for( Map.Entry<String, String> itemsTagNames : forWriterJobUUID.getValue().entrySet() ){
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
                                         * validate values, write to file
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
                                        
                                        ConcurrentHashMap<Integer, String> statusNameForKeyPointFlow = storageWordStatusName.getStatusNameForKeyPointFlow(getKeyName);
                                        ConcurrentHashMap<Integer, Boolean> statusWorkersForKeyPointFlow = storageWordStatusWorkers.getStatusWorkersForKeyPointFlow(getKeyWorkers);
                                        //isWriteProcess - 1640531930
                                        Boolean getIsWriteInProcess = statusWorkersForKeyPointFlow.get(1640531930);
                                        if( getIsWriteInProcess ){
                                            continue;
                                        }
                                        
                                        
                                        
                                        //storageDirectoryName - 1962941405
                                        String storageDirectoryName = statusNameForKeyPointFlow.get(1962941405);
                                        
                                        Path storageTypeWordWritedFile = fsForWriteData.getPath(storageDirectoryName);
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
                                        
                                        //set into flow
                                        ConcurrentHashMap<Integer, Integer> statusDataFsForKeyPointFlow = storageWordStatusDataFs.getStatusDataFsForKeyPointFlow(getKeyDataFs);
                                        Integer volNum = statusDataFsForKeyPointFlow.get(-1832815869);
                                        Integer sizeDataSrc = 0;
                                        Integer sizeDataDest = 0;
                                        
                                        
                                        //flowFileNamePrefix - -980152217
                                        String prefixFileName = statusNameForKeyPointFlow.get(-980152217);
                                        
                                        //currentFileName - 1517772480
                                        //String currentFileName = statusNameForKeyPointFlow.get(1517772480);
                                        String currentFileName = new String()
                                            .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                                            .concat(prefixFileName.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                                            .concat(String.valueOf(sizeDataSrc))
                                            .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                                            .concat(String.valueOf(volNum));
                                        //newFileName - 521024487
                                        //String newFileName = statusNameForKeyPointFlow.get(521024487);
                                        
                                        
                                        
                                        
                                        
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
                                        Boolean isDataToVol = Boolean.FALSE;
                                        if( pollTypeWordTagFileNameData.size() > AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT ){
                                            isDataToVol = Boolean.TRUE;
                                        }
                                        sizeDataDest = pollTypeWordTagFileNameData.size();
                                        String newFileName = new String()
                                            .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                                            .concat(prefixFileName.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                                            .concat(String.valueOf(sizeDataDest))
                                            .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                                            .concat(String.valueOf(volNum));
                                        /**
                                         * if size > limit pack data, write, and write new vol
                                         * with size - limit data
                                         * 
                                         * in statistic for remove main uuid need delete for all uuids...
                                         */
                                        
                                        /**
                                         * need additional flags 
                                         * isErrorOnWrite, 
                                         * isErrorOnMove, 
                                         * isNullOnDataInCache,
                                         * isErrorOnDataInCache,
                                         */
                                        if( !isDataToVol ){
                                            Path nowWritedFile = fsForWriteData.getPath(storageDirectoryName, currentFileName);

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

                                            Path moveToFile = fsForWriteData.getPath(storageDirectoryName, newFileName);
                                            try{
                                                Files.move(nowWritedFile, moveToFile, StandardCopyOption.ATOMIC_MOVE);
                                                statusWorkersForKeyPointFlow.put(-1884096596, Boolean.TRUE);
                                                statusWorkersForKeyPointFlow.put(-83825824, Boolean.TRUE);
                                                statusNameForKeyPointFlow.put(1517772480, newFileName);
                                                //after delete oldFile
                                                ConcurrentHashMap<String, String> remove = busVal.getValue().remove(mainFlowLabel);
                                                remove = null;
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
                                        } else {
                                            do{
                                                ConcurrentHashMap<String, String> packetForWriteData = new ConcurrentHashMap<String, String>();
                                                for( Map.Entry<String, String> valForVolItem : pollTypeWordTagFileNameData.entrySet() ){
                                                    
                                                    String keyItem = (String) valForVolItem.getKey();
                                                    if( keyItem != null ){
                                                        String valItem = (String) pollTypeWordTagFileNameData.remove(keyItem);

                                                        packetForWriteData.put(keyItem, valItem);
                                                    
                                                        sizeDataDest = packetForWriteData.size();
                                                        if( ( sizeDataDest == AppConstants.STORAGE_WORD_RECORDS_COUNT_LIMIT ) || ( pollTypeWordTagFileNameData.isEmpty() ) ){
                                                            currentFileName = new String()
                                                                .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                                                                .concat(prefixFileName.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                                                                .concat(String.valueOf(0))
                                                                .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                                                                .concat(String.valueOf(volNum));
                                                            //newFileName - 521024487
                                                            //String newFileName = statusNameForKeyPointFlow.get(521024487);
                                                            newFileName = new String()
                                                                .concat(AppFileNamesConstants.SZFS_STORAGE_WORD_FILE_PREFIX)
                                                                .concat(prefixFileName.concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR))
                                                                .concat(String.valueOf(sizeDataDest))
                                                                .concat(AppFileNamesConstants.FILE_DIR_PART_SEPARATOR)
                                                                .concat(String.valueOf(volNum));

                                                            Path nowWritedFile = fsForWriteData.getPath(storageDirectoryName, currentFileName);

                                                            try( ObjectOutputStream oos = 
                                                                new ObjectOutputStream(Files.newOutputStream(nowWritedFile)) )
                                                            {
                                                                oos.writeObject(packetForWriteData);
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

                                                            Path moveToFile = fsForWriteData.getPath(storageDirectoryName, newFileName);
                                                            try{
                                                                Files.move(nowWritedFile, moveToFile, StandardCopyOption.ATOMIC_MOVE);
                                                                statusWorkersForKeyPointFlow.put(-1884096596, Boolean.TRUE);
                                                                statusWorkersForKeyPointFlow.put(-83825824, Boolean.TRUE);
                                                                statusNameForKeyPointFlow.put(521024487, newFileName);
                                                                statusNameForKeyPointFlow.put(1517772480, newFileName);
                                                                
                                                                
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
                                                            volNum++;
                                                            statusDataFsForKeyPointFlow.put(-1832815869, volNum);
                                                            packetForWriteData = new ConcurrentHashMap<String, String>();
                                                        }
                                                    }
                                                }
                                            }while( !pollTypeWordTagFileNameData.isEmpty() );
                                        }
                                        
                                        
                                        
                                        Boolean getIsNeedDelete = statusWorkersForKeyPointFlow.get(-1172779240);
                                        if( getIsNeedDelete ){
                                            String getOldForDeleteFileName = statusNameForKeyPointFlow.get(2045325664);
                                            Path deleteFile = fsForWriteData.getPath(getOldForDeleteFileName);
                                            
                                            try{
                                                Files.deleteIfExists(deleteFile);
                                            } catch (DirectoryNotEmptyException exNotEmptyDir) {
                                                exNotEmptyDir.printStackTrace();
                                            } catch (SecurityException exSecurity) {
                                                exSecurity.printStackTrace();
                                            } catch (IOException exInOut) {
                                                exInOut.printStackTrace();
                                            }
                                            Boolean removeAllFlowStatusByUUID = storageWordStatistic.removeAllFlowStatusByUUID(mainFlowLabel);
                                            removeAllFlowStatusByUUID = null;
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

                } finally {
                    
                }
            } while( outerRuleWord.isRunnedWordWorkRouter() );
            /**
             * @todo write all cache data 
             */
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
