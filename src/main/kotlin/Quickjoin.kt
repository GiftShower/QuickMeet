import com.moandjiezana.toml.TomlWriter
import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Stage
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import tornadofx.App
import tornadofx.FX
import java.text.SimpleDateFormat
import java.util.*


class Quickjoin: App(Qview::class){
    override fun start(stage: Stage) {
        Prep().start()
        val img = Image("https://bit.ly/3iyKsTK")
        stage.title = "QuickMeet"
        CoroutineScope(Default).launch {
            val format1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            while (arun == 0){
                val tme = Date()
                Platform.runLater(Runnable {
                    time.value = format1.format(tme)
                })
                delay(1000L)
                yield()
            }
        }
        super.start(stage)
        FX.primaryStage.icons += img
    }
    override fun stop() {
        Default.cancel()
        arun = 1
        val tomlWriter = TomlWriter()
        tomlWriter.write(webinarmap, fil)
        super.stop()
    }

    fun main(args: Array<String>) {
        launch(*args)
    }
}