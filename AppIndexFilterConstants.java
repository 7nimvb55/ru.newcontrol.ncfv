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

/**
 * @todo release from serializable to XML interface for add expression 
 * in etc folder or some more release
 * @author wladimirowichbiaran
 */
public class AppIndexFilterConstants {
    protected static String NCLVLABC = "[^a-zA-Z]";
    protected static String NCLVLRABC = "[^а-яА-Я]";
    protected static String NCLVLNUM = "[^0-9]";
    protected static String NCLVLSYM = "[0-9a-zA-Zа-яА-Я ]";
    protected static String NCLVLSPACE = "[^ ]";
}
