/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Администратор
 */
public class NcPreRunFileViewer {
    private boolean messagesToConsole;
    
    /**
     *
     */
    public NcPreRunFileViewer() {
        messagesToConsole = !Ncfv.getRunIsSwing();
    }

    /**
     *
     */
    public static void createNewCfg(){
        ArrayList<String> arrStrCfg = getReadedLinesFromEtcCfg();
    }
    /**
     * 
     * Version of {@link ru.newcontrol.ncfv.NcPreRunFileViewer#initStateCheck()}
     * with return structure of Data
     * @return {@link ru.newcontrol.ncfv.NcParamFv}
     */
    public static NcParamFv getCurrentWorkCfg(){
        File fileCfg = NcIdxFileManager.getOrCreateCfgFile();
        NcParamFv readedWorkCfg = NcParamFvReader.readDataFromWorkCfg();
        boolean boolMD5 = false;
        boolean boolSHA1 = false;
        boolean boolSHA256 = false;
        boolean boolSHA512 = false;
        
        if( fileCfg.exists() && ( !NcParamFvManager.isNcParamFvDataEmpty(readedWorkCfg) ) ){
                boolMD5 = readedWorkCfg.strHexMD5.equalsIgnoreCase(NcAppHelper.toHex(NcFileHash.MD5.checksum(fileCfg)));
                boolSHA1 = readedWorkCfg.strHexSHA1.equalsIgnoreCase(NcAppHelper.toHex(NcFileHash.SHA1.checksum(fileCfg)));
                boolSHA256 = readedWorkCfg.strHexSHA256.equalsIgnoreCase(NcAppHelper.toHex(NcFileHash.SHA256.checksum(fileCfg)));
                boolSHA512 = readedWorkCfg.strHexSHA512.equalsIgnoreCase(NcAppHelper.toHex(NcFileHash.SHA512.checksum(fileCfg)));
        }
        if( (!boolMD5) && (!boolSHA1) && (!boolSHA256) && (!boolSHA512) ){
            readedWorkCfg = getUpdatedAppCfg();
        }
        if( NcParamFvManager.isNcParamFvDataEmpty(readedWorkCfg) ){
            readedWorkCfg = getUpdatedAppCfg();
            if( NcParamFvManager.isNcParamFvDataEmpty(readedWorkCfg) ){
                NcAppHelper.appExitWithMessage("Can't get for current work config, error");
            }
        }
        return readedWorkCfg;
    }
    /**
     *
     * @return
     */
    public static NcParamFv getUpdatedAppCfg(){
        File fileCfg = NcIdxFileManager.getOrCreateCfgFile();
        ArrayList<String> arrStrCfg = getReadedLinesFromEtcCfg();
        NcParamFv paramReadedCfg = parseEtcCfg(arrStrCfg);
        NcParamFv paramFromValideCfg = validateAndApplyCfg(paramReadedCfg);
        ArrayList<String> arrStrToUpdateCfg = getLinesParametersForUpdateCfg(paramFromValideCfg);
        int countLines = updateEtcCfg(arrStrToUpdateCfg);
        NcParamFv paramToWriteCfg = null;
        if( fileCfg.exists() ){
            paramToWriteCfg = NcParamFvManager.setFileHashes(paramFromValideCfg, fileCfg);
        }
        boolean isWrited = NcParamFvWriter.wirteDataInWorkCfg(paramToWriteCfg);
        if( !isWrited ){
            NcAppHelper.appExitWithMessage("Can't write work configuration parameters");
            return new NcParamFv();
        }
        return paramToWriteCfg;
    }
    
