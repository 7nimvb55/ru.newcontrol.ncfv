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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Администратор
 */
public class NcIdxFileManager {
    /**
     * 
     * @param prefixFileName
     * @param fID
     * @return 
     */    
    public static String getFileNameToRecord(String prefixFileName, long fID){
        return prefixFileName + "-" + (fID - (fID % 100) + (100 - 1));
    }
    /**
     * 
     * @param prefixFileName
     * @param fID
     * @param sID
     * @return 
     */    
    public static String getFileNameToRecordLongWord(String prefixFileName, long fID, long sID){
        return prefixFileName + "-" + fID + "-" + (sID - (sID % 100) + (100 - 1));
    }
    /**
     * 
     * @param prefixFileName
     * @param fID
     * @param sID
     * @return 
     */    
    public static String getFileNameWithTwoIDs(String prefixFileName, long fID, long sID){
        return prefixFileName + "-" + (fID - (fID % 100) + (100 - 1)) + "-" + (sID - (sID % 100) + (100 - 1));
    }
    /**
     * Check for input file, File.exist() && File.canRead() && File.canWrite() && File.isFile()  
     * @param strFIds
     * @return true if all of checked params true
     * false if one of param false
     */    
    public static boolean fileExistRWAccessChecker(File strFIds){
        try{
            if(strFIds.exists()
                    && strFIds.canRead()
                    && strFIds.canWrite()
                    && strFIds.isFile()){
                return true;
            }
        }catch(NullPointerException ex){
            return false;
        }
        return false;
    }
    /**
     * 
     * @param String inputFileName
     * @return 
     */    
    public static boolean dirOrFileExistRAccessChecker(String inputFileName){
        File strFIds = new File(inputFileName);
            try{
                if( strFIds.exists() && strFIds.canRead() ){
                    return true;
                }
            }catch(NullPointerException ex){
                return false;
            }
            return false;
    }
    /**
     * Check for input file, File.exist() && File.canRead() && File.canWrite() && File.isDirectory()  
     * @param strFIds
     * @return true if all of checked params true
     * false if one of param false
     */
    public static boolean dirExistRWAccessChecker(File strFIds){
        try{
            if(strFIds.exists()
                    && strFIds.canRead()
                    && strFIds.canWrite()
                    && strFIds.isDirectory()){
                return true;
            }
        }catch(NullPointerException ex){
            return false;
        }
        return false;
    }
    /**
     * Method for check Application path on the read and write permitions and
     * end call end of application methods if path not have need permitions
     * @return {@link java.io.file} object with appPath/etc/ncvf.conf file
     */
    public static File getOrCreateCfgFile(){
        String strCfgFile = createStrPathForCfgFile();
        File fileCfg = new File(strCfgFile);
        if( !NcIdxFileManager.fileExistRWAccessChecker(fileCfg) ){
            try {
                if( fileCfg.createNewFile() ){
                    NcPreRunFileViewer.createCfg(getStrCanPathFromFile(fileCfg));
                }
            } catch (IOException ex) {
                Logger.getLogger(NcPreRunFileViewer.class.getName()).log(Level.SEVERE, null, ex);
                NcAppHelper.appExitWithMessageFSAccess(getStrCanPathFromFile(fileCfg));
            }
        }
        return fileCfg;
    }
    private static String createStrPathForCfgFile(){
        String appPath = getAppWorkDirStrPath();
        String newSubDirEtc = strPathCombiner(appPath, "/etc");
        File dirToCfg = new File(newSubDirEtc);
        if( !NcIdxFileManager.dirExistRWAccessChecker(dirToCfg) ){
            if( !dirToCfg.mkdir() ){
                NcAppHelper.appExitWithMessageFSAccess(getStrCanPathFromFile(dirToCfg));
            }
        }
        return strPathCombiner(newSubDirEtc, "/ncfv.conf");
    }
    /**
     * Path config in serializable class for operative use
     * {@link ru.newcontrol.ncfv.NcParamFv}
     * @return
     */
    public static String getWorkCfgPath(){
        String strToReturnDataInAppDir = getOrCreateAppDataSubDir();
        String strToReturnDataInAppDirFile = strPathCombiner(strToReturnDataInAppDir, "workcfg.dat");
        return strToReturnDataInAppDirFile;
    }
    /**
     * 
     * @return 
     */
    public static String getOrCreateAppDataSubDir(){
        String strAppPath = getAppWorkDirStrPath();
        String strToReturnDataInAppDir = strPathCombiner(strAppPath, "/appdata");
        File dirForAppData = new File(strToReturnDataInAppDir);
        if( !dirExistRWAccessChecker(dirForAppData) ){
            if( !dirForAppData.mkdirs() ){
                NcAppHelper.appExitWithMessageFSAccess(getStrCanPathFromFile(dirForAppData));
            }
        }
        return getStrCanPathFromFile(dirForAppData);
    }
    /**
     * From system properties get application path, after that get parent path
     * and check permissions if it not have read and write, than call
     * {@link ru.newcontrol.ncfv.NcAppHelper#appExitWithMessageFSAccess(java.lang.String) }
     * for exit from application
     * @return
     */
    public static String getAppWorkDirStrPath(){
        String appPath = System.getProperty("java.class.path");
        File pathToApp = new File(appPath);
        File dirToApp = pathToApp.getParentFile();
        if( dirExistRWAccessChecker(dirToApp) ){
            return getStrCanPathFromFile(dirToApp);
        }
        NcAppHelper.appExitWithMessageFSAccess(getStrCanPathFromFile(dirToApp));
        return getStrCanPathFromFile(getErrorForFileOperation());
    }
    /**
     *
     * @return
     */
    public static String getUserHomeDirStrPath(){
        String appPath = System.getProperty("user.home");
        File pathToApp = new File(appPath);
        if( !dirExistRWAccessChecker(pathToApp) ){
            return getStrCanPathFromFile(getErrorForFileOperation());
        }
        return getStrCanPathFromFile(pathToApp);
    }
    /**
     *
     * @return
     */
    public static File getAppWorkDirFile(){
        String appPath = System.getProperty("java.class.path");
        File pathToApp = new File(appPath);
        if( fileExistRWAccessChecker(pathToApp) ){
            File dirToApp = pathToApp.getParentFile();
            if( !dirExistRWAccessChecker(dirToApp) ){
                return getErrorForFileOperation();
            }
            return dirToApp;
        }
        return getErrorForFileOperation();
    }

