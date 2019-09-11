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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThFileListLogicBuild {
    protected void doBuildToIndexFileList(final ThFileListRule outerRuleFileList){
        AdilRule adilRule = outerRuleFileList.getIndexRule().getAdilRule();
        AdilState adilState = adilRule.getAdilState();
        String msgToLog = AdilConstants.INFO_LOGIC_POSITION
                + AdilConstants.CANONICALNAME
                + ThFileListLogicBuild.class.getCanonicalName()
                + AdilConstants.METHOD
                + "doBuildToIndexFileList()";
        adilState.putLogLineByProcessNumberMsg(5, 
                msgToLog
                + AdilConstants.START);
        ThDirListBusReaded busJobForRead = outerRuleFileList.getIndexRule().getIndexState().getBusJobForRead();
        // bus for output data to next index system
        ThFileListState fileListState = outerRuleFileList.getFileListState();
        ThFileListBusToNext busJobForFileListToNext = fileListState.getBusJobForFileListToNext();
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
                            
                            ConcurrentHashMap<Integer, Path> outputedDataForSend = buildDataForSend(
                                        readedPath, 
                                        dirListReaded);
                            
                            busJobForFileListToNext.addWriterJob(recordItem.getKey(), outputedDataForSend);
                            //for (int i = 0; i < dirListReaded.getNameCount(); i++) {
                                /**
                                 * @todo release index by depth dirictories, file extentions, data time map
                                 * create job for bus index jobs workers
                                 * do for word index in runnables workers,
                                 * sort results to packets by queue system
                                 * 
                                 * for dirlist reader need release jobforneed read data
                                 * before end of release current packet
                                 */
                                //Path namePart = dirListReaded.getName(i);

                                //System.out.println(
                                //        "file: " + readedPath.toString()
                                //        + " recnum " + recordItem.getKey().toString()
                                //        + " dataToNext " + namePart.toString()        );
                                /**
                                 * 
                                 */
                                
                                
                            //}
                            
                        }
                    }
                    jobForRead.cleanReadedData();
                    jobForRead.setTrueReaderJobDone();
                    jobForRead = null;
                }
            }
        }
        adilState.putLogLineByProcessNumberMsg(5, 
                msgToLog
                + AdilConstants.FINISH);
    }
    private static ConcurrentHashMap<Integer, Path> buildDataForSend(
            final Path readedPath, 
            final Path namePart){
        Path funcReadedPath;
        Path funcNamePart;
        ConcurrentHashMap<Integer, Path> dataForSend;
        try {
            funcReadedPath = (Path) readedPath;
            funcNamePart = (Path) namePart;
            dataForSend = new ConcurrentHashMap<Integer, Path>();
            /**
             * indexOfnamePathElement
             * funcReadedPath - 1506682974
             * funcNamePart - -589260798
             */
            dataForSend.put(1506682974, funcReadedPath);
            dataForSend.put(-589260798, funcNamePart);
            return dataForSend;
        } finally {
            funcReadedPath = null;
            funcNamePart = null;
            dataForSend = null;
        }
        
    }
}

