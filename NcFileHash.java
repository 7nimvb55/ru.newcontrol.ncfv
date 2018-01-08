/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    public String getName() {
        return name;
    }

    /**
     *
     * @param input
     * @return
     */
    public byte[] checksum(File input) {
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
