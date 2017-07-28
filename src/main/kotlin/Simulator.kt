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
import org.opencv.features2d.DescriptorMatcher
import org.opencv.core.MatOfDMatch
import java.util.LinkedList
import org.opencv.features2d.DMatch


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
    val targetFeature = MatOfKeyPoint()


    featureDetector.detect(targetImage, targetFeature)

    val targetDescriptor = MatOfKeyPoint()

    val outputImage = Mat(targetImage.rows(), targetImage.cols(), CV_LOAD_IMAGE_COLOR)
    val newKeypointColor = Scalar(255.0, 0.0, 0.0)

    println("Drawing key points on object image...")
    Features2d.drawKeypoints(targetImage, targetFeature, outputImage, newKeypointColor, 0)

    Highgui.imwrite("testout.png", outputImage)


    descExtractor.compute(targetImage, targetFeature, targetDescriptor)


    val screenImage = Highgui.imread("images/gamescreen.png", CV_LOAD_IMAGE_COLOR)
    val screenKeyPoint = MatOfKeyPoint()
    val screenKeyDescriptor = MatOfKeyPoint()

    featureDetector.detect(screenImage, screenKeyPoint)

    descExtractor.compute(screenImage, screenKeyPoint, screenKeyDescriptor)


    val matchoutput = Mat(screenImage.rows() * 2, screenImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR)
    val matchestColor = Scalar(0.0, 255.0, 0.0)


    val matches = LinkedList<MatOfDMatch>()
    val descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED)

    descriptorMatcher.knnMatch(targetDescriptor, screenKeyDescriptor, matches, 2)


    val nndrRatio = 0.7f
    val goodMatchesList = LinkedList<DMatch>()

    for (matofDMatch in matches) {
        val distanceMatchArray = matofDMatch.toArray()
        val m1 = distanceMatchArray[0]
        val m2 = distanceMatchArray[1]

        if (m1.distance <= m2.distance * nndrRatio) {
            goodMatchesList.addLast(m1)
        }
    }


    println("the mathes size  ${goodMatchesList.size}")

    if (goodMatchesList.size >= 7) {
        System.out.println("Object Found!!!")
    }


//
//    featureDetector.detect(screenImage, backgroundFeature)
//
//
//
//    for (ky in keyPoints) {
//        println(ky)
//    }

}





