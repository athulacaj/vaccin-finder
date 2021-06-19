package co.athulacaj.vaccinefinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent myServiceIntent;
    private EditText editPinInput;
    private EditText editTimeInput;
private  String pinCode;
private  int refreshTime=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editPinInput = findViewById(R.id.txtPin);
        editTimeInput = findViewById(R.id.txtTime);

    }


    public void startService(View v) throws IOException {
        pinCode= editPinInput.getText().toString();
        if (editTimeInput.getText().toString().length()>0) {
            refreshTime = Integer.parseInt(editTimeInput.getText().toString());
        }
        if(pinCode.length()==6 && refreshTime>=1) {
            Intent serviceIntent = new Intent(this, MyService.class);
            serviceIntent.putExtra("pinCode", pinCode);
            serviceIntent.putExtra("refreshTime", refreshTime);
            ContextCompat.startForegroundService(this, serviceIntent);
            Intent i=new Intent(this,VaccineActivity.class);
            startActivity(i);
        }else if(refreshTime<5) {
            Toast.makeText(this,"refresh time must be minimum 5 mnts",Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(this,"Enter a valid pincode!!",Toast.LENGTH_LONG).show();

        }
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, MyService.class);
        stopService(serviceIntent);

    }

    @Override
    public void onClick(View v) {

    }
}

