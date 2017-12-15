package com.OneShotPreview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;

public class Splash
  extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().addFlags(1024);
    requestWindowFeature(1);
    setContentView(2130903048);
    ((TelephonyManager)getSystemService("phone")).getDeviceId();
    new Handler().postDelayed(new Runnable()
    {
      public void run() {}
    }, 100L);
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.OneShotPreview.Splash
 * JD-Core Version:    0.7.0.1
 */