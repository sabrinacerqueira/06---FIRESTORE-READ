package br.edu.fatecpg.firestoneread

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.fatecpg.firestoneread.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (auth.currentUser != null) {
            abrirPrincipal()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEntrar.setOnClickListener { entrar() }
        binding.btnCadastrar.setOnClickListener { cadastrar() }
        binding.tvEsqueciSenha.setOnClickListener { recuperarSenha() }
    }

    private fun entrar() {
        val email = binding.etEmail.text.toString().trim()
        val senha = binding.etSenha.text.toString()

        if (!emailValido(email) || !senhaValida(senha)) return

        carregando(true)
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { abrirPrincipal() }
            .addOnFailureListener {
                carregando(false)
                Toast.makeText(this, "Falha ao entrar: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    private fun cadastrar() {
        val email = binding.etEmail.text.toString().trim()
        val senha = binding.etSenha.text.toString()

        if (!emailValido(email) || !senhaValida(senha)) return

        carregando(true)
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                Toast.makeText(this, "Cadastro realizado", Toast.LENGTH_SHORT).show()
                abrirPrincipal()
            }
            .addOnFailureListener {
                carregando(false)
                Toast.makeText(this, "Falha ao cadastrar: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    private fun recuperarSenha() {
        val email = binding.etEmail.text.toString().trim()

        if (!emailValido(email)) return

        carregando(true)
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                carregando(false)
                Toast.makeText(this, "E-mail de recuperação enviado", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                carregando(false)
                Toast.makeText(this, "Falha ao enviar: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    private fun emailValido(email: String): Boolean {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Informe um e-mail válido"
            return false
        }
        binding.tilEmail.error = null
        return true
    }

    private fun senhaValida(senha: String): Boolean {
        if (senha.length < 6) {
            binding.tilSenha.error = "A senha deve ter ao menos 6 caracteres"
            return false
        }
        binding.tilSenha.error = null
        return true
    }

    private fun carregando(ativo: Boolean) {
        binding.progress.visibility = if (ativo) View.VISIBLE else View.GONE
        binding.btnEntrar.isEnabled = !ativo
        binding.btnCadastrar.isEnabled = !ativo
        binding.tvEsqueciSenha.isEnabled = !ativo
    }

    private fun abrirPrincipal() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
