### Android Arc-SeekBar

#### How to get it

Step 1. Add it in your root build.gradle at the end of repositories:
```
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```

Step 2. Add the dependency in moudle build.gradle
```
dependencies {
        compile 'com.github.rorschach:ShaderSeekArc:v1.11'
    }
```

#### How to use it 

in `.xml` file
```
<me.rorschach.library.ShaderSeekArc
    android:id="@+id/seek_arc"
    android:layout_width="300dp"
    android:layout_height="300dp"
    android:layout_gravity="center" 

    //default values
    app:startValue="16"
    app:endValue="28"
    app:progress="24"
    app:startColor="0xFFFFF314"
    app:endColor="0xFFCC151C"
    app:startAngle="-225"
    app:endAngle="45"
    app:arcWidthRate="0.5"
    app:showMark="true" 
    app:showProgress="true"
    app:markSize="30"
    app:markColor="0xff33B5E5"
    app:progressTextSize="35"
    app:progressTextColor="0xffFFBB33"
    app:lineColor="0xff33B5E5" />
```


in `.java` file
```
    ShaderSeekArc seekArc = (ShaderSeekArc) findViewById(R.id.seek_arc);

// colors of shader
    int[] colors = new int[]{0xFF2C3EFF, 0xFF53FF65, 0xFF000000};
    seekArc.setColors(colors);

// notice the length of colors and positions must be equals, 
// position could be null
    float[] positions = new float[]{0, 1f / 2, 1};
    seekArc.setPositions(positions);

// also can just set startColor and endColor
   seekArc.setStartColor(0xFFFF2D0C);
   seekArc.setEndColor(0xFF1636FF);

// notice the endValue must large than startValue, 
// and progress should between them
   seekArc.setStartValue(20);
   seekArc.setEndValue(30);
   seekArc.setProgress(25);

// notice the endAngle must large than startAngle in math,
// and (endAngle - startAngle) should less　than or equals 360
   seekArc.setStartAngle(-180);
   seekArc.setEndAngle(180);

// call back
   seekArc.setOnSeekArcChangeListener(new ShaderSeekArc.OnSeekArcChangeListener() {

       @Override
       public void onProgressChanged(ShaderSeekArc seekArc, float progress) {
            Log.d(TAG, "progress " + progress);
       }

       @Override
       public void onStartTrackingTouch(ShaderSeekArc seekArc) {
            Log.d(TAG, "onStartTrackingTouch");
       }

       @Override
       public void onStopTrackingTouch(ShaderSeekArc seekArc) {
            Log.d(TAG, "onStopTrackingTouch");
       }
   });
```

#### ScreenShots

default state

![default](https://raw.githubusercontent.com/rorschach/ShaderSeekArc/master/screenshots/default.png)

change by xml

![xml](https://raw.githubusercontent.com/rorschach/ShaderSeekArc/master/screenshots/custom_xml.png)

change by code

![code](https://raw.githubusercontent.com/rorschach/ShaderSeekArc/master/screenshots/custom_code.png)

change by xml and code

![together](https://raw.githubusercontent.com/rorschach/ShaderSeekArc/master/screenshots/custom_xml_plus_code.png)

#### LICENSE

   Copyright (c) 2016, Rorschach <rorschach.loliopop@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.