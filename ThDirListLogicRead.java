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

/**
 *
 * @author wladimirowichbiaran
 */
public class ThDirListLogicRead {
    protected void doIndexStorage(final ThDirListRule outerRuleDirListReadWork){
        AdilRule adilRule = outerRuleDirListReadWork.getIndexRule().getAdilRule();
        AdilState adilState = adilRule.getAdilState();
        String msgToLog = AdilConstants.INFO_LOGIC_POSITION
                + AdilConstants.CANONICALNAME
                + ThDirListLogicRead.class.getCanonicalName()
                + AdilConstants.METHOD
                + "doIndexStorage()";
        adilState.putLogLineByProcessNumberMsg(3, 
                msgToLog
                + AdilConstants.START);
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
            //currentIndexStorages.updateMapForStorages();
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
                    /**
                     * @todo recode to do while ...
                     * read one job
                     * add readerJob for Word
                     * goto sleep, while job not ready for Done
                     */
                    if( !jobForRead.isBlankObject() ){

                        Path filePath = ThDirListFileSystemHelper.getFilePath(fsForReadData, jobForRead.getReadedPath());
                        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readDataFromFile = ThDirListFileSystemHelper.readDataFromFile(filePath);
                        jobForRead.putReadedData(readDataFromFile);
                        jobForRead.setTrueReaderJobDone();
                        busJobForSendToIndexWord.addReaderJob(jobForRead);
                        /*System.out.println("idx: " 
                                + countJobs 
                                + " file " 
                                + jobForRead.getReadedPath().toString() 
                                + " read records count " 
                                + jobForRead.getReadedDataSize().toString()
                                + " jobDone " + jobForRead.isReaderJobDone().toString()
                        );*/
                        
                        adilState.putLogLineByProcessNumberMsg(3, 
                            msgToLog
                            + AdilConstants.STATE
                            + AdilConstants.VARNAME
                            + "countJobs"
                            + AdilConstants.VARVAL
                            + String.valueOf(countJobs)
                            + AdilConstants.VARNAME
                            + "jobForRead.getReadedPath()"
                            + AdilConstants.VARVAL
                            + jobForRead.getReadedPath().toString()
                            + AdilConstants.VARNAME
                            + "jobForRead.getReadedDataSize()"
                            + AdilConstants.VARVAL
                            + String.valueOf(jobForRead.getReadedDataSize())
                            + AdilConstants.VARNAME
                            + "jobForRead.isReaderJobDone().toString()"
                            + AdilConstants.VARVAL
                            + String.valueOf(jobForRead.isReaderJobDone())
                        );
                        countJobs++;
                    }
                    jobForRead = busReadedJob.getJobForRead();
                }
                adilState.putLogLineByProcessNumberMsg(3, 
                            msgToLog
                            + AdilConstants.STATE
                            + AdilConstants.VARNAME
                            + "countJobs"
                            + AdilConstants.VARVAL
                            + String.valueOf(countJobs));
                
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
        adilState.putLogLineByProcessNumberMsg(3, 
                msgToLog
                + AdilConstants.FINISH);
    }
    
}
