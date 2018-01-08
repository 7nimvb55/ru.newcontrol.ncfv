/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

/**
 * inClassId - in TreeMap structure for change data betveen classes
 * toFileId - Id from Directory List File
 * strSubString - returned by one of NcPathToArrListStr.(*NCLVLABC*).retStr
 * or NcPathToArrListStr.NCLVLNUM.retArrListStr method from class NcPathToArrListStr
 * hexSubString - returned by toStrUTFinHEX()
 * @author Администратор
 */
public class NcDcIdxSubStringToOperationUse {
    /** inClassId - in TreeMap structure for change data betveen classes */
    public long inClassId;
    /** toFileId - Id from Directory List File */
    public long toFileId;
    /** 
     * strSubString - returned by one of NcPathToArrListStr.(*NCLVLABC*).retStr
     * or NcPathToArrListStr.NCLVLNUM.retArrListStr method from class NcPathToArrListStr
     */
    public String strSubString;

    /**
     *
     */
    public int strSubStringHash;
    /** hexSubString - returned by toStrUTFinHEX() */
    public String hexSubString;
    /** h - strSubString.hashCode() */
    public int hexSubStringHash;
    /** p - (position) subString position in source str */
    public int positionSubString;
    /** l - (length) subString length */
    public int lengthSubString;

    /**
     *
     */
    public long recordTime;

    /**
     *
     */
    public int recordHash;

    /**
     *
     */
    public NcDcIdxSubStringToOperationUse() {
        this.inClassId = -777;
        this.toFileId = -777;
        this.strSubString = "";
        this.strSubStringHash = -777;
        this.hexSubString = "";
        this.hexSubStringHash = -777;
        this.positionSubString = -777;
        this.lengthSubString = -777;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.inClassId
                + this.toFileId
                + this.strSubString
                + this.strSubStringHash
                + this.hexSubString
                + this.hexSubStringHash
                + this.positionSubString
                + this.lengthSubString
                + nowSysTime).hashCode();
    }

    /**
     *
     * @param inClassId
     * @param toFileId
     * @param strSubString
     * @param hexSubString
     * @param positionSubString
     * @param lengthSubString
     */
    public NcDcIdxSubStringToOperationUse(long inClassId,
            long toFileId,
            String strSubString,
            String hexSubString,
            int positionSubString,
            int lengthSubString) {
        this.inClassId = inClassId;
        this.toFileId = toFileId;
        this.strSubString = strSubString;
        this.strSubStringHash = strSubString.hashCode();
        this.hexSubString = hexSubString;
        this.hexSubStringHash = hexSubString.hashCode();
        this.positionSubString = positionSubString;
        this.lengthSubString = lengthSubString;
        long nowSysTime = System.nanoTime();
        this.recordTime = nowSysTime;
        this.recordHash = (
                ""
                + this.inClassId
                + this.toFileId
                + this.strSubString
                + this.strSubStringHash
                + this.hexSubString
                + this.hexSubStringHash
                + this.positionSubString
                + this.lengthSubString
                + nowSysTime).hashCode();
    }
    

}
