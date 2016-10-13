package com.ww.administrator.demotest;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2016/8/21.
 */
public class SocketTest extends Activity{
    WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);

        new Thread(new Runnable() {
            @Override
            public void run() {
                setSocket();
            }
        }).start();

        //connectWebSocket();

    }

    private void setSocket() {
        try {
            //10.0.3.2
            System.out.println("准备连接");
            Socket socket = new Socket("192.168.0.104", 8080);
            System.out.println("连接上了");

            /*Intent intent = new Intent();
            intent.setClass(SocketTest.this, ConnectActivity.class);
            SocketTest.this.startActivity(intent);*/
            InputStream inputStream = socket.getInputStream();
            byte buffer[] = new byte[1024*4];
            int temp = 0;
            String res = null;
            //从inputstream中读取客户端所发送的数据
            System.out.println("接收到服务器的信息是：");

            while ((temp = inputStream.read(buffer)) != -1){
                System.out.println(new String(buffer, 0, temp));
                res += new String(buffer, 0, temp);
            }

            System.out.println("已经结束接收信息……");

            socket.close();
            inputStream.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("192.168.0.104:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }


        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = (TextView)findViewById(R.id.tv_soc);
                        textView.setText(textView.getText() + "\n" + message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
}
