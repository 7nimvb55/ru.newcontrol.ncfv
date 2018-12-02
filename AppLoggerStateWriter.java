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
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerStateWriter {
    private Path toHTMLlogFile;
    private Boolean toHtmlFileNameSet;
    private Boolean toHTMLlogFileChanged;
    private Boolean toHTMLjobInitStart;
    private long initStartNanoTime;
    //@todo work place start time
    private Boolean toHTMLjobInitEnd;
    private long initEndNanoTime;
    //@todo work place end time
    private Boolean toHTMLjobWrokPalceInit;
    //@todo work place param: thread, thread group, thread id, thread name
    private Boolean toHTMLjobIsDone;
    private Boolean toHTMLisNewRunner;
    private final UUID randomUUID;
    private final long CreationNanoTime;
    private ArrayBlockingQueue<String> partLinesForWrite;
    private Boolean isSetPartLinesForWrite;
    private String newJobThreadGroupName;
    private String newJobThreadName;
    private Boolean fromHtmlBlankJob;

    public AppLoggerStateWriter() {
        setFalsePartLinesForWrite();
        randomUUID = UUID.randomUUID();
        CreationNanoTime = System.nanoTime();
        setFalseToHTMLLogFileNameChanged();
        setFalseToHtmlFileNameSet();
        setFalseToHTMLJobDone();
        setFalseToHTMLNewRunner();
        setFalseInitStartWrite();
        setFalseInitEndWrite();
        setFalseBlankObject();
    }
    public AppLoggerStateWriter(String strForEmptyWork) {
        setFalsePartLinesForWrite();
        randomUUID = UUID.randomUUID();
        CreationNanoTime = System.nanoTime();
        setFalseToHTMLLogFileNameChanged();
        setFalseToHtmlFileNameSet();
        setThreadName(strForEmptyWork);
        setThreadName(strForEmptyWork);
        setTrueToHTMLJobDone();
        setFalseToHTMLNewRunner();
        setFalseInitStartWrite();
        setFalseInitEndWrite();
        setTrueBlankObject();
    }
    protected Boolean isInitStartWrite(){
        if( this.toHTMLjobInitStart ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    protected void setTrueInitStartWrite(){
        this.initStartNanoTime = System.nanoTime();
        this.toHTMLjobInitStart = Boolean.TRUE;
    }
    protected void setFalseInitStartWrite(){
        this.toHTMLjobInitStart = Boolean.FALSE;
    }
    protected Boolean isEndStartWrite(){
        if( this.toHTMLjobInitEnd ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    protected void setTrueInitEndWrite(){
        this.initEndNanoTime = System.nanoTime();
        this.toHTMLjobInitEnd = Boolean.TRUE;
    }
    protected void setFalseInitEndWrite(){
        this.toHTMLjobInitEnd = Boolean.FALSE;
    }
    
    protected Boolean isBlankObject(){
        if( this.fromHtmlBlankJob){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    protected void setTrueBlankObject(){
        this.fromHtmlBlankJob = Boolean.TRUE;
    }
    protected void setFalseBlankObject(){
        this.fromHtmlBlankJob = Boolean.FALSE;
    }
    
    protected void setPartLinesForWrite(ArrayBlockingQueue<String> outerPartLinesForWrite){
        this.partLinesForWrite = outerPartLinesForWrite;
        setTruePartLinesForWrite();
    }
    protected ArrayBlockingQueue<String> getPartLinesForWrite(){
        if( isPartLinesForWrite() ){
            return this.partLinesForWrite;
        }
        return new ArrayBlockingQueue<String>(1);
    }
    protected void setThreadGroupName(String outerThreadGroupName){
        this.newJobThreadGroupName = outerThreadGroupName;
    }
    protected String getThreadGroupName(){
        return this.newJobThreadGroupName;
    }
            
    protected void setThreadName(String outerThreadName){
        this.newJobThreadName = outerThreadName;
    }
    protected String getThreadName(){
        return this.newJobThreadName;
    }
    protected UUID getID(){
        return this.randomUUID;
    }
    protected long getCreationTime(){
        return this.CreationNanoTime;
    }
    
    protected Boolean isPartLinesForWrite(){
        if( this.isSetPartLinesForWrite ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    protected void setTruePartLinesForWrite(){
        this.isSetPartLinesForWrite = Boolean.TRUE;
    }
    protected void setFalsePartLinesForWrite(){
        this.isSetPartLinesForWrite = Boolean.FALSE;
    }
    
    protected Boolean isToHtmlFileNameSet(){
        if( this.toHtmlFileNameSet ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    protected void setTrueToHtmlFileNameSet(){
        this.toHtmlFileNameSet = Boolean.TRUE;
    }
    protected void setFalseToHtmlFileNameSet(){
        this.toHtmlFileNameSet = Boolean.FALSE;
    }
    
    protected void setTrueToHTMLNewRunner(){
        this.toHTMLisNewRunner = Boolean.TRUE;
    }
    protected void setTrueToHTMLJobDone(){
        this.toHTMLjobIsDone = Boolean.TRUE;
    }
    protected void setTrueToHTMLLogFileNameChanged(){
        this.toHTMLlogFileChanged = Boolean.TRUE;
    }
    protected void setFalseToHTMLNewRunner(){
        this.toHTMLisNewRunner = Boolean.FALSE;
    }
    protected void setFalseToHTMLJobDone(){
        this.toHTMLjobIsDone = Boolean.FALSE;
    }
    protected void setFalseToHTMLLogFileNameChanged(){
        this.toHTMLlogFileChanged = Boolean.FALSE;
    }
    protected void setToHTMLFileName(Path newLogFileName){
        this.toHTMLlogFile = newLogFileName;
        setTrueToHTMLLogFileNameChanged();
        setTrueToHtmlFileNameSet();
    }
    protected Path getToHTMLLogFileName(){
        return Paths.get(this.toHTMLlogFile.toString());
    }
    
    
    protected Boolean isToHTMLNewRunner(){
        if( this.toHTMLisNewRunner ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected Boolean isToHTMLJobDone(){
        if( this.toHTMLjobIsDone ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected Boolean isToHTMLLogFileNameChanged(){
        if( this.toHTMLlogFileChanged ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
}
