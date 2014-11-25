/*
 * Copyright (c) 2014 Plexonic Ltd
 */
package saltr;

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
import saltr.response.SLTResponseTemplate;

public class SLTIdentityDialogBuilder extends AlertDialog.Builder {
    private EditText name;
    private EditText email;
    private LinearLayout layout;

    public SLTIdentityDialogBuilder(Context context) {
        super(context);

        name = new EditText(getContext());
        email = new EditText(getContext());
        layout = new LinearLayout(getContext());

        name.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        email.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        name.setLines(1);
        email.setLines(1);
        name.setHint("Device Name");
        email.setHint("myemail@example.com");
        layout.addView(email);
        layout.addView(name);
        layout.setOrientation(1);

        setTitle("Register Device with SALTR");
        setView(layout);
        setPositiveButton("Ok", null);
        setNegativeButton("Cancel", null);
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
                        Editable editableName = name.getText();
                        Editable editableEmail = email.getText();

                        SLTAddDeviceToSaltrApiCall apiCall = new SLTAddDeviceToSaltrApiCall(timeout, devMode, editableName.toString(), editableEmail.toString(), clientKey, deviceId);
                        apiCall.call(new SLTAddDeviceDelegate() {
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
