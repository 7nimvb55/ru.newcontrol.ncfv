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
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    public static JDialog getDialogLogViewer(NcSwGUIComponentStatus lComp){
        String componentPath = NcStrGUIComponent.SMAIN.getStr()
            + NcStrGUIComponent.SJFRAME.getStr();
        JFrame mainFrame = (JFrame) lComp.getComponentByPath(componentPath);
        
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
        File logFile = NcLogFileManager.getLogFile();
        String strLogFile = logFile.getName();
        try {
            strLogFile = logFile.getCanonicalPath();
        } catch (IOException ex) {
            
            String strMsgText = NcStrLogMsgField.CLASSNAME.getStr()
                + NcSwModalLogViewer.class.getName()
                + NcStrLogMsgField.MSG.getStr()
                + ex.getMessage();
            NcAppHelper.outMessage(strMsgText);
        }
        strLogReaded.putAll(NcLogFileManager.readFromLog());
        DefaultMutableTreeNode strReadedTime = null;
        DefaultMutableTreeNode strReadedParent = getNN("Lines count ("
                + strLogReaded.size() + ") "
                + strLogFile + " (" + logFile.length() + ")");
        DefaultMutableTreeNode strReadedChild = null;
        
        forTreeTop.add(strReadedParent);
        
        boolean isAdd = false;
        for( Map.Entry<Long, String> strItem : strLogReaded.entrySet() ){
            strReadedChild = getNN(strItem.getValue());
            strReadedParent.add(strReadedChild);
            if( strItem.getValue().contains(NcStrLogMsgField.TIME.getStr()) ){
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
                JScrollPane scrollPane = (JScrollPane) compLocalIndex.getComponentByPath("JScrollPane-treeView");
                //scrollPane.setVisible(false);
                //scrollPane = null;
                scrollPane = getScrolledTree(compLocalIndex);
                JPanel centralPanel = (JPanel) compLocalIndex.getComponentByPath("JPanel-PanelCenter");
                //centralPanel.add(scrollPane);
                //scrollPane.setVisible(true);
                scrollPane.revalidate();
                scrollPane.repaint();
                centralPanel.repaint();
            }
        });
        return buttonSearch;
    }
    
}
