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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.TableModel;



/**
 *
 * @author Администратор
 */
public class NcSwingIndexManagerApp {
    private static final String APP_TITLE = "Index Managment";
    private static NcSwGUIComponentStatus centerPanelInClass;
    private static TableModel ncTableModel;
    private static JScrollPane ncScrollTable;
    private static JTable ncTable;
    private static JPanel centerPanel;
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
