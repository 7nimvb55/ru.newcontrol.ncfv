/*
 * Copyright 2018 wladimirowichbiaran.
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
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppThManagerIndexStorage extends Thread {
    private AppThWorkDirListRule innerRuleForDirListWorkers;

    public AppThManagerIndexStorage(AppThWorkDirListRule ruleForDirListWorkers) {
        super(ruleForDirListWorkers.getThreadGroupWorkerDirList(), ruleForDirListWorkers.getNameIndexStorage());
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    
    @Override
    public void run() {
        Path pathIndexFile = NcFsIdxStorageInit.buildPathToFileOfIdxStorage();
        Map<String, String> fsProperties = NcFsIdxStorageInit.getFsPropExist();
        //System.out.println("\n\n\n file storage path: " + pathIndexFile.toString());
        Boolean existFSfile = NcFsIdxOperationFiles.existAndHasAccessRWNotLink(pathIndexFile);
        //System.out.println("NcFsIdxOperationFiles.existAndHasAccessRWNotLink(): " + existFSfile.toString());
        if( !existFSfile ){
            fsProperties = NcFsIdxStorageInit.getFsPropCreate();
        }
        /*for (Map.Entry<String, String> entry : fsProperties.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Val: " + entry.getValue());
        }*/
        
        Boolean ifException = Boolean.FALSE;
        
        URI uriZipIndexStorage = URI.create("jar:file:" + pathIndexFile.toUri().getPath());
        try(FileSystem fsZipIndexStorage = 
            FileSystems.newFileSystem(uriZipIndexStorage, fsProperties)){
            
            innerRuleForDirListWorkers.setFsZipIndexStorage(fsZipIndexStorage);
            AppThWorkDirListState workDirListState = innerRuleForDirListWorkers.getWorkDirListState();
            int countForSetWaitReader = 0;
            while( !innerRuleForDirListWorkers.isDirListReaderSetted() ){
                countForSetWaitReader++;
            }
            int countForSetWaitTacker = 0;
            while( !innerRuleForDirListWorkers.isDirListTackerSetted() ){
                countForSetWaitTacker++;
            }
            int countForSetWaitPacker = 0;
            while( !innerRuleForDirListWorkers.isDirListPackerSetted()){
                countForSetWaitPacker++;
            }
            int countForSetWaitWriter = 0;
            while( !innerRuleForDirListWorkers.isDirListWriterSetted() ){
                countForSetWaitWriter++;
            }
            
            workDirListState.startDirlistReader();
            
            
            workDirListState.joinDirlistReader();
            workDirListState.joinDirlistTacker();
            workDirListState.joinDirlistPacker();
            workDirListState.joinDirlistWriter();
            
            //@todo thread finished before writer start and life
            //NcParamFs dataStorage = NcFsIdxStorageInit.initStorageStructure(fsZipIndexStorage);
            //innerRuleForDirListWorkers.getNameDirListWriter().wait();
            //see example for pipes, open read, write
        } catch (IOException ex) {
            ex.printStackTrace();
            /*NcAppHelper.logException(NcThMifWriterDirList.class.getCanonicalName(), ex);
            String strMsg = "Imposible to create file for index Storage, see log";
            NcAppHelper.outMessage(
                NcStrLogMsgField.ERROR_CRITICAL.getStr()
                + strMsg
            );*/
            ifException = Boolean.TRUE;
        } catch (Exception ex){
            ex.printStackTrace();
            /*NcAppHelper.logException(NcThMifWriterDirList.class.getCanonicalName(), ex);
            String strMsg = "Imposible for exec operation in the index Storage, see log"
                    + NcStrLogMsgField.EXCEPTION_MSG.getStr() + ex.getMessage();
            NcAppHelper.outMessage(
                NcStrLogMsgField.ERROR_CRITICAL.getStr()
                + strMsg
            );*/
            ifException = Boolean.TRUE;
        }
        
    }
    
}
