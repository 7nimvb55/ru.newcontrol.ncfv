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
public interface NcThProcGUICallbackInterface {
    /**
     * Not used
     */
    @NcThProcTypeDetectInterface
    void appendSrchResult();
    /**
     * Not used
     */
    @NcThProcTypeDetectInterface
    void setSrcResult();
    /**
     * Not used
     */
    @NcThProcTypeDetectInterface
    void showProgressSwitch();
    /**
     * Not used
     */
    @NcThProcTypeDetectInterface
    void startSrch();
    /**
     * Not used
     */
    @NcThProcTypeDetectInterface
    void stopSrch();
    /**
     * Not used
     * @param strMessage 
     */
    @NcThProcTypeDetectInterface(NcThProcType.SYNC)
    void showError(String strMessage);
}
