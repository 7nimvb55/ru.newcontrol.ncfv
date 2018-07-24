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
public enum NcStrLogLogicVar {
    
    
    
    LA_SET_VISIBLE_GUI("[LA_GUI_SET_VISIBLE_GUI]"),
    LA_GUI_WAIT_FOR_USER_INPUT("[LA_GUI_WAIT_FOR_USER_INPUT]"),
    LA_JPANEL_CENTER("[LA_GUI_JPANEL_CENTER]"),
    LA_JPANEL_PAGESTART("[LA_GUI_JPANEL_PAGESTART]"),
    LA_JPANEL_PAGEEND("[LA_GUI_JPANEL_PAGEEND]"),
    LA_JPANEL_LINESTART("[LA_GUI_JPANEL_LINESTART]"),
    LA_JPANEL_LINEEND("[LA_GUI_JPANEL_LINEEND]"),
    LA_PANEL_LINESTART("[LA_GUI_JPANEL_LINESTART]"),
    LA_JMENU("[LA_GUI_JMENU]"),
    LA_JMENUBAR("[LA_GUI_JMENUBAR]"),
    
    LA_CFG_WORK_GET_CURRENT("[LA_CFG_WORK_GET_CURRENT]"),
    LA_CFG_WORK_READ_FROM_FILE("[LA_CFG_WORK_READ_FROM_FILE]"),
    LA_CFG_WORK_GENERATE_ZERO("[LA_CFG_WORK_GENERATE_ZERO]"),
    LA_CFG_WORK_VALIDATE_APPLY("[LA_CFG_WORK_VALIDATE_APPLY]"),
    
    LA_CHECK("[LA_CFG_WORK_CFG]"),
    LA_CHECK_SUB_DIR("[LA_CFG_CHECK_SUB_DIRS]");
    private String strMsg;
    NcStrLogLogicVar(String strMsg){
        this.strMsg = strMsg;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicCfg#NcPreRunFileViewerGetCurrentWorkCfg() }
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicCfg#NcParamFvReaderReadDataRead() }
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicCfg#NcParamFvReaderReadDataGenerate() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicGUI#NcSwMainMenuGetMainMenu() }
     * <li>{@link ru.newcontrol.ncfv.NcLogLogicGUI#NcSwPanelCenterGetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#toLALRgetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineStart#toLALRgetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageEnd#toLALRgetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageStart#toLALRgetPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#toLALRcreateGuiPanel() }
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#toLALRendOfCreateGUI() }
     * </ul>
     * @return 
     */
    protected String getStr(){
        return strMsg;
    }
}
