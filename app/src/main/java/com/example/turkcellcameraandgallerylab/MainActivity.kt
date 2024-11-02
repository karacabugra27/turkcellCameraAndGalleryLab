package com.example.turkcellcameraandgallerylab

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {
    var gecerliDosyaYolu: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        izinKontrol()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        kameraAc()
    }

    fun kameraAc() {

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            val fotoCekIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (fotoCekIntent.resolveActivity(packageManager) != null) {
                val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                // android cihazın resim dosyaları için ayrılan klasöre erişim
                val resimDosyasi: File = File.createTempFile("ResimDosyaAdi", ".jpg", storageDir)
                // geçici bir resim dosyası oluşturur
                gecerliDosyaYolu = resimDosyasi.absolutePath
                // gecerliDosyaYoluna resim dosyasinin yolunu atar
                if (resimDosyasi != null) {
                    val photoUri: Uri = FileProvider.getUriForFile(this, packageName, resimDosyasi)
                    fotoCekIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(fotoCekIntent, 1)
                }
            }
        }
    }



override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (resultCode == Activity.RESULT_OK) {
        Toast.makeText(this, "Acildi", Toast.LENGTH_SHORT).show()
    }
}

fun izinKontrol() {
    if ((ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    ) {
        kameraAc()
    } else {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 1
        )
    }
}
}

