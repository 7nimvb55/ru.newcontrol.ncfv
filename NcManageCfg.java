/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.ArrayList;
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
     *
     */
    public static File ncfvdi;
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
     *
     */
    public NcManageCfg() {
        
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
     *
     */
    public TreeMap<Long, NcDiskInfo> arrDiskInfo;
/**
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
 * Create index work directory
 * @param ncdiskToCreate
 * @return creted directory object type of class File
 */    
    private File mcCreateWorkDir(NcDiskInfo ncdiskToCreate) throws IOException{
        File createdDir = new File(ncdiskToCreate.diskLetter + ":" + indexPath);
        if(createdDir.mkdir()){
            return createdDir;
        }
        throw new IOException("Can not create: " + createdDir.getAbsolutePath());
    }
/**
 * @deprecated 
 * Method for existing check or create if it not exist
 */    
    private void mcCheckAndCreateFolderStructure() throws IOException{
        
        if( ( ncfvdi == null ) || ( !ncfvdi.exists() )  ){
            mcFoundCfgOnDisk();
            mcSearchOrSetWorkDir();
        }
        String strPathWorkDir = ncfvdi.getAbsolutePath();
        for(String strSubDir : workSubDir){
            File cwsd = new File(strPathWorkDir+strSubDir);
            if( ! cwsd.exists() ){
                if( !cwsd.mkdir() ){
                    throw new IOException("Can not create: " + cwsd.getAbsolutePath());
                }
            }
        }
    }
    /**
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
            Logger.getLogger(NcManageCfg.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return arrDiskInfo.size();
    }
    private String mcGetWorkCfgDirName(){
        if(ncfvdi == null){
            try {
                mcSearchOrSetWorkDir();
            } catch (IOException ex) {
                Logger.getLogger(NcManageCfg.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(ncfvdi == null){
            return null;
        }
        return ncfvdi.getAbsolutePath()+workSubDir[1];
    }
    /**
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
     * @deprecated 
     */
    public void mcUpdateCfgOnDisk(){
        arrDiskInfo = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        mcWriteDiskConfiguration();
    }
    /**
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
            Logger.getLogger(NcManageCfg.class.getName()).log(Level.SEVERE, null, ex); 
            return -1;
        } 
        return arrDiskInfo.size();
    }
 /** 
  * Checked Exist, and ready for Read, Write operations for one of work SubDirictories
     * @param subDir
     * @return 
  */
    public static boolean mcCheckRWEfSubDir(String subDir){
        if( isCfgLoadAndReady ){
                String potentialWorkPath = ncfvdi.getAbsolutePath();
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
  * @deprecated 
  * @return 
  */
    public static File getDirList(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/fl");
    }
 /** fh - contained files of Directory lists hashes information it is provided by*/
 /** class NcDirListToFilesHashes*/
 /**
  * @deprecated 
  * @return 
  */
    public static File getDirListHash(){
       return NcIdxFileManager.getIndexWorkSubDirFileByName("/fh");
    }
 /** 
  * fx - for index contained hash of path, and relesad metodth of quick serch 
  * and return information about exist records in Index
  * class NcDirListToFilesHashes
  */
 /**
  * @deprecated 
  * @return 
  */
    public static File getDirListExist(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/fx");
    }    
 /** w - contained files of information about word to be searched it is provided by*/
 /** class NcSubStringsToFilesForIndex*/
 /**
  * @deprecated 
  * @return 
  */
    public static File getDirWords(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/w");
    }
 /** sw - Storage of word, contains information about word, his heximal codes, and*/
 /** hashes of this string, provided by class NcLongWord*/
 /**
  * @deprecated 
  * @return 
  */
    public static File getDirStorageWords(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/sw");
    } 
 /** lw - contained files of File lists for long word and his ids information it*/
 /** is provided by class NcLongWord*/
  /**
  * @deprecated 
  * @return 
  */
    public static File getDirLongWordList(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/lw");
    }     
 /** ln - contained files of information about word to be searched, his name is ids*/
 /** this ids and word for it contained in directory lw in files*/
/**
  * @deprecated 
  * @return 
  */    
    public static File getDirLongWord(){
        return NcIdxFileManager.getIndexWorkSubDirFileByName("/ln");
    }
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
     *
     * @return
     */
    public static String[] getWorkSubDirList(){
        return workSubDir;
    }

    /**
     *
     * @return
     */
    public static String[] getWorkFileNames(){
        return workFileNames;
    }
}
