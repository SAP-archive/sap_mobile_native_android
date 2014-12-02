package com.sap.dcode.agency.ui.components;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sap.dcode.agency.R;
import com.sap.dcode.util.TraceLog;

public class ConfirmationDialog extends Dialog {

    public static final int YES = 0;
    public static final int NO = 1;

    private DialogListener listener;
    private String message;
    private String additionalText;
    private String additionalText2;

    /**
     * Constructor.
     *
     * @param context The context in which this dialog is being displayed
     * @param message The message (i.e. title) at the top of the dialog
     */
    public ConfirmationDialog(Context context, String message) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // dont use this, can't set background
        setContentView(R.layout.component_confirmation_dialog);
        this.message = message;
    }

    /**
     * Constructor.
     *
     * @param context The context in which this dialog is being displayed
     * @param message The message (i.e. title) at the top of the dialog
     * @param additionalText The first line of additional text
     */
    public ConfirmationDialog(Context context, String message, String additionalText) {
        this(context, message);
        this.additionalText = additionalText;
    }

    /**
     * Constructor.
     *
     * @param context The context in which this dialog is being displayed
     * @param message The message (i.e. title) at the top of the dialog
     * @param additionalText The first line of additional text
     * @param additionalText2 The second line of additional text
     */
    public ConfirmationDialog(Context context, String message, String additionalText,  String additionalText2) {
        this(context, message, additionalText);
        this.additionalText2 = additionalText2;
    }

    /**
     * Set a DialogListener.
     * @param listener
     */
    public void setListener(DialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setCancelable(true);

        TextView title = (TextView) findViewById(R.id.Title);
        title.setText(message);

        setAdditionalText(additionalText);
        setAdditionalText2(additionalText2);

        Button confirmBtn = (Button) findViewById(R.id.ConfirmButton);
        Button cancelBtn = (Button) findViewById(R.id.CancelButton);

        /*
         * Listener for the "confirm" button. If the user clicks this button then
         * the dialog is dismissed and passes YES to listener.onUserAction().
         */
        confirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onConfirm();
            }
        });

        /*
         * Listener for the "cancel" button. If the user clicks on this button then the
         * dialog is dismissed and passes NO to listener.onUserAction().
         */
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }

    protected void setAdditionalText(final String text) {

        TextView addText = (TextView) findViewById(R.id.AdditionalText);
        if (text != null) {
            addText.setText(text);
            addText.setVisibility(View.VISIBLE);
        } else {
            addText.setVisibility(View.GONE);
        }

    }

    protected void setAdditionalText2(final String text) {
        TraceLog.scoped(this).d("setAdditionalText2", text);

        TextView addText2 = (TextView) findViewById(R.id.AdditionalText2);
        if (text != null) {
            addText2.setText(text);
            addText2.setVisibility(View.VISIBLE);
        } else {
            addText2.setVisibility(View.GONE);
        }
    }

    protected void onConfirm() {
        //close this dialog
        dismiss();
        listener.onUserAction(YES);
    }

    protected void onCancel() {
        //close this dialog
        dismiss();
        listener.onUserAction(NO);
    }

}
