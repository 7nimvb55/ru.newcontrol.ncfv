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

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerController {
    private AppLoggerStateWriter currentJob;
    public AppLoggerController(Path logForHtmlCurrentLogSubDir,
            ArrayBlockingQueue<String> outputForWrite) {
        Path pathTable = AppFileOperationsSimple.getNewLogHtmlTableFile(logForHtmlCurrentLogSubDir);
        this.currentJob = AppLoggerInfoToTables.initWriterNewJobLite(outputForWrite, pathTable);
        ThreadGroup newJobThreadGroup = new ThreadGroup(currentJob.getThreadGroupName());
        Thread writeToHtmlByThread = new Thread(newJobThreadGroup, 
                new AppLoggerRunnableWrite(this), 
                currentJob.getThreadName());
        writeToHtmlByThread.start();
    }
    protected AppLoggerStateWriter currentWriterJob(){
        return this.currentJob;
    }
}
