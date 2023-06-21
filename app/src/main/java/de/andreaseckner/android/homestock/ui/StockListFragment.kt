package de.andreaseckner.android.homestock.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.andreaseckner.android.homestock.HomeStockApplication
import de.andreaseckner.android.homestock.R
import de.andreaseckner.android.homestock.StockItemViewModel
import de.andreaseckner.android.homestock.StockViewModelFactory
import de.andreaseckner.android.homestock.databinding.FragmentStockListBinding
import de.andreaseckner.android.homestock.ui.stock.StockItemListAdapter

class StockListFragment : Fragment() {

    private var _binding: FragmentStockListBinding? = null

    private val viewModel: StockItemViewModel by activityViewModels {
        StockViewModelFactory(
            (activity?.application as HomeStockApplication).database.stockItemDao()
        )
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addStockItem.setOnClickListener {
            val action = StockListFragmentDirections.actionNavigationStockListToNavigationAddItem()
            this.findNavController().navigate(action)
        }

        val adapter = StockItemListAdapter {}
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.allItems.observe(this.viewLifecycleOwner) { items ->
            items.let {
                adapter.submitList(it)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
