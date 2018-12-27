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

import java.io.FilePermission;
import java.lang.reflect.ReflectPermission;
import java.nio.file.Path;
import java.security.Permissions;
import java.security.Policy;
import java.util.PropertyPermission;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppEtcSecurityHelper {
    protected static void createNewSecurity(){
        //java.io.FilePermission
        //java.net.SocketPermission
        //java.net.NetPermission
        //java.security.SecurityPermission
        //java.lang.RuntimePermission
        //java.util.PropertyPermission
        //java.awt.AWTPermission
        //java.lang.reflect.ReflectPermission
        //java.io.SerializablePermission
        
        Permissions permissions = new Permissions();
        
        Path appPath = AppFileOperationsSimple.getAppRWEDCheckedPath();
        
        permissions.add(new FilePermission(appPath.toString(), "read, write"));
        permissions.add(new FilePermission(appPath.toString() + "/-", "read, write"));
        
        Path userHomePath = AppFileOperationsSimple.getUserHomeRWEDCheckedPath();
        
        permissions.add(new FilePermission(userHomePath.toString(), "read, write"));
        permissions.add(new FilePermission(userHomePath.toString() + "/-", "read, write"));
        
        permissions.add(new ReflectPermission("suppressAccessChecks", "read"));
        
        for (String namesKey : System.getProperties().stringPropertyNames()) {
            if( namesKey.isEmpty() ){
                continue;
            }
            permissions.add(new PropertyPermission(namesKey, "read"));
        }
        
        /*permissions.add(new PropertyPermission("*", "read"));
        permissions.add(new PropertyPermission("line.separator", "read"));
        permissions.add(new PropertyPermission("java.class.path", "read"));*/
        
        permissions.add(new RuntimePermission("getenv.*", "read"));
        
        permissions.add(new RuntimePermission("getStackTrace", "read"));
        permissions.add(new RuntimePermission("modifyThreadGroup", "read"));
        permissions.add(new RuntimePermission("modifyThread", "read"));
        permissions.add(new RuntimePermission("accessDeclaredMembers", "read"));
        permissions.add(new RuntimePermission("fileSystemProvider", "read"));
        permissions.add(new RuntimePermission("fileSystemProvider", "write"));
        
        Policy.setPolicy(new AppEtcSecurityPolicy(permissions));
        System.setSecurityManager(new AppEtcSecurityManager());
    }
}
