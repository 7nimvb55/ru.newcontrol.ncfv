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
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcFsDefaults {
    protected static Path getUserHomePath() throws IOException{
        String usrHomePath = System.getProperty("user.home");
        Path parentForFS = Paths.get(usrHomePath);
        parentForFS = parentForFS.normalize();
        parentForFS = parentForFS.toAbsolutePath();
        parentForFS = parentForFS.toRealPath(LinkOption.NOFOLLOW_LINKS);
        return parentForFS;
    }
    protected static Path getAppPath() throws IOException{
        String appPath = System.getProperty("java.class.path");
        Path parentForFS = Paths.get(appPath);
        parentForFS = parentForFS.normalize();
        parentForFS = parentForFS.toAbsolutePath();
        parentForFS = parentForFS.toRealPath(LinkOption.NOFOLLOW_LINKS);
        return parentForFS;
    }
    protected static Path getHomeOrAppOrRootStorage(){
        Path toReturn = null;
        try{
            toReturn = NcFsDefaults.getUserHomePath();
            return toReturn;
        }catch(IOException ex){
            NcAppHelper.logException(NcFsIdxStorageInit.class.getCanonicalName(), ex);
            String strMsg = "Bad User Home Directory or link "
                + NcStrLogMsgField.EXCEPTION_MSG.getStr() + ex.getMessage();
            NcAppHelper.outMessage(
                NcStrLogMsgField.ERROR.getStr()
                + strMsg
            );
        }
        toReturn = getFirstDefaultRoot();
        
        if( toReturn == null ){
            try{
                toReturn = NcFsDefaults.getAppPath();
                return toReturn;
            }catch(IOException ex){
                NcAppHelper.logException(NcFsIdxStorageInit.class.getCanonicalName(), ex);
                String strMsg = "Bad User Home Directory or link "
                    + NcStrLogMsgField.EXCEPTION_MSG.getStr() + ex.getMessage();
                NcAppHelper.outMessage(
                    NcStrLogMsgField.ERROR.getStr()
                    + strMsg
                );
            }
        }
/**
 *  @TODO if toReturn null new Throw Exception
 * for say to user about filesystems not founded 
 * and user need contact to system Administrator
 * or not use app
 */
        return toReturn;
    }
    protected static Path getFirstDefaultRoot(){
         
        for (Iterator iterator1 = FileSystems.getDefault().getRootDirectories().iterator(); iterator1.hasNext();) {
            Path next = (Path) iterator1.next();
            return next;
            
        }
        return null;
    }
}
