package practice.kuouweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by a312689543 on 2016/4/13.
 */
public class HttpUtil {
    public static void sendRequestHttp(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL(address);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    connection.setConnectTimeout(8000);
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    if((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    if (listener!=null){
                        // 回调onFinish()方法
                        listener.onFinish(response.toString());
                    }

                } catch (Exception e) {
                   if(listener!=null){
                       // 回调onError()方法
                       listener.onError(e);
                   }
                }finally {
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();;
    }
}