    /**
     *
     * @return
     */
    public static NcParamFv getDefaultCfgValues(){
        TreeMap<Long, NcDiskInfo> sysDisk = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        TreeMap<Integer, String> diskUserAlias = new TreeMap<Integer, String>();
        for( Map.Entry<Long, NcDiskInfo> itemDisk : sysDisk.entrySet() ){
            diskUserAlias.put((int) itemDisk.getValue().diskID, itemDisk.getValue().humanAlias);
        }
        String strIndex = "/ncfvdi";
        if( !NcAppHelper.isWindows() ){
            String homePath = NcIdxFileManager.getUserHomeDirStrPath();
            String appDir = NcIdxFileManager.getAppWorkDirStrPath();
            strIndex = NcIdxFileManager.strPathCombiner(homePath, strIndex);
            String kwdOut = NcIdxFileManager.strPathCombiner(appDir, "keywordout.list");
            String kwdIn = NcIdxFileManager.strPathCombiner(appDir, "keywordin.list");
            String dirOut = NcIdxFileManager.strPathCombiner(appDir, "dirout.list");
            String dirIn = NcIdxFileManager.strPathCombiner(appDir, "dirin.list");
            NcParamFv defaultCfgForReturn = new NcParamFv(
            strIndex,
            kwdOut,
            kwdIn,
            dirOut,
            dirIn,
            diskUserAlias,
            "",
            "",
            "",
            "",
            new TreeMap<Integer, File>());
        return defaultCfgForReturn;
        }
        NcParamFv defaultCfgForReturn = new NcParamFv(
            strIndex,
            "keywordout.list",
            "keywordin.list",
            "dirout.list",
            "dirin.list",
            diskUserAlias,
            "",
            "",
            "",
            "",
            new TreeMap<Integer, File>());
        return defaultCfgForReturn;
    }
    
    
    /**
     * Method for validate parameters from readed configuration file, if setted
     * parameters has wrong format, than used default value and configuration
     * file will be rewrited with work config
     * @param inFuncReadedCfg returned by method {@link #parseEtcCfg parseEtcCfg()};
     * @return Parameters to write into updated Cfg file
     */
    public static NcParamFv validateAndApplyCfg(NcParamFv inFuncReadedCfg){
        
        boolean aliasChanged = NcParamJournalDisk.updateUserAliasInJournalDisk(inFuncReadedCfg.diskUserAlias);
        
        NcParamFv defaultWorkCfg = getDefaultCfgValues();
        NcParamFv outFuncWorkCfg;
        
        String strPotWorkDir = NcPathFromUserChecker.strInputAppWorkDirFromUser(inFuncReadedCfg.indexPath, defaultWorkCfg.indexPath);
        String strPotFileKeyOutSearch = NcPathFromUserChecker.strInputAppWorkFileFromUser(inFuncReadedCfg.keywordsOutOfSearch, defaultWorkCfg.keywordsOutOfSearch);
        String strPotFileKeyInSearch = NcPathFromUserChecker.strInputAppWorkFileFromUser(inFuncReadedCfg.keywordsInSearch, defaultWorkCfg.keywordsInSearch);
        String strPotFileDirOutIndex = NcPathFromUserChecker.strInputAppWorkFileFromUser(inFuncReadedCfg.dirOutOfIndex, defaultWorkCfg.dirOutOfIndex);
        String strPotFileDirInIndex = NcPathFromUserChecker.strInputAppWorkFileFromUser(inFuncReadedCfg.dirInIndex, defaultWorkCfg.dirInIndex);
        
        boolean resultCheckWorkDir = NcParamCfgToDiskReleaser.checkOrCreateIdxDirStructure(strPotWorkDir);
        boolean resultCheckWorkDirDefault = false;
        if( !resultCheckWorkDir ){
            NcParamCfgToDiskReleaser.getIdxDirStructure(strPotWorkDir);
            strPotWorkDir = NcPathFromUserChecker.strInputAppWorkDirDefault(defaultWorkCfg.indexPath);
            resultCheckWorkDirDefault = NcParamCfgToDiskReleaser.checkOrCreateIdxDirStructure(strPotWorkDir);
        }
        boolean resultCheckFileKeyOutSearch = NcParamCfgToDiskReleaser.checkOrCreateFiles(strPotFileKeyOutSearch);
        boolean resultCheckFileKeyOutSearchDefault = false;
        if( !resultCheckFileKeyOutSearch ){
            NcParamCfgToDiskReleaser.getWorkFileParams(strPotFileKeyOutSearch);
            strPotFileKeyOutSearch = NcPathFromUserChecker.strInputAppWorkFileDefault(defaultWorkCfg.keywordsOutOfSearch);
            resultCheckFileKeyOutSearchDefault = NcParamCfgToDiskReleaser.checkOrCreateFiles(strPotFileKeyOutSearch);
        }
        boolean resultCheckFileKeyInSearch = NcParamCfgToDiskReleaser.checkOrCreateFiles(strPotFileKeyInSearch);
        boolean resultCheckFileKeyInSearchDefault = false;
        if( !resultCheckFileKeyInSearch ){
            NcParamCfgToDiskReleaser.getWorkFileParams(strPotFileKeyInSearch);
            strPotFileKeyInSearch = NcPathFromUserChecker.strInputAppWorkFileDefault(defaultWorkCfg.keywordsInSearch);
            resultCheckFileKeyInSearchDefault = NcParamCfgToDiskReleaser.checkOrCreateFiles(strPotFileKeyInSearch);
        }
        boolean resultCheckFileDirOutIndex = NcParamCfgToDiskReleaser.checkOrCreateFiles(strPotFileDirOutIndex);
        boolean resultCheckFileDirOutIndexDefault = false;
        if( !resultCheckFileDirOutIndex ){
            NcParamCfgToDiskReleaser.getWorkFileParams(strPotFileDirOutIndex);
            strPotFileDirOutIndex = NcPathFromUserChecker.strInputAppWorkFileDefault(defaultWorkCfg.dirOutOfIndex);
            resultCheckFileDirOutIndexDefault = NcParamCfgToDiskReleaser.checkOrCreateFiles(strPotFileDirOutIndex);
        }
        boolean resultCheckFileDirInIndex = NcParamCfgToDiskReleaser.checkOrCreateFiles(strPotFileDirInIndex);
        boolean resultCheckFileDirInIndexDefault = false;
        if( !resultCheckFileDirInIndex ){
            NcParamCfgToDiskReleaser.getWorkFileParams(strPotFileDirInIndex);
            strPotFileDirInIndex = NcPathFromUserChecker.strInputAppWorkFileDefault(defaultWorkCfg.dirInIndex);
            resultCheckFileDirInIndexDefault = NcParamCfgToDiskReleaser.checkOrCreateFiles(strPotFileDirInIndex);
        }
        outFuncWorkCfg = new NcParamFv(
                strPotWorkDir,
                strPotFileKeyOutSearch,
                strPotFileKeyInSearch,
                strPotFileDirOutIndex,
                strPotFileDirInIndex,
                inFuncReadedCfg.diskUserAlias,
                "",
                "",
                "",
                "",
                NcIdxFileManager.getIndexSubDirectories(strPotWorkDir)
        );
        
        return outFuncWorkCfg;
    }

    
    /**
     *
     * @param forCheck
     */
    public static void checkInDirFileReadable(File forCheck){
        if(forCheck.isFile()){
            NcAppHelper.outMessage(forCheck.getName());
        }
        if(forCheck.isDirectory()){
            File[] filesListFromDir = forCheck.listFiles();
            for(File itemOfFiles : filesListFromDir){
                checkInDirFileReadable(itemOfFiles);
            }
        }
    }

