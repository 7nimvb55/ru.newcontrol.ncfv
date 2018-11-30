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
    
    private AppLoggerBus loggerBus;
    private AppLoggerRule managerForOrder;
    private AppLoggerBusJob loggerJobBus;
    private AppLoggerState currentJob;
    
    private ArrayBlockingQueue<ArrayBlockingQueue<String>> commandsOutPut;
    private ArrayBlockingQueue<String> listForRunnableLogStrs;
    private ArrayBlockingQueue<String> readedLinesFromLogHTML;
    private ArrayBlockingQueue<ArrayBlockingQueue<String>> readedArrayForLines;
    //private ArrayBlockingQueue<String> readedLinesFromTablesWork;
    private ConcurrentSkipListMap<String, Path> listLogStorageFiles;
    
    
    private ArrayBlockingQueue<Path> readedFilesListInLogHtmlByTableMask;
    
    
    
    
    
    
    private Path fileForWrite;
    private Path fileForRead;
    
    

    public AppLoggerList() {
        setTrueNewLoggerList();
        
        setFalseNeedForSaveCss();
        setFalseNeedForSaveJs();
        setFalseNeedForSaveIndexHtml();
        
        this.loggerBus = new AppLoggerBus();
        this.loggerJobBus = new AppLoggerBusJob();
        
        System.out.println("+|0000001|+|AppLoggerList||||||||+++++++++++|||||||||+++++++++++new AppLoggerList(); ");
        this.commandsOutPut = loggerBus.getCommandsOutPut();
        this.listForRunnableLogStrs = loggerBus.getListForRunnableLogStrs();
        //this.readedLinesFromLogHTML = loggerBus.getReadedLinesFromLogHTML();
        //this.readedLinesFromTablesWork = 
        
        //this.readedArrayForLines = loggerBus.
        this.listLogStorageFiles = this.loggerBus.getLogHtmlStorageList();
        this.fileForWrite = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_TABLE_PREFIX);
        
        this.managerForOrder = new AppLoggerRule(this.loggerBus, this.loggerJobBus);
        this.currentJob = this.managerForOrder.getCurrentJob();
    }
    protected AppLoggerBus getLoggerBus(){
        return this.loggerBus;
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
        
        //this.managerForOrder.setStringBusForLogWrite(AppObjectsBusHelper.cleanBusForRunnables(this.listForRunnableLogStrs));
        System.out.println("-------|||||||||-----------|||||||||------------AppLoggerList.makeWrite for " 
                + this.managerForOrder.getStringBusForLogWrite().size());
        System.out.println("-------|||||||||-----------|||||||||------------AppLoggerList.makeWrite to " 
                + this.managerForOrder.getCurrentJob().getToHTMLLogFileName().toString());
        waitForPrevJobDoneForWriter();
        String nowTimeStringWithMS = 
                    AppFileOperationsSimple.getNowTimeStringWithMS();
        String nameJobThreadGroup = "WriterGroup-" + nowTimeStringWithMS;
        ThreadGroup newJobThreadGroup = new ThreadGroup(nameJobThreadGroup);
        String nameJobThread = "writerToHtml-" + nowTimeStringWithMS;
        
        Thread writeToHtmlByThread = new Thread(newJobThreadGroup, this.managerForOrder.getRunnableWriter(), nameJobThread);
        this.managerForOrder.getWriterCurrentJob(
            AppObjectsBusHelper.cleanBusForRunnables(this.listForRunnableLogStrs),
            newJobThreadGroup,
            writeToHtmlByThread,
            this.fileForWrite
        );
        System.out.println("Write Thread id " 
                + writeToHtmlByThread.getId() 
                +  " thread name " 
                + writeToHtmlByThread.getName() 
                + " State " 
                + writeToHtmlByThread.getState().name());
        writeToHtmlByThread.start();
        System.out.println("Write Thread id " 
                + writeToHtmlByThread.getId() 
                +  " thread name " 
                + writeToHtmlByThread.getName() 
                + " State " 
                + writeToHtmlByThread.getState().name());
        waitForPrevJobDoneForWriter();
        System.out.println("Write Thread id " 
                + writeToHtmlByThread.getId() 
                +  " thread name " 
                + writeToHtmlByThread.getName() 
                + " State " 
                + writeToHtmlByThread.getState().name());
        //cleanLogStingQueue();
    }
    protected void cleanLogStingQueue(){
        this.listForRunnableLogStrs.clear();
    }
    protected void waitForPrevJobDoneForWriter(){
        System.out.println("-------|||||||||-----------|||||||||------------make write prev isjobdone " 
                + this.currentJob.isToHTMLJobDone());
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
            this.fileForWrite = this.loggerBus.getNewFileForLogHtml();
            this.currentJob.setToHTMLFileName(this.fileForWrite);
        }
        System.out.println("AppLoggerList.setNewTableFileForLogHtml() for write " + this.fileForWrite.toString());
    }
    protected void doReadFromLogHtmlListOfTables(){
        ArrayBlockingQueue<Path> logHtmlListTableFiles = this.loggerBus.getLogHtmlListTableFiles();
        for(Path elementOfTables : logHtmlListTableFiles){
            waitForPrevJobDoneForReader();
            setNextReadedFileFromLogHtml(elementOfTables);
            addAncorStructure(elementOfTables);
            System.out.println("AppLoggerList.doReadFromLogHtmlListOfTables() for read file " + elementOfTables.toString());
            readListOfTables();
            waitForPrevJobDoneForReader();
        }
        addReadedTablesIntoList();
    }
    protected void addReadedTablesIntoList(){
        ArrayBlockingQueue<ArrayBlockingQueue<String>> readedArrayLinesFromRunner = this.managerForOrder.getStringBusForLogRead();
        for( ArrayBlockingQueue<String> tableItems : readedArrayLinesFromRunner ){
            this.listForRunnableLogStrs.addAll(tableItems);
        }
    }
    protected void addAncorStructure(Path fileForRead){
        String strForAncor = "<p><a name=\"" + fileForRead.getFileName().toString().split("\\.")[0] + "\">"
                        + fileForRead.toString() + "</a></p>";
        this.loggerBus.addStringToRunnableBus(strForAncor);
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
        //this.readedArrayForLines.add(this.managerForOrder.getStringBusForLogRead());
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
    
    
    
}
