package es.frantoribio.recetas.ui

import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialog
import es.frantoribio.recetas.R
import es.frantoribio.recetas.RecetasApplication
import es.frantoribio.recetas.databinding.FragmentEditRecetaBinding
import es.frantoribio.recetas.model.Receta
import kotlinx.coroutines.launch
import java.util.*


class EditRecetaFragment : Fragment(), MenuProvider {

    val oldNotes by navArgs<EditRecetaFragmentArgs>() //para recoger los datos enviados por naveg

    lateinit var binding: FragmentEditRecetaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditRecetaBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //opciones del menu en este fragment
        activity?.addMenuProvider(this)

        //mostrar los datos en el fragment
        binding.editName.setText(oldNotes.data.name)
        binding.editProducto.setText(oldNotes.data.producto)
        binding.editCategoria.setText(oldNotes.data.categorias)
        binding.editWebUrl.setText(oldNotes.data.webUrl)

        //crear la funcion para el update
        binding.btnDone.setOnClickListener {
            updateShopping(it)
        }
    }

    private fun updateShopping(it: View?) {

        //almacenamos en una variable los valores nuevos
        val name = binding.editName.text.toString()
        val producto = binding.editProducto.text.toString()
        val categoria = binding.editCategoria.text.toString()
        val webUrl = binding.editWebUrl.text.toString()

        val d = Date()
        val shoppingDate: CharSequence = DateFormat.format("MMMM d, yyyy ", d.time)

        //crear el objeto Receta
        val data = Receta (
            oldNotes.data.id, //recogemos el id ya que no va a cambiar
            name = name,
            producto = producto,
            categorias = categoria, //pasamos el dato a Long
            webUrl = webUrl,
            date = shoppingDate.toString(),
            iscompleated = oldNotes.data.iscompleated ,// recogemos este dato ya que no se ha modificado
        )

        //lanzamos una corrutina para la tarea de modificar un registro
        lifecycleScope.launch {
            RecetasApplication.database.recetaDao().updateReceta(data)
        }

        Toast.makeText(requireContext(),"Receta modificada", Toast.LENGTH_SHORT).show()

        //volver hacia atras en la nevagacion
        Navigation.findNavController(requireView()).navigate(R.id.action_editRecetaFragment_to_homeFragment)
    }


    //empezamos el tratamiento del borrado activando el menu

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        if(menuItem.itemId == R.id.menu_delete){

            //vamos a mostrar el dialog por el final de la pantalla
            //para ello creamos un estilo de ventana
            val bottonSheet = BottomSheetDialog(requireContext(),R.style.BottonSheetStyle)
            bottonSheet.setContentView(R.layout.dialog_delete)

            val textYes = bottonSheet.findViewById<TextView>(R.id.dialogYes)
            val textNo  = bottonSheet.findViewById<TextView>(R.id.dialogNo)

            textYes?.setOnClickListener {
                //lanzamos una corrutina para la tarea de borrar un registro
                lifecycleScope.launch {
                    RecetasApplication.database.recetaDao().deleteReceta(oldNotes.data.id!!)
                }

                Toast.makeText(requireContext(),"Receta borrada", Toast.LENGTH_SHORT).show()
                bottonSheet.dismiss()  //cerramos el dialog
                Navigation.findNavController(requireView()).navigate(R.id.action_editRecetaFragment_to_homeFragment)
            }

            textNo?.setOnClickListener {
                bottonSheet.dismiss()
            }

            bottonSheet.show()
        }

        return true
    }

    //eliminar el menu al salir del fragment
    override fun onDestroyView() {
        activity?.removeMenuProvider(this)
        super.onDestroyView()
    }
}