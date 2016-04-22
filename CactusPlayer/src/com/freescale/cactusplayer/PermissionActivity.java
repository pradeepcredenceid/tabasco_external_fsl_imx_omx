/*
 * Copyright (C) 2016 Freescale Semiconductor, Inc.
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
package com.freescale.cactusplayer;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Intent;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;

public class PermissionActivity extends Activity {

	private int mNumPermissionsToRequest = 0;
	private boolean mShouldRequestStoragePermission = false;
	private boolean mFlagHasStoragePermission = true;
	private int mIndexPermissionRequestStorage = 0;
	private static final int PERMISSION_REQUEST_CODE = 0;
	private static final String TAG = "Cactus Player PermissionActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.permissions);
		checkPermission();
	}

	private void checkPermission(){

		if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
				!= PackageManager.PERMISSION_GRANTED){
			mNumPermissionsToRequest++;
			mShouldRequestStoragePermission  = true;
		}else{
			mFlagHasStoragePermission  = true;
		}

		String[] permissionToRequest = new String[mNumPermissionsToRequest];
		int permissionRequestIndex = 0;

		if(mShouldRequestStoragePermission){
			permissionToRequest[permissionRequestIndex] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
			mIndexPermissionRequestStorage= permissionRequestIndex;
			permissionRequestIndex++;
		}

		if(permissionToRequest.length > 0){
			requestPermissions(permissionToRequest, PERMISSION_REQUEST_CODE);
		}else{
			Intent enterVideoMenu = new Intent(this, VideoMenu.class);
			startActivity(enterVideoMenu);
			finish();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
			String permissions[], int[] grantResults) {
		switch (requestCode) {
		case PERMISSION_REQUEST_CODE:
			if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				Log.v(TAG, "Grant permission successfully");
				enterVideoMenu();
			} else {
				popupWarningDialog();
			}
			break;
		default:
			break;
		}
	}

	private void popupWarningDialog(){

		DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case Dialog.BUTTON_POSITIVE:
					enterVideoMenu();
					break;
				case Dialog.BUTTON_NEGATIVE:
					PermissionActivity.this.finish();
					break;
				default:
					break;
				}
			}
		};

		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setTitle(R.string.Warning);
		builder.setMessage(R.string.PermissionNotGrant);
		builder.setIcon(R.drawable.cactus2);
		builder.setPositiveButton(R.string.OK,dialogOnclicListener);
		builder.setNegativeButton(R.string.Cancel, dialogOnclicListener);
		builder.create().show();
	}

	private void enterVideoMenu(){
		Intent enterVideoMenu = new Intent(this, VideoMenu.class);
		startActivity(enterVideoMenu);
		finish();
	}

}


