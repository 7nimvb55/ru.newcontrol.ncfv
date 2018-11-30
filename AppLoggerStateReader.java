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
public class AppLoggerStateReader {
    private Path fromHTMLlogFile;
    private Boolean fromHtmlFileNameSet;
    private Boolean fromHTMLlogFileChanged;
    private Boolean fromHTMLjobInitStart;
    private long initStartNanoTime;
    //@todo work place start time
    private Boolean fromHTMLjobInitEnd;
    private long initEndNanoTime;
    //@todo work place end time
    private Boolean fromHTMLjobWrokPalceInit;
    //@todo work place param: thread, thread, group, thread id, thread name
    
    
    private Boolean fromHTMLjobIsDone;
    private Boolean fromHTMLisNewRunner;
    private final UUID randomUUID;
    private final long CreationNanoTime;
    private String newJobThreadGroupName;
    private String newJobThreadName;
    

    public AppLoggerStateReader() {
        
        randomUUID = UUID.randomUUID();
        CreationNanoTime = System.nanoTime();
        setFalseFromHTMLLogFileNameChanged();
        setFalseFromHTMLJobDone();
        setFalseFromHTMLNewRunner();
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
    
    protected Boolean isFromHtmlFileNameSet(){
        if( this.fromHtmlFileNameSet ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected void setTrueFromHtmlFileNameSet(){
        this.fromHtmlFileNameSet = Boolean.TRUE;
    }
    
    
    protected void setTrueFromHTMLNewRunner(){
        this.fromHTMLisNewRunner = Boolean.TRUE;
    }
    protected void setTrueFromHTMLJobDone(){
        this.fromHTMLjobIsDone = Boolean.TRUE;
    }
    protected void setTrueFromHTMLLogFileNameChanged(){
        this.fromHTMLlogFileChanged = Boolean.TRUE;
    }
    
    protected void setFalseFromHtmlFileNameSet(){
        this.fromHtmlFileNameSet = Boolean.FALSE;
    }
    protected void setFalseFromHTMLNewRunner(){
        this.fromHTMLisNewRunner = Boolean.FALSE;
    }
    protected void setFalseFromHTMLJobDone(){
        this.fromHTMLjobIsDone = Boolean.FALSE;
    }
    protected void setFalseFromHTMLLogFileNameChanged(){
        this.fromHTMLlogFileChanged = Boolean.FALSE;
    }
    
    protected void setFromHTMLFileName(Path newLogFileName){
        this.fromHTMLlogFile = newLogFileName;
        setTrueFromHTMLLogFileNameChanged();
    }
    
    protected Boolean isFromHTMLLogFileNameChanged(){
        if( this.fromHTMLlogFileChanged ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected Path getFromHTMLLogFileName(){
        return Paths.get(this.fromHTMLlogFile.toString());
    }
    protected Boolean isFromHTMLJobDone(){
        if( this.fromHTMLjobIsDone ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    protected Boolean isFromHTMLNewRunner(){
        if( this.fromHTMLisNewRunner ){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
