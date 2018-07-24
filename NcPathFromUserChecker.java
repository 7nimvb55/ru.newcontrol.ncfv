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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#validateAndApplyCfg(ru.newcontrol.ncfv.NcParamFv) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     * Shortly Prefix for maximum free space disk drive letter for Windows based systems
     * Method for check input string to any mask matches and append prefix with
     * letter of disk drive with maximum of free space readable and writable disk
     * space, for not windows system, returned input string path with root symbol
     * If the inputed path not matches of filters, then method returned default value
     * @param strInput
     * @param strDefault
     * @return string of path for potential work index directory
     */
    protected static String strInputAppWorkDirFromUser(String strInput, String strDefault){
        if( strInput.equals(strDefault) ){
            return strDefault;
        }
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#validateAndApplyCfg(ru.newcontrol.ncfv.NcParamFv) }
     * </ul>
     * @param strDefault
     * @return
     */
    protected static String strInputAppWorkDirDefault(String strDefault){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#validateAndApplyCfg(ru.newcontrol.ncfv.NcParamFv) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#main(java.lang.String[]) }
     * </ul>
     * Shortly Prefix for default application work directory with /files in subDir
     * Method for check input string to any mask matches and append prefix with
     * letter of disk drive and path for application work sub directory /files
     * if letter not writed in the parameters for windows system
     * If the inputed path not matches of filters, then method returned default value
     * @param strInput
     * @param strDefault
     * @return string of path for potential files with search condition data
     */
    protected static String strInputAppWorkFileFromUser(String strInput, String strDefault){
        if( strInput.equals(strDefault) ){
            return strDefault;
        }
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPreRunFileViewer#validateAndApplyCfg(ru.newcontrol.ncfv.NcParamFv) }
     * </ul>
     * @param strDefault
     * @return
     */
    protected static String strInputAppWorkFileDefault(String strDefault){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileDefault(java.lang.String) }
     * </ul>
     * Function add for input Path string prefix, for example in value your input:
     * some_file.name, this file have not absolutly path, this function add
     * /work/path/to/this_app/files/some_file.name, for Windows
     * D:\work\path\to\this_app\files\some_file.name
     * @param strInput
     * @return 
     */
    private static String strInputAddPrefixWorkAppDir(String strInput){
        String strWorkCfgPath = NcIdxFileManager.getWorkCfgPath();
        String strAppPath = NcIdxFileManager.getAppWorkDirStrPath();
        if( strAppPath.length() == 0 ){
            return strInput;
        }
        strAppPath = NcIdxFileManager.strPathCombiner(strAppPath, "files");
        return NcIdxFileManager.strPathCombiner(strAppPath, strInput);
    }

    /**
     * Not used
     * @param strInput
     * @return
     */
    private static String strInputAddPrefixWorkAppRoot(String strInput){
        File strAppPath = NcIdxFileManager.getAppWorkDirFile();
        if( strAppPath == null ){
            return strInput;
        }
        String strAppRoot = strAppPath.toPath().getRoot().toString();
        return NcIdxFileManager.strPathCombiner(strAppRoot, strInput);
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirDefault(java.lang.String) }
     * </ul>
     * @param strInput
     * @return
     */
    private static String strInputAddPrefixMaxFreeSpaceRoot(String strInput){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileFromUser(java.lang.String, java.lang.String) }
     * </ul>
     * Function for read input string format, and return default value for
     * filtered strings with not supported masks in the string content
     * @param strInput
     * @param strInputDefault
     * @param strDefault
     * @return 
     */
    private static String strInputPathFormatFilter(String strInput, String strInputDefault){
        String strDefault = strInputPathFormatFilterForDefault(strInputDefault);
        if( strDefault.equalsIgnoreCase(strInput)){
            NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
            + NcStrLogMsgField.CHECK_RESULT.getStr()
            + NcStrServiceMsg.STRING_EQUAL.getStr()
            + NcStrLogMsgField.TO_RETURN.getStr()
            + NcStrLogMsgField.VARVAL.getStr()
            + NcStrVarDescription.STR_DEFAULT.getStr() + strDefault
            + NcStrLogMsgField.VARVAL.getStr()
            + NcStrVarDescription.STR_INPUT.getStr() + strInput);
            return strDefault;
        }
        if( !strPathValidContinue(strInput) ){
            NcAppHelper.outMessage(NcStrLogMsgField.WARNING.getStr()
            + NcStrLogMsgField.CHECK_RESULT.getStr()
            + NcStrServiceMsg.PATH_CONTINUE_NOT_VALID.getStr()
            + NcStrLogMsgField.TO_RETURN.getStr()
            + NcStrLogMsgField.VARVAL.getStr()
            + NcStrVarDescription.STR_DEFAULT.getStr() + strDefault
            + NcStrLogMsgField.DISCARDED.getStr()
            + NcStrLogMsgField.VARVAL.getStr()
            + NcStrVarDescription.STR_INPUT.getStr() + strInput);
            return strDefault;
        }
        if( !strPathValidStart(strInput) ){
            NcAppHelper.outMessage(NcStrLogMsgField.WARNING.getStr()
            + NcStrLogMsgField.CHECK_RESULT.getStr()
            + NcStrServiceMsg.PATH_START_NOT_VALID.getStr()
            + NcStrLogMsgField.TO_RETURN.getStr()
            + NcStrLogMsgField.VARVAL.getStr()
            + NcStrVarDescription.STR_DEFAULT.getStr() + strDefault
            + NcStrLogMsgField.DISCARDED.getStr()
            + NcStrLogMsgField.VARVAL.getStr()
            + NcStrVarDescription.STR_INPUT.getStr() + strInput);
            return strDefault;
        }
        if( !NcAppHelper.isWindows() ){
            if( strPathRootStartForNotWindows(strInput) ){
                NcAppHelper.outMessage(NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.CHECK_RESULT.getStr()
                + NcStrServiceMsg.PATH_FOR_NOT_WINDOWS_SYSTEM.getStr()
                + NcStrLogMsgField.TO_RETURN.getStr()
                + NcStrLogMsgField.VARVAL.getStr()
                + NcStrVarDescription.STR_INPUT.getStr() + strInput);
                return strInput;
            }
        }
        return strInput;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirDefault(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileDefault(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilter(java.lang.String, java.lang.String) }
     * </ul>
     * @param strDefault
     * @return
     */
    private static String strInputPathFormatFilterForDefault(String strDefault){

        if( !strPathValidContinue(strDefault) ){
            String strGenerateErrorVal = NcIdxFileManager.strPathCombiner(
                NcIdxFileManager.getAppWorkDirStrPath(),
                "/wrongDefaults/f_" + System.nanoTime() + ".error");
            NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
                + NcStrLogMsgField.CHECK_RESULT.getStr()
                + NcStrServiceMsg.PATH_CONTINUE_NOT_VALID.getStr()
                + NcStrLogMsgField.IN_SET_DEFAULT_ERROR_GENERATE_ERROR_VAL.getStr()
                + NcStrLogMsgField.TO_RETURN.getStr()
                + NcStrLogMsgField.VARVAL.getStr()
                + NcStrVarDescription.STR_GENERATE_ERROR_VAL.getStr()
                + strGenerateErrorVal
                + NcStrLogMsgField.DISCARDED.getStr()
                + NcStrLogMsgField.VARVAL.getStr()
                + NcStrVarDescription.STR_DEFAULT.getStr() + strDefault);
            return strGenerateErrorVal;
        }
        if( !strPathValidStart(strDefault) ){
            String strGenerateErrorVal = NcIdxFileManager.strPathCombiner(
                NcIdxFileManager.getAppWorkDirStrPath(),
                "/wrongDefaults/f_" + System.nanoTime() + ".error");
            NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
                + NcStrLogMsgField.CHECK_RESULT.getStr()
                + NcStrServiceMsg.PATH_START_NOT_VALID.getStr()
                + NcStrLogMsgField.IN_SET_DEFAULT_ERROR_GENERATE_ERROR_VAL.getStr()
                + NcStrLogMsgField.TO_RETURN.getStr()
                + NcStrLogMsgField.VARVAL.getStr()
                + NcStrVarDescription.STR_GENERATE_ERROR_VAL.getStr()
                + strGenerateErrorVal
                + NcStrLogMsgField.DISCARDED.getStr()
                + NcStrLogMsgField.VARVAL.getStr()
                + NcStrVarDescription.STR_DEFAULT.getStr() + strDefault);
            return strGenerateErrorVal;
        }
        if( !NcAppHelper.isWindows() ){
            if( strPathRootStartForNotWindows(strDefault) ){
                String strGenerateErrorVal = NcIdxFileManager.strPathCombiner(
                    NcIdxFileManager.getAppWorkDirStrPath(),
                    "/wrongDefaults/f_" + System.nanoTime() + ".error");
                NcAppHelper.outMessage(NcStrLogMsgField.ERROR.getStr()
                    + NcStrLogMsgField.CHECK_RESULT.getStr()
                    + NcStrServiceMsg.PATH_FOR_NOT_WINDOWS_SYSTEM.getStr()
                    + NcStrLogMsgField.IN_SET_DEFAULT_ERROR_GENERATE_ERROR_VAL.getStr()
                    + NcStrLogMsgField.TO_RETURN.getStr()
                    + NcStrLogMsgField.VARVAL.getStr()
                    + NcStrVarDescription.STR_GENERATE_ERROR_VAL.getStr()
                    + strGenerateErrorVal
                    + NcStrLogMsgField.DISCARDED.getStr()
                    + NcStrLogMsgField.VARVAL.getStr()
                    + NcStrVarDescription.STR_DEFAULT.getStr() + strDefault);
                return strGenerateErrorVal;
            }
        }
        return strDefault;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilter(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilterForDefault(java.lang.String) }
     * </ul>
     * Return true for not Windows system and Path string starts with root for
     * length > 2 or root and dot for length > 3, next symbol after string starn may be
     * digit or alfabettical symbols
     * @param inFuncStrPath
     * @return true in not Windows, for "/[0-9a-zA-Z]" or "/.[0-9a-zA-Z]"
     * in other string start, return false
     */
    private static boolean strPathRootStartForNotWindows(String inFuncStrPath){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirDefault(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileDefault(java.lang.String) }
     * </ul>
     * @param inFuncStrPath
     * @return
     */
    private static boolean strPathWinNetworkStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 2).equalsIgnoreCase("\\\\")
                && strSubStart.substring(2, 3).matches("[0-9a-zA-Z]") ){
            return true;
        }
        return false;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirDefault(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileDefault(java.lang.String) }
     * </ul>
     * @param inFuncStrPath
     * @return
     */
    private static boolean strPathWinDiskLetterStart(String inFuncStrPath){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirDefault(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileDefault(java.lang.String) }
     * </ul>
     * @param inFuncStrPath
     * @return
     */
    private static boolean strPathRootStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 1).equalsIgnoreCase("/")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]") ){
            return true;
        }
        return false;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirDefault(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileDefault(java.lang.String) }
     * </ul>
     * @param inFuncStrPath
     * @return
     */
    private static boolean strPathWinSubDirStart(String inFuncStrPath){
        String strSubStart = inFuncStrPath.toUpperCase().substring(0, 3);
        if( strSubStart.substring(0, 1).equalsIgnoreCase("\\")
                && strSubStart.substring(1, 2).matches("[0-9a-zA-Z]") ){
            return true;
        }
        return false;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkDirDefault(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileFromUser(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputAppWorkFileDefault(java.lang.String) }
     * </ul>
     * @param inFuncStrPath
     * @return
     */
    private static boolean strPathSymOrDigitsStart(String inFuncStrPath){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilter(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilterForDefault(java.lang.String) }
     * </ul>
     * If input string in first three symblos has function mask content, then function
     * return true
     * @param inFuncStrPath
     * @return true if three symbols of start string in mask
     * other string return false
     */
    private static boolean strPathValidStart(String inFuncStrPath){
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilter(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilterForDefault(java.lang.String) }
     * </ul>
     * Check for input String Path in first tree symbols have not unmask characters
     * 
     * @param inFuncStrPath
     * @return true if mask symbols not found
     * false if one of feltered mask have matches in the string above 3 positions
     */
    private static boolean strPathValidContinue(String inFuncStrPath){
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
