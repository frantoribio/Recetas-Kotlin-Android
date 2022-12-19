package es.frantoribio.recetas.ui

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import es.frantoribio.recetas.R
import es.frantoribio.recetas.RecetasApplication
import es.frantoribio.recetas.databinding.FragmentCreateRecetaBinding
import es.frantoribio.recetas.model.Receta
import kotlinx.coroutines.launch
import java.util.*


class CreateRecetaFragment : Fragment() {

    lateinit var binding: FragmentCreateRecetaBinding
    var priority: String = "1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateRecetaBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDone.setOnClickListener {
            createReceta()
        }
    }

    private fun createReceta() {
        val name = binding.editName.text.toString()
        val producto = binding.editProducto.text.toString()
        val webUrl = binding.editWebUrl.text.toString()
        val categoria = binding.editCategoria.text.toString()
        val d = Date()
        val recetaDate: CharSequence = DateFormat.format("MMMM d, yyyy ", d.time)

        Log.i("info", "create receta: $recetaDate")

        //crear Receta
        val data = Receta(
            null,
            name = name,
            producto = producto,
            categorias = categoria,
            webUrl = webUrl,
            date = recetaDate.toString(),
            iscompleated = false
        )
        //lanzamos una courutine para la tarea de añadir un registro
        lifecycleScope.launch{
            RecetasApplication.database.recetaDao().insertReceta(data)
        }

        Toast.makeText(requireContext(), "Receta añadida correctamente", Toast.LENGTH_SHORT).show()

        //volver al fragmet Homefragment
        Navigation.findNavController(requireView()).navigate(R.id.action_createRecetaFragment_to_homeFragment)
    }

}