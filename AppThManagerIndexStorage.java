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
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppThManagerIndexStorage implements Runnable {
    private AppThWorkDirListRule innerRuleForDirListWorkers;

    public AppThManagerIndexStorage(AppThWorkDirListRule ruleForDirListWorkers) {
        super();
        this.innerRuleForDirListWorkers = ruleForDirListWorkers;
    }
    
    @Override
    public void run() {
        Path pathIndexFile = NcFsIdxStorageInit.buildPathToFileOfIdxStorage();
        Map<String, String> fsProperties = NcFsIdxStorageInit.getFsPropExist();
        
        Boolean existFSfile = NcFsIdxOperationFiles.existAndHasAccessRWNotLink(pathIndexFile);
        
        if( !existFSfile ){
            fsProperties = NcFsIdxStorageInit.getFsPropCreate();
        }
        
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
            
            

        } catch (IOException ex) {
            ex.printStackTrace();
            ifException = Boolean.TRUE;
        } catch (Exception ex){
            ex.printStackTrace();
            ifException = Boolean.TRUE;
        }
    }
}