    /**
     *
     * @return
     */
    public static TreeMap<Long, NcDiskInfo> initDiskInfo(){
        TreeMap<Long, NcDiskInfo> sysDisk = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        for( Map.Entry<Long, NcDiskInfo> itemDisk : sysDisk.entrySet() ){
            NcAppHelper.outMessage("diskID: \t" + Long.toString(itemDisk.getValue().diskID));
            NcAppHelper.outMessage("humanAlias: \t" + itemDisk.getValue().humanAlias);
            NcAppHelper.outMessage("programAlias: \t" + itemDisk.getValue().programAlias);
            NcAppHelper.outMessage("strFileStore: \t" + itemDisk.getValue().strFileStore);
            NcAppHelper.outMessage("DiskLetter: \t" + itemDisk.getValue().diskLetter);
            NcAppHelper.outMessage("longSerialNumber: \t" + Long.toString(itemDisk.getValue().longSerialNumber));
            NcAppHelper.outMessage("strHexSerialNumber: \t" + itemDisk.getValue().strHexSerialNumber);
            NcAppHelper.outMessage("DiskFStype: \t" + itemDisk.getValue().diskFStype);
            NcAppHelper.outMessage("isReadonly: \t" + itemDisk.getValue().isReadonly);
            NcAppHelper.outMessage("availSpace: \t" + Long.toString(itemDisk.getValue().availSpace));
            NcAppHelper.outMessage("totalSpace: \t" + Long.toString(itemDisk.getValue().totalSpace));
            NcAppHelper.outMessage("unAllocatedSpace: \t" + Long.toString(itemDisk.getValue().unAllocatedSpace));
            NcAppHelper.outMessage("usedSpace: \t" + Long.toString(itemDisk.getValue().usedSpace));
        }
        return sysDisk;
    }
    
