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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThWorkerGUIDirListScan {
    protected static void scanDirToIdxDirList(NcSwGUIComponentStatus lComp, Path pathDirToScan) throws Exception{
        if( !NcFsIdxOperationDirs.existAndHasAccessR(pathDirToScan) ){
            throw new Exception("Directory not have access for read" + pathDirToScan.toString());
        }
        Path pathDevDirToScan = Paths.get("/usr/home/wladimirowichbiaran/work");
        String componentPath = NcSwGUIComponentRouter.pathMainFramePanelPageStartButtonSearch();
        JButton buttonSearch = (JButton) lComp.getComponentByPath(componentPath);
        buttonSearch.setEnabled(false);
        
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelPageEndProgressBar();
        JProgressBar progressBar = (JProgressBar) lComp.getComponentByPath(componentPath);
        progressBar.setIndeterminate(true);
        
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelCenter();
        JPanel panelLineEnd = (JPanel) lComp.getComponentByPath(componentPath);
        panelLineEnd.repaint();
        
        ArrayList<String> arrStr = new ArrayList<String>();
        BlockingQueue<TreeMap<Long, NcDataListAttr>> pipeDirList = new ArrayBlockingQueue(1000, true);
        
        final NcFsIdxFileVisitor fileVisitor = new NcFsIdxFileVisitor(lComp, pipeDirList);
        //Path prePathToStart = pathDirToScan.toAbsolutePath();
        Path prePathToStart = pathDevDirToScan;
        
        prePathToStart = prePathToStart.normalize();
        try {
            prePathToStart = prePathToStart.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        arrStr.add("pathToStart:" + prePathToStart.toString());
        final Path pathToStart = prePathToStart;
        arrStr.add("[count Dir]"
        + fileVisitor.getCountPostVisitDir()
        + "[count File]"
        + fileVisitor.getCountVisitFile());
        
        TreeMap<Long, NcDcIdxDirListToFileAttr> makeSearchResult = 
                new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
        arrStr.clear();
        SwingWorker<Void, TreeMap<Long, NcDataListAttr>> underGroundWorker = 
                new SwingWorker<Void, TreeMap<Long, NcDataListAttr>> () {
                    
            @Override
            protected Void doInBackground() {
                try {
                    Files.walkFileTree(pathToStart, fileVisitor);
                } catch (IOException ex) {
                    NcAppHelper.logException(NcThWorkerGUIDirListScan.class.getCanonicalName(), ex);
                }
                int emptyCount = 0;
                try {
                    //NcFsIdxStorage.putDataToIndex(lComp, pathDevDirToScan);
                    do {
                        do {                        
                            TreeMap<Long, NcDataListAttr> makeListResult = 
                                new TreeMap<Long, NcDataListAttr>();
                            makeListResult.putAll(fileVisitor.buffDirList.take());
                            if( makeListResult.size() > 0 ){
                                emptyCount = 0;
                            }
                            publish(makeListResult);
                        } while ( !fileVisitor.buffDirList.isEmpty());
                        emptyCount++;
                        Thread.sleep(1000);
                    } while ( emptyCount != 5 );
                } catch (InterruptedException ex) {
                    NcAppHelper.logException(NcThWorkerGUIDirListScan.class.getCanonicalName(), ex);
                }
                return null;
            }
            
            @Override
            protected void process(List<TreeMap<Long, NcDataListAttr>> chunks){
                ArrayList<String> arrOutStr = null;
                arrOutStr = new ArrayList<String>();
                for(TreeMap<Long, NcDataListAttr> item : chunks){
                    for (Map.Entry<Long, NcDataListAttr> entry : item.entrySet()) {
                        Long key = entry.getKey();
                        NcDataListAttr value = entry.getValue();
                        arrOutStr.add(value.getShortDataToString());
                    }
                    //for publish and save to index code here
                    NcThWorkerUpGUITreeOutput.outputTreeAddChildren(lComp, arrOutStr);
                }
                arrStr.add("New chunk count: " + chunks.size());
                NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
            }
            
            @Override
            protected void done(){
                NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
                progressBar.setIndeterminate(false);
                buttonSearch.setEnabled(true);
                String componentPath = NcSwGUIComponentRouter.pathMainFramePanelCenter();
                JPanel panelCenter = (JPanel) lComp.getComponentByPath(componentPath);
                panelCenter.repaint();
            }
                    
        };
        underGroundWorker.execute();
    }
}
