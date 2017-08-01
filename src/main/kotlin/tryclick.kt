import com.ulong51.WebSimulator
import org.openqa.selenium.Dimension


fun main(args: Array<String>) {

    val simulator = WebSimulator("http://127.0.0.1:7456/")

    try {
        simulator.designDimension(Dimension(750, 600))
                .launch()
                .wait(2)
                .click("images/login.png")
                .wait(3)

    } finally {
        simulator.quit()
    }

}