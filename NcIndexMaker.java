/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
/**
 *
 * @author Администратор
 */
public class NcIndexMaker {
    private File currentFile;

    /**
     *
     * @param ncFile
     */
    public NcIndexMaker(File ncFile) {
        currentFile = ncFile;
    }

    /**
     *
     * @param fncFile
     * @return
     */
    public String[] getFilterStringsArray(File fncFile){
        NcIndexPreProcessFiles ncIdxPreReturn = new NcIndexPreProcessFiles(fncFile);

        String[] toRet = new String[0];

        try {
            toRet = ncIdxPreReturn.getFileDataToSwing(fncFile);
        } catch (IOException ex) {
            Logger.getLogger(NcIndexMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toRet;
    }
    
    /**
     *
     * @param fncFile
     * @return
     */
    public String[] getMakeIndexForFile(File fncFile){
        NcIndexPreProcessFiles ncIdxPreReturn = new NcIndexPreProcessFiles(fncFile);

        String[] toRet = new String[0];

        try {
            toRet = ncIdxPreReturn.getResultMakeIndex(fncFile);
        } catch (IOException ex) {
            Logger.getLogger(NcIndexMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return toRet;
    }
}
