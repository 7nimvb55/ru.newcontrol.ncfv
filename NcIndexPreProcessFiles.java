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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Администратор
 */
public class NcIndexPreProcessFiles {
    /** currently selected File. */
    private File currentFile;
    private boolean ncRecursive = true;
    private ArrayList<Long> appendToDirListIDs = new ArrayList<Long>();

    /**
     *
     */
    public NcIndexPreProcessFiles() {
    }
    
    /**
     *
     * @param ncFile
     */
    public NcIndexPreProcessFiles(File ncFile) {
        currentFile = ncFile;
    }
    
    /**
     *
     * @param ncFile
     * @param fNcRecursive
     */
    public NcIndexPreProcessFiles(File ncFile, boolean fNcRecursive) {
        currentFile = ncFile;
        ncRecursive = fNcRecursive;
    }
    
    /**
     *
     * @return
     */
    public boolean getRecursive(){
        return ncRecursive;
    }
    
    /**
     *
     * @param fNcRecursive
     * @return
     */
    public boolean setRecursive(boolean fNcRecursive){
        ncRecursive = fNcRecursive;
        return ncRecursive;
    }
    
    /**
     *
     * @return
     */
    public File getCurrentFile(){
        return currentFile;
    }
    
    /**
     *
     * @param ncFile
     * @return
     */
    public File setCurrentFile(File ncFile){
        currentFile = ncFile;
        return currentFile;
    }
/**
 * This method to release functional of inteface button "Make Index" in
 * developmend code version
 * @param ncFile
 * @return 
 * @throws java.io.IOException 
 */    
public String[] getFileDataToSwing(File ncFile) throws IOException{
        
        String strAbsolutlePath = ncFile.getAbsolutePath();
        
        char strDisk = (char) strAbsolutlePath.charAt(0);
        String strPath = strAbsolutlePath.substring(2);
        
        String strABC = NcPathToArrListStr.NCLVLABC.retStr(strPath);
        String strRABC = NcPathToArrListStr.NCLVLRABC.retStr(strPath);
        String strSNUM = NcPathToArrListStr.NCLVLNUM.retStr(strPath);
        String strSYM = NcPathToArrListStr.NCLVLSYM.retStr(strPath);
        String strSPACE = NcPathToArrListStr.NCLVLSPACE.retStr(strPath);
        
        String cStrABC = NcPathToArrListStr.getResultStr(NcPathToArrListStr.NCLVLABC.retArrListStr(strPath),strPath);
        String cStrRABC = NcPathToArrListStr.getResultStr(NcPathToArrListStr.NCLVLRABC.retArrListStr(strPath),strPath);
        String cStrNUM = NcPathToArrListStr.getResultStr(NcPathToArrListStr.NCLVLNUM.retArrListStr(strPath),strPath);
        String cStrSYM = NcPathToArrListStr.getResultStr(NcPathToArrListStr.NCLVLSYM.retArrListStr(strPath),strPath);
        String cStrSPACE = NcPathToArrListStr.getResultStr(NcPathToArrListStr.NCLVLSPACE.retArrListStr(strPath),strPath);
        
        String cp = ncFile.getCanonicalPath();
        String ap = ncFile.getAbsolutePath();
        
        String strABCh = NcPathToArrListStr.toStrUTFinHEX(strABC);
        String strRABCh = NcPathToArrListStr.toStrUTFinHEX(strRABC);
        String strSNUMh = NcPathToArrListStr.toStrUTFinHEX(strSNUM);
        String strSYMh = NcPathToArrListStr.toStrUTFinHEX(strSYM);
        String strSPACEh = NcPathToArrListStr.toStrUTFinHEX(strSPACE);
        

        
        NcIMinFS ncwd = new NcIMinFS();
        String isIdx = ncwd.getIndexLastModifiedForDirectoryOrFile(ncFile) > 0 ? "In index" : "Not Indexed";
        NcIndexManageIDs ncThisManageIDs = ncwd.getNcIndexManageIDs();
        
        NcTmpNowProcessInfo ncIDsDAta = ncThisManageIDs.getIdsReadedData();
        
        String[] fstroA = {
            "getIndexLastModifiedForDirectoryOrFile: " + isIdx,
            "getLoadLastIDsStatus: " + ncThisManageIDs.getLoadLastIDsStatus(),
            "Current Directory List File Name: " + ncIDsDAta.listname,
            "Last Recorded ID: " + ncIDsDAta.listnameid,
            "toUrl: " + ncFile.getAbsolutePath(),
            //"Char: " + String.valueOf('A'),
            "Ext:" + NcPathToArrListStr.getExtention(ncFile),
            "SRC(aP): " + ap,
            "SRC[HEX]: " + Integer.toHexString(ap.hashCode()),
            "NAME: " + ncFile.getName(),

            "PARENT: " + ncFile.getParent(),
            "CanonicalPath(CP)" + cp,
            "CP[HEX]:" + Integer.toHexString(cp.hashCode()),
            "ABC: " + strABC,
            "ABC[HEX]: " + strABCh,
            "check: " + NcPathToArrListStr.checkStrUtfHex(strABCh , strABC),
            "ABC[search]: " + cStrABC,
            "RABC: " + strRABC,
            "RABC[HEX]: " + strRABCh,
            "check: " + NcPathToArrListStr.checkStrUtfHex(strRABCh , strRABC),
            "RABC[search]: " + cStrRABC,
            "NUM: " + strSNUM,
            "NUM[HEX]: " + strSNUMh,
            "check: " + NcPathToArrListStr.checkStrUtfHex(strSNUMh , strSNUM),
            "NUM[search]: " + cStrNUM,
            "SYM: " + strSYM,
            "SYM[HEX]: " + strSYMh,
            "check: " + NcPathToArrListStr.checkStrUtfHex(strSYMh , strSYM),
            "SYM[search]: " + cStrSYM,
            "SPACE: " + ":" + strSPACE + ":L=" + strSPACE.length(),
            "SPACE[HEX]: " + strSPACEh,
            "check: " + NcPathToArrListStr.checkStrUtfHex(strSPACEh , strSPACE),
            "SPACE[search]: " + cStrSPACE
        };
        
        return fstroA;
    }

