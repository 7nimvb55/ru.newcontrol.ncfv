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
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     */
    protected static void outToConsoleIdxDirs(){
        NcParamFv currentWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
        TreeMap<Integer, File> listSubDirs = new TreeMap<Integer, File>();
        listSubDirs.putAll(currentWorkCfg.tmIndexSubDirs);
        listSubDirs.put("index".hashCode(), new File(currentWorkCfg.indexPath));
        for( Map.Entry<Integer, File> items : listSubDirs.entrySet() ){
            NcAppHelper.outMessageToConsole(NcIdxFileManager.getStrCanPathFromFile(items.getValue()));
        }
        NcAppHelper.outMessage("Next way");
        TreeMap<Integer, File> indexWorkSubDirFilesList = NcIdxFileManager.getIndexWorkSubDirFilesList();
        for( Map.Entry<Integer, File> itemsNextWay : indexWorkSubDirFilesList.entrySet() ){
            NcAppHelper.outMessageToConsole("key: " + itemsNextWay.getKey());
            NcAppHelper.outMessageToConsole(NcIdxFileManager.getStrCanPathFromFile(itemsNextWay.getValue()));
        }
        NcAppHelper.outMessageToConsole("By name");
        String[] arrStrCode = NcManageCfg.getWorkSubDirList();
        for( String itemSubDir : arrStrCode ){
            NcAppHelper.outMessageToConsole("key name: " + itemSubDir + "\tkey value: " + itemSubDir.hashCode() );
            NcAppHelper.outMessageToConsole(NcIdxFileManager.getStrCanPathFromFile(indexWorkSubDirFilesList.get(itemSubDir.hashCode())));
        }
    }
    /**
     * Not used
     * Check files in index subFolders
     */
    private static void checkInIndexFolderContent(){
        
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     * Compare for content of DirListAttr and DirListExist, by fileds:
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr#dirListID NcDcIdxDirListToFileAttr.dirListID} == {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist#dirListID NcDcIdxDirListToFileExist.dirListID}
     * <li>{@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr#diskID NcDcIdxDirListToFileAttr.diskID} == {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist#diskID NcDcIdxDirListToFileExist.diskID}
     * <li>{@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr#pathHash NcDcIdxDirListToFileAttr.pathHash} == {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist#pathHash NcDcIdxDirListToFileExist.pathHash}
     * </ul>
     * Output to console result of compare lists
     */
    protected static void getNotEqualsRecordDirListAttrVsExist(){
        long recordId = 0;
        
        File recordsAttr = NcIdxFileManager.getFileForDirListAttrContainedRecordId(recordId);
        File recordsExist = NcIdxFileManager.getFileForDirListExistContainedRecordId(recordId);
        int countError = 0;
        do{
            NcAppHelper.outMessageToConsole("In file: " + NcIdxFileManager.getStrCanPathFromFile(recordsAttr));
            NcAppHelper.outMessageToConsole("In file: " + NcIdxFileManager.getStrCanPathFromFile(recordsExist));
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
            
            NcAppHelper.outMessageToConsole("Start check for Attr count of records: " + dataFromDirList.size());
            NcAppHelper.outMessageToConsole("Start check for Exist count of records: " + dataFromDirListExist.size());
            
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

                
                NcAppHelper.outMessageToConsole("Attr dirListID: " + dataAttr.dirListID + " Exist dirListID:" + dataExist.dirListID);
            }
            recordsAttr = NcIdxFileManager.getFileForDirListAttrContainedRecordId(recordLastAttrId + 1);
            recordsExist = NcIdxFileManager.getFileForDirListExistContainedRecordId(recordLastExistId + 1);
            
            if( !badDiskIdDirListAttr.isEmpty() ){
                NcAppHelper.outMessageToConsole("In file: " + NcIdxFileManager.getStrCanPathFromFile(recordsAttr) + "\n BadDiskID count of records " + badDiskIdDirListAttr.size());
            }
            if( !badDirIdDirListAttr.isEmpty() ){
                NcAppHelper.outMessageToConsole("In file: " + NcIdxFileManager.getStrCanPathFromFile(recordsAttr) + "\n BadDiskID count of records " + badDirIdDirListAttr.size());
            }
            if( !badPathHashDirListAttr.isEmpty() ){
                NcAppHelper.outMessageToConsole("In file: " + NcIdxFileManager.getStrCanPathFromFile(recordsAttr) + "\n BadDiskID count of records " + badPathHashDirListAttr.size());
            }
            if( !badDiskIdDirListExist.isEmpty() ){
                NcAppHelper.outMessageToConsole("In file: " + NcIdxFileManager.getStrCanPathFromFile(recordsExist) + "\n BadDiskID count of records " + badDiskIdDirListExist.size());
            }
            if( !badDirIdDirListExist.isEmpty() ){
                NcAppHelper.outMessageToConsole("In file: " + NcIdxFileManager.getStrCanPathFromFile(recordsExist) + "\n BadDiskID count of records " + badDirIdDirListExist.size());
            }    
            if( !badPathHashDirListExist.isEmpty() ){
                NcAppHelper.outMessageToConsole("In file: " + NcIdxFileManager.getStrCanPathFromFile(recordsExist) + "\n BadDiskID count of records " + badPathHashDirListExist.size());
            }    
        }
        while(NcIdxFileManager.isErrorForFileOperation(recordsAttr) ||
        NcIdxFileManager.isErrorForFileOperation(recordsExist));
        
        
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkInIndexFolderContent() }
     * </ul>
     * This method return not finished (breaked) record of data
     * @param inFuncFileFromDirList
     * @param inFuncFileFromDirListExist 
     */
    private static void getNotFinishedAppendToIndex(TreeMap<Long, File> inFuncFileFromDirList, TreeMap<Long, File> inFuncFileFromDirListExist){
        TreeMap<Long, NcDcIdxDirListToFileAttr> dataFromDirList = new TreeMap<>();
        TreeMap<Long, NcDcIdxDirListToFileExist> dataFromDirListExist = new TreeMap<>();
        for(Map.Entry<Long, File> itemFromDirList : inFuncFileFromDirList.entrySet() ){
            dataFromDirList.putAll((Map<? extends Long, ? extends NcDcIdxDirListToFileAttr>) NcIdxFileManager.getDataFromFile(itemFromDirList.getValue()));
        }
        for(Map.Entry<Long, File> itemFromDirListExist : inFuncFileFromDirListExist.entrySet() ){
            dataFromDirListExist.putAll((Map<? extends Long, ? extends NcDcIdxDirListToFileExist>) NcIdxFileManager.getDataFromFile(itemFromDirListExist.getValue()));
        }
        NcAppHelper.outMessageToConsole("DirList record count: " + dataFromDirList.size());
        NcAppHelper.outMessageToConsole("Files count: " + inFuncFileFromDirList.size());
        NcAppHelper.outMessageToConsole("DirList Exist record count: " + dataFromDirListExist.size());
        NcAppHelper.outMessageToConsole("Files count: " + inFuncFileFromDirListExist.size());
        
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotFinishedAppendToIndex(java.util.TreeMap, java.util.TreeMap) }
     * </ul>
     * Print to console data from {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr}
     * @param inFuncData {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr}
     */
    protected static void outToConsoleDirListAttr(NcDcIdxDirListToFileAttr inFuncData){
        NcAppHelper.outMessageToConsole("dirListID: " + inFuncData.dirListID);
        NcAppHelper.outMessageToConsole("diskID: " + inFuncData.diskID);
        NcAppHelper.outMessageToConsole("diskSnLong: " + inFuncData.diskSnLong);
        NcAppHelper.outMessageToConsole("diskTotalSpace: " + inFuncData.diskTotalSpace);
        NcAppHelper.outMessageToConsole("diskProgramAlias: " + inFuncData.diskProgramAlias);
        NcAppHelper.outMessageToConsole("diskProgramAliasHash: " + inFuncData.diskProgramAliasHash);
        NcAppHelper.outMessageToConsole("diskSnHex: " + inFuncData.diskSnHex);
        NcAppHelper.outMessageToConsole("diskSnHexHash: " + inFuncData.diskSnHexHash);
        NcAppHelper.outMessageToConsole("diskLetter: " + inFuncData.diskLetter);
        NcAppHelper.outMessageToConsole("path: " + inFuncData.path);
        NcAppHelper.outMessageToConsole("pathHash: " + inFuncData.pathHash);
        NcAppHelper.outMessageToConsole("fileLength: " + inFuncData.fileLength);
        NcAppHelper.outMessageToConsole("fileCanRead: " + inFuncData.fileCanRead);
        NcAppHelper.outMessageToConsole("fileCanWrite: " + inFuncData.fileCanWrite);
        NcAppHelper.outMessageToConsole("fileCanExecute: " + inFuncData.fileCanExecute);
        NcAppHelper.outMessageToConsole("fileIsHidden: " + inFuncData.fileIsHidden);
        NcAppHelper.outMessageToConsole("fileLastModified: " + inFuncData.fileLastModified);
        NcAppHelper.outMessageToConsole("fileIsDirectory: " + inFuncData.fileIsDirectory);
        NcAppHelper.outMessageToConsole("fileIsFile: " + inFuncData.fileIsFile);
        NcAppHelper.outMessageToConsole("recordTime: " + inFuncData.recordTime);
        NcAppHelper.outMessageToConsole("deletedRec: " + inFuncData.deletedRec);
        NcAppHelper.outMessageToConsole("changedRecordID: " + inFuncData.changedRecordID);
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotFinishedAppendToIndex(java.util.TreeMap, java.util.TreeMap) }
     * </ul>
     * Print to console data from {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist}
     * @param inFuncData {@link ru.newcontrol.ncfv.NcDcIdxDirListToFileExist}
     */
    private static void outToConsoleDirListExist(NcDcIdxDirListToFileExist inFuncData){
        if( inFuncData.nanoTimeEndAddToIndex < 0 ){
            NcAppHelper.outMessageToConsole("dirListID: " + inFuncData.dirListID);
            NcAppHelper.outMessageToConsole("diskID: " + inFuncData.diskID);
            NcAppHelper.outMessageToConsole("pathWithOutDiskLetter: " + inFuncData.pathWithOutDiskLetter);
            NcAppHelper.outMessageToConsole("pathHash: " + inFuncData.pathHash);
            NcAppHelper.outMessageToConsole("nanoTimeStartAddToIndex: " + inFuncData.nanoTimeStartAddToIndex);
            NcAppHelper.outMessageToConsole("nanoTimeEndAddToIndex: " + inFuncData.nanoTimeEndAddToIndex);
            NcAppHelper.outMessageToConsole("recordTime: " + inFuncData.recordTime);
        }
        
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkInIndexFolderContent() }
     * </ul>
     * List of data has 100 records, this method return file names for records < 100
     * @param inFuncSubDir
     * @return 
     */
    private static TreeMap<Long, File> getNotFullFiles(File inFuncSubDir){
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
                        NcAppHelper.outMessage(NcIdxFileManager.getStrCanPathFromFile(itemFile.getValue()) +
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkInIndexFolderContent() }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotFullFiles(java.io.File) }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkFilesOnReadable(java.io.File) }
     * </ul>
     * If on the file write operation breaked, data in file damage, this method test file
     * on readable and delete damaged files
     * @param inFuncSubDir 
     */
    private static void checkFilesOnReadable(File inFuncSubDir){
        TreeMap<Integer, File> itemsInSubDir = NcIdxFileManager.getFileListFromSubDir(inFuncSubDir);
        for(Map.Entry<Integer, File> itemFile : itemsInSubDir.entrySet()){
            if( itemFile.getValue().isDirectory() ){
                checkFilesOnReadable(itemFile.getValue());
            }
            else{
                if( !NcIdxFileManager.isDataInFileNotWrong(itemFile.getValue()) ){
                    if ( itemFile.getValue().delete() ){
                        NcAppHelper.outMessage(NcIdxFileManager.getStrCanPathFromFile(itemFile.getValue()) +
                        "_|_|_|_deleted");
                    }
                }
            }
        }
    }
    
    /**
     * Not used
     * @param inFuncNameSubDir
     * @return
     */
    private static Object getNcClassNameForSubDir(String inFuncNameSubDir){
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
     * Not used
     * @param inFuncSubDir
     */
    private static void outFilesFromSubDirToConsole(File inFuncSubDir){
        File[] inDirFiles = inFuncSubDir.listFiles();
        NcAppHelper.outMessageToConsole("");
        NcAppHelper.outMessageToConsole("Directory path: " + NcIdxFileManager.getStrCanPathFromFile(inFuncSubDir));
        NcAppHelper.outMessageToConsole("Count files in directory: " + inDirFiles.length);
        for( File itemFile : inDirFiles){
            NcAppHelper.outMessageToConsole("" + itemFile.getName());
        }
    }
    
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkInIndexFolderContent() }
     * </ul>
     */
    private static void checkTmpIDsData(){
        File fileTmpIDs = NcIdxFileManager.getTmpIdsFile();
        boolean fileGet = NcIdxFileManager.isErrorForFileOperation(fileTmpIDs);
        if( fileGet ){
            NcAppHelper.outMessage(
                NcStrLogMsgField.ERROR_CRITICAL.getStr()
                + NcStrVarDescription.TMP_IDS_FILE.getStr()
                + NcStrServiceMsg.NOT_EXIST_OR_WRONG.getStr());
        }
        if( !fileGet ){
            NcTmpNowProcessInfo readedTmpIDsData= NcIndexManageIDs.getTmpIDsData(fileTmpIDs);
            boolean isTmpIdsDataWrong = NcIndexManageIDs.isTmpIDsDataWrong(readedTmpIDsData);
            if( !isTmpIdsDataWrong ){
                getTmpIdsDataToConsole(readedTmpIDsData);
            }
            boolean isEmpty = NcIndexManageIDs.isTmpIDsDataEmpty(readedTmpIDsData);
            if( isEmpty ){
                NcAppHelper.outMessage(
                    NcStrLogMsgField.ERROR.getStr()
                    + NcStrVarDescription.TMP_IDS_FILE.getStr()
                    + NcStrServiceMsg.READED_DATA_IS_EMPTY.getStr());
            }
        }
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkTmpIDsData() }
     * </ul>
     * @param readedTmpIDsData
     */
    private static void getTmpIdsDataToConsole(NcTmpNowProcessInfo readedTmpIDsData){
        NcAppHelper.outMessageToConsole("TmpIdsData: ");
        NcAppHelper.outMessageToConsole("journalid: " + readedTmpIDsData.journalid + " \tjournalname: " + readedTmpIDsData.journalname);
        NcAppHelper.outMessageToConsole("listnameid: " + readedTmpIDsData.listnameid + " \tlistnameid: " + readedTmpIDsData.listname);
        NcAppHelper.outMessageToConsole("hashlistnnameid: " + readedTmpIDsData.hashlistnameid + " \thashlistname: " + readedTmpIDsData.hashlistname);
        NcAppHelper.outMessageToConsole("longwordlistnameid: " + readedTmpIDsData.longwordlistnameid + " \tlongwordlistname: " + readedTmpIDsData.longwordlistname);
    }

    
}
