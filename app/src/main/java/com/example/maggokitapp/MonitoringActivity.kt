package com.example.maggokitapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.maggokitapp.databinding.ActivityMonitoringBinding
import com.example.maggokitapp.service.NotificationService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MonitoringActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonitoringBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonitoringBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        startService(Intent(this, NotificationService::class.java))


        databaseReference = Firebase.database.reference

        databaseReference.child("control").get()
            .addOnSuccessListener { result ->
                when (result.value) {
                    "1" -> {
                        binding.switchControl.isChecked = true
                    }
                    "0" -> {
                        binding.switchControl.isChecked = false
                    }
                }
            }

        binding.switchControl.setOnCheckedChangeListener { _, condition ->
            when (condition) {
                true -> hidupkanAlat()
                false -> matikanAlat()
            }
        }

        val reference = databaseReference.child("suhu")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.value

                    binding.progressBarSuhu.apply {
                        progressMax = 100.0F
                        setProgressWithAnimation(snapshot.value.toString().toFloat(), 1000L)
                    }

                    binding.tvSuhu.text = snapshot.value.toString()
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        val referenceKelembaban = databaseReference.child("kelembaban")
        referenceKelembaban.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.value

                    binding.progressBarKelembaban.apply {
                        progressMax = 100.0F
                        setProgressWithAnimation(snapshot.value.toString().toFloat(), 1000L)
                    }
                    binding.tvKelembaan.text = snapshot.value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        //ProgressBar Berat
        val referenceBerat = databaseReference.child("berat")
        referenceBerat.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    snapshot.value

                    binding.progressBarBerat.apply {
                        progressMax = 50.0F
                        setProgressWithAnimation(snapshot.value.toString().toFloat(), 1000L)
                    }
                    binding.tvBerat.text = snapshot.value.toString()
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun hidupkanAlat() {
        databaseReference.child("control").setValue("1").addOnSuccessListener { }
    }

    private fun matikanAlat() {
        databaseReference.child("control").setValue("0").addOnSuccessListener { }
    }


    override fun onDestroy() {
        startService(Intent(this, NotificationService::class.java))
        super.onDestroy()
    }

}