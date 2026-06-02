package com.repon.app.ui.edit

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.repon.app.data.AppDatabase
import com.repon.app.data.ConsumableItem
import com.repon.app.data.ItemRepository
import com.repon.app.notify.WorkScheduler
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddEditViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = ItemRepository(AppDatabase.get(app).itemDao())

    var loadedId: Long = 0L
        private set
    var isExisting by mutableStateOf(false)
        private set
    var ready by mutableStateOf(false)
        private set

    var name by mutableStateOf("")
    var emoji by mutableStateOf("📦")
    var durationText by mutableStateOf("")
    var bufferDays by mutableIntStateOf(3)
    var startEpochDay by mutableLongStateOf(LocalDate.now().toEpochDay())
    var notify by mutableStateOf(true)
    var restockCount by mutableIntStateOf(0)
    private var createdEpochDay = LocalDate.now().toEpochDay()

    var nameError by mutableStateOf(false)
    var durationError by mutableStateOf(false)

    fun load(id: Long?) {
        if (ready) return
        if (id == null || id == 0L) {
            ready = true
            return
        }
        viewModelScope.launch {
            repo.get(id)?.let { item ->
                loadedId = item.id
                isExisting = true
                name = item.name
                emoji = item.emoji
                durationText = item.durationDays.toString()
                bufferDays = item.bufferDays
                startEpochDay = item.startEpochDay
                notify = item.notify
                restockCount = item.restockCount
                createdEpochDay = item.createdEpochDay
            }
            ready = true
        }
    }

    fun save(onDone: () -> Unit) {
        val duration = durationText.toIntOrNull()
        nameError = name.isBlank()
        durationError = duration == null || duration <= 0
        if (nameError || durationError) return

        val item = ConsumableItem(
            id = loadedId,
            name = name.trim(),
            emoji = emoji,
            durationDays = duration!!,
            bufferDays = bufferDays,
            startEpochDay = startEpochDay,
            notify = notify,
            restockCount = restockCount,
            lastNotifiedCycleStart = -1L,
            createdEpochDay = createdEpochDay
        )
        viewModelScope.launch {
            repo.upsert(item)
            WorkScheduler.triggerNow(getApplication())
            onDone()
        }
    }

    fun delete(onDone: () -> Unit) {
        if (loadedId == 0L) {
            onDone()
            return
        }
        viewModelScope.launch {
            repo.get(loadedId)?.let { repo.delete(it) }
            onDone()
        }
    }
}
