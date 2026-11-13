package br.edu.fatecpg.firestoneread

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.fatecpg.firestoneread.databinding.ItemProdutoBinding
import java.text.NumberFormat
import java.util.Locale

class ProdutoAdapter(
    private val produtos: List<Produto>
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    private val formatoMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    inner class ProdutoViewHolder(val binding: ItemProdutoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val binding = ItemProdutoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProdutoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtos[position]
        holder.binding.tvNome.text = produto.nome
        holder.binding.tvCategoria.text = produto.categoria
        holder.binding.tvPreco.text = formatoMoeda.format(produto.preco)
    }

    override fun getItemCount(): Int = produtos.size
}