    /**
     *
     * @param arrStrReadedCfg
     * @return
     */
    public static NcParamFv parseEtcCfg(ArrayList<String> arrStrReadedCfg){
        NcParamFv paramReadedCfg;
        String indexPathTmp = "/ncfvdi";
        String keywordsOutOfSearchTmp = "keywordout.list";
        String keywordsInSearchTmp = "keywordin.list";
        String dirOutOfIndexTmp = "dirout.list";
        String dirInIndexTmp = "dirin.list";
        TreeMap<Integer, String> diskUserAliasTmp;
        diskUserAliasTmp = new TreeMap<Integer, String>();
        for( String itemStrCfg : arrStrReadedCfg ){
            String strFilter = itemStrCfg.replaceAll("\\s*(\\s|,|!|\"|\\||$|~|@|#|\\*|%|\\^|&|\\(|\\)|\\{|\\}|\\[|\\]|<|>|\\?)\\s*", "").toString();
            String[] arrStrKeyVal = strFilter.split("=");
            if(arrStrKeyVal.length == 2){
                String keyName = arrStrKeyVal[0].toLowerCase().toString();
                if(keyName.equalsIgnoreCase("indexpath")){
                    indexPathTmp = arrStrKeyVal[1].toString();
                }
                if(keyName.equalsIgnoreCase("file_name_keywords_out_of_search")){
                    keywordsOutOfSearchTmp = arrStrKeyVal[1].toString();
                }
                if(keyName.equalsIgnoreCase("file_name_keywords_in_search")){
                    keywordsInSearchTmp = arrStrKeyVal[1].toString();
                }
                if(keyName.equalsIgnoreCase("file_name_dir_out_of_index")){
                    dirOutOfIndexTmp = arrStrKeyVal[1].toString();
                }
                if(keyName.equalsIgnoreCase("file_name_dir_in_index")){
                    dirInIndexTmp = arrStrKeyVal[1].toString();
                }
                int intDiskID = -1;
                if(keyName.indexOf("alias") > -1){
                    String[] strAlias = keyName.split("_");
                    if(strAlias.length == 2){
                        if(strAlias[0].equalsIgnoreCase("alias")){
                            String strDiskID = "";
                            if( (strAlias[1].matches("[0-9]") ) ){
                                strDiskID = strAlias[1].toString();
                                if( strDiskID.length() > 0 ){
                                    intDiskID = Integer.parseInt(strDiskID);
                                }
                            }
                            if(intDiskID > -1){
                                diskUserAliasTmp.put((int) intDiskID, arrStrKeyVal[1].toString());
                            }
                        }
                    }
                }
            }
        }
        paramReadedCfg = new NcParamFv(
            indexPathTmp,
            keywordsOutOfSearchTmp,
            keywordsInSearchTmp,
            dirOutOfIndexTmp,
            dirInIndexTmp,
            diskUserAliasTmp,
            "",
            "",
            "",
            "",
            new TreeMap<Integer, File>()
        );
        return paramReadedCfg;
    }



    /**
     *
     * @return
     */
    public static ArrayList<String> getReadedLinesFromEtcCfg(){
        File fileCfg = NcIdxFileManager.getOrCreateCfgFile();
        return readCfg(fileCfg.getAbsolutePath());
    }

