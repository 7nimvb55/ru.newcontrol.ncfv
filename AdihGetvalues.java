/*
 * Copyright 2019 wladimirowichbiaran.
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Adih
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>helper Get values from system properties
 * </ul>
 * @author wladimirowichbiaran
 */
public class AdihGetvalues {
    /**
     * 
     * @return yyyyMMddHHmmss
     */
    protected static String getNowTimeString(){
        Long currentDateTime;
        Date currentDate;
        DateFormat df;
        try {
            currentDateTime = System.currentTimeMillis();
            currentDate = new Date(currentDateTime);
            df = new SimpleDateFormat("yyyyMMddHHmmss");
            return df.format(currentDate);
        } finally {
            currentDateTime = null;
            currentDate = null;
            df = null;
        }
    }
    /**
     * 
     * @return yyyyMMddHHmmssSSS
     */
    protected static String getNowTimeStringMillis(){
        Long currentDateTime;
        Date currentDate;
        DateFormat df;
        try {
            currentDateTime = System.currentTimeMillis();
            currentDate = new Date(currentDateTime);
            df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            return df.format(currentDate);
        } finally {
            currentDateTime = null;
            currentDate = null;
            df = null;
        }
    }
    /**
     * 
     * @return yyyy-MM-dd HH:mm:ss
     */
    protected static String getNowTimeStringHuman(){
        Long currentDateTime;
        Date currentDate;
        DateFormat df;
        try {
            currentDateTime = System.currentTimeMillis();
            currentDate = new Date(currentDateTime);
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return df.format(currentDate);
        } finally {
            currentDateTime = null;
            currentDate = null;
            df = null;
        }
    }
    /**
     * 
     * @return yyyy-MM-dd HH:mm:ss.SSS
     */
    protected static String getNowTimeStringMillisHuman(){
        Long currentDateTime;
        Date currentDate;
        DateFormat df;
        try {
            currentDateTime = System.currentTimeMillis();
            currentDate = new Date(currentDateTime);
            df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            return df.format(currentDate);
        } finally {
            currentDateTime = null;
            currentDate = null;
            df = null;
        }
    }
    /**
     * 
     * @return yyyy-MM-dd_HH-mm-ss
     */
    protected static String getNowTimeStringFsNames(){
        Long currentDateTime;
        Date currentDate;
        DateFormat df;
        try {
            currentDateTime = System.currentTimeMillis();
            currentDate = new Date(currentDateTime);
            df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            return df.format(currentDate);
        } finally {
            currentDateTime = null;
            currentDate = null;
            df = null;
        }
    }
    /**
     * 
     * @return yyyy-MM-dd_HH-mm-ss-SSS
     */
    protected static String getNowTimeStringMillisFsNames(){
        Long currentDateTime;
        Date currentDate;
        DateFormat df;
        try {
            currentDateTime = System.currentTimeMillis();
            currentDate = new Date(currentDateTime);
            df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS");
            return df.format(currentDate);
        } finally {
            currentDateTime = null;
            currentDate = null;
            df = null;
        }
    }
    protected static int getDefaultSleepValue(){
        return 500;
    }
}
