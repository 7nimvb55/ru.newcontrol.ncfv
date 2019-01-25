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

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThDirListFileSystemHelper {
    //static function for release logic for do in DirList dl
    protected static Path getFilePath(FileSystem fileIndexStorage, Path pathToFile){
        
        return fileIndexStorage.getPath(pathToFile.toString());
    }
    protected static ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readDataFromFile(Path fileForRead){
        ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> readedDataFromFile = 
                new ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>();
        
        Boolean fileWriteException = Boolean.FALSE;
        try(ObjectInputStream oos = 
                new ObjectInputStream(Files.newInputStream(fileForRead)))
        {
            readedDataFromFile.putAll((ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr>) oos.readObject());
        } catch(Exception ex){
            ex.printStackTrace();
            fileWriteException = Boolean.TRUE;
        }
        return readedDataFromFile;
    }
    
}
