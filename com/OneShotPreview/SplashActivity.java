package com.OneShotPreview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;

public class SplashActivity
  extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().addFlags(1024);
    requestWindowFeature(1);
    setContentView(2130903049);
    ((TelephonyManager)getSystemService("phone")).getDeviceId();
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        SplashActivity.this.finish();
        Intent localIntent = new Intent(SplashActivity.this, Home.class);
        SplashActivity.this.startActivity(localIntent);
      }
    }, 5000L);
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.OneShotPreview.SplashActivity
 * JD-Core Version:    0.7.0.1
 */