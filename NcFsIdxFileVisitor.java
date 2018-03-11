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
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsIdxFileVisitor implements FileVisitor {
    private final NcSwGUIComponentStatus lComp;
    private long countVisitFile;
    private long countVisitFileFailed;
    private long countPreVisitDir;
    private long countPostVisitDir;
    private long count;
    protected BlockingQueue<TreeMap<Long, NcDataListAttr>> buffDirList;
    
    public NcFsIdxFileVisitor(NcSwGUIComponentStatus lComp,
            BlockingQueue<TreeMap<Long, NcDataListAttr>> inputDirList){
        this.lComp = lComp;
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
        TreeMap<Long, NcDataListAttr> toPipe = new TreeMap<Long, NcDataListAttr>();
        
        
        
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
        toPipe.put(this.count, attrEntity);
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
    private void makeListAttributes(Object objectFile, BasicFileAttributes attrs){
        
        TreeMap<Long, NcDcIdxDirListToFileAttr> toPipe = new TreeMap<Long, NcDcIdxDirListToFileAttr>();
        Path file = (Path) objectFile;
        Class<?> aClass = objectFile.getClass();
        String objectFileCanonicalName = aClass.getCanonicalName();
        
        String strRealPath = "Not avalable";
        
        FileStore fsFile = null;
        long totalSpace = 0;
        
        boolean isHidden = false;
        boolean isRegularFile = true;
        long fileTime = 0;
        try {
            
            FileStore fileStore = Files.getFileStore(file);
            FileSystem fileSystem = file.getFileSystem();
            fsFile = Files.getFileStore(file);
            totalSpace = fsFile.getTotalSpace();
            
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
        }
        
        long recId = System.nanoTime();
        try{
            UUID randomUUID = UUID.randomUUID();//
            
            Set<PosixFilePermission> posixFilePermissions = Files.getPosixFilePermissions(file, LinkOption.NOFOLLOW_LINKS);//
            for (PosixFilePermission posixFilePermission : posixFilePermissions) {
                posixFilePermission.GROUP_EXECUTE.equals(posixFilePermission);
                posixFilePermission.GROUP_READ.equals(posixFilePermission);
                posixFilePermission.GROUP_WRITE.equals(posixFilePermission);
                posixFilePermission.OTHERS_EXECUTE.equals(posixFilePermission);
                posixFilePermission.OTHERS_READ.equals(posixFilePermission);
                posixFilePermission.OTHERS_WRITE.equals(posixFilePermission);
                posixFilePermission.OWNER_EXECUTE.equals(posixFilePermission);
                posixFilePermission.OWNER_READ.equals(posixFilePermission);
                posixFilePermission.OWNER_WRITE.equals(posixFilePermission);
            }
            String probeContentType = Files.probeContentType(file);
            String fileToString = file.toString();
            
            strRealPath = file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString();//
            UserPrincipal owner = Files.getOwner(file, LinkOption.NOFOLLOW_LINKS);//

            Path fileName = file.getFileName();//
            int nameCount = file.getNameCount();
            Path parent = file.getParent();
            Path root = file.getRoot();
            int hashCode = file.hashCode();//   



            Object fileKey = attrs.fileKey();
            String canonicalName = fileKey.getClass().getCanonicalName();
            
            boolean directory = attrs.isDirectory();//
            boolean readable = Files.isReadable(file);//
            boolean writable = Files.isWritable(file);//
            boolean executable = Files.isExecutable(file);//
            boolean other = attrs.isOther();//
            
            boolean regularFile = attrs.isRegularFile();//
            boolean regularFile1 = Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS);
            
            boolean notExists = Files.notExists(file, LinkOption.NOFOLLOW_LINKS);//
            boolean exists = Files.exists(file, LinkOption.NOFOLLOW_LINKS);//
            boolean hidden = Files.isHidden(file);//
            
            boolean absolute = file.isAbsolute();//

            boolean filesSymbolicLink = Files.isSymbolicLink(file);
            boolean symbolicLink = attrs.isSymbolicLink();//

            FileTime creationTime = attrs.creationTime();//
            FileTime lastAccessTime = attrs.lastAccessTime();//
            FileTime filesLastModifiedTime = Files.getLastModifiedTime(file, LinkOption.NOFOLLOW_LINKS);
            FileTime lastModifiedTime = attrs.lastModifiedTime();//
            
            fileTime = Files.getLastModifiedTime(file, LinkOption.NOFOLLOW_LINKS).toMillis();
            long filesSize = Files.size(root);
            long size = attrs.size();//
            String toString = attrs.toString();
            
            isRegularFile = Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS);
            isHidden = Files.isHidden(file);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
        }
        
        NcDcIdxDirListToFileAttr entityOfList = new NcDcIdxDirListToFileAttr(
            recId,//long dirListID,
            0,//long diskID,
            0,//long diskSnLong,
            totalSpace,//long diskTotalSpace,
            "not released in this version",//String diskProgramAlias,
            "not released in this version",//String diskSnHex,
            '#',//char diskLetter,
            file.toString(),//String path,
            attrs.size(),//long fileLength,
            Files.isReadable(file),//boolean fileCanRead,
            Files.isWritable(file),//boolean fileCanWrite,
            Files.isExecutable(file),//boolean fileCanExecute,
            isHidden,//boolean fileIsHidden,
            fileTime,//long fileLastModified,
            attrs.isDirectory(),//boolean fileIsDirectory,
            isRegularFile,//boolean fileIsFile,
            false,//boolean deletedRec,
            0//long changedRecordID
        );
        //toPipe.put(this.count, entityOfList);
        //buffDirList.add(toPipe);
        this.count++;
    }
    private void outAttributesToGUI(Object objectFile, BasicFileAttributes attrs){
        Path file = (Path) objectFile;
        String strRealPath = "Not avalable";
        try{
            strRealPath = file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString();
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
        }    
            
        ArrayList<String> arrStr = new ArrayList<String>();
        arrStr.add("[Path.toString()]" + file.toString());
        arrStr.add("[Path.toRealPath(LinkOption.NOFOLLOW_LINKS).toString()]" + strRealPath);
        
        arrStr.add("[BasicFileAttributes.creationTime()]" + attrs.creationTime());
        arrStr.add("[BasicFileAttributes.fileKey()]" + attrs.fileKey());
        arrStr.add("[BasicFileAttributes.hashCode()]" + attrs.hashCode());
        arrStr.add("[BasicFileAttributes.isDirectory()]" + attrs.isDirectory());
        arrStr.add("[BasicFileAttributes.isOther()]" + attrs.isOther());
        arrStr.add("[BasicFileAttributes.isRegularFile()]" + attrs.isRegularFile());
        arrStr.add("[BasicFileAttributes.isSymbolicLink()]" + attrs.isSymbolicLink());
        arrStr.add("[BasicFileAttributes.lastAccessTime()]" + attrs.lastAccessTime());
        arrStr.add("[BasicFileAttributes.lastModifiedTime()]" + attrs.lastModifiedTime());
        arrStr.add("[BasicFileAttributes.size()]" + attrs.size());
        arrStr.add("[BasicFileAttributes.toString()]" + attrs.toString());

        NcThWorkerUpGUITreeOutput.outputTreeAddChildren(lComp, arrStr);
    }

    @Override
    public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
        BasicFileAttributes rAttr = Files.readAttributes((Path) dir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        this.countPreVisitDir++;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
        BasicFileAttributes rAttr = Files.readAttributes((Path) file, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        makeListAttrForStorage(file, rAttr);
        this.countVisitFile++;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
        this.countVisitFileFailed++;
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
        BasicFileAttributes rAttr = Files.readAttributes((Path) dir, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
        makeListAttrForStorage(dir, rAttr);
        this.countPostVisitDir++;
        return FileVisitResult.CONTINUE;
    }
    
}
