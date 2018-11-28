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
public class AppLoggerRunnableHtmlWrite implements Runnable {
    
    private AppLoggerRule managerForThis;
    public AppLoggerRunnableHtmlWrite(
            AppLoggerRule outerManagerForThis
    ) {
        super();
        this.managerForThis = outerManagerForThis;
        this.managerForThis.getCurrentJob().setTrueToHTMLNewRunner();
        System.out.println("*** ||| *** ||| *** create log writer *** ||| *** ||| ***");
    }
    
    @Override
    public void run() {
        ArrayBlockingQueue<String> stringBusForLog = this.managerForThis.getStringBusForLogWrite();
        AppLoggerState currentJob = this.managerForThis.getCurrentJob();
        ArrayList<String> forRecord = AppObjectsBusHelper.cleanBusArrayBlockingToArrayString(stringBusForLog);
        currentJob.setFalseToHTMLJobDone();
        try {
            if( currentJob.isFromHTMLLogFileNameChanged() ){
                Files.write(currentJob.getToHTMLLogFileName(), forRecord, Charset.forName("UTF-8"));
                currentJob.setFalseToHTMLLogFileNameChanged();
            }
        } catch (IOException ex) {
            ex.getMessage();
            ex.printStackTrace();
        }
        currentJob.setFalseToHTMLNewRunner();
        currentJob.setTrueToHTMLJobDone();
    }

}