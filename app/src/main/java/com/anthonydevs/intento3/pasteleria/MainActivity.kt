package com.anthonydevs.intento3.pasteleria
//MAIN ACTIVITY = LOGIN
import android.content.Intent // <-- Importación agregada para navegar entre actividades
import android.os.Bundle
import android.widget.TextView // <-- Importación agregada
import android.widget.Toast // <-- Importación agregada
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton // <-- Importación agregada

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Nota: Las siguientes líneas (opcionales) se usan para el "Edge-to-Edge" UI,
        // pero pueden ser eliminadas si no las estás usando.
        /*
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        */

        val loginButton = findViewById<MaterialButton>(R.id.login_button)
        val signUpLink = findViewById<TextView>(R.id.signup_link)

        // Lógica al hacer clic en el botón de Login
        loginButton.setOnClickListener {
            // Navegar al catálogo en lugar de mostrar un Toast
            val intent = Intent(this, CatalogoActivity::class.java)
            startActivity(intent)

            // Opcional: finalizar esta actividad para que no se pueda volver atrás
            // finish()
        }

        //Falta boton de volver en la barra nav
        // Lógica al hacer clic en el enlace de registro
        signUpLink.setOnClickListener {
            // Aquí va el código para ir a la pantalla de registro
            Toast.makeText(this, "Navegando a Sign Up", Toast.LENGTH_SHORT).show()
        }
    }
}
