/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.dicadeveloper.runnerapp.services.sensors;

import android.content.Context;

/**
 * A Polar heart rate sensor manager.
 * 
 * @author John R. Gerthoffer
 */
public class PolarSensorManager extends BluetoothSensorManager {
  public PolarSensorManager(Context context) {
    super(context, new PolarMessageParser());
  }
}