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

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author wladimirowichbiaran
 * @param <E>
 */
public class NcDataTransporter<E> extends CopyOnWriteArrayList<E> {
    final transient ReentrantLock lock = new ReentrantLock();
    private static final int COUNT_IN_PACK = 100;
    
    void putInPack(E c){
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            
            Class<?> aClass = c.getClass();
            //aClass.getMethod(name, parameterTypes);
            Method[] declaredMethods = aClass.getDeclaredMethods();
            for ( Method declaredMethod : declaredMethods ) {
                System.out.println(declaredMethod.getName());
            }
            
            
        } finally {
            lock.unlock();
        }
    }
}
