package com.vikendu.currency_converter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView mTextView, output, dollar, last_update;
    EditText mEditText;
    Button mButton, mButton2;
    double curr_final = 0.0d;

    public double reduce_deci(double in)
    {
        long factor = (long) Math.pow(10, 2);
        in = in * factor;
        long tmp = Math.round(in);

        return (double) tmp / factor;
    }
    public void enter_val()
    {
            Toast.makeText(MainActivity.this, "Enter Some Amount", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = findViewById(R.id.button2);
        mEditText = findViewById(R.id.editText2);
        mTextView = findViewById(R.id.textView4);
        output = findViewById(R.id.textView);
        dollar = findViewById(R.id.textView3);
        mButton2 = findViewById(R.id.switch_but);
        last_update = findViewById(R.id.textView2);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://free.currencyconverterapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        currencyService c1 = retrofit.create(currencyService.class);
        c1.getExchange().enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                currency currencyObj = response.body();

                curr_final = (currencyObj.USD_INR);

                dollar.setText("Latest Price ₹"+reduce_deci(curr_final));

                Date currentTime = Calendar.getInstance().getTime();
                last_update.setText("Last Updated: "+currentTime);
            }
                @Override
                public void onFailure(Call<currency> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                }
            });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Double res;
                if(mEditText.getText().toString().equals(""))
                {
                    enter_val();
                    output.setText("");
                }
                else if (mTextView.getText().toString().equals("Enter amount in ₹:")) {
                    res = Double.parseDouble(mEditText.getText().toString()) / (double) curr_final;
                    output.setText("$" + reduce_deci(res));
                }
                else {
                    res = Double.parseDouble(mEditText.getText().toString()) * (double) curr_final;
                    output.setText("₹" + reduce_deci(res));
                     }
            }
            });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTextView.getText().toString().equals("Enter amount in ₹:")) {
                    mTextView.setText("Enter amount in $:");
                    mButton2.setText("Switch $ with ₹");

                    if(!(mEditText.getText().toString().equals(""))) {

                        double res = Double.parseDouble(mEditText.getText().toString()) * (double) curr_final;
                        output.setText("₹" + reduce_deci(res));
                    }
                }
                else
                {
                    mTextView.setText("Enter amount in ₹:");
                    mButton2.setText("Switch ₹ with $");

                    if(!(mEditText.getText().toString().equals(""))) {

                        double res = Double.parseDouble(mEditText.getText().toString()) / (double) curr_final;
                        output.setText("$" + reduce_deci(res));
                    }
                }
                    }
                });
            }
            }

