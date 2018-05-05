package trkus.services.com.trkus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class UserTypeSelection extends AppCompatActivity {

    FrameLayout customer,seller;
    Button btn_confirm,btn_previous;
    Intent intent;
    String UserTypeId,MobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usertype);

        intent = getIntent();
        UserTypeId=intent.getStringExtra("UserTypeId");
        MobileNumber=intent.getStringExtra("MobileNumber");

        btn_previous=(Button)findViewById(R.id.btn_previous);
        btn_confirm=(Button)findViewById(R.id.btn_confirm);
        customer=(FrameLayout)findViewById(R.id.customer);
        seller=(FrameLayout)findViewById(R.id.seller);

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verification=new Intent(UserTypeSelection.this,UserDetailForm.class);
                verification.putExtra("MobileNumber",MobileNumber);
                verification.putExtra("UserTypeId","1");
                startActivity(verification);
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verification=new Intent(UserTypeSelection.this,UserDetailForm.class);
                verification.putExtra("MobileNumber",MobileNumber);
                verification.putExtra("UserTypeId","2");
                startActivity(verification);
            }
        });
    }
}
