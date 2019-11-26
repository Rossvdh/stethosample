package us.antenado.sthetosample.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import us.antenado.sthetosample.R;
import us.antenado.sthetosample.StethoSample;


/**
 * Created by isilva on 18/07/16.
 */
public class JsonGetActivity extends AppCompatActivity {

    Button btn_get;
    TextView tv_get_response;
    String url = "http://jsonplaceholder.typicode.com/posts/1";
    StethoSample app;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_json_get);
        app = (StethoSample) getApplication();

        btn_get = (Button) findViewById(R.id.btn_get);
        tv_get_response = (TextView) findViewById(R.id.tv_get_response);


        btn_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getJson();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getJson() throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        app.httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                final String responseData = response.body().string();

                JsonGetActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JsonGetActivity.this.tv_get_response.setText(responseData);
                    }
                });

            }
        });

    }
}
