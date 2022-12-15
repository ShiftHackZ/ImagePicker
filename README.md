# ImagePicker Android Library

Android library that can be used as quick solution to ImagePicker feature implementation.

[![](https://jitpack.io/v/ShiftHackZ/ImagePicker.svg)](https://jitpack.io/#ShiftHackZ/ImagePicker)

## Features
- Permission handle requests
- Camera photo picker
- Gallery single photo picker
- Gallery multiple photo picker
- Custom gallery picker, supports multiple selection (for old non-AOSP Android ROMs that does not support multiple selection intent)

## Implementation

1. In project-level gradle add new maven repository:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

2. In app-level gradle add new implementation:

```groovy
dependencies {
    implementation 'com.github.ShiftHackZ:ImagePicker:v2.0'
}
```

3. Create file `provider_path.xml` in `res/xml` folder:

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path name="media" path="." />
    <external-path name="external_files" path="."/>
</paths>
```

4. In your `AndroidManifest.xml` add the file provider inside the `<application` tag:

```xml
<provider 
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.provider" 
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data 
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/provider_path" />
</provider>
```

5. In order to receive images, implement `ImagePickerCallback` in your Fragment/Activity or as object:

```kotlin
class MainActivity : AppCompatActivity(), ImagePickerCallback {

    override fun onImagePickerResult(result: PickedResult) {
        when (result) {
            PickedResult.Empty -> {
                // No file was selected, noting to do
            }
            is PickedResult.Error -> {
                val throwable = result.throwable
                // Some error happened, handle this throwable
            }
            is PickedResult.Multiple -> {
                val pickedImages = result.images
                val files = pickedImages.map { it.file }
                // Selected multiple images, do whatever you want with files
            }
            is PickedResult.Single -> {
                val pickedImage = result.image
                val file = pickedImage.file
                // Selected one image, do whatever you want with file
            }
        }
    }
}
```

6. Create an instance of ImagePicker using ImagePicker.Builder(), which require 2 mandatory params: current Activity and ImagePickerCallback:

```kotlin
val imagePicker = ImagePicker.Builder(this.packageName + ".provider", this)
    .useGallery(true)                           // Use gallery picker if true
    .useCamera(true)                            // Use camera picker if true
    .autoRotate(true)                           // Returns 0 degress rotated images, instead of exif-rotated images if true
    .multipleSelection(true)                    // Allow multiple selection in gallery picker
    .minimumSelectionCount(2)                   // Defines min count of GallerySelector.CUSTOM multiple selection gallery picker
    .maximumSelectionCount(3)                   // Defines max count of GallerySelector.CUSTOM multiple selection gallery picker
    .galleryPicker(GalleryPicker.CUSTOM)        // Available values: GalleryPicker.NATIVE, GalleryPicker.CUSTOM
    .build()
```


7. Finally, launch your ImagePicker:

```kotlin
imagePicker.launch(context)
```

## Screenshots

<img width="300" src="https://github.com/ShiftHackZ/ImagePicker/raw/master/docs/assets/screenshot_1.png" /><img width="300" src="https://github.com/ShiftHackZ/ImagePicker/raw/master/docs/assets/screenshot_2.png" /><img width="300" src="https://github.com/ShiftHackZ/ImagePicker/raw/master/docs/assets/screenshot_3.png" />

## Credits
- Developer: Dmitriy Moroz 
- E-Mail: dmitriy@moroz.cc
