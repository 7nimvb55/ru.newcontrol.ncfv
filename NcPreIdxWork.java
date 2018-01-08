/*
 *  Copyright 2017 Administrator of development departament newcontrol.ru .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Администратор
 */
public class NcPreIdxWork {
    public static void outToConsoleIdxDirs(){
        NcParamFv currentWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
        TreeMap<Integer, File> listSubDirs = new TreeMap<Integer, File>();
        listSubDirs.putAll(currentWorkCfg.tmIndexSubDirs);
        listSubDirs.put("index".hashCode(), new File(currentWorkCfg.indexPath));
        for( Map.Entry<Integer, File> items : listSubDirs.entrySet() ){
            NcAppHelper.outMessage(items.getValue().getAbsolutePath());
        }
        NcAppHelper.outMessage("Next way");
        TreeMap<Integer, File> indexWorkSubDirFilesList = NcIdxFileManager.getIndexWorkSubDirFilesList();
        for( Map.Entry<Integer, File> itemsNextWay : indexWorkSubDirFilesList.entrySet() ){
            NcAppHelper.outMessage("key: " + itemsNextWay.getKey());
            NcAppHelper.outMessage(itemsNextWay.getValue().getAbsolutePath());
        }
        NcAppHelper.outMessage("By name");
        String[] arrStrCode = NcManageCfg.getWorkSubDirList();
        for( String itemSubDir : arrStrCode ){
            NcAppHelper.outMessage("key name: " + itemSubDir + "\tkey value: " + itemSubDir.hashCode() );
            NcAppHelper.outMessage(indexWorkSubDirFilesList.get(itemSubDir.hashCode()).getAbsolutePath());
        }
    }
    /**
     * Check files in index subFolders
     */
    public static void checkInIndexFolerContent(){
        
        NcParamFv readedWorkCfg = NcParamFvReader.readDataFromWorkCfg();
        if( NcParamFvManager.isNcParamFvDataEmpty(readedWorkCfg) ){
            readedWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
        }
        
        File fileWorkDir = new File(readedWorkCfg.indexPath);
        TreeMap<Integer, File> listSubDirs = new TreeMap<Integer, File>();
        listSubDirs.putAll(readedWorkCfg.tmIndexSubDirs);
    
        File fileFx = listSubDirs.get("/fx".hashCode());
        TreeMap<Long, File> fileFromDirListExist = getNotFullFiles(fileFx);
        File fileFl = listSubDirs.get("/fl".hashCode());
        TreeMap<Long, File> fileFromDirList = getNotFullFiles(fileFl);
        File fileLw = listSubDirs.get("/lw".hashCode());
        checkFilesOnReadable(fileLw);
        File fileLn = listSubDirs.get("/ln".hashCode());
        checkFilesOnReadable(fileLn);
        File fileW = listSubDirs.get("/w".hashCode());
        checkFilesOnReadable(fileW);
        File fileT = listSubDirs.get("/t".hashCode());
        checkFilesOnReadable(fileT);
        File fileSw = listSubDirs.get("/sw".hashCode());
        checkFilesOnReadable(fileSw);
        checkTmpIDsData();
        getNotFinishedAppendToIndex(fileFromDirList, fileFromDirListExist);
    }
    /**
     * Compare for content of DirListAttr and DirListExist, by fileds:
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr#dirListID NcDcIdxDirListToFileAttr.dirListID} == {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist#dirListID NcDcIdxDirListToFileExist.dirListID}
     * <li>{@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr#diskID NcDcIdxDirListToFileAttr.diskID} == {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist#diskID NcDcIdxDirListToFileExist.diskID}
     * <li>{@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr#pathHash NcDcIdxDirListToFileAttr.pathHash} == {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist#pathHash NcDcIdxDirListToFileExist.pathHash}
     * </ul>
     * Output to console result of compare lists
     * 
     */
    public static void getNotEqualsRecordDirListAttrVsExist(){
        long recordId = 0;
        
        File recordsAttr = NcIdxFileManager.getFileForDirListAttrContainedRecordId(recordId);
        File recordsExist = NcIdxFileManager.getFileForDirListExistContainedRecordId(recordId);
        int countError = 0;
        do{
            NcAppHelper.outMessage("In file: " + recordsAttr.getAbsolutePath());
            NcAppHelper.outMessage("In file: " + recordsExist.getAbsolutePath());
            if( NcIdxFileManager.isErrorForFileOperation(recordsAttr) ){
                if( countError > 3 ){
                    break;
                }
                countError++;
                continue;
            }
            if( NcIdxFileManager.isErrorForFileOperation(recordsExist) ){
                if( countError > 3 ){
                    break;
                }
                countError++;
                continue;
            }

            TreeMap<Long, NcDcIdxDirListToFileAttr> dataFromDirList = new TreeMap<>();
            TreeMap<Long, NcDcIdxDirListToFileExist> dataFromDirListExist = new TreeMap<>();

            TreeMap<Long, NcDcIdxDirListToFileAttr> badDirIdDirListAttr = new TreeMap<>();
            TreeMap<Long, NcDcIdxDirListToFileExist> badDirIdDirListExist = new TreeMap<>();

            TreeMap<Long, NcDcIdxDirListToFileAttr> badDiskIdDirListAttr = new TreeMap<>();
            TreeMap<Long, NcDcIdxDirListToFileExist> badDiskIdDirListExist = new TreeMap<>();

            TreeMap<Long, NcDcIdxDirListToFileAttr> badPathHashDirListAttr = new TreeMap<>();
            TreeMap<Long, NcDcIdxDirListToFileExist> badPathHashDirListExist = new TreeMap<>();


            dataFromDirList.putAll((Map<? extends Long, ? extends NcDcIdxDirListToFileAttr>) 
                    NcIdxFileManager.getDataFromFile(recordsAttr));
            dataFromDirListExist.putAll((Map<? extends Long, ? extends NcDcIdxDirListToFileExist>) 
                    NcIdxFileManager.getDataFromFile(recordsExist));
            
            NcAppHelper.outMessage("Start check for Attr count of records: " + dataFromDirList.size());
            NcAppHelper.outMessage("Start check for Exist count of records: " + dataFromDirListExist.size());
            
            long DirListAttrId = dataFromDirList.firstEntry().getValue().dirListID;
            long DirListExistId = dataFromDirListExist.firstEntry().getValue().dirListID;
            long recordFirstAttrId = dataFromDirList.firstKey();
            long recordFirstExistId = dataFromDirListExist.firstKey();

            long recordLastAttrId = dataFromDirList.lastKey();
            long recordLastExistId = dataFromDirListExist.lastKey();

            NcDcIdxDirListToFileAttr dataFirstAttr = dataFromDirList.firstEntry().getValue();
            NcDcIdxDirListToFileExist dataFirstExist = dataFromDirListExist.firstEntry().getValue();

            NcDcIdxDirListToFileAttr dataLastAttr = dataFromDirList.lastEntry().getValue();
            NcDcIdxDirListToFileExist dataLastExist = dataFromDirListExist.lastEntry().getValue();

            NcDcIdxDirListToFileAttr dataAttr = dataFromDirList.firstEntry().getValue();
            NcDcIdxDirListToFileExist dataExist = dataFromDirListExist.firstEntry().getValue();
            
            boolean isDiskIdEquals = false;
            boolean isDirListId = false;
            boolean isDirNameHash = false;
            long currentAttrId = 0;
            long currentExistId = 0;

            long prevAttrId = 0;
            long prevExistId = 0;
            
            
            Iterator<NcDcIdxDirListToFileAttr> iterAttr = dataFromDirList.values().iterator();
            Iterator<NcDcIdxDirListToFileExist> iterExist = dataFromDirListExist.values().iterator();
            while( iterAttr.hasNext() ){

                dataAttr = iterAttr.next();
                if( iterExist.hasNext() ){
                    dataExist = iterExist.next();
                }
                if( dataAttr.equals(dataFirstAttr) ){
                    currentAttrId = recordFirstAttrId;
                }
                if( dataExist.equals(dataFirstExist) ){
                    currentExistId = recordFirstExistId;
                }
                isDiskIdEquals = dataAttr.diskID == dataExist.diskID;
                isDirListId = dataAttr.dirListID == dataExist.dirListID;
                isDirNameHash = dataAttr.pathHash == dataExist.pathHash;

                if( !isDiskIdEquals ){
                    badDiskIdDirListAttr.put(System.nanoTime(),dataAttr);
                    badDiskIdDirListExist.put(System.nanoTime(),dataExist);
                }

                if( !isDirListId ){
                    badDirIdDirListAttr.put(System.nanoTime(),dataAttr);
                    badDirIdDirListExist.put(System.nanoTime(),dataExist);
                }

                if( !isDirNameHash ){
                    badPathHashDirListAttr.put(System.nanoTime(),dataAttr);
                    badPathHashDirListExist.put(System.nanoTime(),dataExist);
                }
                
                if( iterAttr.hasNext() ){
                    prevAttrId = currentAttrId;
                    currentAttrId++;
                }

                if( iterExist.hasNext() ){
                    prevExistId = currentExistId;
                    currentExistId++;
                }

                
                NcAppHelper.outMessage("Attr dirListID: " + dataAttr.dirListID + " Exist dirListID:" + dataExist.dirListID);
            }
            recordsAttr = NcIdxFileManager.getFileForDirListAttrContainedRecordId(recordLastAttrId + 1);
            recordsExist = NcIdxFileManager.getFileForDirListExistContainedRecordId(recordLastExistId + 1);
            
            if( !badDiskIdDirListAttr.isEmpty() ){
                NcAppHelper.outMessage("In file: " + recordsAttr.getAbsolutePath() + "\n BadDiskID count of records " + badDiskIdDirListAttr.size());
            }
            if( !badDirIdDirListAttr.isEmpty() ){
                NcAppHelper.outMessage("In file: " + recordsAttr.getAbsolutePath() + "\n BadDiskID count of records " + badDirIdDirListAttr.size());
            }
            if( !badPathHashDirListAttr.isEmpty() ){
                NcAppHelper.outMessage("In file: " + recordsAttr.getAbsolutePath() + "\n BadDiskID count of records " + badPathHashDirListAttr.size());
            }
            if( !badDiskIdDirListExist.isEmpty() ){
                NcAppHelper.outMessage("In file: " + recordsExist.getAbsolutePath() + "\n BadDiskID count of records " + badDiskIdDirListExist.size());
            }
            if( !badDirIdDirListExist.isEmpty() ){
                NcAppHelper.outMessage("In file: " + recordsExist.getAbsolutePath() + "\n BadDiskID count of records " + badDirIdDirListExist.size());
            }    
            if( !badPathHashDirListExist.isEmpty() ){
                NcAppHelper.outMessage("In file: " + recordsExist.getAbsolutePath() + "\n BadDiskID count of records " + badPathHashDirListExist.size());
            }    
        }
        while(NcIdxFileManager.isErrorForFileOperation(recordsAttr) ||
        NcIdxFileManager.isErrorForFileOperation(recordsExist));
        
        
    }
    /**
     * This method return not finished (breaked) record of data
     * @param inFuncFileFromDirList
     * @param inFuncFileFromDirListExist 
     */
    public static void getNotFinishedAppendToIndex(TreeMap<Long, File> inFuncFileFromDirList, TreeMap<Long, File> inFuncFileFromDirListExist){
        TreeMap<Long, NcDcIdxDirListToFileAttr> dataFromDirList = new TreeMap<>();
        TreeMap<Long, NcDcIdxDirListToFileExist> dataFromDirListExist = new TreeMap<>();
        for(Map.Entry<Long, File> itemFromDirList : inFuncFileFromDirList.entrySet() ){
            dataFromDirList.putAll((Map<? extends Long, ? extends NcDcIdxDirListToFileAttr>) NcIdxFileManager.getDataFromFile(itemFromDirList.getValue()));
        }
        for(Map.Entry<Long, File> itemFromDirListExist : inFuncFileFromDirListExist.entrySet() ){
            dataFromDirListExist.putAll((Map<? extends Long, ? extends NcDcIdxDirListToFileExist>) NcIdxFileManager.getDataFromFile(itemFromDirListExist.getValue()));
        }
        NcAppHelper.outMessage("DirList record count: " + dataFromDirList.size());
        NcAppHelper.outMessage("Files count: " + inFuncFileFromDirList.size());
        NcAppHelper.outMessage("DirList Exist record count: " + dataFromDirListExist.size());
        NcAppHelper.outMessage("Files count: " + inFuncFileFromDirListExist.size());
        
        for(Map.Entry<Long, NcDcIdxDirListToFileExist> itemFromDirListExist : dataFromDirListExist.entrySet() ){
            outToConsoleDirListExist(itemFromDirListExist.getValue());
            if( itemFromDirListExist.getValue().nanoTimeEndAddToIndex < 0 ){
                for(Map.Entry<Long, NcDcIdxDirListToFileAttr> itemFromDirList : dataFromDirList.entrySet() ){
                    if( itemFromDirListExist.getValue().dirListID == itemFromDirList.getValue().dirListID ){
                        outToConsoleDirListAttr(itemFromDirList.getValue());
                    }
                }
            }
        }
    }
    /**
     * Print to console data from {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr}
     * @param inFuncData {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr}
     */
    public static void outToConsoleDirListAttr(NcDcIdxDirListToFileAttr inFuncData){
        NcAppHelper.outMessage("dirListID: " + inFuncData.dirListID);
        NcAppHelper.outMessage("diskID: " + inFuncData.diskID);
        NcAppHelper.outMessage("diskSnLong: " + inFuncData.diskSnLong);
        NcAppHelper.outMessage("diskTotalSpace: " + inFuncData.diskTotalSpace);
        NcAppHelper.outMessage("diskProgramAlias: " + inFuncData.diskProgramAlias);
        NcAppHelper.outMessage("diskProgramAliasHash: " + inFuncData.diskProgramAliasHash);
        NcAppHelper.outMessage("diskSnHex: " + inFuncData.diskSnHex);
        NcAppHelper.outMessage("diskSnHexHash: " + inFuncData.diskSnHexHash);
        NcAppHelper.outMessage("diskLetter: " + inFuncData.diskLetter);
        NcAppHelper.outMessage("path: " + inFuncData.path);
        NcAppHelper.outMessage("pathHash: " + inFuncData.pathHash);
        NcAppHelper.outMessage("fileLength: " + inFuncData.fileLength);
        NcAppHelper.outMessage("fileCanRead: " + inFuncData.fileCanRead);
        NcAppHelper.outMessage("fileCanWrite: " + inFuncData.fileCanWrite);
        NcAppHelper.outMessage("fileCanExecute: " + inFuncData.fileCanExecute);
        NcAppHelper.outMessage("fileIsHidden: " + inFuncData.fileIsHidden);
        NcAppHelper.outMessage("fileLastModified: " + inFuncData.fileLastModified);
        NcAppHelper.outMessage("fileIsDirectory: " + inFuncData.fileIsDirectory);
        NcAppHelper.outMessage("fileIsFile: " + inFuncData.fileIsFile);
        NcAppHelper.outMessage("recordTime: " + inFuncData.recordTime);
        NcAppHelper.outMessage("deletedRec: " + inFuncData.deletedRec);
        NcAppHelper.outMessage("changedRecordID: " + inFuncData.changedRecordID);
    }
    /**
     * Print to console data from {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist}
     * @param inFuncData {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist}
     */
    public static void outToConsoleDirListExist(NcDcIdxDirListToFileExist inFuncData){
        if( inFuncData.nanoTimeEndAddToIndex < 0 ){
            NcAppHelper.outMessage("dirListID: " + inFuncData.dirListID);
            NcAppHelper.outMessage("diskID: " + inFuncData.diskID);
            NcAppHelper.outMessage("pathWithOutDiskLetter: " + inFuncData.pathWithOutDiskLetter);
            NcAppHelper.outMessage("pathHash: " + inFuncData.pathHash);
            NcAppHelper.outMessage("nanoTimeStartAddToIndex: " + inFuncData.nanoTimeStartAddToIndex);
            NcAppHelper.outMessage("nanoTimeEndAddToIndex: " + inFuncData.nanoTimeEndAddToIndex);
            NcAppHelper.outMessage("recordTime: " + inFuncData.recordTime);
        }
        
    }
    /**
     * List of data has 100 records, this method return file names for records < 100
     * @param inFuncSubDir
     * @return 
     */
    public static TreeMap<Long, File> getNotFullFiles(File inFuncSubDir){
        TreeMap<Integer, File> itemsInSubDir = NcIdxFileManager.getFileListFromSubDir(inFuncSubDir);
        TreeMap<Long, File> notFullItemsInSubDir = new TreeMap<Long, File>();
        for(Map.Entry<Integer, File> itemFile : itemsInSubDir.entrySet()){
            if( itemFile.getValue().isDirectory() ){
                checkFilesOnReadable(itemFile.getValue());
            }
            else{
                int recordsInFile = NcIdxFileManager.getCountRecordDataInFile(itemFile.getValue());
                if( recordsInFile < 0 ){
                    if ( itemFile.getValue().delete() ){
                        NcAppHelper.outMessage(itemFile.getValue().getAbsolutePath() +
                        "_|_|_|_deleted");
                    }
                }
                if( recordsInFile != 100){
                    notFullItemsInSubDir.put(System.nanoTime(), itemFile.getValue());
                }
            }
        }
        return notFullItemsInSubDir;
    }
    /**
     * If on the file write operation breaked, data in file damage, this method test file
     * on readable and delete damaged files
     * @param inFuncSubDir 
     */
    public static void checkFilesOnReadable(File inFuncSubDir){
        TreeMap<Integer, File> itemsInSubDir = NcIdxFileManager.getFileListFromSubDir(inFuncSubDir);
        for(Map.Entry<Integer, File> itemFile : itemsInSubDir.entrySet()){
            if( itemFile.getValue().isDirectory() ){
                checkFilesOnReadable(itemFile.getValue());
            }
            else{
                if( !NcIdxFileManager.isDataInFileNotWrong(itemFile.getValue()) ){
                    if ( itemFile.getValue().delete() ){
                        NcAppHelper.outMessage(itemFile.getValue().getAbsolutePath() +
                        "_|_|_|_deleted");
                    }
                }
            }
        }
    }
    
