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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Администратор
 */
public class NcIndexManageIDs {
    private NcManageCfg ncThisMcCfg;
    /** Work directory object*/
    private File workDir;
    /** Data about count of IDs in Indexes readed from saved on Disk tempFile*/
    //private NcTmpNowProcessInfo idsReadedData;
    /** Data about count of IDs in Indexes ready to save on Disk tempFile*/
    private NcTmpNowProcessInfo idsToWriteData;
    /** Flag set to true if data about IDs changed and ready to save */
    private boolean idsReadyToWrite;
    /** Flag set to true when method checkDataForAllDirListFiles() finished
     * check for All work dirictories, if dirictories changed in change functions,
     * need set this flag in false
     */
    private boolean isDataOnDiskChecked;    
    /** Flag set to true when IDs data read form IDs temp file or repair from
     * work dirictories or generated Zeros
     */
    private boolean existLastIDs;
    /** Data about count of IDs in Indexes calculate from Files in work Dirictories*/
    private NcTmpNowProcessInfo idsInDirData;
    /** Data readed from maximus Directory List File */
    private TreeMap<Long, NcDcIdxDirListToFileAttr> lastRecDataDFL;
    /** Data readed from maximus Directory List Hashes File */
    private ArrayList<NcDcIdxDirListToFileHash> lastRecDataDFHL;
    /** Data readed from maximus Directory List Long Word File */
    private ArrayList<NcDcIdxLongWordListToFile> lastRecDataDLWL;

    /**
     *
     * @param ncThisMcCfg
     */
    public NcIndexManageIDs(NcManageCfg ncThisMcCfg) {
       this.ncThisMcCfg = ncThisMcCfg;
       createDirListFile();
    }

    /**
     *
     * @return
     */
    public boolean getLoadLastIDsStatus(){
        return this.existLastIDs;
    }
    /**
 * Create Directory List File
 * Generate and manage File names, check existing files
 */
    private void createDirListFile(){
        /** Read last IDs data form Disk, if record not found,
         * attemt to check folders and repair data or write Zero data in file
         */
        int retWriteTmpIDs = -1;
        NcTmpNowProcessInfo retReadTmpIDs = readTmpIds();
        if( isTmpIDsDataEmpty(retReadTmpIDs) ){
            retReadTmpIDs = checkDataForAllDirListFiles();
            if( isTmpIDsDataEmpty(retReadTmpIDs) ){
                retReadTmpIDs = 
                    new NcTmpNowProcessInfo("",
                        -1,
                        "",
                        -1,
                        "",
                        0,
                        "",
                        -1);
            }
            retWriteTmpIDs = writeTmpIDs(retReadTmpIDs);
        }
        
    }

    /**
     *
     * @return
     */
    public NcTmpNowProcessInfo getIdsReadedData(){
        return readTmpIds();
    }

