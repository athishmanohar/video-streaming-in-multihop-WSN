package oauth.signpost.jetty;

import oauth.signpost.AbstractOAuthConsumer;
import oauth.signpost.http.HttpRequest;
import org.mortbay.jetty.client.HttpExchange;

public class JettyOAuthConsumer
  extends AbstractOAuthConsumer
{
  private static final long serialVersionUID = 1L;
  
  public JettyOAuthConsumer(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
  }
  
  protected HttpRequest wrap(Object paramObject)
  {
    return new HttpRequestAdapter((HttpExchange)paramObject);
  }
}


/* Location:           C:\Users\user\Desktop\classes_dex2jar.jar
 * Qualified Name:     oauth.signpost.jetty.JettyOAuthConsumer
 * JD-Core Version:    0.7.0.1
 */