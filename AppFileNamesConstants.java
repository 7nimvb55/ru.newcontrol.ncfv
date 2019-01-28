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

/**
 *
 * @author wladimirowichbiaran
 */
public class AppFileNamesConstants {
    protected static String LOG_SUB_DIR = "log";
    protected static String CFG_SUB_DIR = "etc";
    protected static String CFG_GENERATED_FILENAME = "autogen";
    protected static String CFG_GENERATED_EXT = ".cfg";
    protected static String LOG_EXT = ".log";
    
    protected static String LOG_HTML_SUB_DIR = "html";
    protected static String LOG_HTML_CSS_SUB_DIR = "css";
    protected static String LOG_HTML_CSS_PREFIX = "report-";
    protected static String LOG_HTML_CSS_EXT = ".css";
    protected static String LOG_HTML_JS_SUB_DIR = "js";
    protected static String LOG_HTML_JS_MENU_PREFIX = "domenu-";
    protected static String LOG_HTML_JS_EXT = ".js";
    protected static String LOG_HTML_EXT = ".html";
    protected static String LOG_HTML_TABLE_PREFIX = "table-";
    protected static String LOG_HTML_MENU_PREFIX = "menu-";
    protected static String LOG_HTML_HEADER_PREFIX = "header-";
    protected static String LOG_HTML_FOOTER_PREFIX = "footer-";
    protected static String LOG_INDEX_PREFIX = "index-";
    protected static String LOG_HTML_KEY_FOR_CURRENT_SUB_DIR = "currentHtmlSubDir";
    
    protected static String SZFS_DIR_LIST_FILE_PREFIX = "dl-";
    protected static String SZFS_DIR_LIST_FILE_NOT_LIMITED = "notlimited-";
    
    // In thread system used for main index directories
    protected static String DIR_IDX = "/ncidxfv";
    // old versions prefixes, names
    protected static String DIR_TMP = "/t";
    protected static String DIR_DIR_LIST = "/di";
    protected static String DIR_JOURNAL = "/j";
    protected static String DIR_FILE_LIST = "/fl";
    protected static String DIR_FILE_TYPE = "/ft";
    protected static String DIR_FILE_HASH = "/fh";
    protected static String DIR_FILE_EXIST = "/fx";
    protected static String DIR_WORD = "/w";
    protected static String DIR_STORAGE_WORD = "/sw";
    protected static String DIR_LONG_WORD_LIST = "/lw";
    protected static String DIR_LONG_WORD_DATA = "/ln";
    
    protected static String PRE_DIR_LIST = "dl-";
    
    protected static String DIR_APP_DATA = "/appdata";
    
    protected static String FILE_INDEX_CONTAINS = "/ncidxfv.zip";
    // In threads system used constants for folder and file names
    protected static String FILE_INDEX_KEY_MAP_URI = "UriForStorage";
    protected static String FILE_INDEX_PREFIX_TMP = "t-";
    protected static String FILE_INDEX_PREFIX_DIR_LIST = "di-";
    protected static String FILE_INDEX_PREFIX_JOURNAL = "j-";
    protected static String FILE_INDEX_PREFIX_FILE_LIST = "fl-";
    protected static String FILE_INDEX_PREFIX_FILE_TYPE = "ft-";
    protected static String FILE_INDEX_PREFIX_FILE_HASH = "fh-";
    protected static String FILE_INDEX_PREFIX_FILE_EXIST = "fx-";
    protected static String FILE_INDEX_PREFIX_WORD = "w-";
    protected static String FILE_INDEX_PREFIX_STORAGE_WORD = "sw-";
    protected static String FILE_INDEX_PREFIX_LONG_WORD_LIST = "lw-";
    protected static String FILE_INDEX_PREFIX_LONG_WORD_DATA = "ln-";
    protected static String FILE_INDEX_EXT = ".zip";
    
    protected static String FILE_JOURNAL_DISK = "/jdisk.dat";
    protected static String FILE_DISK_DATA = "/disks.dat";
    protected static String FILE_ID_DATA = "/ids.dat";
    
    protected static String FILE_APP_LOG = "/app.log";
    
    protected static String DIR_APP_CFG = "/etc";
    protected static String FILE_APP_CFG = "/ncfv.conf";
    
    protected static String FILE_SRCH_KEY_OUT = "/keywordout.list";
    protected static String FILE_SRCH_KEY_IN = "/keywordin.list";
    protected static String FILE_SRCH_DIR_OUT = "/dirout.list";
    protected static String FILE_SRCH_DIR_IN = "/dirin.list";
    
}
