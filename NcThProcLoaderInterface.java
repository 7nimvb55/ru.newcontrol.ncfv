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
 * Developed based on the publications found on the Internet at
 * http://www.skipy.ru/technics/gui_sync.html
 * Thanks and best regards to author of publishing
 * 
 * @author wladimirowichbiaran
 */
public interface NcThProcLoaderInterface {
    /**
     * Loading operation in new Thread for run
     */
    void execute();
    /**
     * Cancel run thread
     */
    void cancel();
    /**
     * Get status of current operation if avalable
     */
    void state();
    /**
     * Get errors in runned process if generated
     */
    void error();
    /**
     * Get statistics of executed operations if avalable
     */
    void stats();
}
