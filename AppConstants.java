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
    protected static final Integer LOG_LINES_COUNT = 1000;
    protected static final Integer LIMIT_MESSAGES_FOR_LOG_IN_QUEUE_COUNT = 1;
    protected static final Integer LOG_LEVEL_DEVELOPMENT = 7;
    protected static final Integer LOG_LEVEL_DEBUG = 5;
    protected static final Integer LOG_LEVEL_USE = 3;
    protected static final Integer LOG_LEVEL_SILENT = 1;
    protected static final Integer LOG_LEVEL_CURRENT = LOG_LEVEL_DEVELOPMENT;
    
    protected static final Boolean LOG_LEVEL_IS_DEV_TO_CONS_HTML_LOGGER_READ_FROM_FILE_SIZE = Boolean.TRUE;
}
