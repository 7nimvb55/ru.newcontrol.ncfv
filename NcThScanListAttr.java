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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
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
        System.out.println("start NcThScanListAttr.fsScanListAttr");
        //Thread.sleep(5000);
        try{
            runMakeIndex();
        }
        catch(InterruptedException ex){
            System.out.println("NcThScanListAttr.fsScanListAttr InterruptedException "
            + ex.getMessage());
            
            
            NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            String classInfoToString = NcAppHelper.getClassInfoToString(NcThScanListAttr.class);
            throw new Exception( NcStrLogMsgField.ERROR.getStr()
                    + classInfoToString
                    + NcStrLogMsgField.MSG_INFO.getStr()
                    + " Start make index InterruptedException ", ex);
        }
        catch(IOException ex){
            System.out.println("NcThScanListAttr.fsScanListAttr IOException "
            + ex.getMessage());
            
            
            NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            String classInfoToString = NcAppHelper.getClassInfoToString(NcThScanListAttr.class);
            throw new Exception( NcStrLogMsgField.ERROR.getStr()
                    + classInfoToString
                    + NcStrLogMsgField.MSG_INFO.getStr()
                    + " Start make index not init ", ex);
            
        } finally {
            compChangeForDone(ncButton, lComp);
        }
        
        //Async start for build index of directories
        //after task finished need repaint button
        //compChangeForDone(ncButton, lComp);
    }
    protected static void runMakeIndex() throws IOException, InterruptedException{
            System.out.println("start NcThScanListAttr.runMakeIndex");
            
            NcThExStatus jobStatus = new NcThExStatus(null);
            System.out.println("new NcThExStatus");
            System.out.println("jobStatus.getScanPath() " + jobStatus.getScanPath().toString());
            
            NcThMifExecPool initJobParam = jobStatus.initJobParam();
            
            initJobParam.execute(new NcThMifWriterDirList(
            jobStatus.getPackDirList()));
            
            initJobParam.execute(new NcThMifRunDirList(
                    jobStatus.getPipeDirList(), 
                    jobStatus.getScanPath()));
            
            initJobParam.execute(new NcThMifTakeDirList(
            jobStatus.getPipeDirList(),
            jobStatus.getFromPipeDirWalker()));
            
            initJobParam.execute(new NcThMifPackDirList(
            jobStatus.getFromPipeDirWalker(),
            jobStatus.getPackDirList()));
            
            
            
            System.out.println("NcThMifExecPool.execute(new NcThMifRunDirList)");
            /*for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
                System.out.println("jobStatus.getPipeDirList().size() = " + jobStatus.getPipeDirList().size());
            }*/
            
            System.out.println("initJobParam.getActiveCount() = " + initJobParam.getActiveCount());
            System.out.println("initJobParam.getLargestPoolSize() = " + initJobParam.getLargestPoolSize());
            System.out.println("initJobParam.getMaximumPoolSize() = " + initJobParam.getMaximumPoolSize());
            System.out.println("initJobParam.getPoolSize() = " + initJobParam.getPoolSize());
            System.out.println("initJobParam.getTaskCount() = " + initJobParam.getTaskCount());
            System.out.println("initJobParam.getQueue().size() = " + initJobParam.getQueue().size());
            initJobParam.shutdown();
    }
    /**
     * @deprecated 
     * @param ncButton
     * @param lComp
     * @param pathDirToScan
     * @throws Exception 
     */
    protected static void oldVerfsScanListAttr(JButton ncButton, NcSwGUIComponentStatus lComp, Path pathDirToScan) throws Exception{
        
        compChangeForStart(ncButton, lComp);
        ArrayList<String> arrStr = new ArrayList<String>();
        try{
            Path pathDevDirToScan = Paths.get("/usr/home/wladimirowichbiaran/work");

            
            arrStr.add("[START][SCANNER][DIRECTORY]"
            + pathDevDirToScan.toString());
            NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
            ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>> getResult = 
                    new ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>();
            NcThExecPool executorScan = new NcThExecPool(pathDevDirToScan);
            
            executorScan.autoExecRouter();
            /*NcThExRouter thRouter = new NcThExRouter();
            
            Future submitRouter = executorScan.submit(thRouter);
            thRouter.setExecPool(executorScan);
            int countWait = 0;
            
            while( !submitRouter.isDone() ){
                if( submitRouter.isCancelled() ){
                     NcAppHelper.outMessage("[BUTTONTHREAD] is canceled in main thread " + countWait);
                }
                NcAppHelper.outMessage("[BUTTONTHREAD] not for done " + countWait);
                countWait++;
                if( countWait == 30 ){
                    NcAppHelper.outMessage("[BUTTONTHREAD] call for thread.setNeedSleep(30) " + countWait);
                    thRouter.setNeedSleep(30L);
                }
                if( countWait == 70 ){
                    NcAppHelper.outMessage("[BUTTONTHREAD] call for thread.setNeedSleep(0) " + countWait);
                    thRouter.setNeedSleep(0L);
                }
                if( countWait > 100 ){
                    NcAppHelper.outMessage("[BUTTONTHREAD] call for thread.finishHim " + countWait);
                    thRouter.finishHim();
                }
                if( countWait > 130 ){
                    NcAppHelper.outMessage("[BUTTONTHREAD] call for thread.finishHimByInterrupted " + countWait);
                    thRouter.finishHimByInterrupted();
                }
            }*/
            
            //NcThExDirTreeWalk dirWalker = null;
            //while( dirWalker == null){
            //    try{
            //        thRouter.startDirWalk(pathDevDirToScan);
            //        dirWalker = thRouter.getDirWalker();
            //    }catch(Exception ex){
            //            NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            //    }
            //}
            //BlockingQueue queue = dirWalker.getQueue();
            /*Future submit = executorScan.submit(dirWalker);
            while( !submit.isDone() ){
                try{
                    queue = (BlockingQueue) submit.get();
                }catch(Exception ex){
                    NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
                }
            }
            int size = queue.size();
            arrStr.add("[SIZE]"
            + size);
            NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);*/
            //NcAppHelper.outMessage("executorScan create");
            //
            //NcThExRouter thRouter = new NcThExRouter();
            //Future submitRouter = executorScan.submit(thRouter);
            //NcAppHelper.outMessage((String) submitRouter.get());
            //NcThExRouter thRouter = new NcThExRouter(executorScan);
            
            
            /*ConcurrentSkipListMap call = thRouter.call();
            //Future submitRouter = executorScan.submit(thRouter);
            if( thRouter.startDirWalk(pathDevDirToScan)){
                NcAppHelper.outMessage("walker create");
                dirWalker = thRouter.getDirWalker();
            }
            
            if( dirWalker != null){
                if( thRouter.startListScaner(dirWalker)){
                    NcAppHelper.outMessage("Scaner create");
                }else{
                    NcAppHelper.outMessage("Scaner not create");
                }
            }else{
                NcAppHelper.outMessage("Scaner is null");
            }*/
            
            
            /*NcThExDirTreeWalk dirWalker = new NcThExDirTreeWalk(pathDevDirToScan);
            
            
            
            Future<ConcurrentSkipListMap<UUID, ConcurrentSkipListMap<UUID, NcDataListAttr>>> futureWalk =
                    executorScan.submit(dirWalker);
            while( !futureWalk.isDone() ){
                try {
                    arrStr.add("[WAIT][RESULT][ALL][DIRECTORY]"
                        + pathDevDirToScan.toString());
                    NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
                    getResult.putAll(futureWalk.get());

                } catch (InterruptedException ex) {
                    NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
                } catch (ExecutionException ex) {
                    NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
                }
            }
            
            NcThExListAttrScanDir dirListScanner = new NcThExListAttrScanDir(dirWalker);
            NcAppHelper.outMessage("scaner create");
            
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
            NcAppHelper.outMessage("packer create");
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
            }*/

            executorScan.shutdown();
            arrStr.clear();
            arrStr.add("[EXECUTOR][SHUTDOWN]");
            NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
            
        }catch(IllegalThreadStateException ex){
            
            System.out.println("IllegalThreadStateException");
            
            compChangeForDone(ncButton, lComp);
            NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            String classInfoToString = NcAppHelper.getClassInfoToString(NcThScanListAttr.class);
            throw new Exception( NcStrLogMsgField.ERROR.getStr()
                    + classInfoToString
                    + NcStrLogMsgField.MSG_INFO.getStr()
                    + " part of start make index is Interrupted ", ex);
        }catch(Exception ex){
            
            System.out.println("Exception: catch in NcThScanListAttr.fsScanListAttr");
            
            compChangeForDone(ncButton, lComp);
            NcAppHelper.logException(NcThScanListAttr.class.getCanonicalName(), ex);
            String classInfoToString = NcAppHelper.getClassInfoToString(NcThScanListAttr.class);
            throw new Exception( NcStrLogMsgField.ERROR.getStr()
                    + classInfoToString
                    + NcStrLogMsgField.MSG_INFO.getStr()
                    + " part of start make index ", ex);
        }
        compChangeForDone(ncButton, lComp);
        
    }

    private static void compChangeForStart(JButton ncButton, NcSwGUIComponentStatus lComp){
        ncButton.setEnabled(false);
        String name = ncButton.getText();
        ncButton.setText("(W)" + name);
        ActionListener[] actionListeners = ncButton.getActionListeners();
        
        ArrayList<String> arrStrAction = new ArrayList<String>();
        for (ActionListener actionListener : actionListeners) {
            arrStrAction.add(actionListener.toString());
        }
        arrStrAction.add("[MAKE INDEX STARTED]");
        ncButton.repaint();
        NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStrAction);
        /*ncButton.addActionListener(new ActionListener(){
        public void  actionPerformed(ActionEvent e){
        try {
        } catch (Exception ex) {
        NcAppHelper.logException(NcSwGUIComponent.class.getCanonicalName(), ex);
        }
        }
        }
        );*/
        
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
