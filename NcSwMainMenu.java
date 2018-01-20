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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwMainMenu {
    public static JMenuBar getMainMenu(NcSwGUIComponentStatus lComp){
        JMenuBar menuMain = new JMenuBar();
        toLALRMainMenu();
        menuMain.add(getMenuFile());
        menuMain.add(getMenuDevelop(lComp));
        menuMain.add(getMenuService());
        menuMain.add(getMenuHelp());
        return menuMain;
    }
    public static JMenu getMenuFile(){
        JMenu menuFile = new JMenu("File");
        menuFile.add(NcSwMenuItems.getSubDirChecker());
        return menuFile;
    }
    public static JMenu getMenuDevelop(NcSwGUIComponentStatus lComp){
        JMenu menuDevelop = new JMenu("Development");
        menuDevelop.add(NcSwMenuItems.getLogFileReader(lComp));
        menuDevelop.add(NcSwMenuItems.getEnvironmentViewer(lComp));
        menuDevelop.add(NcSwMenuItems.getPropertiesViewer(lComp));
        return menuDevelop;
    }
    public static JMenu getMenuHelp(){
        JMenu menuHelp = new JMenu("Help");
        menuHelp.add(NcSwMenuItems.getAbout());
        return menuHelp;
    }
    public static JMenu getMenuService(){
        JMenu menuService = new JMenu("Service");
        menuService.add(NcSwMenuItems.getEtcEditor());
        return menuService;
    }
    private static void toLALRMainMenu(){
        if( NcfvRunVariables.isLALRNcSwMainMenuMainMenu() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogLogicVar.LA_JMENUBAR.getStr()
                + NcStrLogMsgField.APP_LOGIC_NEXT_WAY_VAR.getStr()
                + NcStrLogLogicVar.LA_JMENU.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }
}
