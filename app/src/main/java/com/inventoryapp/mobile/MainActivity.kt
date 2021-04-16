package com.inventoryapp.mobile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.inventoryapp.mobile.databinding.ActivityMainBinding
import com.inventoryapp.mobile.entity.Item
import com.inventoryapp.mobile.ui.main.InventoryViewModel
import com.inventoryapp.mobile.ui.main.InventoryViewModel.InventoryAction
import com.inventoryapp.mobile.ui.main.SearchInventoryFragmentDirections
import com.inventoryapp.mobile.ui.main.UploadInventoryFragmentDirections
import com.inventoryapp.mobile.ui.main.ViewInventoryFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel by viewModels<InventoryViewModel>()

    private val navController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavGraph()
        observeViewModel()
    }

    private fun setNavGraph() {
        navController.apply {
            graph = navInflater.inflate(R.navigation.nav_graph)
        }
    }

    private fun observeViewModel() {
        viewModel.inventoryActionLiveData.observe(this) { event ->
            when (event) {
                is InventoryAction.NavigateToUploadInventoryFragment -> navigateToUploadInventoryFragment()
                is InventoryAction.NavigateToViewInventoryFragment -> navigateToViewInventoryFragment()
                is InventoryAction.NavigateToSearchInventoryFragment -> navigateToSearchInventoryFragment()
            }
        }
    }

    private fun navigateToSearchInventoryFragment() {
        if (navController.currentDestination?.id == R.id.uploadInventoryFragment) {
            navController.navigate(UploadInventoryFragmentDirections.actionUploadInventoryFragmentToSearchInventoryFragment())
        }
    }

    private fun navigateToUploadInventoryFragment() {
        when (navController.currentDestination?.id) {
            R.id.viewInventoryFragment -> navController.navigate(ViewInventoryFragmentDirections.actionViewInventoryFragmentToUploadInventoryFragment())
            R.id.searchInventoryFragment -> navController.navigate(SearchInventoryFragmentDirections.actionSearchInventoryFragmentToUploadInventoryFragment())
        }
    }

    private fun navigateToViewInventoryFragment() {
        if (navController.currentDestination?.id == R.id.uploadInventoryFragment) {
            navController.navigate(UploadInventoryFragmentDirections.actionUploadInventoryFragmentToViewInventoryFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}