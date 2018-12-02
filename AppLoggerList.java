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
    
    private Integer countJobForReader;
    private Integer countDoneJobForReader;
    
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
        this.countDoneJobForReader = 0;
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
        //this.currentJob = this.managerForOrder.getCurrentJob();
    }
    protected AppLoggerBus getLoggerBus(){
        return this.loggerBus;
    }
    protected void doWriteToLogHtmlCurrentFile(){
        if( isNeedForSaveIndexHtml() ){
            if( this.managerForOrder.isAllReadedJobBegin() ){
                System.out.println("----------------------------------------AppLoggerBusJob.getCountJobForReader() " 
                        + this.loggerJobBus.getCountJobForReader()
                );
                System.out.println("+|+|+|+ countJobForReader "
                        + this.countJobForReader
                        + " countDoneJobForReader "
                        + this.countDoneJobForReader
                );
                System.out.println("+|+|+|+AppLoggerBus.countOfArrayBusForHtmlRead() "
                        + this.loggerBus.countOfArrayBusForHtmlRead()
                );
                setNewIndexFileForLogHtml();
            }
        }
        else if( isNeedForSaveCss() ){
            setNewCssFileForLogHtml();
        }
        else if( isNeedForSaveJs() ){
            setNewJsFileForLogHtml();
        }
        else if( !isNewLoggerList() ){
            this.fileForWrite = this.loggerBus.getNewFileForLogHtml();
        } else {
            this.fileForWrite = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_TABLE_PREFIX);
        }
        setFalseNewLoggerList();
        makeWriteJob();
    }
    protected void makeWriteJob(){
        //this.managerForOrder.setStringBusForLogWrite(AppObjectsBusHelper.cleanBusForRunnables(this.listForRunnableLogStrs));
        System.out.println("-------|||||||||-----------|||||||||------------AppLoggerList.makeWrite for " 
                + this.managerForOrder.getStringBusForLogWrite().size());
        System.out.println("-------|||||||||-----------|||||||||------------AppLoggerList.makeWrite to " 
                + this.fileForWrite.toString());

        String nowTimeStringWithMS = 
                    AppFileOperationsSimple.getNowTimeStringWithMS();
        String nameJobThreadGroup = "WriterGroup-" + nowTimeStringWithMS;
        
        String nameJobThread = "writerToHtml-" + nowTimeStringWithMS;
        AppLoggerStateWriter writerNewJob = this.managerForOrder.initWriterNewJob(
                AppObjectsBusHelper.cleanBusForRunnables(this.listForRunnableLogStrs),
                nameJobThreadGroup,
                nameJobThread,
                this.fileForWrite
        );
        this.loggerJobBus.addStateJobForWriterRunnableBus(writerNewJob);
        
        waitForPrevJobDoneForWriter();
        
        AppLoggerStateWriter currentWriterJob = this.managerForOrder.currentWriterJob();
        
        ThreadGroup newJobThreadGroup = new ThreadGroup(currentWriterJob.getThreadGroupName());
        Thread writeToHtmlByThread = new Thread(newJobThreadGroup, 
                this.managerForOrder.getRunnableWriter(), 
                currentWriterJob.getThreadName());
        
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
        
        AppLoggerStateWriter currentWriterJob = this.managerForOrder.currentWriterJob();
        System.out.println("-------|||||||||-----------|||||||||------------make write prev isjobdone " 
                + currentWriterJob.isToHTMLJobDone());
        //if( !this.currentJob.isToHTMLNewRunner() ){
        try{
            System.out.println("wait for prev done");
            while( !currentWriterJob.isToHTMLJobDone() ){
                Thread curThr = Thread.currentThread();
                curThr.sleep(50);
        }
             System.out.println(" end wait for prev done");
        } catch(InterruptedException ex){
            ex.printStackTrace();
        } catch(SecurityException ex){
            ex.printStackTrace();
        }
        //}
    }
    protected void waitForPrevJobDoneForReader(){
        //if( !this.currentJob.isFromHTMLNewRunner() ){
        AppLoggerStateReader currentReaderJob = this.managerForOrder.currentReaderJob();
        if( !currentReaderJob.isBlankObject() ){
            try{
                while( !currentReaderJob.isFromHTMLJobDone() ){
                    Thread curThr = Thread.currentThread();
                    curThr.sleep(50);
                }
            } catch(InterruptedException ex){
                ex.printStackTrace();
            } catch(SecurityException ex){
                ex.printStackTrace();
            }
            this.countDoneJobForReader++;
        }
    }
    protected void setNewCssFileForLogHtml(){
        this.fileForWrite = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_CSS_PREFIX);
        //this.currentJob.setToHTMLFileName(this.fileForWrite);
    }
    protected void setNewJsFileForLogHtml(){
        this.fileForWrite = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_JS_MENU_PREFIX);
        //this.currentJob.setToHTMLFileName(this.fileForWrite);
    }
    protected void setNewIndexFileForLogHtml(){
        this.fileForWrite = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_INDEX_PREFIX);
        //this.currentJob.setToHTMLFileName(this.fileForWrite);
    }
    protected void doReadFromLogHtmlListOfTables(){
        
        ArrayBlockingQueue<Path> logHtmlListTableFiles = this.loggerBus.getLogHtmlListTableFiles();
        this.countJobForReader = logHtmlListTableFiles.size();
        for(Path elementOfTables : logHtmlListTableFiles){
            //waitForPrevJobDoneForReader();
            //setNextReadedFileFromLogHtml(elementOfTables);
            
            String nowTimeStringWithMS = 
                    AppFileOperationsSimple.getNowTimeStringWithMS();
            String nameJobThreadGroup = "ReaderGroup-" + nowTimeStringWithMS;

            String nameJobThread = "readerFromHtml-" + nowTimeStringWithMS;
            AppLoggerStateReader readerNewJob = this.managerForOrder.initReaderNewJob(
                nameJobThreadGroup,
                nameJobThread,
                elementOfTables
            );
            if( !readerNewJob.isAncorStructure() ){
                readerNewJob.setAncorString(getAncorStructure(elementOfTables));
            }
            this.loggerJobBus.addStateJobForReaderRunnableBus(readerNewJob);
            this.managerForOrder.setTrueAllReadedJobBegin();
            
            System.out.println(
                    "this.managerForOrder.isAllReadedJobBegin() "
                    + this.managerForOrder.isAllReadedJobBegin()
                    + " AppLoggerBusJob.getCountJobForReader() "
                    + this.loggerJobBus.getCountJobForReader()
                    + " AppLoggerList.doReadFromLogHtmlListOfTables() for read file " 
                    + elementOfTables.toString());
            
            readListOfTables();
            
            //waitForPrevJobDoneForReader();
        }
        //addReadedTablesIntoList();
    }
    protected void addReadedTablesIntoList(){
        ArrayBlockingQueue<ArrayBlockingQueue<String>> readedArrayLinesFromRunner = this.managerForOrder.getStringBusForLogRead();
        for( ArrayBlockingQueue<String> tableItems : readedArrayLinesFromRunner ){
            this.listForRunnableLogStrs.addAll(tableItems);
        }
    }
    protected static String getAncorStructure(Path fileForRead){
        return "<p><a name=\"" + fileForRead.getFileName().toString().split("\\.")[0] + "\">"
                        + fileForRead.toString() + "</a></p>";
    }
    protected void readListOfTables(){
        AppLoggerStateReader currentReaderJob = this.managerForOrder.currentReaderJob();
        if( !currentReaderJob.isBlankObject() ){
            System.out.println(
                        " +|+|+|+|+|+   AppLoggerList.readListOfTables() this.managerForOrder.isAllReadedJobBegin() "
                        + this.managerForOrder.isAllReadedJobBegin()
                        + " AppLoggerBusJob.getCountJobForReader() "
                        + this.loggerJobBus.getCountJobForReader()
                        + " currentReaderJob.getFromHTMLLogFileName() "
                        + currentReaderJob.getFromHTMLLogFileName());
            ThreadGroup newJobThreadGroup = new ThreadGroup(currentReaderJob.getThreadGroupName());
            Thread readFromHtmlByThread = new Thread(newJobThreadGroup, 
                    this.managerForOrder.getRunnableReader(), 
                    currentReaderJob.getThreadName());
            //this.managerForOrder.setStringBusForLogRead(this.readedLinesFromTablesWork);
            //ThreadGroup newJobThreadGroup = new ThreadGroup("ReaderGroup-" + nowTimeStringWithMS);
            //Thread readFromHtmlByThread = new Thread(newJobThreadGroup, this.managerForOrder.getRunnableReader(), "readToHtml-" + nowTimeStringWithMS);
            System.out.println("-----Reader Thread id " + readFromHtmlByThread.getId() +  " thread name " + readFromHtmlByThread.getName() + " State " + readFromHtmlByThread.getState().name() + "-----Reader");
            readFromHtmlByThread.start();
            System.out.println("-----Reader Thread id " + readFromHtmlByThread.getId() +  " thread name " + readFromHtmlByThread.getName() + " State " + readFromHtmlByThread.getState().name() + "-----Reader");
            waitForPrevJobDoneForReader();
            System.out.println("-----Reader Thread id " + readFromHtmlByThread.getId() +  " thread name " + readFromHtmlByThread.getName() + " State " + readFromHtmlByThread.getState().name() + "-----Reader");
            System.out.println("currentReaderJob.isFromHTMLJobDone() " + currentReaderJob.isFromHTMLJobDone());
        }
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