    /**
     *
     * @param inFuncNameSubDir
     * @return
     */
    public static Object getNcClassNameForSubDir(String inFuncNameSubDir){
        switch (inFuncNameSubDir){
            case "/t":
                    return Object.class;
            case "/di":
                    return Object.class;
            case "/j":
                    return Object.class;
            case "/fl":
                    return NcDcIdxDirListToFileAttr.class;
            case "/ft":
                    return NcDcIdxDirListToFileType.class;
            case "/fh":
                    return NcDcIdxDirListToFileHash.class;
            case "/fx":
                    return NcDcIdxDirListToFileExist.class;
            case "/w":
                    return NcDcIdxWordToFile.class;
            case "/sw":
                    return NcDcIdxStorageWordToFile.class;
            case "/lw":
                    return NcDcIdxLongWordListToFile.class;
            case "/ln":
                    return NcDcIdxWordToFile.class;
        }
        return Object.class;
    }
    // wirte to text log files in /di directory into file name = timeStart(from /fx subdir lists) in system.nanotime
    // -> /subdir/namefile, id dir list, id in storage word
    // 100 records into one file, after record, rename to timeStart-TimeStop in system.nanotime
    // after danage data, part of not readable files may be repair form parsed logs, smoke and sleep

    /**
     *
     * @param inFuncSubDir
     */
    public static void outFilesFromSubDirToConsole(File inFuncSubDir){
        File[] inDirFiles = inFuncSubDir.listFiles();
        NcAppHelper.outMessage("");
        NcAppHelper.outMessage("Directory path: " + inFuncSubDir.getAbsolutePath());
        NcAppHelper.outMessage("Count files in directory: " + inDirFiles.length);
        for( File itemFile : inDirFiles){
            NcAppHelper.outMessage("" + itemFile.getName());
        }
    }
    
