package com.mrntlu.PassVault;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        AboutView view = AboutBuilder.with(this)
                .setPhoto(R.mipmap.profile_picture)
                .setCover(R.mipmap.profile_cover)
                .setName("MrNtlu")
                .setSubTitle("Mobile Developer")
                .setAppIcon(R.mipmap.ic_launcher)
                .setAppName(R.string.app_name)
                .addGooglePlayStoreLink("8269784969410642250")
                .addGitHubLink("MrNtlu")
                .addEmailLink("mrntlu@gmail.com")
                .addLinkedInLink("burak-fidan")
                .addFiveStarsAction("com.mrntlu.PassVault")
                .setVersionNameAsAppSubTitle()
                .addMoreFromMeAction("MrNtlu")
                .addFeedbackAction("mrntlu@gmail.com")
                .addShareAction(R.string.app_name)
                .setWrapScrollView(false)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build();

        AboutView aboutViewLayout= findViewById(R.id.about_us);
        aboutViewLayout.addView(view);
    }
}
