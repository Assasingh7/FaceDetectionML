package com.google.facedetectionml

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.facedetectionml.databinding.ActivityMainBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            val intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager)!=null){
startActivityForResult(intent,123)
            }else{
                Toast.makeText(this, "Oops something went wrong", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==123 && resultCode== RESULT_OK){
            val extras=data?.extras
            val bitmap=extras?.get("data") as? Bitmap
            if(bitmap!=null){
                detectFace(bitmap)
            }
        }
    }
    fun detectFace(bitmap: Bitmap){
        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
        val image = InputImage.fromBitmap(bitmap, 0)
        val detector = FaceDetection.getClient(options)
// Or, to use the default option:
// val detector = FaceDetection.getClient();
        val result = detector.process(image)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                // ...
                var resultText=" "
                var i=1
                for(face in faces){
                    resultText="Face Number : $i"+
                            "\nSmile : ${face.smilingProbability?.times(100)}%"+
                            "\nLeft Eye Open: ${face.leftEyeOpenProbability?.times(100)}"+
                            "\nRight Eye Open: ${face.rightEyeOpenProbability?.times(100)}"
                    i++
                }
                if (faces.isEmpty()){
                    Toast.makeText(this, "No Face Detected", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, resultText, Toast.LENGTH_LONG).show()
                }

            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }

}