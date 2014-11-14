/*
 * Copyright (c) 2014 Albert C. Braun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.albertcbraun.wifidlite;

/**
 * Defines configuration settings for the {@link WifiDLite} object.
 */
public interface Configuration {

    /**
     * The number of seconds between the last time the
     * {@link com.albertcbraun.wifidlite.Heartbeat}
     * completed a run and the next time a run is started.
     * <p>
     * Note: this has not been tested with delay times below
     * five seconds. Alter at your own risk.
     *
     * @return delay in seconds
     */
    public int getHeartbeatDelay();
}
