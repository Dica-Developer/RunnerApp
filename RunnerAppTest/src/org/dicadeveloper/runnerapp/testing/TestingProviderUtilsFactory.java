// Copyright 2010 Google Inc. All Rights Reserved.

package org.dicadeveloper.runnerapp.testing;

import org.dicadeveloper.runnerapp.content.MyTracksProviderUtils;
import org.dicadeveloper.runnerapp.content.MyTracksProviderUtils.Factory;

import android.content.Context;

/**
 * A fake factory for {@link MyTracksProviderUtils} which always returns a
 * predefined instance.
 *
 * @author Rodrigo Damazio
 */
public class TestingProviderUtilsFactory extends Factory {
  private MyTracksProviderUtils instance;

  public TestingProviderUtilsFactory(MyTracksProviderUtils instance) {
    this.instance = instance;
  }

  @Override
  protected MyTracksProviderUtils newForContext(Context context) {
    return instance;
  }

  public static Factory installWithInstance(MyTracksProviderUtils instance) {
    Factory oldFactory = Factory.getInstance();
    Factory factory = new TestingProviderUtilsFactory(instance);
    MyTracksProviderUtils.Factory.overrideInstance(factory);
    return oldFactory;
  }

  public static void restoreOldFactory(Factory factory) {
    MyTracksProviderUtils.Factory.overrideInstance(factory);
  }
}
