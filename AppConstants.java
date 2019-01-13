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
public class AppConstants {
    protected static final Integer LOG_HTML_CLEAN_FROM_BUS_ABOVE_QUEUE_SIZE = 1;
    protected static final Integer LOG_HTML_JOB_QUEUE_SIZE = 100;
    protected static final Integer LOG_HTML_MESSAGES_QUEUE_SIZE = 10000;
    protected static final Integer LOG_LINES_COUNT = 100;
    protected static final Integer LIMIT_MESSAGES_FOR_LOG_IN_QUEUE_COUNT = 100;
    protected static final Integer LOG_LEVEL_DEVELOPMENT = 7;
    protected static final Integer LOG_LEVEL_DEBUG = 5;
    protected static final Integer LOG_LEVEL_USE = 3;
    protected static final Integer LOG_LEVEL_SILENT = 1;
    protected static final Integer LOG_LEVEL_CURRENT = LOG_LEVEL_DEVELOPMENT;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_CREATE_TEXT_LOGGER = Boolean.FALSE;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_LOGGER_READ_FROM_FILE_SIZE = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_VIEW_THREADS_PARAM = Boolean.FALSE;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_WAITER_START = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_WAITER_ALL_STACK = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_WAITER_ALL_PRINT_TRACE = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_WAITER_CURRENT_STACK = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_WAITER_STOP = Boolean.FALSE;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_RUNNABLE_CREATE = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_RUNNABLE_RUN_JOB = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_RUNNABLE_JOB_END_READ = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_READER_RUNNABLE_JOB_READ_NULL = Boolean.FALSE;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_WAITER_START = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_WAITER_ALL_STACK = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_WAITER_FOUNDED_IN_STACK = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_WAITER_ALL_PRINT_TRACE = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_WAITER_CURRENT_STACK = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_WAITER_STOP = Boolean.FALSE;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_RUNNABLE_CREATE = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_RUNNABLE_GET_NEW_JOB = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_RUNNABLE_VIEW_NEW_JOB_PARAM = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_JOB_WRITER_RUNNABLE_VIEW_SIZE_JOB_PARAM = Boolean.FALSE;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_BUS_HELPER_VIEW_SIZE_DATA_FOR_CLEAN_OUT = Boolean.FALSE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_BUS_HELPER_VIEW_SIZE_DATA_FOR_CLEAN_OUTPUT = Boolean.FALSE;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_WALKER_DO_READ_FS_TO_PIPE_SET_STARTED = Boolean.TRUE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_WALKER_DO_READ_FS_TO_PIPE = Boolean.TRUE;
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_DIR_LIST_TACKER_RUN = Boolean.TRUE;
    
    
    protected static final Integer PIPE_READ_FS_TO_TACKER_WORKER_QUEUE_SIZE = 100000;
}
