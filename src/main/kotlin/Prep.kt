import javafx.beans.property.SimpleStringProperty
import java.io.*

lateinit var fil: File
lateinit var webinarmap: MutableMap<String, Any>
var time = SimpleStringProperty("InitValue")
var arun = 0

class Prep {
    fun start() {
        val dataFolder = System.getenv("APPDATA") //앱데이터 불러오기
        fil = File("$dataFolder\\Autoenter\\data.toml")
        val folder = File("$dataFolder\\Autoenter")
        try {
            if(!folder.exists()) folder.mkdirs() //폴더 생성 <- 이새끼가 문제네 , 이제 될듯 , 니 파일 다시 안썼잖아
            val success = fil.exists()
            if (success) {
                println("Failed to create datafile. Already Exists.")
            } else{
                fil.createNewFile()
                println("Created New File!")
            } //data.toml 생성
        } catch (e: IOException) {
            e.printStackTrace()
        }
        webinarmap = HashMap()
    }
}