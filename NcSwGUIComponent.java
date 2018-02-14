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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwGUIComponent {
    private static final long ncForGB = 1024*1024*1024;
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @return
     */
    protected static JLabel createEmptyLabel() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(100, 30));
        return label;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @param textSize
     * @param strToolTip
     * @return
     */
    protected static JPanel getTextFieldForSearchPanel(Dimension textSize, String strToolTip){
        JPanel textFieldForSearchPanel = new JPanel();
        textFieldForSearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        JTextField textWordForSearch = new JTextField();
        textWordForSearch.setPreferredSize(textSize);
        textWordForSearch.setToolTipText(strToolTip);
        
        textFieldForSearchPanel.add(textWordForSearch);
        return textFieldForSearchPanel;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineStart#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @return
     */
    protected static ArrayList<JButton> getRootButtons(){
        ArrayList<JButton> toReturn = new ArrayList<JButton>();
        
        FileSystemView fileSystemView;
        fileSystemView = FileSystemView.getFileSystemView();
        //File[] ncDisks = fileSystemView.getRoots();
        File[] ncDisks = File.listRoots();
       
        
        for(File itemDisk : ncDisks){
            
            Icon itemIcon = fileSystemView.getSystemIcon(itemDisk);
            String strSysTypeDescription = fileSystemView.getSystemTypeDescription(itemDisk);
            if( "".equals(strSysTypeDescription) || (strSysTypeDescription == null) ){
                if( !NcAppHelper.isWindows() ){
                    strSysTypeDescription = "Root";
                }
            }
            String itemDisplayName = NcIdxFileManager.getStrCanPathFromFile(itemDisk) + " - " +
                    strSysTypeDescription;
            
            if( NcIdxFileManager.getStrCanPathFromFile(itemDisk).length() > 2 ){
                itemDisplayName = NcIdxFileManager.getStrCanPathFromFile(itemDisk).substring(0, 2) + " - " +
                    strSysTypeDescription;
            }
            long ncDriveFreeGb = itemDisk.getFreeSpace()/ncForGB;
            long ncDriveTotalGb = itemDisk.getTotalSpace()/ncForGB;
            
            String itemToolTipText = 
                    "Free: " + ncDriveFreeGb +
                    " Gb Total: " + ncDriveTotalGb + " Gb";
            
            if(ncDriveTotalGb != 0){
                double ncDriveFreePercent = (((double) itemDisk.getFreeSpace() / itemDisk.getTotalSpace()) * 100);
                String strPercent= Double.toString(ncDriveFreePercent).substring(0, 5);
                itemToolTipText = "Free: " + strPercent +
                    " % " + itemToolTipText;
            }
            
            String strDescr = fileSystemView.getSystemDisplayName(itemDisk);
            if(strDescr.length() > 0 ){
                itemToolTipText = strDescr + " " + itemToolTipText;
            }
            
            JButton ncButton = createButton(itemDisplayName, itemIcon, itemToolTipText);
         
           
            toReturn.add(ncButton);
        }

        
        return toReturn;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwGUIComponent#getRootButtons() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getAndOrButtonPanel() }
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getAddDelButtonPanel() }
     * <li>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelPageStart#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @param ncName
     * @param ncIcon
     * @param ncToolTipText
     * @return
     */
    protected static JButton createButton(String ncName, Icon ncIcon, String ncToolTipText) {
        JButton ncButton = new JButton(ncName);
        if(ncIcon != null){
            ncButton.setIcon(ncIcon);
        }
        ncButton.setToolTipText(ncToolTipText);
        return ncButton;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @return
     */
    protected static JList createJListWordInSearch(){
        DefaultListModel listWordInSearch = new DefaultListModel();
        ArrayList<String> arrKeyIn = 
                NcEtcKeyWordListManager.getKeyWordInSearchFromFile();
        if( arrKeyIn.size() == 0 ){
            listWordInSearch.addElement("* None *");
        }
        else{
            for(String itemKey : arrKeyIn){
                listWordInSearch.addElement(itemKey);
            }
        }
        
        JList ncList = new JList(listWordInSearch);
        ncList.setVisibleRowCount(7);
        ncList.setFocusable(false);
        return ncList;
    }

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @return
     */
    protected static JList createJListWordNotInSearch(){
        DefaultListModel listWordNotInSearch = new DefaultListModel();
        
        ArrayList<String> arrKeyOut = 
                NcEtcKeyWordListManager.getKeyWordOutSearchFromFile();
        if( arrKeyOut.size() == 0 ){
            listWordNotInSearch.addElement("* None *");
        }
        else{
            for(String itemKey : arrKeyOut){
                listWordNotInSearch.addElement(itemKey);
            }
        }
        JList ncList = new JList(listWordNotInSearch);
        ncList.setVisibleRowCount(7);
        ncList.setFocusable(false);
        return ncList;
    }

    /**
     * Not used
     * @param ncJList
     * @return
     */
    private static JList addJListElement(JList ncJList){
        return ncJList;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwStatusPanel#addProgressBar(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @return 
     */
    protected static JProgressBar getProgressBar(){
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        return progressBar;
    }
}
