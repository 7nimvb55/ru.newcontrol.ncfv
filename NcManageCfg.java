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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>This class for created and manage index for search in disks FS
 * in disk, where freeSpace > 1 Gb creates folder ncfvdi E:\ncfvdi\
 * in created directory, creates subDirectories:</p>
 * <ul>
 * <li>t - contained temp files (maximum id for directorys groups, current file
 * names in process, count of streams in work and his work dirs)
 * <code>{@link #getTmpIdsFile getTmpIdsFile()}</code>
 * 
 * <li>di - contained files for Disks Information save
 * returned by <code>{@link #mcGetWorkCfgDirName mcGetWorkCfgDirName()}</code>
 * <li>j - contained files for journals
 * returned by <code> </code>
 * <li>fl - contained files of Directory lists information it is provided by
 * <code>class 
 * {@link ru.newcontrol.ncfv.NcDirListToFilesForIndex#NcDirListToFilesForIndex
 * NcDirListToFilesForIndex}</code>
 * returned by <code>{@link #getDirFilesList getDirFilesList()}</code>
 * <li>ft -  contained files of Directory lists information it is provided by
 * <code>class 
 * {@link ru.newcontrol.ncfv.NcDirListToFilesType#NcDirListToFilesType NcDirListToFilesType}</code>
 * returned by <code> </code>
 * <li>fh - contained files of Directory lists hashes information it is provided by
 * class {@link ru.newcontrol.ncfv.NcDirListToFilesHashes#NcDirListToFilesHashes NcDirListToFilesHashes}
 * returned by <code>{@link #getDirHashesList getDirHashesList()}</code>
 * <li>fx - for index contained hash of path, and relesad metodth of quick serch 
 * and return information about exist records in Index
 * returned by <code> </code>
 * <li>w - contained files of information about word to be searched it is provided by
 * <code>class {@link ru.newcontrol.ncfv.NcSubStringsToFilesForIndex#NcSubStringsToFilesForIndex
 * NcSubStringsToFilesForIndex}</code>
 * returned by <code>{@link #getDirWords getDirWords()}</code>
 * <li>sw - Storage of word, contains information about word, his heximal codes, and
 * hashes of this string, provided by <code>
 * class {@link ru.newcontrol.ncfv.NcListLongWord#NcListLongWord NcListLongWord}
 * </code>
 * returned by <code>{@link #getDirStorageWords getDirStorageWords()}</code>
 * 
 * >>> Here need recode
 * 
 * <li>lw - contained files two types, of Files with lists for long word and his ids information it
 * is provided by <code>class {@link ru.newcontrol.ncfv.NcListLongWord#NcListLongWord
 * NcListLongWord}</code> and Files, contained information about IDs (by Directory List), position,
 * length it is provided by
 * <code>class {@link ru.newcontrol.ncfv.NcSubStringsToFilesForIndex#NcSubStringsToFilesForIndex
 * NcSubStringsToFilesForIndex}</code>
 * returned by <code>{@link #getDirLongWordList getDirLongWordList()}</code>
 * 
 * >>> Here need recode
 * 
 * <li>ln - contained files of information about word to be searched, his name is ids
 * this ids and word for it contained in directory lw in files
 * returned by <code> </code>
 * </ul> 
 * <p>fl, fh, lw - lists directory contained names for numeric last of ids groups,
 * where ids group maximum size is 100 records (ids 0-99, 100-199, 200-299...)</p>
 * 
 * @author Administrators from http://newcontrol.ru
 * @since ru.newcontrol.ncfv 1.0
 */
public class NcManageCfg {
    private static String indexPath;

    /**
     * Used in 
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIMinFS#NcIMinFS() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#NcManageCfg() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcCheckAndCreateFolderStructure() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcGetWorkCfgDirName() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcCheckRWEfSubDir(java.lang.String) }
     * </ul>
     */
    protected static File ncfvdi;
    private static long workDiskIdx;
    private static int intIterationCreateDir;
    private static boolean isCfgLoadAndReady = false;
    private static final String[] workSubDir = {
        "/t",
        "/di",
        "/j",
        "/fl",
        "/ft",
        "/fh",
        "/fx",
        "/w",
        "/sw",
        "/lw",
        "/ln",
    };
    private static final String[] workFileNames = {
        "/disks.dat",
        "/ids.dat"
    };

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIMinFS#NcIMinFS() }
     * </ul>
     */
    protected NcManageCfg() {
        
        NcParamFv readedWorkCfg = NcParamFvReader.readDataFromWorkCfg();
        if( NcParamFvManager.isNcParamFvDataEmpty(readedWorkCfg) ){
            readedWorkCfg = NcPreRunFileViewer.getCurrentWorkCfg();
        }
        indexPath = readedWorkCfg.indexPath;
        ncfvdi = new File(readedWorkCfg.indexPath);
        arrDiskInfo = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        isCfgLoadAndReady = mcLoadCfgFormDiskOrCreate();
        
    }
    
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIMinFS#getDiskCount() }
     * <li>{@link ru.newcontrol.ncfv.NcIMinFS#getDiskInfo() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#NcManageCfg() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcGetMaxFreeSpace() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcWriteDiskConfiguration() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcUpdateCfgOnDisk() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcReadDiskConfiguration() }
     * </ul>
     */
    protected TreeMap<Long, NcDiskInfo> arrDiskInfo;
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcCheckAndCreateFolderStructure() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcGetWorkCfgDirName() }
     * </ul>
     * @deprecated 
     * Search index work folder
     */    
    private void mcSearchOrSetWorkDir() throws IOException{
        NcParamCfgToDiskReleaser.checkOrCreateIdxDirStructure(indexPath);
        /*if(intIterationCreateDir < 5){
            intIterationCreateDir++;
        }
        else{
            throw new IOException("Create work folders out of legal 5 counts");
        }
        if( arrDiskInfo == null ){
            arrDiskInfo = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        }
        if( arrDiskInfo != null ){
            for ( Map.Entry<Long, NcDiskInfo> nccd : arrDiskInfo.entrySet() ){
                File ncfdir = new File(nccd.getValue().diskLetter + ":" + indexPath);
                if( ncfdir.exists() ){
                    ncfvdi = ncfdir;
                }
            }
            if( ncfvdi == null ){
                long ncidx = mcGetMaxFreeSpace();
                ncfvdi = mcCreateWorkDir(arrDiskInfo.get(ncidx));
                mcCheckAndCreateFolderStructure();
                mcWriteDiskConfiguration();
            }
        }*/
    }
    /**
     * Not used
     * Find disk with maximus avalable space for make index work directory
     * @return index of record in class NcDiskInfo
     */    
    private long mcGetMaxFreeSpace(){
        long tmpFreeSpace = 0;
        if( arrDiskInfo != null){
            for ( Map.Entry<Long, NcDiskInfo> nccd: arrDiskInfo.entrySet() ){
                if( !nccd.getValue().isReadonly ){
                    if(tmpFreeSpace < nccd.getValue().availSpace){
                        tmpFreeSpace = nccd.getValue().availSpace;
                        workDiskIdx = nccd.getKey();
                    }
                }
            }
        }
        else{
            return -1;
        }
        return workDiskIdx;
    }
    /**
     * Not used
     * Create index work directory
     * @param ncdiskToCreate
     * @return creted directory object type of class File
     */    
    private File mcCreateWorkDir(NcDiskInfo ncdiskToCreate) throws IOException{
        File createdDir = new File(ncdiskToCreate.diskLetter + ":" + indexPath);
        if( createdDir.mkdir() ){
            return createdDir;
        }
        String strMsg = NcStrServiceMsg.ERROR_NOT_CREATE.getStr() + NcIdxFileManager.getStrCanPathFromFile(createdDir);
        NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
            + strMsg);
        throw new IOException(strMsg);
    }
    /**
     * Not used
     * @deprecated 
     * Method for existing check or create if it not exist
     */    
    private void mcCheckAndCreateFolderStructure() throws IOException{
        
        if( ( ncfvdi == null ) || ( !ncfvdi.exists() )  ){
            mcFoundCfgOnDisk();
            mcSearchOrSetWorkDir();
        }
        String strPathWorkDir = NcIdxFileManager.getStrCanPathFromFile(ncfvdi);
        for(String strSubDir : workSubDir){
            File fileWorkSubDir = new File(strPathWorkDir+strSubDir);
            if( !fileWorkSubDir.exists() ){
                if( !fileWorkSubDir.mkdir() ){
                    String strMsg = NcStrServiceMsg.ERROR_NOT_CREATE.getStr() + NcIdxFileManager.getStrCanPathFromFile(fileWorkSubDir);
                    NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
                    + strMsg);
                    throw new IOException(strMsg);
                }
            }
        }
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcUpdateCfgOnDisk() }
     * </ul>
     * For delete method 
     * @deprecated 
     * @return 
     */
    private int mcWriteDiskConfiguration(){
        if( arrDiskInfo == null ){
            return -1;
        }
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(mcGetWorkCfgDirName() + workFileNames[0])))
        {
            oos.writeObject(arrDiskInfo);
        }
        catch(Exception ex){
            NcAppHelper.logException(
                NcManageCfg.class.getCanonicalName(), ex);
            return -1;
        } 
        return arrDiskInfo.size();
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcWriteDiskConfiguration() }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcReadDiskConfiguration() }
     * </ul>
     * @return 
     */
    private String mcGetWorkCfgDirName(){
        if(ncfvdi == null){
            try {
                mcSearchOrSetWorkDir();
            } catch (IOException ex) {
                NcAppHelper.logException(
                    NcManageCfg.class.getCanonicalName(), ex);
            }
        }
        if(ncfvdi == null){
            return NcIdxFileManager.getStrCanPathFromFile(NcIdxFileManager.getErrorForFileOperation());
        }
        return NcIdxFileManager.strPathCombiner(NcIdxFileManager.getStrCanPathFromFile(ncfvdi), workSubDir[1]);
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#NcManageCfg() }
     * </ul>
     * @deprecated 
     * @return 
     */
    private boolean mcLoadCfgFormDiskOrCreate(){
        /*mcFoundCfgOnDisk();
        if( ( workDiskIdx > -1 ) && ( arrDiskInfo.size() > 0 ) && ( ncfvdi != null ) ){
            return true;
        }
        return false;*/
        return true;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcCheckAndCreateFolderStructure() }
     * </ul>
     * @deprecated 
     * Method found on disks
     */    
    private void mcFoundCfgOnDisk(){
        /*long mcCfgLastModified = 0;
        FileSystem mcfs = FileSystems.getDefault();
        for (FileStore store: mcfs.getFileStores()) {
            if( !store.isReadOnly() ){
                char cLet = store.toString().charAt(store.toString().indexOf(':')-1);
                String potentialWorkPath = cLet + ":" + indexPath;
                File mcWorkDir = new File(potentialWorkPath);
                if(mcWorkDir.exists()
                        && mcWorkDir.isDirectory()
                        && mcWorkDir.isAbsolute()
                        && mcWorkDir.canWrite()
                        && mcWorkDir.canRead()){
                    if(mcCfgLastModified < mcWorkDir.lastModified()){
                        mcCfgLastModified = mcWorkDir.lastModified();
                        ncfvdi = mcWorkDir;
                    }
                }                
            }
        }*/
        //mcReadDiskConfiguration();
    }
    /**
     * Not used
     * @deprecated 
     */
    private void mcUpdateCfgOnDisk(){
        arrDiskInfo = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        mcWriteDiskConfiguration();
    }
    /**
     * Not used
     * @deprecated 
     * @return 
     */
    private int mcReadDiskConfiguration(){
        
        String strCfgPath = mcGetWorkCfgDirName() + workFileNames[0];
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(strCfgPath)))
        {
            arrDiskInfo = (TreeMap<Long, NcDiskInfo>)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                NcManageCfg.class.getCanonicalName(), ex);
            return -1;
        } 
        return arrDiskInfo.size();
    }
    /** 
     * Not used
     * Checked Exist, and ready for Read, Write operations for one of work SubDirictories
     * @param subDir
     * @return 
     */
    private static boolean mcCheckRWEfSubDir(String subDir){
        if( isCfgLoadAndReady ){
                String potentialWorkPath = NcIdxFileManager.getStrCanPathFromFile(ncfvdi);
                boolean ifInArray = false;
                for( String strDir : workSubDir ){
                    if( subDir.equalsIgnoreCase(strDir) ){
                        ifInArray = true;
                    }
                }
                if( !ifInArray ){
                    return false;
                }        
               
                potentialWorkPath = potentialWorkPath + subDir;
                File mcWorkDir = new File(potentialWorkPath);
                if(mcWorkDir.exists()
                        && mcWorkDir.isDirectory()
                        && mcWorkDir.isAbsolute()
                        && mcWorkDir.canWrite()
                        && mcWorkDir.canRead()){
                        return true;
                }
        }                
        return false;    
    }
 /**
  * @return 
  * @deprecated 
  * t - contained temp files (maximum id for directorys groups, current file
  * names in process, count of streams in work and his work dirs)*/
    /*public static File getTmpIdsFile(){
        if( mcCheckRWEfSubDir(workSubDir[0]) ){
            String potentialWorkPath = ncfvdi.getAbsolutePath();
            potentialWorkPath = potentialWorkPath + workSubDir[0] + workFileNames[1];
            File mcTmpCfgFile = new File(potentialWorkPath);
            return mcTmpCfgFile;
        }
        return null;
    }*/
    /*public static File getTmpIdsFile(){
        
        return NcIdxFileManager.getTmpIdsFile();
    }*/
    /*public static File getTmpSubDir(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/t");
    }*/
 /** di - contained files for Disks Information save*/
 /** j - contained files for journals*/
 /** fl - contained files of Directory lists information it is provided by*/
 /** class NcDirListToFilesForIndex*/
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListFileReader#ncReadFromDirListFile(long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListFileWriter#ncWriteToDirListFile(java.util.TreeMap, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListManager#putToDirectoryList(ru.newcontrol.ncfv.NcDcIdxDirListToFileAttr) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListManager#getByListIDs(java.util.TreeMap) }
     * </ul>
     * @deprecated 
     * @return 
     */
    protected static File getDirList(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/fl");
    }
 /** fh - contained files of Directory lists hashes information it is provided by*/
 /** class NcDirListToFilesHashes*/
    /**
     * Not used
     * @deprecated 
     * @return 
     */
    private static File getDirListHash(){
       return NcIdxFileManager.getIndexWorkSubDirFileByName("/fh");
    }
 /** 
  * fx - for index contained hash of path, and relesad metodth of quick serch 
  * and return information about exist records in Index
  * class NcDirListToFilesHashes
  */
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListFileReader#ncReadFromDirListExist(long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxDirListFileWriter#ncWriteToDirListExist(java.util.TreeMap, long) }
     * </ul>
     * @deprecated 
     * @return 
     */
    protected static File getDirListExist(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/fx");
    }    
 /** w - contained files of information about word to be searched it is provided by*/
 /** class NcSubStringsToFilesForIndex*/
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordFileReader#ncReadFromWord(java.lang.String, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordFileWriter#ncWriteForWord(java.util.TreeMap, java.lang.String, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#putWord(java.util.TreeMap) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxWordManager#getWord(java.util.TreeMap) }
     * </ul>
     * @deprecated 
     * @return 
     */
    protected static File getDirWords(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/w");
    }
 /** sw - Storage of word, contains information about word, his heximal codes, and*/
 /** hashes of this string, provided by class NcLongWord*/
    /**
     * Not used
     * @deprecated 
     * @return 
     */
    private static File getDirStorageWords(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/sw");
    } 
 /** lw - contained files of File lists for long word and his ids information it*/
 /** is provided by class NcLongWord*/
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordListFileReader#ncReadFileContainedId(ru.newcontrol.ncfv.NcDcIdxLongWordListToFile, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordListFileWriter#ncWriteData(java.util.TreeMap, ru.newcontrol.ncfv.NcDcIdxLongWordListToFile, long) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordListManager#getOrCreateLongWordID(ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordManager#getForSearchLongWordID(ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * </ul>
     * @deprecated 
     * @return 
     */
    protected static File getDirLongWordList(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/lw");
    }     
 /** ln - contained files of information about word to be searched, his name is ids*/
 /** this ids and word for it contained in directory lw in files*/
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordManager#putLongWordInFile(java.util.TreeMap, ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * <li>{@link ru.newcontrol.ncfv.NcIdxLongWordManager#getLongWordFromFile(java.util.TreeMap, ru.newcontrol.ncfv.NcDcIdxLongWordListToFile) }
     * </ul>
     * @deprecated 
     * @return 
     */    
    protected static File getDirLongWord(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/ln");
    }
    /**
     * Not used
     * @param strFIds
     * @return 
     */
    private boolean fileExistRWAccessChecker(File strFIds){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcParamCfgToDiskReleaser#checkOrCreateIdxDirStructure(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcParamCfgToDiskReleaser#getIdxDirStructure(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#outToConsoleIdxDirs() }
     * </ul>
     * @return
     */
    protected static String[] getWorkSubDirList(){
        return workSubDir;
    }

    /**     
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcIdxFileManager#getTmpIdsFile() }
     * </ul>
     * @return
     */
    protected static String[] getWorkFileNames(){
        return workFileNames;
    }
}
