package br.edu.fatecpg.firestoneread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.fatecpg.firestoneread.databinding.FragmentListarBinding
import com.google.firebase.firestore.FirebaseFirestore

class ListarFragment : Fragment() {

    private var _binding: FragmentListarBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvProdutos.layoutManager = LinearLayoutManager(requireContext())
        binding.swipeRefresh.setOnRefreshListener { carregar() }
        carregar()
    }

    private fun carregar() {
        binding.swipeRefresh.isRefreshing = true
        db.collection("Produtos")
            .get()
            .addOnSuccessListener { resultado ->
                val b = _binding ?: return@addOnSuccessListener
                val lista = resultado.documents.map { doc ->
                    Produto(
                        id = doc.id,
                        nome = doc.getString("nome").orEmpty(),
                        categoria = doc.getString("categoria").orEmpty(),
                        preco = doc.getDouble("preco") ?: 0.0
                    )
                }
                b.rvProdutos.adapter = ProdutoAdapter(lista)
                b.tvVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
                b.swipeRefresh.isRefreshing = false
            }
            .addOnFailureListener {
                _binding?.swipeRefresh?.isRefreshing = false
                Toast.makeText(requireContext(), "Erro ao carregar: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
