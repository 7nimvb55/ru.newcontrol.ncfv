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
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcIdxNioDirListManager {
    protected static CopyOnWriteArrayList<Path> getFilesFromDirListStorage() {
        CopyOnWriteArrayList<Path> toReturn= new CopyOnWriteArrayList<Path>();
        
        Path pathIndexFile = NcFsIdxStorageInit.buildPathToFileOfIdxStorage();
        Map<String, String> fsProperties = NcFsIdxStorageInit.getFsPropExist();
        System.out.println("\n\n\n file storage path: " + pathIndexFile.toString());
        Boolean existFSfile = NcFsIdxOperationFiles.existAndHasAccessRWNotLink(pathIndexFile);
        System.out.println("NcFsIdxOperationFiles.existAndHasAccessRWNotLink(): " + existFSfile.toString());
        if( !existFSfile ){
            fsProperties = NcFsIdxStorageInit.getFsPropCreate();
        }
        for (Map.Entry<String, String> entry : fsProperties.entrySet()) {
            System.out.println("Key: " + entry.getKey() + " Val: " + entry.getValue());
        }
        URI uriZipIndexStorage = URI.create("jar:file:" + pathIndexFile.toUri().getPath());
        try(FileSystem fsZipIndexStorage = 
            FileSystems.newFileSystem(uriZipIndexStorage, fsProperties)){
            
            NcParamFs dataStorage = NcFsIdxStorageInit.initStorageStructure(fsZipIndexStorage);
            try{
                
            SimpleFileVisitor<Path> visitorPath = new SimpleFileVisitor<Path>(){
            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs){
                toReturn.add(file);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs){
                toReturn.add(dir);
                return FileVisitResult.CONTINUE;
            }
            
            };
            Path toScanPath =  fsZipIndexStorage.getPath(dataStorage.getDirDirList().toString());   
            Files.walkFileTree(toScanPath, 
                visitorPath);    
                    
            }catch(IOException ex){
                
            }
        }catch(IOException ex){
                
        }
        return toReturn;
    }
}
