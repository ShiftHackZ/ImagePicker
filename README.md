# ImagePicker Android Library

Android library that can be used as quick solution to ImagePicker feature implementation.

[![](https://jitpack.io/v/ShiftHackZ/ImagePicker.svg)](https://jitpack.io/#ShiftHackZ/ImagePicker)

## Features
- Permission handle requests
- Camera photo picker
- Gallery single photo picker
- Gallery multiple photo picker

## Implementation

1. In project-level gradle add new maven repository:

<pre>
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
</pre>

2. In app-level gradle add new implementation:

<pre>
dependencies {
    implementation 'com.github.ShiftHackZ:ImagePicker:v1.0'
}
</pre>

3. In order to receive images, implement ImagePickerCallback in your Fragment/Activity or as object:

<pre>
public class MainActivity extends AppCompatActivity implements ImagePickerCallback {
    ...
    @Override
    public void onImagesSelected(List<File> files) {
        // Do whatever you want with list of files
        for (int i = 0; i < files.size(); i++) {
            // As example you can process each file inside for-cycle
        }        
    }    
    ...
}
</pre>

4. Create an instance of ImagePicker using ImagePicker.Builder(), which require 2 mandatory params: current Activity and ImagePickerCallback:

<pre>
ImagePicker imagePicker = new ImagePicker.Builder(activity, callback)
    .useGallery(true)
    .useCamera(true)
    .useMultiSelection(true)
    .build();
</pre>

List of Builder methods:
- useGallery(boolean)           // Pass 'true' if you want to enable gallery picker
- useMultiSelection(boolean)    // Pass 'true' if you need gallery picker to support multiple photo selection
- useCamera(boolean)            // Pass 'true' if you want to enable camera picker

5. Finally, launch your ImagePicker:

<pre>
imagePicker.start();
</pre>

## Credits
- Developer: Dmitriy Moroz 
- E-Mail: dmitriy@moroz.cc
