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
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsIdxStorageInit {
    
    protected static ThreadLocal<NcParamFs> getStorage(NcSwGUIComponentStatus lComp){
        ThreadLocal<NcParamFs> toReturn = openStorage();
        NcParamFs fileStorageParam = toReturn.get();
        
        ArrayList<String> arrStr = new ArrayList<String>();
        arrStr.add("NcParamFs:");
        arrStr.add(fileStorageParam.toString());
        
        arrStr.add("Storage:");
        arrStr.add(toReturn.toString());
        arrStr.add("isOpen: " + fileStorageParam.getIdxFs().isOpen());
        //arrStr.add("getDirDirList: " + fileStorageParam.getDirDirList().toString());
        
        //NcParamFs dataStorage = initStorageStructure(fileStorage);
        //toReturn.set(dataStorage);
        
        
        arrStr.add("attempt to open storage:");
        /*try(FileSystem fsZipIndexStorage = 
            FileSystems.newFileSystem(toReturn, getFsPropExist())){
            arrStr.add("isOpen: " + fsZipIndexStorage.isOpen());
            Iterable<Path> rootDirectories = fsZipIndexStorage.getRootDirectories();
            
            Iterator<Path> iterator = rootDirectories.iterator();
            while (iterator.hasNext()) {
                Path next = iterator.next();
                arrStr.add(next.toString());

            }
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorageInit.class.getCanonicalName(), ex);
        }*/
        
        NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
        return toReturn;
    }
    
    private static ThreadLocal<NcParamFs> openStorage(){
        Path pathIndexFile = buildPathToFileOfIdxStorage();
        Map<String, String> fsProperties = getFsPropExist();
        ThreadLocal<NcParamFs> toReturn = new ThreadLocal<>();
        if( !NcFsIdxOperationFiles.existAndHasAccessRWNotLink(pathIndexFile) ){
            fsProperties = getFsPropCreate();
        }
        URI uriZipIndexStorage = URI.create("jar:" + pathIndexFile.toUri());
        try(FileSystem fsZipIndexStorage = 
            FileSystems.newFileSystem(uriZipIndexStorage, fsProperties)){
            
            NcParamFs dataStorage = initStorageStructure(fsZipIndexStorage);
            
            toReturn.set(dataStorage);
            return toReturn;
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorageInit.class.getCanonicalName(), ex);
        }
        String strMsg = "Imposible to create file for index Storage, see log";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR_CRITICAL.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    }
    
    

    protected static NcParamFs initStorageStructure(FileSystem inFS){
        
        Path dirDirList = inFS.getPath(NcStrFileDir.DIR_DIR_LIST.getStr());
        
        NcFsIdxOperationDirs.create(dirDirList);
        
        Path dirFileExist = inFS.getPath(NcStrFileDir.DIR_FILE_EXIST.getStr());
        NcFsIdxOperationDirs.create(dirFileExist);
        
        Path dirFileHash = inFS.getPath(NcStrFileDir.DIR_FILE_HASH.getStr());
        NcFsIdxOperationDirs.create(dirFileHash);
        
        Path dirFileList = inFS.getPath(NcStrFileDir.DIR_FILE_LIST.getStr());
        NcFsIdxOperationDirs.create(dirFileList);
        
        Path dirFileType = inFS.getPath(NcStrFileDir.DIR_FILE_TYPE.getStr());
        NcFsIdxOperationDirs.create(dirFileType);
        
        Path dirJournal = inFS.getPath(NcStrFileDir.DIR_JOURNAL.getStr());
        NcFsIdxOperationDirs.create(dirJournal);
        
        Path dirLongWordList = inFS.getPath(NcStrFileDir.DIR_LONG_WORD_LIST.getStr());
        NcFsIdxOperationDirs.create(dirLongWordList);
        
        Path dirLongWordData = inFS.getPath(NcStrFileDir.DIR_LONG_WORD_DATA.getStr());
        NcFsIdxOperationDirs.create(dirLongWordData);
        
        Path dirStorageWord = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr());
        NcFsIdxOperationDirs.create(dirStorageWord);
        
        Path dirStorageWordAbc = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLABC.getName());
        NcFsIdxOperationDirs.create(dirStorageWordAbc);
        
        Path dirStorageWordNum = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLNUM.getName());
        NcFsIdxOperationDirs.create(dirStorageWordNum);
        
        Path dirStorageWordRabc = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLRABC.getName());
        NcFsIdxOperationDirs.create(dirStorageWordRabc);
        
        Path dirStorageWordSpace = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLSPACE.getName());
        NcFsIdxOperationDirs.create(dirStorageWordSpace);
        
        Path dirStorageWordSym = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLSYM.getName());
        NcFsIdxOperationDirs.create(dirStorageWordSym);
        
        Path dirTmp = inFS.getPath(NcStrFileDir.DIR_TMP.getStr());
        NcFsIdxOperationDirs.create(dirTmp);
        
        Path dirWord = inFS.getPath(NcStrFileDir.DIR_WORD.getStr());
        NcFsIdxOperationDirs.create(dirWord);
        
        NcParamFs forReturn = new NcParamFs(inFS, 
            dirDirList, 
            dirFileExist, 
            dirFileHash, 
            dirFileList, 
            dirFileType, 
            dirJournal, 
            dirLongWordList, 
            dirLongWordData, 
            dirStorageWord, 
            dirStorageWordAbc, 
            dirStorageWordNum, 
            dirStorageWordRabc, 
            dirStorageWordSpace, 
            dirStorageWordSym, 
            dirTmp, 
            dirWord);
        return forReturn;
    }
    
    protected static Map<String, String> getFsPropCreate(){
        Map<String, String> zipfsPropeties = new HashMap<>();
        zipfsPropeties.put("create","true");
        zipfsPropeties.put("encoding","UTF-8");
        
        return zipfsPropeties;
    }
    protected static Map<String, String> getFsPropExist(){
        Map<String, String> zipfsPropeties = new HashMap<>();
        zipfsPropeties.put("create","false");
        zipfsPropeties.put("encoding","UTF-8");
        return zipfsPropeties;
    }

    protected static Path buildPathToFileOfIdxStorage(){
        String fileStorageName = NcStrFileDir.FILE_INDEX_CONTAINS.getStr();
        Path workPath = getWorkDirByRWAccess();
        Path workFilePath = Paths.get(workPath.toString(), fileStorageName);
        workFilePath = workFilePath.normalize().toAbsolutePath();
        return workFilePath;
    }
    private static Path getWorkDirByRWAccess() throws RuntimeException{
        try {
            Path userHomePath = NcFsDefaults.getUserHomePath();
            if( NcFsIdxOperationDirs.existAndHasAccessRWNotLink(userHomePath) ){
                return userHomePath;
            }
        } catch (IOException ex) {
            NcAppHelper.logException(
                    NcFsIdxStorageInit.class.getCanonicalName(), ex);
        }
        try {
            Path appPath = NcFsDefaults.getAppPath();
            if( NcFsIdxOperationDirs.existAndHasAccessRWNotLink(appPath) ){
                return appPath;
            }
        } catch (IOException ex) {
            NcAppHelper.logException(
                    NcFsIdxStorageInit.class.getCanonicalName(), ex);
        }
        String strMsg = "User home and application directories is wrong, "
                + "for application work need read, write access"
                + "in not sybolink links dirictories";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR_CRITICAL.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    }
}
