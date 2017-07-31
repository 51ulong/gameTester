import com.oracle.tools.packager.windows.WinExeBundler
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.CvType
import org.opencv.highgui.Highgui
import org.opencv.highgui.Highgui.CV_LOAD_IMAGE_UNCHANGED
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import java.io.FileOutputStream


fun main(args: Array<String>) {


    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    val exePath = "/usr/bin/chromedriver"
    System.setProperty("webdriver.chrome.driver", exePath)
    val driver = ChromeDriver()
    driver.get("http://127.0.0.1:7456/")
    driver.manage().window().size = Dimension(750, 600)
    driver.navigate().refresh()

    Thread.sleep(1000)

    val screenBytes = driver.getScreenshotAs(OutputType.BYTES)
//    val osw = FileOutputStream("scree.shot.png")
//    osw.write(screenBytes)


    try {

//
//        val mat = Highgui.imread("scree.shot.png")
//        println("from high")
//        println(mat.size())
//        println(mat.type())
//
//        println(mat.get(0,0))

        println(screenBytes.size)
        val screenMatByte = Mat(1, screenBytes.size, CvType.CV_8U)
        screenMatByte.put(0, 0, screenBytes)

        val screenMat = Highgui.imdecode(screenMatByte, CV_LOAD_IMAGE_UNCHANGED)

        println("next")
        println(screenMat.size())

    } finally {
        driver.close()

    }

//    Highgui.imwrite("matchoutput-2.jpg", screenMat)


}