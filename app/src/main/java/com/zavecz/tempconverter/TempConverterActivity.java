package com.zavecz.tempconverter;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.zavecz.tempconverter.R;

import java.text.NumberFormat;

public class TempConverterActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    //define widgets
    private EditText mFahrenhietET;
    private TextView mCelciusConvertedTV;
    private float celcius;

    private String mFahrenhietETString = "";

    //define SharedPrefferences object
    private SharedPreferences mSavedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_converter);

        //get refferences to widgets
        mFahrenhietET = (EditText) findViewById(R.id.mFarenhietET);
        mCelciusConvertedTV = (TextView) findViewById(R.id.mCelciusConvertedTV);

        //set the listeners
        mFahrenhietET.setOnEditorActionListener(this);

        //get SharedPreferences object
        mSavedValues = getSharedPreferences("savedValues", MODE_PRIVATE);

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED){
            calculateAndDisplay();
        }
        return false;
    }

    private void calculateAndDisplay() {

        //get fahrenhiet from user
        mFahrenhietETString = mFahrenhietET.getText().toString();
        float fahrenhiet;
        if(mFahrenhietETString.equals("")){
            fahrenhiet = 0;
        } else{
            fahrenhiet = Float.parseFloat(mFahrenhietETString);
        }

        //calculate and display
        celcius = (fahrenhiet-32) * 5/9;
        NumberFormat degrees = NumberFormat.getInstance();
        mCelciusConvertedTV.setText(degrees.format(celcius));

    }

    @Override
    protected void onPause() {

        //save the instance variables
        SharedPreferences.Editor editor = mSavedValues.edit();
        editor.putString("fahrenhietString", mFahrenhietETString);
        editor.putFloat("celcius", celcius);
        editor.apply();

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //get instance variables
        mFahrenhietETString = mSavedValues.getString("fahrenhietString", "");
        celcius = mSavedValues.getFloat("celcius", celcius);

        //set the bill amount on its widget
        mFahrenhietET.setText(mFahrenhietETString);

        //call the calculate and display method
        calculateAndDisplay();
    }
}
