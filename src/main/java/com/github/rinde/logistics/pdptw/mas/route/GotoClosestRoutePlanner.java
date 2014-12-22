/*
 * Copyright (C) 2013-2014 Rinde van Lon, iMinds DistriNet, KU Leuven
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.rinde.logistics.pdptw.mas.route;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.github.rinde.rinsim.core.model.pdp.PDPModel;
import com.github.rinde.rinsim.core.pdptw.DefaultParcel;
import com.github.rinde.rinsim.geom.Point;
import com.github.rinde.rinsim.util.StochasticSupplier;
import com.github.rinde.rinsim.util.StochasticSuppliers;
import com.google.common.base.Optional;

/**
 * A {@link RoutePlanner} implementation that lets a vehicle go to its closest
 * destination.
 * @author Rinde van Lon
 */
public class GotoClosestRoutePlanner extends AbstractRoutePlanner {

  Comparator<DefaultParcel> comp;

  private Optional<DefaultParcel> current;
  private final List<DefaultParcel> parcels;

  /**
   * New instance.
   */
  public GotoClosestRoutePlanner() {
    comp = new ClosestDistanceComparator();
    current = Optional.absent();
    parcels = newArrayList();
  }

  @Override
  protected final void doUpdate(Collection<DefaultParcel> onMap, long time) {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    final Collection<DefaultParcel> inCargo = Collections.checkedCollection(
        (Collection) pdpModel.get().getContents(vehicle.get()),
        DefaultParcel.class);
    parcels.clear();
    parcels.addAll(onMap);
    parcels.addAll(onMap);
    parcels.addAll(inCargo);
    updateCurrent();
  }

  private void updateCurrent() {
    if (parcels.isEmpty()) {
      current = Optional.absent();
    } else {
      current = Optional.of(Collections.min(parcels, comp));
    }
  }

  @Override
  protected void nextImpl(long time) {
    if (current.isPresent()) {
      parcels.remove(current.get());
    }
    updateCurrent();
  }

  @Override
  public Optional<DefaultParcel> current() {
    return current;
  }

  @Override
  public boolean hasNext() {
    return !parcels.isEmpty();
  }

  /**
   * @return A {@link StochasticSupplier} that supplies
   *         {@link GotoClosestRoutePlanner} instances.
   */
  public static StochasticSupplier<GotoClosestRoutePlanner> supplier() {
    return new StochasticSuppliers.AbstractStochasticSupplier<GotoClosestRoutePlanner>() {
      private static final long serialVersionUID = 1701618808844264668L;

      @Override
      public GotoClosestRoutePlanner get(long seed) {
        return new GotoClosestRoutePlanner();
      }
    };
  }

  static Point getPos(DefaultParcel parcel, PDPModel model) {
    if (model.getParcelState(parcel).isPickedUp()) {
      return parcel.dto.deliveryLocation;
    }
    return parcel.dto.pickupLocation;
  }

  class ClosestDistanceComparator implements Comparator<DefaultParcel> {
    @Override
    public int compare(@Nullable DefaultParcel arg0,
        @Nullable DefaultParcel arg1) {
      final Point cur = roadModel.get().getPosition(vehicle.get());
      final Point p0 = getPos(checkNotNull(arg0), pdpModel.get());
      final Point p1 = getPos(checkNotNull(arg1), pdpModel.get());
      return Double.compare(Point.distance(cur, p0), Point.distance(cur, p1));
    }
  }
}