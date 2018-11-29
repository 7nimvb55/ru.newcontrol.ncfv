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

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppObjectsBusHelper {
    protected static ArrayList<String> cleanBusArrayBlockingToArrayString(ArrayBlockingQueue<String> listForLogStrs){
        System.out.println("--------size bus in converter " + listForLogStrs.size());
        ArrayList<String> forRecord = new ArrayList<String>();
        String poll;
        do{
            poll = listForLogStrs.poll();
            forRecord.add(poll);
        }while( !listForLogStrs.isEmpty() );
        System.out.println("--------size converted array for write " + forRecord.size());
        return forRecord;
    }
    protected static ArrayBlockingQueue<String> cleanBusForRunnables(ArrayBlockingQueue<String> listForLogStrs){
        System.out.println("--------size bus in converter " + listForLogStrs.size());
        ArrayBlockingQueue<String> forRecord = new ArrayBlockingQueue<String>(listForLogStrs.size() + 100);
        String poll;
        do{
            poll = listForLogStrs.poll();
            forRecord.add(poll);
        }while( !listForLogStrs.isEmpty() );
        System.out.println("--------size converted array for write " + forRecord.size());
        return forRecord;
    }
}
