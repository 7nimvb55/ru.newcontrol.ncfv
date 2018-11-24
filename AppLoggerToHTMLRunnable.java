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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerToHTMLRunnable implements Runnable {
    
    private ArrayBlockingQueue<String> listForLogStrings;
    private Path logFile;
    private Boolean logFileChanged;
    private Boolean jobIsDone;
    private Boolean isNewRunner;

    public AppLoggerToHTMLRunnable(
            ArrayBlockingQueue<String> listForLogStrs, 
            Path outerLogFile) {
        super();
        this.isNewRunner = Boolean.TRUE;
        this.logFileChanged = Boolean.FALSE;
        this.jobIsDone = Boolean.FALSE;
        this.listForLogStrings = listForLogStrs;
        this.logFile = outerLogFile;
        String threadInfoToString = NcAppHelper.getThreadInfoToString(Thread.currentThread());
        System.out.println("create logger StrArrOutToHtml " + threadInfoToString);
    }
    
    @Override
    public void run() {
        ArrayList<String> forRecord = new ArrayList<String>();
        String poll;
        do{
            poll = this.listForLogStrings.poll();
            forRecord.add(poll);
        }while( !this.listForLogStrings.isEmpty() );
        
        
        this.jobIsDone = Boolean.FALSE;
        try {
            Files.write(this.logFile, forRecord, Charset.forName("UTF-8"));
            this.logFileChanged = Boolean.FALSE;
        } catch (IOException ex) {
            ex.getMessage();
            ex.printStackTrace();
        }
        this.jobIsDone = Boolean.TRUE;
        this.isNewRunner = Boolean.FALSE;
    }
    protected void setNewLogFileName(Path newLogFileName){
        this.logFile = newLogFileName;
        this.logFileChanged = Boolean.TRUE;
    }
    protected Boolean isLogFileNameChanged(){
        return this.logFileChanged;
    }
    protected Path getLogFileName(){
        return this.logFile;
    }
    protected Boolean isJobDone(){
        return this.jobIsDone;
    }
    protected Boolean isNewRunner(){
        return this.isNewRunner;
    }
}