    /**
     *
     * @return
     */
    public static String getJournalDiskPath(){
        String strToReturnDataInAppDir = getOrCreateAppDataSubDir();
        String strToReturnDataInAppDirFile = strPathCombiner(strToReturnDataInAppDir, "jdisk.dat");
        return strToReturnDataInAppDirFile;
    }
    /**
     * For directory /fl, this method generated names for files with records,
     * and check for exist file in the directory, return list of exist files
     * @return TreeMap<Integer, File>
     */
    public static File getExistFilesForDirListAttrByNameGenerated(){
        TreeMap<Integer, File> indexWorkSubDirFilesList = getIndexWorkSubDirFilesList();
        long recordID = 0;
        File filePathSubDir = indexWorkSubDirFilesList.get("/fl".hashCode());
        File fileWithRecords;
        TreeMap<Long, File> listFiles = new TreeMap<Long, File>();
        do{
            String fileName = getFileNameToRecord("/dl", recordID);
            String strPathFile = strPathCombiner(getStrCanPathFromFile(filePathSubDir), fileName);
            fileWithRecords = new File(strPathFile);
            if( fileExistRWAccessChecker(fileWithRecords) ){
                listFiles.put(recordID, fileWithRecords);
            }
            recordID = recordID + 100;
        }
        while( fileExistRWAccessChecker(fileWithRecords) );
        return fileWithRecords;
    }
    /**
     * Concat strings to Path with check of end and start of concated strings
     * @param strFirst
     * @param String strFirst
     * @param strSecond
     * @param String strSecond
     * @return 
     */
    public static String strPathCombiner(String strFirst, String strSecond){
        if( (strFirst.length() < 1) && (strSecond.length() < 1) ){
            return getStrCanPathFromFile(getErrorForFileOperation());
        }
        if( strFirst.substring(strFirst.length() - 1).equalsIgnoreCase("\\")
            || strFirst.substring(strFirst.length() - 1).equalsIgnoreCase("/") ){
            if( strSecond.substring(0, 1).matches("[0-9a-zA-Z]")
                    || strSecond.substring(0, 1).equalsIgnoreCase(".") ){
                return strFirst + strSecond;
            }
        }
        if( strFirst.substring(strFirst.length() - 1).matches("[0-9a-zA-Z]")
            || strFirst.substring(strFirst.length() - 1).equalsIgnoreCase(".") ){
            if( strSecond.substring(0, 1).equalsIgnoreCase("\\")
                    || strSecond.substring(0, 1).equalsIgnoreCase("/") ){
                return strFirst + strSecond;
            }
        }
        if( strFirst.substring(strFirst.length() - 1).matches("[0-9a-zA-Z]")
            || strFirst.substring(strFirst.length() - 1).equalsIgnoreCase(".") ){
            if( strSecond.substring(0, 1).matches("[0-9a-zA-Z]")
                    || strSecond.substring(0, 1).equalsIgnoreCase(".") ){
                return strFirst + System.getProperty("file.separator") + strSecond;
            }
        }
        if( strFirst.substring(strFirst.length() - 1).equalsIgnoreCase("\\")
            || strFirst.substring(strFirst.length() - 1).equalsIgnoreCase("/") ){
            if( strSecond.substring(0, 1).equalsIgnoreCase("\\")
                || strSecond.substring(0, 1).equalsIgnoreCase("/") ){
                if( strSecond.substring(1, 2).matches("[0-9a-zA-Z]")
                    || strSecond.substring(1, 2).equalsIgnoreCase(".") ){
                    return strFirst + strSecond.substring(1);
                }
                return getStrCanPathFromFile(getErrorForFileOperation());
            }
        }
        return getStrCanPathFromFile(getErrorForFileOperation());
    }
    /**
     * Methods of this application, generate errors (exeptions) in operation with
     * objects java.io.File, for return not null value and not generated new exeption of null,
     * return value generated from method 
     * {@link ru.newcontrol.ncfv.NcIdxFileManager#getErrorForFileOperation()}
     * 
     * <p>This method check for generated error object</p>
     * @param inputFile {@link java.io.File#File(java.lang.String) java.io.File}
     * @return true for found error object
     */
    public static boolean isErrorForFileOperation(File inputFile){
        return ( getStrCanPathFromFile(inputFile).indexOf("notExistFileError") > -1 );
    }
    /**
     * Analogue for {@link ru.newcontrol.ncfv.NcIdxFileManager#isErrorForFileOperation(java.io.File)}
     * @param inputFilePath
     * @return true if input param has error of operation path
     */
    public static boolean isErrorForFileOperationByString(String inputFilePath){
        return ( inputFilePath.indexOf("notExistFileError") > -1 );
    }
    /**
     * When File operation has exception, function will return generated this
     * method, object, returned result need check with method
     * {@link #isErrorForFileOperation(java.io.File) isErrorForFileOperation(java.io.File)}
     * @return 
     */
    public static File getErrorForFileOperation(){
        NcAppHelper.outMessage("Error in file operation, NcIdxFileManager.getErrorForFileOperation() called");
        return new File("notExistFileError");
    }
    /**
     * For directory /fl, this method generated name for file contained Id
     * @param recordID
     * @return 
     */
    public static File getFileForDirListAttrContainedRecordId(long recordID){
        if( recordID < 0 ){
            return getErrorForFileOperation();
        }
        File fileWithRecords;
        TreeMap<Integer, File> indexWorkSubDirFilesList = getIndexWorkSubDirFilesList();
        File filePathSubDir = indexWorkSubDirFilesList.get("/fl".hashCode());
        String fileName = NcIdxFileManager.getFileNameToRecord("/dl", recordID);
        String strPathFile = strPathCombiner(getStrCanPathFromFile(filePathSubDir), fileName);
        fileWithRecords = new File(strPathFile);
        if( fileExistRWAccessChecker(fileWithRecords) ){
            return fileWithRecords;
        }
        return getErrorForFileOperation();
    }
    /**
     * For directory /fx, this method generated names for files with records,
     * and check for exist file in the directory, return list of exist files
     * @return TreeMap<Integer, File>
     */
    public static File getExistFilesForDirListExistByNameGenerated(){
        TreeMap<Integer, File> indexWorkSubDirFilesList = getIndexWorkSubDirFilesList();
        long recordID = 0;
        File filePathSubDir = indexWorkSubDirFilesList.get("/fx".hashCode());
        File fileWithRecords;
        TreeMap<Long, File> listFiles = new TreeMap<Long, File>();
        do{
            String fileName = NcIdxFileManager.getFileNameToRecord("/e", recordID);
            String strPathFile = strPathCombiner(getStrCanPathFromFile(filePathSubDir), fileName);
            fileWithRecords = new File(strPathFile);
            if( fileExistRWAccessChecker(fileWithRecords) ){
                listFiles.put(recordID, fileWithRecords);
            }
            recordID = recordID + 100;
        }
        while( fileExistRWAccessChecker(fileWithRecords) );
        return fileWithRecords;
    }
    /**
     * For directory /fx, this method generated name for file contained Id
     * @param recordID
     * @return 
     */
    public static File getFileForDirListExistContainedRecordId(long recordID){
        if( recordID < 0 ){
            return getErrorForFileOperation();
        }
        File fileWithRecords;
        TreeMap<Integer, File> indexWorkSubDirFilesList = getIndexWorkSubDirFilesList();
        File filePathSubDir = indexWorkSubDirFilesList.get("/fx".hashCode());
        String fileName = NcIdxFileManager.getFileNameToRecord("/e", recordID);
        String strPathFile = strPathCombiner(getStrCanPathFromFile(filePathSubDir), fileName);
        fileWithRecords = new File(strPathFile);
        if( fileExistRWAccessChecker(fileWithRecords) ){
            return fileWithRecords;
        }
        return getErrorForFileOperation();
    }
    /**
     * Get Index Path and subDirictories in the TreeMap<Integer, File> structure,
     * where index of object has value type of "/di".hashCode, "/sw".hashCode
     * about subDirectories list see:
     * <ul>
     * <li>{@link NcManageCfg#workSubDir NcManageCfg.workSubDir}
     * <li>{@link NcManageCfg#getWorkSubDirList() NcManageCfg.getWorkSubDirList()}
     * </ul>
     * @return 
     */
    public static TreeMap<Integer, File> getIndexWorkSubDirFilesList(){
        
        NcParamFv readedWorkCfg = NcParamFvReader.readDataFromWorkCfg();
        if( NcParamFvManager.isNcParamFvDataEmpty(readedWorkCfg) ){
            readedWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
        }
        boolean isNotCreate = NcParamCfgToDiskReleaser.checkOrCreateIdxDirStructure(readedWorkCfg.indexPath);
        if( !isNotCreate ){
            return new TreeMap<Integer, File>();
        }
        File fileWorkDir = new File(readedWorkCfg.indexPath);
        
        TreeMap<Integer, File> listSubDirs = new TreeMap<Integer, File>();
        listSubDirs.putAll(readedWorkCfg.tmIndexSubDirs);
        listSubDirs.put("index".hashCode(), fileWorkDir);
        return listSubDirs;
    }
    /**
     *
     * @param inFuncWorkDir
     * @return
     */
    public static TreeMap<Integer, File> getIndexSubDirectories(String inFuncWorkDir){
        TreeMap<Integer, File> listSubDirs = new TreeMap<Integer, File>();
        File fileWorkDir = new File(inFuncWorkDir);
        boolean boolResultCreation = false;
        String[] strSubDirs = NcManageCfg.getWorkSubDirList();
        
        for( String itemSubDir : strSubDirs ){
            String strPathName = NcIdxFileManager.strPathCombiner(getStrCanPathFromFile(fileWorkDir), itemSubDir);
            File fileForCreateDir = new File(strPathName);
            boolean tmpCheck = NcIdxFileManager.dirExistRWAccessChecker(fileForCreateDir);
            if( !tmpCheck ){
                NcParamCfgToDiskReleaser.createSubDir(fileWorkDir, itemSubDir);
                tmpCheck = NcIdxFileManager.dirExistRWAccessChecker(fileForCreateDir);
            }
            boolResultCreation = boolResultCreation && tmpCheck;
            if( tmpCheck ){
                listSubDirs.put(itemSubDir.hashCode(), fileForCreateDir);
            }
        }
        return listSubDirs;
    }
    /**
     * Check for parameters in list of subDir returned by
     * {@link ru.newcontrol.ncfv.NcManageCfg#getWorkSubDirList()} or
     * "index" directory appended in list by method {@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexWorkSubDirFilesList()}
     * @param inFuncName
     * @return java.io.File object or error data in this object generated by
     * {@link ru.newcontrol.ncfv.NcIdxFileManager#getErrorForFileOperation()}
     */
    public static File getIndexWorkSubDirFileByName(String inFuncName){
        TreeMap<Integer, File> indexWorkSubDirFilesList = getIndexWorkSubDirFilesList();
        if( indexWorkSubDirFilesList.isEmpty() ){
            return getErrorForFileOperation();
        }
        if( !isDirNameInList(inFuncName) ){
            return getErrorForFileOperation();
        }
        
        File filePathSubDir = indexWorkSubDirFilesList.get(inFuncName.hashCode());
        return filePathSubDir;
    }
    public static boolean isDirNameInList(String inFuncName){
        for(String itemSubDir : NcManageCfg.getWorkSubDirList()){
            if( itemSubDir.equalsIgnoreCase(inFuncName) ){
                return true;
            }
        }
        if( "index".equalsIgnoreCase(inFuncName) ){
                return true;
        }
        return false;
    }
    /**
     * Returned list of files in directory in structure TreeMap<Integer, File>
     * @param inFuncSubDir
     * @return list of files in TreeMap<Integer, File>
     */
    public static TreeMap<Integer, File> getFileListFromSubDir(File inFuncSubDir){
        TreeMap<Integer, File> itemsInSubDirs = new TreeMap<Integer, File>();
        int idx = 0;
        for(File itemFile : inFuncSubDir.listFiles()){
            itemsInSubDirs.put(idx, itemFile);
            idx++;
        }
        return itemsInSubDirs;
    }
    /**
     * Read from file with structure TreeMap<Long, ?>
     * @param inForRead
     * @return 
     */
    public static TreeMap<Long, ?> getDataFromFile(File inForRead){
        TreeMap<Long, ?> ncDataFromDirList = new TreeMap<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inForRead)))
        {
            ncDataFromDirList = (TreeMap<Long, ?>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcPreIdxWork.class.getName()).log(Level.SEVERE, null, ex);
            return new TreeMap<Long, String>();
        } 
        return ncDataFromDirList;
    }
    /**
     * Read from file with structure TreeMap<Long, ?> and return count of records
     * @param inForRead
     * @return 
     */
    public static int getCountRecordDataInFile(File inForRead){
        TreeMap<Long, ?> ncDataFromDirList = new TreeMap<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inForRead)))
        {
            ncDataFromDirList = (TreeMap<Long, ?>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcPreIdxWork.class.getName()).log(Level.SEVERE, null, ex);
            return -777;
        } 
        return ncDataFromDirList.size();
    }
    /**
     * If some files can read to TreeMap<Long, ?> structure, then return false
     * Data in file wrong or it created not this application classes
     * @param inForRead
     * @return 
     */
    public static boolean isDataInFileNotWrong(File inForRead){
        TreeMap<Long, ?> ncDataFromDirList = new TreeMap<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inForRead)))
        {
            ncDataFromDirList = (TreeMap<Long, ?>)ois.readObject();
        }
        catch(Exception ex){
            Logger.getLogger(NcIdxDirListFileReader.class.getName()).log(Level.SEVERE, null, ex); 
            return false;
        } 
        return true;
    }
    /**
     * Get File object with data for {@link ru.newcontrol.ncfv.NcIndexManageIDs} class
     * with structured by cless {@link ru.newcontrol.ncfv.NcTmpNowProcessInfo}
     * @return 
     */
    public static File getTmpIdsFile(){
        NcParamFv readedWorkCfg = NcParamFvReader.readDataFromWorkCfg();
        if( NcParamFvManager.isNcParamFvDataEmpty(readedWorkCfg) ){
            readedWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
        }
        TreeMap<Integer, File> listSubDirs = new TreeMap<Integer, File>();
        listSubDirs.putAll(readedWorkCfg.tmIndexSubDirs);
        File fileT = listSubDirs.get("/t".hashCode());
        String strFilePath = strPathCombiner(getStrCanPathFromFile(fileT), NcManageCfg.getWorkFileNames()[1]);
        File fileForTmpIds = new File(strFilePath);
        boolean boolCheck = NcIdxFileManager.fileExistRWAccessChecker(fileForTmpIds);
        if( boolCheck ){
            return fileForTmpIds;
        }
        return getErrorForFileOperation();
    }
    
    public static String getStrCanPathFromFile(File inFuncFile){
        String strCanonicalPath = "";
        
        try {
            strCanonicalPath = inFuncFile.getCanonicalPath();
        } catch (IOException ex) {
            NcAppHelper.outMessage("Can not getCanonicalPath() for: "
                    + inFuncFile.getAbsolutePath());
            NcAppHelper.outMessage(ex.getMessage());
            strCanonicalPath = getErrorForFileOperation().getAbsolutePath();
        }
        return strCanonicalPath;
    }
    

}
