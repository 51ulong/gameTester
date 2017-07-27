/**
 * Created by administrator on 6/13/17.
 */


package com.ulong51

import org.opencv.core.*
import org.opencv.features2d.BFMatcher
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.xfeatures2d.SURF
import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.FeatureDetector
import org.opencv.core.KeyPoint
import org.opencv.features2d.DescriptorExtractor
import org.opencv.features2d.Features2d
import org.opencv.core.Scalar
import org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_COLOR
import org.opencv.core.Mat


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


//    val featureDetector = FeatureDetector.create(FeatureDetector.SURF)
    val featureDetector = SURF.create()

    val descExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF)

    DescriptorExtractor


    val loginImage = Imgcodecs.imread("images/login.png")

    val screenImage = Imgcodecs.imread("images/gamescreen.png")

    val targetFeature = MatOfKeyPoint()
    val backgroundFeature = MatOfKeyPoint()

    featureDetector.detect(loginImage, targetFeature)


    val targetDescripto = MatOfKeyPoint()
    descExtractor.compute(loginImage, targetFeature, targetDescripto)

    val outputImage = Mat(loginImage.rows(), loginImage.cols(), CV_LOAD_IMAGE_COLOR)
    val newKeypointColor = Scalar(255.0, 0.0, 0.0)

    val keyPoints = targetFeature.toArray()


    println("Drawing key points on object image...")
    Features2d.drawKeypoints(loginImage, targetFeature, outputImage, newKeypointColor, 0)


    Imgcodecs.imwrite("testout.png", outputImage)


//
//    featureDetector.detect(screenImage, backgroundFeature)
//
//
//
//    for (ky in keyPoints) {
//        println(ky)
//    }

}





