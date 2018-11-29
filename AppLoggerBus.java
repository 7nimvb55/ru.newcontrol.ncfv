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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppLoggerBus {
    private ArrayBlockingQueue<ArrayList<String>> commandsOutPut;
    private ArrayBlockingQueue<String> listForRunnableLogStrs;
    private ArrayBlockingQueue<String> readedLinesFromLogHTML;
    private ArrayList<ArrayBlockingQueue<String>> readedArrayForLines;
    private ArrayBlockingQueue<String> readedLinesFromTablesWork;
    private ConcurrentSkipListMap<String, Path> listLogStorageFiles;
    private ArrayBlockingQueue<Path> readedFilesListInLogHtmlByTableMask;
}
