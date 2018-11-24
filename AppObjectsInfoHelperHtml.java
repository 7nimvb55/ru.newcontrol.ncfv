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

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppObjectsInfoHelperHtml {
    protected static void commandOutPutBusToHtml(
            ArrayBlockingQueue<ArrayList<String>> commandsOutPutBusData,
            ArrayBlockingQueue<String> listStringsForLogInRunnable){
        ArrayList<String> pollFirstEntryToLog;
        int indexedSwitch = 0;
        do{
            pollFirstEntryToLog = commandsOutPutBusData.poll();
            if( pollFirstEntryToLog != null ){
                String forOutPutToLog = "";
                if( pollFirstEntryToLog.size() > 1 ){
                    for( String element : pollFirstEntryToLog ){
                        if( indexedSwitch == 0 ){

                            indexedSwitch++;
                        }
                        if( indexedSwitch == 1 ){

                            indexedSwitch = 2;
                        }
                        if( indexedSwitch == 2 ){

                            indexedSwitch = 1;
                        }
                    }
                }
                if( pollFirstEntryToLog.size() == 1 ){
                    String forOutTimeStamp = pollFirstEntryToLog.get(0).length() == 17 
                            ? getFormatedTimeStamp(pollFirstEntryToLog.get(0))
                            : "";
                    if( forOutTimeStamp.isEmpty() ){
                        continue;
                    }
                    forOutPutToLog = "<h1>Time stamp: " + forOutTimeStamp + "</h1>";
                }
                if( pollFirstEntryToLog.size() == 0 ){
                    continue;
                }
                listStringsForLogInRunnable.add(forOutPutToLog);
            }
        }while( pollFirstEntryToLog != null );
    }
    
    protected static String getFormatedTimeStamp(String strForFormat){
        char[] bytesForStampFormat = strForFormat.toCharArray();
        char[] newStampFormat = {
            bytesForStampFormat[0],
            bytesForStampFormat[1],
            bytesForStampFormat[2],
            bytesForStampFormat[3],
            '-',
            bytesForStampFormat[4],
            bytesForStampFormat[5],
            '-',
            bytesForStampFormat[6],
            bytesForStampFormat[7],
            ' ',
            bytesForStampFormat[8],
            bytesForStampFormat[9],
            ':',
            bytesForStampFormat[10],
            bytesForStampFormat[11],
            ':',
            bytesForStampFormat[12],
            bytesForStampFormat[13],
            '.',
            bytesForStampFormat[14],
            bytesForStampFormat[15],
            bytesForStampFormat[16]
        };
        String strForReturn = new String(newStampFormat);
        return strForReturn;
    }
    protected static TreeMap<Integer, String> getStringListForSaveTableAddThead(String headString,TreeMap<Integer, String> listForLogStrs){
        TreeMap<Integer, String> withTheadLogStrs = new TreeMap<Integer, String>();
        int indexStrs = 0;
        withTheadLogStrs.put(indexStrs, "<TABLE>");
        indexStrs++;
        
        withTheadLogStrs.put(indexStrs, "<THEAD>");
        indexStrs++;
        withTheadLogStrs.put(indexStrs, "<TR><TH>" + headString + "</TH></TR>");
        indexStrs++;
        withTheadLogStrs.put(indexStrs, "</THEAD>");
        indexStrs++;
        
        withTheadLogStrs.put(indexStrs, "<TBODY>");
        indexStrs++;
        for( Map.Entry<Integer, String> lines: listForLogStrs.entrySet()){
            withTheadLogStrs.put(indexStrs, "<TR><TD>" + lines.getValue() + "</TD></TR>");
            indexStrs++;
        }
        withTheadLogStrs.put(indexStrs, "</TBODY>");
        indexStrs++;
        withTheadLogStrs.put(indexStrs, "</TABLE>");
        indexStrs++;
        return withTheadLogStrs;
    }
    
    protected static void getStringListForSaveTable(
            ArrayBlockingQueue<String> listForRunnableLogStrs,
            TreeMap<Integer, String> srcDataLogStrs,
            String runnedCmdStr){
        
        TreeMap<Integer, String> listForLogStrs = getStringListForSaveTableAddThead(runnedCmdStr, srcDataLogStrs);
        
        Map.Entry<Integer, String> pollFirstEntryToLog;
        do{
            pollFirstEntryToLog = listForLogStrs.pollFirstEntry();
            if( pollFirstEntryToLog != null ){
                listForRunnableLogStrs.add(pollFirstEntryToLog.getValue());
            }
        }while( pollFirstEntryToLog != null );
        
    }
    
    protected static ConcurrentSkipListMap<Integer, String> getLinesForTopSaveIndex(
            ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs,
            Path fileJsMenuPrefix,
            Path fileCssPrefix
    ){
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<html lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-US\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<title>Log report for created Thread Object</title>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<script src=\"./js/" + fileJsMenuPrefix.toString() + "\" type=\"text/javascript\" defer=\"YES\"></script>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<link rel=\"stylesheet\" href=\"./css/" + fileCssPrefix.toString() + "\" type=\"text/css\"></link>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"</head>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<body class=\"body\" onload=\"allClose()\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"header-content\" class=\"content-header\">header page Report for threads state");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"menu-content\" class=\"content-menu-items\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <ul id=\"menu\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 1</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 2</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 3</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 4</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"page-content\" class=\"content-imported-page\">");
        
        return listForRunnableLogStrs;
    }
    protected static void getLinesForBottomSaveIndex(
            ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs
    ){
        
        int strIndex = listForRunnableLogStrs.lastKey();
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"footer-content\" class=\"footer-page\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            footer of page report");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    </body>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"</html>");
        
    }
    
    protected static ConcurrentSkipListMap<Integer, String> getLinesForSaveIndex(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<html lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-US\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<title>Log report for created Thread Object</title>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<script src=\"./js/menu.js\" type=\"text/javascript\" defer=\"YES\"></script>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<link rel=\"stylesheet\" href=\"./css/report.css\" type=\"text/css\"></link>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<link rel=\"import\" href=\"table-20181115100212827.html\"></link>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"</head>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"<body class=\"body\" onload=\"allClose()\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"header-content\" class=\"content-header\">Лось жывотное коварное");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"menu-content\" class=\"content-menu-items\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <ul id=\"menu\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 1</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 2</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 3</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 4</a>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                <ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 1</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 2</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 3</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 4</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 5</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 6</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"                  <li><a href=\"#\">sub menu 7</a></li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"               </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            </li>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </ul>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"page-content\" class=\"content-imported-page\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <div id=\"item-content1\"><iframe id=\"datatable\" src=\"./table-20181115100212827.html\" class=\"frame-table\" width=\"70%\"></iframe></div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            <div id=\"item-content2\"><iframe id=\"datatable\" src=\"./table-20181115100212944.html\" class=\"frame-table\" width=\"70%\"></iframe></div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        <div id=\"footer-content\" class=\"footer-page\">");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"            footer of page report");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"        </div>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    </body>");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"</html>");
        return listForRunnableLogStrs;
    }
    
    
    
    protected static ConcurrentSkipListMap<Integer, String> getLinesForSaveCss(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,".body{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background-color: #666666;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    text-align: center;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"frame-table{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border: 1px solid #d6e9c6;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#header-content{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #FF8000;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding: 24px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border-bottom: 3px solid #B5B5B5;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    min-width: 355px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#page-content{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    align-content: flex-end;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    text-decoration: underline;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    height:500px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding: 29px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #888888;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    min-width: 355px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    overflow: auto;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#item-content-1{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    height:250px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin-right: 350px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #f6cf65;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    display: inline-block;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#item-content-2{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    height:250px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin-right: 350px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #f6cf65;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    display: inline-block;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu-content{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    height: 500px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    width: 300px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    float: left;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    overflow: auto;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background:#80FF00;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    width:280px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-type:none;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin:0");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu li{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border-bottom:1px solid #FFFFFF;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding:3px");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu li a{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    color:#000000;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    font-family:verdana,arial,sans-serif;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    text-decoration:none");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu li ul{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border-top:1px solid #FFFFFF;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    margin:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-type:square;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-position:inside");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#menu li ul li{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    border:0;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-type:square;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    color:#FFFFFF;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    list-style-position:inside");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"#footer-content{");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    background: #FF8000;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    padding: 11px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"    min-width: 355px;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        return listForRunnableLogStrs;
    }
    protected static ConcurrentSkipListMap<Integer, String> getLinesForSaveJsMenu(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"function openMenu(node){");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"var subMenu = node.parentNode.getElementsByTagName(\"ul\")[0];");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"subMenu.style.display === \"none\" ? subMenu.style.display = \"block\" : subMenu.style.display = \"none\";");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"function allClose(){");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"var list = document.getElementById(\"menu\").getElementsByTagName(\"ul\");");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"for(var i=0;i<list.length;i++){");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"	list[i].style.display = \"none\";");
	strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        return listForRunnableLogStrs;
    }
    protected static ConcurrentSkipListMap<Integer, String> getLinesForSaveJsLoadHtml(){
        ConcurrentSkipListMap<Integer, String> listForRunnableLogStrs = new ConcurrentSkipListMap<Integer, String>();
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"function importTable20181115100212944(){");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"var link = document.createElement('link');");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"link.rel = 'import';");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"link.href = 'table-20181115100212944.html';");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"link.onload = function(this.e){console.log('Loaded import: ' + e.target.href);};");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"link.onerror = function(this.e){console.log('Error loading import: ' + e.target.href);};");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"function importTable001(){");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"var content = document.querySelector('link[rel=\"import\"]').import;");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"alert(content);");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"document.body.appendChild(content.cloneNode(true));");
        strIndex++;
        listForRunnableLogStrs.put(strIndex,"}");
        return listForRunnableLogStrs;
    }
}
