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
public enum NcStrGUIComponent {
    SMAIN("MAIN"),
    SMODAL("MODAL"),
    SENVIRONMENT("ENVIRONMENT"),
    SPROPERTIES("PROPERTIES"),
    SLOGVIEW("LOGVIEW"),
    
    SCENTER("CENTER"),
    SPAGESTART("PAGESTART"),
    SPAGEEND("PAGEEND"),
    SLINESTART("LINESTART"),
    SLINEEND("LINEEND"),
    
    SJCOMPONENT("JCOMPONENT"),
    SJSCROLLPANE("JSCROLLPANE"),
    SJFRAME("JFRAME"),
    SJDIALOG("JDIALOG"),
    
    SJMENUBAR("JMENUBAR"),
    SJMENU("JMENU"),
    SJMENUITEM("JMENUITEM"),
    
    SJPANEL("JPANEL"),
    SJTREE("JTREE"),
    SJTABLE("JTABLE"),
    SJTABBEDPANE("JTABBEDPANE"),
    
    
    STEXTFIELD("TEXTFIELD"),
    SJLABEL("JLABEL"),
    SJBUTTON("JBUTTON"),
    SJPROGRESSBAR("JPROGRESSBAR"),
    
    SSEARCH("SEARCH"),
    SSTACK("STACK"),
    SWORK("WORK"),
    SOUTPUT("OUTPUT"),
    SMANAGE("MANAGE"),
    SDIRECTORY("DIRECTORY");
    
    /**
     * Constructor
     */
    private String strMsg;
    NcStrGUIComponent(String strMsg){
        this.strMsg = strMsg;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMenuItems#getEnvironmentViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwMenuItems#getPropertiesViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwModalLogViewer#getDialogLogViewer(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelCenter#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineStart#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageStart#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwThreadManager#setToViewSearchedResult(ru.newcontrol.ncfv.NcSwGUIComponentStatus, java.lang.String) }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#createGui() }
     * </ul>
     * @return 
     */
    protected String getStr(){
        return strMsg;
    }
}
