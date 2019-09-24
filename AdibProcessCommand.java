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
 * Adib
 * <ul>
 * <li>Automated
 * <li>data
 * <li>indexing
 * <li>Bus Process Command
 * </ul>
 * Contains Bus with commands for runners (runnable inside workers), 
 * Type bus:  wait, do, ready
 * process number: 0..13 from {@link AdihHelper#getProcessNames() AdihHelper.getProcessNames()}
 * command code: 0..13 from {@link AdihHelper#getCommandNames() AdihHelper.getCommandNames()}
 * 
 * @author wladimirowichbiaran
 */
public class AdibProcessCommand {
    
}
