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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsIdxStorage {
    protected static void putDataToIndex(){
        
    }
    protected static void getDataFromIndex(){
        ThreadLocal<FileSystem> openFsIdx = openStorage();
    }
    protected static void getIndexStorage(){
    }
    private static ThreadLocal<FileSystem> openStorage(){
        ThreadLocal<FileSystem> fsIdx = new ThreadLocal<>();
        Path pathIndexFile = getRealPath(getIndexFileStorageFile());
        
        URI uriZipIndexStorage = URI.create("jar:" + pathIndexFile.toUri());
        try(FileSystem fsZipIndexStorage = 
                FileSystems.newFileSystem(uriZipIndexStorage, getFsPropExist())){
            fsIdx.set(fsZipIndexStorage);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        return fsIdx;
    }
    private static Path createStorage(Path pathIndexFile){
        URI uriZipIndexStorage = URI.create("jar:" + pathIndexFile.toUri());
        try(FileSystem fsZipIndexStorage = 
                FileSystems.newFileSystem(uriZipIndexStorage, getFsPropCreate())){
            //Path pdfe = fsZipIndexStorage.getPath(NcStrFileDir.DIR_FILE_EXIST.getStr());
            //Files.createDirectory(pdfe);
            createDirs(fsZipIndexStorage);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        return pathIndexFile;
    }
    private static void createDirs(FileSystem inFS){
        Path pdfe = inFS.getPath(NcStrFileDir.DIR_DIR_LIST.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_FILE_EXIST.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_FILE_HASH.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_FILE_LIST.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_FILE_TYPE.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_JOURNAL.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_LONG_WORD_LIST.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_LONG_WORD_DATA.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLABC.getName());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLNUM.getName());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLRABC.getName());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLSPACE.getName());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_STORAGE_WORD.getStr(),
                NcTypeOfWord.NCLVLSYM.getName());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        
        pdfe = inFS.getPath(NcStrFileDir.DIR_WORD.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        pdfe = inFS.getPath(NcStrFileDir.DIR_TMP.getStr());
        try {
            Files.createDirectory(pdfe);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        
    }
    private static Map<String, String> getFsPropCreate(){
        Map<String, String> zipfsPropeties = new HashMap<>();
        zipfsPropeties.put("create","true");
        zipfsPropeties.put("encoding","UTF-8");
        return zipfsPropeties;
    }
    private static Map<String, String> getFsPropExist(){
        Map<String, String> zipfsPropeties = new HashMap<>();
        zipfsPropeties.put("create","false");
        zipfsPropeties.put("encoding","UTF-8");
        return zipfsPropeties;
    }
    private static Path getRealPath(Path forTestPath){
        Path strRealPath;
        File fileStorage = forTestPath.toFile();
        if( !NcIdxFileManager.fileExistRWAccessChecker(fileStorage) ){
            createStorage(forTestPath);
        }
        try {
            strRealPath = forTestPath.toRealPath();
        } catch (IOException ex) {
            NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
                + NcStrServiceMsg.ERROR_FILE_NOT_CANONICAL_PATH.getStr()
                + forTestPath.toString());
            NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
            
            strRealPath = Paths.get(NcIdxFileManager.getErrorForFileOperation().getAbsolutePath());
        }
        
        return strRealPath;
    }
    private static Path getIndexFileStorageFile(){
        String fileStorageName = NcStrFileDir.FILE_INDEX_CONTAINS.getStr();
        String usrHomePath = System.getProperty("user.home");
         
        Path parentForFS = Paths.get(usrHomePath);
        
        File fileIndexDir = parentForFS.toFile();
        if( !NcIdxFileManager.dirExistRWAccessChecker(fileIndexDir) ){
            NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
                + NcStrLogMsgText.NOT_EXIST_OR_READ_WRITE_PERMISSIONS_FOR_FILE.getStr()
                + usrHomePath);
            String appPath = System.getProperty("java.class.path");
            parentForFS = Paths.get(appPath);
            fileIndexDir = parentForFS.toFile();
            if( !NcIdxFileManager.dirExistRWAccessChecker(fileIndexDir) ){
                NcAppHelper.appExitWithMessageFSAccess(usrHomePath);
            }
        }
        String strCanPathFromFile = NcIdxFileManager.getStrCanPathFromFile(fileIndexDir);
        return Paths.get(strCanPathFromFile, fileStorageName);
    }
}
