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

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;



/**
 *
 * @author Администратор
 */
public class NcSwingIndexManagerApp {
    private static final String APP_TITLE = "Index Managment";
    private static NcSwGUIComponentStatus centerPanelInClass;

    /**
     *
     */
    public NcSwingIndexManagerApp() {
        

    }
    
    /**
     *
     */
    public static void NcRunSIMA(){
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Significantly improves the look of the output in
                    // terms of the file names returned by FileSystemView!
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch(Exception weTried) {
                    weTried.getMessage();
                    weTried.getStackTrace();
                }
                createGui();
                
            }
        });
    }

    /**
     *
     */
    public static void createGui(){
        JFrame frame = new JFrame(APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        frame.setJMenuBar(NcSwMainMenu.getMainMenu());
        mainPanel.add(NcSwPanelPageStart.getNorthPanel(), BorderLayout.NORTH);
        mainPanel.add(NcSwPanelPageEnd.getSouthPanel(), BorderLayout.SOUTH);
        mainPanel.add(NcSwPanelLineStart.getWestPanel(), BorderLayout.WEST);
        mainPanel.add(NcSwPanelLineEnd.getEastPanel(), BorderLayout.EAST);

        centerPanelInClass = NcSwPanelCenter.getCenterPanel();
        mainPanel.add(centerPanelInClass.centerPanel, BorderLayout.CENTER);
        
        frame.getContentPane().add(mainPanel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public static NcSwGUIComponentStatus getComponentStatus(){
        return centerPanelInClass;
    }
}
