/**
 * Created by administrator on 6/13/17.
 */


package com.ulong51

import org.opencv.core.*

import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.FeatureDetector
import org.opencv.features2d.DescriptorExtractor
import org.opencv.features2d.Features2d
import org.opencv.core.Scalar
import org.opencv.core.Mat
import org.opencv.highgui.Highgui
import org.opencv.highgui.Highgui.CV_LOAD_IMAGE_COLOR


class Simulator {


    fun click(btn: String) {
        println(btn)
    }
}


fun main(args: Array<String>) {

    println(System.getProperty("java.library.path"))

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)

    println(Core.VERSION)


    val featureDetector = FeatureDetector.create(FeatureDetector.SURF)
    val descExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF)

    var targetImage = Highgui.imread("images/login.png", CV_LOAD_IMAGE_COLOR)
    val screenImage = Highgui.imread("images/gamescreen.png",CV_LOAD_IMAGE_COLOR)

    val targetFeature = MatOfKeyPoint()
    val backgroundFeature = MatOfKeyPoint()

    featureDetector.detect(targetImage, targetFeature)


    val targetDescripto = MatOfKeyPoint()
    descExtractor.compute(targetImage, targetFeature, targetDescripto)

    val outputImage = Mat(targetImage.rows(), targetImage.cols(), CV_LOAD_IMAGE_COLOR)
    val newKeypointColor = Scalar(255.0, 0.0, 0.0)

    println("Drawing key points on object image...")
    Features2d.drawKeypoints(targetImage, targetFeature, outputImage, newKeypointColor, 0)

    Highgui.imwrite("testout.png", outputImage)



    val sceenKeyPoint =  MatOfKeyPoint()
    val sceenKeyDescripto =  MatOfKeyPoint()

//
//    featureDetector.detect(screenImage, backgroundFeature)
//
//
//
//    for (ky in keyPoints) {
//        println(ky)
//    }

}





