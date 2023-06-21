package de.andreaseckner.android.homestock

import androidx.lifecycle.*
import de.andreaseckner.android.homestock.data.StockItem
import de.andreaseckner.android.homestock.data.StockItemDao
import kotlinx.coroutines.launch

class StockItemViewModel(private val itemDao: StockItemDao) : ViewModel() {

    val allItems: LiveData<List<StockItem>> = itemDao.getItems().asLiveData()

    private fun insertItem(item: StockItem) {
        viewModelScope.launch {
            itemDao.insert(item)
        }
    }

    fun deleteItem(item: StockItem) {
        viewModelScope.launch {
            itemDao.delete(item)
        }
    }

    fun retrieveItem(id: Int): LiveData<StockItem> {
        return itemDao.getItem(id).asLiveData()
    }

    private fun updateItem(item: StockItem) {
        viewModelScope.launch {
            itemDao.update(item)
        }
    }

    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): StockItem {
        return StockItem(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    fun updateItem(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val updatedItem = getUpdatedItemEntry(itemId, itemName, itemPrice, itemCount)
        updateItem(updatedItem)
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
            return false
        }
        return true
    }

    private fun getUpdatedItemEntry(
        itemId: Int,
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): StockItem {
        return StockItem(
            id = itemId,
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }
}

class StockViewModelFactory(private val itemDao: StockItemDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StockItemViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
