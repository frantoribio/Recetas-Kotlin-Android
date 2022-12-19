package es.frantoribio.recetas.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import es.frantoribio.recetas.R
import es.frantoribio.recetas.RecetasApplication
import es.frantoribio.recetas.adapter.RecetasAdapter
import es.frantoribio.recetas.adapter.RecetasOnClickListener
import es.frantoribio.recetas.databinding.FragmentHomeBinding
import es.frantoribio.recetas.model.Receta
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), RecetasOnClickListener {

    lateinit var  binding : FragmentHomeBinding
    private lateinit var mGridLayoutManager: GridLayoutManager
    private lateinit var mAdapter: RecetasAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //se indica a donde se va a navegar
        binding.btnAddNotes.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_createRecetaFragment)
        }

        getAllRecetas()
        setupRecyclerView()
        setFilter()


    }

    private fun setFilter() {
        //primera opcion sin filtro
        binding.allNotes.setOnClickListener {
            getAllRecetas()
            setupRecyclerView()
        }
        binding.filterMeGusta.setOnClickListener {
            getCompleatedShopping()
            setupRecyclerView()
        }
        binding.filterNoMeGusta.setOnClickListener {
            getPendingShopping()
            setupRecyclerView()
        }
    }

    private fun getPendingShopping() {
        lifecycleScope.launch{
            val recetasList = RecetasApplication.database.recetaDao().getPendingReceta()
            mAdapter.setRecetas(recetasList)
        }
    }

    private fun getCompleatedShopping() {
        lifecycleScope.launch {
            val recetaComple = RecetasApplication.database.recetaDao().getCompleatedReceta()
            mAdapter.setRecetas(recetaComple)
        }
    }



    private fun setupRecyclerView() {
        mAdapter = RecetasAdapter(mutableListOf(),this)
        mGridLayoutManager = GridLayoutManager(requireContext(),2)

        binding.recycler.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayoutManager
            adapter = mAdapter
        }
    }

    private fun getAllRecetas() {
        lifecycleScope.launch {
            val recetas = RecetasApplication.database.recetaDao().getAllRecetas()
            mAdapter.setRecetas(recetas)
        }
    }


    //estos son los miembros de la interface onClick

    override fun onCompleatedReceta(receta: Receta) {
        //modificar el valor de iscompleated
        receta.iscompleated = !receta.iscompleated
        //actualizar las datos
        lifecycleScope.launch {
            val recetasList = RecetasApplication.database.recetaDao().updateReceta(receta)
            mAdapter.update(receta)
        }
    }

    override fun onClickReceta(receta: Receta) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditRecetaFragment(receta)
        Navigation.findNavController(requireView()).navigate(action)
    }
}