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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AdihZipStorages {
    private final Long timeCreation;
    private final UUID objectLabel;
    private final ConcurrentSkipListMap<Integer, URI> storagesUriList;
    private final ConcurrentSkipListMap<Integer, Path> zipStoreFileList;
    AdihZipStorages(final ThIndexRule ruleIndexOuter){
        this.timeCreation = System.nanoTime();
        this.objectLabel = UUID.randomUUID();
        this.storagesUriList = new ConcurrentSkipListMap<Integer, URI>();
        this.zipStoreFileList = new ConcurrentSkipListMap<Integer, Path>();
    }
    protected static URI getUriStorageByNumber(){
        URI uri;
        try {
            uri = new URI("file:///foo/bar");
        } catch (URISyntaxException ex) {
            
        }
        return null;
    }
    protected static void updateStorageList(){
        
    }
    private static Boolean checkStorageZipFile(){
        return Boolean.TRUE;
    }
    private static void readZipFileList(){
        
    }
    private static void closeOpenedStores(){
        URI byPrefixGetUri = Paths.get("/", "file.zip").toUri();
        FileSystem fileSystem = FileSystems.getFileSystem(byPrefixGetUri);
        FileSystemProvider provider = fileSystem.provider();
        Boolean open = fileSystem.isOpen();
        Iterable<FileStore> fileStores = fileSystem.getFileStores();
        for(FileStore itemFs : fileStores ){

        }
        try {
            fileSystem.close();
        } catch (IOException exIo) {
            System.err.println(ThStorageWordLogicWrite.class.getCanonicalName() 
                    + " error for open storage for index, reason " 
                    + exIo.getMessage());
            exIo.printStackTrace();
        }
    }
}