    /**
     *
     * @return
     */
    public static ArrayList<String> getRemTextForCfgFile(){
        ArrayList<String> strTextRemark = new ArrayList<String>();
        strTextRemark.add("##################################################");
        strTextRemark.add("#   This Config file generated automaticaly by");
        strTextRemark.add("#   NewControlFileViewer application, your must change parameters");
        strTextRemark.add("#   in the configuration options, wrong parameters automaticaly");
        strTextRemark.add("#   changed and rewrited to default value");
        strTextRemark.add("#   ");
        strTextRemark.add("#   WARNING !!! Remember, this file automaticaly rewrited,");
        strTextRemark.add("#   if your need make remarks, do it in the your files");
        strTextRemark.add("#   ");
        strTextRemark.add("#   In the text of options may be used next symbols: A-Z a-z 0-9 : \\ / _ -");
        strTextRemark.add("#   If first symbol of string has #, then this is remark string");
        strTextRemark.add("#   ");
        strTextRemark.add("#   For parameter indexpath, and Windows based systems, if not set");
        strTextRemark.add("#   drive letter mask: C:\\, then application choice disk with maximum");
        strTextRemark.add("#   of avalable space and create work folder in that disk");
        strTextRemark.add("#   ");
        strTextRemark.add("#   If the disk not avalable, default choesd maximum freespace disk");
        strTextRemark.add("#   and create index in default folder name");
        strTextRemark.add("#   ");
        strTextRemark.add("#   If you have removed disk drive with lagest avalable space, application");
        strTextRemark.add("#   create index in it, after remove this disk drive from computer, application");
        strTextRemark.add("#   in the next run, not found work directory and create new on the disk drive");
        strTextRemark.add("#   with maximum of avalable space");
        strTextRemark.add("#   ");
        strTextRemark.add("#   If the file_name_* parameters not have full path for files, then application");
        strTextRemark.add("#   searched it in the appPath/files folder, where this conf file");
        strTextRemark.add("#   On the Windows based systems folder for search files of file_name_* parameters");
        strTextRemark.add("#   also is his value for example: file_name_dir_in_index=/somedir/dirin.list");
        strTextRemark.add("#   application search this file appDriveLetter:/somedir/dirin.list");
        strTextRemark.add("#   Very simple in file_name_* parameters write need file name and put it");
        strTextRemark.add("#   in appPath/files/file.name");
        strTextRemark.add("#   Additional alias of disks writed in string after automaticaly");
        strTextRemark.add("#   created, whith numbers ID of disks, where disk ID your must");
        strTextRemark.add("#   view, if run this application with: -getdisks parameter");
        strTextRemark.add("##################################################");
        return strTextRemark;
    }

