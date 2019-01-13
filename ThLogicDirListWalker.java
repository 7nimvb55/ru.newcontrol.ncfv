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
    
    private final ThreadLocal<AppObjectsList> objectListAndLogger;
    private final ThreadLocal<ThFsFileVisitor> fileVisitor;
    private final ThreadLocal<ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>>> pipeVisitorToTacker;
    
    public ThLogicDirListWalker(final AppObjectsList objectListAndLogger) throws IOException {
        this.isNotHaveLoggerThread = new ThreadLocal<Boolean>();
        
        if( objectListAndLogger == null ){
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
        this.isNotHaveLoggerThread.set(Boolean.FALSE);
            this.objectListAndLogger = new ThreadLocal<AppObjectsList>();
            this.objectListAndLogger.set(objectListAndLogger);
        
        
        this.pipeVisitorToTacker = new ThreadLocal<ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>>>();
        ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> pTT = new ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>>(1000);
        this.pipeVisitorToTacker.set(pTT);
        this.fileVisitor = new ThreadLocal<ThFsFileVisitor>();
        ThFsFileVisitor fv = null;
        try{
            fv = new ThFsFileVisitor(this.pipeVisitorToTacker.get(),
                this.objectListAndLogger.get());
        } catch (IOException ex){
            objectListAndLogger.putLogMessageState(
                    AppMsgEnFiledForLog.CREATE
                    + "Pipe in DirListWalker work not init "
                    + AppMsgEnFiledForLog.CONSTRUCTOR 
                    + ThLogicDirListWalker.class.getCanonicalName() 
                    + AppMsgEnFiledForLog.EX_DESCR
                    + ex.getMessage()
            );
            objectListAndLogger.doLogger();
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
        objectListAndLogger.putLogMessageState(AppMsgEnFiledForLog.CREATE + ThLogicDirListWalker.class.getCanonicalName());
        objectListAndLogger.doLogger();
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
            this.objectListAndLogger.get().putLogMessageError(toLoggerMsg);
        }
    }
    
}
