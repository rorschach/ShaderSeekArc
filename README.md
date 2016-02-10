### Android Arc-SeekBar

#### How to get it

Step 1.Add it in your root build.gradle at the end of repositories:
```
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```

Step 2. Add the dependency
```
dependencies {
        compile 'com.github.rorschach:ShaderSeekArc:v0.8'
    }
```

#### How to use it 

in `.xml` file
```
    <me.rorschach.library.ShaderSeekArc
        android:id="@+id/seek_arc"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        app:startValue="16"
        app:endValue="28"
        app:progress="24"
        app:startAngle="-225"
        app:endAngle="45"
        app:showMark="true"
        app:showProgress="true" />
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
