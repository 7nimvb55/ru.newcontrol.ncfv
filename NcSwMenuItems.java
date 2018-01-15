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
    public static JMenuItem getLogFileReader(){
        return new JMenuItem("Log View");
    }
    /**
     * For Development
     * @return 
     */
    public static JMenuItem getEnvironmentViewer(){
        JMenuItem toRetMi = new JMenuItem("Env View");
        toRetMi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                NcSwModalDevHelper.showModalEnvironment();
            }
        });
        return toRetMi;
    }
    /**
     * For Settings
     * @return 
     */
    public static JMenuItem getEtcEditor(){
        return new JMenuItem("Settings");
    }
    /**
     * For File
     * @return 
     */
    public static JMenuItem getSubDirChecker(){
        return new JMenuItem("Check SubDir");
    }
    /**
     * For Settings
     * @return 
     */
    public static JMenuItem getDirInEditor(){
        return new JMenuItem("Dir in search list");
    }
    /**
     * For Settings
     * @return 
     */
    public static JMenuItem getDirOutEditor(){
        return new JMenuItem("Dir out search list");
    }
    /**
     * For Help
     * @return 
     */
    public static JMenuItem getAbout(){
        return new JMenuItem("About");
    }
}
