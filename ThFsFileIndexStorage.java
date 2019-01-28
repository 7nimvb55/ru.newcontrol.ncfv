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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThFsFileIndexStorage {
    protected static Path getNewDirListFile(//NcParamFs indexStorage, 
            AppThWorkDirListRule outerRuleDirListWork,
            Boolean notFull) {
        FileSystem indexStorage = outerRuleDirListWork.getFsZipIndexStorage();
        String newTmpFileName = AppFileNamesConstants.SZFS_DIR_LIST_FILE_PREFIX;
        if( notFull ){
            newTmpFileName = newTmpFileName + AppFileNamesConstants.SZFS_DIR_LIST_FILE_NOT_LIMITED;
        }
        newTmpFileName = newTmpFileName + UUID.randomUUID().toString();
        
        Path getNewName = indexStorage.getPath(newTmpFileName);
        return getNewName;
    }
    protected static void writeData(ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> pollDataToDirListFile,
            AppThWorkDirListRule outerRuleDirListWork){
            //NcParamFs indexStorage){
        Boolean fileWriteException = Boolean.FALSE;
        Path newDirListFile;
        if( pollDataToDirListFile.size() != AppConstants.DIR_LIST_RECORDS_COUNT_LIMIT ){
            newDirListFile = ThFsFileIndexStorage.getNewDirListFile(outerRuleDirListWork, Boolean.TRUE);
        } else {
            newDirListFile = ThFsFileIndexStorage.getNewDirListFile(outerRuleDirListWork, Boolean.FALSE);
        }
         

        if( pollDataToDirListFile != null ){
            try(ObjectOutputStream oos = 
                new ObjectOutputStream(Files.newOutputStream(newDirListFile)))
            {
                oos.writeObject(pollDataToDirListFile);
            } catch(Exception ex){
                ex.printStackTrace();
                fileWriteException = Boolean.TRUE;
            }
        }
        if( fileWriteException ){
            try{
                Files.createFile(outerRuleDirListWork.getFsZipIndexStorage().getPath( 
                        newDirListFile.getFileName().toString() + ".lck"));
            } catch(IOException ex){
                ex.printStackTrace();
                fileWriteException = Boolean.TRUE;
            }
        }
    }
    private static void pathIsNotDirectory(Path innerWorkPath) throws IOException{
        if ( !Files.exists(innerWorkPath, LinkOption.NOFOLLOW_LINKS) ){
            System.out.println("[ERROR] File or Directory exist and it is not a Directory: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory exist and it is not a Directory: " + innerWorkPath.toString());
        }
        if ( !Files.isDirectory(innerWorkPath, LinkOption.NOFOLLOW_LINKS) ){
            System.out.println("[ERROR] File exist and it is not a Directory: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File exist and it is not a Directory: " + innerWorkPath.toString());
        }
    }
    private static void pathIsNotReadWriteLink(Path innerWorkPath) throws IOException{
        if ( !Files.isReadable(innerWorkPath) ){
            System.out.println("[ERROR] File or Directory exist and it is not a Readable: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory exist and it is not a Readable: " + innerWorkPath.toString());
        }
        if ( !Files.isWritable(innerWorkPath) ){
            System.out.println("[ERROR] File or Directory exist and it is not a Writable: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory exist and it is not a Writable: " + innerWorkPath.toString());
        }
        if ( Files.isSymbolicLink(innerWorkPath) ){
            System.out.println("[ERROR] File or Directory exist and it is not a SymbolicLink: " + innerWorkPath.toString());
            throw new IOException("[ERROR] File or Directory exist and it is a SymbolicLink: " + innerWorkPath.toString());
        }
    }

}
