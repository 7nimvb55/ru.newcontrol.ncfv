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

/**
 *
 * @author wladimirowichbiaran
 */
public class NcfvRunVariables {
    private static boolean devStage = true;
    private static boolean outWithTrace = false;
    private static boolean outWithPrintFunc = false;
    private static boolean outWithFileName = false;
    private static final int LOGLINES = 10000;
    private static boolean outToLogFile = true;
    private static boolean outToLogWithTrace = true;
    private static boolean outToLogPrintFunc = true;
    private static boolean outToLogFileName = true;
    private static boolean outToLogNewRecordAppend = false;
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
    public static int getLogLinesCount(){
        return LOGLINES;
    }
    public static boolean isOutToLogFile(){
        return outToLogFile;
    }
    public static boolean isOutToLogFileIncludeFile(){
        return outToLogFileName;
    }
    public static boolean isOutToLogFileTraceWithPrintFunc(){
        return outToLogPrintFunc;
    }
    public static boolean isOutToLogFileWithTrace(){
        return outToLogWithTrace;
    }
    public static boolean isOutToLogNewRecordAppend(){
        return outToLogNewRecordAppend;
    }
}
