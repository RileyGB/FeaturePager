[![Maven Central](https://img.shields.io/badge/maven%20central-appintro-green.svg)](http://search.maven.org/#browse%7C2137414099)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AppIntro-green.svg?style=flat)](https://android-arsenal.com/details/1/1939)
[![Android Gems](http://www.android-gems.com/badge/PaoloRotolo/AppIntro.svg?branch=master)](http://www.android-gems.com/lib/PaoloRotolo/AppIntro)

<p>Sample App:</p>
<a href="https://play.google.com/store/apps/details?id=paolorotolo.github.com.appintroexample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-AC-global-none-all-co-pr-py-PartBadges-Oct1515-1"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" width="300" /></a>

# AppIntro
AppIntro is an Android Library that helps you make a **cool intro** for your app, like the ones in Google apps.

*Watch YouTube video [here](https://www.youtube.com/watch?v=OlAugnH3jFY&feature=youtu.be).*

<img src="https://github.com/PaoloRotolo/AppIntro/blob/master/art/intro.png" width="300">
<img src="https://github.com/PaoloRotolo/AppIntro/blob/master/art/layout2.png" width="300">


##How to use
Add this to your **build.gradle**:
```java
repositories {
    mavenCentral()
}

dependencies {
  compile 'com.github.paolorotolo:appintro:3.4.0'
}
```

Create a new **Activity that extends AppIntro**:

```java
public class MyIntro extends AppIntro {

    // Please DO NOT override onCreate. Use init.
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        addSlide(first_fragment);
        addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);
        
        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance(title, description, image, background_colour));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(false);
        setProgressButtonEnabled(false);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed() {
    // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed() {
    // Do something when users tap on Done button.
    }
    
        @Override
    public void onSlideChanged() {
    // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
    // Do something when users tap on Next button.
    }

}
```
