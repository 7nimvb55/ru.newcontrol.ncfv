/*
 * Copyright 2018 wladimirowichbiaran.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.newcontrol.ncfv;

/**
 *
 * @author wladimirowichbiaran
 */
public enum NcStrLogMsgField {
    APP_LOGIC_NOW("[APPNOW]"),
    APP_LOGIC_NEXT_WAY_VAR("[APPNEXTWAYVAR]"),
    EXCEPTION_MSG("[EXCEPTION_MSG]"),
    ERROR("[ERROR]"),
    ERROR_CRITICAL("[ERROR][CRITICAL]"),
    WARNING("[WARNING]"),
    INFO("[INFO]"),
    INFO_LOGIC_POSITION("[INFOLOGICPOSITION]"),
    MSG_ERROR("[MSG][ERROR]"),
    MSG_ERROR_CRITICAL("[MSG][ERROR][CRITICAL]"),
    MSG_WARNING("[MSG][WARNING]"),
    MSG_INFO("[MSG][INFO]"),
    
    VARVAL("[VARVAL]"),
    VARNAME("[VARNAME]"),
    CHECK_RESULT("[CHECK_RESULT]"),
    
    TO_RETURN("[TO_RETURN]"),
    DISCARDED("[DISCARDED]"),
    IN_SET_DEFAULT_ERROR_GENERATE_ERROR_VAL("[INSETDEFAULTERRORGENERATEERRORVALUE]"),
    
    DELIMITER("[]"),
    TIME("[TIME]"),
    THREAD("[THREAD]"),
    THREAD_GROUP("[THREADGROUP]"),
    THREAD_GROUP_NAME("[THREADGROUPNAME]"),
    COUNT("[COUNT]"),
    CLASSLOADER("[CLASSLOADER]"),
    TOSTRING("[TOSTRING]"),
    NAME("[NAME]"),
    CANONICALNAME("[CANONICALNAME]"),
    ID("[ID]"),
    STATE("[STATE]"),
    STACK("[STACK]"),
    TRACE("[TRACE]"),
    FILENAME("[FILENAME]"),
    CLASSNAME("[CLASSNAME]"),
    METHODNAME("[METHODNAME]"),
    LINENUM("[LINENUM]"),
    NATIVE("[NATIVE]"),
    ELEMENTNUM("[ELEMENTNUM]"),
    MSG("[MSG]");
    private String strMsg;
    NcStrLogMsgField(String strMsg){
        this.strMsg = strMsg;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcAppHelper#appExitWithMessageFSAccess(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcAppHelper#appExitWithMessage(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcAppHelper#logException(java.lang.String, java.lang.Exception) }
     * <li>{@link ru.newcontrol.ncfv.NcAppHelper#outMessageToAppLogFile(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcAppHelper#strArrToConsoleOutPut(java.lang.String[]) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcDiskUtils#printToConsoleNcDiskInfo(ru.newcontrol.ncfv.NcDiskInfo) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcIndexManageIDs#checkDataForAllDirListFiles() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcLogColorizer#getHtmlStr(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcLogFileManager#createLogFile(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcLogFileManager#putToLogStr(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcLogFileManager#putToLog(java.util.TreeMap) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicCfg#NcParamFvReaderReadDataRead() }
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicCfg#NcParamFvReaderReadDataGenerate() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicGUI#NcSwMainMenuGetMainMenu() }
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicGUI#NcSwPanelCenterGetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcCreateWorkDir(ru.newcontrol.ncfv.NcDiskInfo) }
     * <li>{@link ru.newcontrol.ncfv.NcManageCfg#mcCheckAndCreateFolderStructure() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcParamCfgToDiskReleaser#getWorkFileParams(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcParamCfgToDiskReleaser#getIdxDirStructure(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcParamFvManager#isNcParamFvDataHashTrue(ru.newcontrol.ncfv.NcParamFv) }
     * <li>{@link ru.newcontrol.ncfv.NcParamFvManager#ncParamFvDataOutPut(ru.newcontrol.ncfv.NcParamFv) }
     * <li>{@link ru.newcontrol.ncfv.NcParamFvManager#checkToWrite(ru.newcontrol.ncfv.NcParamFv) }
     * <li>{@link ru.newcontrol.ncfv.NcParamFvManager#checkFromRead(ru.newcontrol.ncfv.NcParamFv) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilter(java.lang.String, java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcPathFromUserChecker#strInputPathFormatFilterForDefault(java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcPreIdxWork#checkTmpIDsData() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSrchGetResult#outSearchResult(java.util.TreeMap, java.util.TreeMap) }
     * <li>{@link ru.newcontrol.ncfv.NcSrchKeyWordInput#getIdDataForSplittedKeyWord(java.util.ArrayList) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwModalLogViewer#getTreeNodes(javax.swing.tree.DefaultMutableTreeNode) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#toLALRgetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineStart#toLALRgetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageEnd#toLALRgetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageStart#toLALRgetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#toLALRcreateGui() }
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#toLALRcreateGuiPanel() }
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#toLALRendOfCreateGUI() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#toLALRMain() }
     * </ul>
     * @return 
     */
    protected String getStr(){
        return strMsg;
    }
}
