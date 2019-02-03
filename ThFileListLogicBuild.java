/*
 * Copyright 2019 wladimirowichbiaran.
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
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThFileListLogicBuild {
    protected void doBuildToIndexFileList(final ThFileListRule outerRuleFileList){
        ThDirListBusReaded busJobForRead = outerRuleFileList.getIndexRule().getIndexState().getBusJobForRead();
        while( !busJobForRead.isJobQueueEmpty() ){
            ThDirListStateJobReader jobForRead = busJobForRead.getJobForRead();
            if( !jobForRead.isBlankObject() ){
                if( !jobForRead.isReadedDataEmpty() ){
                    if( jobForRead.isReaderJobDone() ){
                        Path readedPath = jobForRead.getReadedPath();
                        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readedData = jobForRead.getReadedData();

                        int iterations = 0;
                        //System.out.println(ThWordLogicBuild.class.getCanonicalName() + " read from job size " + readedData.size());
                        
                        for( Map.Entry<UUID, TdataDirListFsObjAttr> recordItem : readedData.entrySet() ){
                            String shortDataToString = recordItem.getValue().file;
                            Path dirListReaded = Paths.get(shortDataToString);
                            for (int i = 0; i < dirListReaded.getNameCount(); i++) {
                                /**
                                 * @todo release index by depth dirictories, file extentions, data time map
                                 * create job for bus index jobs workers
                                 * do for word index in runnables workers,
                                 * sort results to packets by queue system
                                 * 
                                 * for dirlist reader need release jobforneed read data
                                 * before end of release current packet
                                 */
                                Path namePart = dirListReaded.getName(i);

                                System.out.println(
                                        "file: " + readedPath.toString()
                                        + " recnum " + recordItem.getKey().toString()
                                        + " dataToNext " + namePart.toString()        );
                            }
                        }
                    }
                    jobForRead.cleanReadedData();
                    jobForRead.setTrueReaderJobDone();
                    jobForRead = null;
                }
            }
        }
    }
}

