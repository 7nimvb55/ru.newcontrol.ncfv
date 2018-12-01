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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerRule {
    private ArrayBlockingQueue<String> stringForLogHtmlWrite;
    //private ArrayBlockingQueue<String> linesFromLogHtmlRead;
    private ArrayBlockingQueue<ArrayBlockingQueue<String>> readedArrayForLines;
    
    private AppLoggerRunnableHtmlWrite writerToHtmlRunnable;
    private AppLoggerRunnableHtmlRead readerFromHtmlRunnable;
    
    private Boolean isCreatedRunnableWriter;
    private Boolean isCreatedRunnableReader;
    
    private AppLoggerBus logBus;
    private AppLoggerBusJob logJobBus;
    // for compatable new and old versions uncomment
    private AppLoggerStateWriter stateJobForWriteRunner;
    private Boolean isJobForWriter;
    
    private AppLoggerStateReader stateJobForReaderRunner;
    private Boolean isJobForReader;
    private Boolean isReadedJobComplete;
    private Boolean isReadedJobBegin;
    
    private ConcurrentSkipListMap<String, Path> currentLogHTMLStorage;

    public AppLoggerRule(AppLoggerBus outerLoggerBus, AppLoggerBusJob outerLoggerJobBus) {
        this.logBus = outerLoggerBus;
        this.logJobBus = outerLoggerJobBus;
        this.logBus.getCommandsOutPut();
        this.logBus.getListForRunnableLogStrs();
        this.logBus.getLogHtmlStorageList();
        this.stringForLogHtmlWrite = this.logBus.getListForRunnableLogStrs();
        //this.readedArrayForLines = loggerBus.;
        //this.linesFromLogHtmlRead = readedLinesFromLogHtmlBus;
        
        
        // for compatable new and old versions uncomment
        //this.stateJobForRunner = new AppLoggerState();
        //this.stateJobForRunner.setToHTMLFileName(newLogHtmlTableFile);
        setFalseAllReadedJobComplete();
        setFalseAllReadedJobBegin();
        setFalseJobForWriter();
        setFalseJobForReader();
        setFalseCreatedRunnableWriter();
        setFalseCreatedRunnableReader();
    }
    protected void setTrueAllReadedJobBegin(){
        this.isReadedJobBegin = Boolean.TRUE;
    }
    protected void setFalseAllReadedJobBegin(){
        this.isReadedJobBegin = Boolean.FALSE;
    }
    protected Boolean isAllReadedJobBegin(){
        if( this.isReadedJobBegin ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueAllReadedJobComplete(){
        this.isReadedJobComplete = Boolean.TRUE;
    }
    protected void setFalseAllReadedJobComplete(){
        this.isReadedJobComplete = Boolean.FALSE;
    }
    protected Boolean isAllReadedJobComplete(){
        if( this.isReadedJobComplete ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueJobForWriter(){
        this.isJobForWriter = Boolean.TRUE;
    }
    protected void setFalseJobForWriter(){
        this.isJobForWriter = Boolean.FALSE;
    }
    protected Boolean isCurrentWriterJob(){
        if( this.isJobForWriter ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueJobForReader(){
        this.isJobForReader = Boolean.TRUE;
    }
    protected void setFalseJobForReader(){
        this.isJobForReader = Boolean.FALSE;
    }
    protected Boolean isCurrentReaderJob(){
        if( this.isJobForReader ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    
    protected AppLoggerStateWriter currentWriterJob(){
        if( !this.logJobBus.isJobForWriterEmpty() ){
            
            this.stateJobForWriteRunner = this.logJobBus.getInitedForWriter();
            if( !this.stateJobForWriteRunner.isToHTMLJobDone() ){
                setTrueJobForWriter();
            }
        } else {
            if( !this.stateJobForWriteRunner.isToHTMLJobDone() ){
                return this.stateJobForWriteRunner;
            }
        }
        setFalseJobForWriter();
        return new AppLoggerStateWriter("HaventJobForRun-AppLoggerRule.getCurrentJob");
    }
    protected AppLoggerStateReader currentReaderJob(){
        System.out.println("                           AppLoggerRule.currentReaderJob()  jobBus.getCountJobForReader  " 
                + this.logJobBus.getCountJobForReader()
                + "     jobBus.isJobForReaderEmpty()      "
                + this.logJobBus.isJobForReaderEmpty()
        );
        if( this.isJobForReader ){
            System.out.println("          AppLoggerRule.isJobForReader " 
                    + this.isJobForReader 
                    + "              " 
                    
                            );
        }
        if( !this.logJobBus.isJobForReaderEmpty() ){
            
            this.stateJobForReaderRunner = this.logJobBus.getInitedForReader();
            if( !this.stateJobForReaderRunner.isFromHTMLJobDone() ){
                setTrueJobForReader();
                return this.stateJobForReaderRunner;
            }
        } /*else {
            if( !this.stateJobForReaderRunner.isFromHTMLJobDone() ){
                return this.stateJobForReaderRunner;
            }
        }*/
        setFalseJobForReader();
        return new AppLoggerStateReader("HaventJobForRun-AppLoggerRule.getCurrentJob");
    }
    
    protected AppLoggerStateReader initReaderNewJob(
            String newJobThreadGroupName,
            String writeToHtmlByThreadName,
            Path fileNameForRead
    ){
        AppLoggerStateReader createNewReaderJob = AppLoggerRuleHelper.createNewReaderJob();
        createNewReaderJob.setTrueInitStartRead();
        createNewReaderJob.setThreadGroupName(newJobThreadGroupName);
        createNewReaderJob.setThreadName(writeToHtmlByThreadName);
        createNewReaderJob.setFromHTMLFileName(fileNameForRead);
        createNewReaderJob.setTrueInitEndRead();
        return createNewReaderJob;
    }
    protected AppLoggerStateWriter initWriterNewJob(
            ArrayBlockingQueue<String> partLinesForWrite,
            String newJobThreadGroupName,
            String writeToHtmlByThreadName,
            Path fileNameForWrite
    ){
        AppLoggerStateWriter createNewWriterJob = AppLoggerRuleHelper.createNewWriterJob();
        createNewWriterJob.setTrueInitStartWrite();
        createNewWriterJob.setPartLinesForWrite(partLinesForWrite);
        createNewWriterJob.setThreadGroupName(newJobThreadGroupName);
        createNewWriterJob.setThreadName(writeToHtmlByThreadName);
        createNewWriterJob.setToHTMLFileName(fileNameForWrite);
        createNewWriterJob.setTrueInitEndWrite();
        return createNewWriterJob;
    }
    protected ArrayBlockingQueue<String> getStringBusForLogWrite(){
        return this.stringForLogHtmlWrite;
    }
    protected ArrayBlockingQueue<ArrayBlockingQueue<String>> getStringBusForLogRead(){
        return this.readedArrayForLines;
        //return this.linesFromLogHtmlRead;
    }
    protected void setStringBusForLogWrite(ArrayBlockingQueue<String> outerListForLogStrs){
        this.stringForLogHtmlWrite = outerListForLogStrs;
    }
    protected void setStringBusForLogRead(ArrayBlockingQueue<String> readedLinesFromReaderRunner){
        this.readedArrayForLines.add(AppObjectsBusHelper.cleanBusForRunnables(readedLinesFromReaderRunner));
        //this.linesFromLogHtmlRead = AppObjectsBusHelper.cleanBusForRunnables(readedLinesFromLogHtmlBus);
    }
    
    protected void setFalseCreatedRunnableWriter(){
        this.isCreatedRunnableWriter = Boolean.FALSE;
    }
    protected void setTrueCreatedRunnableWriter(){
        this.isCreatedRunnableWriter = Boolean.TRUE;
    }
    protected Boolean isCreatedRunnableWriter(){
        return this.isCreatedRunnableWriter;
    }
    
    protected AppLoggerRunnableHtmlWrite getRunnableWriter(){
        if ( !isCreatedRunnableWriter() ){
            this.writerToHtmlRunnable = new AppLoggerRunnableHtmlWrite(this);
            setTrueCreatedRunnableWriter();
        }
        return this.writerToHtmlRunnable;
    }
    
    protected void setFalseCreatedRunnableReader(){
        this.isCreatedRunnableReader = Boolean.FALSE;
    }
    protected void setTrueCreatedRunnableReader(){
        this.isCreatedRunnableReader = Boolean.TRUE;
    }
    protected Boolean isCreatedRunnableReader(){
        return this.isCreatedRunnableReader;
    }
    
    protected AppLoggerRunnableHtmlRead getRunnableReader(){
        if ( !isCreatedRunnableReader() ){
            this.readerFromHtmlRunnable = new AppLoggerRunnableHtmlRead(this);
            setTrueCreatedRunnableReader();
        }
        return this.readerFromHtmlRunnable;
    }
    
    
}
