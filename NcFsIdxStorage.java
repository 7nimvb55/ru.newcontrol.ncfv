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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsIdxStorage {
    protected static void putDataToIndex(NcSwGUIComponentStatus lComp, Path dirForScan){
        FileSystem aDefault = FileSystems.getDefault();
        Iterable<FileStore> fileStores = aDefault.getFileStores();
        
        ArrayList<String> arrStr = new ArrayList<String>();
        arrStr.add("getFileStores:");
        arrStr.add(fileStores.toString());
        
        for (Iterator<FileStore> iteratorFileStores = fileStores.iterator();
                iteratorFileStores.hasNext();) {
            FileStore next = iteratorFileStores.next();
            arrStr.add("FileStore.name:");
            arrStr.add(next.name());
            arrStr.add("FileStore.type:");
            arrStr.add(next.type());
        }
        Iterable<Path> rootDirectories = aDefault.getRootDirectories();
        
        for (Iterator<Path> iteratorRootDirectories = rootDirectories.iterator();
                iteratorRootDirectories.hasNext();) {
            Path next = iteratorRootDirectories.next();
            arrStr.add("Path.toString:");
            arrStr.add(next.toString());
            arrStr.add("Path.getRoot.toString:");
            arrStr.add(next.getRoot().toString());
        }
        String strPathToStart = "";
        Path pathToStart = dirForScan.toAbsolutePath();
        pathToStart = pathToStart.normalize();
        try {
            arrStr.add("File.getCanonicalPath:");
            pathToStart = pathToStart.toRealPath(LinkOption.NOFOLLOW_LINKS);
            strPathToStart = pathToStart.toString();
            arrStr.add(strPathToStart);
            
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        
        
        /*NcFsIdxFileVisitor fileVisitor = new NcFsIdxFileVisitor(lComp);
        
        arrStr.add("pathToStart:" + pathToStart.toString());
        
        arrStr.add("[count Dir]"
        + fileVisitor.getCountPostVisitDir()
        + "[count File]"
        + fileVisitor.getCountVisitFile());
        //NcFsIdxOperationFiles.outToGUIFileAttributes(pathToStart, lComp);
        try {
            arrStr.add("Start: Files.walkFileTree");
            
            Files.walkFileTree(pathToStart, fileVisitor);
            
            arrStr.add("After start: Files.walkFileTree");
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        arrStr.add("[count preVisitDir]"
            + fileVisitor.getCountPreVisitDir()
            + "[count postVisitDir]"
            + fileVisitor.getCountPostVisitDir()
            + "[count VisitFile]"
            + fileVisitor.getCountVisitFile()
            + "[count VisitFileFailed]"
            + fileVisitor.getCountVisitFileFailed());
        //NcThWorkerUpGUITreeOutput.outputTreeAddChildren(lComp, arrStr);*/
        NcThWorkerUpGUITreeWork.workTreeAddChildren(lComp, arrStr);
    }
    
    protected static void getDataFromIndex(NcSwGUIComponentStatus lComp){
        ThreadLocal<NcParamFs> openFsIdx = 
                NcFsIdxStorageInit.getStorage(lComp);
        
    }
    protected static void getIndexStorage(){
    }
    
    
}
