/*
 * Copyright 2013 Google Inc.
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

package org.dicadeveloper.runnerapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.dicadeveloper.runnerapp.util.DialogUtils;

/**
 * Abstract My Tracks DialogFragment.
 * 
 * @author Jimmy Shih
 */
public abstract class AbstractMyTracksDialogFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Dialog dialog = createDialog();
    dialog.setOnShowListener(new DialogInterface.OnShowListener() {

        @Override
      public void onShow(DialogInterface dialogInterface) {
        DialogUtils.setDialogTitleDivider(getActivity(), dialog);
      }
    });
    return dialog;
  }

  protected abstract Dialog createDialog();
}