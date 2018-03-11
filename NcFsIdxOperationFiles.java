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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
    protected static void outToGUIFileAttributes(Path file, NcSwGUIComponentStatus lComp){
        try{
            
            //Files.readAttributes(file, attributes, options);
            //Set<String> supportedFileAttributeViews = file.getFileSystem().supportedFileAttributeViews();
            Set<String> supportedFileAttributeViews = new HashSet<String>();
            supportedFileAttributeViews.add("creationTime");
            supportedFileAttributeViews.add("lastModifiedTime");
            supportedFileAttributeViews.add("size");
            supportedFileAttributeViews.add("isDirectory");
            supportedFileAttributeViews.add("isFile");
            supportedFileAttributeViews.add("isReadable");
            supportedFileAttributeViews.add("isWritable");
            supportedFileAttributeViews.add("isExecutable");
            supportedFileAttributeViews.add("isHidden");
            
            ArrayList<String> arrStr = new ArrayList<String>();
            arrStr.add(file.toRealPath(LinkOption.NOFOLLOW_LINKS).toString());
            arrStr.add("FileSystems.getDefault().supportedFileAttributeViews()");
            for (String supportedFileAttributeView : supportedFileAttributeViews) {
                arrStr.add("[KEY]" + supportedFileAttributeView);
            }
            TreeMap<String, Object> forReturn = new TreeMap<String, Object>();
            for (String supportedFileAttributeView : supportedFileAttributeViews) {
                Object attribute = new String("Not avalable");
                try{
                    //attribute = Files.getAttribute(file, supportedFileAttributeView, LinkOption.NOFOLLOW_LINKS);
                    attribute = Files.readAttributes(file, supportedFileAttributeView, LinkOption.NOFOLLOW_LINKS);
                }catch(Exception ex){
                    NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
                }
                forReturn.putIfAbsent(supportedFileAttributeView, attribute);
            }
            
            for (Map.Entry<String, Object> entry : forReturn.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                Class<?> aClass = value.getClass();
                String strOut = "[KEY]" + key + "[VALCLASS]" + aClass.getCanonicalName()
                        + "[VAl]" + value.toString();
                arrStr.add(strOut);
            }
            NcThWorkerUpGUITreeOutput.outputTreeAddChildren(lComp, arrStr);
        } catch (IOException ex) {
            NcAppHelper.logException(NcFsIdxFileVisitor.class.getCanonicalName(), ex);
        }
    }
}
