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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;


/**
 *
 * @author Администратор
 */
public class NcDiskUtils {
    private static final long Kb = 1024;
    private static final long Mb = 1024 * 1024;
    private static TreeMap<Long, NcDiskInfo> ncDiskInfo = getDiskInfo();
    
    /**
     *
     */
    public NcDiskUtils() {
    }
    
    /**
     *
     * @param drive
     * @return
     */
    public static String getIntSerialNumber(String drive) {
        String result = "";
        int retSerialNumber = 0;
        try {
            File file = File.createTempFile("realhowto",".vbs");
            file.deleteOnExit();
            try (FileWriter fw = new java.io.FileWriter(file)) {
                String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                        +"Set colDrives = objFSO.Drives\n"
                        +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
                        +"Wscript.Echo objDrive.SerialNumber";  // see note
                fw.write(vbs);
            } // see note
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            try (BufferedReader input = new BufferedReader
                      (new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    result += line;
                }
            }
        }
        catch(IOException ex) {
            Logger.getLogger(NcDiskUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result.trim();
    }
    /**
     * Get now information about disks in the system
     * @return 
     */
    public static TreeMap<Long, NcDiskInfo> getDiskInfo(){
        TreeMap<Long, NcDiskInfo> toRetDI = new TreeMap<Long, NcDiskInfo>();
        long diskID = 0;
        try {
            FileSystem fs = FileSystems.getDefault();
            
            for (FileStore store : fs.getFileStores()) {
                if(NcAppHelper.isWindows()){
                    
                    char cLet = getDiskLetterFromFileStore(store);
                    long dSN = 0;
                    if(cLet != '#'){
                        dSN = convertSN(getIntSerialNumber("" + cLet));
                    }
                    
                    String strHexDsn = Long.toHexString(dSN);
                    NcDiskInfo cDI = new NcDiskInfo(
                            diskID, 
                            dSN,
                            strHexDsn,
                            store.toString(),
                            store.name(),
                            cLet,
                            store.type(),
                            getProgramAlias(diskID),
                            getHumanAlias(diskID),
                            store.getTotalSpace(),
                            (store.getTotalSpace() - store.getUnallocatedSpace()),
                            store.getUsableSpace(),
                            store.getUnallocatedSpace(),
                            store.isReadOnly());
                    toRetDI.put(diskID, cDI);
                }
                else{
                    NcDiskInfo cDI = new NcDiskInfo(
                            diskID, 
                            0,
                            "",
                            store.toString(),
                            store.name(),
                            '#',
                            store.type(),
                            getProgramAlias(diskID),
                            getHumanAlias(diskID),
                            store.getTotalSpace(),
                            (store.getTotalSpace() - store.getUnallocatedSpace()),
                            store.getUsableSpace(),
                            store.getUnallocatedSpace(),
                            store.isReadOnly());
                    toRetDI.put(diskID, cDI);
                    
                }
                    diskID++;
            }
        } catch (IOException ex) {
                Logger.getLogger(NcDiskUtils.class.getName()).log(Level.SEVERE, null, ex);
                return new TreeMap<Long, NcDiskInfo>();
        }
        boolean isDiskHashTrue = NcDiskUtils.isDiskInfoRecordsHashTure(toRetDI);
        if( !isDiskHashTrue ){
            NcAppHelper.appExitWithMessage("Can't get and create disk info, error in records hash");
            return new TreeMap<Long, NcDiskInfo>();
        }
        return toRetDI;
    }

    /**
     *
     * @param store
     * @return
     */
    public static char getDiskLetterFromFileStore(FileStore store){
        String forProcess = store.toString().toUpperCase();
        
        char cLet = '#';
        if(NcAppHelper.isWindows()){
            if( forProcess.indexOf(':') > -1 ){
                String  strDiskLetter = forProcess.substring(forProcess.indexOf(":") - 1, forProcess.indexOf(":"));
                
                if( strDiskLetter.matches("[A-Z]") && (strDiskLetter.length() == 1) ){
                    cLet = strDiskLetter.charAt(0);
                }
            }
        }
        return cLet;
    }

    /**
     *
     * @param strPath
     * @return
     */
    public static char getDiskLetterFromPath(String strPath){
        String forProcess = strPath.toUpperCase().toString();
        char cLet = '#';
        if(NcAppHelper.isWindows()){
            if(forProcess.indexOf(':') == 1){
                if( forProcess.matches("[A-Z]") ){
                    cLet = forProcess.charAt(0);
                }
            }
        }
        return cLet;
    }
    private static long convertSN(String indSN){
        long outsn = 0;
        try{
            outsn = Long.parseLong(indSN);
        }
        catch(NumberFormatException ex){
            Logger.getLogger(NcDiskUtils.class.getName()).log(Level.SEVERE, null, ex);
            try{
                outsn = DatatypeConverter.parseLong(indSN);
            }
            catch(NumberFormatException extwo){
                Logger.getLogger(NcDiskUtils.class.getName()).log(Level.SEVERE, null, extwo);
                return 0;
            }
        }
        return outsn;
    }
/**
 * For example, when use USB drive, disk letter may be changed sometimes, 
 * for additional option in detect may be used totalSpace
 * @param ncFile
 * @return
 * diskID provided by class {@link ru.newcontrol.ncfv.NcDiskInfo#diskID NcDiskInfo}
 */
    public static long getDiskIDbyLetterTotalSpace(File ncFile){
        String strForDisk = NcIdxFileManager.getStrCanPathFromFile(ncFile).toUpperCase().substring(0);
        char ncDiskLetterFromPath = strForDisk.charAt(0);
        long ncNowStrorageIndex = -1;
        for(Map.Entry<Long, NcDiskInfo> ncOneOfDisk : ncDiskInfo.entrySet()){
            if ((ncOneOfDisk.getValue().diskLetter == ncDiskLetterFromPath)
                    && (ncFile.exists())
                    && (ncOneOfDisk.getValue().totalSpace == ncFile.getTotalSpace())){
                ncNowStrorageIndex = ncOneOfDisk.getValue().diskID;
            }
        }
        return ncNowStrorageIndex;
    }

    /**
     *
     * @param diskID
     * @return
     */
    public static String getHumanAlias(long diskID){
        return "DiskAliasNumber-" + diskID;
    }

    /**
     *
     * @param diskID
     * @return
     */
    public static String getProgramAlias(long diskID){
        return "DiskAliasNumber-" + diskID + "-" + System.nanoTime();
    }
    public static boolean isNcDiskInfoHashTrue(NcDiskInfo inFuncData){
        return inFuncData.reordHash == (""
            + inFuncData.diskID
            + inFuncData.longSerialNumber
            + inFuncData.strHexSerialNumber
            + inFuncData.strFileStore
            + inFuncData.strFileStoreName
            + inFuncData.diskLetter
            + inFuncData.diskFStype
            + inFuncData.programAlias
            + inFuncData.humanAlias
            + inFuncData.totalSpace
            + inFuncData.usedSpace
            + inFuncData.availSpace
            + inFuncData.unAllocatedSpace
            + inFuncData.isReadonly
            + inFuncData.recordCreationTime).hashCode();
    }
    public static boolean isDiskInfoRecordsHashTure(TreeMap<Long, NcDiskInfo> inFuncData){
        boolean isRecordHash = true;
        for(Map.Entry<Long, NcDiskInfo> itemDisk : inFuncData.entrySet() ){
            boolean boolRecHashTrue = isNcDiskInfoHashTrue(itemDisk.getValue());
            if( !boolRecHashTrue ){
                printToConsoleNcDiskInfo(itemDisk.getValue());
                isRecordHash = false;
            }
        }
        return isRecordHash;
    }
    public static void printToConsoleNcDiskInfo(NcDiskInfo inFuncData){
        
        int calcHash = (""
            + inFuncData.diskID
            + inFuncData.longSerialNumber
            + inFuncData.strHexSerialNumber
            + inFuncData.strFileStore
            + inFuncData.strFileStoreName
            + inFuncData.diskLetter
            + inFuncData.diskFStype
            + inFuncData.programAlias
            + inFuncData.humanAlias
            + inFuncData.totalSpace
            + inFuncData.usedSpace
            + inFuncData.availSpace
            + inFuncData.unAllocatedSpace
            + inFuncData.isReadonly
            + inFuncData.recordCreationTime).hashCode();
        NcAppHelper.outMessage("ID:\t" + inFuncData.diskID
        + "\nCalculated hash of record: " + calcHash
        + "\nIn record hash: " + inFuncData.reordHash
        + "\ncompare result: "
        + (calcHash == inFuncData.reordHash));
        
    }
}
