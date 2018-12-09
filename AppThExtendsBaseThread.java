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

import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author wladimirowichbiaran
 */
public class AppThExtendsBaseThread implements Runnable {
    private static ThreadLocal<Integer> testValue;
    private int secondTest;

    public AppThExtendsBaseThread() {
        super();
        secondTest = 5;
    }
    
    @Override
    public void run() {
        
        System.out.println(secondTest + "[second]" + this.toString());
        
        testValue = new ThreadLocal<Integer>();
        testValue.set(15);
        
        secondTest++;
        System.out.println(secondTest + "[second]" + this.toString());
        
        Integer get = testValue.get();
        System.out.println(get + "[]" + this.toString());
        
        secondTest++;
        System.out.println(secondTest + "[second]" + this.toString());
        
        get++;
        testValue.set(get);
        Integer get1 = testValue.get();
        System.out.println(get1 + "[]" + this.toString());
        
        secondTest++;
        System.out.println(secondTest + "[second]" + this.toString());
        testValue.remove();
    }
}
