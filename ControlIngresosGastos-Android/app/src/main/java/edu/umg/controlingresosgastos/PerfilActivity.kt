package edu.umg.controlingresosgastos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {

    private lateinit var imgFotoPerfil: ImageView
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText

    private val seleccionarImagenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                try {
                    contentResolver.takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    val sharedPreferences = getSharedPreferences("PerfilPrefs", MODE_PRIVATE)
                    sharedPreferences.edit().putString("ruta_foto", imageUri.toString()).apply()
                    imgFotoPerfil.setImageURI(imageUri)
                } catch (e: Exception) {
                    // Manejo de errores
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        imgFotoPerfil = findViewById(R.id.imgFotoPerfil)
        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etCorreo = findViewById(R.id.etCorreo)

        val btnCambiarFoto = findViewById<Button>(R.id.btnCambiarFoto)
        val btnGuardarPerfil = findViewById<Button>(R.id.btnGuardarPerfil)

        btnCambiarFoto?.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            seleccionarImagenLauncher.launch(intent)
        }

        // Cargar datos guardados previamente al abrir la actividad
        cargarDatosPerfil()

        // Botón Guardar Perfil
        btnGuardarPerfil?.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val apellido = etApellido.text.toString().trim()
            val correo = etCorreo.text.toString().trim()

            val sharedPreferences = getSharedPreferences("PerfilPrefs", MODE_PRIVATE)
            sharedPreferences.edit().apply {
                putString("nombre", nombre)
                putString("apellido", apellido)
                putString("correo", correo)
                apply()
            }

            Toast.makeText(this, "¡Perfil guardado con éxito!", Toast.LENGTH_SHORT).show()
            finish() // Regresa a la pantalla principal
        }
    }

    private fun cargarDatosPerfil() {
        val sharedPreferences = getSharedPreferences("PerfilPrefs", MODE_PRIVATE)

        // Cargar foto
        val rutaImagen = sharedPreferences.getString("ruta_foto", null)
        if (!rutaImagen.isNullOrEmpty()) {
            try {
                val uri = Uri.parse(rutaImagen)
                try {
                    contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } catch (e: Exception) {
                    // Permisos ya concedidos
                }
                imgFotoPerfil.setImageURI(uri)
            } catch (e: Exception) {
                // Error de URI
            }
        }

        // Cargar textos guardados
        etNombre.setText(sharedPreferences.getString("nombre", ""))
        etApellido.setText(sharedPreferences.getString("apellido", ""))
        etCorreo.setText(sharedPreferences.getString("correo", ""))
    }
}