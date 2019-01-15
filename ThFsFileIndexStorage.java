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
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class ThFsFileIndexStorage {
    protected static Path getNewDirListFile(URI dirForCreate) throws IOException{
        Path getPathFromUri = Paths.get(dirForCreate);
        
        //pathIsNotDirectory(getPathFromUri);
        //pathIsNotReadWriteLink(getPathFromUri);
        String newTmpFileName = AppFileNamesConstants.SZFS_DIR_LIST_PREFIX + UUID.randomUUID().toString();
        Path getNewName = Paths.get(dirForCreate.toString(), "di", newTmpFileName);
        return getNewName;
    }
    protected static void writeData(ConcurrentSkipListMap<UUID, TdataDirListFsObjAttr> pollDataToDirListFile, 
            FileSystem fsZipIndexStorage, Path fnOut){
        Boolean fileWriteException = Boolean.FALSE;
        /*Path newDirListFile = null;
        try{
            newDirListFile = ThFsFileIndexStorage.getNewDirListFile(dirForCreate);
        } catch(IOException ex){
                ex.printStackTrace();
                fileWriteException = Boolean.TRUE;
        }*/
        
        
        /*try{
            
            //Files.createFile(newDirListFile);
        } catch(IOException ex){
            ex.printStackTrace();
            fileWriteException = Boolean.TRUE;
        }*/
        //newDirListFile.toString()
        
        if( pollDataToDirListFile != null ){
            try(ObjectOutputStream oos = 
                new ObjectOutputStream(fsZipIndexStorage.provider().newOutputStream(fnOut)
                //new FileOutputStream(newOutputStream)
                        ))
            {
                oos.writeObject(pollDataToDirListFile);
            } catch(Exception ex){
                ex.printStackTrace();
                fileWriteException = Boolean.TRUE;
            }
        }
        if( fileWriteException ){
            try{
                Files.createFile(Paths.get(fnOut.toString() + ".lck"));
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
