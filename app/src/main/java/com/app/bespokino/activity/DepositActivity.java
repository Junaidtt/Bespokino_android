package com.app.bespokino.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.bespokino.R;

public class DepositActivity extends AppCompatActivity {

    Button submitButton;
    EditText firstName,lastName,address,city,state,zipCode,country,email,cellNumber,creditCardType,creditCardNumber,expMonth,expYear,edCSV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        firstName =(EditText)findViewById(R.id.edFirstName);
        lastName =(EditText)findViewById(R.id.edLastName);
        address =(EditText)findViewById(R.id.edAddress);
        city =(EditText)findViewById(R.id.edCity);
        state =(EditText)findViewById(R.id.edState);
        zipCode =(EditText)findViewById(R.id.edZipcode);
        country =(EditText)findViewById(R.id.edCountry);
        email =(EditText)findViewById(R.id.edEmail);
        cellNumber =(EditText)findViewById(R.id.edPhonenumber);
        creditCardType =(EditText)findViewById(R.id.edCardType);
        creditCardNumber =(EditText)findViewById(R.id.edCreditCardNumber);
        expMonth = (EditText)findViewById(R.id.edExMonth);
        expYear =(EditText)findViewById(R.id.edExYear);
        edCSV = (EditText)findViewById(R.id.edExCSV);

        submitButton = (Button)findViewById(R.id.submitButton);


        final String firstname= firstName.getText().toString();
        final String lastname =  lastName.getText().toString();
        final String addressDetails =  address.getText().toString();
        final String citname = city.getText().toString();
        final String statename= state.getText().toString();
        final String zipcode = zipCode.getText().toString();
        final String countryname = country.getText().toString();
        final String emailDetail = email.getText().toString();
        final String cellnumber = cellNumber.getText().toString();
        final String creditCardtype = creditCardType.getText().toString();
        final String creditcardnumber = creditCardNumber.getText().toString();
        final String expmonth = expMonth.getText().toString();
        final String expyear = expYear.getText().toString();
        final String edcsv = edCSV.getText().toString();


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(DepositActivity.this,SelfMeasuringToolActivity.class);
                startActivity(intent);

             /*   if(firstname.isEmpty()||lastname.isEmpty()||addressDetails.isEmpty()||citname.isEmpty()||statename.isEmpty()||zipcode.isEmpty()||
                        countryname.isEmpty()||emailDetail.isEmpty()||cellnumber.isEmpty()||creditCardtype.isEmpty()||creditcardnumber.isEmpty()
                        ||expmonth.isEmpty()||expyear.isEmpty()||edcsv.isEmpty()){

                Toast.makeText(getApplicationContext(),"Check all fields are not empty",Toast.LENGTH_SHORT).show();

                }
                else{

                }*/
            }
        });





    }
}
