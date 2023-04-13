package screens.report

/*
*   viewModel экрана Составление заявки с сохранением
*   стейтов и связью с server
* */


import data.remote.models.RequestModel
import data.remote.services.contract.ReportService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.skia.impl.Log
import utils.ViewModel

class ReportViewModel : ViewModel() {

    private val reportService = ReportService.create()

    private val _firstname = MutableStateFlow("")
    val firstname = _firstname.asStateFlow()

    private val _secondname = MutableStateFlow("")
    val secondname = _secondname.asStateFlow()

    private val _lastname = MutableStateFlow("")
    val lastname = _lastname.asStateFlow()

    private val _expDriving = MutableStateFlow("")
    val expDriving = _expDriving.asStateFlow()

    private val _selectedRight = MutableStateFlow("")
    val selectedRight = _selectedRight.asStateFlow()

    private val _selectedAuto = MutableStateFlow("")
    val selectedAuto = _selectedAuto.asStateFlow()

    private val _dateStart = MutableStateFlow("")
    val dateStart = _dateStart.asStateFlow()

    private val _dateEnd = MutableStateFlow("")
    val dateEnd = _dateEnd.asStateFlow()

    fun onFirstnameChange(newFirstname: String){
        _firstname.value = newFirstname
    }
    fun onSecondnameChange(newSecondname: String){
        _secondname.value = newSecondname
    }
    fun onLastnameChange(newLastname: String) {
        _lastname.value = newLastname
    }
    fun onExpDrivingChange(newExpDriving: String) {
        _expDriving.value = newExpDriving
    }
    fun onSelectedRightChange(newRight: String) {
        _selectedRight.value = newRight
    }
    fun onSelectedAutoChange(newAuto: String) {
        _selectedAuto.value = newAuto
    }

    fun onDateStartChange(newDate: String) {
        _dateStart.value = newDate
    }

    fun onDateEndChange(newDate: String) {
        _dateEnd.value = newDate
    }

    fun isValidFields(
        fName: String,
        sName: String,
        lName: String
    ): Boolean
        = sName.length >= 5 && fName.length >= 3 && lName.length >= 5

    suspend fun postReportToServer(
        fNmae: String,
        sName: String,
        lName: String,
        selectRight: String,
        expDrive: String,
        selectAuto: String,
        dStart: String,
        dEnd: String
    ): Boolean {

        val auto = selectAuto.split("  ")

        val res = reportService.postReport(RequestModel(
            fNmae,
            sName,
            lName,
            selectRight,
            expDrive.toInt(),
            auto[0],
            auto[1],
            dStart,
            dEnd
        )).result

        Log.warn("res request is = $res")
        return res
    }
}