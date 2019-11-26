package us.antenado.sthetosample.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import us.antenado.sthetosample.R;
import us.antenado.sthetosample.StethoSample;

/**
 * Created by isilva on 18/07/16.
 */
public class ImageGetActivity extends AppCompatActivity {

    StethoSample app;
    Button bt_image;
    ImageView iv_image;
    String url = "https://via.placeholder.com/600/61a65";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_get);

        app = (StethoSample) getApplication();

        bt_image = (Button) findViewById(R.id.bt_image);
        iv_image = (ImageView) findViewById(R.id.iv_image);

        bt_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
    }

    private void getImage() {
        Picasso.with(this).load(url).into(iv_image);
    }
}
