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
import java.util.UUID;
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
        BlockingQueue<TreeMap<UUID, NcDataListAttr>> pipeDirList = new ArrayBlockingQueue(1000, true);
        
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
        UUID randomUUID = UUID.randomUUID();
        
        TreeMap<UUID, NcDataListAttr> makeForRecord = 
                new TreeMap<UUID, NcDataListAttr>();
                
        TreeMap<UUID, NcDataListAttr> listForRecord = 
                new TreeMap<UUID, NcDataListAttr>();
        
        ArrayList<TreeMap<UUID, NcDataListAttr>> listOfListForRecord =
                new ArrayList<TreeMap<UUID, NcDataListAttr>>();
        ArrayList<Integer> listOfSizeIterarion = new ArrayList<Integer>();
        NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
        arrStr.clear();
        SwingWorker<Void, TreeMap<UUID, NcDataListAttr>> underGroundWorker = 
                new SwingWorker<Void, TreeMap<UUID, NcDataListAttr>> () {
                    
            @Override
            protected Void doInBackground() {
                try {
                    Files.walkFileTree(pathToStart, fileVisitor);
                } catch (IOException ex) {
                    NcAppHelper.logException(NcThWorkerGUIDirListScan.class.getCanonicalName(), ex);
                }
                int emptyCount = 0;
                int size = 0;
                boolean hasData = Boolean.FALSE;
                try {
                    do {
                        boolean notExitFromReadData = Boolean.TRUE;
                        do { 
                            size = fileVisitor.buffDirList.size();
                            if( (size > 0) ){
                                hasData = Boolean.TRUE;
                                emptyCount = 0;
                                publish(fileVisitor.buffDirList.take());
                            }
                            if( hasData ){
                               if( size == 0 ){
                                    notExitFromReadData = Boolean.FALSE;
                                } 
                            }
                            listOfSizeIterarion.add(size);
                        } while ( notExitFromReadData );
                        emptyCount++;
                    } while ( emptyCount < 5 );
                } catch (InterruptedException ex) {
                    NcAppHelper.logException(NcThWorkerGUIDirListScan.class.getCanonicalName(), ex);
                }
                return null;
            }
            
            @Override
            protected void process(List<TreeMap<UUID, NcDataListAttr>> chunks){
                ArrayList<String> arrOutStr = null;
                arrOutStr = new ArrayList<String>();
                int numPart = 0;
                for(TreeMap<UUID, NcDataListAttr> item : chunks){
                    arrOutStr.add("Part" + numPart + " of " + chunks.size());
                    for (Map.Entry<UUID, NcDataListAttr> entry : item.entrySet()) {
                        UUID key = entry.getKey();
                        NcDataListAttr value = entry.getValue();
                        arrOutStr.add("[key]" + key
                            + "[value]" + value.getShortDataToString());
                    }
                    makeForRecord.putAll(item);
                    //for publish and save to index code here
                    NcThWorkerUpGUITreeOutput.outputTreeAddChildren(lComp, arrOutStr);
                    arrOutStr.clear();
                    numPart++;
                }
                if( makeForRecord.size() > 100 ){
                    int countKey = 0;
                    for (Map.Entry<UUID, NcDataListAttr> itemForRecord : makeForRecord.entrySet()) {
                        if( countKey < 100){
                            UUID key = itemForRecord.getKey();
                            listForRecord.put(key, makeForRecord.get(key));
                        }
                        countKey++;
                    }
                    listOfListForRecord.add(listForRecord);
                    arrStr.add("[process]makeForRecord: " + makeForRecord.size());
                }
                arrStr.add("[process]listForRecord count: " + listForRecord.size());
                arrStr.add("[process]listOfListForRecord count: " + listOfListForRecord.size());
                arrStr.add("[process]makeForRecord: " + makeForRecord.size());
                NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
                arrOutStr.clear();
                arrStr.clear();
            }
            
            @Override
            protected void done(){
                arrStr.add("[done]listForRecord count: " + listForRecord.size());
                arrStr.add("[done]listOfListForRecord count: " + listOfListForRecord.size());
                arrStr.add("[done]makeForRecord: " + makeForRecord.size());

                arrStr.add("[done]");
                arrStr.add("[done]listOfListForRecord: " + listOfListForRecord.size());
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
