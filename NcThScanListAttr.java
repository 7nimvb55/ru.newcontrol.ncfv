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
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
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
        Path pathDevDirToScan = pathDirToScan;

        ArrayList<String> arrStr = new ArrayList<String>();
        arrStr.add("[START][SCANNER][DIRECTORY]"
        + pathDevDirToScan.toString());
        NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> getResult = 
                new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>();
        NcThExecPool executorScan = new NcThExecPool();
        
        
        NcThExDirTreeWalk dirWalker = new NcThExDirTreeWalk(pathDevDirToScan);
        NcThExListAttrScanDir dirListScanner = new NcThExListAttrScanDir(dirWalker);
        
        Future<ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>> futureWalk =
                executorScan.submit(dirWalker);
        Future<ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>> futureScan =
                executorScan.submit(dirListScanner);
        arrStr.clear();
        while( !futureScan.isDone() ){
            try {
                arrStr.add("[WAIT][RESULT][ALL][DIRECTORY]"
                    + pathDevDirToScan.toString());
                NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
                getResult.putAll(futureScan.get());
                
            } catch (InterruptedException ex) {
                NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            } catch (ExecutionException ex) {
                NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            }
        }
        
        ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> listOfPacket =
                new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>();
        
        NcThExListPack resultPacker = new NcThExListPack(getResult, lComp);
        Future<ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>> futurePack =
                executorScan.submit(resultPacker);
        arrStr.clear();
        while( !futurePack.isDone() ){
            try {
                arrStr.add("[WAIT][RESULT][ALL][PACK]"
                    + pathDevDirToScan.toString());
                NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
                listOfPacket.putAll(futurePack.get());
                
            } catch (InterruptedException ex) {
                NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            } catch (ExecutionException ex) {
                NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            }
        }
        ArrayList<String> arrOutStr = new ArrayList<String>();
        arrOutStr.clear();
        int recordIdx = 0;
        for (Map.Entry<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> itemPacket : listOfPacket.entrySet()) {
            //arrOutStr.clear();
            UUID key = itemPacket.getKey();
            ConcurrentSkipListMap<UUID, NcDataListAttr> value = itemPacket.getValue();
            arrOutStr.add("<html><body><b>"
                    + "[PACKET][key]" + key
                    + "[SIZE]" + value.size()
                    + "</b></body><html>");
            recordIdx = 0;
            for (Map.Entry<UUID, NcDataListAttr> entryPack : value.entrySet()) {

                UUID keyInPack = entryPack.getKey();
                NcDataListAttr valueInPack = entryPack.getValue();
                arrOutStr.add("[" + recordIdx + "]"
                    + "[key]" + keyInPack
                    + "[value]" + valueInPack.getShortDataToString());
                recordIdx++;
            }
        }
        if( arrOutStr.size() > 0 ){
                NcThWorkerUpGUITreeOutput.outputTreeAddChildren(lComp, arrOutStr);
        }

        executorScan.shutdown();
        arrStr.add("[EXECUTOR][SHUTDOWN][RESULT][SIZE]" + listOfPacket.size());
        
        NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
        arrStr.clear();
        compChangeForDone(ncButton, lComp);
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
