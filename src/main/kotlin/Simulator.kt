/**
 * Created by administrator on 6/13/17.
 */


package com.ulong51

import org.opencv.core.*
import org.opencv.features2d.BFMatcher
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.xfeatures2d.SURF
import org.opencv.core.MatOfKeyPoint




class Simulator {


    fun click(btn: String) {
        println(btn)
    }
}


fun main(args: Array<String>) {

    println(System.getProperty("java.library.path"))

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    println(Core.VERSION)


    val mat = Mat(10, 10, CvType.CV_8UC1, Scalar(0.0))

    println(mat.row(1).col(1).setTo(Scalar(1.2)))
    val sim1 = Simulator()

    val put = mat.put(5, 5, 77.0)

    val dector = SURF.create()



    println(mat.dump())


    val loginImage = Imgcodecs.imread("images/login.png")

    var screenImage = Imgcodecs.imread("images/gamescreen.png")


    val dummy1 = MatOfKeyPoint()
    val dummy2 = MatOfKeyPoint()

    dector.detect(loginImage, dummy1)
    dector.detect(screenImage, dummy2)

    val bfMatcher = BFMatcher()


    val matchs = bfMatcher.match(dummy1,dummy2)





    sim1.click("start_png.png")

    println(Core.NATIVE_LIBRARY_NAME)
}





