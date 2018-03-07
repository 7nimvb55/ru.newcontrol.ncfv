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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThWorkerUpGUIKeyWordSearch {
    protected static void searchKeyWordGetResult(NcSwGUIComponentStatus lComp){
        
        String strPathTable = 
                NcSwGUIComponentRouter.pathMainFramePanelCenterTable();
        JTable tableShowSearchResult = (JTable) lComp.getComponentByPath(strPathTable);
        
        tableShowSearchResult.setEnabled(false);
        TableModel modelShowStack = tableShowSearchResult.getModel();
        String componentPath = NcSwGUIComponentRouter.pathMainFramePanelPageStartTextFieldSearch();
        JTextField textField = (JTextField) lComp.getComponentByPath(componentPath);
        String strSrch = textField.getText();
        
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelPageStartButtonSearch();
        JButton buttonSearch = (JButton) lComp.getComponentByPath(componentPath);
        buttonSearch.setEnabled(false);
        
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelPageEndProgressBar();
        JProgressBar progressBar = (JProgressBar) lComp.getComponentByPath(componentPath);
        progressBar.setIndeterminate(true);
        
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelCenter();
        JPanel panelLineEnd = (JPanel) lComp.getComponentByPath(componentPath);
        panelLineEnd.repaint();
        
        TreeMap<Long, NcDcIdxDirListToFileAttr> makeSearchResult = 
                new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        
        
        SwingWorker<Void, TreeMap<Long, NcDcIdxDirListToFileAttr>> underGroundWorker = 
                new SwingWorker<Void, TreeMap<Long, NcDcIdxDirListToFileAttr>> () {
                    
            @Override
            protected Void doInBackground() {
                TreeMap<Long, NcDcIdxDirListToFileAttr> makeSearchByKeyFromInput = 
                        NcSrchGetResult.makeSearchByKeyFromInput(strSrch);
                publish(makeSearchByKeyFromInput);
                return null;
            }
            
            @Override
            protected void process(List<TreeMap<Long, NcDcIdxDirListToFileAttr>> chunks){
                for(TreeMap<Long, NcDcIdxDirListToFileAttr> item : chunks){
                    makeSearchResult.putAll(item);
                }
                
                
            }
            
            @Override
            protected void done(){
                progressBar.setIndeterminate(false);
                TableModel locNewTableModel = new NcSIMASearchResultTableModel(makeSearchResult);
                tableShowSearchResult.setModel(locNewTableModel);
                tableShowSearchResult.setEnabled(true);
                buttonSearch.setEnabled(true);
                String componentPath = NcSwGUIComponentRouter.pathMainFramePanelCenter();
                JPanel panelCenter = (JPanel) lComp.getComponentByPath(componentPath);
                panelCenter.repaint();
                
            }
                    
        };
        underGroundWorker.execute();
    }
}
