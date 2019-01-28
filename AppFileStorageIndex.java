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
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppFileStorageIndex {
    private ConcurrentSkipListMap<String, Map<String, String>> storagesMap;
    private ArrayList<String> listOfPrefix;
    public AppFileStorageIndex() {
        this.storagesMap = AppFileStorageIndex.getStoragesMap();
        this.listOfPrefix = AppFileStorageIndex.getListOfPrefix();
    }
    protected URI byPrefixGetUri(String prefixOfStorage){
        URI uriFromMapByPrefix = AppFileStorageIndex.getUriFromMapByPrefix(this.storagesMap, prefixOfStorage);
        return uriFromMapByPrefix;
    }
    protected Map<String, String> byPrefixGetMap(String prefixOfStorage){
        Map<String, String> paramMapByPrefix = AppFileStorageIndex.getParamMapByPrefix(this.storagesMap, prefixOfStorage);
        return paramMapByPrefix;
    }
    protected void updateMapForStorages(){
        this.storagesMap = AppFileStorageIndex.getStoragesMap();
        this.listOfPrefix = AppFileStorageIndex.getListOfPrefix();
    }
    protected ArrayList<String> listOfPrefixes(){
        return this.listOfPrefix;
    }
    
    protected static Path getIndexFolder(){
        Path appRWEDCheckedPath = AppFileOperationsSimple.getUserHomeRWEDCheckedPath();
        Path orCreateAnySubDir = AppFileOperationsSimple.getOrCreateAnySubDir(appRWEDCheckedPath, 
                AppFileNamesConstants.DIR_IDX);
        return orCreateAnySubDir;
    }
    /**
     * get 
     * @param fsPrefixConstant
     * @return 
     */
    protected static Map<String, String> buildForStorageByPrefix(String fsPrefixConstant){
        Path indexFolder = AppFileStorageIndex.getIndexFolder();
        String prefixWithTime = fsPrefixConstant 
                + AppFileOperationsSimple.getNowTimeStringWithMS() 
                + AppFileNamesConstants.FILE_INDEX_EXT;
        Path getStoragePath = Paths.get(indexFolder.toString(), prefixWithTime);
        Map<String, String> fsPropExistOrCreate = AppFileStorageIndex.getFsPropExist();
        if( !AppFileOperationsSimple.existAndHasAccessRWNotLink(getStoragePath) ){
            fsPropExistOrCreate = AppFileStorageIndex.getFsPropCreate();
        }
        URI uriZipIndexStorage = URI.create("jar:" + getStoragePath.toUri());
        fsPropExistOrCreate.put(AppFileNamesConstants.FILE_INDEX_KEY_MAP_URI, uriZipIndexStorage.toString());
        return fsPropExistOrCreate;
    }
    protected static Map<String, String> getForStorageByPrefix(Path outerStoragePath){
        Map<String, String> fsPropExistOrCreate = AppFileStorageIndex.getFsPropExist();
        if( !AppFileOperationsSimple.existAndHasAccessRWNotLink(outerStoragePath) ){
            fsPropExistOrCreate = AppFileStorageIndex.getFsPropCreate();
        }
        URI uriZipIndexStorage = URI.create("jar:" + outerStoragePath.toUri());
        fsPropExistOrCreate.put(AppFileNamesConstants.FILE_INDEX_KEY_MAP_URI, uriZipIndexStorage.toString());
        return fsPropExistOrCreate;
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
    protected static ConcurrentSkipListMap<String, Map<String, String>> getStoragesMap(){
        ConcurrentSkipListMap<String, Map<String, String>> toReturnStorages = new ConcurrentSkipListMap<String, Map<String, String>>();
        ArrayList<String> listOfPrefix = AppFileStorageIndex.getListOfPrefix();
        for( String itemPrefix : listOfPrefix){
            Boolean haveException = Boolean.FALSE;
            Path fromIndexDirLastStorage;
            try{
                fromIndexDirLastStorage = AppFileStorageIndex.getFromIndexDirLastStorage(itemPrefix);
                Map<String, String> forStorageByPrefix = AppFileStorageIndex.getForStorageByPrefix(fromIndexDirLastStorage);
                toReturnStorages.put(itemPrefix, forStorageByPrefix);
            } catch (IOException ex) {
                System.err.print(ex.getMessage());
                ex.printStackTrace();
                haveException = Boolean.TRUE;
            }
            if( haveException ){
                Map<String, String> buildForStorageByPrefix = AppFileStorageIndex.buildForStorageByPrefix(itemPrefix);
                toReturnStorages.put(itemPrefix, buildForStorageByPrefix);
                System.out.println("For prefix " + itemPrefix
                        + " create " + buildForStorageByPrefix.size());
            }
        }
        return toReturnStorages;
    }
    protected static URI getUriFromMapByPrefix(ConcurrentSkipListMap<String, Map<String, String>> storagesMap,
            String fsPrefixConstant){
        Map<String, String> getMapWithURI = storagesMap.get(fsPrefixConstant);
        String strForUri = getMapWithURI.get(AppFileNamesConstants.FILE_INDEX_KEY_MAP_URI);
        URI uriZipIndexStorage = URI.create(strForUri);
        return uriZipIndexStorage;
    }
    protected static Map<String, String> getParamMapByPrefix(ConcurrentSkipListMap<String, Map<String, String>> storagesMap,
            String fsPrefixConstant){
        Map<String, String> getMapWithURI = storagesMap.get(fsPrefixConstant);
        Map<String, String> forReturnMap = new HashMap<>();
        getMapWithURI.remove(AppFileNamesConstants.FILE_INDEX_KEY_MAP_URI);
        forReturnMap.putAll(getMapWithURI);
        return forReturnMap;
    }
    /**
     * 
     * @param prefixForFound
     * @return
     * @throws IOException 
     */
    protected static Path getFromIndexDirLastStorage(String prefixForFound) throws IOException{
        Path indexFolder = AppFileStorageIndex.getIndexFolder();
        ArrayList<Path> filesByMaskFromDir = AppFileOperationsSimple.getFilesByMaskFromDir(indexFolder, prefixForFound);
        if( filesByMaskFromDir.isEmpty() ){
            throw new IOException(AppFileStorageIndex.class.getCanonicalName() 
                    + " not found storages for prefix " 
                    + prefixForFound);
        }
            Path getMaxCompared = filesByMaskFromDir.get(0);
            for(Path itemMask : filesByMaskFromDir){
                if( getMaxCompared.compareTo(itemMask) > 0 ){
                    getMaxCompared = itemMask;
                }
            }
        return getMaxCompared;
    }
    protected static ArrayList<String> getListOfPrefix(){
        ArrayList<String> listForReturn = new ArrayList<String>();
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_DIR_LIST);
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_TMP);
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_JOURNAL);
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_LIST);
        //type and depth in dirictories structure
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_TYPE);
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_HASH);
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_EXIST);
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_WORD);
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD);
        listForReturn.add(AppFileNamesConstants.FILE_INDEX_PREFIX_LONG_WORD_LIST);
        return listForReturn;
    }
}
