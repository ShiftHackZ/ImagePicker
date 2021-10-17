package com.shz.imagepicker.imagepicker;

import java.io.File;
import java.util.List;

public interface ImagePickerCallback {
    void onImagesSelected(List<File> files);
}
