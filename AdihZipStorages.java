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
import java.net.URISyntaxException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdihZipStorages {
    private final Long timeCreation;
    private final UUID objectLabel;
    private final ConcurrentSkipListMap<Integer, URI> storagesUriList;
    private final ConcurrentSkipListMap<Integer, Path> zipStoreFileList;
    private final ConcurrentSkipListMap<Integer, FileSystem> openedZipStoreList;
    AdihZipStorages(final ThIndexRule ruleIndexOuter){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.storagesUriList = new ConcurrentSkipListMap<Integer, URI>();
        this.zipStoreFileList = new ConcurrentSkipListMap<Integer, Path>();
        this.openedZipStoreList = new ConcurrentSkipListMap<Integer, FileSystem>();
    }

    /**
     * <ul>
     * <li>   0 -   UserHome
     * <li>   1 -   ClassPathApplicationDirectory
     * <li>   2 -   ncidxfvSubDirIndex
     * 
     * <li>   3 -   di-indexDirList
     * <li>   4 -   t-indexTempData
     * <li>   5 -   j-indexJournal
     * <li>   6 -   fl-indexFileList
     *              
     * <li>   7 -   ft-indexFileType
     * <li>   8 -   fh-indexFileHash
     * <li>   9 -   fx-indexFileExist
     * 
     * <li>  10 -   w-indexWord
     * <li>  11 -   sw-indexStorageWord
     * <li>  12 -   lw-indexLongWordList
     * <li>  13 -   ln-indexLongWordData
     * </ul>
     * This list of parameters changed in {@link ru.newcontrol.ncfv.AdihHelper#getParamNames AdihHelper.getParamNames()}
     * Return code of parameter by his number, calculeted from some fileds
     * @param numParam
     * @return hashCode for Parameter by his number
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds or not natural number <code>numParam &lt 0 (Zero)</code>
     * @see ru.newcontrol.ncfv.AdihHelper#getParamNames AdihHelper.getParamNames()
     */
    private Integer getParamCodeByNumber(int numParam){
        String[] paramNames;
        try {
            paramNames = AdihHelper.getParamNames();
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + " 0 (zero) > , need for return " + numParam + "count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusError.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + "count parameters: " 
                                + paramNames.length 
                                + ", need for return " + numParam);
            } 
            int codeForParameter = paramNames[numParam]
                    .concat(String.valueOf(this.timeCreation))
                    .concat(this.objectLabel.toString()).hashCode();
            return codeForParameter;
        } finally {
            paramNames = null;
        }
    }
    /**
     * Count records (array.length) returned from {@link #getParamNames }
     * @return 
     */
    private Integer getParamCount(){
        String[] paramNames;
        try {
            paramNames = AdihHelper.getParamNames();
            return paramNames.length;
        } finally {
            paramNames = null;
        }
    }
    /**
     * 
     * @param numParam
     * @return name of param by his number
     * @throws IllegalArgumentException when inputed number of parameter
     * out of bounds or not natural number <code>numParam &lt 0 (Zero)</code>
     * @see ru.newcontrol.ncfv.AdihHelper#getParamNames AdihHelper.getParamNames()
     */
    private String getParamNameByNumber(int numParam){
        String[] paramNames;
        String paramName;
        try {
            paramNames = AdihHelper.getParamNames();
            if( numParam < 0 ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + " 0 (zero) > , need for return " + numParam + "count parameters: " 
                                + paramNames.length);
            }
            if( numParam > (paramNames.length - 1) ){
                throw new IllegalArgumentException(ThWordStatusMainFlow.class.getCanonicalName() 
                                + " parameters of flow statusMainFlow in StorageWord is not valid, "
                                + "count parameters: " 
                                + paramNames.length 
                                + ", need for return " + numParam);
            } 
            paramName = new String(paramNames[numParam]);
            return paramName;
        } finally {
            paramNames = null;
            paramName = null;
        }
    }
    private void fillAllListFromFoundedStorages(){
        Boolean isFoundOnFileSystem = Boolean.FALSE;
        if( !isFoundOnFileSystem ){
            createStoragesList();
        } else {
            
        }
    }
    /**
     * generate list of typed bus for temporary cache lines for log
     */
    private void createStoragesList(){
        Path toAddIntoList = null;
        Path subDirIndex = null;
        URI uriZipIndexStorage;
        String prefixStorageByNumber = new String();
        Integer paramCodeByNumber;
        Integer countParamsDataFsForSet;
        Integer idx;
        try {
            countParamsDataFsForSet = getParamCount();
            
            prefixStorageByNumber = AdihHelper.getPrefixStorageByNumber(0);
            paramCodeByNumber = getParamCodeByNumber(0);
            toAddIntoList = AdihFileOperations.getUserHomeCheckedPath();
            this.zipStoreFileList.put(paramCodeByNumber, toAddIntoList);
            
            prefixStorageByNumber = AdihHelper.getPrefixStorageByNumber(1);
            paramCodeByNumber = getParamCodeByNumber(1);
            toAddIntoList = AdihFileOperations.getAppCheckedPath();
            this.zipStoreFileList.put(paramCodeByNumber, toAddIntoList);
            
            prefixStorageByNumber = AdihHelper.getPrefixStorageByNumber(2);
            paramCodeByNumber = getParamCodeByNumber(2);
            toAddIntoList = createIfNotExistSubDirIndex();
            subDirIndex = toAddIntoList;
            this.zipStoreFileList.put(paramCodeByNumber, toAddIntoList);
            
            for(idx = 3; idx < countParamsDataFsForSet; idx++ ){
                prefixStorageByNumber = AdihHelper.getPrefixStorageByNumber(idx);
                toAddIntoList = buildZipStoragesPath(subDirIndex, prefixStorageByNumber);
                uriZipIndexStorage = URI.create(AppFileNamesConstants.PREFIX_TO_URI_STORAGES + toAddIntoList.toUri());
                paramCodeByNumber = getParamCodeByNumber(idx);
                this.zipStoreFileList.put(paramCodeByNumber, toAddIntoList);
                this.storagesUriList.put(paramCodeByNumber, uriZipIndexStorage);
            }
        } finally {
            toAddIntoList = null;
            subDirIndex = null;
            uriZipIndexStorage = null;
            AdihUtilization.utilizeStringValues(new String[]{prefixStorageByNumber});
            prefixStorageByNumber = null;
            paramCodeByNumber = null;
            countParamsDataFsForSet = null;
            idx = null;
        }
    }
    private void fillOpenStoreList(){
        Path storageFile;
        URI valueForStorage;
        Boolean pathIsFile;
        FileSystem storageFileSystem;
        try {
            for( Map.Entry<Integer, URI> itemOfURI : this.storagesUriList.entrySet() ){
                storageFile = this.zipStoreFileList.get(itemOfURI.getKey());
                if( storageFile != null ){
                    valueForStorage = itemOfURI.getValue();
                    storageFileSystem = AdihFileOperations.getStorageFileSystem(storageFile, valueForStorage);
                    if( storageFileSystem != null ){
                        this.openedZipStoreList.put(itemOfURI.getKey(), storageFileSystem);
                    }
                }
            }
        } finally {
            storageFile = null;
            valueForStorage = null;
            pathIsFile = null;
            storageFileSystem = null;
        }
    }
    /**
     * 
     */
    private void closeOpenedAndUtilizeValuesFromList(){
        FileSystem removedStorageItem;
        Boolean closeOpenedStorage;
        try {
            for( Map.Entry<Integer, FileSystem> itemOfURI : this.openedZipStoreList.entrySet() ){
                removedStorageItem = itemOfURI.getValue();
                closeOpenedStorage = AdihFileOperations.closeOpenedStorage(removedStorageItem);
            }
        } finally {
            removedStorageItem = null;
            closeOpenedStorage = null;
        }
    }
    /**
     * 
     */
    protected void utilizeAllLists(){
        Integer key;
        FileSystem removeZipStorageItem;
        URI removeUriStorageItem;
        Path removePathStorageItem;
        try {
            closeOpenedAndUtilizeValuesFromList();
            for( Map.Entry<Integer, FileSystem> itemOfFs : this.openedZipStoreList.entrySet() ){
                key = itemOfFs.getKey();
                removeZipStorageItem = this.openedZipStoreList.remove(key);
                removeZipStorageItem = null;
                key = null;
            }
            this.openedZipStoreList.clear();
            for( Map.Entry<Integer, URI> itemOfURI : this.storagesUriList.entrySet() ){
                key = itemOfURI.getKey();
                removeUriStorageItem = this.storagesUriList.remove(key);
                removeUriStorageItem = null;
                key = null;
            }
            this.storagesUriList.clear();
            for( Map.Entry<Integer, Path> itemOfPath : this.zipStoreFileList.entrySet() ){
                key = itemOfPath.getKey();
                removePathStorageItem = this.zipStoreFileList.remove(key);
                removeZipStorageItem = null;
                key = null;
            }
            this.zipStoreFileList.clear();
        } finally {
            removePathStorageItem = null;
            removeZipStorageItem = null;
            removeUriStorageItem = null;
            key = null;
        }
    }
    /**
     * 
     * @param parenForStorage
     * @param prefixStorage
     * @return 
     */
    private static Path buildZipStoragesPath(Path parenForStorage, String prefixStorage){
        Path forReturnStorage = null;
        Path searchinIndexDirStorageByPrefix = null;
        String parentDir = new String();
        String buildedName = new String();
        try{
            //search in default file system existing files by mask, create path and return
            searchinIndexDirStorageByPrefix = AdihFileOperations.searchinIndexDirStorageByPrefix(parenForStorage, prefixStorage);
            if( searchinIndexDirStorageByPrefix != null ){
                return searchinIndexDirStorageByPrefix;
            }
            parentDir = parenForStorage.toString();
            buildedName = prefixStorage
                    .concat(AdihGetvalues.getNowTimeStringMillisFsNames())
                    .concat(AppFileNamesConstants.FILE_INDEX_EXT);
            try {
                    forReturnStorage = Paths.get(parentDir, buildedName);
            } catch(InvalidPathException exInvPath) {
                System.err.println(AdihZipStorages.class.getCanonicalName() 
                        + "[ERROR] Index SubDirectory build not complete path is " 
                        + parentDir + " and builded name " + buildedName
                        + AdilConstants.EXCEPTION_MSG 
                        + exInvPath.getMessage()
                );
                exInvPath.printStackTrace();
                System.exit(0);
            }
            return forReturnStorage;
        } finally {
            forReturnStorage = null;
            AdihUtilization.utilizeStringValues(new String[]{parentDir, buildedName});
            searchinIndexDirStorageByPrefix = null;
        }
    }
    /**
     * <code>/...pathToUserHome/ncidxfv/</code>
     * @return 
     */
    private static Path createIfNotExistSubDirIndex(){
        Boolean createDirIfNotExist = null;
        Path userHomeSubDirIndex = null;
        Path userHomeCheckedPath = AdihFileOperations.getUserHomeCheckedPath();
        String userHomeStr = userHomeCheckedPath.toString();
        String prefixStorageByNumber = AdihHelper.getPrefixStorageByNumber(2);
        try{
            try {
                    userHomeSubDirIndex = Paths.get(userHomeStr,prefixStorageByNumber);
            } catch(InvalidPathException exInvPath) {
                System.err.println(AdihZipStorages.class.getCanonicalName() 
                        + "[ERROR] Index SubDirectory build not complete path is " 
                        + userHomeStr + " and subDir " + prefixStorageByNumber
                        + AdilConstants.EXCEPTION_MSG 
                        + exInvPath.getMessage()
                );
                exInvPath.printStackTrace();
                System.exit(0);
            }

            createDirIfNotExist = AdihFileOperations.createDirIfNotExist(userHomeSubDirIndex);
            if( !createDirIfNotExist ){
                System.err.println(AdihZipStorages.class.getCanonicalName() 
                            + "[ERROR] Index SubDirectory build not complete path is " 
                            + userHomeSubDirIndex.toString() + " "
                            + AdihFileOperations.class.getCanonicalName() 
                            + ".createDirIfNotExist() return "
                            + String.valueOf(createDirIfNotExist)
                    );
                    System.exit(0);
            }
            return userHomeSubDirIndex;
        } finally {
            createDirIfNotExist = null;
            userHomeSubDirIndex = null;
            userHomeCheckedPath = null;
            AdihUtilization.utilizeStringValues(new String[]{userHomeStr, prefixStorageByNumber});
        }
    }
    
    private static Path getPathStorageFile(String prefixStorageName){
        return null;
    }
    private static URI getUriStorageByNumber(Path storageFile){
        URI uri;
        try {
            uri = new URI("file:///foo/bar");
        } catch (URISyntaxException ex) {
            
        }
        return null;
    }
    protected static void updateStorageList(){
        
    }
    private static Boolean checkStorageZipFile(){
        return Boolean.TRUE;
    }
    private static void readZipFileList(){
        
    }
    private static void closeOpenedStores(){
        URI byPrefixGetUri = Paths.get("/", "file.zip").toUri();
        FileSystem fileSystem = FileSystems.getFileSystem(byPrefixGetUri);
        FileSystemProvider provider = fileSystem.provider();
        Boolean open = fileSystem.isOpen();
        Iterable<FileStore> fileStores = fileSystem.getFileStores();
        for(FileStore itemFs : fileStores ){

        }
        try {
            fileSystem.close();
        } catch (IOException exIo) {
            System.err.println(ThStorageWordLogicWrite.class.getCanonicalName() 
                    + " error for open storage for index, reason " 
                    + exIo.getMessage());
            exIo.printStackTrace();
        }
    }
}
