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
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.Border;



/**
 *
 * @author wladimirowichbiaran
 */
public class NcSwPanelLineEnd {
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwingIndexManagerApp#createGui() }
     * </ul>
     * @return
     */
    protected static JPanel getPanel(NcSwGUIComponentStatus lComp){
        JPanel lineEndPanel = getPanelTreeShowStack(lComp);
        return lineEndPanel;
    }
    
    private static JPanel getPanelTreeShowStack(NcSwGUIComponentStatus lComp){
        JTree treeStack = NcSwGUITreeShowStack.getTreeShowStack(lComp);
        
        JScrollPane scrollTreeStackPane = new JScrollPane(treeStack);
        JPanel panelTreeStackPane = new JPanel();
        panelTreeStackPane.add(scrollTreeStackPane);
        String componentPath = NcSwGUIComponentRouter.pathMainFramePanelLineEndTabbedPaneStackScrollPane();
        //***lComp.putComponents(componentPath, scrollTreeStackPane);
        lComp.putComponents(componentPath, scrollTreeStackPane);
        
        
        JPanel panel = new JPanel();
        
        Border eastBorder = BorderFactory.createTitledBorder("EAST panel");
        
        Dimension dimMin = new Dimension(50, 70);
        panel.setMinimumSize(dimMin);
        Dimension dimMax = new Dimension(250, 700);
        panel.setMaximumSize(dimMax);
        Dimension dimPreff = new Dimension(250, 407);
        panel.setPreferredSize(dimPreff);
        panel.setBorder(eastBorder);
        
        JPanel panelForTabbed = new JPanel();
        
        JTabbedPane tabbedPane = new JTabbedPane();
        //***tabbedPane.addTab("Stack", scrollTreeStackPane);
        tabbedPane.addTab("Stack", panelTreeStackPane);
        
        JTree treeWork = NcSwGUITreeShowWork.showWork(lComp);
        JScrollPane scrollTreeWorkPane = new JScrollPane(treeWork);
        JPanel panelTreeWorkPane = new JPanel();
        panelTreeWorkPane.add(scrollTreeWorkPane);
        tabbedPane.addTab("Work", panelTreeWorkPane);
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelLineEndTabbedPaneWorkScrollPane();
        lComp.putComponents(componentPath, scrollTreeWorkPane);
        
        JTree treeOutput = NcSwGUITreeShowOutput.showOutput(lComp);
        JScrollPane scrollTreeOutputPane = new JScrollPane(treeOutput);
        JPanel panelTreeOutputPane = new JPanel();
        panelTreeOutputPane.add(scrollTreeOutputPane);
        tabbedPane.addTab("Output", panelTreeOutputPane);
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelLineEndTabbedPaneOutputScrollPane();
        lComp.putComponents(componentPath, scrollTreeOutputPane);
        
        
        panelForTabbed.add(tabbedPane);
        panel.add(panelForTabbed);
        
        
        Dimension preferredSize = scrollTreeStackPane.getPreferredSize();
        Dimension widePreffSize = new Dimension(230,
                ((int) preferredSize.getHeight()) - 30);
        scrollTreeStackPane.setPreferredSize(widePreffSize);
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelLineEnd();
        lComp.putComponents(componentPath, panel);
        
        
        
        return panel;
    }
    
    private static JPanel getSearchKeyWordManagePanel(NcSwGUIComponentStatus lComp){
        String[] forTextToolTip = {
            "For search with keyword, input it and press \"+\" Button",
            "For search with out keyword, input it and press \"+\" Button",
            "Help about keyword in Search",
            "Help about keyword out of Search",
        };
        String[] forComponentText = {
            "How to use",
        };
    
        Dimension textFiledForSearchDimension = new Dimension(100, 20);        
        
        JPanel eastPanel = new JPanel();
        String componentPath = NcSwGUIComponentRouter.pathMainFramePanelLineEnd();
        lComp.putComponents(componentPath, eastPanel);
        Border eastBorder = BorderFactory.createTitledBorder("EAST panel");
        eastPanel.setBorder(eastBorder);
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));
        
        eastPanel.setAlignmentX(JComponent.TOP_ALIGNMENT);
        eastPanel.setAlignmentY(JComponent.CENTER_ALIGNMENT);
        
        JButton ncHowToInSearch = NcSwGUIComponent.createButton(
                forComponentText[0],null,forTextToolTip[2]);
        
        eastPanel.add(ncHowToInSearch);
        
        JPanel textInSearchPanel = NcSwGUIComponent.getTextFieldForSearchPanel(
                textFiledForSearchDimension, forTextToolTip[0]);
        eastPanel.add(textInSearchPanel);
        
        eastPanel.add(getAndOrButtonPanel());
        
        JList wiSearch = NcSwGUIComponent.createJListWordInSearch();
        JScrollPane wiScroll = new JScrollPane(wiSearch);
        wiScroll.setPreferredSize(new Dimension(100, 100));
        
        eastPanel.add(wiScroll);
        
        JPanel buttonPanel1 = getAddDelButtonPanel();
        eastPanel.add(buttonPanel1);
        
        JSeparator ncSeparator = new JSeparator(JSeparator.HORIZONTAL);
        eastPanel.add(ncSeparator);
        
        eastPanel.add(NcSwGUIComponent.createButton(
                forComponentText[0],null,forTextToolTip[3]));
        
        JPanel textOutSearchPanel = NcSwGUIComponent.getTextFieldForSearchPanel(
                textFiledForSearchDimension, forTextToolTip[1]);
        eastPanel.add(textOutSearchPanel);
        
        eastPanel.add(getAndOrButtonPanel());
        
        JList wniSearch = NcSwGUIComponent.createJListWordNotInSearch();
        JScrollPane wniScroll = new JScrollPane(wniSearch);
        wniScroll.setPreferredSize(new Dimension(100, 100));
        
        eastPanel.add(wniScroll);
        
        JPanel buttonPanel2 = getAddDelButtonPanel();
        eastPanel.add(buttonPanel2);
        toLALRgetPanel();
        return eastPanel;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @return
     */
    private static JPanel getAndOrButtonPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(NcSwGUIComponent.createButton("&&",null,""));
        buttonPanel.add(NcSwGUIComponent.createButton("||",null,""));
        return buttonPanel;
    }    

    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * @return
     */
    private static JPanel getAddDelButtonPanel(){
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(NcSwGUIComponent.createButton("+",null,""));
        buttonPanel.add(NcSwGUIComponent.createButton("-",null,""));
        return buttonPanel;
    }
    /**
     * Used in
     * <ul>
     * <li>{@link ru.newcontrol.ncfv.NcSwPanelLineEnd#getPanel(ru.newcontrol.ncfv.NcSwGUIComponentStatus) }
     * </ul>
     * LogAppLogicRecord (LALR) - toLALR(class MethodName)
     * make record in log file
     * @return
     */
    private static void toLALRgetPanel(){
        if( NcfvRunVariables.isLALRNcSwPanelLineEndgetPanel() ){
            String strLogMsg = NcStrLogMsgField.INFO.getStr()
                + NcStrLogMsgField.APP_LOGIC_NOW.getStr()
                + NcStrLogLogicVar.LA_JPANEL_LINEEND.getStr()
                + NcStrLogMsgField.APP_LOGIC_NEXT_WAY_VAR.getStr()
                + NcStrLogLogicVar.LA_JPANEL_CENTER.getStr();
            NcAppHelper.outMessage(strLogMsg);
        }
    }
}
