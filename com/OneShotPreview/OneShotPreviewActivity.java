package com.OneShotPreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OneShotPreviewActivity
  extends Activity
{
  static ArrayList<String> list1;
  static byte[] mydata;
  private Activity _activity;
  private Camera _camera;
  Intent intent;
  String ip;
  int mPreviewImageFormat = 17;
  private Camera.PreviewCallback mPreviewListener = new Camera.PreviewCallback()
  {
    public void onPreviewFrame(byte[] paramAnonymousArrayOfByte, Camera paramAnonymousCamera)
    {
      Camera.Parameters localParameters = OneShotPreviewActivity.this._camera.getParameters();
      Camera.Size localSize = localParameters.getPreviewSize();
      YuvImage localYuvImage = new YuvImage(paramAnonymousArrayOfByte, localParameters.getPreviewFormat(), localSize.width, localSize.height, null);
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      localYuvImage.compressToJpeg(new Rect(0, 0, 200, 200), 90, localByteArrayOutputStream);
      final byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
      if (OneShotPreviewActivity.this.s1 != null) {
        new Thread()
        {
          public void run()
          {
            try
            {
              OneShotPreviewActivity.this.o1.writeObject(arrayOfByte);
              OneShotPreviewActivity.this.o1.flush();
              return;
            }
            catch (IOException localIOException)
            {
              localIOException.printStackTrace();
            }
          }
        }.start();
      }
      if (OneShotPreviewActivity.this.s2 != null) {
        new Thread()
        {
          public void run()
          {
            try
            {
              OneShotPreviewActivity.this.o2.writeObject(arrayOfByte);
              OneShotPreviewActivity.this.o2.flush();
              return;
            }
            catch (IOException localIOException)
            {
              localIOException.printStackTrace();
            }
          }
        }.start();
      }
      if (OneShotPreviewActivity.this.s3 != null) {
        new Thread()
        {
          public void run()
          {
            try
            {
              OneShotPreviewActivity.this.o3.writeObject(arrayOfByte);
              OneShotPreviewActivity.this.o3.flush();
              return;
            }
            catch (IOException localIOException)
            {
              localIOException.printStackTrace();
            }
          }
        }.start();
      }
      OneShotPreviewActivity.this.takepicture();
    }
  };
  ObjectOutputStream o1;
  ObjectOutputStream o2;
  ObjectOutputStream o3;
  Socket s1;
  Socket s2;
  Socket s3;
  
  public static JSONObject UpdateVideo(String paramString)
    throws Exception
  {
    String str1 = paramString + "%27&$top=10&$format=BYTE";
    String str2 = new String(Base64.encode(("" + ":" + "").getBytes(), 0));
    System.out.println(str2);
    URLConnection localURLConnection = new URL(str1).openConnection();
    localURLConnection.setRequestProperty("Authorization", "Basic " + str2);
    localURLConnection.setRequestProperty("DATA", new String(mydata));
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localURLConnection.getInputStream()));
    StringBuffer localStringBuffer = new StringBuffer();
    for (;;)
    {
      String str3 = localBufferedReader.readLine();
      if (str3 == null)
      {
        localBufferedReader.close();
        String str4 = localStringBuffer.toString();
        System.out.println(str4);
        return new JSONObject(str4);
      }
      localStringBuffer.append(str3);
    }
  }
  
  public Bitmap ConvertBitmap(byte[] paramArrayOfByte)
  {
    byte[] arrayOfByte = new byte[4 * paramArrayOfByte.length];
    for (int i = 0;; i++)
    {
      if (i >= paramArrayOfByte.length)
      {
        Bitmap localBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        localBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(arrayOfByte));
        return localBitmap;
      }
      int j = i * 4;
      int k = 1 + i * 4;
      int m = 2 + i * 4;
      int n = (byte)(0xFFFFFFFF ^ paramArrayOfByte[i]);
      arrayOfByte[m] = n;
      arrayOfByte[k] = n;
      arrayOfByte[j] = n;
      arrayOfByte[(3 + i * 4)] = -1;
    }
  }
  
  public void DisplayToast(final String paramString)
  {
    runOnUiThread(new Runnable()
    {
      public void run()
      {
        Toast.makeText(OneShotPreviewActivity.this.getApplicationContext(), paramString, 6000000).show();
      }
    });
  }
  
  public JSONObject getJSONFromUrl(String paramString)
    throws IOException, JSONException
  {
    InputStream localInputStream = new DefaultHttpClient().execute(new HttpGet(paramString)).getEntity().getContent();
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
    StringBuilder localStringBuilder = new StringBuilder();
    for (;;)
    {
      String str1 = localBufferedReader.readLine();
      if (str1 == null)
      {
        localInputStream.close();
        String str2 = localStringBuilder.toString();
        System.out.println("xxxxxxxxxxxxx thmbnail urlllllllllllllllll    lllllllllllllll " + str2);
        return new JSONObject(str2);
      }
      localStringBuilder.append(str1);
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    this._activity = this;
    super.onCreate(paramBundle);
    list1 = new ArrayList();
    list1 = (ArrayList)getIntent().getSerializableExtra("key");
    try
    {
      if (list1.size() == 3)
      {
        this.s1 = new Socket((String)list1.get(0), 1234);
        this.s2 = new Socket((String)list1.get(1), 1234);
        this.s3 = new Socket((String)list1.get(2), 1234);
        this.o1 = new ObjectOutputStream(this.s1.getOutputStream());
        this.o2 = new ObjectOutputStream(this.s2.getOutputStream());
        this.o3 = new ObjectOutputStream(this.s3.getOutputStream());
      }
      for (;;)
      {
        getWindow().addFlags(1024);
        requestWindowFeature(1);
        setContentView(new CameraPreview(this));
        return;
        if (list1.size() != 2) {
          break;
        }
        this.s1 = new Socket((String)list1.get(0), 1234);
        this.s2 = new Socket((String)list1.get(1), 1234);
        this.o1 = new ObjectOutputStream(this.s1.getOutputStream());
        this.o2 = new ObjectOutputStream(this.s2.getOutputStream());
      }
    }
    catch (UnknownHostException localUnknownHostException)
    {
      for (;;)
      {
        localUnknownHostException.printStackTrace();
        DisplayToast(localUnknownHostException.getMessage());
        continue;
        if (list1.size() == 1)
        {
          this.s1 = new Socket((String)list1.get(0), 1234);
          DisplayToast("connected : " + (String)list1.get(0));
          this.o1 = new ObjectOutputStream(this.s1.getOutputStream());
        }
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        localIOException.printStackTrace();
        DisplayToast(localIOException.getMessage());
      }
    }
  }
  
  public void onDestroy()
  {
    super.onDestroy();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    paramMotionEvent.getAction();
    return super.onTouchEvent(paramMotionEvent);
  }
  
  public void setCameraDisplayOrientation(Camera paramCamera, int paramInt)
  {
    Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
    Camera.getCameraInfo(paramInt, localCameraInfo);
    int i = this._activity.getWindowManager().getDefaultDisplay().getRotation();
    int j = 0;
    switch (i)
    {
    default: 
      if (localCameraInfo.facing != 1) {
        break;
      }
    }
    for (int k = (360 - (j + localCameraInfo.orientation) % 360) % 360;; k = (360 + (localCameraInfo.orientation - j)) % 360)
    {
      Log.d("camera", "result= " + k);
      paramCamera.setDisplayOrientation(k);
      return;
      j = 0;
      break;
      j = 90;
      break;
      j = 180;
      break;
      j = 270;
      break;
    }
  }
  
  public void takepicture()
  {
    this._camera.setOneShotPreviewCallback(this.mPreviewListener);
  }
  
  public class CameraPreview
    extends SurfaceView
    implements SurfaceHolder.Callback
  {
    private SurfaceHolder holder = getHolder();
    
    CameraPreview(Context paramContext)
    {
      super();
      this.holder.addCallback(this);
      this.holder.setType(3);
    }
    
    public void configure(int paramInt1, int paramInt2, int paramInt3)
    {
      OneShotPreviewActivity.this._camera.stopPreview();
      setPictureFormat(paramInt1);
      setPreviewSize(paramInt2, paramInt3);
      OneShotPreviewActivity.this._camera.startPreview();
    }
    
    protected void setPictureFormat(int paramInt)
    {
      Camera.Parameters localParameters = OneShotPreviewActivity.this._camera.getParameters();
      List localList = localParameters.getSupportedPictureFormats();
      Iterator localIterator;
      if (localList != null) {
        localIterator = localList.iterator();
      }
      do
      {
        if (!localIterator.hasNext()) {
          return;
        }
      } while (((Integer)localIterator.next()).intValue() != paramInt);
      localParameters.setPreviewFormat(paramInt);
      OneShotPreviewActivity.this._camera.setParameters(localParameters);
    }
    
    protected void setPreviewSize(int paramInt1, int paramInt2)
    {
      Camera.Parameters localParameters = OneShotPreviewActivity.this._camera.getParameters();
      List localList = localParameters.getSupportedPreviewSizes();
      Iterator localIterator;
      if (localList != null) {
        localIterator = localList.iterator();
      }
      Camera.Size localSize;
      do
      {
        if (!localIterator.hasNext()) {
          return;
        }
        localSize = (Camera.Size)localIterator.next();
      } while ((localSize.width > paramInt1) || (localSize.height > paramInt2));
      localParameters.setPreviewSize(localSize.width, localSize.height);
      OneShotPreviewActivity.this._camera.setParameters(localParameters);
    }
    
    public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
    {
      configure(paramInt1, paramInt2, paramInt3);
      try
      {
        Thread.sleep(1000L);
        OneShotPreviewActivity.this._camera.setOneShotPreviewCallback(OneShotPreviewActivity.this.mPreviewListener);
        return;
      }
      catch (InterruptedException localInterruptedException)
      {
        localInterruptedException.printStackTrace();
      }
    }
    
    public void surfaceCreated(SurfaceHolder paramSurfaceHolder)
    {
      OneShotPreviewActivity.this._camera = Camera.open();
      try
      {
        OneShotPreviewActivity.this._camera.setPreviewDisplay(paramSurfaceHolder);
        OneShotPreviewActivity.this.setCameraDisplayOrientation(OneShotPreviewActivity.this._camera, 0);
        OneShotPreviewActivity.this._camera.startPreview();
        return;
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          localIOException.printStackTrace();
        }
      }
    }
    
    public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
    {
      if (OneShotPreviewActivity.this._camera != null)
      {
        OneShotPreviewActivity.this._camera.stopPreview();
        OneShotPreviewActivity.this._camera.release();
        OneShotPreviewActivity.this._camera = null;
      }
    }
  }
  
  public class UploadVideoDatatask
    extends AsyncTask<String, Void, String>
  {
    public UploadVideoDatatask() {}
    
    protected String doInBackground(String... paramVarArgs)
    {
      try
      {
        JSONArray localJSONArray = OneShotPreviewActivity.UpdateVideo("").getJSONObject("results").getJSONArray("d");
        if (localJSONArray.length() < 0)
        {
          String str = localJSONArray.getJSONObject(0).getString("status");
          boolean bool = str.equalsIgnoreCase("success");
          if (bool) {
            return str;
          }
          return "fail";
        }
      }
      catch (Exception localException)
      {
        localException.printStackTrace();
      }
      return null;
    }
    
    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
      Toast.makeText(OneShotPreviewActivity.this.getApplicationContext(), "Data loaded " + paramString, 6000000).show();
    }
    
    protected void onPreExecute()
    {
      super.onPreExecute();
      Toast.makeText(OneShotPreviewActivity.this.getApplicationContext(), "Fetching Links. Please wait!!!", 6000000).show();
    }
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.OneShotPreview.OneShotPreviewActivity
 * JD-Core Version:    0.7.0.1
 */