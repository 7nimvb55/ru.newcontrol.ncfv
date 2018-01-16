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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwModalLogViewer {
    private static String modalTitle = "View log file";
    
    public static JDialog getDialogLogViewer(JFrame mainFrame){
        NcSwGUIComponentStatus compIndex = new NcSwGUIComponentStatus();
        compIndex.putComponents("JFrame-mainFrame", mainFrame);
        JDialog modalWindow = new JDialog(mainFrame, modalTitle, true);
        compIndex.putComponents("JDialog-modalWindow", modalWindow);
        JPanel modalPanelPageStart = getPanelPageStart(compIndex);
        JPanel modalPanelCenter = getPanelCenter(compIndex);
        JPanel modalPanelPageEnd = getPanelPageEnd(modalWindow);
        
        modalWindow.add(modalPanelPageStart, BorderLayout.PAGE_START);
        modalWindow.add(modalPanelCenter, BorderLayout.CENTER);
        modalWindow.add(modalPanelPageEnd, BorderLayout.PAGE_END);
        Dimension pSize = new Dimension(800, 600);
        modalWindow.setSize(pSize);
        modalWindow.setPreferredSize(pSize);
        modalWindow.setLocationRelativeTo(mainFrame);
        modalWindow.repaint();
        return modalWindow;
    }
    private static JPanel getPanelCenter(NcSwGUIComponentStatus compLocalIndex){
        JPanel modalPanelInFunc = new JPanel();
        compLocalIndex.putComponents("JPanel-PanelCenter", modalPanelInFunc);
        JScrollPane treeScroll = getScrolledTree(compLocalIndex);
        modalPanelInFunc.add(treeScroll);
        return modalPanelInFunc;
    }
    private static JScrollPane getScrolledTree(NcSwGUIComponentStatus compLocalIndex){
        DefaultMutableTreeNode treeTop = 
                new DefaultMutableTreeNode("Log file contained:");
        JTree treeNodes = getTreeNodes(treeTop);
        compLocalIndex.putComponents("JTree-treeNodes", treeNodes);
        JScrollPane treeView = new JScrollPane(treeNodes);
        compLocalIndex.putComponents("JScrollPane-treeView", treeView);
        return treeView;
    }
    private static JTree getTreeNodes(DefaultMutableTreeNode forTreeTop){
        TreeMap<Long, String> strLogReaded = new TreeMap<Long, String>();
        strLogReaded.putAll(NcLogFileManager.readFromLog());
        DefaultMutableTreeNode strReadedTime = null;
        DefaultMutableTreeNode strReadedParent = getNN("Lines count (" + strLogReaded.size() + ") " + NcLogFileManager.getLogFile().getAbsolutePath());
        DefaultMutableTreeNode strReadedChild = null;
        
        forTreeTop.add(strReadedParent);
        
        boolean isAdd = false;
        for( Map.Entry<Long, String> strItem : strLogReaded.entrySet() ){
            strReadedChild = getNN(strItem.getValue());
            strReadedParent.add(strReadedChild);
            if( strItem.getValue().indexOf(": [time]") > -1 ){
                strReadedParent = getNN(strItem.getValue());
                forTreeTop.add(strReadedParent);
            }
           
        }
        forTreeTop.add(strReadedParent);
        return new JTree(forTreeTop);
    }
    private static DefaultMutableTreeNode getNN(String strNodeName){
        return new DefaultMutableTreeNode(strNodeName);
    }
    private static JPanel getPanelPageStart(NcSwGUIComponentStatus compLocalIndex){
        JPanel modalPanelInFunc = new JPanel();
        JTextField textSearch = new JTextField();
        textSearch.setColumns(25);
        modalPanelInFunc.add(textSearch);
        JButton buttonSearch = getButtonSearch();
        modalPanelInFunc.add(buttonSearch);
        JButton buttonUpdate = getButtonUpdate(compLocalIndex);
        modalPanelInFunc.add(buttonUpdate);
        return modalPanelInFunc;
    }
    private static JPanel getPanelPageEnd(JDialog modalWindowInFunc){
        JPanel modalPanelInFunc = new JPanel();
        JButton buttonClose = getButtonClose(modalWindowInFunc);
        modalPanelInFunc.add(buttonClose);
        return modalPanelInFunc;
    }
    private static JButton getButtonClose(JDialog modalWindowForButton){
        JButton buttonClose = new JButton("Close");
        buttonClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                modalWindowForButton.setVisible(false);
            }
        });
        return buttonClose;
    }
    private static JButton getButtonSearch(){
        JButton buttonSearch = new JButton("Search");
        buttonSearch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                
            }
        });
        return buttonSearch;
    }
    private static JButton getButtonUpdate(NcSwGUIComponentStatus compLocalIndex){
        JButton buttonSearch = new JButton("Update");
        buttonSearch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                JScrollPane scrollPane = (JScrollPane) compLocalIndex.getComponentsByType("JScrollPane-treeView");
                scrollPane.setVisible(false);
                scrollPane = null;
                scrollPane = getScrolledTree(compLocalIndex);
                scrollPane.setVisible(true);
                scrollPane.repaint();
                JPanel centralPanel = (JPanel) compLocalIndex.getComponentsByType("JPanel-PanelCenter");
                centralPanel.repaint();
            }
        });
        return buttonSearch;
    }
    
}
