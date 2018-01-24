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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwMenuItems {
    /**
     * For Development
     * @return 
     */
    public static JMenuItem getLogFileReader(NcSwGUIComponentStatus lComp){
        
        JMenuItem toRetMi = new JMenuItem(NcStrGUILabel.LOG_VIEW.getStr());
        toRetMi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                JDialog modalLogViewer =
                    NcSwModalLogViewer.getDialogLogViewer(lComp);
                modalLogViewer.pack();
                modalLogViewer.setVisible(true);
            }
        });
        return toRetMi;
    }
    /**
     * For Development
     * @return 
     */
    public static JMenuItem getEnvironmentViewer(NcSwGUIComponentStatus lComp){
        JMenuItem toRetMi = new JMenuItem(NcStrGUILabel.ENV_VIEW.getStr());
        toRetMi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String componentPath = NcStrGUIComponent.SMAIN.getStr()
                    + NcStrGUIComponent.SJFRAME.getStr();
                JFrame mainFrame =
                    (JFrame) lComp.getComponentByPath(componentPath);
                NcSwModalDevHelper.showModalEnvironment(mainFrame);
            }
        });
        return toRetMi;
    }
    
    /**
     * For Development
     * @return 
     */
    public static JMenuItem getPropertiesViewer(NcSwGUIComponentStatus lComp){
        JMenuItem toRetMi = new JMenuItem(NcStrGUILabel.PROPERTIES_VIEW.getStr());
        toRetMi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String componentPath = NcStrGUIComponent.SMAIN.getStr()
                    + NcStrGUIComponent.SJFRAME.getStr();
                JFrame mainFrame =
                    (JFrame) lComp.getComponentByPath(componentPath);
                NcSwModalDevHelper.showModalProperties(mainFrame);
            }
        });
        return toRetMi;
    }
    /**
     * For Settings
     * @return 
     */
    public static JMenuItem getEtcEditor(){
        return new JMenuItem(NcStrGUILabel.SETTINGS.getStr());
    }
    /**
     * For File
     * @return 
     */
    public static JMenuItem getSubDirChecker(){
        return new JMenuItem(NcStrGUILabel.CHECK_SUBDIR.getStr());
    }
    /**
     * For File
     * @return 
     */
    public static JMenuItem getAppExit(){
        JMenuItem toRetMi = new JMenuItem(NcStrGUILabel.APP_EXIT.getStr());
        toRetMi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });
        return toRetMi;
    }
    /**
     * For Settings
     * @return 
     */
    public static JMenuItem getDirInEditor(){
        return new JMenuItem(NcStrGUILabel.DIR_IN_INDEX.getStr());
    }
    /**
     * For Settings
     * @return 
     */
    public static JMenuItem getDirOutEditor(){
        return new JMenuItem(NcStrGUILabel.DIR_OUT_INDEX.getStr());
    }
    /**
     * For Help
     * @return 
     */
    public static JMenuItem getAbout(){
        return new JMenuItem(NcStrGUILabel.ABOUT.getStr());
    }
}
