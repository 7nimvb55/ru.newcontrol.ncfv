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
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThFsFileVisitor implements FileVisitor {
    private FileVisitResult visitResult;
    private Long indexOfProcessIteration;
    private Long countVisitFile;
    private Long countVisitFileFailed;
    private Long countPreVisitDir;
    private Long countPostVisitDir;
    private Long count;
    private Long sleepInVisitFile;
    private Long sleepInVisitFileFailed;
    private Long sleepInPreVisitDir;
    private Long sleepInPostVisitDir;
    private Long sleepTimeDownScanSpeed;
    private ThreadLocal<AppObjectsList> objectListAndLogger;
    private Boolean isNotHaveLoggerThread;
    
    private ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> buffDirList;
    
    
    public ThFsFileVisitor(
            final ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> inputDirList,
            final AppObjectsList objectListAndLogger) throws IOException{
        
        if( objectListAndLogger != null ){
            isNotHaveLoggerThread = Boolean.FALSE;
            this.objectListAndLogger = new ThreadLocal<AppObjectsList>();
            this.objectListAndLogger.set(objectListAndLogger);
        } else {
            isNotHaveLoggerThread = Boolean.TRUE;
        }
        if( inputDirList == null){
            throw new IOException("Pipe for output in DirListWalker work is null " 
                    + ThFsFileVisitor.class.getCanonicalName());
        }
        this.indexOfProcessIteration = 0L;
        
        this.visitResult = FileVisitResult.CONTINUE;
        this.countVisitFile = 0L;
        this.countVisitFileFailed = 0L;
        this.countPreVisitDir = 0L;
        this.countPostVisitDir = 0L;
        
        this.buffDirList = inputDirList;
        
        this.count = 0L;
        this.sleepInVisitFile = 0L;
        this.sleepInVisitFileFailed = 0L;
        this.sleepInPreVisitDir = 0L;
        this.sleepInPostVisitDir = 0L;
        
        this.sleepTimeDownScanSpeed = 100L;
        
    }
    protected ArrayBlockingQueue<ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>> getBuffDirList(){
        return buffDirList;
    }
    protected long getCountVisitFile(){
        return this.countVisitFile;
    }
    
    protected long getCountVisitFileFailed(){
        return this.countVisitFileFailed;
    }
    
    protected long getCountPreVisitDir(){
        return this.countPreVisitDir;
    }
    
    protected long getCountPostVisitDir(){
        return this.countPostVisitDir;
    }
    private void makeListAttrForStorage(Object objectFile, BasicFileAttributes attrs){
        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> toPipe = new ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>();
        
        Path file = (Path) getPathFromObject(objectFile);
        Path fileName = file.getFileName();
        Path fileRealPath = Paths.get("");
        int hashCode = file.hashCode();
        
        Set<PosixFilePermission> posixFilePermissions =
            new HashSet<PosixFilePermission>();
        
        FileTime creationTime = attrs.creationTime();
        FileTime lastAccessTime = attrs.lastAccessTime();
        FileTime lastModifiedTime = attrs.lastModifiedTime();
        long size = attrs.size();
        long filesSize = -777L;
        
        boolean directory = attrs.isDirectory();
        boolean readable = Files.isReadable(file);
        boolean writable = Files.isWritable(file);
        boolean executable = Files.isExecutable(file);
        boolean other = attrs.isOther();
        boolean symbolicLink = attrs.isSymbolicLink();
        boolean absolute = file.isAbsolute();
        boolean notExists = Files.notExists(file, LinkOption.NOFOLLOW_LINKS);
        boolean exists = Files.exists(file, LinkOption.NOFOLLOW_LINKS);
            
        boolean regularFile = attrs.isRegularFile();
        boolean hidden = Boolean.FALSE;
        
        boolean exHidden = Boolean.FALSE;
        boolean exPosix = Boolean.FALSE;
        
        boolean exReal = Boolean.FALSE;
        boolean exSize = Boolean.FALSE;
        boolean notEqualSize = Boolean.FALSE;
        
        
        try {
            hidden = Files.isHidden(file);
            
        } catch (IOException ex) {
            this.errorInFunctionsProcess(IOException.class, "Files.isHidden", 
            file.toString(), 
            ex);
            exHidden = Boolean.TRUE;
            
        } catch (SecurityException ex) {
            this.errorInFunctionsProcess(SecurityException.class, "Files.isHidden", 
            file.toString(), 
            ex);
            exHidden = Boolean.TRUE;
        }
        //@todo basic... owner... group...
        try {
            posixFilePermissions = Files.getPosixFilePermissions(file, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ex) {
            this.errorInFunctionsProcess(IOException.class, "Files.getPosixFilePermissions", 
            file.toString(), 
            ex);
            exPosix = Boolean.TRUE;
        } catch (UnsupportedOperationException ex) {
            this.errorInFunctionsProcess(UnsupportedOperationException.class, "Files.getPosixFilePermissions", 
            file.toString(), 
            ex);
            exPosix = Boolean.TRUE;
        } catch (SecurityException ex) {
            this.errorInFunctionsProcess(SecurityException.class, "Files.getPosixFilePermissions", 
            file.toString(), 
            ex);
            exPosix = Boolean.TRUE;
        }
        try {
            fileRealPath = file.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ex) {
            this.errorInFunctionsProcess(IOException.class, "file.toRealPath", 
            file.toString(), 
            ex);
            exReal = Boolean.TRUE;
        } catch (SecurityException ex) {
            this.errorInFunctionsProcess(SecurityException.class, "file.toRealPath", 
            file.toString(), 
            ex);
            exPosix = Boolean.TRUE;
        }
        try {
            filesSize = Files.size(file);
        } catch (IOException ex) {
            this.errorInFunctionsProcess(IOException.class, "Files.size", 
            file.toString(), 
            ex);
            exSize = Boolean.TRUE;
        } catch (SecurityException ex) {
            this.errorInFunctionsProcess(SecurityException.class, "Files.size", 
            file.toString(), 
            ex);
            exPosix = Boolean.TRUE;
        }
        if( filesSize != size ){
            notEqualSize = Boolean.TRUE;
        }
        TdataDirListFsObjAttr attrEntity = new TdataDirListFsObjAttr(
            this.indexOfProcessIteration,
            file,
            fileName,
            fileRealPath,
            hashCode,
            posixFilePermissions,
            creationTime,
            lastAccessTime,
            lastModifiedTime,
            size,
            filesSize,
            directory,
            readable,
            writable,
            executable,
            other,
            symbolicLink,
            absolute,
            notExists,
            exists,
            regularFile,
            hidden,
            exHidden,
            exPosix,
            exReal,
            exSize,
            notEqualSize
        );
        toPipe.put(UUID.randomUUID(), attrEntity);
        
        this.indexOfProcessIteration++;
        
        /*
         * @todo get queue max length for compare with queue.size()      
         */
        if( this.buffDirList.size() > 950 ){
            try {
                String strThreadInfo = NcAppHelper.getThreadInfoToString(Thread.currentThread());
                NcAppHelper.outMessage(
                    strThreadInfo
                    + NcStrLogMsgField.MSG_WARNING.getStr()
                    + "Queue size limit, go to sleep time, "
                    + this.sleepTimeDownScanSpeed + " ms");
                Thread.sleep(this.sleepTimeDownScanSpeed);
            } catch (InterruptedException ex) {
                this.errorInFunctionsProcess(InterruptedException.class, "Thread.sleep", 
                String.valueOf(this.sleepTimeDownScanSpeed), 
                ex);
            }
        }
        try {
            this.buffDirList.put(toPipe);
            //System.out.println("[Runner]buffDirList-" + this.buffDirList.size());
        } catch (NullPointerException ex) {
                this.errorInFunctionsProcess(NullPointerException.class, 
                this.buffDirList.toString() + ".put", 
                "Null", 
                ex);
        } catch (InterruptedException ex) {
                this.errorInFunctionsProcess(NullPointerException.class, 
                this.buffDirList.toString() + ".put", 
                toPipe.toString(), 
                ex);
        }
        
        this.count++;
        
    }
    private void checkPipeSize(){
        this.buffDirList.remainingCapacity();
        this.buffDirList.size();
    }
    private static Path getPathFromObject(final Object objectFile){
        if( !Path.class.isInstance(objectFile) ){
            String strMethod = "makeListAttrForStorage()";
            try {
                strMethod = NcFsIdxFileVisitor.class.getDeclaredMethod("makeListAttrForStorage").toGenericString();
            } catch (NoSuchMethodException ex) {
                NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
            } catch (SecurityException ex) {
                NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
            }
            String strException = "For method "
                + strMethod + " of class "
                + NcFsIdxFileVisitor.class.getCanonicalName()
                + " need instance of "
                + Path.class.getCanonicalName()
                + " passed class "
                + objectFile.getClass().getCanonicalName();
            throw new IllegalArgumentException(strException);
        }
        return (Path) objectFile;
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        //needSleep(this.sleepInPreVisitDir.get());
        
        BasicFileAttributes rAttr = Files.readAttributes((Path) dir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        long localCount = this.countPreVisitDir;
        localCount++;
        this.countPreVisitDir = localCount;
        return this.visitResult;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        //needSleep(this.sleepInVisitFile.get());
        BasicFileAttributes rAttr = Files.readAttributes((Path) file, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        makeListAttrForStorage(file, rAttr);
        long localCount = this.countVisitFile;
        localCount++;
        this.countVisitFile = localCount;
        return this.visitResult;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        //needSleep(this.sleepInVisitFileFailed.get());
        long localCount = this.countVisitFileFailed;
        localCount++;
        this.countVisitFileFailed = localCount;
        return this.visitResult;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        //needSleep(this.sleepInPostVisitDir.get());
        BasicFileAttributes rAttr = Files.readAttributes((Path) dir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        makeListAttrForStorage(dir, rAttr);
        long localCount = this.countPostVisitDir;
        localCount++;
        this.countPostVisitDir = localCount;
        return this.visitResult;
    }
    
    protected void setVisitToContinue(){
        this.visitResult = FileVisitResult.CONTINUE;
    }
    protected void setVisitToSkipSiblings(){
        this.visitResult = FileVisitResult.SKIP_SIBLINGS;
    }
    protected void setVisitToSkipSubtree(){
        this.visitResult = FileVisitResult.SKIP_SUBTREE;
    }
    protected void setVisitToTerminate(){
        this.visitResult = FileVisitResult.TERMINATE;
    }
    private void errorInFunctionsProcess(Class<?> exClass, String functionText, 
            String valueFile, 
            Exception exOuter){
        if( isNotHaveLoggerThread ){
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
