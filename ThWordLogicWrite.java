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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URI;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Write data into files in word index storage
 * @author wladimirowichbiaran
 */
public class ThWordLogicWrite {
    protected void doWriteJobFromBusForWord(ThWordRule ruleWordOuter){
     
        long counIterations = 0;
        ThIndexRule indexRule = ruleWordOuter.getIndexRule();
        ThIndexStatistic indexStatistic = indexRule.getIndexStatistic();
        indexStatistic.updateDataStorages();
        do{ 
            AppFileStorageIndex currentIndexStorages = ruleWordOuter.getIndexRule().getIndexState().currentIndexStorages();
            /**
             * @todo release some bus for each index system, 
             * got jobs and read write it in by queue system, workers whrere
             * opened storages
             * 
             * save data in limited file packets
             */
            URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
            Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(
                    AppFileNamesConstants.FILE_INDEX_PREFIX_WORD); 
            
            try( FileSystem fsForReadData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){
            ThWordState wordState = ruleWordOuter.getWordState();
            ThWordBusWrited busJobForWrite = wordState.getBusJobForWordWrite();
            if(!busJobForWrite.isJobQueueEmpty()){
                ThWordStateJobWriter jobForWrite = busJobForWrite.getJobForWrite();
                
                    do{
                        if( !jobForWrite.isBlankObject() && !jobForWrite.isWritedDataEmpty() ){
                            currentIndexStorages.updateMapForStorages();
                            String writerPath = jobForWrite.getFileNameForWrite();
                            
                            ConcurrentSkipListMap<UUID, TdataWord> writerData = 
                                    new ConcurrentSkipListMap<UUID, TdataWord>();
                            writerData.putAll(jobForWrite.getWriterData());
                            writerPath = indexStatistic.createNewNameForWriteWithAllAddRecords(
                                    AppFileNamesConstants.FILE_INDEX_PREFIX_WORD, 
                                    writerPath, 
                                    writerData);
                            
                            Path nowWritedFile = fsForReadData.getPath(writerPath);
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
                
            }
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
        } while( ruleWordOuter.isRunnedWordWorkBuild() );
        
        
    }
}