    /**
     *
     */
    public static void checkTmpIDsData(){
        File fileTmpIDs = NcIdxFileManager.getTmpIdsFile();
        boolean fileGet = NcIdxFileManager.isErrorForFileOperation(fileTmpIDs);
        if( fileGet ){
            NcAppHelper.outMessage("TmpIDsFile not exist or wrong");
        }
        if( !fileGet ){
            NcTmpNowProcessInfo readedTmpIDsData= NcIndexManageIDs.getTmpIDsData(fileTmpIDs);
            boolean isTmpIdsDataWrong = NcIndexManageIDs.isTmpIDsDataWrong(readedTmpIDsData);
            if( !isTmpIdsDataWrong ){
                getTmpIdsDataToConsole(readedTmpIDsData);
            }
            boolean isEmpty = NcIndexManageIDs.isTmpIDsDataEmpty(readedTmpIDsData);
            if( isEmpty ){
                NcAppHelper.outMessage("TmpIDsFile readed data is Empty");
            }
        }
    }

    /**
     *
     * @param readedTmpIDsData
     */
    public static void getTmpIdsDataToConsole(NcTmpNowProcessInfo readedTmpIDsData){
        NcAppHelper.outMessage("TmpIdsData: ");
                NcAppHelper.outMessage("journalid: " + readedTmpIDsData.journalid + " \tjournalname: " + readedTmpIDsData.journalname);
                NcAppHelper.outMessage("listnameid: " + readedTmpIDsData.listnameid + " \tlistnameid: " + readedTmpIDsData.listname);
                NcAppHelper.outMessage("hashlistnnameid: " + readedTmpIDsData.hashlistnameid + " \thashlistname: " + readedTmpIDsData.hashlistname);
                NcAppHelper.outMessage("longwordlistnameid: " + readedTmpIDsData.longwordlistnameid + " \tlongwordlistname: " + readedTmpIDsData.longwordlistname);
    }

    
}
