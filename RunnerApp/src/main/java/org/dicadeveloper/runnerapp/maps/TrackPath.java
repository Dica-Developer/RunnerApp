/*
 * Copyright 2012 Google Inc.
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

package org.dicadeveloper.runnerapp.maps;

import com.google.android.gms.maps.model.Polyline;

import org.dicadeveloper.runnerapp.MapOverlay.CachedLocation;
import org.dicadeveloper.runnerapp.stats.TripStatistics;
import org.osmdroid.api.IMap;

import java.util.ArrayList;
import java.util.List;

/**
 * A track path.
 * 
 * @author Jimmy Shih
 */
public interface TrackPath {

  /**
   * Updates state.
   * 
   * @param tripStatistics the trip statistics
   * @return true if the state is updated.
   */
  public boolean updateState(TripStatistics tripStatistics);

  /**
   * Updates the path.
   * 
   * @param startIndex the start index
   * @param points the points
   */
  public void updatePath(IMap googleMap, ArrayList<Polyline> paths, int startIndex,
      List<CachedLocation> points);
}