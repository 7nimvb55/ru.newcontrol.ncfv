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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
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
        
        ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> listFromScan = 
                new ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>>();
        ArrayList<String> arrStr = new ArrayList<String>();
        ArrayBlockingQueue<TreeMap<UUID, NcDataListAttr>> pipeDirList = new ArrayBlockingQueue(1000, true);
        
        NcFsIdxFileVisitor fileVisitor = new NcFsIdxFileVisitor(lComp, pipeDirList);
        
        arrStr.add("pathToStart:" + pathToStart.toString());
        
        arrStr.add("[count Dir]"
        + fileVisitor.getCountPostVisitDir()
        + "[count File]"
        + fileVisitor.getCountVisitFile());
        
        underGroundScan(pathToStart, fileVisitor, listFromScan, 1, lComp);
        ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> listOfPacket =
                new ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>>();
        //packetCreator(listFromScan, listOfPacket);
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
            NcFsIdxFileVisitor fileVisitor, ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> listOfListForRecord, int countTh, NcSwGUIComponentStatus lComp){
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
    private static void publishScanList(NcSwGUIComponentStatus lComp,
            ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> pipeDirList,
            ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> listPack,
            int countTh){
        Runnable scanResult = new Runnable() {
            private final TreeMap<UUID, NcDataListAttr> makeForRecord = 
                new TreeMap<UUID, NcDataListAttr>();
            private final TreeMap<UUID, NcDataListAttr> listForRecord = 
                new TreeMap<UUID, NcDataListAttr>();
            final transient ReentrantLock lock = new ReentrantLock();
            
            final Semaphore avalableThToScan = new Semaphore(countTh);
            public void run(){
                try {
                    avalableThToScan.acquire();
                    
                    packetCreator(lComp, pipeDirList, listPack, 1);
                    
                    ArrayList<String> arrOutStr = null;
                    arrOutStr = new ArrayList<String>();

                    ArrayList<String> arrStr = null;
                    arrStr = new ArrayList<String>();
                    int numPart = 0;

                    for(Map.Entry<UUID, TreeMap<UUID, NcDataListAttr>> item : pipeDirList.entrySet()){
                        //for publish and save to index code here
                        NcDataTransporter<TreeMap<UUID, NcDataListAttr>> dataPack =
                                new NcDataTransporter<TreeMap<UUID, NcDataListAttr>>();
                        
                        
                        //dataPack.putInPack(item);
                        if( item.getValue().size() > 0 ){
                            
                            
                                //listPack.put(item.getKey(), item.getValue());
                            arrOutStr.clear();
                            for (Map.Entry<UUID, NcDataListAttr> entry : item.getValue().entrySet()) {
                                
                                UUID key = entry.getKey();
                                NcDataListAttr value = entry.getValue();
                                arrOutStr.add("[key]" + key
                                    + "[value]" + value.getShortDataToString());
                            }
                            if( arrOutStr.size() > 0 ){
                                NcThWorkerUpGUITreeOutput.outputTreeAddChildren(lComp, arrOutStr);
                                
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
                + "[listOfListForRecord.size]" + listPack.size());
                NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, listStrArr);
                listStrArr = null;
            }
        };
        Thread backGroundResult = new Thread(scanResult);
        backGroundResult.checkAccess();
        backGroundResult.start();
    }
    private static void packetCreator(
            NcSwGUIComponentStatus lComp,
            ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> pipeDirList,
            ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> listPack,
            int countTh){
        Runnable scanResult = new Runnable() {
            final transient ReentrantLock lock = new ReentrantLock();
            final Semaphore avalableThToScan = new Semaphore(countTh);
            public void run(){
                try {
                    avalableThToScan.acquire();
                    
                    ArrayList<String> listStrArr = new ArrayList<String>();
                    //final ReentrantLock lock = this.lock;
                    do{
                    listStrArr.clear();
                    TreeMap<UUID, NcDataListAttr> dataPack =
                                new TreeMap<UUID, NcDataListAttr>();
                    

                            ConcurrentHashMap.KeySetView<UUID, TreeMap<UUID, NcDataListAttr>> keySetListPack = listPack.keySet();
                            for (Iterator<UUID> iterator = keySetListPack.iterator(); iterator.hasNext();) {
                                final ReentrantLock lock = this.lock;
                                lock.lock();
                                try {
                                    UUID nextKey = iterator.next();
                                    TreeMap<UUID, NcDataListAttr> getPacket = listPack.get(nextKey);
                                    if( getPacket == null ){
                                        continue;
                                    } else {
                                        int packSize = getPacket.size();
                                        listStrArr.add("[packetCreator][run][listPack.("
                                                + nextKey + ").size]"
                                                + packSize);
                                        if( packSize != 100 ){
                                            dataPack = listPack.remove(nextKey);

                                        }
                                    }
                                } finally {
                                        lock.unlock();
                                }
                            }
                            
                    
                    listStrArr.add("[packetCreator][run][initPacket][dataPack.size]"
                        + dataPack.size());
                    
                    listStrArr.add("[packetCreator][run][pipeDirList.size]"
                        + pipeDirList.size()
                        + "[packetCreator][run][startIteration]"
                        + "[dataPack.size]" + dataPack.size()
                        + "[listPack.size]" + listPack.size());
                    ConcurrentHashMap.KeySetView<UUID, TreeMap<UUID, NcDataListAttr>> keySet = pipeDirList.keySet();
                    for (Iterator<UUID> iterator = keySet.iterator(); iterator.hasNext();) {
                        UUID next = iterator.next();
                    
                        //for publish and save to index code here
                        /*lock.lock();
                        try {*/
                        TreeMap<UUID, NcDataListAttr> nowPack = pipeDirList.remove(next);
                        int nowSize = nowPack.size();
                        
                        int currentPack = dataPack.size();
                        listStrArr.add("[packetCreator][run][pipeDirList.remove][nowPack][size]"
                        + nowSize
                        + "[dataPack.size]" + currentPack
                        + "[listPack.size]" + listPack.size());
                        if( currentPack == 100 ){
                            listPack.put(UUID.randomUUID(), dataPack);
                            dataPack = new TreeMap<UUID, NcDataListAttr>();
                            listStrArr.add("[packetCreator][run][initPacket][dataPack.size]"
                                + dataPack.size());
                        }
                        /*} finally {
                            lock.unlock();
                        }*/
                        currentPack = dataPack.size();
                        if( (nowSize + currentPack)  < 101 ){
                            dataPack.putAll(nowPack);
                            currentPack = dataPack.size();
                            listStrArr.add("[packetCreator][run][dataPack.putAll][nowPack][size]"
                                + nowSize
                                + "[dataPack.size]" + currentPack
                                + "[listPack.size]" + listPack.size());
                            continue;
                        }
                        if( (nowSize + currentPack) > 100){
                            for (Map.Entry<UUID, NcDataListAttr> entry : nowPack.entrySet()) {
                                UUID key = entry.getKey();
                                NcDataListAttr value = entry.getValue();
                                currentPack = dataPack.size();
                                if( currentPack == 100 ){
                                    listPack.put(UUID.randomUUID(), dataPack);
                                    dataPack = new TreeMap<UUID, NcDataListAttr>();
                                    listStrArr.add("[packetCreator][run][initPacket][dataPack.size]"
                                        + dataPack.size());
                                }
                                dataPack.put(key, value);
                            }
                        }
                        
                    }
                    listPack.put(UUID.randomUUID(), dataPack);
                    dataPack = new TreeMap<UUID, NcDataListAttr>();
                    listStrArr.add("[packetCreator][run][pipeDirList.size]"
                        + pipeDirList.size()
                        + "[packetCreator][run][endIteration]"
                        + "[dataPack.size]" + dataPack.size()
                        + "[listPack.size]" + listPack.size());
                    
                    }while( pipeDirList.size() != 0 );
                    listStrArr.add("[packetCreator][run][finishStady][listPack.size]"
                        + listPack.size());
                    for (Map.Entry<UUID, TreeMap<UUID, NcDataListAttr>> entryItem : listPack.entrySet()) {
                        UUID key = entryItem.getKey();
                        TreeMap<UUID, NcDataListAttr> value = entryItem.getValue();
                        listStrArr.add("[packetCreator][run][report][listPack(" + key + ").size]"
                        + value.size());
                    }
                    NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, listStrArr);
                    avalableThToScan.release();
                } catch (InterruptedException ex) {
                    NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
                }
                }
            };
            Thread backGroundResult = new Thread(scanResult);
            backGroundResult.checkAccess();
            backGroundResult.start();
    }
    private static void getListOfPacket(){
        
    }
    private static void doBackgroundReadList(Path pathToStart,
            NcFsIdxFileVisitor fileVisitor, NcSwGUIComponentStatus lComp, ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> listOfListForRecord){
        
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
                    ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>> copyOnWriteArrayList = new ConcurrentHashMap<UUID, TreeMap<UUID, NcDataListAttr>>();
                    size = fileVisitor.buffDirList.size();
                    if( (size > 0) ){
                        hasData = Boolean.TRUE;
                        emptyCount = 0;
                        
                        
                        
                        TreeMap<UUID, NcDataListAttr> take = fileVisitor.buffDirList.take();
                        UUID key = UUID.randomUUID();
                        copyOnWriteArrayList.put(key, take);
                        
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
