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
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerRunnableWrite implements Runnable {
    private ArrayBlockingQueue<AppLoggerController> busManager;
    public AppLoggerRunnableWrite(
            AppLoggerController outerManagerForThis
    ) {
        super();
        this.busManager = new ArrayBlockingQueue<AppLoggerController>(100);
        outerManagerForThis.currentWriterJob().setTrueToHTMLNewRunner();
        busManager.add(outerManagerForThis);
        
        System.out.println("*** ||| *** ||| *** create log writer *** ||| *** ||| ***");
    }
    
    @Override
    public void run() {
        AppLoggerController managerForThis = busManager.poll();
        AppLoggerStateWriter currentJob = managerForThis.currentWriterJob();
        if( !currentJob.isToHTMLJobDone() ){
            ArrayList<String> forRecord = 
                    AppObjectsBusHelper.cleanBusArrayBlockingToArrayString(
                            currentJob.getPartLinesForWrite());

            System.out.println("report writerRunnable size for " 
                    + forRecord.size()
                    + " write to "
                    + currentJob.getToHTMLLogFileName().toString()
            );
            
            //try {
                if( currentJob.isToHtmlFileNameSet() ){
                    AppFileOperationsSimple.writeToFile(currentJob.getToHTMLLogFileName(), forRecord);
                    //Files.write(currentJob.getToHTMLLogFileName(), forRecord, Charset.forName("UTF-8"));
                    currentJob.setFalseToHTMLLogFileNameChanged();
                }
            /*} catch (IOException ex) {
                ex.getMessage();
                ex.printStackTrace();
            }*/
            //forRecord.clear();
        }
        
        currentJob.setFalseToHTMLNewRunner();
        currentJob.setTrueToHTMLJobDone();
    }
}
