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
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.ProviderNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThIndexStatistic {
    /**
     * ConcurrentHashMap<Integer, Integer> (<hashFieldCode, Value>)
     * hashFieldCode:
     * - Size
     * - VolumeNum
     * Search by tagFileName current VolumeNum and get his size
     * data for record size summ with writed size and compared with limit for
     * index type if need accumulate data to limit size, send it into cache
     * data structure, while volume not have limited size or time limit in nanos
     * 
     * and control to sizes for cache lists
     * 
     * ConcurrentHashMap<Integer,  - Strorage hash value
     *   ConcurrentHashMap<Integer, - Type of word index hash value
     *     ConcurrentHashMap<String, 
     *       ConcurrentHashMap<Integer, Integer>>>>
     */
    private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<String, ConcurrentHashMap<Integer, Integer>>>> fileStoragesMap;
    /**
     * <String, <String, Integer>> Structure for <Storage, <tagFileName, volumeCol>>
     * String - part of file name indexName (tag file name)
     * Integer - Count volume of this tagFileNames
     */
    private ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Integer>> listTagFileNameCountVol;
    /**
     * String - index name of storage
     * URI - to storage file, if not exist, than we not have storage
     */
    private ConcurrentSkipListMap<String, URI> storagesStatus;
    /**
     * <String, <String, Integer>> Structure for <Storage, <fullFileName, recordSize>>
     * String - index name of storage
     * String - full file name with size and voulme count
     * Integer - size for data records count in file
     */
    private ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Integer>> listFileNameSize;
    /**
     * list of Files for job start and not done
     * String - full file name with size and voulme count
     * String - index name of storage
     * for example:
     * check for storage exist
     * get from listTagFileNameCountVol number of current volume number, 
     * increment, save incremented value number in list
     * build new name with incremented value number
     * {@link ru.newcontrol.ncfv.AppFileOperationsSimple#buildInStorageFileName(java.lang.String, java.util.concurrent.ConcurrentSkipListMap, java.lang.Integer) }
     * check file name in listFileNameSize, after that listFilesInProcess
     * if file name not found
     * 
     * add name to listFilesInProcess, write file
     * add name to listFileNameSize, remove listFilesInProcess
     * 
     * @todo recode structure to equal of file list with volume numbers
     */
    private ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Integer>> listFilesInProcess;
    private ThIndexRule ruleThIndex;
    
    ThIndexStatistic(ThIndexRule outerRule){
        this.ruleThIndex = outerRule;
        this.storagesStatus = new ConcurrentSkipListMap<String, URI>();
        this.listTagFileNameCountVol = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Integer>>();
        this.listFilesInProcess = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Integer>>();
        this.listFileNameSize = new ConcurrentSkipListMap<String, ConcurrentSkipListMap<String, Integer>>();
        updateDataStorages();
    }
    /**
     * Update all storages and his contained files info
     */
    protected void updateDataStorages(){
        getListsOfFilesFromStorages();
    }
    protected void getListsOfFilesFromStorages(){
        AppFileStorageIndex currentIndexStorages = this.ruleThIndex.getIndexState().currentIndexStorages();
        currentIndexStorages.updateMapForStorages();
        ArrayList<String> listOfPrefixes = currentIndexStorages.listOfPrefixes();
        for(String itemStorages : listOfPrefixes){
            URI byPrefixGetUri = currentIndexStorages.byPrefixGetUri(itemStorages);
            Map<String, String> byPrefixGetMap = currentIndexStorages.byPrefixGetMap(itemStorages);
                            
            try( FileSystem fsForReadData = FileSystems.newFileSystem(byPrefixGetUri, byPrefixGetMap) ){
                Path rootForStorage = fsForReadData.getPath(AppFileNamesConstants.DIR_IDX_ROOT);
                this.storagesStatus.put(itemStorages, byPrefixGetUri);
                ConcurrentSkipListMap<String, Integer> listForVol = new ConcurrentSkipListMap<String, Integer>();
                this.listTagFileNameCountVol.put(itemStorages, listForVol);
                ConcurrentSkipListMap<String, Integer> listForProcess = new ConcurrentSkipListMap<String, Integer>();
                this.listFilesInProcess.put(itemStorages, listForProcess);
                ConcurrentSkipListMap<String, Integer> listForSize = new ConcurrentSkipListMap<String, Integer>();
                this.listFileNameSize.put(itemStorages, listForSize);
                ArrayList<Path> filesByMaskFromDir = AppFileOperationsSimple.getFilesByMaskFromDir(rootForStorage, "*");
                processPathToList(filesByMaskFromDir, itemStorages);
            } catch(FileSystemAlreadyExistsException exExist){
                System.err.println(ThIndexStatistic.class.getCanonicalName() + " newFileSystem for URI "
                        + byPrefixGetUri.toString() + " error " + exExist.getMessage());
                exExist.printStackTrace();
            } catch(FileSystemNotFoundException exNotFound){
                System.err.println(ThIndexStatistic.class.getCanonicalName() + " newFileSystem for URI "
                        + byPrefixGetUri.toString() + " error " + exNotFound.getMessage());
                exNotFound.printStackTrace();
            } catch(ProviderNotFoundException exProvNotFound){
                System.err.println(ThIndexStatistic.class.getCanonicalName() + " newFileSystem for URI "
                        + byPrefixGetUri.toString() + " error " + exProvNotFound.getMessage());
                exProvNotFound.printStackTrace();
            } catch(IllegalArgumentException exIllArg){
                System.err.println(ThIndexStatistic.class.getCanonicalName() + " newFileSystem for URI "
                        + byPrefixGetUri.toString() + " error " + exIllArg.getMessage());
                exIllArg.printStackTrace();
            } catch(SecurityException exSecureEx){
                System.err.println(ThIndexStatistic.class.getCanonicalName() + " newFileSystem for URI "
                        + byPrefixGetUri.toString() + " error " + exSecureEx.getMessage());
                exSecureEx.printStackTrace();
            } catch (IOException exIoEx) {
                System.err.println(ThIndexStatistic.class.getCanonicalName() + " newFileSystem for URI "
                        + byPrefixGetUri.toString() + " error " + exIoEx.getMessage());
                exIoEx.printStackTrace();
            }
            
        }
    }
    /**
     * 
     * @param filesFromDir
     * @param itemStorage 
     */
    protected void processPathToList(ArrayList<Path> filesFromDir, String itemStorage){
        ConcurrentSkipListMap<String, Integer> newRecord = new ConcurrentSkipListMap<String, Integer>();
        char[] separatorName = AppFileNamesConstants.FILE_DIR_PART_SEPARATOR.toCharArray();
        for(Path  folderItem : filesFromDir){
            Path itemPathFormDir = folderItem.getFileName();
            String nameIndexPart = "";
            String sizePart = "";
            String volumePart = "";
            String nameOfFile = itemPathFormDir.toString();
            char[] nameOfFileToCharArray = nameOfFile.toCharArray();
            Boolean firstSeparator = Boolean.FALSE;
            Boolean secondSeparator = Boolean.FALSE;
            int idx  = nameOfFileToCharArray.length;
            int firstIndx = 0;
            int secondIndx = 0;
            
            do{
                idx--;
                if( nameOfFileToCharArray[idx] == separatorName[0] ){
                    if( firstSeparator && !secondSeparator ){
                            secondSeparator = Boolean.TRUE;
                            secondIndx = idx;
                    }
                    if( !firstSeparator ){
                        firstSeparator = Boolean.TRUE;
                        firstIndx = idx;
                    }
                }
                if( firstSeparator && secondSeparator ){
                    nameIndexPart = nameOfFile.substring(0, secondIndx);
                    sizePart = nameOfFile.substring(secondIndx + 1, firstIndx);
                    try{
                        addToListFileNameSize(itemStorage, nameOfFile, Integer.valueOf(sizePart));
                    } catch(NumberFormatException ex){
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                    
                    volumePart = nameOfFile.substring(firstIndx + 1);
                    
                    try{
                        newRecord.put(nameIndexPart, Integer.valueOf(volumePart));
                    } catch(NumberFormatException ex){
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            } while( idx != 0 );
        }
        ConcurrentSkipListMap<String, Integer> getListForStorage = this.listTagFileNameCountVol.get(itemStorage);
        if( getListForStorage == null ){
            this.listTagFileNameCountVol.put(itemStorage, newRecord);
        } else {
            //test for need for put list into list
            getListForStorage.putAll(newRecord);
        }
        
    }
    /**
     * get limit records for storate 
     * @param itemStorage
     * @return 
     */
    protected Integer getLimitForStorage(String itemStorage){
        switch( itemStorage.hashCode() ){
            case 99400://AppFileNamesConstants.FILE_INDEX_PREFIX_DIR_LIST = "di-";       
                return AppConstants.DIR_LIST_RECORDS_COUNT_LIMIT;
            case 101415://AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_LIST = "fl-";     
                return AppConstants.FILE_LIST_RECORDS_COUNT_LIMIT;
            case 3641: //AppFileNamesConstants.FILE_INDEX_PREFIX_TMP = "t-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
            case 3331: //AppFileNamesConstants.FILE_INDEX_PREFIX_JOURNAL = "j-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
            case 101663: //AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_TYPE = "ft-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
            case 101291: //AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_HASH = "fh-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
            case 101787: //AppFileNamesConstants.FILE_INDEX_PREFIX_FILE_EXIST = "fx-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
            case 3734: //AppFileNamesConstants.FILE_INDEX_PREFIX_WORD = "w-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
            case 114249: //AppFileNamesConstants.FILE_INDEX_PREFIX_STORAGE_WORD = "sw-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
            case 107522: //AppFileNamesConstants.FILE_INDEX_PREFIX_LONG_WORD_LIST = "lw-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
            case 107243: //AppFileNamesConstants.FILE_INDEX_PREFIX_LONG_WORD_DATA = "ln-";
                return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT; 
        }
        return AppConstants.DEFAULT_STORAGE_RECORDS_COUNT_LIMIT;
    }
    /**
     * save not limited file name to list
     * need used in storages where technology has one not limited file
     * for example DirList (di-)
     * ...
     * @todo in perspective need release worker for contain index in limited packets
     */
    protected void addToListFileNameSize(String itemStorage, String fileName, Integer fileSize){
        ConcurrentSkipListMap<String, Integer> getStorage = this.listFileNameSize.get(itemStorage);
        if( getStorage == null ){
            getStorage = new ConcurrentSkipListMap<String, Integer>();
            this.listFileNameSize.put(itemStorage, getStorage);
        }
        getStorage.put(fileName, fileSize);
        this.listFileNameSize.put(itemStorage, getStorage);
    }
    /**
     * Get list files of storage and get volume number
     * return null if not have storage and value for tagName
     * @param itemStorage
     * @param tagFileName
     * @return 
     */
    protected Integer getVolumeNumberByTagName(String itemStorage, String tagFileName){
        ConcurrentSkipListMap<String, Integer> getListOfStorage = this.listTagFileNameCountVol.get(itemStorage);
        if( getListOfStorage == null ){
            return null;
        }
        Integer getVolumeNum = getListOfStorage.get(tagFileName);
        return getVolumeNum;
    }
    /**
     * Get list files of storage and get volume number, increment it, put to list
     * return null if not have storage
     * if not have value for tagName, add zero and return it
     * @param itemStorage
     * @param tagFileName
     * @return incremented and updated in list volume number, added if not exist
     */
    protected Integer getIncrementedVolumeNumberByTagName(String itemStorage, String tagFileName){
        Integer getVolumeNum = -1;
        ConcurrentSkipListMap<String, Integer> getListOfStorage = this.listTagFileNameCountVol.get(itemStorage);
        if( getListOfStorage == null ){
            getListOfStorage = new ConcurrentSkipListMap<String, Integer>();
            this.listTagFileNameCountVol.put(itemStorage, getListOfStorage);
        }
        getVolumeNum = getListOfStorage.get(tagFileName);
        if( getVolumeNum == null){
            getVolumeNum = -1;
        }
        getVolumeNum++;
        getListOfStorage.put(tagFileName, getVolumeNum);
        return getVolumeNum;
    }
    /**
     * build new file name for write to storage, found created name in lists
     * if not found than add to inProcess list and return
     * if found in list, build new name and check in lists, etc...
     * @param itemStorage
     * @param tagFileName
     * @param forSizeDetect
     * @return 
     */
    protected String createNewNameForWriteWithAllAddRecords(String itemStorage, String tagFileName, ConcurrentSkipListMap<?, ?> forSizeDetect){
        Integer incrementedVolumeNumberByTagName;
        String buildInStorageFileName;
        do{
            do{
                incrementedVolumeNumberByTagName = 
                        getIncrementedVolumeNumberByTagName(itemStorage, 
                                tagFileName);
                buildInStorageFileName = 
                        AppFileOperationsSimple.buildInStorageFileName(tagFileName, 
                                forSizeDetect, 
                                incrementedVolumeNumberByTagName);
            } while( isExistInListSize(itemStorage, buildInStorageFileName) );
        } while( isExistInListInProcess(itemStorage, buildInStorageFileName) );
        //Check for all lists for new instace of not existing storages list found...
        ConcurrentSkipListMap<String, Integer> listForStorages = this.listFilesInProcess.get(itemStorage);
        if( listForStorages == null ){
            listForStorages = new ConcurrentSkipListMap<String, Integer>();
        }
        listForStorages.put(buildInStorageFileName, incrementedVolumeNumberByTagName);
        this.listFilesInProcess.put(itemStorage, listForStorages);
        return buildInStorageFileName;
    }
    /**
     * After build name, before check in
     * {@link ru.newcontrol.ncfv.ThIndexStatistic#isExistInListInProcess(java.lang.String, java.lang.String)  }
     * before write to storage
     * Check for file name in list of storage
     * {@link ru.newcontrol.ncfv.ThIndexStatistic#listFileNameSize }
     * 
     * @param itemStorage
     * @param inStorageFileName
     * @return check result, true if found
     */
    protected Boolean isExistInListSize(String itemStorage, String inStorageFileName){
        ConcurrentSkipListMap<String, Integer> getListOfStorage = this.listFileNameSize.get(itemStorage);
        if( getListOfStorage == null ){
            return Boolean.FALSE;
        }
        Integer getVolumeNum = getListOfStorage.get(inStorageFileName);
        if( getVolumeNum == null ){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    } 
    /**
     * After build name, before write to storage
     * Check for file name in list of storage
     * {@link ru.newcontrol.ncfv.ThIndexStatistic#listFilesInProcess }
     * @param itemStorage
     * @param inStorageFileName
     * @return 
     */
    protected Boolean isExistInListInProcess(String itemStorage, String inStorageFileName){
        ConcurrentSkipListMap<String, Integer> returnedStorage = this.listFilesInProcess.get(itemStorage);
        if( returnedStorage == null ){
            return Boolean.FALSE;
        }
        Integer returnedFileName = returnedStorage.get(inStorageFileName);
        if( returnedFileName == null ){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    /**
     * after write file to storage
     * add to list of file names in storages and his size
     * remove from in process list
     * @param itemStorage
     * @param lastWritedFileName 
     */
    protected void addToListSizeRemoveListProcess(String itemStorage, Path lastWritedFileName){
        Path fileName = lastWritedFileName.getFileName();
        Map<String, String> fromNameTagSizeVol = AppFileOperationsSimple.getFromNameTagSizeVol(fileName);
        addToListFileNameSize(itemStorage, fileName.toString(), Integer.valueOf(fromNameTagSizeVol.get("sizePart")));
        this.listFilesInProcess.remove(fileName.toString());
    }
    /**
     * 
     * @param itemStorage
     * @return 
     */
    protected Boolean isStorageExist(String itemStorage){
        if( this.storagesStatus.get(itemStorage) == null ){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    /**
     * 
     */
    protected void printToConsoleListContent(){
        for(Map.Entry<String, URI> itemStatus : this.storagesStatus.entrySet() ){
            System.out.println("storagesStatus key: " + itemStatus.getKey() + " val:" + itemStatus.getValue().toString());
            
        }
        System.out.println(" storagesStatus countRecord:" + String.valueOf(this.storagesStatus.size()));
        for(Map.Entry<String, ConcurrentSkipListMap<String, Integer>> itemCountVol : this.listTagFileNameCountVol.entrySet() ){
            System.out.println("listTagFileNameCountVol key: " + itemCountVol.getKey() + " val:" + String.valueOf(itemCountVol.getValue().size()));
            for(Map.Entry<String, Integer> listCountVol : itemCountVol.getValue().entrySet() ){
                System.out.println("key: " + listCountVol.getKey() + " val:" + listCountVol.getValue().toString());
            }
            System.out.println(" listTagFileNameCountVol countRecord:" + String.valueOf(itemCountVol.getValue().size()));
        }
        for(Map.Entry<String, ConcurrentSkipListMap<String, Integer>> itemNameSize : this.listFileNameSize.entrySet() ){
            System.out.println("listFileNameSize key: " + itemNameSize.getKey() + " val:" + String.valueOf(itemNameSize.getValue().size()));
            for(Map.Entry<String, Integer> listNameSize : itemNameSize.getValue().entrySet() ){
                System.out.println("key: " + listNameSize.getKey() + " val:" + listNameSize.getValue().toString());
            }
            System.out.println(" listFileNameSize countRecord:" + String.valueOf(itemNameSize.getValue().size()));
        }
        for(Map.Entry<String, ConcurrentSkipListMap<String, Integer>> itemProcess : this.listFilesInProcess.entrySet() ){
            System.out.println("listFilesInProcess key: " + itemProcess.getKey() + " val:" + String.valueOf(itemProcess.getValue().size()));
            for(Map.Entry<String, Integer> listProcess : itemProcess.getValue().entrySet() ){
                System.out.println("key: " + listProcess.getKey() + " val:" + listProcess.getValue().toString());
            }
            System.out.println(" listFilesInProcess countRecord:" + String.valueOf(itemProcess.getValue().size()));
        }
    }
}
