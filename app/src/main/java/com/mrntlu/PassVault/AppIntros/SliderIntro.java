package com.mrntlu.PassVault.AppIntros;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.mrntlu.PassVault.MainActivity;
import com.mrntlu.PassVault.R;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class SliderIntro extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SliderPage sliderPage=new SliderPage();
        sliderPage.setTitle("Welcome to PassVault");
        sliderPage.setDescription("Online & Offline Password Center");
        sliderPage.setImageDrawable(R.drawable.passvault_logo);
        sliderPage.setBgColor(Color.parseColor("#1B3147"));

        SliderPage sliderPage2=new SliderPage();
        sliderPage2.setTitle("Offline Storage");
        sliderPage2.setDescription("You can save your passwords OFFLINE and only you can access them.");
        sliderPage2.setImageDrawable(R.drawable.ic_protection);
        sliderPage2.setBgColor(Color.parseColor("#2C3E50"));

        SliderPage sliderPage3=new SliderPage();
        sliderPage3.setTitle("Online Storage");
        sliderPage3.setDescription("You can save your passwords ONLINE and reach from any other Android device without security problems!");
        sliderPage3.setImageDrawable(R.drawable.ic_cloud);
        sliderPage3.setBgColor(Color.parseColor("#42586E"));

        SliderPage sliderPage4=new SliderPage();
        sliderPage4.setTitle("Rate Us & Comment");
        sliderPage4.setDescription("Please don't forget to rate and comment :)");
        sliderPage4.setImageDrawable(R.drawable.ic_review);
        sliderPage4.setBgColor(Color.parseColor("#617487"));

        addSlide(AppIntroFragment.newInstance(sliderPage));
        addSlide(AppIntroFragment.newInstance(sliderPage2));
        addSlide(AppIntroFragment.newInstance(sliderPage3));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        setSeparatorColor(Color.parseColor("#FFFFFF"));
        showStatusBar(false);
        showSkipButton(false);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
