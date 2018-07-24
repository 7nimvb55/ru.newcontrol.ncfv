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
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsIdxOperationDirs {
    protected static void create(Path inFPath){
        if( !Files.exists(inFPath) ){
            try {
                Path toCreate = inFPath.normalize();
                toCreate = toCreate.toAbsolutePath();
                Files.createDirectory(inFPath);
            } catch (IOException ex) {
                NcAppHelper.logException(NcFsIdxOperationDirs.class.getCanonicalName(), ex);
            }
        }
    }
    protected static Path getRealPath(Path inFPath){
        if( !Files.exists(inFPath) ){
            try {
                return inFPath.toRealPath(LinkOption.NOFOLLOW_LINKS);
            } catch (IOException ex) {
                NcAppHelper.logException(NcFsIdxOperationDirs.class.getCanonicalName(), ex);
            }
        }
        String strMsg = "Imposible to build real path, see log";
        NcAppHelper.outMessage(
            NcStrLogMsgField.ERROR_CRITICAL.getStr()
            + strMsg
        );
        throw new RuntimeException(strMsg);
    }
    protected static boolean existAndHasAccessRW(Path inFPath){
        try {
            Path normPath = inFPath.normalize();
            normPath = normPath.toAbsolutePath();
            if( Files.exists(normPath, LinkOption.NOFOLLOW_LINKS) ){
                if( Files.isDirectory(normPath, LinkOption.NOFOLLOW_LINKS) ){
                    if( Files.isReadable(normPath) ){
                        return Files.isWritable(normPath);
                    }
                }
            }
        } catch (Exception ex) {
            NcAppHelper.logException(NcFsIdxOperationDirs.class.getCanonicalName(), ex);
        }
        return false;
    }
    protected static boolean existAndHasAccessRWNotLink(Path inFPath){
        try {
            Path normPath = inFPath.normalize();
            normPath = normPath.toAbsolutePath();
            if( Files.isSymbolicLink(normPath) ){
                return false;
            }
            if( Files.exists(normPath, LinkOption.NOFOLLOW_LINKS) ){
                if( Files.isDirectory(normPath, LinkOption.NOFOLLOW_LINKS) ){
                    if( Files.isReadable(normPath) ){
                        return Files.isWritable(normPath);
                    }
                }
            }
        } catch (Exception ex) {
            NcAppHelper.logException(NcFsIdxOperationDirs.class.getCanonicalName(), ex);
        }
        return false;
    }
    protected static boolean existAndHasAccessR(Path inFPath){
        try {
            Path normPath = inFPath.normalize();
            normPath = normPath.toAbsolutePath();
            if( Files.exists(normPath, LinkOption.NOFOLLOW_LINKS) ){
                if( Files.isDirectory(normPath, LinkOption.NOFOLLOW_LINKS) ){
                    return Files.isReadable(normPath);
                }
            }
        } catch (Exception ex) {
            NcAppHelper.logException(NcFsIdxOperationDirs.class.getCanonicalName(), ex);
        }
        return false;
    }
    protected static Path checkScanPath(Path toScan) throws IOException{
        if( !existAndHasAccessR(toScan) ){
            throw new IOException("Directory not have access for read " + toScan.toString());
        }
        Path prePathToStart = toScan.normalize();
        prePathToStart = prePathToStart.toAbsolutePath();
        try {
            prePathToStart = prePathToStart.toRealPath(LinkOption.NOFOLLOW_LINKS);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxStorage.class.getCanonicalName(), ex);
        }
        return prePathToStart;
    }
    protected static boolean existAndHasAccessOnlyR(Path inFPath){
        try {
            Path normPath = inFPath.normalize();
            normPath = normPath.toAbsolutePath();
            if( Files.exists(normPath, LinkOption.NOFOLLOW_LINKS) ){
                if( Files.isDirectory(normPath, LinkOption.NOFOLLOW_LINKS) ){
                    if( Files.isReadable(normPath) ){
                        return !Files.isWritable(normPath);
                    }
                }
            }
        } catch (Exception ex) {
            NcAppHelper.logException(NcFsIdxOperationDirs.class.getCanonicalName(), ex);
        }
        return false;
    }
}
