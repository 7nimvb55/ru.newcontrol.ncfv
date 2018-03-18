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
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsIdxFileVisitor implements FileVisitor {
    private FileVisitResult visitResult;
    private long countVisitFile;
    private long countVisitFileFailed;
    private long countPreVisitDir;
    private long countPostVisitDir;
    private long count;
    protected BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> buffDirList;
    
    public NcFsIdxFileVisitor(
            BlockingQueue<ConcurrentSkipListMap<UUID, NcDataListAttr>> inputDirList){
        this.visitResult = FileVisitResult.CONTINUE;
        this.countVisitFile = 0;
        this.countVisitFileFailed = 0;
        this.countPreVisitDir = 0;
        this.countPostVisitDir = 0;
        this.buffDirList = inputDirList;
        this.count = 0;
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
        ConcurrentSkipListMap<UUID, NcDataListAttr> toPipe = new ConcurrentSkipListMap<UUID, NcDataListAttr>();
        
        Path file = getPathFromObject(objectFile);
        Path fileName = file.getFileName();
        Path fileRealPath = Paths.get("");
        int hashCode = file.hashCode();
        
        Set<PosixFilePermission> posixFilePermissions =
            new HashSet<PosixFilePermission>();
        
        FileTime creationTime = attrs.creationTime();
        FileTime lastAccessTime = attrs.lastAccessTime();
        FileTime lastModifiedTime = attrs.lastModifiedTime();
        long size = attrs.size();
        long filesSize = -777;
        
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
            NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
            exHidden = Boolean.TRUE;
        }
        try {
            posixFilePermissions = Files.getPosixFilePermissions(file, LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
            exPosix = Boolean.TRUE;
        }
        try {
            fileRealPath = file.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
            exReal = Boolean.TRUE;
        }
        try {
            filesSize = Files.size(file);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
            exSize = Boolean.TRUE;
        }
        if( filesSize != size ){
            notEqualSize = Boolean.TRUE;
        }
        NcDataListAttr attrEntity = new NcDataListAttr(
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
        buffDirList.add(toPipe);
        this.count++;
    }
    private static Path getPathFromObject(Object objectFile){
        Path file;
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
        return file = (Path) objectFile;
         
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        BasicFileAttributes rAttr = Files.readAttributes((Path) dir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        this.countPreVisitDir++;
        return this.visitResult;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        BasicFileAttributes rAttr = Files.readAttributes((Path) file, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        makeListAttrForStorage(file, rAttr);
        this.countVisitFile++;
        return this.visitResult;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        this.countVisitFileFailed++;
        return this.visitResult;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        BasicFileAttributes rAttr = Files.readAttributes((Path) dir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        makeListAttrForStorage(dir, rAttr);
        this.countPostVisitDir++;
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
    
}
