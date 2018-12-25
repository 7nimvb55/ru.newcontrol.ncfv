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
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerRunnableRead implements Runnable {
    
    private AppLoggerController busManager;
    
    public AppLoggerRunnableRead(AppLoggerController outerManagerForThis){
        super();
        
        this.busManager = outerManagerForThis;
        this.busManager.currentReaderJob().setTrueFromHTMLNewRunner();
        //this.managerForThis..setTrueFromHTMLNewRunner();
        String threadInfoToString = NcAppHelper.getThreadInfoToString(Thread.currentThread());
        String outCreate = "*** ||| *** ||| *** create log reader *** ||| *** ||| ***" + threadInfoToString;
        NcAppHelper.outToConsoleIfDevAndParamTrue(outCreate, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_RUNNABLE_CREATE);
        
    }
    
    @Override
    public void run() {
        try{
            AppLoggerController managerForThis = this.busManager;
            if( managerForThis != null ){
                AppLoggerStateReader currentJob = managerForThis.currentReaderJob();
                if( !currentJob.isBlankObject() ){
                    if( !currentJob.isFromHTMLJobDone() ){
                        Path fileForReadInThisJob = currentJob.getFromHTMLLogFileName();
                        //currentJob.setFalseFromHTMLJobDone();
                        String outRunJob = "_|_|_|_|_|_ AppLoggerRunnableHtmlRead.run() fromHTMLLogFileName " 
                                        + fileForReadInThisJob.toString() 
                                        + " _|_|_|_|_|_"
                                        + " start for read file";
                        NcAppHelper.outToConsoleIfDevAndParamTrue(outRunJob, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_RUNNABLE_RUN_JOB);
                        ArrayBlockingQueue<String> readedLines = new ArrayBlockingQueue<String>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
                        //readedLines.add(fileForReadInThisJob.toString());
                        String ancorString = currentJob.getAncorString();
                        if( ancorString.length() > 17 ){
                            readedLines.add(ancorString);
                        }
                        //try {
                            readedLines.addAll(AppFileOperationsSimple.readFromFile(fileForReadInThisJob));
                            //readedLines.addAll(Files.readAllLines(fileForReadInThisJob, Charset.forName("UTF-8")));
                            if( readedLines != null){
                                String outJobReadEnd = "_|_|_|_|_|_ AppLoggerRunnableHtmlRead.run() fromHTMLLogFileName " 
                                        + fileForReadInThisJob.toString() 
                                        + " _|_|_|_|_|_"
                                        + " readedLines.size() " + readedLines.size();
                                NcAppHelper.outToConsoleIfDevAndParamTrue(outJobReadEnd, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_RUNNABLE_JOB_END_READ);
                                managerForThis.setStringBusForLogRead(currentJob.getID(), readedLines);
                            } else {
                                String outJobReadNull = "_|_|NULL|_|_ AppLoggerRunnableHtmlRead.run() fromHTMLLogFileName " 
                                        + fileForReadInThisJob.toString() 
                                        + " _|_|NULL|_|_"
                                        + " readedLines.size() is null";
                                NcAppHelper.outToConsoleIfDevAndParamTrue(outJobReadNull, AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_RUNNABLE_JOB_READ_NULL);
                                managerForThis.setStringBusForLogRead(currentJob.getID(), new ArrayBlockingQueue<String>(1));
                            }

                            currentJob.setFalseFromHTMLLogFileNameChanged();
                        /*} catch (IOException ex) {
                            ex.getMessage();
                            ex.printStackTrace();
                        }*/
                    }
                
                }
                currentJob.setTrueFromHTMLJobDone();
                currentJob.setFalseFromHTMLNewRunner();
            }
        } finally {
            
        }
    }
    
}
