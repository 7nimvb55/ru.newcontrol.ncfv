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
import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author Администратор
 */
public class NcAppHelper {

    /**
     *
     */
    public static void outPutToConsoleDiskInfo(){
        TreeMap<Long, NcDiskInfo> sysDisk = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        if( !sysDisk.isEmpty() ){
            outMessageToConsole("If your need to write alias parameter into Configuration file");
            outMessageToConsole("write it in this example format:");
            String[] strForDisk = {"USB",
            "Black",
            "Document",
            "Flash",
            "Storage",
            "HDD",
            "Network",
            "System",
            "Bootable",
            "Silver",
            "Grey",
            "Old",
            "New",
            "FromWorker"};
            String strFirst = "";
            int i = 0;
            for(Map.Entry<Long, NcDiskInfo> itemDisk : sysDisk.entrySet()){
                if(i == 0){
                    strFirst = strForDisk[(int) Math.round(Math.random()*12)]
                    + strForDisk[(int) Math.round(Math.random()*12)]
                    + strForDisk[(int) Math.round(Math.random()*12)]
                    + strForDisk[(int) Math.round(Math.random()*12)]
                    + strForDisk[(int) Math.round(Math.random()*12)];
                    outMessageToConsole("alias_" + itemDisk.getValue().diskID + "="
                    + strFirst);
                }
                else{
                    outMessageToConsole("alias_" + itemDisk.getValue().diskID + "="
                    + strForDisk[(int) Math.round(Math.random()*12)]
                    + strForDisk[(int) Math.round(Math.random()*12)]
                    + strForDisk[(int) Math.round(Math.random()*12)]
                    + strForDisk[(int) Math.round(Math.random()*12)]
                    + strForDisk[(int) Math.round(Math.random()*12)]);
                }
                outMessageToConsole("Disk name and letter: " + itemDisk.getValue().strFileStore);
                outMessageToConsole("Disk name: " + itemDisk.getValue().strFileStoreName);
                if(isWindows()){
                    outMessageToConsole("Serial number: " + itemDisk.getValue().strHexSerialNumber);
                }
                outMessageToConsole("File system: " + itemDisk.getValue().diskFStype);
                outMessageToConsole("Total space in bytes: " + Long.toString(itemDisk.getValue().totalSpace));
                outMessageToConsole("Total space in Kb: " + Long.toString(Math.round(itemDisk.getValue().totalSpace/1024)));
                outMessageToConsole("Total space in Mb: " + Long.toString(Math.round(itemDisk.getValue().totalSpace/(1024*1024))));
                outMessageToConsole(" ");
                i++;
            }

            outMessageToConsole("where " + sysDisk.firstEntry().getValue().diskID + " is diskID and " + strFirst);
            outMessageToConsole("is User alias label returned in search results");
            
        }
        else{
            outMessageToConsole("Information about disks is Empty, contact your system Administrator");
            System.exit(0);
        }
    }

    /**
     *
     * @param pathErr
     */
    public static void appExitWithMessageFSAccess(String pathErr){
        outMessage("For run application in the path: " + pathErr
            + "\n application must have permission on read, write on the file system"
            + "\n for use functions of this application your must have run it in the"
            + "\n system administrator privilegies, this application, read files on"
            + "\n your file system, create some files in your file system, for more"
            + "\n information about use this application read manual or contact to"
            + "\n your system administrator");
        System.exit(0);
    }
    public static void appExitWithMessage(String strErrMessage){
        NcAppHelper.outMessage(strErrMessage);
        System.exit(0);
    }

