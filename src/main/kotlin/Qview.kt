import com.moandjiezana.toml.Toml
import java.net.URL
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import tornadofx.*


class Qview: View("QuickMeet") {
    private val toml = Toml()
    private val sspDesc = time.stringBinding { "%s".format(it) }
    private fun loadDat() {
        if(persons.isNotEmpty()) persons.clear()
        if(!webinarmap.isNullOrEmpty()){
            for(i in webinarmap.entries){
                persons.add(Person(i.key, i.value as String))
            }
        }
    } //set persons to webinarmap
    private val persons = FXCollections.observableArrayList<Person>()

    private val tbl = tableview(persons) {
        column("회의 이름", Person::name).prefWidth(40)
        column("주소",Person::url)
        selectionModel.selectionModeProperty().set(SelectionMode.SINGLE)
        contextmenu {
            item("회의 입장").action {
                hostServices.showDocument(persons[selectionModel.selectedIndex].url)
            }
        }
    }
    private val namae = SimpleStringProperty()
    private val metUrl = SimpleStringProperty()
    init {
        //load data before popup
        if(toml.read(fil).toMap().isNotEmpty()) {
            webinarmap = toml.read(fil).toMap()
            loadDat()
        }
    }

    override val root = vbox {
        borderpane {
            top = form{
                fieldset("회의 정보") {
                    field("이름") {
                        textfield(namae)
                    }
                    field("주소") {
                        textfield(metUrl)
                    }
                }
            }
            left = vbox {
                button("추가"){
                    vboxConstraints {
                        marginRight = 10.0
                        vgrow = Priority.ALWAYS
                    }
                    useMaxWidth = true
                    action {
                        if(namae.value.isNotBlank() && metUrl.value.isNotBlank()){
                            if(isValid(when{
                                    !metUrl.value.contains("://") -> "http://${metUrl.value}"
                                        else -> metUrl.value
                            })) webinarmap[namae.value] = metUrl.value
                            loadDat()
                            println(webinarmap)
                            namae.value = ""
                            metUrl.value = ""
                        }
                    }
                    shortcut("Enter")
                }
                button("제거"){
                    vboxConstraints {
                        marginRight = 10.0
                        vgrow = Priority.ALWAYS
                    }
                    useMaxWidth = true
                    action{
                        val selrows = tbl.selectionModel.selectedItems
                        val rows = ArrayList(selrows)
                        rows.forEach{row -> webinarmap.remove(row.name) }
                        loadDat()
                    }
                    shortcut("Delete")
                }

            }
            center = tbl
            bottom = label(sspDesc){
                this.font = Font(20.0)
            }
        }
    }
}

class Person(name: String, url: String) {
    val nameProperty = SimpleStringProperty(name)
    var name by  nameProperty

    val urlProperty = SimpleStringProperty(url)
    var url by urlProperty
}

fun isValid(url: String): Boolean{
    return try{
        URL(url).toURI()
        true
    }catch (e: Exception){
        false
    }
}