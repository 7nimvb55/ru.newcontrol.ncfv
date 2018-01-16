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


/**
 *
 * @author Администратор
 */
public class NcPathFromUserChecker {
    /**
     * Shortly Prefix for maximum free space disk drive letter for Windows based systems
     * Method for check input string to any mask matches and append prefix with
     * letter of disk drive with maximum of free space readable and writable disk
     * space, for not windows system, returned input string path with root symbol
     * If the inputed path not matches of filters, then method returned default value
     * @param strInput
     * @param strDefault
     * @return string of path for potential work index directory
     */
    public static String strInputAppWorkDirFromUser(String strInput, String strDefault){
        String strOuput = strInputPathFormatFilter(strInput, strDefault);
        if( strPathWinNetworkStart(strOuput) ){
            if( !NcAppHelper.isWindows() ){
                return "/" + strOuput.substring(2);
            }
            return strOuput;
        }
        if( NcAppHelper.isWindows() ){
            if( strPathWinDiskLetterStart(strOuput) ){
                return strOuput;
            }
            if( strPathRootStart(strOuput) ){
                return strInputAddPrefixMaxFreeSpaceRoot(strOuput);
            }
            if( strPathWinSubDirStart(strOuput) ){
                return strInputAddPrefixMaxFreeSpaceRoot(strOuput);
            }
            if( strPathSymOrDigitsStart(strOuput) ){
                return strInputAddPrefixMaxFreeSpaceRoot(strOuput);
            }
        }
        if( !NcAppHelper.isWindows() ){
            if( strPathRootStart(strOuput) ){
                return strOuput;
            }
            if( strPathWinDiskLetterStart(strOuput) ){
                return "/" + strOuput.substring(3);
            }
            if( strPathWinSubDirStart(strOuput) ){
                return "/" + strOuput.substring(1);
            }
            if( strPathSymOrDigitsStart(strOuput) ){
                return "/" + strOuput;
            }
        }
        return strOuput;
    }

