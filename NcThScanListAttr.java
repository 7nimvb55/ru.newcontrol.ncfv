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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcThScanListAttr {
    protected static void fsScanListAttr(JButton ncButton, NcSwGUIComponentStatus lComp, Path pathDirToScan) throws Exception{
        
        compChangeForStart(ncButton, lComp);
        Path pathDevDirToScan = Paths.get("/usr/home/wladimirowichbiaran/work");
        Path pathToStart = NcFsIdxOperationDirs.checkScanPath(pathDevDirToScan);
        
        CopyOnWriteArrayList<TreeMap<UUID, NcDataListAttr>> listOfListForRecord = 
                new CopyOnWriteArrayList<TreeMap<UUID, NcDataListAttr>>();
        ArrayList<String> arrStr = new ArrayList<String>();
        BlockingQueue<TreeMap<UUID, NcDataListAttr>> pipeDirList = new ArrayBlockingQueue(1000, true);
        
        NcFsIdxFileVisitor fileVisitor = new NcFsIdxFileVisitor(lComp, pipeDirList);
        
        arrStr.add("pathToStart:" + pathToStart.toString());
        
        arrStr.add("[count Dir]"
        + fileVisitor.getCountPostVisitDir()
        + "[count File]"
        + fileVisitor.getCountVisitFile());
        
        underGroundScan(pathToStart, fileVisitor, listOfListForRecord, 1, lComp);
        
        /*UUID randomUUID = UUID.randomUUID();
        
        TreeMap<UUID, NcDataListAttr> makeForRecord = 
                new TreeMap<UUID, NcDataListAttr>();
                
        TreeMap<UUID, NcDataListAttr> listForRecord = 
                new TreeMap<UUID, NcDataListAttr>();
        
        ArrayList<TreeMap<UUID, NcDataListAttr>> listOfListForRecord =
                new ArrayList<TreeMap<UUID, NcDataListAttr>>();
        ArrayList<Integer> listOfSizeIterarion = new ArrayList<Integer>();*/
        NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
        arrStr.clear();
        compChangeForDone(ncButton, lComp);
    }
    private static void underGroundScan(Path pathToStart,
            NcFsIdxFileVisitor fileVisitor, CopyOnWriteArrayList<TreeMap<UUID, NcDataListAttr>> listOfListForRecord, int countTh, NcSwGUIComponentStatus lComp){
        Runnable scanDir = new Runnable() {
            final Semaphore avalableThToScan = new Semaphore(countTh);
            public void run(){
                try {
                    avalableThToScan.acquire();
                    doBackgroundReadList(pathToStart,
                            fileVisitor, lComp, listOfListForRecord);
                    avalableThToScan.release();
                } catch (InterruptedException ex) {
                    NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
                }
            }
        };
        Thread backGroundScan = new Thread(scanDir);
        backGroundScan.checkAccess();
        backGroundScan.start();
    }
    private static void publishScanList(NcSwGUIComponentStatus lComp, List<TreeMap<UUID, NcDataListAttr>> pipeDirList, CopyOnWriteArrayList<TreeMap<UUID, NcDataListAttr>> listOfListForRecord, int countTh){
        Runnable scanResult = new Runnable() {
            private final TreeMap<UUID, NcDataListAttr> makeForRecord = 
                new TreeMap<UUID, NcDataListAttr>();
            private final TreeMap<UUID, NcDataListAttr> listForRecord = 
                new TreeMap<UUID, NcDataListAttr>();
        
            
            final Semaphore avalableThToScan = new Semaphore(countTh);
            public void run(){
                try {
                    avalableThToScan.acquire();
                    ArrayList<String> arrOutStr = null;
                    arrOutStr = new ArrayList<String>();

                    ArrayList<String> arrStr = null;
                    arrStr = new ArrayList<String>();
                    int numPart = 0;
                    for(TreeMap<UUID, NcDataListAttr> item : pipeDirList){
                        //for publish and save to index code here
                        if( item.size() > 0 ){
                            listOfListForRecord.addIfAbsent(item);
                            for (Map.Entry<UUID, NcDataListAttr> entry : item.entrySet()) {
                                UUID key = entry.getKey();
                                NcDataListAttr value = entry.getValue();
                                arrOutStr.add("[key]" + key
                                    + "[value]" + value.getShortDataToString());
                            }
                            if( arrOutStr.size() > 0 ){
                                NcThWorkerUpGUITreeOutput.outputTreeAddChildren(lComp, arrOutStr);
                                arrOutStr.clear();
                            }
                        }
                        numPart++;
                    }
                    avalableThToScan.release();
                } catch (InterruptedException ex) {
                    NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
                }
                ArrayList<String> listStrArr = new ArrayList<String>();
                listStrArr.add("[publishScanList][run][done]"
                + "[listOfListForRecord.size]" + listOfListForRecord.size());
                NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, listStrArr);
                listStrArr = null;
            }
        };
        Thread backGroundResult = new Thread(scanResult);
        backGroundResult.checkAccess();
        backGroundResult.start();
    }
    private static void doBackgroundReadList(Path pathToStart,
            NcFsIdxFileVisitor fileVisitor, NcSwGUIComponentStatus lComp, CopyOnWriteArrayList<TreeMap<UUID, NcDataListAttr>> listOfListForRecord){
        
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
                    CopyOnWriteArrayList<TreeMap<UUID, NcDataListAttr>> copyOnWriteArrayList = new CopyOnWriteArrayList<TreeMap<UUID, NcDataListAttr>>();
                    size = fileVisitor.buffDirList.size();
                    if( (size > 0) ){
                        hasData = Boolean.TRUE;
                        emptyCount = 0;
                        
                        TreeMap<UUID, NcDataListAttr> take = fileVisitor.buffDirList.take();
                        //publish(fileVisitor.buffDirList.take());
                        copyOnWriteArrayList.addIfAbsent(take);
                        publishScanList(lComp, copyOnWriteArrayList, listOfListForRecord, 1);
                    }
                    if( hasData ){
                       if( size == 0 ){
                            notExitFromReadData = Boolean.FALSE;
                        } 
                    }
                    //listOfSizeIterarion.add(size);
                } while ( notExitFromReadData );
                emptyCount++;
            } while ( emptyCount < 5 );
            
        } catch (InterruptedException ex) {
            NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
        }
    }
    private static void compChangeForStart(JButton ncButton, NcSwGUIComponentStatus lComp){
        ncButton.setEnabled(false);
        
        String componentPath = NcSwGUIComponentRouter.pathMainFramePanelPageEndProgressBar();
        JProgressBar progressBar = (JProgressBar) lComp.getComponentByPath(componentPath);
        progressBar.setIndeterminate(true);
        
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelCenter();
        JPanel panelLineEnd = (JPanel) lComp.getComponentByPath(componentPath);
        panelLineEnd.repaint();
    }
    private static void compChangeForDone(JButton ncButton, NcSwGUIComponentStatus lComp){
        String componentPath = NcSwGUIComponentRouter.pathMainFramePanelPageEndProgressBar();
        JProgressBar progressBar = (JProgressBar) lComp.getComponentByPath(componentPath);

        progressBar.setIndeterminate(false);
        ncButton.setEnabled(true);
        componentPath = NcSwGUIComponentRouter.pathMainFramePanelCenter();
        JPanel panelCenter = (JPanel) lComp.getComponentByPath(componentPath);
        panelCenter.repaint();
    }
}
