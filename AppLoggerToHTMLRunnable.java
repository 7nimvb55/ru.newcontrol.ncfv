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
public class AppLoggerToHTMLRunnable implements Runnable {
    private ArrayBlockingQueue<String> messagesQueueForLogging;
    private Integer linesCount;
    private Path newLogFile;

    public AppLoggerToHTMLRunnable(ArrayBlockingQueue<String> messagesQueueOuter, Path outerLogFile) {
        super();
        messagesQueueForLogging = messagesQueueOuter;
        linesCount = AppConstants.LOG_LINES_COUNT;
        newLogFile = outerLogFile;
        String threadInfoToString = NcAppHelper.getThreadInfoToString(Thread.currentThread());
        System.out.println("create logger StrArrOutToHtml " + threadInfoToString);
    }
    
    @Override
    public void run() {

            //@todo rebuild for limit str count in files, add log rotate in arch folder, create zip for old logs
            ArrayList<String> strForLog = new ArrayList<String>();
            while ( !messagesQueueForLogging.isEmpty() ) {            
                strForLog.add(messagesQueueForLogging.poll());
                linesCount++;
            }

            //@todo recode for transerring file names, only write not read
            ArrayList<String> lines = new ArrayList<>();
            try {
                lines.addAll(Files.readAllLines(newLogFile, Charset.forName("UTF-8")));
            } catch (IOException ex) {
                ex.getMessage();
                ex.printStackTrace();
            }
            lines.addAll(strForLog);

            try {
                Files.write(newLogFile, lines, Charset.forName("UTF-8"));
            } catch (IOException ex) {
                ex.getMessage();
                ex.printStackTrace();
            }
            if(lines.size() > linesCount){
                newLogFile = AppFileOperationsSimple.getNewLogFile();
            }

    }
}
