package es.frantoribio.recetas.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import es.frantoribio.recetas.R
import es.frantoribio.recetas.databinding.ViewRecetaBinding
import es.frantoribio.recetas.model.Receta


class RecetasAdapter(private var recetasList: MutableList<Receta>,
                     private var listener: RecetasOnClickListener)
    : RecyclerView.Adapter<RecetasAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_receta, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = recetasList.get(position)
        holder.bind(item)

        holder.setListener(item)
    }

    override fun getItemCount(): Int {
        return recetasList.size
    }

    fun setRecetas(recetas: MutableList<Receta>) {
        this.recetasList = recetas
        notifyDataSetChanged()
    }

    fun update(receta: Receta) {
        val index = recetasList.indexOf(receta)
        //preguntamos por el index
        if(index != -1){
            recetasList.set(index, receta)
            notifyItemChanged(index)
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){

        val binding = ViewRecetaBinding.bind(view)

        fun bind(receta: Receta){
            binding.textName.text = receta.name
            binding.textProducto.text = receta.producto
            binding.textCategorias.text = receta.categorias
            binding.textWeb.text = receta.webUrl
            binding.textDate.text = receta.date
            binding.viewCompleted.isChecked = receta.iscompleated
        }

        fun setListener(item: Receta) {
            binding.viewCompleted.setOnClickListener { listener.onCompleatedReceta(item) }
            binding.root.setOnClickListener { listener.onClickReceta(item) }

        }
    }
}