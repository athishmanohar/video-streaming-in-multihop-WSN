package com.OneShotPreview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class Home
  extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().addFlags(1024);
    requestWindowFeature(1);
    setContentView(2130903043);
    Button localButton1 = (Button)findViewById(2131034116);
    Button localButton2 = (Button)findViewById(2131034122);
    ((Button)findViewById(2131034119)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(Home.this, Splash.class);
        Home.this.startActivity(localIntent);
      }
    });
    localButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(Home.this, Register.class);
        Home.this.startActivity(localIntent);
      }
    });
    localButton2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(Home.this, AndroidReceiverActivity.class);
        Home.this.startActivity(localIntent);
      }
    });
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.OneShotPreview.Home
 * JD-Core Version:    0.7.0.1
 */