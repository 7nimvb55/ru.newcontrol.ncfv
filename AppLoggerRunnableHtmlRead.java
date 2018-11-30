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

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerRunnableHtmlRead implements Runnable {
    
    private AppLoggerRule managerForThis;
    public AppLoggerRunnableHtmlRead(AppLoggerRule outerManagerForThis){
        super();
    this.managerForThis = outerManagerForThis;
    this.managerForThis.getCurrentJob().setTrueFromHTMLNewRunner();
        String threadInfoToString = NcAppHelper.getThreadInfoToString(Thread.currentThread());
        System.out.println("*** ||| *** ||| *** create log reader *** ||| *** ||| ***" + threadInfoToString);
    }
    
    @Override
    public void run() {
        AppLoggerState currentJob = this.managerForThis.getCurrentJob();
        Path fromHTMLLogFileName = this.managerForThis.getCurrentJob().getFromHTMLLogFileName();
        currentJob.setFalseFromHTMLJobDone();
        System.out.println("_|_|_|_|_|_ AppLoggerRunnableHtmlRead.run() fromHTMLLogFileName " 
                        + fromHTMLLogFileName.toString() 
                        + " _|_|_|_|_|_"
                        + " start for read file");
        ArrayBlockingQueue<String> readedLines = new ArrayBlockingQueue<String>(1000);
        try {
            readedLines.addAll(Files.readAllLines(fromHTMLLogFileName, Charset.forName("UTF-8")));
            if( readedLines != null){
                System.out.println("_|_|_|_|_|_ AppLoggerRunnableHtmlRead.run() fromHTMLLogFileName " 
                        + fromHTMLLogFileName.toString() 
                        + " _|_|_|_|_|_"
                        + " readedLines.size() " + readedLines.size());
                this.managerForThis.setStringBusForLogRead(readedLines);
            } else {
                System.out.println("_|_|_|_|_|_ AppLoggerRunnableHtmlRead.run() fromHTMLLogFileName " 
                        + fromHTMLLogFileName.toString() 
                        + " _|_|_|_|_|_"
                        + " readedLines.size() is null");
                this.managerForThis.setStringBusForLogRead(new ArrayBlockingQueue<String>(1));
            }
            
            currentJob.setFalseFromHTMLLogFileNameChanged();
        } catch (IOException ex) {
            ex.getMessage();
            ex.printStackTrace();
        }
        currentJob.setTrueFromHTMLJobDone();
        currentJob.setFalseFromHTMLNewRunner();
    }

}