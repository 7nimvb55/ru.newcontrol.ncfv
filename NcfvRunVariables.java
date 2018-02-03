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

/**
 *
 * @author wladimirowichbiaran
 */
public class NcfvRunVariables {
    private static boolean devStage = true;
    
    private static boolean noOutToConsole = true;
    
    private static boolean outWithTrace = false;
    private static boolean outWithPrintFunc = false;
    private static boolean outWithFileName = false;
    
    private static final int LOGLINES = 10000;
    private static boolean boolCanRecord = false;
    
    private static boolean outToLogFile = true;
    private static boolean outToLogWithTrace = true;
    private static boolean outToLogPrintFunc = true;
    private static boolean outToLogFileName = true;
    private static boolean outToLogNewRecordAppend = false;
    
    private static boolean boolLALRMakeMain = true;
    private static boolean boolLALRNcSwIdxMngAppCreateGui = true;
    private static boolean boolLALRNcSwMainMenuMainMenu = true;
    private static boolean boolLALRNcSwPanelPageStartgetPanel = true;
    private static boolean boolLALRNcSwPanelPageEndgetPanel = true;
    private static boolean boolLALRNcSwPanelLineStartgetPanel = true;
    private static boolean boolLALRNcSwPanelLineEndgetPanel = true;
    private static boolean boolLALRNcSwPanelCentergetPanel = true;
    
    private static boolean boolLALRNcParamFvReaderReadDataFromWorkCfg = true;
    private static boolean boolLALRNcPreRunFileViewerGetCurrentWorkCfg = true;
    private static boolean boolLALRNcPreRunFileViewerValidateAndApplyCfg = true;
    
    private static boolean boolLALRNcIdxFileManagerFileExistRWAccessChecker = true;
    
    protected static void setCanRecord(){
        boolCanRecord = true;
    }
    
    protected static boolean getCanRecord(){
        return boolCanRecord;
    }
    protected static boolean getIncludeFile(){
        return outWithFileName;
    }
    protected static boolean getTraceWithPrintFunc(){
        return outWithPrintFunc;
    }
    protected static boolean getWithTrace(){
        return outWithTrace;
    }
    protected static boolean getStage(){
        return devStage;
    }
    protected static boolean isNoOutToConsole(){
        return noOutToConsole;
    }
    protected static int getLogLinesCount(){
        return LOGLINES;
    }
    protected static boolean isOutToLogFile(){
        return outToLogFile;
    }
    protected static boolean isOutToLogFileIncludeFile(){
        return outToLogFileName;
    }
    protected static boolean isOutToLogFileTraceWithPrintFunc(){
        return outToLogPrintFunc;
    }
    protected static boolean isOutToLogFileWithTrace(){
        return outToLogWithTrace;
    }
    protected static boolean isOutToLogNewRecordAppend(){
        return outToLogNewRecordAppend;
    }
    protected static boolean isLALRMakeMain(){
        return boolLALRMakeMain;
    }
    protected static boolean isLALRNcSwIdxMngAppCreateGui(){
        return boolLALRNcSwIdxMngAppCreateGui;
    }
    protected static boolean isLALRNcSwMainMenuMainMenu(){
        return boolLALRNcSwMainMenuMainMenu;
    }
    protected static boolean isLALRNcSwPanelPageStartgetPanel(){
        return boolLALRNcSwPanelPageStartgetPanel;
    }
    protected static boolean isLALRNcSwPanelPageEndgetPanel(){
        return boolLALRNcSwPanelPageEndgetPanel;
    }
    protected static boolean isLALRNcSwPanelLineStartgetPanel(){
        return boolLALRNcSwPanelLineStartgetPanel;
    }
    protected static boolean isLALRNcSwPanelLineEndgetPanel(){
        return boolLALRNcSwPanelLineEndgetPanel;
    }
    protected static boolean isLALRNcSwPanelCentergetPanel(){
        return boolLALRNcSwPanelCentergetPanel;
    }
    protected static boolean isLALRNcParamFvReaderReadDataFromWorkCfg(){
        return boolLALRNcParamFvReaderReadDataFromWorkCfg;
    }
    protected static boolean isLALRNcPreRunFileViewerGetCurrentWorkCfg(){
        return boolLALRNcPreRunFileViewerGetCurrentWorkCfg;
    }
    protected static boolean isLALRNcPreRunFileViewerValidateAndApplyCfg(){
        return boolLALRNcPreRunFileViewerValidateAndApplyCfg;
    }
    protected static boolean isLALRNcIdxFileManagerFileExistRWAccessChecker(){
        return boolLALRNcIdxFileManagerFileExistRWAccessChecker;
    }
}
