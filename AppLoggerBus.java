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
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerBus {
    private ArrayBlockingQueue<ArrayBlockingQueue<String>> commandsOutPut;
    private ArrayBlockingQueue<String> listForRunnableLogStrs;
    private ArrayBlockingQueue<String> readedLinesFromLogHTML;
    private ArrayBlockingQueue<ArrayBlockingQueue<String>> readedArrayForLines;
    //private ArrayBlockingQueue<String> readedLinesFromTablesWork;
    private ConcurrentSkipListMap<String, Path> listLogStorageFiles;
    private ArrayBlockingQueue<Path> readedFilesListInLogHtmlByTableMask;
    
    private Boolean isLogHtmlListTableFilesInit;
    private Boolean isLogHtmlStorageInit;
    
    private int countOfToQueueCapacityChange;
    
    private String instanceStartTimeWithMS;

    public AppLoggerBus() {
        this.commandsOutPut = new ArrayBlockingQueue<ArrayBlockingQueue<String>>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        this.listForRunnableLogStrs = new ArrayBlockingQueue<String>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        this.readedLinesFromLogHTML = new ArrayBlockingQueue<String>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        //this.readedLinesFromTablesWork = new ArrayBlockingQueue<String>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        
        this.readedArrayForLines = new ArrayBlockingQueue<ArrayBlockingQueue<String>>(AppConstants.LOG_HTML_MESSAGES_QUEUE_SIZE);
        
        setFalseForLogHtmlListTableFiles();
        setFalseForLogHtmlStorage();
        
        this.instanceStartTimeWithMS = 
                AppFileOperationsSimple.getNowTimeStringWithMS();
        this.listLogStorageFiles = getLogHtmlStorageList();
    }
    
    protected void setFalseForLogHtmlListTableFiles(){
        this.isLogHtmlListTableFilesInit = Boolean.FALSE;
    }
    protected void setTrueForLogHtmlListTableFiles(){
        this.isLogHtmlListTableFilesInit = Boolean.TRUE;
    }
    protected Boolean isSetForLogHtmlListTableFiles(){
        return this.isLogHtmlListTableFilesInit;
    }
    protected ArrayBlockingQueue<Path> getLogHtmlListTableFiles(){
        if( !isSetForLogHtmlListTableFiles() ){
            Path dirForRead = this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR);
            ArrayList<Path> filesByMaskFromDir = AppFileOperationsSimple.getFilesByMaskFromDir(
                dirForRead,
                "{" + AppFileNamesConstants.LOG_HTML_TABLE_PREFIX + "}*");
            this.readedFilesListInLogHtmlByTableMask = new ArrayBlockingQueue<Path>(filesByMaskFromDir.size());
            for( Path fileForRead : filesByMaskFromDir ){
                this.readedFilesListInLogHtmlByTableMask.add(fileForRead);
            }
            setTrueForLogHtmlListTableFiles();
        }
        return this.readedFilesListInLogHtmlByTableMask;
    }
    protected ArrayBlockingQueue<Path> updateLogHtmlListTableFiles(){
        if( isSetForLogHtmlListTableFiles() ){
            setFalseForLogHtmlListTableFiles();
            this.readedFilesListInLogHtmlByTableMask.clear();
        }
        return this.getLogHtmlListTableFiles();
    }
    
    protected void setFalseForLogHtmlStorage(){
        this.isLogHtmlStorageInit = Boolean.FALSE;
    }
    protected void setTrueForLogHtmlStorage(){
        this.isLogHtmlStorageInit = Boolean.TRUE;
    }
    protected Boolean isSetForLogHtmlStorage(){
        return this.isLogHtmlStorageInit;
    }
    protected ConcurrentSkipListMap<String, Path> getLogHtmlStorageList(){
        if( !isSetForLogHtmlStorage() ){
            Path logForHtmlCurrentLogSubDir = 
                    AppFileOperationsSimple.getLogForHtmlCurrentLogSubDir(this.instanceStartTimeWithMS);
            this.listLogStorageFiles = 
                    AppFileOperationsSimple.getNewLogFileInLogHTML(logForHtmlCurrentLogSubDir);
            this.listLogStorageFiles.put(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR, logForHtmlCurrentLogSubDir);
            
            setTrueForLogHtmlStorage();
        }
        return this.listLogStorageFiles;
    }
    protected void addAllStringsToRunnableBus(ArrayBlockingQueue<String> linesForSave){
        String pollFirstForSaveJsMenu = "";
        do{
            pollFirstForSaveJsMenu = linesForSave.poll();
            if( pollFirstForSaveJsMenu != null ){
                addStringToRunnableBus(pollFirstForSaveJsMenu);
            }
        }while( !linesForSave.isEmpty() );
    }
    
    protected void addStringToRunnableBus(String forPut){
        extendSizeForStringToQueue();
        this.listForRunnableLogStrs.add(forPut);
    }
    /**
     * @todo need develop code for write to file part when queue is full,
     * save data about writed part and save more parts in the next iterations
     */
    protected void extendSizeForStringToQueue(){
        if( ((this.listForRunnableLogStrs.size() 
                - (this.listForRunnableLogStrs.size() % 10)) / 10) 
                > this.listForRunnableLogStrs.remainingCapacity() 
        ){
            System.out.println("-|-|-|-|-QUEUE WARNING SIZE "
                + this.listForRunnableLogStrs.size()
                + "-|-|-|-|-QUEUE REMAINING CAPACITY "
                + this.listForRunnableLogStrs.remainingCapacity()
                + "-|-|-|-|-COUNT OF CHANGE QUEUE CAPACITY"
                + getCountOfToQueueCapacityChange()
            );
            int nowSize = this.listForRunnableLogStrs.size() + this.listForRunnableLogStrs.remainingCapacity();
            ArrayBlockingQueue<String> extendedQueue = new ArrayBlockingQueue<String>(nowSize * 10);
            do{ 
                String poll = this.listForRunnableLogStrs.poll();
                if( poll != null ){
                    extendedQueue.add(poll);
                }
            }while( !this.listForRunnableLogStrs.isEmpty() );
            incrementCountOfToQueueCapacityChange();
            this.listForRunnableLogStrs = extendedQueue;
        }
        
    }
    protected int getCountOfToQueueCapacityChange(){
        return this.countOfToQueueCapacityChange;
    }
    protected void incrementCountOfToQueueCapacityChange(){
        this.countOfToQueueCapacityChange++;
    }
    protected void addStringFromRunnableBus(String forPut){
        this.readedLinesFromLogHTML.add(forPut);
    }
    protected Path getLogFilesListElement(String keyForPath){
        return this.listLogStorageFiles.get(keyForPath);
    }
    protected void setLogFilesListElement(String keyForPath, Path ementForPut){
        this.listLogStorageFiles.put(keyForPath, ementForPut);
    }
    protected Path getCurrentLogHtmlStorageSubDir(){
        return this.listLogStorageFiles.get(AppFileNamesConstants.LOG_HTML_KEY_FOR_CURRENT_SUB_DIR);
    }
    protected Path getNewFileForLogHtml(){
        return AppFileOperationsSimple.getNewLogHtmlTableFile(getCurrentLogHtmlStorageSubDir());
    }
    protected void addToCommandsResultBus(ArrayBlockingQueue<String> forPut){
        this.commandsOutPut.add(forPut);
    }
    protected void clearCommandsResultBus(){
        this.commandsOutPut.clear();
    }
    protected void clearStringToRunnableBus(){
        this.listForRunnableLogStrs.clear();
    }
    protected void clearStringFromRunnableBus(){
        this.readedLinesFromLogHTML.clear();
    }
    protected ArrayBlockingQueue<ArrayBlockingQueue<String>> getCommandsOutPut(){
        return this.commandsOutPut;
    }
    protected ArrayBlockingQueue<String> getListForRunnableLogStrs(){
        return this.listForRunnableLogStrs;
    }
    protected ArrayBlockingQueue<String> getReadedLinesFromLogHTML(){
        return this.readedLinesFromLogHTML;
    }
    
}
