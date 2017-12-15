package com.OneShotPreview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Main
  extends Activity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903042);
    final EditText localEditText = (EditText)findViewById(2131034115);
    ((Button)findViewById(2131034116)).setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(Main.this, OneShotPreviewActivity.class);
        localIntent.putExtra("ip", localEditText.getText().toString());
        Main.this.startActivity(localIntent);
      }
    });
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.OneShotPreview.Main
 * JD-Core Version:    0.7.0.1
 */