    /**
     *
     * @param ncFile
     * @return
     * @throws IOException
     */
    public String[] getResultMakeIndex(File ncFile) throws IOException{
        NcIMinFS ncwd = new NcIMinFS();
        
        NcIndexManageIDs ncThisManageIDs = ncwd.getNcIndexManageIDs();
        
        TreeMap<Long, NcDiskInfo> ncDiskInfo = ncwd.getDiskInfo();
//        ncDiskInfo.size();
//        ncDiskInfo.iterator().hasNext();
        
        String strAbsolutlePath = ncFile.getAbsolutePath();
        String strPath = strAbsolutlePath.substring(2);
        

        long nextID = ncThisManageIDs.getIdsReadedData().listnameid;
//For NcIndexFilesReaderWriter.put part of code
        
//Create new Data for Record
        if(nextID == -1){
            nextID = 0;
        }
        NcDcIdxDirListToFileAttr forRecordData = getDataToDL(ncDiskInfo,
                ncFile,
                nextID);
        
        long writedID = -1;
        writedID = NcIdxDirListManager.putToDirectoryList(forRecordData);
        
        String strTmpAppend = "wrong";
        if(writedID > 0){
            strTmpAppend = "writed";

        }
        
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureABC = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLABC.retArrListStr(strPath),
                strPath,
                writedID);
        //Input for put in Storage Word code here
        NcIdxStorageWordManager.putInStorageWord("NCLVLABC", StructureABC);
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureRABC = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLRABC.retArrListStr(strPath),
                strPath,
                writedID);
        //Input for put in Storage Word code here
        NcIdxStorageWordManager.putInStorageWord("NCLVLRABC", StructureRABC);
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureNUM = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLNUM.retArrListStr(strPath),
                strPath,
                writedID);
        //Input for put in Storage Word code here
        NcIdxStorageWordManager.putInStorageWord("NCLVLNUM", StructureNUM);
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureSYM = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLSYM.retArrListStr(strPath),
                strPath,
                writedID);
        //Input for put in Storage Word code here
        NcIdxStorageWordManager.putInStorageWord("NCLVLSYM", StructureSYM);
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureSPACE = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLSPACE.retArrListStr(strPath),
                strPath,
                writedID);


        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureWord = new TreeMap<Long, NcDcIdxSubStringToOperationUse>();
        //For Word Length > 25
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureLongWord = new TreeMap<Long, NcDcIdxSubStringToOperationUse>();
        long lwIdx = 0;
        long wIdx = 0;
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureABC.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureRABC.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureNUM.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureSYM.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureSPACE.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }

        long countWritedWordIDs = NcIdxWordManager.putWord(StructureWord);
        long countWritedLongWordIDs = NcIdxLongWordListManager.putLongWord(StructureLongWord);
        
