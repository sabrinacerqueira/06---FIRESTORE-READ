package br.edu.fatecpg.firestoneread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.edu.fatecpg.firestoneread.databinding.FragmentCadastrarBinding
import com.google.firebase.firestore.FirebaseFirestore

class CadastrarFragment : Fragment() {

    private var _binding: FragmentCadastrarBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastrarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSalvar.setOnClickListener { salvar() }
    }

    private fun salvar() {
        val nome = binding.etNome.text.toString().trim()
        val categoria = binding.etCategoria.text.toString().trim()
        val preco = binding.etPreco.text.toString().trim().replace(",", ".").toDoubleOrNull()

        binding.tilNome.error = if (nome.isEmpty()) "Informe o nome" else null
        binding.tilCategoria.error = if (categoria.isEmpty()) "Informe a categoria" else null
        binding.tilPreco.error = if (preco == null) "Informe um preço válido" else null

        if (nome.isEmpty() || categoria.isEmpty() || preco == null) return

        val produto = hashMapOf(
            "nome" to nome,
            "categoria" to categoria,
            "preco" to preco
        )

        binding.btnSalvar.isEnabled = false
        db.collection("Produtos")
            .add(produto)
            .addOnSuccessListener {
                _binding?.let {
                    it.btnSalvar.isEnabled = true
                    it.etNome.text?.clear()
                    it.etCategoria.text?.clear()
                    it.etPreco.text?.clear()
                    it.etNome.requestFocus()
                }
                Toast.makeText(requireContext(), "Produto cadastrado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                _binding?.btnSalvar?.isEnabled = true
                Toast.makeText(requireContext(), "Erro ao salvar: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
