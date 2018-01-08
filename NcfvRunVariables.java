/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcfvRunVariables {
    private static boolean devStage = true;
    private static boolean outWithTrace = false;
    private static boolean outWithPrintFunc = false;
    private static boolean outWithFileName = false;
    public static boolean getIncludeFile(){
        return outWithFileName;
    }
    public static boolean getTraceWithPrintFunc(){
        return outWithPrintFunc;
    }
    public static boolean getWithTrace(){
        return outWithTrace;
    }
    public static boolean getStage(){
        return devStage;
    }
}
