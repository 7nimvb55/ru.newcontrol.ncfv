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
public class AppLoggerInfoToReport {
    protected static AppLoggerStateReader initReaderNewJobLite(
            Path fileNameForWrite
    ){
        String instanceStartTimeWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        
        AppLoggerStateReader createNewReaderJob = AppLoggerRuleHelper.createNewReaderJob();
        createNewReaderJob.setTrueInitStartRead();
        createNewReaderJob.setThreadGroupName("grReader-" + instanceStartTimeWithMS + "-" + UUID.randomUUID().toString());
        createNewReaderJob.setThreadName("runReader-" + instanceStartTimeWithMS + "-" + UUID.randomUUID().toString());
        createNewReaderJob.setFromHTMLFileName(fileNameForWrite);
        createNewReaderJob.setTrueInitEndRead();
        return createNewReaderJob;
    }
}
