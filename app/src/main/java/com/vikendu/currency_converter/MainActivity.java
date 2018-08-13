package com.vikendu.currency_converter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView mTextView, output, dollar;
    EditText mEditText;
    Button mButton, mButton2;
    float curr_final = 0.0f;

    public double reduce_deci(double in)
    {
        long factor = (long) Math.pow(10, 2);
        in = in * factor;
        long tmp = Math.round(in);

        return (double) tmp / factor;
        }
//    public float currency(float in2)
//    {
//        curr_final = in2;
//        return curr_final;
//    }
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
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://free.currencyconverterapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        currencyServive c1 = retrofit.create(currencyServive.class);
        c1.getExchange().enqueue(new Callback<currency>() {
            @Override
            public void onResponse(Call<currency> call, Response<currency> response) {
                //List<currency> currencyList = Response.body();
                currency currencyObj = response.body();
                //assert currencyObj != null;
                float f = (currencyObj.USD_INR);
                curr_final = f;
                dollar.setText("Today's Price ₹"+f);
                Double res;
                if(mEditText.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this, "Enter Some Amount", Toast.LENGTH_SHORT).show();
                    output.setText("");
                }
                else if (mTextView.getText().toString().equals("Enter amount in ₹:")) {
                    res = Double.parseDouble(mEditText.getText().toString()) / (double) f;
                    output.setText("$" + reduce_deci(res));
                }
                else {
                    res = Double.parseDouble(mEditText.getText().toString()) * (double) f;
                    //Log.d("obtained value",Float.toString(res));
                    output.setText("₹" + reduce_deci(res));

                     }
            }
            @Override
            public void onFailure(Call<currency> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mTextView.getText().toString().equals("Enter amount in ₹:")) {
                    mTextView.setText("Enter amount in $:");
                    mButton2.setText("Switch $ with ₹");
//                    double f = Double.parseDouble(dollar.getText().toString());
                    if(!(mEditText.getText().toString().equals(""))) {
                        double res = Double.parseDouble(mEditText.getText().toString()) * (double) curr_final;
                        output.setText("₹" + reduce_deci(res));
                    }

                }
                else

                {
                    mTextView.setText("Enter amount in ₹:");
                    mButton2.setText("Switch ₹ with $");
//                    double f = Double.parseDouble(dollar.getText().toString());
                    if(!(mEditText.getText().toString().equals(""))) {
                        double res = Double.parseDouble(mEditText.getText().toString()) / (double) curr_final;
                        output.setText("$" + reduce_deci(res));
                    }
                }
                currencyServive c1 = retrofit.create(currencyServive.class);
                c1.getExchange().enqueue(new Callback<currency>() {
                    @Override
                    public void onResponse(Call<currency> call, Response<currency> response) {
                        //List<currency> currencyList = Response.body();
                        currency currencyObj = response.body();
                        //assert currencyObj != null;
                        float f = (currencyObj.USD_INR);
                        dollar.setText("Today's Price ₹"+f);
                    }
                    @Override
                    public void onFailure(Call<currency> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
