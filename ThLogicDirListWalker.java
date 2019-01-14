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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * This class release for logic and runned in NcThMifRunDirList.run()
 * need pipe (ArrayBlockingQueue) from rule or state for transfer data into tacker...
 * @param objectListAndLogger
 * @throws NullPointerException
 * @throws IOException
 * 
 * @author wladimirowichbiaran
 */

public class ThLogicDirListWalker {
    
    private final ThreadLocal<Boolean> isNotHaveLoggerThread;
    
    private final ThreadLocal<AppThWorkDirListRule> objectDirListRule;
    private final ThreadLocal<AppThWorkDirListState> objectListAndLogger;
    
    private final ThreadLocal<Path> currentPathForMakeIndex;
    
    private final ThreadLocal<ThFsFileVisitor> fileVisitor;
    private final ThreadLocal<ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>>> pipeVisitorToTacker;
    
    public ThLogicDirListWalker( final AppThWorkDirListRule objectDirListRule ) throws IOException {
        this.isNotHaveLoggerThread = new ThreadLocal<Boolean>();
        
        if( objectDirListRule == null ){
            this.isNotHaveLoggerThread.set(Boolean.TRUE);
            throw new NullPointerException(AppMsgEnFiledForLog.CREATE
                    + "Logic in DirListWalker work not init "
                    + AppMsgEnFiledForLog.CONSTRUCTOR
                    + ThLogicDirListWalker.class.getCanonicalName()
                    + AppMsgEnFiledForLog.EX_SRC_CLASS
                    + AppObjectsList.class.getCanonicalName()
                    + AppMsgEnFiledForLog.F_FIELD_NAME
                    + "objectListAndLogger"
                    + AppMsgEnFiledForLog.F_VALUE
                    + "null ");
        }
        this.objectDirListRule = new ThreadLocal<AppThWorkDirListRule>();
        this.objectDirListRule.set(objectDirListRule);
        
        this.currentPathForMakeIndex = new ThreadLocal<Path>();
        this.currentPathForMakeIndex.set(this.objectDirListRule.get().getCurrentPathForMakeIndex());
        
        this.isNotHaveLoggerThread.set(Boolean.FALSE);
        this.objectListAndLogger = new ThreadLocal<AppThWorkDirListState>();
        this.objectListAndLogger.set(this.objectDirListRule.get().getWorkDirListState());
        
        
        this.pipeVisitorToTacker = new ThreadLocal<ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>>>();
        
        this.pipeVisitorToTacker.set(this.objectDirListRule.get().getWorkDirListState().getPipeReaderToTacker());
        this.fileVisitor = new ThreadLocal<ThFsFileVisitor>();
        ThFsFileVisitor fv = null;
        try{
            fv = new ThFsFileVisitor(this.pipeVisitorToTacker.get(),
                this.objectListAndLogger.get().getListOfObjectAndLogger());
        } catch (IOException ex){
            this.objectListAndLogger.get().getListOfObjectAndLogger().putLogMessageState(
                    AppMsgEnFiledForLog.CREATE
                    + "Pipe in DirListWalker work not init "
                    + AppMsgEnFiledForLog.CONSTRUCTOR 
                    + ThLogicDirListWalker.class.getCanonicalName() 
                    + AppMsgEnFiledForLog.EX_DESCR
                    + ex.getMessage()
            );
            this.objectListAndLogger.get().getListOfObjectAndLogger().doLogger();
        }
        if( fv != null){
            this.fileVisitor.set(fv);
        } else {
            throw new IOException( AppMsgEnFiledForLog.CREATE
                    + "Logic in DirListWalker work not init "
                    + AppMsgEnFiledForLog.CONSTRUCTOR
                    + ThLogicDirListWalker.class.getCanonicalName()
                    + AppMsgEnFiledForLog.EX_SRC_CLASS
                    + ThFsFileVisitor.class.getCanonicalName()
                    + AppMsgEnFiledForLog.F_FIELD_NAME
                    + "fileVisitor"
                    + AppMsgEnFiledForLog.F_VALUE
                    + "null " 
                    
            );
        }
        objectListAndLogger.get().getListOfObjectAndLogger().putLogMessageState(AppMsgEnFiledForLog.CREATE 
                + ThLogicDirListWalker.class.getCanonicalName());
        objectListAndLogger.get().getListOfObjectAndLogger().doLogger();
    }
    private void errorInFunctionsProcess(Class<?> exClass, String functionText, 
            String valueFile, 
            Exception exOuter){
        if( isNotHaveLoggerThread.get() ){
            NcAppHelper.logException(ThFsFileVisitor.class.getCanonicalName(), exOuter);
        } else {
            String strErrorInApp = functionText
                    + AppMsgEnFiledForLog.F_VALUE
                    + valueFile
                    + AppMsgEnFiledForLog.F_EX_MSG
                    + exOuter.getMessage();
            String toLoggerMsg = NcAppHelper.exceptionToString(exClass, ThFsFileVisitor.class, strErrorInApp);
            this.objectListAndLogger.get().getListOfObjectAndLogger().putLogMessageError(toLoggerMsg);
        }
    }
    protected void doReadFsToPipe(){
        try{
            do{
                this.objectDirListRule.get().setDirListReaderLogicRunned();
            }while( !this.objectDirListRule.get().isDirListReaderLogicRunned() );
            NcAppHelper.outToConsoleIfDevAndParamTrue(this.objectDirListRule.get().getNameDirlistReader() 
                    + ".setDirListReaderLogicRunned", 
                    AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_WALKER_DO_READ_FS_TO_PIPE_SET_STARTED);
            try {
                Files.walkFileTree(this.currentPathForMakeIndex.get(), this.fileVisitor.get());
            } catch (IOException ex) {
                this.errorInFunctionsProcess(
                    IOException.class,
                    "Files.walkFileTree",
                    this.currentPathForMakeIndex.get().toString()
                    + ", "
                    + this.fileVisitor.get().toString(),
                    ex
                );
            } catch (IllegalStateException ex) {
                this.errorInFunctionsProcess(
                    IllegalStateException.class,
                    "Files.walkFileTree",
                    this.currentPathForMakeIndex.get().toString()
                    + ", "
                    + this.fileVisitor.get().toString(),
                    ex
                );
            } catch (SecurityException ex) {
                this.errorInFunctionsProcess(
                    SecurityException.class,
                    "Files.walkFileTree",
                    this.currentPathForMakeIndex.get().toString()
                    + ", "
                    + this.fileVisitor.get().toString(),
                    ex
                );
            }
            do{
                this.objectDirListRule.get().setDirListReaderLogicFinished();
            }while( !this.objectDirListRule.get().isDirListReaderLogicRunned() );
            NcAppHelper.outToConsoleIfDevAndParamTrue(this.objectDirListRule.get().getNameDirlistReader() 
                    + ".setDirListReaderLogicFinished", 
                    AppConstants.LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_WALKER_DO_READ_FS_TO_PIPE_SET_STARTED);
        } finally {
            this.isNotHaveLoggerThread.remove();
            this.objectDirListRule.remove();
            this.objectListAndLogger.remove();
            this.currentPathForMakeIndex.remove();
            this.fileVisitor.remove();
            this.pipeVisitorToTacker.remove();
        }
    }
}