    /**
     *
     * @param fIdsToWrite
     * @return
     */
    public int setNewIdsData(NcTmpNowProcessInfo fIdsToWrite){
        int retWriteTmpIDs = writeTmpIDs(fIdsToWrite);
        if ( retWriteTmpIDs < 1 ){
            return retWriteTmpIDs;
        }
        return retWriteTmpIDs;
    }
    private int writeTmpIDs(NcTmpNowProcessInfo fIdsToWrite){
        try(ObjectOutputStream oos = 
                new ObjectOutputStream(
                new FileOutputStream(NcIdxFileManager.getTmpIdsFile())))
        {
            oos.writeObject(fIdsToWrite);
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIndexManageIDs.class.getCanonicalName(), ex);
            return -1;
        }
        return 1;
    }
    
    private static NcTmpNowProcessInfo readTmpIds(){
        NcTmpNowProcessInfo idsReadData;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NcIdxFileManager.getTmpIdsFile())))
        {
            idsReadData = (NcTmpNowProcessInfo)ois.readObject();
        }
        catch(Exception ex){
            NcAppHelper.logException(
                    NcIndexManageIDs.class.getCanonicalName(), ex);
            return new NcTmpNowProcessInfo();
        }
        return idsReadData;
    }

    /**
     *
     * @param inFuncTmpIds
     * @return
     */
    public static NcTmpNowProcessInfo getTmpIDsData(File inFuncTmpIds){
        return readTmpIds();
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    public static boolean isTmpIDsDataWrong(NcTmpNowProcessInfo inFuncData){
        if( inFuncData == null ){
            return true;
        }
        if( !isTmpIDsDataHashTrue(inFuncData) ){
            return true;
        }
        return false;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    public static boolean isTmpIDsDataHasEmptyField(NcTmpNowProcessInfo inFuncData){
        if( inFuncData == null ){
            return true;
        }
        boolean journalnameIsEmpty = inFuncData.journalname == "";
        boolean journalidIsEmpty = inFuncData.journalid == -777;
        boolean listnameIsEmpty = inFuncData.listname == "";
        boolean listnameidIsEmpty = inFuncData.listnameid == -777;
        boolean hashlistIsEmpty = inFuncData.hashlistname == "";
        boolean hashlistnnameidIsEmpty = inFuncData.hashlistnameid == -777;
        boolean longwordlistnameIsEmpty = inFuncData.longwordlistname == "";
        boolean longwordlistnameidIsEmpty = inFuncData.longwordlistnameid == -777;
        return journalnameIsEmpty
                || journalidIsEmpty
                || listnameIsEmpty
                || listnameidIsEmpty
                || hashlistIsEmpty
                || hashlistnnameidIsEmpty
                || longwordlistnameIsEmpty
                || longwordlistnameidIsEmpty;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    public static boolean isTmpIDsDataEmpty(NcTmpNowProcessInfo inFuncData){
        if( inFuncData == null ){
            return true;
        }
        boolean journalnameIsEmpty = inFuncData.journalname == "";
        boolean journalidIsEmpty = inFuncData.journalid == -777;
        boolean listnameIsEmpty = inFuncData.listname == "";
        boolean listnameidIsEmpty = inFuncData.listnameid == -777;
        boolean hashlistIsEmpty = inFuncData.hashlistname == "";
        boolean hashlistnnameidIsEmpty = inFuncData.hashlistnameid == -777;
        boolean longwordlistnameIsEmpty = inFuncData.longwordlistname == "";
        boolean longwordlistnameidIsEmpty = inFuncData.longwordlistnameid == -777;
        
        boolean hashIsTrue =  isTmpIDsDataHashTrue(inFuncData);
        return journalnameIsEmpty
                && journalidIsEmpty
                && listnameIsEmpty
                && listnameidIsEmpty
                && hashlistIsEmpty
                && hashlistnnameidIsEmpty
                && longwordlistnameIsEmpty
                && longwordlistnameidIsEmpty
                && hashIsTrue;
    }

    /**
     *
     * @param inFuncData
     * @return
     */
    public static boolean isTmpIDsDataHashTrue(NcTmpNowProcessInfo inFuncData){
        return inFuncData.recordHash == (""
                + inFuncData.journalname
                + inFuncData.journalid
                + inFuncData.listname
                + inFuncData.listnameid
                + inFuncData.hashlistname
                + inFuncData.hashlistnameid
                + inFuncData.longwordlistname
                + inFuncData.longwordlistnameid
                + inFuncData.recordTime).hashCode();
    }

/**
 * Method check afte record parameters: readyForRecord, count in readyForRecordData,
 * count records in current Directory List File, and if count readyForRecord has
 * record out of bound for current record File, than this method, creat chunk for
 * this recorded data, chunk 1 writed into current File and other data in chunk 2
 * recorded into new file
 * @return 
 */
    private NcTmpNowProcessInfo checkDataForAllDirListFiles(){
        TreeMap<Integer, File> indexWorkSubDirFilesList = NcIdxFileManager.getIndexWorkSubDirFilesList();
        File ncmfsDFL = indexWorkSubDirFilesList.get("/fl".hashCode());
        if( NcIdxFileManager.isErrorForFileOperation(ncmfsDFL) ){
            NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
            + "/fl directory check error"
            + NcIdxFileManager.getStrCanPathFromFile(ncmfsDFL));
            return new NcTmpNowProcessInfo();
        }
        File ncmfsDFHL = indexWorkSubDirFilesList.get("/fx".hashCode());
        if( NcIdxFileManager.isErrorForFileOperation(ncmfsDFHL) ){
            NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
            + "/fx directory check error"
            + NcIdxFileManager.getStrCanPathFromFile(ncmfsDFHL));
            return new NcTmpNowProcessInfo();
        }
        File ncmfsDLWL = indexWorkSubDirFilesList.get("/lw".hashCode());
        if( NcIdxFileManager.isErrorForFileOperation(ncmfsDLWL) ){
            NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
            + "/lw directory check error"
            + NcIdxFileManager.getStrCanPathFromFile(ncmfsDLWL));
            return new NcTmpNowProcessInfo();
        }
        
        TreeMap<Integer, File> listDFL = NcIdxFileManager.getFileListFromSubDir(ncmfsDFL);
        TreeMap<Integer, File> listDFHL = NcIdxFileManager.getFileListFromSubDir(ncmfsDFHL);
        TreeMap<Integer, File> listDLWL = NcIdxFileManager.getFileListFromSubDir(ncmfsDLWL);
        
        

        /** Not released in this version */
        long fCjournalid = 0;
        String fCjournalname = "";
        /** Get last name for files in Dirictories */
        String fClistname = getLastNameFormDirListFiles(listDFL);
        String fChashlistname = getLastNameFormDirListFiles(listDFHL);
        String fClongwordlistname = getLastNameFormDirListFiles(listDLWL);

        /** Read data from last names in Directories, and get last IDs, if has
         errors in read (read function return -1), then set IDs to Zero*/
        long fClistnameid = -1;
        long fChashlistnnameid = -1;
        long fClongwordlistnameid = -1;
        if( readFromDFLIds(fClistname) > 0 ){
            fClistnameid = lastRecDataDFL.lastKey();
        }
        if( readFromDFHLIds(fChashlistname) > 0 ){
            fChashlistnnameid = lastRecDataDFHL.get(lastRecDataDFHL.size() - 1).dirListID;
        }
        if( readFromDLWLIds(fClongwordlistname) > 0 ){
            fClongwordlistnameid = lastRecDataDLWL.get(lastRecDataDLWL.size() - 1).nameID;
        }

        NcTmpNowProcessInfo inDirIdsData = new NcTmpNowProcessInfo(
            fCjournalname,
            fCjournalid, 
            fClistname,
            fClistnameid, 
            fChashlistname,
            fChashlistnnameid, 
            fClongwordlistname,
            fClongwordlistnameid);
        return inDirIdsData;
    }
    private String getLastNameFormDirListFiles(TreeMap<Integer, File> filesList){
        String retLastName = "";
        if( filesList.isEmpty() ){
            return retLastName;
        }
        for(Map.Entry<Integer, File> itemName : filesList.entrySet()){
            if ( NcIdxFileManager.getStrCanPathFromFile(itemName.getValue()).compareToIgnoreCase(retLastName) > 0){
                retLastName = NcIdxFileManager.getStrCanPathFromFile(itemName.getValue());
            }
        }
        return retLastName;
    }
    private long readFromDFLIds(String strFIds){
        File fileFIds = new File(strFIds);
        if(fileExistRWAccessChecker(fileFIds)){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileFIds)))
            {
                lastRecDataDFL = (TreeMap<Long, NcDcIdxDirListToFileAttr>)ois.readObject();
            }
            catch(Exception ex){
                NcAppHelper.logException(
                    NcIndexManageIDs.class.getCanonicalName(), ex);
                return -1;
            }
        }
        else{
            return -1;
        }
        return lastRecDataDFL.lastKey();
    }
    private int readFromDFHLIds(String strFIds){
        File fileFIds = new File(strFIds);
        if(fileExistRWAccessChecker(fileFIds)){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileFIds)))
            {
                lastRecDataDFHL = (ArrayList<NcDcIdxDirListToFileHash>)ois.readObject();
            }
            catch(Exception ex){
                NcAppHelper.logException(
                    NcIndexManageIDs.class.getCanonicalName(), ex);
                return -1;
            }
        }
        else{
            return -1;
        }
        return lastRecDataDFHL.size();
    }
    private int readFromDLWLIds(String strFIds){
        File fileFIds = new File(strFIds);
        if(fileExistRWAccessChecker(fileFIds)){
            try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileFIds)))
            {
                lastRecDataDLWL = (ArrayList<NcDcIdxLongWordListToFile>)ois.readObject();
            }
            catch(Exception ex){
                NcAppHelper.logException(
                    NcIndexManageIDs.class.getCanonicalName(), ex);
                return -1;
            }
        }
        else{
            return -1;
        }
        return lastRecDataDLWL.size();
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
            NcAppHelper.logException(
                    NcIndexManageIDs.class.getCanonicalName(), ex);
            return false;
        }
        return false;
    }

}
