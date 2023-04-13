package screens.home

import com.itextpdf.text.Document
import com.itextpdf.text.Element
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import data.remote.models.ContractModel
import data.remote.services.reports.ContractService
import utils.ViewModel
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(): ViewModel() {

    private val FONT = "D:\\Code\\DashboardCurs\\src\\jvmMain\\kotlin\\styles\\font\\arial_bolditalicmt.ttf"
    private val service = ContractService.create()

    suspend fun getContract(): ContractModel = service.getContract()

    fun getPDF(contract: ContractModel) {
        val myDoc = Document()
        val mFileName = "Договор"
        val mFilePath = "D:\\$mFileName.pdf"

        try {

            val bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED)
            val fontHeader = Font(bf, 16f, Font.NORMAL)
            val font = Font(bf, 12f, Font.NORMAL)

            PdfWriter.getInstance(myDoc, FileOutputStream(mFilePath))
            myDoc.open()

            val data = "Договор по автопрокату\n\n"
            val data1 = "ФИО арендатора: ${contract.sName} ${contract.fName} ${contract.lName} \n"
            val data2 = "Категория прав: '${contract.right}' со стажем: ${contract.expDriving} лет \n"
            val data3 = "Автомобиль для аренды: ${contract.brand} ${contract.model} класса ${contract.clas} \n"
            val data4 = "Сроки аренды: от ${contract.dateStart} до ${contract.dateEnd} \n"
            val data5 = "Цена аренды(сутк.): ${contract.rentalPrice} руб. и общая стоимость проката ${contract.allPrice} руб. \n"

            val parag = Paragraph(data, fontHeader)
            parag.alignment = Element.ALIGN_CENTER

            myDoc.addAuthor("Ilya")
            myDoc.add(parag)
            myDoc.add(Paragraph(data1,font))
            myDoc.add(Paragraph(data2,font))
            myDoc.add(Paragraph(data3,font))
            myDoc.add(Paragraph(data4,font))
            myDoc.add(Paragraph(data5,font))
            myDoc.close()
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }
}