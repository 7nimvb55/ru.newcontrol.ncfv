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

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThWordLogicStorageWalker {
    // jobWalkerStorageType - scanNotLimited - hashCode
    protected void doScanListNotLimitedFiles(final ThWordRule ruleWordWriteWork,
                            final ConcurrentHashMap<Integer, Integer> currentWalkerStorageJob){
        ThIndexRule indexRule = ruleWordWriteWork.getIndexRule();
        ThIndexState indexState = indexRule.getIndexState();
        AppFileStorageIndex currentIndexStorages = indexState.currentIndexStorages();
        URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
        Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
    }
                   
    // jobWalkerStorageType - scanLimited - hashCode
    protected void doScanListLimitedFiles(final ThWordRule ruleWordWriteWork,
                            final ConcurrentHashMap<Integer, Integer> currentWalkerStorageJob){
        ThIndexRule indexRule = ruleWordWriteWork.getIndexRule();
        ThIndexState indexState = indexRule.getIndexState();
        AppFileStorageIndex currentIndexStorages = indexState.currentIndexStorages();
        URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
        Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
    }
    // jobWalkerStorageType - scanAllFiles - hashCode
    protected void doScanListAllFiles(final ThWordRule ruleWordWriteWork,
                            final ConcurrentHashMap<Integer, Integer> currentWalkerStorageJob){
        ThIndexRule indexRule = ruleWordWriteWork.getIndexRule();
        ThIndexState indexState = indexRule.getIndexState();
        AppFileStorageIndex currentIndexStorages = indexState.currentIndexStorages();
        URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
        Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
    }
    // jobWalkerStorageType - moveFilesDirectories - hashCode
    /*protected void doMoveFilesDirectories(final ThWordRule ruleWordWriteWork,
                            final ConcurrentHashMap<Integer, Integer> currentWalkerStorageJob){
        
    }*/
    // jobWalkerStorageType - createDirectoryTypeWord - hashCode
    protected void doCreateDirectoryTypeWord(final ThWordRule ruleWordWriteWork,
                            final ConcurrentHashMap<Integer, Integer> currentWalkerStorageJob){
        ThIndexRule indexRule = ruleWordWriteWork.getIndexRule();
        ThIndexState indexState = indexRule.getIndexState();
        AppFileStorageIndex currentIndexStorages = indexState.currentIndexStorages();
        URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
        Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
        Integer getTypeWordDirectory = currentWalkerStorageJob.get(4);
        
        
        //AppFileOperationsSimple.getOrCreateAnySubDir(AppFileNamesConstants.DIR_IDX_ROOT, String.valueOf(getTypeWordDirectory));
        
    }
    /*
    protected ArrayBlockingQueue<Path> getLogHtmlListTableFiles(){
        if( !isSetForLogHtmlListTableFiles() ){
            Path dirForRead = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR);
            ArrayList<Path> filesByMaskFromDir = AppFileOperationsSimple.getFilesByMaskFromDir(
                dirForRead,
                "{" + AppFileNamesConstants.LOG_HTML_TABLE_PREFIX + "}*");
            this.readedFilesListInLogHtmlByTableMask = new ArrayBlockingQueue<Path>(filesByMaskFromDir.size());
            for( Path fileForRead : filesByMaskFromDir ){
                this.readedFilesListInLogHtmlByTableMask.add(fileForRead);
            }
            setTrueForLogHtmlListTableFiles();
        }
        return this.readedFilesListInLogHtmlByTableMask;
    }
    */
}
