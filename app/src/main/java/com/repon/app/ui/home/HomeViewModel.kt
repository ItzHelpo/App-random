package com.repon.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repon.app.data.AppDatabase
import com.repon.app.data.ConsumableItem
import com.repon.app.data.ItemRepository
import com.repon.app.notify.WorkScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ItemRepository(AppDatabase.get(app).itemDao())

    val items: StateFlow<List<ConsumableItem>> = repo.items.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun restock(item: ConsumableItem) = viewModelScope.launch {
        repo.markRestocked(item, LocalDate.now().toEpochDay())
        WorkScheduler.triggerNow(getApplication())
    }

    fun delete(item: ConsumableItem) = viewModelScope.launch {
        repo.delete(item)
    }
}
