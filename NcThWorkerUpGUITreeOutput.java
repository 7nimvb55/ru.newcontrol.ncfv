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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThWorkerUpGUITreeOutput {
    protected static void outputTreeAddChildren(NcSwGUIComponentStatus lComp,
            ArrayList<String> arrStr){
        String strPathTree = 
                NcSwGUIComponentRouter.pathMainFramePanelLineEndTabbedPaneOutputScrollPaneTreeShowOutput();
        JTree treeShowStack = (JTree) lComp.getComponentByPath(strPathTree);
        
        
        treeShowStack.setEnabled(false);
        TreeModel modelShowStack = treeShowStack.getModel();
        
        DefaultMutableTreeNode rootElement = 
                (DefaultMutableTreeNode) modelShowStack.getRoot();
        
        String strNowTime =  NcStrLogMsgField.TIME.getStr()
                + java.time.LocalDateTime.now().toString();
        DefaultMutableTreeNode timeChild =
                new DefaultMutableTreeNode(strNowTime);
        
        SwingWorker<Void, String> underGroundWorker = 
                new SwingWorker<Void, String> () {
                    
            @Override
            protected Void doInBackground() {
                
                //ArrayList<String> arrStr = NcAppStackTrace.getAllStack();
                
                for (String string : arrStr) {
                    publish(string);
                }
                return null;
            }
            
            @Override
            protected void process(List<String> chunks){
                for (String chunk : chunks) {
                    timeChild.add(new DefaultMutableTreeNode(chunk));
                }
                
            }
            
            @Override
            protected void done(){
                rootElement.add(timeChild);
                DefaultTreeModel treeForAdd = new DefaultTreeModel(rootElement);
                treeShowStack.setModel(treeForAdd);
                treeShowStack.setEnabled(true);
                
                String componentPath = NcSwGUIComponentRouter.pathMainFramePanelLineEndTabbedPaneOutputScrollPane();
                JScrollPane scrollTreeStackPane = (JScrollPane) lComp.getComponentByPath(componentPath);
                Dimension preferredSize = scrollTreeStackPane.getPreferredSize();
                Dimension widePreffSize = new Dimension(250,
                (int) preferredSize.getHeight());
                scrollTreeStackPane.setPreferredSize(widePreffSize);
                scrollTreeStackPane.revalidate();
                
                componentPath = NcSwGUIComponentRouter.pathMainFramePanelLineEnd();
                JPanel panelLineEnd = (JPanel) lComp.getComponentByPath(componentPath);
                panelLineEnd.repaint();
            }
                    
        };
        underGroundWorker.execute();
    }
}
