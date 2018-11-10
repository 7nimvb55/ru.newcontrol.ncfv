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
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLogger extends Thread {
    private ArrayBlockingQueue<String> messagesQueueForLogging;
    private Integer linesCount;
    private Path newLogFile;

    public AppLogger(ArrayBlockingQueue<String> messagesQueueOuter) {
        super();
        messagesQueueForLogging = messagesQueueOuter;
        linesCount = AppConstants.LOG_LINES_COUNT;
        newLogFile = AppFileOperationsSimple.getNewLogFile();
    }
    
    @Override
    public void run() {
        //@todo rebuild for limit str count in files, add log rotate in arch folder, create zip for old logs
        ArrayList<String> strForLog = new ArrayList<String>();
        System.out.println("lines for log " + messagesQueueForLogging.size());
        while ( !messagesQueueForLogging.isEmpty() ) {            
            strForLog.add(messagesQueueForLogging.poll());
            linesCount++;
        }
        
        
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
