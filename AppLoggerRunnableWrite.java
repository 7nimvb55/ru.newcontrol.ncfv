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

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerRunnableWrite implements Runnable {
    private AppLoggerController managerForThis;
    public AppLoggerRunnableWrite(
            AppLoggerController outerManagerForThis
    ) {
        super();
        this.managerForThis = outerManagerForThis;
        this.managerForThis.currentWriterJob().setTrueToHTMLNewRunner();
        System.out.println("*** ||| *** ||| *** create log writer *** ||| *** ||| ***");
    }
    
    @Override
    public void run() {
        AppLoggerStateWriter currentJob = this.managerForThis.currentWriterJob();
        if( !currentJob.isToHTMLJobDone() ){
            ArrayList<String> forRecord = 
                    AppObjectsBusHelper.cleanBusArrayBlockingToArrayString(
                            currentJob.getPartLinesForWrite());

            System.out.println("report writerRunnable size for " 
                    + forRecord.size()
                    + " write to "
                    + currentJob.getToHTMLLogFileName().toString()
            );
            try {
                if( currentJob.isToHtmlFileNameSet() ){
                    Files.write(currentJob.getToHTMLLogFileName(), forRecord, Charset.forName("UTF-8"));
                    currentJob.setFalseToHTMLLogFileNameChanged();
                }
            } catch (IOException ex) {
                ex.getMessage();
                ex.printStackTrace();
            }
        }
        currentJob.setFalseToHTMLNewRunner();
        currentJob.setTrueToHTMLJobDone();
    }
}
