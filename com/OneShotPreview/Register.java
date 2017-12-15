package com.OneShotPreview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Register
  extends Activity
{
  static List<String> history;
  static ArrayList<String> list;
  static List<String> removeItems;
  static String systemIP;
  static String userinfo = "";
  Button Uploadbtn;
  String ans;
  String ans1 = "";
  Button btn;
  CompoundButton buttonView;
  CheckBox cb;
  Button downloadBtn;
  EditText et;
  String full = "";
  int i = 1;
  String[] ip = new String[30];
  int j = 2;
  LinearLayout ll;
  int o;
  ObjectInputStream oinlogin;
  DataInputStream ois;
  DataOutputStream oos;
  ObjectOutputStream ooslogin;
  ObjectInputStream oosreg;
  Button remove;
  Socket soclogin;
  Socket socreg;
  Socket ss;
  ServerSocket sslogin;
  ServerSocket ssreg;
  ScrollView sv;
  int t;
  int tt;
  
  public void AddIp()
  {
    for (int k = 0;; k++)
    {
      if (k >= history.size()) {
        return;
      }
      this.cb = new CheckBox(this);
      this.cb.setText((CharSequence)history.get(k));
      this.et.setText("");
      this.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
      {
        public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
        {
          if (paramAnonymousBoolean) {
            Register.this.ans = ((String)paramAnonymousCompoundButton.getText());
          }
          Register localRegister = Register.this;
          localRegister.ans1 = (localRegister.ans1 + Register.this.ans + "#");
          Register.list.add(Register.this.ans);
        }
      });
      this.ll.addView(this.cb);
    }
  }
  
  public void ConnectSocket()
  {
    try
    {
      File localFile = new File("/sdcard/migration.apk");
      Socket localSocket = new Socket(this.et.getText().toString(), 4444);
      DisplayToast("Socket connected");
      ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localSocket.getOutputStream());
      FileInputStream localFileInputStream = new FileInputStream(localFile);
      byte[] arrayOfByte = new byte[localFileInputStream.available()];
      localFileInputStream.read(arrayOfByte);
      localFileInputStream.close();
      DisplayToast("Writing data");
      localObjectOutputStream.writeObject(arrayOfByte);
      DisplayToast("Data written");
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
        Toast.makeText(Register.this.getApplicationContext(), paramString, 6000000).show();
      }
    });
  }
  
  public void Receive()
  {
    new Thread()
    {
      public void run()
      {
        try
        {
          for (;;)
          {
            Register.this.ssreg = new ServerSocket(6666);
            Register.this.socreg = Register.this.ssreg.accept();
            Register.this.oosreg = new ObjectInputStream(Register.this.socreg.getInputStream());
            Register.userinfo = (String)Register.this.oosreg.readObject();
            Register.this.runOnUiThread(new Runnable()
            {
              public void run()
              {
                Toast.makeText(Register.this.getApplicationContext(), "Data received:: " + Register.userinfo, 600000).show();
              }
            });
            Register.this.ssreg.close();
            Register.this.socreg.close();
            Register.this.oosreg.close();
          }
        }
        catch (Exception localException)
        {
          System.out.println("EEEEEEEEEEEEE" + localException.getMessage());
          localException.printStackTrace();
        }
      }
    }.start();
  }
  
  public void Userlogin()
  {
    new Thread()
    {
      public void run()
      {
        for (;;)
        {
          try
          {
            Register.this.sslogin = new ServerSocket(5555);
            Register.this.soclogin = Register.this.sslogin.accept();
            Register.this.oinlogin = new ObjectInputStream(Register.this.soclogin.getInputStream());
            Register.this.ooslogin = new ObjectOutputStream(Register.this.soclogin.getOutputStream());
            if (((String)Register.this.oinlogin.readObject()).contains(Register.userinfo))
            {
              Register.this.ooslogin.writeObject("success");
              Register.this.ooslogin.close();
              Register.this.oinlogin.close();
              Register.this.soclogin.close();
              Register.this.sslogin.close();
              continue;
              continue;
            }
          }
          catch (Exception localException)
          {
            System.out.println("EEEEEEEEEEEEE" + localException.getMessage());
            localException.printStackTrace();
          }
          Register.this.ooslogin.writeObject("fail");
        }
      }
    }.start();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().addFlags(1024);
    requestWindowFeature(1);
    ((TelephonyManager)getSystemService("phone")).getDeviceId();
    if (list == null)
    {
      list = new ArrayList();
      removeItems = new ArrayList();
      history = new ArrayList();
    }
    this.sv = new ScrollView(this);
    this.ll = new LinearLayout(this);
    this.ll.setOrientation(1);
    this.sv.addView(this.ll);
    TextView localTextView = new TextView(this);
    localTextView.setText("Please enter ip Address here ");
    this.ll.addView(localTextView);
    this.et = new EditText(this);
    this.ll.addView(this.et);
    Button localButton = new Button(this);
    localButton.setText("Register");
    this.ll.addView(localButton);
    this.btn = new Button(this);
    this.btn.setText("Broadcast");
    this.ll.addView(this.btn);
    this.remove = new Button(this);
    this.remove.setText("About ");
    this.Uploadbtn = new Button(this);
    this.Uploadbtn.setText("Upload");
    this.Uploadbtn.setVisibility(8);
    this.ll.addView(this.Uploadbtn);
    this.downloadBtn = new Button(this);
    this.downloadBtn.setText("Download");
    this.ll.addView(this.downloadBtn);
    this.Uploadbtn.setVisibility(8);
    this.downloadBtn.setVisibility(8);
    this.ll.setBackgroundResource(2130837504);
    this.downloadBtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (Register.this.et.getText().toString().equalsIgnoreCase(""))
        {
          Toast.makeText(Register.this.getApplicationContext(), "Enter IP address.", 6000000).show();
          return;
        }
        Register.systemIP = Register.this.et.getText().toString();
      }
    });
    this.remove.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(Register.this, Splash.class);
        Register.this.startActivity(localIntent);
      }
    });
    this.Uploadbtn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (Register.this.et.getText().toString().equalsIgnoreCase(""))
        {
          Toast.makeText(Register.this.getApplicationContext(), "Enter IP address.", 6000000).show();
          return;
        }
        Register.systemIP = Register.this.et.getText().toString();
      }
    });
    localButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Register.this.cb = new CheckBox(Register.this);
        Register.this.cb.setText(Register.this.et.getText().toString());
        Register.this.et.setText("");
        Register.this.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
          public void onCheckedChanged(CompoundButton paramAnonymous2CompoundButton, boolean paramAnonymous2Boolean)
          {
            if (paramAnonymous2Boolean) {
              Register.this.ans = ((String)paramAnonymous2CompoundButton.getText());
            }
            Register localRegister = Register.this;
            localRegister.ans1 = (localRegister.ans1 + Register.this.ans + "#");
            Register.list.add(Register.this.ans);
          }
        });
        Register.this.ll.addView(Register.this.cb);
      }
    });
    this.btn.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        if (Register.list.size() > 3)
        {
          Toast.makeText(Register.this.getApplicationContext(), "Please slect only 3 ips", 60000).show();
          return;
        }
        Iterator localIterator = Register.list.iterator();
        for (;;)
        {
          if (!localIterator.hasNext())
          {
            Intent localIntent = new Intent(Register.this, OneShotPreviewActivity.class);
            localIntent.putExtra("key", Register.list);
            Register.this.startActivity(localIntent);
            return;
          }
          String str = (String)localIterator.next();
          Register.history.add(str);
        }
      }
    });
    setContentView(this.sv);
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.OneShotPreview.Register
 * JD-Core Version:    0.7.0.1
 */