    /**
     *
     * @return
     */
    public static ArrayList<String> getDefaultParametersForCfg(){
        TreeMap<Long, NcDiskInfo> sysDisk = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        ArrayList<String> strTextRemark = new ArrayList<String>();
        NcParamFv defaultValues = getDefaultCfgValues();
        strTextRemark.add("indexpath=" + defaultValues.indexPath);
        strTextRemark.add("file_name_keywords_out_of_search=" + defaultValues.keywordsOutOfSearch);
        strTextRemark.add("file_name_keywords_in_search=" + defaultValues.keywordsInSearch);
        strTextRemark.add("file_name_dir_out_of_index=" + defaultValues.dirOutOfIndex);
        strTextRemark.add("file_name_dir_in_index=" + defaultValues.dirInIndex);
        for( Map.Entry<Long, NcDiskInfo> itemDisk : sysDisk.entrySet() ){
            strTextRemark.add("##################################################");
            strTextRemark.add("#_|_" + "disk_id_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().diskID);
            strTextRemark.add("#_|_" + "file_store_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().strFileStore);
            strTextRemark.add("#_|_" + "file_store_name_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().strFileStoreName);
            strTextRemark.add("#_|_" + "disk_serial_number_heximal_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().strHexSerialNumber);
            strTextRemark.add("#_|_" + "disk_avalable_space_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().availSpace + "\tbyte");
            strTextRemark.add("#_|_" + "disk_used_space_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().usedSpace + "\tbyte"
                    + " disk_total_space - disk_unallocated_space");
            strTextRemark.add("#_|_" + "disk_total_space_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().totalSpace + "\tbyte");
            strTextRemark.add("#_|_" + "disk_unallocated_space_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().unAllocatedSpace + "\tbyte");
            strTextRemark.add("#_|_" + "program_alias_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().programAlias);
            strTextRemark.add("##################################################");
            strTextRemark.add("alias_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().humanAlias);
        }
        return strTextRemark;
    }
    /**
     *
     * @param ncStrCfgPath
     */
    public static void createCfg(String ncStrCfgPath){
        ArrayList<String> strTextRemark = getRemTextForCfgFile();
        ArrayList<String> strParameters = getDefaultParametersForCfg();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(ncStrCfgPath)))
        {
            for(String itemStr : strTextRemark){
                String text = itemStr.toString();
                bw.write(text);
                bw.newLine();
            }
            for(String itemStr : strParameters){
                String text = itemStr.toString();
                bw.write(text);
                bw.newLine();
            }
        }
        catch(IOException ex){
            String strForExit = "Can't write parameters into file: "
                    + ncStrCfgPath;
            NcAppHelper.outMessage(strForExit);
            NcAppHelper.appExitWithMessage(ex.getMessage());
        }
    }
    /**
     *
     * @param linesParametersForUpdateCfg
     * @return
     */
    public static int updateEtcCfg(ArrayList<String> linesParametersForUpdateCfg){
        String pathToCfg = NcIdxFileManager.getOrCreateCfgFile().getAbsolutePath();
        ArrayList<String> strTextRemark = getRemTextForCfgFile();
        ArrayList<String> strParameters = linesParametersForUpdateCfg;
        int lines = 0;
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(pathToCfg)))
        {
            for(String itemStr : strTextRemark){
                String text = itemStr.toString();
                bw.write(text);
                bw.newLine();
                lines++;
            }
            for(String itemStr : strParameters){
                String text = itemStr.toString();
                bw.write(text);
                bw.newLine();
                lines++;
            }
        }
        catch(IOException ex){
            NcAppHelper.outMessage(ex.getMessage());
        }
        return lines;
    }

    /**
     *
     * @param linesValues
     * @return
     */
    public static ArrayList<String> getLinesParametersForUpdateCfg(NcParamFv linesValues){
        TreeMap<Long, NcDiskInfo> sysDisk = NcParamJournalDisk.getFromJournalDiskOrCreateIt();
        ArrayList<String> strTextRemark = new ArrayList<String>();
        
        strTextRemark.add("indexpath=" + linesValues.indexPath);
        strTextRemark.add("file_name_keywords_out_of_search=" + linesValues.keywordsOutOfSearch);
        strTextRemark.add("file_name_keywords_in_search=" + linesValues.keywordsInSearch);
        strTextRemark.add("file_name_dir_out_of_index=" + linesValues.dirOutOfIndex);
        strTextRemark.add("file_name_dir_in_index=" + linesValues.dirInIndex);
        for( Map.Entry<Long, NcDiskInfo> itemDisk : sysDisk.entrySet() ){
            strTextRemark.add("##################################################");
            strTextRemark.add("#_|_" + "disk_id_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().diskID);
            strTextRemark.add("#_|_" + "file_store_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().strFileStore);
            strTextRemark.add("#_|_" + "file_store_name_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().strFileStoreName);
            strTextRemark.add("#_|_" + "disk_serial_number_heximal_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().strHexSerialNumber);
            strTextRemark.add("#_|_" + "disk_avalable_space_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().availSpace + "\tbyte");
            strTextRemark.add("#_|_" + "disk_used_space_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().usedSpace + "\tbyte"
                    + " disk_total_space - disk_unallocated_space");
            strTextRemark.add("#_|_" + "disk_total_space_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().totalSpace + "\tbyte");
            strTextRemark.add("#_|_" + "disk_unallocated_space_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().unAllocatedSpace + "\tbyte");
            strTextRemark.add("#_|_" + "program_alias_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().programAlias);
            strTextRemark.add("##################################################");
            strTextRemark.add("alias_" + itemDisk.getValue().diskID + "=" + itemDisk.getValue().humanAlias);
        }
        return strTextRemark;
    }

    /**
     *
     * @param ncStrCfgPath
     * @return
     */
    public static ArrayList<String> readCfg(String ncStrCfgPath){
        ArrayList<String> strForReturn;
        strForReturn = new ArrayList<String>();
        try(BufferedReader br = new BufferedReader(new FileReader(ncStrCfgPath)))
        {
            String s;
            while((s=br.readLine())!=null){
                if( (s.indexOf("#") > 0) ){
                    String[] strSplit = s.split("#");
                    if ( strSplit.length > 0 ){
                        strForReturn.add(strSplit[0].trim());
                    }
                }
                if( (s.indexOf("#") == -1) && (s.indexOf("=") > 5)){
                    strForReturn.add(s.trim());
                }
            }
        }
         catch(IOException ex){
            NcAppHelper.outMessage(ex.getMessage());
        }   
        return strForReturn;
    }

    
}