    /**
     *
     */
    public static void getNcSysProperties(){
        Properties sProp = System.getProperties();
        Set<String> strPropName = sProp.stringPropertyNames();
        Map<String, String> sEnv = System.getenv();
        outMessageToConsole("");
        outMessageToConsole("");
        outMessageToConsole("System.getProperties");
        outMessageToConsole("");
        for( String itemPorperties : strPropName ){
            outMessageToConsole("Property name: \t" + itemPorperties);
            outMessageToConsole("Property value: \t" + sProp.getProperty(itemPorperties));
        }
        outMessageToConsole("");
        outMessageToConsole("");
        outMessageToConsole("System.getenv");
        outMessageToConsole("");
        for(Map.Entry<String, String> itemEnv : sEnv.entrySet()){
            outMessageToConsole("Key of environment: \t" + itemEnv.getKey());
            outMessageToConsole("Value of environment: \t" + itemEnv.getValue());
        }
        
        File[] fileRoots = File.listRoots();
        outMessageToConsole("");
        outMessageToConsole("");
        outMessageToConsole("File.listRoots");
        outMessageToConsole("");
        for(File itemFile : fileRoots){
            try {
                outMessageToConsole("getAbsolutePath: " + itemFile.getAbsolutePath());
                outMessageToConsole("getCanonicalPath: " + itemFile.getCanonicalPath());
                outMessageToConsole("toString: " + itemFile.toString());
            
                outMessageToConsole("getName: " + itemFile.getName());
                outMessageToConsole("getFreeSpace: " + itemFile.getFreeSpace());
                outMessageToConsole("getUsableSpace: " + itemFile.getUsableSpace());
                outMessageToConsole("getTotalSpace: " + itemFile.getTotalSpace());
            } catch (IOException ex) {
                Logger.getLogger(NcPreRunFileViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        FileSystem fs = FileSystems.getDefault();
        outMessageToConsole("");
        outMessageToConsole("");
        outMessageToConsole("FileSystems.getDefault.getFileStores");
        outMessageToConsole("");
        for (FileStore store : fs.getFileStores()) {
            outMessageToConsole("FileStore: " + store.toString());
            outMessageToConsole("name: " + store.name());
            
            outMessageToConsole("type: " + store.type());
            outMessageToConsole("isReadOnly: " + store.isReadOnly());
                
            try {
                outMessageToConsole("getTotalSpace: " + store.getTotalSpace());
                outMessageToConsole("getUsableSpace: " + store.getUsableSpace());
                outMessageToConsole("getUnallocatedSpace: " + store.getUnallocatedSpace());
            } catch (IOException ex) {
                Logger.getLogger(NcPreRunFileViewer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        outMessageToConsole("");
        outMessageToConsole("");
        outMessageToConsole("FileSystems.getDefault.getRootDirectories");
        outMessageToConsole("");
        for (Path storePath : fs.getRootDirectories()) {
            outMessageToConsole("getNameCount: " + storePath.getNameCount());
            outMessageToConsole("Path.toString: " + storePath.toString());
            outMessageToConsole("getFileSystem.toString: " + storePath.getFileSystem().toString());
        }
        outMessageToConsole("");
        outMessageToConsole("");
        outMessageToConsole("NcDiskUtils.getDiskInfo");
        outMessageToConsole("");
        TreeMap<Long, NcDiskInfo> sysDisk = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        for( Map.Entry<Long, NcDiskInfo> itemDisk : sysDisk.entrySet() ){
            NcAppHelper.outMessageToConsole("");
            NcAppHelper.outMessageToConsole("diskID: \t" + Long.toString(itemDisk.getValue().diskID));
            NcAppHelper.outMessageToConsole("humanAlias: \t" + itemDisk.getValue().humanAlias);
            NcAppHelper.outMessageToConsole("programAlias: \t" + itemDisk.getValue().programAlias);
            NcAppHelper.outMessageToConsole("strFileStore: \t" + itemDisk.getValue().strFileStore);
            NcAppHelper.outMessageToConsole("strFileStoreName: \t" + itemDisk.getValue().strFileStoreName);
            NcAppHelper.outMessageToConsole("DiskLetter: \t" + itemDisk.getValue().diskLetter);
            NcAppHelper.outMessageToConsole("longSerialNumber: \t" + Long.toString(itemDisk.getValue().longSerialNumber));
            NcAppHelper.outMessageToConsole("strHexSerialNumber: \t" + itemDisk.getValue().strHexSerialNumber);
            NcAppHelper.outMessageToConsole("DiskFStype: \t" + itemDisk.getValue().diskFStype);
            NcAppHelper.outMessageToConsole("isReadonly: \t" + itemDisk.getValue().isReadonly);
            NcAppHelper.outMessageToConsole("availSpace: \t" + Long.toString(itemDisk.getValue().availSpace));
            NcAppHelper.outMessageToConsole("totalSpace: \t" + Long.toString(itemDisk.getValue().totalSpace));
            NcAppHelper.outMessageToConsole("unAllocatedSpace: \t" + Long.toString(itemDisk.getValue().unAllocatedSpace));
            NcAppHelper.outMessageToConsole("usedSpace: \t" + Long.toString(itemDisk.getValue().usedSpace));
        }
        
    }

    /**
     *
     * @param strMessage
     */
    public static void outMessage(String strMessage){
        if( NcfvRunVariables.getStage() ){
        if(!Ncfv.getRunIsSwing()){
            if( NcfvRunVariables.getWithTrace() ){
                String strNowTime = java.time.LocalDateTime.now().toString();
                outMessageToConsole("at " + strNowTime + "\n");
                Thread t = Thread.currentThread();
                StackTraceElement[] nowT = t.getStackTrace();
                int idx = 0;
                for(StackTraceElement itemT : nowT ){
                    if( idx > 1 || NcfvRunVariables.getTraceWithPrintFunc() ){
                        String strOutFile = "";
                        if( NcfvRunVariables.getIncludeFile() ){
                            strOutFile = itemT.getFileName() + "\t";
                        }
                        String strOut = 
                            "\t" + itemT.getClassName()
                            + "." + itemT.getMethodName()
                            + "\t[" + itemT.getLineNumber() + "]"
                            + (itemT.isNativeMethod() ? "-native" : "");
                        outMessageToConsole(strOutFile + strOut);
                    }
                    idx++;
                }
            }
            outMessageToConsole(strMessage);
        }
        outMessageToAppLogFile(strMessage);
        }
    }

    /**
     *
     * @param strMessage
     */
    public static void outMessageToConsole(String strMessage){
        System.out.println(strMessage);
    }

    /**
     *
     * @param strMessage
     */
    public static void outMessageToAppLogFile(String strMessage){
        
        //System.out.println(strMessage);
    }
    
/**
 * Find disk with maximum avalable space and not ReadOnly for make index work directory
 * @return index of record in class NcDiskInfo
 */    
    public static NcDiskInfo getNcDiskInfoForMaxFreeSpace(){
        NcDiskInfo ncDisk = null;
            long tmpFreeSpace = 0;
            TreeMap<Long, NcDiskInfo> sysDisk = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
            if( !sysDisk.isEmpty() ){
                for ( Map.Entry<Long, NcDiskInfo> nccd : sysDisk.entrySet() ){
                    if( !nccd.getValue().isReadonly ){
                        if(tmpFreeSpace < nccd.getValue().availSpace){
                            tmpFreeSpace = nccd.getValue().availSpace;
                            ncDisk = nccd.getValue();
                        }
                    }
                }
                return ncDisk;
            }
        return ncDisk;
    }

    /**
     *
     * @return
     */
    public static boolean isWindows(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf( "win" ) >= 0); 
    }

    /**
     *
     * @return
     */
    public static boolean isMac(){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf( "mac" ) >= 0); 
    }

    /**
     *
     * @return
     */
    public static boolean isUnix (){
        String os = System.getProperty("os.name").toLowerCase();
        return (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0);
    }



    /**
     *
     * @param bytes
     * @return
     */
    public static String toHex(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }
    public static void strArrToConsoleOutPut(String[] strArrForOutPut){
        for(int i = 0; i < strArrForOutPut.length ; i++){
                NcAppHelper.outMessage( i + "\t" + strArrForOutPut[i]);
        }
    }
}
