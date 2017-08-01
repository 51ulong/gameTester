package com.ulong51

import org.opencv.core.Mat
import org.opencv.highgui.Highgui
import org.opencv.core.CvType
import org.openqa.selenium.Dimension
import org.openqa.selenium.OutputType
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.remote.RemoteWebDriver


class WebSimulator {

    init {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver")
    }

    private var url: String
    private var dimension: Dimension = Dimension(750, 600)
    private var targetFinder: TargetFinder = TargetFinder()

    private val driver: RemoteWebDriver = ChromeDriver()

    constructor(targetUrl: String) {
        this.url = targetUrl
    }

    fun designDimension(dimension: Dimension): WebSimulator {
        this.dimension = dimension
        return this
    }

    fun launch(): WebSimulator {
        driver.get(url)
        driver.manage().window().size = dimension
        driver.navigate().refresh()
        return this
    }

    fun click(targeImagePath: String): WebSimulator {

        val screenMat = toPngMat(driver.getScreenshotAs(OutputType.BYTES))

        val clickPoint = targetFinder
                .withScreen(screenMat)
                .findTarget(Highgui.imread(targeImagePath))

        val gameCanvas = driver.findElementById("GameCanvas")
        val action = Actions(driver)

        action.moveToElement(gameCanvas, 0, 0)
                .moveByOffset(clickPoint.x.toInt(), clickPoint.y.toInt())
                .click()
                .perform()

        return this
    }

    fun wait(nSecond: Int): WebSimulator {
        Thread.sleep((nSecond * 1000).toLong())

        return this
    }

    private fun toPngMat(byteArray: ByteArray): Mat {
        val matOfBytes = Mat(1, byteArray.size, CvType.CV_8U)
        matOfBytes.put(0, 0, byteArray)

        return Highgui.imdecode(matOfBytes, Highgui.CV_LOAD_IMAGE_UNCHANGED)
    }

    fun quit() {
        driver.quit()
    }

}





