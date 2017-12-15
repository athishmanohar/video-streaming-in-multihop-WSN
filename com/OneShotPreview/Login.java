package com.OneShotPreview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Login
  extends Activity
{
  EditText mIP;
  EditText mPwd;
  EditText mUsername;
  
  public void ConnectSocketget()
  {
    try
    {
      Socket localSocket = new Socket(this.mIP.getText().toString(), 5555);
      DisplayToast("Socket connected");
      ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localSocket.getOutputStream());
      ObjectInputStream localObjectInputStream = new ObjectInputStream(localSocket.getInputStream());
      DisplayToast("Writing data");
      localObjectOutputStream.writeObject(this.mUsername.getText().toString() + "#" + this.mPwd.getText().toString());
      if (((String)localObjectInputStream.readObject()).equalsIgnoreCase("success"))
      {
        DisplayToast("Login successs");
        startActivity(new Intent(this, Register.class));
      }
      for (;;)
      {
        localObjectOutputStream.close();
        localObjectInputStream.close();
        localSocket.close();
        return;
        DisplayToast("Login Fail");
      }
      return;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
      return;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
      return;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      localClassNotFoundException.printStackTrace();
    }
  }
  
  public void ConnectSocketreg()
  {
    try
    {
      Socket localSocket = new Socket(this.mIP.getText().toString(), 6666);
      DisplayToast("Socket connected");
      ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localSocket.getOutputStream());
      DisplayToast("Writing data");
      localObjectOutputStream.writeObject(this.mUsername.getText().toString() + "#" + this.mPwd.getText().toString());
      localObjectOutputStream.close();
      localSocket.close();
      return;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      localFileNotFoundException.printStackTrace();
      return;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
  }
  
  public void DisplayToast(final String paramString)
  {
    runOnUiThread(new Runnable()
    {
      public void run()
      {
        Toast.makeText(Login.this.getApplicationContext(), paramString, 600000).show();
      }
    });
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903045);
    Button localButton1 = (Button)findViewById(2131034116);
    Button localButton2 = (Button)findViewById(2131034122);
    this.mUsername = ((EditText)findViewById(2131034115));
    this.mPwd = ((EditText)findViewById(2131034118));
    this.mIP = ((EditText)findViewById(2131034129));
    localButton1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if ((Login.this.mUsername.getText().toString().equals("admin")) || (Login.this.mPwd.equals("admin")))
        {
          Intent localIntent = new Intent(Login.this, Register.class);
          Login.this.startActivity(localIntent);
          return;
        }
        Toast.makeText(Login.this.getApplicationContext(), "Invalid credentials", 60000000).show();
      }
    });
    localButton2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        new Thread()
        {
          public void run()
          {
            Login.this.ConnectSocketreg();
          }
        }.start();
      }
    });
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.OneShotPreview.Login
 * JD-Core Version:    0.7.0.1
 */