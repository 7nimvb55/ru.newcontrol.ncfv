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

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsIdxOperationFiles {
    protected static boolean existAndHasAccessRWNotLink(Path inFPath){
        try {
            Path normPath = inFPath.normalize();
            normPath = normPath.toAbsolutePath();
            if( Files.isSymbolicLink(normPath) ){
                return false;
            }
            if( Files.exists(normPath, LinkOption.NOFOLLOW_LINKS) ){
                if( Files.isRegularFile(normPath, LinkOption.NOFOLLOW_LINKS) ){
                    if( Files.isReadable(normPath) ){
                        return Files.isWritable(normPath);
                    }
                }
            }
        } catch (Exception ex) {
            NcAppHelper.logException(NcFsIdxOperationFiles.class.getCanonicalName(), ex);
        }
        return false;
    }
}
