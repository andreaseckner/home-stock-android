package de.andreaseckner.android.homestock.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import de.andreaseckner.android.homestock.HomeStockApplication
import de.andreaseckner.android.homestock.StockItemViewModel
import de.andreaseckner.android.homestock.StockViewModelFactory
import de.andreaseckner.android.homestock.data.StockItem
import de.andreaseckner.android.homestock.data.StockItemRoomDatabase
import de.andreaseckner.android.homestock.databinding.FragmentAddItemBinding
import de.andreaseckner.android.homestock.util.ReminderWorker
import java.util.*
import java.util.concurrent.TimeUnit

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null

    private val stockItemViewModel: StockItemViewModel by activityViewModels {
        StockViewModelFactory(
            (activity?.application as HomeStockApplication).database.stockItemDao()
        )
    }

    lateinit var item: StockItem

    var chosenYear = 0
    var chosenMonth = 0
    var chosenDay = 0
    var chosenHour = 0
    var chosenMin = 0


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val today = Calendar.getInstance()

        binding.datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { _, year, month, day ->
            chosenYear = year
            chosenMonth = month
            chosenDay = day
        }

        binding.timePicker.setOnTimeChangedListener { _, hour, minute ->
            chosenHour = hour
            chosenMin  = minute
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.saveAction.setOnClickListener {
            addNewItem()
        }
    }

    private fun isEntryValid(): Boolean {
        return stockItemViewModel.isEntryValid(
            binding.itemName.text.toString(),
            binding.itemPrice.text.toString(),
            binding.itemCount.text.toString()
        )
    }

    private fun clearForm() {
        binding.itemName.text?.clear()
        binding.itemPrice.text?.clear()
        binding.itemCount.text?.clear()
    }

    private fun addNewItem() {
        if (isEntryValid()) {
            stockItemViewModel.addNewItem(
                binding.itemName.text.toString(),
                binding.itemPrice.text.toString(),
                binding.itemCount.text.toString(),
            )
        }

        val userSelectedDateTime =Calendar.getInstance()
        userSelectedDateTime.set(chosenYear, chosenMonth, chosenDay, chosenHour , chosenMin)

        val todayDateTime = Calendar.getInstance()

        val delayInSeconds = (userSelectedDateTime.timeInMillis/1000L) - (todayDateTime.timeInMillis/1000L)

        createWorkRequest(binding.itemName.text.toString(), delayInSeconds)

        StockItemRoomDatabase.getDatabase(requireContext())


        clearForm()
    }

    private fun createWorkRequest(message: String,timeDelayInSeconds: Long  ) {
        val myWorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
            .setInputData(workDataOf(
                "title" to "Reminder",
                "message" to message,
            )
            )
            .build()

        WorkManager.getInstance(requireContext()).enqueue(myWorkRequest)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
