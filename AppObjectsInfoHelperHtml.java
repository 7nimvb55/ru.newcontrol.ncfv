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
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppObjectsInfoHelperHtml {
    protected static ArrayList<String> commandsOutPutToHtml(ArrayList<String> listCommandOutPut){
        ArrayList<String> listStringsForLogInRunnable = new ArrayList<String>();
        int indexedSwitch = 0;
        if( (listCommandOutPut != null)
                && (listCommandOutPut.size() > 0 ) ){
                String forOutPutToLog = "";
                if( listCommandOutPut.size() > 1 ){
                    String forCmdResultOut = "<TBODY>";
                    listStringsForLogInRunnable.add("<TABLE>");
                    for( String element : listCommandOutPut ){
                        if( indexedSwitch == 0 ){
                            String forOutTimeStamp = "";
                            try{
                            forOutTimeStamp = listCommandOutPut.get(0).length() == 17 
                                ? getFormatedTimeStamp(listCommandOutPut.get(0))
                                : "";
                            } catch (NoSuchElementException ex){
                                ex.printStackTrace();
                            }
                            if( !forOutTimeStamp.isEmpty() ){
                                listStringsForLogInRunnable.add("<THEAD>");
                                forOutPutToLog = "<TR><TH>Time stamp</TH><TH>" + forOutTimeStamp + "</TH></TR>";
                                listStringsForLogInRunnable.add(forOutPutToLog);
                                forOutPutToLog = "<TR><TH>Command</TH><TH>Result</TH></TR>";
                                listStringsForLogInRunnable.add(forOutPutToLog);
                                listStringsForLogInRunnable.add("</THEAD>");
                                forOutPutToLog = "";
                            }
                            indexedSwitch = 1;
                            continue;
                        }
                        if( indexedSwitch == 2 ){
                            listStringsForLogInRunnable.add(forCmdResultOut.concat("<TD>" + element + "</TD>") + "</TR>");
                            forCmdResultOut = "";
                            indexedSwitch = 1;
                            continue;
                        }
                        if( indexedSwitch == 1 ){
                            forCmdResultOut = forCmdResultOut.concat("<TR><TD>" + element + "</TD>");
                            indexedSwitch = 2;
                            continue;
                        }
                    }
                    listStringsForLogInRunnable.add("</TBODY>");
                    listStringsForLogInRunnable.add("</TABLE>");
                }
                if( listCommandOutPut.size() == 1 ){
                    String forOutTimeStamp = "";
                    try{
                    forOutTimeStamp = listCommandOutPut.get(0).length() == 17 
                        ? getFormatedTimeStamp(listCommandOutPut.get(0))
                        : "";
                    } catch (NoSuchElementException ex){
                        ex.printStackTrace();
                    }
                    if( !forOutTimeStamp.isEmpty() ){
                        forOutPutToLog = "<h1>Time stamp: " + forOutTimeStamp + "</h1>";
                        listStringsForLogInRunnable.add(forOutPutToLog);
                    }
                }
            }
        return listStringsForLogInRunnable;
    }
    protected static ArrayBlockingQueue<String> commandOutPutToHtmlBus(ArrayList<String> listCommandOutPut){
        ArrayList<String> listStringsForLogInRunnable = new ArrayList<String>();
        int indexedSwitch = 0;
        if( (listCommandOutPut != null)
                && (listCommandOutPut.size() > 0 ) ){
                String forOutPutToLog = "";
                if( listCommandOutPut.size() > 1 ){
                    String forCmdResultOut = "<TBODY>";
                    listStringsForLogInRunnable.add("<TABLE>");
                    for( String element : listCommandOutPut ){
                        if( indexedSwitch == 0 ){
                            String forOutTimeStamp = "";
                            try{
                            forOutTimeStamp = listCommandOutPut.get(0).length() == 17 
                                ? getFormatedTimeStamp(listCommandOutPut.get(0))
                                : "";
                            } catch (NoSuchElementException ex){
                                ex.printStackTrace();
                            }
                            if( !forOutTimeStamp.isEmpty() ){
                                listStringsForLogInRunnable.add("<THEAD>");
                                forOutPutToLog = "<TR><TH>Time stamp</TH><TH>" + forOutTimeStamp + "</TH></TR>";
                                listStringsForLogInRunnable.add(forOutPutToLog);
                                forOutPutToLog = "<TR><TH>Command</TH><TH>Result</TH></TR>";
                                listStringsForLogInRunnable.add(forOutPutToLog);
                                listStringsForLogInRunnable.add("</THEAD>");
                                forOutPutToLog = "";
                            }
                            indexedSwitch = 1;
                            continue;
                        }
                        if( indexedSwitch == 2 ){
                            listStringsForLogInRunnable.add(forCmdResultOut.concat("<TD>" + element + "</TD>") + "</TR>");
                            forCmdResultOut = "";
                            indexedSwitch = 1;
                            continue;
                        }
                        if( indexedSwitch == 1 ){
                            forCmdResultOut = forCmdResultOut.concat("<TR><TD>" + element + "</TD>");
                            indexedSwitch = 2;
                            continue;
                        }
                    }
                    listStringsForLogInRunnable.add("</TBODY>");
                    listStringsForLogInRunnable.add("</TABLE>");
                }
                if( listCommandOutPut.size() == 1 ){
                    String forOutTimeStamp = "";
                    try{
                    forOutTimeStamp = listCommandOutPut.get(0).length() == 17 
                        ? getFormatedTimeStamp(listCommandOutPut.get(0))
                        : "";
                    } catch (NoSuchElementException ex){
                        ex.printStackTrace();
                    }
                    if( !forOutTimeStamp.isEmpty() ){
                        forOutPutToLog = "<h1>Time stamp: " + forOutTimeStamp + "</h1>";
                        listStringsForLogInRunnable.add(forOutPutToLog);
                    }
                }
            }
        return AppObjectsBusHelper.cleanBusFromArray(listStringsForLogInRunnable);
    }
    /**
     * @deprecated 
     * @param commandsOutPutBusData
     * @param listStringsForLogInRunnable 
     */
    protected static void commandOutPutBusToHtml(
            ArrayBlockingQueue<ArrayBlockingQueue<String>> commandsOutPutBusData,
            ArrayBlockingQueue<String> listStringsForLogInRunnable){
        ArrayBlockingQueue<String> pollFirstEntryToLog;
        int indexedSwitch = 0;
        do{
            pollFirstEntryToLog = commandsOutPutBusData.poll();
            if( pollFirstEntryToLog != null ){
                String forOutPutToLog = "";
                if( pollFirstEntryToLog.size() > 1 ){
                    String forCmdResultOut = "<TBODY>";
                    listStringsForLogInRunnable.add("<TABLE>");
                    for( String element : pollFirstEntryToLog ){
                        if( indexedSwitch == 0 ){
                            String forOutTimeStamp = "";
                            try{
                            forOutTimeStamp = pollFirstEntryToLog.element().length() == 17 
                                ? getFormatedTimeStamp(pollFirstEntryToLog.element())
                                : "";
                            } catch (NoSuchElementException ex){
                                ex.printStackTrace();
                            }
                            if( !forOutTimeStamp.isEmpty() ){
                                listStringsForLogInRunnable.add("<THEAD>");
                                forOutPutToLog = "<TR><TH>Time stamp</TH><TH>" + forOutTimeStamp + "</TH></TR>";
                                listStringsForLogInRunnable.add(forOutPutToLog);
                                forOutPutToLog = "<TR><TH>Command</TH><TH>Result</TH></TR>";
                                listStringsForLogInRunnable.add(forOutPutToLog);
                                listStringsForLogInRunnable.add("</THEAD>");
                                forOutPutToLog = "";
                            }
                            indexedSwitch = 1;
                            continue;
                        }
                        if( indexedSwitch == 2 ){
                            listStringsForLogInRunnable.add(forCmdResultOut.concat("<TD>" + element + "</TD>") + "</TR>");
                            forCmdResultOut = "";
                            indexedSwitch = 1;
                            continue;
                        }
                        if( indexedSwitch == 1 ){
                            forCmdResultOut = forCmdResultOut.concat("<TR><TD>" + element + "</TD>");
                            indexedSwitch = 2;
                            continue;
                        }
                    }
                    listStringsForLogInRunnable.add("</TBODY>");
                    listStringsForLogInRunnable.add("</TABLE>");
                }
                if( pollFirstEntryToLog.size() == 1 ){
                    String forOutTimeStamp = "";
                    try{
                    forOutTimeStamp = pollFirstEntryToLog.element().length() == 17 
                        ? getFormatedTimeStamp(pollFirstEntryToLog.element())
                        : "";
                    } catch (NoSuchElementException ex){
                        ex.printStackTrace();
                    }
                    if( !forOutTimeStamp.isEmpty() ){
                        forOutPutToLog = "<h1>Time stamp: " + forOutTimeStamp + "</h1>";
                        listStringsForLogInRunnable.add(forOutPutToLog);
                    }
                }
            }
        }while( !commandsOutPutBusData.isEmpty() );
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
    protected static ArrayBlockingQueue<String> createLinesForIndex(
            ArrayBlockingQueue<ArrayBlockingQueue<String>> readedStringFormFile,
            Path fileJsMenuPrefix,
            Path fileCssPrefix,
            ArrayList<Path> filesByMaskFromDir
    ){
        ArrayList<String> linesForSave = new ArrayList<String>();
        linesForSave.add("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        linesForSave.add("<html lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-US\">");
        linesForSave.add("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        linesForSave.add("<title>Log report for created Thread Object</title>");
        linesForSave.add("<script src=\"./js/" + fileJsMenuPrefix.toString() + "\" type=\"text/javascript\" defer=\"YES\"></script>");
        linesForSave.add("<link rel=\"stylesheet\" href=\"./css/" + fileCssPrefix.toString() + "\" type=\"text/css\"></link>");
        linesForSave.add("</head>");
        linesForSave.add("<body class=\"body\" onload=\"allClose()\">");
        linesForSave.add("        <div id=\"header-content\" class=\"content-header\">");
        linesForSave.add("                <button id=\"buttonLime\" onclick=\"highlightLime()\">Lime</button>");
        linesForSave.add("                <input type=\"text\" name=\"enter\" class=\"enter\" value=\"\" id=\"limeTextElement\"/>");
        linesForSave.add("                <button id=\"buttonYellow\" onclick=\"highlightYellow()\">Yellow</button>");
        linesForSave.add("                <input type=\"text\" name=\"enter\" class=\"enter\" value=\"\" id=\"yellowTextElement\"/>");
        linesForSave.add("                <button id=\"buttonRed\" onclick=\"highlightRed()\">Red</button>");
        linesForSave.add("                <input type=\"text\" name=\"enter\" class=\"enter\" value=\"\" id=\"redTextElement\"/>");
        linesForSave.add("                <button id=\"buttonCyan\" onclick=\"highlightCyan()\">Cyan</button>");
        linesForSave.add("                <input type=\"text\" name=\"enter\" class=\"enter\" value=\"\" id=\"cyanTextElement\"/>");
        linesForSave.add("        </div>");
        linesForSave.add("        <div id=\"menu-content\" class=\"content-menu-items\">");
        linesForSave.add("        <ul id=\"menu\">");
        
        linesForSave.addAll(AppObjectsInfoHelperHtml.createMenuItems(filesByMaskFromDir));
        
        linesForSave.add("        </ul>");
        linesForSave.add("        </div>");
        linesForSave.add("        <div id=\"page-content\" class=\"content-imported-page\">");
        
        linesForSave.addAll(addReadedLinesFromFilesSortByArrayFromDir(readedStringFormFile, filesByMaskFromDir));
        
        linesForSave.add("        </div>");
        linesForSave.add("        <div id=\"footer-content\" class=\"footer-page\">");
        linesForSave.add("            footer of page report");
        linesForSave.add("        </div>");
        linesForSave.add("    </body>");
        linesForSave.add("</html>");
        
        return AppObjectsBusHelper.cleanBusFromArray(linesForSave);
    }
    protected static ArrayList<String> addReadedLinesFromFilesSortByArrayFromDir(
            ArrayBlockingQueue<ArrayBlockingQueue<String>> readedStringFormFile,
            ArrayList<Path> filesByMaskFromDir
        ){
        ArrayList<String> linesForReturn = new ArrayList<String>();
        TreeMap<Integer, ArrayBlockingQueue<String>> sortedArray = new TreeMap<Integer, ArrayBlockingQueue<String>>();
        Integer indexOnPage = 0;
        do{
            ArrayBlockingQueue<String> pollReadedArray = readedStringFormFile.poll();
            if( pollReadedArray != null ){
                
                for( Path fileElement : filesByMaskFromDir ){
                    indexOnPage++;
                    String forBotomOfTableAncor = "";
                    if( fileElement.toString().compareTo(pollReadedArray.element()) == 0 ){
                        String pollString = pollReadedArray.poll();
                        if( pollString != null ){
                            String strForAncor = fileElement.getFileName().toString().split("\\.")[0];
                            forBotomOfTableAncor = "                  <li><a href=\"#" 
                                + strForAncor 
                                + "\">goto top of table</a></li>";
                            pollReadedArray.add(forBotomOfTableAncor);
                            if( sortedArray.containsKey(indexOnPage) ){
                                indexOnPage++;
                            }
                            sortedArray.put(indexOnPage, pollReadedArray);
                        }
                        
                    }
                }
            }
        } while( !readedStringFormFile.isEmpty() );
        
        for( Map.Entry<Integer, ArrayBlockingQueue<String>> elementSortedArray : sortedArray.entrySet() ){
            do{
                String pollReadedString = elementSortedArray.getValue().poll();
                if( pollReadedString != null ){
                    linesForReturn.add(pollReadedString);
                }
            } while( !elementSortedArray.getValue().isEmpty() );
        }
        
        return linesForReturn;
    }
    protected static void getLinesForTopSaveIndex(
            ArrayBlockingQueue<String> listForRunnableLogStrs,
            Path fileJsMenuPrefix,
            Path fileCssPrefix,
            ArrayList<Path> filesByMaskFromDir
    ){
        listForRunnableLogStrs.add("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        listForRunnableLogStrs.add("<html lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-US\">");
        listForRunnableLogStrs.add("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        listForRunnableLogStrs.add("<title>Log report for created Thread Object</title>");
        listForRunnableLogStrs.add("<script src=\"./js/" + fileJsMenuPrefix.toString() + "\" type=\"text/javascript\" defer=\"YES\"></script>");
        listForRunnableLogStrs.add("<link rel=\"stylesheet\" href=\"./css/" + fileCssPrefix.toString() + "\" type=\"text/css\"></link>");
        listForRunnableLogStrs.add("</head>");
        listForRunnableLogStrs.add("<body class=\"body\" onload=\"allClose()\">");
        listForRunnableLogStrs.add("        <div id=\"header-content\" class=\"content-header\">header page Report for threads state");
        listForRunnableLogStrs.add("        </div>");
        listForRunnableLogStrs.add("        <div id=\"menu-content\" class=\"content-menu-items\">");
        listForRunnableLogStrs.add("        <ul id=\"menu\">");
        AppObjectsInfoHelperHtml.getMenuItems(listForRunnableLogStrs, filesByMaskFromDir);
        /*listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 1</a>");
        listForRunnableLogStrs.add("                <ul>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 1</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
        listForRunnableLogStrs.add("               </ul>");
        listForRunnableLogStrs.add("            </li>");
        listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 2</a>");
        listForRunnableLogStrs.add("                <ul>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 1</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
        listForRunnableLogStrs.add("               </ul>");
        listForRunnableLogStrs.add("            </li>");
        listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 3</a>");
        listForRunnableLogStrs.add("                <ul>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 1</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
        listForRunnableLogStrs.add("               </ul>");
        listForRunnableLogStrs.add("            </li>");
        listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 4</a>");
        listForRunnableLogStrs.add("                <ul>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 1</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
        listForRunnableLogStrs.add("               </ul>");
        listForRunnableLogStrs.add("            </li>");*/
        listForRunnableLogStrs.add("        </ul>");
        listForRunnableLogStrs.add("        </div>");
        listForRunnableLogStrs.add("        <div id=\"page-content\" class=\"content-imported-page\">");
    }
    protected static ArrayList<String> createMenuItems(
            ArrayList<Path> filesByMaskFromDir){
        ArrayList<String> listForReturn = new ArrayList<String>();
        for( Path fileForRead : filesByMaskFromDir ){
            String strForAncor = fileForRead.getFileName().toString().split("\\.")[0];
            String strForMenuTitle = strForAncor.split("-")[1];
            
            listForReturn.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">" + strForMenuTitle + "</a>");
            listForReturn.add("                <ul>");
            listForReturn.add("                  <li><a href=\"#" 
                + strForAncor 
                + "\">goto table</a></li>");
            listForReturn.add("                  <li><a href=\"#\">sub menu 2</a></li>");
            listForReturn.add("                  <li><a href=\"#\">sub menu 3</a></li>");
            listForReturn.add("                  <li><a href=\"#\">sub menu 4</a></li>");
            listForReturn.add("                  <li><a href=\"#\">sub menu 5</a></li>");
            listForReturn.add("                  <li><a href=\"#\">sub menu 6</a></li>");
            listForReturn.add("                  <li><a href=\"#\">sub menu 7</a></li>");
            listForReturn.add("               </ul>");
            listForReturn.add("            </li>");
        }
        return listForReturn;
    }
    protected static void getMenuItems(
            ArrayBlockingQueue<String> listForRunnableLogStrs, 
            ArrayList<Path> filesByMaskFromDir){
        
        for( Path fileForRead : filesByMaskFromDir ){
            String strForAncor = fileForRead.getFileName().toString().split("\\.")[0];
            String strForMenuTitle = strForAncor.split("-")[1];
            
            listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">" + strForMenuTitle + "</a>");
            listForRunnableLogStrs.add("                <ul>");
            listForRunnableLogStrs.add("                  <li><a href=\"#" 
                + strForAncor 
                + "\">goto table</a></li>");
            listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
            listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
            listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
            listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
            listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
            listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
            listForRunnableLogStrs.add("               </ul>");
            listForRunnableLogStrs.add("            </li>");
        }
    }
    protected static void getLinesForBottomSaveIndex(
            ArrayBlockingQueue<String> listForRunnableLogStrs
    ){
        listForRunnableLogStrs.add("        </div>");
        listForRunnableLogStrs.add("        <div id=\"footer-content\" class=\"footer-page\">");
        listForRunnableLogStrs.add("            footer of page report");
        listForRunnableLogStrs.add("        </div>");
        listForRunnableLogStrs.add("    </body>");
        listForRunnableLogStrs.add("</html>");
    }
    
    protected static ArrayBlockingQueue<String> getLinesForSaveIndex(){
        Integer messagesQueueSize = 1000;
        ArrayBlockingQueue<String> listForRunnableLogStrs = new ArrayBlockingQueue<String>(messagesQueueSize);
        listForRunnableLogStrs.add("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        listForRunnableLogStrs.add("<html lang=\"en-US\" xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en-US\">");
        listForRunnableLogStrs.add("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>");
        listForRunnableLogStrs.add("<title>Log report for created Thread Object</title>");
        listForRunnableLogStrs.add("<script src=\"./js/menu.js\" type=\"text/javascript\" defer=\"YES\"></script>");
        listForRunnableLogStrs.add("<link rel=\"stylesheet\" href=\"./css/report.css\" type=\"text/css\"></link>");
        listForRunnableLogStrs.add("<link rel=\"import\" href=\"table-20181115100212827.html\"></link>");
        listForRunnableLogStrs.add("</head>");
        listForRunnableLogStrs.add("<body class=\"body\" onload=\"allClose()\">");
        listForRunnableLogStrs.add("        <div id=\"header-content\" class=\"content-header\">Лось жывотное коварное");
        listForRunnableLogStrs.add("        </div>");
        listForRunnableLogStrs.add("        <div id=\"menu-content\" class=\"content-menu-items\">");
        listForRunnableLogStrs.add("        <ul id=\"menu\">");
        listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 1</a>");
        listForRunnableLogStrs.add("                <ul>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 1</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
        listForRunnableLogStrs.add("               </ul>");
        listForRunnableLogStrs.add("            </li>");
        listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 2</a>");
        listForRunnableLogStrs.add("                <ul>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 1</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
        listForRunnableLogStrs.add("               </ul>");
        listForRunnableLogStrs.add("            </li>");
        listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 3</a>");
        listForRunnableLogStrs.add("                <ul>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 1</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
        listForRunnableLogStrs.add("               </ul>");
        listForRunnableLogStrs.add("            </li>");
        listForRunnableLogStrs.add("            <li><a href=\"#\" onclick=\"openMenu(this);return false\">menu 4</a>");
        listForRunnableLogStrs.add("                <ul>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 1</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 2</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 3</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 4</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 5</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 6</a></li>");
        listForRunnableLogStrs.add("                  <li><a href=\"#\">sub menu 7</a></li>");
        listForRunnableLogStrs.add("               </ul>");
        listForRunnableLogStrs.add("            </li>");
        listForRunnableLogStrs.add("        </ul>");
        listForRunnableLogStrs.add("        </div>");
        listForRunnableLogStrs.add("        <div id=\"page-content\" class=\"content-imported-page\">");
        listForRunnableLogStrs.add("            <div id=\"item-content1\"><iframe id=\"datatable\" src=\"./table-20181115100212827.html\" class=\"frame-table\" width=\"70%\"></iframe></div>");
        listForRunnableLogStrs.add("            <div id=\"item-content2\"><iframe id=\"datatable\" src=\"./table-20181115100212944.html\" class=\"frame-table\" width=\"70%\"></iframe></div>");
        listForRunnableLogStrs.add("        </div>");
        listForRunnableLogStrs.add("        <div id=\"footer-content\" class=\"footer-page\">");
        listForRunnableLogStrs.add("            footer of page report");
        listForRunnableLogStrs.add("        </div>");
        listForRunnableLogStrs.add("    </body>");
        listForRunnableLogStrs.add("</html>");
        return listForRunnableLogStrs;
    }
    
    
    
    protected static ArrayBlockingQueue<String> getLinesForSaveCss(){
        //@todo fix for queue size exception of full
        Integer messagesQueueSize = 1000;
        ArrayBlockingQueue<String> listForRunnableLogStrs = new ArrayBlockingQueue<String>(messagesQueueSize);
        listForRunnableLogStrs.add(".body{");
        listForRunnableLogStrs.add("    padding:0;");
        listForRunnableLogStrs.add("    margin:0;");
        listForRunnableLogStrs.add("    background-color: #666666;");
        listForRunnableLogStrs.add("    text-align: center;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("frame-table{");
        listForRunnableLogStrs.add("    border: 1px solid #d6e9c6;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table {");
        listForRunnableLogStrs.add("    font-family: verdana,arial,sans-serif;");
        listForRunnableLogStrs.add("    border: 1px solid #999999;");
        listForRunnableLogStrs.add("    width: 350px;");
        listForRunnableLogStrs.add("    height: 200px;");
        listForRunnableLogStrs.add("    text-align: start;");
        listForRunnableLogStrs.add("    border-collapse: collapse;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table td, table th {");
        listForRunnableLogStrs.add("    border: 1px solid #999999;");
        listForRunnableLogStrs.add("    padding: 3px 2px;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table tbody td {");
        listForRunnableLogStrs.add("    font-size: 13px;");
        listForRunnableLogStrs.add("    color: #000000;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table tr:nth-child(even) {");
        listForRunnableLogStrs.add("    background: #666666;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table thead {");
        listForRunnableLogStrs.add("    background: #444444;");
        listForRunnableLogStrs.add("    border-bottom: 3px solid #999999;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table thead th {");
        listForRunnableLogStrs.add("    font-size: 17px;");
        listForRunnableLogStrs.add("    font-weight: bold;");
        listForRunnableLogStrs.add("    color: #FF8000;");
        listForRunnableLogStrs.add("    text-align: center;");
        listForRunnableLogStrs.add("    border-left: 2px solid #999999;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table thead th:first-child {");
        listForRunnableLogStrs.add("    border-left: none;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table tfoot {");
        listForRunnableLogStrs.add("    font-size: 14px;");
        listForRunnableLogStrs.add("    font-weight: bold;");
        listForRunnableLogStrs.add("    color: #333333;");
        listForRunnableLogStrs.add("    background: #555555;");
        listForRunnableLogStrs.add("    border-top: 3px solid #444444;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("table tfoot td {");
        listForRunnableLogStrs.add("    font-size: 14px;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#header-content{");
        listForRunnableLogStrs.add("    background: #FF8000;");
        listForRunnableLogStrs.add("    padding: 24px;");
        listForRunnableLogStrs.add("    border-bottom: 3px solid #B5B5B5;");
        listForRunnableLogStrs.add("    min-width: 355px;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#page-content{");
        listForRunnableLogStrs.add("    align-content: flex-end;");
        //listForRunnableLogStrs.add("    text-decoration: underline;");
        //strIndex++;
        listForRunnableLogStrs.add("    height:500px;");
        listForRunnableLogStrs.add("    padding: 29px;");
        listForRunnableLogStrs.add("    background: #888888;");
        listForRunnableLogStrs.add("    min-width: 355px;");
        listForRunnableLogStrs.add("    overflow: auto;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#item-content-1{");
        listForRunnableLogStrs.add("    height:250px;");
        listForRunnableLogStrs.add("    margin-right: 350px;");
        listForRunnableLogStrs.add("    background: #f6cf65;");
        listForRunnableLogStrs.add("    display: inline-block;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#item-content-2{");
        listForRunnableLogStrs.add("    height:250px;");
        listForRunnableLogStrs.add("    margin-right: 350px;");
        listForRunnableLogStrs.add("    background: #f6cf65;");
        listForRunnableLogStrs.add("    display: inline-block;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#menu-content{");
        listForRunnableLogStrs.add("    height: 500px;");
        listForRunnableLogStrs.add("    width: 300px;");
        listForRunnableLogStrs.add("    float: left;");
        listForRunnableLogStrs.add("    overflow: auto;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#menu{");
        listForRunnableLogStrs.add("    background:#80FF00;");
        listForRunnableLogStrs.add("    width:280px;");
        listForRunnableLogStrs.add("    list-style-type:none;");
        listForRunnableLogStrs.add("    padding:0;");
        listForRunnableLogStrs.add("    margin:0");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#menu li{");
        listForRunnableLogStrs.add("    border-bottom:1px solid #FFFFFF;");
        listForRunnableLogStrs.add("    padding:3px");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#menu li a{");
        listForRunnableLogStrs.add("    color:#000000;");
        listForRunnableLogStrs.add("    font-family:verdana,arial,sans-serif;");
        listForRunnableLogStrs.add("    text-decoration:none");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#menu li ul{");
        listForRunnableLogStrs.add("    border-top:1px solid #FFFFFF;");
        listForRunnableLogStrs.add("    padding:0;");
        listForRunnableLogStrs.add("    margin:0;");
        listForRunnableLogStrs.add("    list-style-type:square;");
        listForRunnableLogStrs.add("    list-style-position:inside");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#menu li ul li{");
        listForRunnableLogStrs.add("    border:0;");
        listForRunnableLogStrs.add("    list-style-type:square;");
        listForRunnableLogStrs.add("    color:#FFFFFF;");
        listForRunnableLogStrs.add("    list-style-position:inside");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("#footer-content{");
        listForRunnableLogStrs.add("    background: #FF8000;");
        listForRunnableLogStrs.add("    padding: 11px;");
        listForRunnableLogStrs.add("    min-width: 355px;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add(".highlightlime {");
        listForRunnableLogStrs.add("  background-color: #00FF00;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add(".highlightyellow {");
        listForRunnableLogStrs.add("  background-color: #FFFF00;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add(".highlightred {");
        listForRunnableLogStrs.add("  background-color: #FF0000;");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add(".highlightcyan {");
        listForRunnableLogStrs.add("  background-color: #00FFFF;");
        listForRunnableLogStrs.add("}");
        return listForRunnableLogStrs;
    }
    protected static ArrayBlockingQueue<String> getLinesForSaveJsMenu(){
        //@todo fix for queue size exception of full
        Integer messagesQueueSize = 1000;
        ArrayBlockingQueue<String> listForRunnableLogStrs = new ArrayBlockingQueue<String>(messagesQueueSize);
        listForRunnableLogStrs.add("function openMenu(node){");
        listForRunnableLogStrs.add("var subMenu = node.parentNode.getElementsByTagName(\"ul\")[0];");
        listForRunnableLogStrs.add("subMenu.style.display === \"none\" ? subMenu.style.display = \"block\" : subMenu.style.display = \"none\";");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("function allClose(){");
        listForRunnableLogStrs.add("var list = document.getElementById(\"menu\").getElementsByTagName(\"ul\");");
        listForRunnableLogStrs.add("for(var i=0;i<list.length;i++){");
        listForRunnableLogStrs.add("	list[i].style.display = \"none\";");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("}");
        //hightlight
        listForRunnableLogStrs.add("function highlightLime() {");
        listForRunnableLogStrs.add("    var inputLimeText = document.getElementById(\"limeTextElement\").value;");
        listForRunnableLogStrs.add("    var contentPageHTML = document.getElementById(\"page-content\");");
        listForRunnableLogStrs.add("    var elementsTable = contentPageHTML.getElementsByTagName('table');");
        listForRunnableLogStrs.add("    for (var jTable = 0; jTable < elementsTable.length; jTable++) {");
        listForRunnableLogStrs.add("        var elementsTableTbody = elementsTable[jTable].getElementsByTagName('tbody');");
        listForRunnableLogStrs.add("        for (var jTbody = 0; jTbody < elementsTableTbody.length; jTbody++) {");
        listForRunnableLogStrs.add("            var elementsTableTrow = elementsTableTbody[jTbody].getElementsByTagName('tr');");
        listForRunnableLogStrs.add("            for (var jTrowIdx = 0; jTrowIdx < elementsTableTrow.length; jTrowIdx++) {");
        listForRunnableLogStrs.add("                var elements = elementsTableTrow[jTrowIdx].getElementsByTagName('td');");
        listForRunnableLogStrs.add("                for (var i = 0; i < elements.length; i++) {");
        listForRunnableLogStrs.add("                    var inputText = elements[i];");
        listForRunnableLogStrs.add("                    var innerHTML = elements[i].innerHTML;");
        listForRunnableLogStrs.add("                    if( inputLimeText > '' ){");
        listForRunnableLogStrs.add("                        var index = innerHTML.indexOf(inputLimeText);");
        listForRunnableLogStrs.add("                        var text = inputLimeText;");
        listForRunnableLogStrs.add("                        if (index >= 0) { ");
        listForRunnableLogStrs.add("                            innerHTML = innerHTML.substring(0,index) + \"<span class='highlightlime'>\" + innerHTML.substring(index,index + text.length) + \"</span>\" + innerHTML.substring(index + text.length);");
        listForRunnableLogStrs.add("                            inputText.innerHTML = innerHTML;");
        listForRunnableLogStrs.add("                        }");
        listForRunnableLogStrs.add("                    }");
        listForRunnableLogStrs.add("                }");
        listForRunnableLogStrs.add("            }");
        listForRunnableLogStrs.add("        }");
        listForRunnableLogStrs.add("    }");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("function highlightYellow() {");
        listForRunnableLogStrs.add("    var inputYellowText = document.getElementById(\"yellowTextElement\").value;");
        listForRunnableLogStrs.add("    var contentPageHTML = document.getElementById(\"page-content\");");
        listForRunnableLogStrs.add("    var elementsTable = contentPageHTML.getElementsByTagName('table');");
        listForRunnableLogStrs.add("    for (var jTable = 0; jTable < elementsTable.length; jTable++) {");
        listForRunnableLogStrs.add("        var elementsTableTbody = elementsTable[jTable].getElementsByTagName('tbody');");
        listForRunnableLogStrs.add("        for (var jTbody = 0; jTbody < elementsTableTbody.length; jTbody++) {");
        listForRunnableLogStrs.add("            var elementsTableTrow = elementsTableTbody[jTbody].getElementsByTagName('tr');");
        listForRunnableLogStrs.add("            for (var jTrowIdx = 0; jTrowIdx < elementsTableTrow.length; jTrowIdx++) {");
        listForRunnableLogStrs.add("                var elements = elementsTableTrow[jTrowIdx].getElementsByTagName('td');");
        listForRunnableLogStrs.add("                for (var i = 0; i < elements.length; i++) {");
        listForRunnableLogStrs.add("                    var inputText = elements[i];");
        listForRunnableLogStrs.add("                    var innerHTML = elements[i].innerHTML;");
        listForRunnableLogStrs.add("                    if( inputYellowText > '' ){");
        listForRunnableLogStrs.add("                        var index = innerHTML.indexOf(inputYellowText);");
        listForRunnableLogStrs.add("                        var text = inputYellowText;");
        listForRunnableLogStrs.add("                        if (index >= 0) {");
        listForRunnableLogStrs.add("                            innerHTML = innerHTML.substring(0,index) + \"<span class='highlightyellow'>\" + innerHTML.substring(index,index + text.length) + \"</span>\" + innerHTML.substring(index + text.length);");
        listForRunnableLogStrs.add("                            inputText.innerHTML = innerHTML;");
        listForRunnableLogStrs.add("                        }");
        listForRunnableLogStrs.add("                    }");
        listForRunnableLogStrs.add("                }");
        listForRunnableLogStrs.add("            }");
        listForRunnableLogStrs.add("        }");
        listForRunnableLogStrs.add("    }");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("function highlightRed() {");
        listForRunnableLogStrs.add("    var inputRedText = document.getElementById(\"redTextElement\").value;");
        listForRunnableLogStrs.add("    var contentPageHTML = document.getElementById(\"page-content\");");
        listForRunnableLogStrs.add("    var elementsTable = contentPageHTML.getElementsByTagName('table');");
        listForRunnableLogStrs.add("    for (var jTable = 0; jTable < elementsTable.length; jTable++) {");
        listForRunnableLogStrs.add("        var elementsTableTbody = elementsTable[jTable].getElementsByTagName('tbody');");
        listForRunnableLogStrs.add("        for (var jTbody = 0; jTbody < elementsTableTbody.length; jTbody++) {");
        listForRunnableLogStrs.add("            var elementsTableTrow = elementsTableTbody[jTbody].getElementsByTagName('tr');");
        listForRunnableLogStrs.add("            for (var jTrowIdx = 0; jTrowIdx < elementsTableTrow.length; jTrowIdx++) {");
        listForRunnableLogStrs.add("                var elements = elementsTableTrow[jTrowIdx].getElementsByTagName('td');");
        listForRunnableLogStrs.add("                for (var i = 0; i < elements.length; i++) {");
        listForRunnableLogStrs.add("                    var inputText = elements[i];");
        listForRunnableLogStrs.add("                    var innerHTML = elements[i].innerHTML;");
        listForRunnableLogStrs.add("                    if( inputRedText > '' ){");
        listForRunnableLogStrs.add("                        var index = innerHTML.indexOf(inputRedText);");
        listForRunnableLogStrs.add("                        var text = inputRedText;");
        listForRunnableLogStrs.add("                        if (index >= 0) {");
        listForRunnableLogStrs.add("                            innerHTML = innerHTML.substring(0,index) + \"<span class='highlightred'>\" + innerHTML.substring(index,index + text.length) + \"</span>\" + innerHTML.substring(index + text.length);");
        listForRunnableLogStrs.add("                            inputText.innerHTML = innerHTML;");
        listForRunnableLogStrs.add("                        }");
        listForRunnableLogStrs.add("                    }");
        listForRunnableLogStrs.add("                }");
        listForRunnableLogStrs.add("            }");
        listForRunnableLogStrs.add("        }");
        listForRunnableLogStrs.add("    }");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("function highlightCyan() {");
        listForRunnableLogStrs.add("    var inputCyanText = document.getElementById(\"cyanTextElement\").value;");
        listForRunnableLogStrs.add("    var contentPageHTML = document.getElementById(\"page-content\");");
        listForRunnableLogStrs.add("    var elementsTable = contentPageHTML.getElementsByTagName('table');");
        listForRunnableLogStrs.add("    for (var jTable = 0; jTable < elementsTable.length; jTable++) {");
        listForRunnableLogStrs.add("        var elementsTableTbody = elementsTable[jTable].getElementsByTagName('tbody');");
        listForRunnableLogStrs.add("        for (var jTbody = 0; jTbody < elementsTableTbody.length; jTbody++) {");
        listForRunnableLogStrs.add("            var elementsTableTrow = elementsTableTbody[jTbody].getElementsByTagName('tr');");
        listForRunnableLogStrs.add("            for (var jTrowIdx = 0; jTrowIdx < elementsTableTrow.length; jTrowIdx++) {");
        listForRunnableLogStrs.add("                var elements = elementsTableTrow[jTrowIdx].getElementsByTagName('td');");
        listForRunnableLogStrs.add("                for (var i = 0; i < elements.length; i++) {");
        listForRunnableLogStrs.add("                    var inputText = elements[i];");
        listForRunnableLogStrs.add("                    var innerHTML = elements[i].innerHTML;");
        listForRunnableLogStrs.add("                    if( inputCyanText > '' ){");
        listForRunnableLogStrs.add("                        var index = innerHTML.indexOf(inputCyanText);");
        listForRunnableLogStrs.add("                        var text = inputCyanText;");
        listForRunnableLogStrs.add("                        if (index >= 0) {");
        listForRunnableLogStrs.add("                            innerHTML = innerHTML.substring(0,index) + \"<span class='highlightcyan'>\" + innerHTML.substring(index,index + text.length) + \"</span>\" + innerHTML.substring(index + text.length);");
        listForRunnableLogStrs.add("                            inputText.innerHTML = innerHTML;");
        listForRunnableLogStrs.add("                        }");
        listForRunnableLogStrs.add("                    }");
        listForRunnableLogStrs.add("                }");
        listForRunnableLogStrs.add("            }");
        listForRunnableLogStrs.add("        }");
        listForRunnableLogStrs.add("    }");
        listForRunnableLogStrs.add("}");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");
        listForRunnableLogStrs.add("");

        
        

        
        return listForRunnableLogStrs;
    }
    protected static ArrayBlockingQueue<String> getLinesForSaveJsLoadHtml(){
        Integer messagesQueueSize = 100;
        ArrayBlockingQueue<String> listForRunnableLogStrs = new ArrayBlockingQueue<String>(messagesQueueSize);
        int strIndex = 0;
        strIndex++;
        listForRunnableLogStrs.add("function importTable20181115100212944(){");
        strIndex++;
        listForRunnableLogStrs.add("var link = document.createElement('link');");
        strIndex++;
        listForRunnableLogStrs.add("link.rel = 'import';");
        strIndex++;
        listForRunnableLogStrs.add("link.href = 'table-20181115100212944.html';");
        strIndex++;
        listForRunnableLogStrs.add("link.onload = function(this.e){console.log('Loaded import: ' + e.target.href);};");
        strIndex++;
        listForRunnableLogStrs.add("link.onerror = function(this.e){console.log('Error loading import: ' + e.target.href);};");
        strIndex++;
        listForRunnableLogStrs.add("}");
        strIndex++;
        listForRunnableLogStrs.add("function importTable001(){");
        strIndex++;
        listForRunnableLogStrs.add("var content = document.querySelector('link[rel=\"import\"]').import;");
        strIndex++;
        listForRunnableLogStrs.add("alert(content);");
        strIndex++;
        listForRunnableLogStrs.add("document.body.appendChild(content.cloneNode(true));");
        strIndex++;
        listForRunnableLogStrs.add("}");
        return listForRunnableLogStrs;
    }
}
