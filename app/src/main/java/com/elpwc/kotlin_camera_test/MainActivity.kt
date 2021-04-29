package com.elpwc.kotlin_camera_test

import android.hardware.Camera
import android.os.Bundle
import android.os.Environment
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.reflect.Method

class MainActivity : AppCompatActivity() {

    lateinit var button1 : Button
    lateinit var sv1: SurfaceView

    lateinit var camera: Camera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1 = findViewById(R.id.button)
        sv1 = findViewById(R.id.surfaceView)


    }

    fun startPreview(holder: SurfaceHolder?) {
        if (camera != null) {
            try {
                camera.setPreviewDisplay(holder) // 先绑定显示的画面
                camera.stopPreview()
                camera.setDisplayOrientation(90)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            camera.startPreview() // 这里才是开始预览
        }
    }

    fun stopPreview() {
        if (camera != null) {
            camera.stopPreview() // 停止预览
        }
    }

    fun button1_onClick(view: View){
        //CameraUtils.setStartPreview(camera, sv1.holder)
        PermissionUtils.camera(this) {

            if (CameraUtils.isCameraAvailable()) {
                camera = CameraUtils.getCamera()!!
                CameraUtils.setParameters(camera)
            }

            startPreview(sv1.holder)

        }
    }


    val pictureCallback = Camera.PictureCallback { data, camera ->
        val pictureFile = File(Environment.getExternalStorageDirectory(), "gaozhongkui-" + System.currentTimeMillis() + ".jpg")
        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close();
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    private fun taskPicture() {
        camera?.autoFocus { success, camera ->
            run {
                camera?.takePicture(null, null, pictureCallback);
            }
        }
    }

}