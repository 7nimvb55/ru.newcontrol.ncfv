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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Application logger system:
 * 1.   AppLoggerList class - make objects lists, data process and control Bus:
 * 1.1. First list implements of Runnables for reader Logger, writer Logger
 * 1.2. Second list new instance of Threads object for secondary etc use for objects provided by first list
 * 1.3. AppLoggerState class - init and save state for runnables objects factory
 * 1.4. AppLoggerRule class - provide for methids of init, change state, controls, and managment for data Bus
 * 
 * 
 * 
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerList {
    private Boolean isNewLoggerList;
    
    private Boolean isNeedForSaveCss;
    private Boolean isNeedForSaveJs;
    private Boolean isNeedForSaveIndexHtml;
    
    private int countOfToQueueCapacityChange;
    
    private ArrayBlockingQueue<ArrayList<String>> commandsOutPut;
    private ArrayBlockingQueue<String> listForRunnableLogStrs;
    private ArrayBlockingQueue<String> readedLinesFromLogHTML;
    private ArrayList<ArrayBlockingQueue<String>> readedArrayForLines;
    private ArrayBlockingQueue<String> readedLinesFromTablesWork;
    private ConcurrentSkipListMap<String, Path> listLogStorageFiles;
    private Boolean isLogHtmlStorageInit;
    
    private ArrayBlockingQueue<Path> readedFilesListInLogHtmlByTableMask;
    private Boolean isLogHtmlListTableFilesInit;
    
    private AppLoggerRule managerForOrder;
    private AppLoggerState currentJob;
    
    private String instanceStartTimeWithMS;
    
    private Path fileForWrite;
    private Path fileForRead;
    
    

    public AppLoggerList() {
        setTrueNewLoggerList();
        
        this.countOfToQueueCapacityChange = 0;
        
        setFalseNeedForSaveCss();
        setFalseNeedForSaveJs();
        setFalseNeedForSaveIndexHtml();
        
        this.commandsOutPut = new ArrayBlockingQueue<ArrayList<String>>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        this.listForRunnableLogStrs = new ArrayBlockingQueue<String>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        this.readedLinesFromLogHTML = new ArrayBlockingQueue<String>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        this.readedLinesFromTablesWork = new ArrayBlockingQueue<String>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        
        this.readedArrayForLines = new ArrayList<ArrayBlockingQueue<String>>();
        
        setFalseForLogHtmlListTableFiles();
        setFalseForLogHtmlStorage();
        
        this.instanceStartTimeWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        this.listLogStorageFiles = getLogHtmlStorageList();
        
        this.managerForOrder = new AppLoggerRule(this.listForRunnableLogStrs, this.readedLinesFromTablesWork, this.listLogStorageFiles);
        this.currentJob = this.managerForOrder.getCurrentJob();
    }
    
    protected void doWriteToLogHtmlCurrentFile(){
        if( isNeedForSaveIndexHtml() ){
            setNewIndexFileForLogHtml();
        }
        else if( isNeedForSaveCss() ){
            setNewCssFileForLogHtml();
        }
        else if( isNeedForSaveJs() ){
            setNewJsFileForLogHtml();
        }
        else if( !isNewLoggerList() ){
            setNewTableFileForLogHtml();
        }
        setFalseNewLoggerList();
        makeWrite();
    }
    protected void makeWrite(){
        
        this.managerForOrder.setStringBusForLogWrite(AppObjectsBusHelper.cleanBusForRunnables(this.listForRunnableLogStrs));
        System.out.println("-------|||||||||-----------|||||||||------------AppLoggerList.makeWrite for " + this.managerForOrder.getStringBusForLogWrite().size());
        System.out.println("-------|||||||||-----------|||||||||------------AppLoggerList.makeWrite to " + this.managerForOrder.getCurrentJob().getToHTMLLogFileName().toString());
        waitForPrevJobDoneForWriter();
        String nowTimeStringWithMS = 
                    AppFileOperationsSimple.getNowTimeStringWithMS();
        ThreadGroup newJobThreadGroup = new ThreadGroup("WriterGroup-" + nowTimeStringWithMS);
        Thread writeToHtmlByThread = new Thread(newJobThreadGroup, this.managerForOrder.getRunnableWriter(), "writerToHtml-" + nowTimeStringWithMS);
        System.out.println("Write Thread id " + writeToHtmlByThread.getId() +  " thread name " + writeToHtmlByThread.getName() + " State " + writeToHtmlByThread.getState().name());
        writeToHtmlByThread.start();
        System.out.println("Write Thread id " + writeToHtmlByThread.getId() +  " thread name " + writeToHtmlByThread.getName() + " State " + writeToHtmlByThread.getState().name());
        waitForPrevJobDoneForWriter();
        System.out.println("Write Thread id " + writeToHtmlByThread.getId() +  " thread name " + writeToHtmlByThread.getName() + " State " + writeToHtmlByThread.getState().name());
        //cleanLogStingQueue();
    }
    protected void cleanLogStingQueue(){
        this.listForRunnableLogStrs.clear();
    }
    protected void waitForPrevJobDoneForWriter(){
        System.out.println("-------|||||||||-----------|||||||||------------make write prev isjobdone " + this.currentJob.isToHTMLJobDone());
        if( !this.currentJob.isToHTMLNewRunner() ){
            try{
                System.out.println("wait for prev done");
                while( !this.currentJob.isToHTMLJobDone() ){
                    Thread curThr = Thread.currentThread();
                    curThr.sleep(50);
                }
                 System.out.println(" end wait for prev done");
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } catch(SecurityException ex){
                ex.printStackTrace();
            }
        }
    }
    protected void waitForPrevJobDoneForReader(){
        if( !this.currentJob.isFromHTMLNewRunner() ){
            try{
                while( !this.currentJob.isFromHTMLJobDone() ){
                    Thread curThr = Thread.currentThread();
                    curThr.sleep(50);
                }
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } catch(SecurityException ex){
                ex.printStackTrace();
            }
        }
    }
    protected void setNewCssFileForLogHtml(){
        this.fileForWrite = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_CSS_PREFIX);
        this.currentJob.setToHTMLFileName(this.fileForWrite);
    }
    protected void setNewJsFileForLogHtml(){
        this.fileForWrite = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_JS_MENU_PREFIX);
        this.currentJob.setToHTMLFileName(this.fileForWrite);
    }
    protected void setNewIndexFileForLogHtml(){
        this.fileForWrite = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_INDEX_PREFIX);
        this.currentJob.setToHTMLFileName(this.fileForWrite);
    }
    protected void setNewTableFileForLogHtml(){
        if( !this.currentJob.isToHTMLLogFileNameChanged() ){
            this.fileForWrite = getNewFileForLogHtml();
            this.currentJob.setToHTMLFileName(this.fileForWrite);
        }
        System.out.println("file for write " + this.fileForWrite.toString());
    }
    protected void doReadFromLogHtmlListOfTables(){
        ArrayBlockingQueue<Path> logHtmlListTableFiles = getLogHtmlListTableFiles();
        for(Path elementOfTables : logHtmlListTableFiles){
            waitForPrevJobDoneForReader();
            setNextReadedFileFromLogHtml(elementOfTables);
            addAncorStructure(elementOfTables);
            readListOfTables();
            waitForPrevJobDoneForReader();
        }
        addReadedTablesIntoList();
    }
    protected void addReadedTablesIntoList(){
        for(ArrayBlockingQueue<String> tableItems : this.readedArrayForLines){
            this.listForRunnableLogStrs.addAll(tableItems);
        }
    }
    protected void addAncorStructure(Path fileForRead){
        String strForAncor = "<p><a name=\"" + fileForRead.getFileName().toString().split("\\.")[0] + "\">"
                        + fileForRead.toString() + "</a></p>";
        addStringToRunnableBus(strForAncor);
    }
    protected void readListOfTables(){
        waitForPrevJobDoneForReader();
        String nowTimeStringWithMS = 
                    AppFileOperationsSimple.getNowTimeStringWithMS();
        //this.managerForOrder.setStringBusForLogRead(this.readedLinesFromTablesWork);
        ThreadGroup newJobThreadGroup = new ThreadGroup("ReaderGroup-" + nowTimeStringWithMS);
        Thread readFromHtmlByThread = new Thread(newJobThreadGroup, this.managerForOrder.getRunnableReader(), "readToHtml-" + nowTimeStringWithMS);
        System.out.println("Write Thread id " + readFromHtmlByThread.getId() +  " thread name " + readFromHtmlByThread.getName() + " State " + readFromHtmlByThread.getState().name());
        readFromHtmlByThread.start();
        System.out.println("Write Thread id " + readFromHtmlByThread.getId() +  " thread name " + readFromHtmlByThread.getName() + " State " + readFromHtmlByThread.getState().name());
        waitForPrevJobDoneForReader();
        System.out.println("Write Thread id " + readFromHtmlByThread.getId() +  " thread name " + readFromHtmlByThread.getName() + " State " + readFromHtmlByThread.getState().name());
        this.readedArrayForLines.add(this.managerForOrder.getStringBusForLogRead());
    }
    protected void setNextReadedFileFromLogHtml(Path elementOfTables){
        if( !this.currentJob.isFromHTMLLogFileNameChanged() ){
            this.fileForRead = elementOfTables;
            this.currentJob.setFromHTMLFileName(this.fileForRead);
        }
    }
    protected void setFalseNeedForSaveCss(){
        this.isNeedForSaveCss = Boolean.FALSE;
    }
    protected void setTrueNeedForSaveCss(){
        this.isNeedForSaveCss = Boolean.TRUE;
    }
    protected Boolean isNeedForSaveCss(){
        return this.isNeedForSaveCss;
    }
    protected void setFalseNeedForSaveJs(){
        this.isNeedForSaveJs = Boolean.FALSE;
    }
    protected void setTrueNeedForSaveJs(){
        this.isNeedForSaveJs = Boolean.TRUE;
    }
    protected Boolean isNeedForSaveJs(){
        return this.isNeedForSaveJs;
    }
    protected void setFalseNeedForSaveIndexHtml(){
        this.isNeedForSaveIndexHtml = Boolean.FALSE;
    }
    protected void setTrueNeedForSaveIndexHtml(){
        this.isNeedForSaveIndexHtml = Boolean.TRUE;
    }
    protected Boolean isNeedForSaveIndexHtml(){
        return this.isNeedForSaveIndexHtml;
    }
    protected void setFalseNewLoggerList(){
        this.isNewLoggerList = Boolean.FALSE;
    }
    protected void setTrueNewLoggerList(){
        this.isNewLoggerList = Boolean.TRUE;
    }
    protected Boolean isNewLoggerList(){
        return this.isNewLoggerList;
    }
    protected void setFalseForLogHtmlListTableFiles(){
        this.isLogHtmlListTableFilesInit = Boolean.FALSE;
    }
    protected void setTrueForLogHtmlListTableFiles(){
        this.isLogHtmlListTableFilesInit = Boolean.TRUE;
    }
    protected Boolean isSetForLogHtmlListTableFiles(){
        return this.isLogHtmlListTableFilesInit;
    }
    protected ArrayBlockingQueue<Path> getLogHtmlListTableFiles(){
        if( !isSetForLogHtmlListTableFiles() ){
            Path dirForRead = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR);
            ArrayList<Path> filesByMaskFromDir = AppFileOperationsSimple.getFilesByMaskFromDir(
                dirForRead,
                "{" + AppFileNamesConstants.LOG_HTML_TABLE_PREFIX + "}*");
            this.readedFilesListInLogHtmlByTableMask = new ArrayBlockingQueue<Path>(filesByMaskFromDir.size());
            for( Path fileForRead : filesByMaskFromDir ){
                this.readedFilesListInLogHtmlByTableMask.add(fileForRead);
            }
            setTrueForLogHtmlListTableFiles();
        }
        return this.readedFilesListInLogHtmlByTableMask;
    }
    protected ArrayBlockingQueue<Path> updateLogHtmlListTableFiles(){
        if( isSetForLogHtmlListTableFiles() ){
            setFalseForLogHtmlListTableFiles();
            this.readedFilesListInLogHtmlByTableMask.clear();
        }
        return this.getLogHtmlListTableFiles();
    }
    
    protected void setFalseForLogHtmlStorage(){
        this.isLogHtmlStorageInit = Boolean.FALSE;
    }
    protected void setTrueForLogHtmlStorage(){
        this.isLogHtmlStorageInit = Boolean.TRUE;
    }
    protected Boolean isSetForLogHtmlStorage(){
        return this.isLogHtmlStorageInit;
    }
    protected ConcurrentSkipListMap<String, Path> getLogHtmlStorageList(){
        if( !isSetForLogHtmlStorage() ){
            Path logForHtmlCurrentLogSubDir = 
                    AppFileOperationsSimple.getLogForHtmlCurrentLogSubDir(this.instanceStartTimeWithMS);
            this.listLogStorageFiles = 
                    AppFileOperationsSimple.getNewLogFileInLogHTML(logForHtmlCurrentLogSubDir);
            this.listLogStorageFiles.put(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR, logForHtmlCurrentLogSubDir);
            this.fileForWrite = getLogFilesListElement(AppFileNamesConstants.LOG_HTML_TABLE_PREFIX);
            setTrueForLogHtmlStorage();
        }
        return this.listLogStorageFiles;
    }
    protected void addAllStringsToRunnableBus(ArrayBlockingQueue<String> linesForSave){
        String pollFirstForSaveJsMenu = "";
        do{
            pollFirstForSaveJsMenu = linesForSave.poll();
            if( pollFirstForSaveJsMenu != null ){
                addStringToRunnableBus(pollFirstForSaveJsMenu);
            }
        }while( !linesForSave.isEmpty() );
    }
    
    protected void addStringToRunnableBus(String forPut){
        extendSizeForStringToQueue();
        this.listForRunnableLogStrs.add(forPut);
    }
    /**
     * @todo need develop code for write to file part when queue is full,
     * save data about writed part and save more parts in the next iterations
     */
    protected void extendSizeForStringToQueue(){
        if( ((this.listForRunnableLogStrs.size() 
                - (this.listForRunnableLogStrs.size() % 10)) / 10) 
                > this.listForRunnableLogStrs.remainingCapacity() 
        ){
            System.out.println("-|-|-|-|-QUEUE WARNING SIZE "
                + this.listForRunnableLogStrs.size()
                + "-|-|-|-|-QUEUE REMAINING CAPACITY "
                + this.listForRunnableLogStrs.remainingCapacity()
                + "-|-|-|-|-COUNT OF CHANGE QUEUE CAPACITY"
                + getCountOfToQueueCapacityChange()
            );
            int nowSize = this.listForRunnableLogStrs.size() + this.listForRunnableLogStrs.remainingCapacity();
            ArrayBlockingQueue<String> extendedQueue = new ArrayBlockingQueue<String>(nowSize * 10);
            do{ 
                String poll = this.listForRunnableLogStrs.poll();
                if( poll != null ){
                    extendedQueue.add(poll);
                }
            }while( !this.listForRunnableLogStrs.isEmpty() );
            incrementCountOfToQueueCapacityChange();
            this.listForRunnableLogStrs = extendedQueue;
        }
        
    }
    protected int getCountOfToQueueCapacityChange(){
        return this.countOfToQueueCapacityChange;
    }
    protected void incrementCountOfToQueueCapacityChange(){
        this.countOfToQueueCapacityChange++;
    }
    protected void addStringFromRunnableBus(String forPut){
        this.readedLinesFromLogHTML.add(forPut);
    }
    protected Path getLogFilesListElement(String keyForPath){
        return this.listLogStorageFiles.get(keyForPath);
    }
    protected void setLogFilesListElement(String keyForPath, Path ementForPut){
        this.listLogStorageFiles.put(keyForPath, ementForPut);
    }
    protected Path getCurrentLogHtmlStorageSubDir(){
        return this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR);
    }
    protected Path getNewFileForLogHtml(){
        return AppFileOperationsSimple.getNewLogHtmlTableFile(getCurrentLogHtmlStorageSubDir());
    }
    protected void addToCommandsResultBus(ArrayList<String> forPut){
        this.commandsOutPut.add(forPut);
    }
    protected void clearCommandsResultBus(){
        this.commandsOutPut.clear();
    }
    protected void clearStringToRunnableBus(){
        this.listForRunnableLogStrs.clear();
    }
    protected void clearStringFromRunnableBus(){
        this.readedLinesFromLogHTML.clear();
    }
    protected ArrayBlockingQueue<ArrayList<String>> getCommandsOutPut(){
        return this.commandsOutPut;
    }
    protected ArrayBlockingQueue<String> getListForRunnableLogStrs(){
        return this.listForRunnableLogStrs;
    }
    protected ArrayBlockingQueue<String> getReadedLinesFromLogHTML(){
        return this.readedLinesFromLogHTML;
    }
}
