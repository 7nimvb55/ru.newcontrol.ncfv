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
     * Used in:
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListFileReader#ncReadFromDirListFile(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListFileReader#ncReadFromDirListExist(long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListFileWriter#ncWriteToDirListFile(java.util.TreeMap, long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListFileWriter#ncWriteToDirListExist(java.util.TreeMap, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListAttrByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListAttrContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListExistByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListExistContainedRecordId(long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordListFileReader#ncReadFileContainedId(ru.newcontrol.ncfv.NcDcIdxLongWordListToFile, long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordListFileWriter#ncWriteData(java.util.TreeMap, ru.newcontrol.ncfv.NcDcIdxLongWordListToFile, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordListManager#getOrCreateLongWordID(ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordManager#getForSearchLongWordID(ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordFileReader#ncReadFromWord(java.lang.String, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordFileWriter#ncWriteForWord(java.util.TreeMap, java.lang.String, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#putWord(java.util.TreeMap) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#getWord(java.util.TreeMap) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#getWordExistFile(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcTypeOfWord#getStorageWordExistFileName(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcTypeOfWord#getStorageWordByIdFileName(java.lang.String, java.lang.String, long) }
     * </ul>
     * @param prefixFileName
     * @param fID
     * @return 
     */    
    protected static String getFileNameToRecord(String prefixFileName, long fID){
        return prefixFileName + "-" + (fID - (fID % 100) + (100 - 1));
    }
    /**
     Used in:
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordManager#putLongWordInFile(java.util.TreeMap, ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordManager#getLongWordFromFile(java.util.TreeMap, ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * </ul>
     * @param prefixFileName
     * @param fID
     * @param sID
     * @return 
     */    
    protected static String getFileNameToRecordLongWord(String prefixFileName, long fID, long sID){
        return prefixFileName + "-" + fID + "-" + (sID - (sID % 100) + (100 - 1));
    }
    /**
     * Not used
     * @param prefixFileName
     * @param fID
     * @param sID
     * @return 
     */    
    private static String getFileNameWithTwoIDs(String prefixFileName, long fID, long sID){
        return prefixFileName + "-" + (fID - (fID % 100) + (100 - 1)) + "-" + (sID - (sID % 100) + (100 - 1));
    }
    /**
     * Check for input file, File.exist() && File.canRead() 
     * && File.canWrite() && File.isFile()
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getOrCreateCfgFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getAppWorkDirFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListAttrByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListAttrContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListExistByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListExistContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getTmpIdsFile() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxStorageWordFileReader#ncReadFileContainedId(java.io.File) }
     * </ul>
     * @param strFIds
     * @return true if all of checked params true
     * false if one of param false
     */    
    protected static boolean fileExistRWAccessChecker(File strFIds){
        try{
            if(strFIds.exists()
                    && strFIds.canRead()
                    && strFIds.canWrite()
                    && strFIds.isFile()){
                return true;
            }
        }catch(NullPointerException ex){
            NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
            return false;
        }
        return false;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     * @param String inputFileName
     * @return 
     */    
    protected static boolean dirOrFileExistRAccessChecker(String inputFileName){
        File strFIds = new File(inputFileName);
        try{
            if( strFIds.exists() && strFIds.canRead() ){
                return true;
            }
        }catch(NullPointerException ex){
            NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
            return false;
        }
        return false;
    }
    /**
     * Check for input file, File.exist() && File.canRead() && File.canWrite() 
     * && File.isDirectory()
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#createStrPathForCfgFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getOrCreateAppDataSubDir() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getAppWorkDirStrPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getUserHomeDirStrPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getAppWorkDirFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexSubDirectories(java.lang.String) }
     * </ul>
     * @param strFIds
     * @return true if all of checked params true
     * false if one of param false
     */
    protected static boolean dirExistRWAccessChecker(File strFIds){
        try{
            if(strFIds.exists()
                    && strFIds.canRead()
                    && strFIds.canWrite()
                    && strFIds.isDirectory()){
                return true;
            }
        }catch(NullPointerException ex){
            NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
            return false;
        }
        return false;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getCurrentWorkCfg() }
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getUpdatedAppCfg() }
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getReadedLinesFromEtcCfg() }
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#updateEtcCfg(java.util.ArrayList) }
     * </ul>
     * Method for check Application path on the read and write permitions and
     * end call end of application methods if path not have need permitions
     * @return {@link java.io.file} object with appPath/etc/ncvf.conf file
     */
    protected static File getOrCreateCfgFile(){
        String strCfgFile = createStrPathForCfgFile();
        File fileCfg = new File(strCfgFile);
        if( !NcIdxFileManager.fileExistRWAccessChecker(fileCfg) ){
            try {
                if( fileCfg.createNewFile() ){
                    NcPreRunFileViewer.createCfg(getStrCanPathFromFile(fileCfg));
                }
            } catch (IOException ex) {
                NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
                NcAppHelper.appExitWithMessageFSAccess(getStrCanPathFromFile(fileCfg));
            }
        }
        return fileCfg;
    }
    /**
     * Used in {@link ru.newcontrol.ncfv.NcIdxFileManager#getOrCreateCfgFile() }
     * @return 
     */
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcParamFvReader#readDataFromWorkCfg() }
     * <li>{@link ru.newcontrol.ncfv.NcParamFvWriter#wirteDataInWorkCfg(ru.newcontrol.ncfv.NcParamFv) }
     * </ul>
     * Path config in serializable class for operative use
     * {@link ru.newcontrol.ncfv.NcParamFv}
     * @return
     */
    protected static String getWorkCfgPath(){
        String strToReturnDataInAppDir = getOrCreateAppDataSubDir();
        String strToReturnDataInAppDirFile = strPathCombiner(strToReturnDataInAppDir, "workcfg.dat");
        return strToReturnDataInAppDirFile;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getWorkCfgPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getJournalDiskPath() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcLogFileManager#getLogFile() }
     * </ul>
     * @return 
     */
    protected static String getOrCreateAppDataSubDir(){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#createStrPathForCfgFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getOrCreateAppDataSubDir() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAddPrefixWorkAppDir(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilterForDefault(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getDefaultCfgValues() }
     * </ul>
     * From system properties get application path, after that get parent path
     * and check permissions if it not have read and write, than call
     * {@link ru.newcontrol.ncfv.NcAppHelper#appExitWithMessageFSAccess(java.lang.String) }
     * for exit from application
     * @return
     */
    protected static String getAppWorkDirStrPath(){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getDefaultCfgValues() }
     * </ul>
     * @return
     */
    protected static String getUserHomeDirStrPath(){
        String appPath = System.getProperty("user.home");
        File pathToApp = new File(appPath);
        if( !dirExistRWAccessChecker(pathToApp) ){
            return getStrCanPathFromFile(getErrorForFileOperation());
        }
        return getStrCanPathFromFile(pathToApp);
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAddPrefixWorkAppRoot(java.lang.String) }
     * </ul>
     * @return
     */
    protected static File getAppWorkDirFile(){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcParamJournalDisk#fileJournalDiskExist() }
     * <li>{@link ru.newcontrol.ncfv.NcParamJournalDisk#fileJournalDiskWrite(java.util.TreeMap) }
     * <li>{@link ru.newcontrol.ncfv.NcParamJournalDisk#fileJournalDiskRead() }
     * </ul>
     * @return
     */
    protected static String getJournalDiskPath(){
        String strToReturnDataInAppDir = getOrCreateAppDataSubDir();
        String strToReturnDataInAppDirFile = strPathCombiner(strToReturnDataInAppDir, "jdisk.dat");
        return strToReturnDataInAppDirFile;
    }
    /**
     * Not used
     * For directory /fl, this method generated names for files with records,
     * and check for exist file in the directory, return list of exist files
     * @return TreeMap<Integer, File>
     */
    private static File getExistFilesForDirListAttrByNameGenerated(){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#createStrPathForCfgFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getWorkCfgPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getOrCreateAppDataSubDir() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getJournalDiskPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListAttrByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListAttrContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListExistByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListExistContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexSubDirectories(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getTmpIdsFile() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#getWordExistFile(java.lang.String) }
     * <li>
     * <li><li>{@link ru.newcontrol.ncfv.NcLogFileManager#getLogFile() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcGetWorkCfgDirName() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcParamCfgToDiskReleaser#createSubDir(java.io.File, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcParamCfgToDiskReleaser#getIdxDirStructure(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAddPrefixWorkAppDir(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAddPrefixWorkAppRoot(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAddPrefixMaxFreeSpaceRoot(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilterForDefault(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#getDefaultCfgValues() }
     * <li>{@link ru.newcontrol.ncfv.NcTypeOfWord#getStorageWordExistFileName(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcTypeOfWord#getStorageWordByIdFileName(java.lang.String, java.lang.String, long) }
     * </ul>
     * Concat strings to Path with check of end and start of concated strings
     * @param strFirst
     * @param strSecond
     * @return 
     */
    protected static String strPathCombiner(String strFirst, String strSecond){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIndexManageIDs#checkDataForAllDirListFiles() }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotEqualsRecordDirListAttrVsExist() }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkTmpIDsData() }
     * </ul>
     * Methods of this application, generate errors (exeptions) in operation with
     * objects java.io.File, for return not null value and not generated new exeption of null,
     * return value generated from method 
     * {@link ru.newcontrol.ncfv.NcIdxFileManager#getErrorForFileOperation()}
     * 
     * <p>This method check for generated error object</p>
     * @param inputFile {@link java.io.File#File(java.lang.String) java.io.File}
     * @return true for found error object
     */
    protected static boolean isErrorForFileOperation(File inputFile){
        return ( getStrCanPathFromFile(inputFile).contains(NcStrServiceMsg.ERROR_FILE_NOT_EXIST.getStr()) );
    }
    /**
     * Not used
     * Analogue for {@link ru.newcontrol.ncfv.NcIdxFileManager#isErrorForFileOperation(java.io.File)}
     * @param inputFilePath
     * @return true if input param has error of operation path
     */
    private static boolean isErrorForFileOperationByString(String inputFilePath){
        return ( inputFilePath.contains(NcStrServiceMsg.ERROR_FILE_NOT_EXIST.getStr()) );
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getAppWorkDirStrPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getUserHomeDirStrPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getAppWorkDirFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#strPathCombiner(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListAttrContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListExistContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexWorkSubDirFileByName(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getTmpIdsFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getStrCanPathFromFile(java.io.File) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcGetWorkCfgDirName() }
     * </ul>
     * When File operation has exception, function will return generated this
     * method, object, returned result need check with method
     * {@link #isErrorForFileOperation(java.io.File) isErrorForFileOperation(java.io.File)}
     * @return 
     */
    protected static File getErrorForFileOperation(){
        NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
            + NcStrLogMsgText.CALLED_ERROR_FOR_FILE_OPERATION.getStr());
        return new File(NcStrServiceMsg.ERROR_FILE_NOT_EXIST.getStr());
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotEqualsRecordDirListAttrVsExist() }
     * </ul> 
     * For directory /fl, this method generated name for file contained Id
     * @param recordID
     * @return 
     */
    protected static File getFileForDirListAttrContainedRecordId(long recordID){
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
     * Not used
     * For directory /fx, this method generated names for files with records,
     * and check for exist file in the directory, return list of exist files
     * @return TreeMap<Integer, File>
     */
    private static File getExistFilesForDirListExistByNameGenerated(){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotEqualsRecordDirListAttrVsExist() }
     * </ul> 
     * For directory /fx, this method generated name for file contained Id
     * @param recordID
     * @return 
     */
    protected static File getFileForDirListExistContainedRecordId(long recordID){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListAttrByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListAttrContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListExistByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListExistContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexWorkSubDirFileByName(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#getWordExistFile(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIndexManageIDs#checkDataForAllDirListFiles() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#outToConsoleIdxDirs() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcTypeOfWord#getStorageWordExistFileName(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcTypeOfWord#getStorageWordByIdFileName(java.lang.String, java.lang.String, long) 
     * </ul> 
     * Get Index Path and subDirictories in the TreeMap<Integer, File> structure,
     * where index of object has value type of "/di".hashCode, "/sw".hashCode
     * about subDirectories list see:
     * <ul>
     * <li>{@link NcManageCfg#workSubDir NcManageCfg.workSubDir}
     * <li>{@link NcManageCfg#getWorkSubDirList() NcManageCfg.getWorkSubDirList()}
     * </ul>
     * @return 
     */
    protected static TreeMap<Integer, File> getIndexWorkSubDirFilesList(){
        
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#validateAndApplyCfg(ru.newcontrol.ncfv.NcParamFv) }
     * </ul>
     * function equals for {@link #getIndexWorkSubDirFilesList() }
     * only not add in list "index"
     * @param inFuncWorkDir
     * @return
     */
    protected static TreeMap<Integer, File> getIndexSubDirectories(String inFuncWorkDir){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#getDirList() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#getDirListHash() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#getDirListExist() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#getDirWords() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#getDirStorageWords() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#getDirLongWordList() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#getDirLongWord() }
     * </ul>
     * Check for parameters in list of subDir returned by
     * {@link ru.newcontrol.ncfv.NcManageCfg#getWorkSubDirList()} or
     * "index" directory appended in list by method {@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexWorkSubDirFilesList()}
     * @param inFuncName
     * @return java.io.File object or error data in this object generated by
     * {@link ru.newcontrol.ncfv.NcIdxFileManager#getErrorForFileOperation()}
     */
    protected static File getIndexWorkSubDirFileByName(String inFuncName){
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
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexWorkSubDirFileByName(java.lang.String) }
     * </ul>
     * @param inFuncName
     * @return 
     */
    private static boolean isDirNameInList(String inFuncName){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIndexManageIDs#checkDataForAllDirListFiles() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotFullFiles(java.io.File) }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkFilesOnReadable(java.io.File) }
     * </ul>
     * Returned list of files in directory in structure TreeMap<Integer, File>
     * @param inFuncSubDir
     * @return list of files in TreeMap<Integer, File>
     */
    protected static TreeMap<Integer, File> getFileListFromSubDir(File inFuncSubDir){
        TreeMap<Integer, File> itemsInSubDirs = new TreeMap<Integer, File>();
        int idx = 0;
        for(File itemFile : inFuncSubDir.listFiles()){
            itemsInSubDirs.put(idx, itemFile);
            idx++;
        }
        return itemsInSubDirs;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotEqualsRecordDirListAttrVsExist() }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotFinishedAppendToIndex(java.util.TreeMap, java.util.TreeMap) }
     * </ul>
     * Read from file with structure TreeMap<Long, ?>
     * @param inForRead
     * @return 
     */
    protected static TreeMap<Long, ?> getDataFromFile(File inForRead){
        TreeMap<Long, ?> ncDataFromDirList = new TreeMap<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inForRead)))
        {
            ncDataFromDirList = (TreeMap<Long, ?>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
            return new TreeMap<Long, String>();
        } 
        return ncDataFromDirList;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotFullFiles(java.io.File) }
     * </ul>
     * Read from file with structure TreeMap<Long, ?> and return count of records
     * @param inForRead
     * @return 
     */
    protected static int getCountRecordDataInFile(File inForRead){
        TreeMap<Long, ?> ncDataFromDirList = new TreeMap<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inForRead)))
        {
            ncDataFromDirList = (TreeMap<Long, ?>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
            return -777;
        } 
        return ncDataFromDirList.size();
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkFilesOnReadable(java.io.File) }
     * </ul>
     * If some files can read to TreeMap<Long, ?> structure, then return false
     * Data in file wrong or it created not this application classes
     * @param inForRead
     * @return 
     */
    protected static boolean isDataInFileNotWrong(File inForRead){
        TreeMap<Long, ?> ncDataFromDirList = new TreeMap<>();
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inForRead)))
        {
            ncDataFromDirList = (TreeMap<Long, ?>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
            return false;
        } 
        return true;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIndexManageIDs#writeTmpIDs(ru.newcontrol.ncfv.NcTmpNowProcessInfo) }
     * <li>{@link ru.newcontrol.ncfv.NcIndexManageIDs#readTmpIds() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkTmpIDsData() }
     * </ul>
     * Get File object with data for {@link ru.newcontrol.ncfv.NcIndexManageIDs} class
     * with structured by cless {@link ru.newcontrol.ncfv.NcTmpNowProcessInfo}
     * @return 
     */
    protected static File getTmpIdsFile(){
        NcParamFv readedWorkCfg = NcParamFvReader.readDataFromWorkCfg();
        if( NcParamFvManager.isNcParamFvDataEmpty(readedWorkCfg) ){
            readedWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
        }
        TreeMap<Integer, File> listSubDirs = new TreeMap<Integer, File>();
        listSubDirs.putAll(readedWorkCfg.tmIndexSubDirs);
        File fileT = listSubDirs.get("/t".hashCode());
        String strFilePath = strPathCombiner(getStrCanPathFromFile(fileT), NcStrFileDir.FILE_ID_DATA.getStr());
        File fileForTmpIds = new File(strFilePath);
        return fileForTmpIds;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getOrCreateCfgFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#createStrPathForCfgFile() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getOrCreateAppDataSubDir() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getAppWorkDirStrPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getUserHomeDirStrPath() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListAttrByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#strPathCombiner(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#isErrorForFileOperation(java.io.File) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListAttrContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getExistFilesForDirListExistByNameGenerated() }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getFileForDirListExistContainedRecordId(long) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getIndexSubDirectories(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getTmpIdsFile() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#outToConsoleIdxDirs() }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotEqualsRecordDirListAttrVsExist() }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#getNotFullFiles(java.io.File) }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkFilesOnReadable(java.io.File) }
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#outFilesFromSubDirToConsole(java.io.File) }
     * </ul>
     * @param inFuncFile
     * @return 
     */
    protected static String getStrCanPathFromFile(File inFuncFile){
        String strCanonicalPath = "";
        try {
            strCanonicalPath = inFuncFile.getCanonicalPath();
        } catch (IOException ex) {
            NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
                + NcStrServiceMsg.ERROR_FILE_NOT_CANONICAL_PATH.getStr()
                + inFuncFile.getAbsolutePath());
            NcAppHelper.logException(
                    NcIdxFileManager.class.getCanonicalName(), ex);
            strCanonicalPath = getErrorForFileOperation().getAbsolutePath();
        }
        return strCanonicalPath;
    }
    

}
