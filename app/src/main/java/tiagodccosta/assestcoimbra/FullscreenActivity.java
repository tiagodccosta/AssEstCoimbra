package tiagodccosta.assestcoimbra;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class FullscreenActivity extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen);


        imageView = (ImageView) findViewById(R.id.fullscreenImage);

        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null) {
            Uri imageUri = callingActivityIntent.getData();
            if(imageUri != null && imageView != null) {
                Glide.with(this)
                        .load(imageUri)
                        .fitCenter()
                        .centerCrop()
                        .into(imageView);
            }
        }

    }

}
