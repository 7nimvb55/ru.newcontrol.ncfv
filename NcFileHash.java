/*
 *  Copyright 2017 Administrator of development departament newcontrol.ru .
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package ru.newcontrol.ncfv;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
/**
 *
 * @author Администратор
 * 
 */
//use in PrintFileHashesToConsole in PrintFileHashes.java 
public enum NcFileHash {

    /**
     *
     */
    MD5("MD5"),

    /**
     *
     */
    SHA1("SHA1"),

    /**
     *
     */
    SHA256("SHA-256"),

    /**
     *
     */
    SHA512("SHA-512");

    private String name;

    NcFileHash(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    protected String getName() {
        return name;
    }

    /**
     *
     * @param input
     * @return
     */
    protected byte[] checksum(File input) {
        try (InputStream in = new FileInputStream(input)) {
            MessageDigest digest = MessageDigest.getInstance(getName());
            byte[] block = new byte[4096];
            int length;
            while ((length = in.read(block)) > 0) {
                digest.update(block, 0, length);
            }
            return digest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
}
