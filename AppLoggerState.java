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

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerState {
    private Path toHTMLlogFile;
    private Boolean toHtmlFileNameSet;
    private Boolean toHTMLlogFileChanged;
    private Boolean toHTMLjobIsDone;
    private Boolean toHTMLisNewRunner;
    
    private Path fromHTMLlogFile;
    private Boolean fromHtmlFileNameSet;
    private Boolean fromHTMLlogFileChanged;
    private Boolean fromHTMLjobIsDone;
    private Boolean fromHTMLisNewRunner;

    public AppLoggerState() {
        setFalseToHTMLLogFileNameChanged();
        setFalseToHTMLJobDone();
        setTrueToHTMLNewRunner();
        
        setFalseFromHTMLLogFileNameChanged();
        setFalseFromHTMLJobDone();
        setTrueFromHTMLNewRunner();
    }
    
    protected Boolean isFromHtmlFileNameSet(){
        return this.fromHtmlFileNameSet;
    }
    
    protected Boolean isToHtmlFileNameSet(){
        return this.toHtmlFileNameSet;
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
    }
    protected Path getToHTMLLogFileName(){
        return this.toHTMLlogFile;
    }
    
    
    protected Boolean isToHTMLNewRunner(){
        return this.toHTMLisNewRunner;
    }
    protected Boolean isToHTMLJobDone(){
        return this.toHTMLjobIsDone;
    }
    protected Boolean isToHTMLLogFileNameChanged(){
        return this.toHTMLlogFileChanged;
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
        return this.fromHTMLlogFileChanged;
    }
    protected Path getFromHTMLLogFileName(){
        return this.fromHTMLlogFile;
    }
    protected Boolean isFromHTMLJobDone(){
        return this.fromHTMLjobIsDone;
    }
    protected Boolean isFromHTMLNewRunner(){
        return this.fromHTMLisNewRunner;
    }
    
}
