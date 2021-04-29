package com.elpwc.kotlin_camera_test

import android.graphics.ImageFormat
import android.hardware.Camera
import android.view.Surface
import android.view.SurfaceHolder
import java.io.IOException
import java.util.*
import kotlin.Comparator

object CameraUtils {
    fun isCameraAvailable(): Boolean {
        val numberOfCameras = Camera.getNumberOfCameras()
        if (numberOfCameras != 0) {
            return true
        }
        return false
    }

    fun getCamera(): Camera? {
        var camera: Camera? = null
        try {
            camera = Camera.open(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return camera
    }

    fun setParameters(camera: Camera?) {
        camera?.let {
            val params: Camera.Parameters = camera.parameters
            params.pictureFormat = ImageFormat.JPEG
            val size = Collections.max(params.supportedPictureSizes, object : Comparator<Camera.Size> {
                override fun compare(lhs: Camera.Size, rhs: Camera.Size): Int {
                    return lhs.width * lhs.height - rhs.width * rhs.height
                }
            })
            params.setPreviewSize(size.width, size.height);
            params.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
            try {
                camera.parameters = params;    //在设置属性时，如果遇到未支持的大小时将会直接报错，故需要捕捉一样，做异常处理
            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    //遇到上面所说的情况，只能设置一个最小的预览尺寸
                    params.setPreviewSize(1920, 1080);
                    camera.parameters = params;
                } catch (e1: Exception) {
                    //到这里还有问题，就是拍照尺寸的锅了，同样只能设置一个最小的拍照尺寸
                    e1.printStackTrace();
                    try {
                        params.setPictureSize(1920, 1080);
                        camera.parameters = params;
                    } catch (ignored: Exception) {}}}
        }
    }

    fun setStartPreview(camera: Camera?, holder: SurfaceHolder?) {
        camera?.let {
            try {
                camera.setPreviewDisplay(holder)
                camera.setDisplayOrientation(Surface.ROTATION_90)
                camera.startPreview()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

}