/**
 * 
 */        

        
        String[] fstroA = {
            "Result for Make Index:",
            "StructureLongWord.size(): " + StructureLongWord.size(),
            "Count Writed IDs in LongWord: " + countWritedLongWordIDs,
            "StructureWord.size(): " + StructureWord.size(),
            "Count Writed IDs in Word: " + countWritedWordIDs,
            "After this record ID: " + nextID,
            "Writed ID: " + writedID,
            "ID in tmp TDs: " + ncThisManageIDs.getIdsReadedData().listnameid,
        };
        return fstroA;
    }

/**
 * 
 * i - id record
 * ds - Disk serial number hex string
 * dh - Disk serial number hashCode
 * dl - Disk letter
 * ps - File.getAbsolutePath()
 * ph - File.getAbsolutePath().hashCode() this path hash code (if change disk hash letter changed?)
 * cs - File Path with out Disk Letter;
 * ch - File Path with out Disk Letter hashCode();
 * l - File.length()
 * r - File.canRead()
 * w - File.canWrite()
 * x - File.canExecute()
 * h - File.isHidden()
 * lm - File.lastModified()
 * td - File.isDirectory()
 * tf - File.isFile()
 * fc - String Files.probeContentType()
     * @param ncDiskInfo
     * @param DirListIdx
     * @param ncFile
     * @return 
 */    
    public NcDcIdxDirListToFileAttr getDataToDL(TreeMap<Long, NcDiskInfo> ncDiskInfo, File ncFile, long DirListIdx){
        String strForDisk = ncFile.getAbsolutePath().toUpperCase().substring(0);
        char ncDiskLetterFromPath = strForDisk.charAt(0);
        int ncDiskIndexes = 0;
        int ncNowStrorageIndex = -1;
        long diskID = -1;
        long longDiskSerialNumber = -1;
        String strDiskSerialNumber = "N/A";
        long diskTotalSpace = -1;
        String diskProgramAlias = "";
        for( Map.Entry<Long, NcDiskInfo> ncOneOfDisk : ncDiskInfo.entrySet() ){
/**
 * @todo develop code for *NIX systems where path not have (?:) drive letter
 */            
            if ((ncOneOfDisk.getValue().diskLetter == ncDiskLetterFromPath)
                    && (ncFile.exists())
                    && (ncOneOfDisk.getValue().totalSpace == ncFile.getTotalSpace())){
                diskID = ncOneOfDisk.getValue().diskID;
                longDiskSerialNumber = ncOneOfDisk.getValue().longSerialNumber;
                diskProgramAlias = ncOneOfDisk.getValue().programAlias;
                strDiskSerialNumber = ncOneOfDisk.getValue().strHexSerialNumber;
                diskTotalSpace = ncOneOfDisk.getValue().totalSpace;
                ncNowStrorageIndex = ncDiskIndexes;
            }
            ncDiskIndexes++;
        }
        
        
        NcDcIdxDirListToFileAttr fileToDirListData = new NcDcIdxDirListToFileAttr(
            DirListIdx,
            diskID,
            longDiskSerialNumber,
            diskTotalSpace,
            diskProgramAlias,    
            strDiskSerialNumber,
            ncDiskLetterFromPath,
            ncFile.getAbsolutePath().substring(2),
            ncFile.length(),
            ncFile.canRead(),
            ncFile.canWrite(),
            ncFile.canExecute(),
            ncFile.isHidden(),
            ncFile.lastModified(),
            ncFile.isDirectory(),
            ncFile.isFile(),
            false,
            -1
        );
        return fileToDirListData;
    }

    /**
     * File content probe to releases in NcDirListToFilesType
     * @param ncCurPathACP
     * @return 
     */
    private String getAttrCurPath(String ncCurPathACP){
        Path file = Paths.get(ncCurPathACP);
        String type = "";
        if (Files.isDirectory(file)) {
            type = "<directory>";
        } else {
            try {
                FileStore store = Files.getFileStore(Paths.get(ncCurPathACP));
                type = Files.probeContentType(file);
                if (type == null)
                    type = "<not recognized>";
            } catch (IOException ex) {
                Logger.getLogger(NcIndexPreProcessFiles.class.getName()).log(Level.SEVERE, null, ex);
                type = "<not recognized>";
            }
        }
        return type;
    }

    /**
     *
     * @param ncFileRec
     * @return
     */
    public long makeIndexRecursive(File ncFileRec){
        
        if(ncFileRec.exists()){
            appendToDirListIDs.clear();
            makeIndexForFolder(ncFileRec);
            return appendToDirListIDs.size();
        }
        return 0;
    }
    
    private long makeIndexForFolder(File ncFile){
        
        if(ncFile.isFile()){
            try {
                appendToDirListIDs.add(makeIndexForFile(ncFile));
            } catch (IOException ex) {
                Logger.getLogger(NcIndexPreProcessFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 1;
        }
        else{
            try {
                appendToDirListIDs.add(makeIndexForFile(ncFile));
            } catch (IOException ex) {
                Logger.getLogger(NcIndexPreProcessFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            for(File itemFile : ncFile.listFiles()){
                makeIndexForFolder(itemFile);
            }
        }
        return 1;
    }
    
    /**
     *
     * @param ncFile
     * @return
     * @throws IOException
     */
    public long makeIndexForFile(File ncFile) throws IOException{
        NcDcIdxDirListToFileExist existDirList;
        boolean writedToExistList = false;
        NcIMinFS ncwd = new NcIMinFS();
        
        NcIndexManageIDs ncThisManageIDs = ncwd.getNcIndexManageIDs();
        
        long startTime = System.nanoTime();
        
        NcTmpNowProcessInfo ncIDsDAta = ncThisManageIDs.getIdsReadedData();
        
        TreeMap<Long, NcDiskInfo> ncDiskInfo = ncwd.getDiskInfo();
        String strAbsolutlePath = ncFile.getAbsolutePath();
        
        String strPath = strAbsolutlePath;
        
        if( !NcAppHelper.isWindows() ){
            strPath = strAbsolutlePath.substring(2);
        }
        

        long nextID = ncThisManageIDs.getIdsReadedData().listnameid;
        if(nextID == -1){
            nextID = 0;
        }
        NcDcIdxDirListToFileAttr forRecordData = getDataToDL(ncDiskInfo,
                ncFile,
                nextID);
        
        /**
         * Mark to start process directory or file here
         */

        long writedID = -1;
        
        writedID = NcIdxDirListManager.putToDirectoryList(forRecordData);
        
        existDirList = new NcDcIdxDirListToFileExist(
                    writedID,
                    forRecordData.diskID,
                    forRecordData.path,
                    startTime,
                    -1
        );      
        
        String strTmpAppend = "wrong";
        if(writedID > 0){
            strTmpAppend = "writed";

            long writeResult = NcIdxDirListExistManager.putToDirListExistStart(existDirList, writedID);
            if(writeResult > -1){
                writedToExistList = true;
            }
        }
        
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureABC = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLABC.retArrListStr(strPath),
                strPath,
                writedID);
        if( !StructureABC.isEmpty() ){
            NcIdxStorageWordManager.putInStorageWord("NCLVLABC", StructureABC);
        }
        
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureRABC = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLRABC.retArrListStr(strPath),
                strPath,
                writedID);
        if( !StructureRABC.isEmpty() ){
            NcIdxStorageWordManager.putInStorageWord("NCLVLRABC", StructureRABC);  
        }
              
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureNUM = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLNUM.retArrListStr(strPath),
                strPath,
                writedID);
        if( !StructureNUM.isEmpty() ){
            NcIdxStorageWordManager.putInStorageWord("NCLVLNUM", StructureNUM);  
        }
                      
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureSYM = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLSYM.retArrListStr(strPath),
                strPath,
                writedID);
        if( !StructureSYM.isEmpty() ){
            NcIdxStorageWordManager.putInStorageWord("NCLVLSYM", StructureSYM);  
        }
                              
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureSPACE = NcPathToArrListStr.getStructureToRecord(
                NcPathToArrListStr.NCLVLSPACE.retArrListStr(strPath),
                strPath,
                writedID);

        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureWord = new TreeMap<Long, NcDcIdxSubStringToOperationUse>();
        //For Word Length > 25
        TreeMap<Long, NcDcIdxSubStringToOperationUse> StructureLongWord = new TreeMap<Long, NcDcIdxSubStringToOperationUse>();
        long lwIdx = 0;
        long wIdx = 0;
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureABC.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureRABC.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureNUM.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureSYM.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        
        for(Map.Entry<Long, NcDcIdxSubStringToOperationUse> item : StructureSPACE.entrySet()){
            if (item.getValue().strSubString.length() > 25){
                StructureLongWord.put(lwIdx, item.getValue());
                lwIdx++;
            }
            else{
                StructureWord.put(wIdx, item.getValue());
                wIdx++;
            }
        }
        if( !StructureWord.isEmpty() ){
            long countWritedWordIDs = NcIdxWordManager.putWord(StructureWord);
        }
        if( !StructureLongWord.isEmpty() ){
            long countWritedLongWordIDs = NcIdxLongWordListManager.putLongWord(StructureLongWord);
        }
        long stopTime = System.nanoTime();
        if(writedToExistList){
            existDirList.nanoTimeEndAddToIndex = stopTime;
            NcIdxDirListExistManager.putToDirListExistStop(existDirList, writedID);
        }
        return writedID;
    }

}
