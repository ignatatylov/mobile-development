import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab4.Building
import com.example.lab4.ClickerState
import com.example.lab4.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ClickerViewModel : ViewModel() {
    private val _state = MutableStateFlow(ClickerState())
    val state: StateFlow<ClickerState> get() = _state

    private val _toastChannel = MutableSharedFlow<String>()
    val toastFlow: SharedFlow<String> = _toastChannel

    var lastToastThreshold = 0L

    val toastMessages = listOf(
        "У вас %d печенья, пора прикупить что-нибудь!",
        "Вы накопили %d печенья! Отличный результат!",
        "Целых %d печенья на вашем счету! Может пора инвестировать?",
        "Поздравляем, %d печенья в вашей копилке! Не останавливайтесь!"
    )


    private val buildingJobs = mutableMapOf<Int, Job>()

    init {
        startTimer()
        loadBuildings()
    }

    fun onCookieClicked() {
        val newCookieCount = _state.value.cookieCount + 1
        _state.value = _state.value.copy(cookieCount = newCookieCount)

        if (newCookieCount > 2000 && newCookieCount > lastToastThreshold) {
            val randomMessage = toastMessages.random().format(newCookieCount)
            sendToast(randomMessage)

            lastToastThreshold = newCookieCount + 2000
        }

        updateBuildingBuyability(newCookieCount)
    }





    private fun sendToast(message: String) {
        viewModelScope.launch {
            _toastChannel.emit(message)
        }
    }

    private fun loadBuildings() {
        val buildings = listOf(
            Building(id = 1, name = "Курсор", cost = 15, imageResId = R.drawable.cursor, canBuy = false, count = 0, cookiesPerSecond = 0.1),
            Building(id = 2, name = "Бабуля", cost = 1, imageResId = R.drawable.grandma, canBuy = false, count = 0, cookiesPerSecond = 1.0),
            Building(id = 3, name = "Ферма", cost = 1100, imageResId = R.drawable.farm, canBuy = false, count = 0, cookiesPerSecond = 8.0),
            Building(id = 4, name = "Шахта", cost = 12000, imageResId = R.drawable.mine, canBuy = false, count = 0, cookiesPerSecond = 47.0),
            Building(id = 5, name = "Фабрика", cost = 130000, imageResId = R.drawable.factory, canBuy = false, count = 0, cookiesPerSecond = 260.0),
            Building(id = 6, name = "Банк", cost = 1400000, imageResId = R.drawable.bank, canBuy = false, count = 0, cookiesPerSecond = 1400.0),
            Building(id = 7, name = "Храм", cost = 20000000, imageResId = R.drawable.temple, canBuy = false, count = 0, cookiesPerSecond = 7800.0),
            Building(id = 8, name = "Башня волшебника", cost = 330000000, imageResId = R.drawable.wizard_tower, canBuy = false, count = 0, cookiesPerSecond = 44000.0),
            Building(id = 9, name = "Космический корабль", cost = 5100000000, imageResId = R.drawable.spacecraft, canBuy = false, count = 0, cookiesPerSecond = 260000.0),
        )
        _state.value = _state.value.copy(buildingList = buildings)
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(1_000L)
                _state.value = _state.value.copy(totalTime = _state.value.totalTime + 1)

                updateCookiesPerSecond()
                updateCookiesPerMin()
            }
        }
    }

    private fun updateCookiesPerSecond() {
        val totalCookiesPerSecond = _state.value.buildingList.sumOf { building ->
            building.count * building.cookiesPerSecond
        }
        _state.value = _state.value.copy(cookiesPerSecond = totalCookiesPerSecond)
    }

    private fun updateCookiesPerMin() {
        val cookiesPerMin = _state.value.cookiesPerSecond * 60
        _state.value = _state.value.copy(cookiesPerMin = cookiesPerMin)
    }


    fun buyBuilding(building: Building) {
        if (_state.value.cookieCount >= building.cost) {
            val updatedCookieCount = _state.value.cookieCount - building.cost
            _state.value = _state.value.copy(cookieCount = updatedCookieCount)

            _state.value = _state.value.copy(
                buildingList = _state.value.buildingList.map { existingBuilding ->
                    if (existingBuilding.id == building.id) {
                        existingBuilding.copy(
                            count = existingBuilding.count + 1,
                            canBuy = existingBuilding.cost <= updatedCookieCount
                        )
                    } else {
                        existingBuilding.copy(
                            canBuy = existingBuilding.cost <= updatedCookieCount
                        )
                    }
                }
            )

            restartBuildingJob(building)
        }
    }


    private fun updateBuildingBuyability(cookieCount: Long) {
        _state.value = _state.value.copy(
            buildingList = _state.value.buildingList.map { building ->
                building.copy(canBuy = building.cost <= cookieCount)
            }
        )
    }


    private fun restartBuildingJob(building: Building) {
        buildingJobs[building.id]?.cancel()

        val updatedBuilding = _state.value.buildingList.first { it.id == building.id }

        if (updatedBuilding.count > 0) {
            val job = viewModelScope.launch {
                while (true) {
                    if (updatedBuilding.cookiesPerSecond < 1) {
                        val interval = (1000L / updatedBuilding.cookiesPerSecond).toLong()
                        kotlinx.coroutines.delay(interval)
                        _state.update { state ->
                            state.copy(cookieCount = state.cookieCount + updatedBuilding.count)
                        }
                    } else {
                        kotlinx.coroutines.delay(1000L)
                        val earnedCookies = (updatedBuilding.count * updatedBuilding.cookiesPerSecond).toInt()
                        _state.update { state ->
                            state.copy(cookieCount = state.cookieCount + earnedCookies)
                        }
                    }
                    updateBuildingBuyability(_state.value.cookieCount)
                }
            }
            buildingJobs[building.id] = job
        }
    }



}