    /**
     *
     * @param strDefault
     * @return
     */
    public static String strInputAppWorkDirDefault(String strDefault){
        String strOuput = strInputPathFormatFilterForDefault(strDefault);
        if( strPathWinNetworkStart(strOuput) ){
            if( !NcAppHelper.isWindows() ){
                return "/" + strOuput.substring(2);
            }
            return strOuput;
        }
        if( NcAppHelper.isWindows() ){
            if( strPathWinDiskLetterStart(strOuput) ){
                return strOuput;
            }
            if( strPathRootStart(strOuput) ){
                return strInputAddPrefixMaxFreeSpaceRoot(strOuput);
            }
            if( strPathWinSubDirStart(strOuput) ){
                return strInputAddPrefixMaxFreeSpaceRoot(strOuput);
            }
            if( strPathSymOrDigitsStart(strOuput) ){
                return strInputAddPrefixMaxFreeSpaceRoot(strOuput);
            }
        }
        if( !NcAppHelper.isWindows() ){
            if( strPathRootStart(strOuput) ){
                return strOuput;
            }
            if( strPathWinDiskLetterStart(strOuput) ){
                return "/" + strOuput.substring(3);
            }
            if( strPathWinSubDirStart(strOuput) ){
                return "/" + strOuput.substring(1);
            }
            if( strPathSymOrDigitsStart(strOuput) ){
                return "/" + strOuput;
            }
        }
        return strOuput;
    }
    /**
     * Shortly Prefix for default application work directory with /files in subDir
     * Method for check input string to any mask matches and append prefix with
     * letter of disk drive and path for application work sub directory /files
     * if letter not writed in the parameters for windows system
     * If the inputed path not matches of filters, then method returned default value
     * @param strInput
     * @param strDefault
     * @return string of path for potential files with search condition data
     */
    public static String strInputAppWorkFileFromUser(String strInput, String strDefault){
        String strOuput = strInputPathFormatFilter(strInput, strDefault);
        if( strPathWinNetworkStart(strOuput) ){
            if( !NcAppHelper.isWindows() ){
                return strInputAddPrefixWorkAppDir(strOuput.substring(2));
            }
            return strOuput;
        }
        if( NcAppHelper.isWindows() ){
            if( strPathWinDiskLetterStart(strOuput) ){
                return strOuput;
            }
            if( strPathRootStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput);
            }
            if( strPathWinSubDirStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput);
            }
            if( strPathSymOrDigitsStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput);
            }
        }
        if( !NcAppHelper.isWindows() ){
            if( strPathRootStart(strOuput) ){
                return strOuput;
            }
            if( strPathWinDiskLetterStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput.substring(3));
            }
            if( strPathWinSubDirStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput.substring(1));
            }
            if( strPathSymOrDigitsStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput);
            }
        }
        return strOuput;
    }

    /**
     *
     * @param strDefault
     * @return
     */
    public static String strInputAppWorkFileDefault(String strDefault){
        String strOuput = strInputPathFormatFilterForDefault(strDefault);
        if( strPathWinNetworkStart(strOuput) ){
            if( !NcAppHelper.isWindows() ){
                return strInputAddPrefixWorkAppDir(strOuput.substring(2));
            }
            return strOuput;
        }
        if( NcAppHelper.isWindows() ){
            if( strPathWinDiskLetterStart(strOuput) ){
                return strOuput;
            }
            if( strPathRootStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput);
            }
            if( strPathWinSubDirStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput);
            }
            if( strPathSymOrDigitsStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput);
            }
        }
        if( !NcAppHelper.isWindows() ){
            if( strPathRootStart(strOuput) ){
                return strOuput;
            }
            if( strPathWinDiskLetterStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput.substring(3));
            }
            if( strPathWinSubDirStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput.substring(1));
            }
            if( strPathSymOrDigitsStart(strOuput) ){
                return strInputAddPrefixWorkAppDir(strOuput);
            }
        }
        return strOuput;
    }
    /**
     * Function add for input Path string prefix, for example in value your input:
     * some_file.name, this file have not absolutly path, this function add
     * /work/path/to/this_app/files/some_file.name, for Windows
     * D:\work\path\to\this_app\files\some_file.name
     * @param strInput
     * @return 
     */
    public static String strInputAddPrefixWorkAppDir(String strInput){
        String strAppPath = NcIdxFileManager.getAppWorkDirStrPath();
        if( strAppPath.length() == 0 ){
            return strInput;
        }
        strAppPath = NcIdxFileManager.strPathCombiner(strAppPath, "files");
        return NcIdxFileManager.strPathCombiner(strAppPath, strInput);
    }

    /**
     *
     * @param strInput
     * @return
     */
    public static String strInputAddPrefixWorkAppRoot(String strInput){
        File strAppPath = NcIdxFileManager.getAppWorkDirFile();
        if( strAppPath == null ){
            return strInput;
        }
        String strAppRoot = strAppPath.toPath().getRoot().toString();
        return NcIdxFileManager.strPathCombiner(strAppRoot, strInput);
    }

    /**
     *
     * @param strInput
     * @return
     */
    public static String strInputAddPrefixMaxFreeSpaceRoot(String strInput){
        long longFreeSpace = 0;
        String strAppRoot = "";
        for( File itemFile : File.listRoots() ){
            if( itemFile.canRead() && itemFile.canWrite() ){
                if( itemFile.getFreeSpace() > longFreeSpace ){
                    longFreeSpace = itemFile.getFreeSpace();
                    strAppRoot = NcIdxFileManager.getStrCanPathFromFile(itemFile);
                }
            }
        }
        return NcIdxFileManager.strPathCombiner(strAppRoot, strInput);
    }
    /**
     * Function for read input string format, and return default value for
     * filtered strings with not supported masks in the string content
     * @param strInput
     * @param strInputDefault
     * @param strDefault
     * @return 
     */
    public static String strInputPathFormatFilter(String strInput, String strInputDefault){
        String strDefault = strInputPathFormatFilterForDefault(strInputDefault);
        if( strDefault.equalsIgnoreCase(strInput)){
            NcAppHelper.outMessage("String equal");
            NcAppHelper.outMessage("return Default: " + strDefault);
            NcAppHelper.outMessage("or return Input: " + strInput);
            return strDefault;
        }
        if( !strPathValidContinue(strInput) ){
            NcAppHelper.outMessage("Continue not valid");
            NcAppHelper.outMessage("return Default: " + strDefault);
            NcAppHelper.outMessage("Breaked: " + strInput);
            return strDefault;
        }
        if( !strPathValidStart(strInput) ){
            NcAppHelper.outMessage("Start not valid");
            NcAppHelper.outMessage("return Default: " + strDefault);
            NcAppHelper.outMessage("Breaked: " + strInput);
            return strDefault;
        }
        if( !NcAppHelper.isWindows() ){
            if( strPathRootStartForNotWindows(strInput) ){
                NcAppHelper.outMessage("Path for root creation");
                NcAppHelper.outMessage("return: " + strInput);
                return strInput;
            }
        }
        return strInput;
    }

    /**
     *
     * @param strDefault
     * @return
     */
    public static String strInputPathFormatFilterForDefault(String strDefault){

        if( !strPathValidContinue(strDefault) ){
            NcAppHelper.outMessage("path continue not valid - default stage");
            NcAppHelper.outMessage("breaked Default: " + strDefault);
            
            strDefault = NcIdxFileManager.strPathCombiner(NcIdxFileManager.getAppWorkDirStrPath(), "/wrongDefaults/f_" + System.nanoTime() + ".error");
            return strDefault;
        }
        if( !strPathValidStart(strDefault) ){
            NcAppHelper.outMessage("path start not valid - default stage");
            NcAppHelper.outMessage("breaked Default: " + strDefault);
            strDefault = NcIdxFileManager.strPathCombiner(NcIdxFileManager.getAppWorkDirStrPath(), "/wrongDefaults/f_" + System.nanoTime() + ".error");
            return strDefault;
        }
        if( !NcAppHelper.isWindows() ){
            if( strPathRootStartForNotWindows(strDefault) ){
                NcAppHelper.outMessage("Path for root creation - default stage");
                NcAppHelper.outMessage("breaked Default: " + strDefault);
                strDefault = NcIdxFileManager.strPathCombiner(NcIdxFileManager.getAppWorkDirStrPath(), strDefault);
                return strDefault;
            }
        }
        return strDefault;
    }
    /**
     * Return true for not Windows system and Path string starts with root for
     * length > 2 or root and dot for length > 3, next symbol after string starn may be
     * digit or alfabettical symbols
     * @param inFuncStrPath
     * @return true in not Windows, for "/[0-9a-zA-Z]" or "/.[0-9a-zA-Z]"
     * in other string start, return false
     */
    public static boolean strPathRootStartForNotWindows(String inFuncStrPath){
        if( NcAppHelper.isWindows() ){
            return false;
        }
        if( inFuncStrPath.length() > 2 ){
            if( inFuncStrPath.substring(0, 1).equalsIgnoreCase("/")
                    && inFuncStrPath.substring(1, 2).matches("[0-9a-zA-Z]") ){
                return true;
            }
        }
        if( inFuncStrPath.length() > 3 ){
            if( inFuncStrPath.substring(0, 1).equalsIgnoreCase("/")
                    && inFuncStrPath.substring(1, 2).equalsIgnoreCase(".")
                    && inFuncStrPath.substring(2, 3).matches("[0-9a-zA-Z]")){
                return true;
            }
        }
        return false;
    }
    


    /**
     *
     * @param inFuncStrPath
     * @return
     */
    public static boolean strPathWinNetworkStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 2).equalsIgnoreCase("\\\\")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        return false;
    }

    /**
     *
     * @param inFuncStrPath
     * @return
     */
    public static boolean strPathWinDiskLetterStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 1).matches("[a-zA-Z]")
                && strSubStart.substring(1, 3).equalsIgnoreCase(":\\") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[a-zA-Z]")
                && strSubStart.substring(1, 3).equalsIgnoreCase(":/") ){
            return true;
        }
        return false;
    }

    /**
     *
     * @param inFuncStrPath
     * @return
     */
    public static boolean strPathRootStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 1).equalsIgnoreCase("/")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]") ){
            return true;
        }
        return false;
    }

    /**
     *
     * @param inFuncStrPath
     * @return
     */
    public static boolean strPathWinSubDirStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 1).equalsIgnoreCase("\\")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]") ){
            return true;
        }
        return false;
    }

    /**
     *
     * @param inFuncStrPath
     * @return
     */
    public static boolean strPathSymOrDigitsStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).equalsIgnoreCase("\\")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]")
                && strSubStart.substring(2, 3).equalsIgnoreCase("\\") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).equalsIgnoreCase("/")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]")
                && strSubStart.substring(2, 3).equalsIgnoreCase("/") ){
            return true;
        }
        return false;
    }
    /**
     * If input string in first three symblos has function mask content, then function
     * return true
     * @param inFuncStrPath
     * @return true if three symbols of start string in mask
     * other string return false
     */
    public static boolean strPathValidStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 1).matches("[a-zA-Z]")
                && strSubStart.substring(1, 3).equalsIgnoreCase(":\\") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[a-zA-Z]")
                && strSubStart.substring(1, 3).equalsIgnoreCase(":/") ){
            return true;
        }
        if( strSubStart.substring(0, 2).equalsIgnoreCase("\\\\")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).equalsIgnoreCase("\\")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).equalsIgnoreCase("/")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).equalsIgnoreCase("\\")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]")
                && strSubStart.substring(2, 3).equalsIgnoreCase("\\") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).equalsIgnoreCase("/")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        if( strSubStart.substring(0, 1).matches("[0-9a-zA-Z]")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]")
                && strSubStart.substring(2, 3).equalsIgnoreCase("/") ){
            return true;
        }
        
        return false;
    }
    /**
     * Check for input String Path in first tree symbols have not unmask characters
     * 
     * @param inFuncStrPath
     * @return true if mask symbols not found
     * false if one of feltered mask have matches in the string above 3 positions
     */
    public static boolean strPathValidContinue(String inFuncStrPath){
        if( inFuncStrPath.length() < 3 ){
            return true;
        }
        if(inFuncStrPath.toUpperCase().substring(2).indexOf("\\\\") > 0){
            return false;
        }
        if(inFuncStrPath.toUpperCase().substring(2).indexOf("//") > 0){
            return false;
        }
        if(inFuncStrPath.toUpperCase().substring(2).indexOf(":/") > 0){
            return false;
        }
        if(inFuncStrPath.toUpperCase().substring(2).indexOf(":\\") > 0){
            return false;
        }
        if(inFuncStrPath.toUpperCase().substring(2).indexOf("::") > 0){
            return false;
        }
        if(inFuncStrPath.toUpperCase().substring(2).indexOf(":") > 0){
            return false;
        }
        if(inFuncStrPath.toUpperCase().substring(2).indexOf("\\/") > 0){
            return false;
        }
        return true;
    }
    
}
