package com.example.valencia21200160ef202401

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tvNombreEquipo: TextView = findViewById(R.id.tvNombreEquipo)
        val tvURL: TextView = findViewById(R.id.tvURL)
        val btSave: Button = findViewById(R.id.btSave)
        val db = FirebaseFirestore.getInstance()

        btSave.setOnClickListener {
            val NombreEquipo = tvNombreEquipo.text.toString()
            val URL = tvURL.text.toString()

            db.collection("Equipos")
            .addSnapshotListener{snapshots, e->
                if(e!=null){
                    Log.e("ERROR FIREBASE","Ocurrió un error");
                    return@addSnapshotListener
                }

                for(dc in snapshots!!.documentChanges){
                    when(dc.type){
                        DocumentChange.Type.ADDED, DocumentChange.Type.MODIFIED ->
                        {
                            tvNombreEquipo.text = dc.document.data["NombreEquipo"].toString()
                            tvURL.text = dc.document.data["ImagenEquipo"].toString()

                            Snackbar.make(
                                findViewById(android.R.id.content)
                                , "Se agregó/modificó un documento"
                                , Snackbar.LENGTH_LONG
                            ).show()
                        }

                        DocumentChange.Type.REMOVED -> {
                            Snackbar.make(
                                findViewById(android.R.id.content)
                                , "Se eliminó un documento"
                                , Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }



            }






        }
    }
}