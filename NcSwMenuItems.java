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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMainMenu#getMenuDevelop(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * For Development
     * @return 
     */
    protected static JMenuItem getLogFileReader(NcSwGUIComponentStatus lComp){
        
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
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMainMenu#getMenuDevelop(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * For Development
     * @return 
     */
    protected static JMenuItem getEnvironmentViewer(NcSwGUIComponentStatus lComp){
        JMenuItem toRetMi = new JMenuItem(NcStrGUILabel.ENV_VIEW.getStr());
        toRetMi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String componentPath = NcSwGUIComponentRouter.pathMainFrame();
                JFrame mainFrame =
                    (JFrame) lComp.getComponentByPath(componentPath);
                NcSwModalDevHelper.showModalEnvironment(mainFrame);
            }
        });
        return toRetMi;
    }
    
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMainMenu#getMenuDevelop(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * For Development
     * @return 
     */
    protected static JMenuItem getPropertiesViewer(NcSwGUIComponentStatus lComp){
        JMenuItem toRetMi = new JMenuItem(NcStrGUILabel.PROPERTIES_VIEW.getStr());
        toRetMi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String componentPath = NcSwGUIComponentRouter.pathMainFrame();
                JFrame mainFrame =
                    (JFrame) lComp.getComponentByPath(componentPath);
                NcSwModalDevHelper.showModalProperties(mainFrame);
            }
        });
        return toRetMi;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMainMenu#getMenuService() }
     * </ul>
     * For Settings
     * @return 
     */
    protected static JMenuItem getEtcEditor(){
        return new JMenuItem(NcStrGUILabel.SETTINGS.getStr());
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMainMenu#getMenuFile() }
     * </ul>
     * For File
     * @return 
     */
    protected static JMenuItem getSubDirChecker(){
        return new JMenuItem(NcStrGUILabel.CHECK_SUBDIR.getStr());
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMainMenu#getMenuFile() }
     * </ul>
     * For File
     * @return 
     */
    protected static JMenuItem getAppExit(){
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
     * Not used
     * For Settings
     * @return 
     */
    private static JMenuItem getDirInEditor(){
        return new JMenuItem(NcStrGUILabel.DIR_IN_INDEX.getStr());
    }
    /**
     * Not used
     * For Settings
     * @return 
     */
    private static JMenuItem getDirOutEditor(){
        return new JMenuItem(NcStrGUILabel.DIR_OUT_INDEX.getStr());
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwMainMenu#getMenuHelp() }
     * </ul>
     * For Help
     * @return 
     */
    protected static JMenuItem getAbout(){
        return new JMenuItem(NcStrGUILabel.ABOUT.getStr());
    }
    protected static JMenuItem getStorageWordViewer(NcSwGUIComponentStatus lComp){
        JMenuItem toRetMi = new JMenuItem(NcStrGUILabel.STORAGEWORD.getStr());
        toRetMi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String componentPath = NcSwGUIComponentRouter.pathMainFrame();
                JFrame mainFrame =
                    (JFrame) lComp.getComponentByPath(componentPath);
                NcSwModalStorageWord.showModalStorageWord(mainFrame);
            }
        });
        return toRetMi;
    }
}
