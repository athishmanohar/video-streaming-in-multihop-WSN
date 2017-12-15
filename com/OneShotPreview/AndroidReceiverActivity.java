package com.OneShotPreview;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class AndroidReceiverActivity
  extends Activity
{
  static boolean isBroadcast = false;
  static int mDeviceCount = 0;
  Button capture;
  EditText et;
  Bitmap image;
  ImageView iv;
  Button mSend;
  EditText mText;
  ObjectInputStream objectoutput1;
  ObjectInputStream oos;
  ObjectOutputStream oos1;
  ObjectOutputStream oos2;
  Socket soc;
  byte[] socdata;
  Socket socket;
  Socket socket1;
  ServerSocket ss;
  
  private void ConnectSocket(final byte[] paramArrayOfByte)
  {
    new Thread()
    {
      public void run()
      {
        try
        {
          AndroidReceiverActivity.this.DisplayToast("writing data");
          System.out.println(AndroidReceiverActivity.this.oos1);
          if ((paramArrayOfByte != null) && (AndroidReceiverActivity.this.oos1 != null))
          {
            AndroidReceiverActivity.this.oos1.writeObject(paramArrayOfByte);
            AndroidReceiverActivity.this.oos1.flush();
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
        }
      }
    }.start();
  }
  
  private void ConnectSocket2(final byte[] paramArrayOfByte)
  {
    new Thread()
    {
      public void run()
      {
        try
        {
          System.out.println(AndroidReceiverActivity.this.oos1);
          if ((paramArrayOfByte != null) && (AndroidReceiverActivity.this.oos1 != null))
          {
            AndroidReceiverActivity.this.oos2.writeObject(paramArrayOfByte);
            AndroidReceiverActivity.this.oos2.flush();
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
        }
      }
    }.start();
  }
  
  public byte[] ConvertToBytes()
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inPreferredConfig = Bitmap.Config.RGB_565;
    Bitmap localBitmap = BitmapFactory.decodeFile("/sdcard/arathi.jpg", localOptions);
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localBitmap.compress(Bitmap.CompressFormat.JPEG, 100, localByteArrayOutputStream);
    return localByteArrayOutputStream.toByteArray();
  }
  
  public void DisplayToast(final String paramString)
  {
    runOnUiThread(new Runnable()
    {
      public void run()
      {
        Toast.makeText(AndroidReceiverActivity.this.getApplicationContext(), paramString, 6000000).show();
      }
    });
  }
  
  public void SaveImage(byte[] paramArrayOfByte, String paramString)
  {
    try
    {
      System.out.println("the data is to be " + paramArrayOfByte);
      FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
      localFileOutputStream.write(paramArrayOfByte);
      localFileOutputStream.close();
      Log.d("Camera", "onPictureTaken - wrote bytes: " + paramArrayOfByte.length);
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
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    getWindow().addFlags(1024);
    requestWindowFeature(1);
    setContentView(2130903047);
    this.mText = ((EditText)findViewById(2131034131));
    this.mSend = ((Button)findViewById(2131034132));
    this.mSend.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SmsManager.getDefault().sendTextMessage(AndroidReceiverActivity.this.mText.getText().toString(), null, "S1", null, null);
      }
    });
    ((TelephonyManager)getSystemService("phone")).getDeviceId();
    Button localButton = (Button)findViewById(2131034133);
    this.et = ((EditText)findViewById(2131034130));
    this.capture = ((Button)findViewById(2131034134));
    this.capture.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AndroidReceiverActivity.this.SaveImage(AndroidReceiverActivity.this.socdata, "/sdcard/share/" + System.currentTimeMillis() + ".jpg");
      }
    });
    localButton.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        final String[] arrayOfString = AndroidReceiverActivity.this.et.getText().toString().split("\\,");
        Toast.makeText(AndroidReceiverActivity.this.getApplicationContext(), arrayOfString.length, 60000).show();
        System.out.println(arrayOfString.length);
        if (AndroidReceiverActivity.this.et.getText().toString().equals(""))
        {
          Toast.makeText(AndroidReceiverActivity.this.getApplicationContext(), "Enter ip", 6000).show();
          return;
        }
        if (arrayOfString.length == 2)
        {
          AndroidReceiverActivity.mDeviceCount = 2;
          new Thread()
          {
            public void run()
            {
              try
              {
                AndroidReceiverActivity.this.socket = new Socket(arrayOfString[0], 1234);
                AndroidReceiverActivity.this.DisplayToast("Socket connected");
                AndroidReceiverActivity.this.oos1 = new ObjectOutputStream(AndroidReceiverActivity.this.socket.getOutputStream());
                AndroidReceiverActivity.isBroadcast = true;
                return;
              }
              catch (UnknownHostException localUnknownHostException)
              {
                localUnknownHostException.printStackTrace();
                return;
              }
              catch (IOException localIOException)
              {
                localIOException.printStackTrace();
              }
            }
          }.start();
          new Thread()
          {
            public void run()
            {
              try
              {
                AndroidReceiverActivity.this.socket1 = new Socket(arrayOfString[1], 1234);
                AndroidReceiverActivity.this.DisplayToast("Socket connected");
                AndroidReceiverActivity.this.oos2 = new ObjectOutputStream(AndroidReceiverActivity.this.socket1.getOutputStream());
                AndroidReceiverActivity.isBroadcast = true;
                return;
              }
              catch (UnknownHostException localUnknownHostException)
              {
                localUnknownHostException.printStackTrace();
                return;
              }
              catch (IOException localIOException)
              {
                localIOException.printStackTrace();
              }
            }
          }.start();
          return;
        }
        if (arrayOfString.length == 1)
        {
          AndroidReceiverActivity.mDeviceCount = 1;
          new Thread()
          {
            public void run()
            {
              try
              {
                AndroidReceiverActivity.this.socket = new Socket(arrayOfString[0], 1234);
                AndroidReceiverActivity.this.DisplayToast("Socket connected");
                AndroidReceiverActivity.this.oos1 = new ObjectOutputStream(AndroidReceiverActivity.this.socket.getOutputStream());
                AndroidReceiverActivity.isBroadcast = true;
                return;
              }
              catch (UnknownHostException localUnknownHostException)
              {
                localUnknownHostException.printStackTrace();
                return;
              }
              catch (IOException localIOException)
              {
                localIOException.printStackTrace();
              }
            }
          }.start();
          return;
        }
        Toast.makeText(AndroidReceiverActivity.this.getApplicationContext(), "No of devices exceeded", 600000).show();
      }
    });
    this.iv = ((ImageView)findViewById(2131034135));
    new Thread()
    {
      /* Error */
      public void run()
      {
        // Byte code:
        //   0: aload_0
        //   1: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   4: ldc 25
        //   6: invokevirtual 29	com/OneShotPreview/AndroidReceiverActivity:DisplayToast	(Ljava/lang/String;)V
        //   9: aload_0
        //   10: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   13: new 31	java/net/ServerSocket
        //   16: dup
        //   17: sipush 1234
        //   20: invokespecial 34	java/net/ServerSocket:<init>	(I)V
        //   23: putfield 38	com/OneShotPreview/AndroidReceiverActivity:ss	Ljava/net/ServerSocket;
        //   26: aload_0
        //   27: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   30: ldc 40
        //   32: invokevirtual 29	com/OneShotPreview/AndroidReceiverActivity:DisplayToast	(Ljava/lang/String;)V
        //   35: aload_0
        //   36: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   39: aload_0
        //   40: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   43: getfield 38	com/OneShotPreview/AndroidReceiverActivity:ss	Ljava/net/ServerSocket;
        //   46: invokevirtual 44	java/net/ServerSocket:accept	()Ljava/net/Socket;
        //   49: putfield 48	com/OneShotPreview/AndroidReceiverActivity:soc	Ljava/net/Socket;
        //   52: aload_0
        //   53: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   56: new 50	java/io/ObjectInputStream
        //   59: dup
        //   60: aload_0
        //   61: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   64: getfield 48	com/OneShotPreview/AndroidReceiverActivity:soc	Ljava/net/Socket;
        //   67: invokevirtual 56	java/net/Socket:getInputStream	()Ljava/io/InputStream;
        //   70: invokespecial 59	java/io/ObjectInputStream:<init>	(Ljava/io/InputStream;)V
        //   73: putfield 63	com/OneShotPreview/AndroidReceiverActivity:oos	Ljava/io/ObjectInputStream;
        //   76: getstatic 69	java/lang/System:out	Ljava/io/PrintStream;
        //   79: ldc 71
        //   81: invokevirtual 76	java/io/PrintStream:println	(Ljava/lang/String;)V
        //   84: getstatic 69	java/lang/System:out	Ljava/io/PrintStream;
        //   87: ldc 78
        //   89: invokevirtual 76	java/io/PrintStream:println	(Ljava/lang/String;)V
        //   92: getstatic 69	java/lang/System:out	Ljava/io/PrintStream;
        //   95: ldc 80
        //   97: invokevirtual 76	java/io/PrintStream:println	(Ljava/lang/String;)V
        //   100: aload_0
        //   101: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   104: aload_0
        //   105: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   108: getfield 63	com/OneShotPreview/AndroidReceiverActivity:oos	Ljava/io/ObjectInputStream;
        //   111: invokevirtual 84	java/io/ObjectInputStream:readObject	()Ljava/lang/Object;
        //   114: checkcast 86	[B
        //   117: putfield 89	com/OneShotPreview/AndroidReceiverActivity:socdata	[B
        //   120: getstatic 69	java/lang/System:out	Ljava/io/PrintStream;
        //   123: aload_0
        //   124: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   127: getfield 89	com/OneShotPreview/AndroidReceiverActivity:socdata	[B
        //   130: invokevirtual 92	java/io/PrintStream:println	(Ljava/lang/Object;)V
        //   133: aload_0
        //   134: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   137: aload_0
        //   138: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   141: getfield 89	com/OneShotPreview/AndroidReceiverActivity:socdata	[B
        //   144: iconst_0
        //   145: aload_0
        //   146: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   149: getfield 89	com/OneShotPreview/AndroidReceiverActivity:socdata	[B
        //   152: arraylength
        //   153: invokestatic 98	android/graphics/BitmapFactory:decodeByteArray	([BII)Landroid/graphics/Bitmap;
        //   156: putfield 102	com/OneShotPreview/AndroidReceiverActivity:image	Landroid/graphics/Bitmap;
        //   159: new 104	com/OneShotPreview/AndroidReceiverActivity$4$1
        //   162: dup
        //   163: aload_0
        //   164: invokespecial 107	com/OneShotPreview/AndroidReceiverActivity$4$1:<init>	(Lcom/OneShotPreview/AndroidReceiverActivity$4;)V
        //   167: invokevirtual 110	com/OneShotPreview/AndroidReceiverActivity$4$1:start	()V
        //   170: aload_0
        //   171: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   174: new 112	com/OneShotPreview/AndroidReceiverActivity$4$2
        //   177: dup
        //   178: aload_0
        //   179: invokespecial 113	com/OneShotPreview/AndroidReceiverActivity$4$2:<init>	(Lcom/OneShotPreview/AndroidReceiverActivity$4;)V
        //   182: invokevirtual 117	com/OneShotPreview/AndroidReceiverActivity:runOnUiThread	(Ljava/lang/Runnable;)V
        //   185: getstatic 121	com/OneShotPreview/AndroidReceiverActivity:isBroadcast	Z
        //   188: ifeq +32 -> 220
        //   191: getstatic 125	com/OneShotPreview/AndroidReceiverActivity:mDeviceCount	I
        //   194: iconst_2
        //   195: if_icmpne +107 -> 302
        //   198: new 127	com/OneShotPreview/AndroidReceiverActivity$4$3
        //   201: dup
        //   202: aload_0
        //   203: invokespecial 128	com/OneShotPreview/AndroidReceiverActivity$4$3:<init>	(Lcom/OneShotPreview/AndroidReceiverActivity$4;)V
        //   206: invokevirtual 129	com/OneShotPreview/AndroidReceiverActivity$4$3:start	()V
        //   209: new 131	com/OneShotPreview/AndroidReceiverActivity$4$4
        //   212: dup
        //   213: aload_0
        //   214: invokespecial 132	com/OneShotPreview/AndroidReceiverActivity$4$4:<init>	(Lcom/OneShotPreview/AndroidReceiverActivity$4;)V
        //   217: invokevirtual 133	com/OneShotPreview/AndroidReceiverActivity$4$4:start	()V
        //   220: getstatic 69	java/lang/System:out	Ljava/io/PrintStream;
        //   223: ldc 135
        //   225: invokevirtual 76	java/io/PrintStream:println	(Ljava/lang/String;)V
        //   228: goto -152 -> 76
        //   231: astore_1
        //   232: getstatic 69	java/lang/System:out	Ljava/io/PrintStream;
        //   235: new 137	java/lang/StringBuilder
        //   238: dup
        //   239: ldc 139
        //   241: invokespecial 141	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
        //   244: aload_1
        //   245: invokevirtual 145	java/lang/Exception:getMessage	()Ljava/lang/String;
        //   248: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   251: invokevirtual 152	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   254: invokevirtual 76	java/io/PrintStream:println	(Ljava/lang/String;)V
        //   257: aload_0
        //   258: getfield 15	com/OneShotPreview/AndroidReceiverActivity$4:this$0	Lcom/OneShotPreview/AndroidReceiverActivity;
        //   261: new 137	java/lang/StringBuilder
        //   264: dup
        //   265: invokespecial 153	java/lang/StringBuilder:<init>	()V
        //   268: aload_1
        //   269: invokevirtual 145	java/lang/Exception:getMessage	()Ljava/lang/String;
        //   272: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   275: invokevirtual 152	java/lang/StringBuilder:toString	()Ljava/lang/String;
        //   278: invokevirtual 29	com/OneShotPreview/AndroidReceiverActivity:DisplayToast	(Ljava/lang/String;)V
        //   281: aload_1
        //   282: invokevirtual 156	java/lang/Exception:printStackTrace	()V
        //   285: return
        //   286: astore_2
        //   287: getstatic 69	java/lang/System:out	Ljava/io/PrintStream;
        //   290: ldc 158
        //   292: invokevirtual 76	java/io/PrintStream:println	(Ljava/lang/String;)V
        //   295: aload_2
        //   296: invokevirtual 156	java/lang/Exception:printStackTrace	()V
        //   299: goto -140 -> 159
        //   302: getstatic 125	com/OneShotPreview/AndroidReceiverActivity:mDeviceCount	I
        //   305: iconst_1
        //   306: if_icmpne -86 -> 220
        //   309: new 160	com/OneShotPreview/AndroidReceiverActivity$4$5
        //   312: dup
        //   313: aload_0
        //   314: invokespecial 161	com/OneShotPreview/AndroidReceiverActivity$4$5:<init>	(Lcom/OneShotPreview/AndroidReceiverActivity$4;)V
        //   317: invokevirtual 162	com/OneShotPreview/AndroidReceiverActivity$4$5:start	()V
        //   320: goto -100 -> 220
        // Local variable table:
        //   start	length	slot	name	signature
        //   0	323	0	this	4
        //   231	51	1	localException1	java.lang.Exception
        //   286	10	2	localException2	java.lang.Exception
        // Exception table:
        //   from	to	target	type
        //   0	76	231	java/lang/Exception
        //   76	100	231	java/lang/Exception
        //   159	220	231	java/lang/Exception
        //   220	228	231	java/lang/Exception
        //   287	299	231	java/lang/Exception
        //   302	320	231	java/lang/Exception
        //   100	159	286	java/lang/Exception
      }
    }.start();
  }
  
  public void onResume()
  {
    super.onResume();
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.OneShotPreview.AndroidReceiverActivity
 * JD-Core Version:    0.7.0.1
 */