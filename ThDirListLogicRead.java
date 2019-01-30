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
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThDirListLogicRead {
    protected void doIndexStorage(final ThDirListRule outerRuleDirListReadWork){
        /**
         * @todo need optimized that part of code, if have a jobForReadList, then
         * index storage exist, check for exist and open it
         * exceptions to logger append records need too
         */
        System.out.println(ThDirListLogicRead.class.getCanonicalName() + " =====>");
        ThDirListBusReaded busJobForSendToIndexWord = outerRuleDirListReadWork.getDirListState().getBusJobForSendToIndexWord();
        ThDirListBusReaded busReadedJob = outerRuleDirListReadWork.getDirListState().getBusJobForRead();
        ThDirListStateJobReader jobForRead = busReadedJob.getJobForRead();
        if( !jobForRead.isBlankObject() ){
 
            ThIndexRule indexRule = outerRuleDirListReadWork.getIndexRule();
            ThIndexState indexState = indexRule.getIndexState();
            AppFileStorageIndex currentIndexStorages = indexState.currentIndexStorages();
            currentIndexStorages.updateMapForStorages();
            /**
             * currentIndexStorages.updateMapForStorages();// - for update Storages info
             */
            System.out.println(ThDirListLogicRead.class.getCanonicalName() + " preOpen storage =====>");
            URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_DIR_LIST);
            Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(AppFileNamesConstants.FILE_INDEX_PREFIX_DIR_LIST);
            try( FileSystem fsForReadData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){    
                System.out.println(ThDirListLogicRead.class.getCanonicalName() + " open storage " + fsForReadData.toString());
                int countJobs = 0;
                while( !busReadedJob.isJobQueueEmpty() ){

                    if( !jobForRead.isBlankObject() ){

                        Path filePath = ThDirListFileSystemHelper.getFilePath(fsForReadData, jobForRead.getReadedPath());
                        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readDataFromFile = ThDirListFileSystemHelper.readDataFromFile(filePath);
                        jobForRead.putReadedData(readDataFromFile);
                        jobForRead.setTrueReaderJobDone();
                        busJobForSendToIndexWord.addReaderJob(jobForRead);
                        System.out.println("idx: " 
                                + countJobs 
                                + " file " 
                                + jobForRead.getReadedPath().toString() 
                                + " read records count " 
                                + jobForRead.getReadedDataSize().toString()
                                + " jobDone " + jobForRead.isReaderJobDone().toString()
                        );
                        countJobs++;
                        
                    }
                    jobForRead = busReadedJob.getJobForRead();
                }
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
        }
    }
    
}
