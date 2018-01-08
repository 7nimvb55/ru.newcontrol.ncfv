/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.newcontrol.ncfv;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author wladimirowichbiaran
 */
public class NcEtcKeyWordListManage {
    public static ArrayList<String> getKeyWordInSearchFromFile(){
        NcParamFv readedWorkCfg = NcParamFvReader.readDataFromWorkCfg();
        ArrayList<String> strForReturn;
        strForReturn = new ArrayList<String>();
        try(BufferedReader br = new BufferedReader(new FileReader(readedWorkCfg.keywordsInSearch)))
        {
            String s;
            while((s=br.readLine())!=null){
                strForReturn.add(s.trim());
            }
        }
         catch(IOException ex){
            NcAppHelper.outMessage(ex.getMessage());
        }   
        return strForReturn;
    }
    public static ArrayList<String> getKeyWordOutSearchFromFile(){
        NcParamFv readedWorkCfg = NcParamFvReader.readDataFromWorkCfg();
        ArrayList<String> strForReturn;
        strForReturn = new ArrayList<String>();
        try(BufferedReader br = new BufferedReader(new FileReader(readedWorkCfg.keywordsOutOfSearch)))
        {
            String s;
            while((s=br.readLine())!=null){
                strForReturn.add(s.trim());
            }
        }
         catch(IOException ex){
            NcAppHelper.outMessage(ex.getMessage());
        }   
        return strForReturn;
    }
}
