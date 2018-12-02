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
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerController {
    private AppLoggerStateWriter currentWriterJob;
    private AppLoggerStateReader currentReaderJob;
    private ArrayBlockingQueue<ArrayBlockingQueue<String>> readedArrayForLines;
    public AppLoggerController(ArrayBlockingQueue<ArrayBlockingQueue<String>> outerReadBus,
            AppLoggerStateReader newJob) {
        this.readedArrayForLines = outerReadBus;
        this.currentReaderJob = newJob;
        this.currentWriterJob = new AppLoggerStateWriter("blankWriter-" + UUID.randomUUID().toString());
        
    }
    public AppLoggerController(AppLoggerStateWriter newJob) {
        this.currentWriterJob = newJob;
        this.currentReaderJob = new AppLoggerStateReader("blankReader-" + UUID.randomUUID().toString());
    }
    public AppLoggerController(Path logForHtmlCurrentLogSubDir,
            ArrayBlockingQueue<String> outputForWrite) {
        Path pathTable = AppFileOperationsSimple.getNewLogHtmlTableFile(logForHtmlCurrentLogSubDir);
        this.currentWriterJob = AppLoggerInfoToTables.initWriterNewJobLite(outputForWrite, pathTable);
        ThreadGroup newJobThreadGroup = new ThreadGroup(currentWriterJob.getThreadGroupName());
        Thread writeToHtmlByThread = new Thread(newJobThreadGroup, 
                new AppLoggerRunnableWrite(this), 
                currentWriterJob.getThreadName());
        writeToHtmlByThread.start();
    }
    protected AppLoggerStateWriter currentWriterJob(){
        return this.currentWriterJob;
    }
    protected AppLoggerStateReader currentReaderJob(){
        return this.currentReaderJob;
    }
    protected void setStringBusForLogRead(ArrayBlockingQueue<String> readedFormHtmlLines){
        ArrayBlockingQueue<String> fromReadFile = new ArrayBlockingQueue<String>(readedFormHtmlLines.size());
        String pollFirstForSave = "";
        do{
            pollFirstForSave = readedFormHtmlLines.poll();
            if( pollFirstForSave != null ){
                fromReadFile.add(new String(pollFirstForSave));
            }
        }while( !readedFormHtmlLines.isEmpty() );
        this.readedArrayForLines.add(fromReadFile);
    }
}
