/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import saltr.SLTRegisterDeviceApiCall;
import saltr.SLTRegisterDeviceDelegate;
import saltr.response.SLTResponseTemplate;

public class SLTRegisterDeviceDialogBuilder extends AlertDialog.Builder {
    private EditText email;
    private LinearLayout layout;

    public SLTRegisterDeviceDialogBuilder(Context context) {
        super(context);
        email = new EditText(getContext());
        layout = new LinearLayout(getContext());

        email.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        email.setLines(1);
        email.setHint("myemail@example.com");
        layout.addView(email);
        layout.setOrientation(1);

        setTitle("Register Device with SALTR");
        setView(layout);
        setNegativeButton("Cancel", null);
        setPositiveButton("Submit", null);
    }

    public void showDialog(final boolean devMode, final int timeout, final String clientKey, final String deviceId) {
        final AlertDialog dialog = create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Editable editableEmail = email.getText();

                        SLTDeviceDetails deviceDetails = new SLTDeviceDetails();
                        String model = deviceDetails.getDeviceName();
                        String os = deviceDetails.getOsVersion();

                        SLTRegisterDeviceApiCall apiCall = new SLTRegisterDeviceApiCall(timeout, devMode,
                                editableEmail.toString(), clientKey, deviceId, model, os);
                        apiCall.call(new SLTRegisterDeviceDelegate() {
                            @Override
                            public void onSuccess(SLTResponseTemplate response) {
                                if (response.getSuccess()) {
                                    dialog.dismiss();
                                }
                                else {
                                    Toast toast = Toast.makeText(getContext(), response.getError().getMessage(), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }

                            @Override
                            public void onFailure() {
                                Toast toast = Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                });
                Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }
}
