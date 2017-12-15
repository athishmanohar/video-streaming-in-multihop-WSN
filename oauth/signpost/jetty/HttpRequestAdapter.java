package oauth.signpost.jetty;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import oauth.signpost.http.HttpRequest;
import org.mortbay.io.Buffer;
import org.mortbay.jetty.HttpFields;
import org.mortbay.jetty.HttpFields.Field;
import org.mortbay.jetty.client.Address;
import org.mortbay.jetty.client.HttpExchange;

public class HttpRequestAdapter
  implements HttpRequest
{
  private HttpExchange request;
  private String requestUrl;
  
  public HttpRequestAdapter(HttpExchange paramHttpExchange)
  {
    this.request = paramHttpExchange;
    buildRequestUrl();
  }
  
  private void buildRequestUrl()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(this.request.getScheme() + "://");
    localStringBuilder.append(this.request.getAddress().toString().replaceAll(":\\d+", ""));
    if (this.request.getURI() != null) {
      localStringBuilder.append(this.request.getURI());
    }
    this.requestUrl = localStringBuilder.toString();
  }
  
  public Map<String, String> getAllHeaders()
  {
    Iterator localIterator = this.request.getRequestFields().getFields();
    HashMap localHashMap = new HashMap();
    while (localIterator.hasNext())
    {
      HttpFields.Field localField = (HttpFields.Field)localIterator.next();
      localHashMap.put(localField.getName(), localField.getValue());
    }
    return localHashMap;
  }
  
  public String getContentType()
  {
    return this.request.getRequestFields().getStringField("Content-Type");
  }
  
  public String getHeader(String paramString)
  {
    return this.request.getRequestFields().getStringField(paramString);
  }
  
  public InputStream getMessagePayload()
    throws IOException
  {
    return new ByteArrayInputStream(this.request.getRequestContent().array());
  }
  
  public String getMethod()
  {
    return this.request.getMethod();
  }
  
  public String getRequestUrl()
  {
    return this.requestUrl;
  }
  
  public void setHeader(String paramString1, String paramString2)
  {
    this.request.setRequestHeader(paramString1, paramString2);
  }
  
  public void setRequestUrl(String paramString)
  {
    throw new RuntimeException(new UnsupportedOperationException());
  }
  
  public Object unwrap()
  {
    return this.request;
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     oauth.signpost.jetty.HttpRequestAdapter
 * JD-Core Version:    0.7.0.1
 */