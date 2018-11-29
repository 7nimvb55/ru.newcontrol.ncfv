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
    private ArrayBlockingQueue<String> linesFromLogHtmlRead;
    
    private AppLoggerRunnableHtmlWrite writerToHtmlRunnable;
    private AppLoggerRunnableHtmlRead readerFromHtmlRunnable;
    
    private Boolean isCreatedRunnableWriter;
    private Boolean isCreatedRunnableReader;
    
    private AppLoggerState stateJobForRunner;
    private ConcurrentSkipListMap<String, Path> currentLogHTMLStorage;

    public AppLoggerRule(ArrayBlockingQueue<String> outerListForLogStrs,
            ArrayBlockingQueue<String> readedLinesFromLogHtmlBus,
            ConcurrentSkipListMap<String, Path> storageForLogHtml) {
        this.stringForLogHtmlWrite = outerListForLogStrs;
        this.linesFromLogHtmlRead = readedLinesFromLogHtmlBus;
        Path newLogHtmlTableFile = storageForLogHtml.get(AppFileNamesConstants.LOG_HTML_TABLE_PREFIX);
        
        this.stateJobForRunner = new AppLoggerState();
        this.stateJobForRunner.setToHTMLFileName(newLogHtmlTableFile);
        setFalseCreatedRunnableWriter();
        setFalseCreatedRunnableReader();
    }
    protected AppLoggerState getCurrentJob(){
        return this.stateJobForRunner;
    }
    protected ArrayBlockingQueue<String> getStringBusForLogWrite(){
        return this.stringForLogHtmlWrite;
    }
    protected ArrayBlockingQueue<String> getStringBusForLogRead(){
        return this.linesFromLogHtmlRead;
    }
    protected void setStringBusForLogWrite(ArrayBlockingQueue<String> outerListForLogStrs){
        this.stringForLogHtmlWrite = outerListForLogStrs;
    }
    protected void setStringBusForLogRead(ArrayBlockingQueue<String> readedLinesFromLogHtmlBus){
        this.linesFromLogHtmlRead = readedLinesFromLogHtmlBus;
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
