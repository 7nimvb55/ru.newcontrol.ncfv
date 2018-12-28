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
public class AppObjectsListLab {
    /**
     * Concept, Runnables release for work algoritm, mechanism, runned in Thread,
     * this metod is laboratory for test this concept
     */
    protected static void runnablesInThreadsTest(){
        ThreadGroup testTG = new ThreadGroup("TestForLogger");
            
            AppThExtendsBaseThread appThExtendsBaseThread = new AppThExtendsBaseThread(3);
            System.out.println(" * * * First create Thread from Runnable and start");
            Thread forTest = new Thread(testTG,appThExtendsBaseThread,"testThread-" + AppFileOperationsSimple.getNowTimeStringWithMS());
            System.out.println("[NAME]" + forTest.getName() 
                    + "[toString]" + forTest.toString()
                    + "[getId]" + String.valueOf(forTest.getId())
                    + "[getPriority]" + String.valueOf(forTest.getPriority())
                    + "[getState][name]" + String.valueOf(forTest.getState().name())
                    + "[getState][ordinal]" + String.valueOf(forTest.getState().ordinal())
                    + "[isDaemon]" + String.valueOf(forTest.isDaemon())
                    + "[isAlive]" + String.valueOf(forTest.isAlive())
                    + "[isInterrupted]" + String.valueOf(forTest.isInterrupted())
            );
            System.out.println("[NcAppHelper.getThreadInfoToString]" + NcAppHelper.getThreadInfoToString(forTest));
            
            try{
                forTest.join();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
            forTest.start();
            System.out.println(" * * * Second create Thread from Runnable and start");
            
            forTest = new Thread(testTG,appThExtendsBaseThread,"testThread" + AppFileOperationsSimple.getNowTimeStringWithMS());
            System.out.println("" + forTest.getName());
            forTest.start();
            try{
                forTest.join();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
            System.out.println(" * * * Thrid create Thread from Runnable and start");
            forTest = new Thread(testTG,appThExtendsBaseThread,"testThread" + AppFileOperationsSimple.getNowTimeStringWithMS());
            System.out.println("" + forTest.getName());
            forTest.start();
            try{
                forTest.join();
            } catch(InterruptedException ex){
                ex.printStackTrace();
            }
    }
}
