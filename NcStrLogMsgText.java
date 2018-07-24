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
public enum NcStrLogMsgText {
    RUN_WITH_OUT_ARGS("Run with out args"),
    GUI_CREATE_JPANEL_FOR_MAIN_FRAME("Gui create JPanel for main frame"),
    APP_LOADER_START("[APP_LOADER_START]"),
    APP_GUI_START("Application start in GUI mode"),
    APP_ERROR_EXIT("Application stop with critical error"),
    LOG_CREATE("Log file created"),
    LOG_RECORD_APPEND("Append new record"),
    CALLED_ERROR_FOR_FILE_OPERATION("Error in file operation, NcIdxFileManager.getErrorForFileOperation() called"),
    LA_CHECK("[LA_WORK_CFG]"),
    LA_CHECK_SUB_DIR("[LA_CHECK_SUB_DIRS]"),
    NOT_EXIST_OR_READ_WRITE_PERMISSIONS_FOR_FILE("[NOT_EXIST_OR_READ_WRITE_PERMISSIONS_FOR_FILE]");
    private String strMsg;
    NcStrLogMsgText(String strMsg){
        this.strMsg = strMsg;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcLogFileManager#createLogFile(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcLogFileManager#putToLogStr(java.lang.String) }
     * <li>{@link ru.newcontrol.ncfv.NcLogFileManager#putToLog(java.util.TreeMap) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#toLALRcreateGui() }
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#toLALRcreateGuiPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.Ncfv#toLALRMain() }
     * </ul>
     * @return 
     */
    protected String getStr(){
        return strMsg;
    }
}
