package edu.android.and59_permission2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


// 안드로이드 OS가 6.0 (Marshmallow) 이상이고
// build.gradle  파일에서 targetSdkVersion 23 이상으로 설정한 경우
// Manifest 파일에서 <uses-permission>  으로 설정한 권한들 중에서
// 위험한 그룹에 속하는 권한들은 앱이 실행 중에 권한 요청을 추가로 해야함.


public class MainActivity extends AppCompatActivity {

    private static final String TAG ="edu.android" ;
    private static final int REQ_PERM_SMS = 1; // 0~255 , -128~127 (1byte 의 숫자만 사용가능하다 )
    private static final int REQ_PERM_CONTACT_AND_LOCATION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendSmsByMethod(View view) {
    //  sendSms();
        // Java code로 권한 요청을 하지 않고 위험한 권한을 사용하려고 하면
        // SecurityException 발생

        // Manifest 파일에 설정한 권한을 획득했는지 아닌 지 체크
        int check = ActivityCompat.checkSelfPermission(this // context 정보
                , Manifest.permission.SEND_SMS); // permission의 정보
        //결과값 보기
        if(check == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "권한 획득한 상태 ");
            sendSms();
        }else {
            Log.i(TAG, "권한 없는 상태 ");
            
            //사용자에게 권한이 필요하다는 메세지를 보여주고, 사용자로 하여금 권한을 설정할 지 말 지 를 결정
            String[] permissions = {Manifest.permission.SEND_SMS};
            ActivityCompat.requestPermissions(this, permissions, REQ_PERM_SMS);





        }




    }

    private void sendSms() {
        String phoneNo = "01012345678";
        EditText editText = (EditText) findViewById(R.id.editText);
        String msg = editText.getText().toString();


        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phoneNo, null, msg , null, null);


        Toast.makeText(this, "SMS SendTo Success!", Toast.LENGTH_SHORT).show();


    }


    // requestPermission() 메소드 호출 후 ,
    // 그 결과값을 받는 메소드를 override
    @Override
    public void onRequestPermissionsResult(int requestCode, // sms 요청시 보냈던 requestCode를 보내주고
                                           @NonNull String[] permissions, // 어떤 요청에 대한 결과냐 (두개 이상이면 두개 이상이 온다)
                                           @NonNull int[] grantResults) { // 허용했는지 안했는지에대한 결과 ( 하나면 하나 오고 두개면 두개온다)
// permission 순서 맞춰서 하나 보내면(String[] permissions) 밑에거에(int[] grantResults) 하나 가고 두개 보내면 밑에거에 두개 간다.
        switch (requestCode) {
            case REQ_PERM_SMS:
                // 사용자가 권한을 허용했는 지 여부를 매개변수를 통해서 확인
                if(permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.i(TAG, "사용자가 SMS 권한 허용함");
                    sendSms();
                }else {
                    Log.i(TAG, "사용자가 권한을 허용하지 않음");
                    Toast.makeText(this, "권한이 없어서 SMS 전송할 수 없어요'^'", Toast.LENGTH_SHORT).show();

                }
                break;
            case REQ_PERM_CONTACT_AND_LOCATION:
                for( int i = 0; i<permissions.length; i++) {
                    Log.i(TAG, permissions[i] + " : " + grantResults[i]);
                }
                break;
        }



    }

    public void requestContactLocation(View view) {

        // 연락처 사용할 수 있는지 검사
        int checkContact = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);


        // 위치 사용할 수 있는지 검사
        int checkLocation = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);



        //경우의 수 나눠보자 ( 한꺼번에 보낼 수 없다고 하면)
        if(checkContact == PackageManager.PERMISSION_GRANTED &&
                checkLocation == PackageManager.PERMISSION_GRANTED) {
            // 이경우는 모든 권한이 있으니까 하고싶은거 하면된다.
            Toast.makeText(this, "contact & location 권한 있음", Toast.LENGTH_SHORT).show();



        }else if (checkContact != PackageManager.PERMISSION_GRANTED &&
                checkLocation != PackageManager.PERMISSION_GRANTED) {
            // 둘 다 아닌 경우 연락처&위치 권한을 모두 요청해야한다.
            Log.i(TAG, "연락처 & 위치 권한 모두 없음 ");
            String[] permissions = {
                    // 요청할 권한의 정보들을 배열로 준다.
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.ACCESS_FINE_LOCATION

            };
            ActivityCompat.requestPermissions(this, permissions, REQ_PERM_CONTACT_AND_LOCATION);
            //method바로쓰니까 static 메소드



        }else if (checkContact ==PackageManager.PERMISSION_GRANTED &&
                checkLocation != PackageManager.PERMISSION_GRANTED){

        }else if(checkContact != PackageManager.PERMISSION_GRANTED &&
                checkLocation == PackageManager.PERMISSION_GRANTED) {


        } // end if()






    }











}
