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
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.calib3d.Calib3d
import org.opencv.core.MatOfPoint2f
import org.opencv.core.MatOfByte


class Simulator {


    fun click(btn: String) {
        println(btn)
    }
}


class TargetFinder {

    private val descExtractor: DescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF)

    private val featureExtractor: FeatureDetector = FeatureDetector.create(FeatureDetector.SURF)

    fun inScreen(): TargetFinder {

        return this
    }

    fun findTarget(): Point {

        return Point()
    }

}


fun main(args: Array<String>) {

    println(System.getProperty("java.library.path"))

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)


    val featureDetector = FeatureDetector.create(FeatureDetector.SURF)
    val descExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF)

    val targetImage = Highgui.imread("images/login.png")
    val targetFeatureKeyPoints = MatOfKeyPoint()
    val targetDescriptorKeyPoints = MatOfKeyPoint()
    featureDetector.detect(targetImage, targetFeatureKeyPoints)
    descExtractor.compute(targetImage, targetFeatureKeyPoints, targetDescriptorKeyPoints)


    val screenImage = Highgui.imread("images/gamescreen.png")
    val screenFeatureKeyPoints = MatOfKeyPoint()
    val screenDescriptorKeyPoints = MatOfKeyPoint()
    featureDetector.detect(screenImage, screenFeatureKeyPoints)
    descExtractor.compute(screenImage, screenFeatureKeyPoints, screenDescriptorKeyPoints)


    val matchoutput = Mat(screenImage.rows() * 2, screenImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR)
    val matchestColor = Scalar(0.0, 255.0, 0.0)


    val matches = LinkedList<MatOfDMatch>()
    val descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED)
    descriptorMatcher.knnMatch(targetDescriptorKeyPoints, screenDescriptorKeyPoints, matches, 2)


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


        val featureKeyPointList = targetFeatureKeyPoints.toList()
        val screenKeyPointList = screenFeatureKeyPoints.toList()

        val objectPoints = LinkedList<Point>()
        val scenePoints = LinkedList<Point>()

        for (match in goodMatchesList) {
            objectPoints.addLast(featureKeyPointList.get(match.queryIdx).pt)
            scenePoints.addLast(screenKeyPointList.get(match.trainIdx).pt)
        }


        val objMatOfPoint2f = MatOfPoint2f()
        objMatOfPoint2f.fromList(objectPoints)
        val scnMatOfPoint2f = MatOfPoint2f()
        scnMatOfPoint2f.fromList(scenePoints)

        val homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3.0)

        val obj_corners = Mat(4, 1, CvType.CV_32FC2)
        val scene_corners = Mat(4, 1, CvType.CV_32FC2)

        obj_corners.put(0, 0, *doubleArrayOf(0.0, 0.0))
        obj_corners.put(1, 0, *doubleArrayOf(targetImage.cols() * 1.0, 0.0))
        obj_corners.put(2, 0, *doubleArrayOf(targetImage.cols() * 1.0, targetImage.rows() * 1.0))
        obj_corners.put(3, 0, *doubleArrayOf(0.0, targetImage.rows() * 1.0))

        println("Transforming object corners to scene corners...")
        Core.perspectiveTransform(obj_corners, scene_corners, homography)

        val img = Highgui.imread("images/gamescreen.png", Highgui.CV_LOAD_IMAGE_COLOR)

        Core.line(img, Point(scene_corners.get(0, 0)), Point(scene_corners.get(1, 0)), Scalar(0.0, 255.0, 0.0), 4)
        Core.line(img, Point(scene_corners.get(1, 0)), Point(scene_corners.get(2, 0)), Scalar(0.0, 255.0, 0.0), 4)
        Core.line(img, Point(scene_corners.get(2, 0)), Point(scene_corners.get(3, 0)), Scalar(0.0, 255.0, 0.0), 4)
        Core.line(img, Point(scene_corners.get(3, 0)), Point(scene_corners.get(0, 0)), Scalar(0.0, 255.0, 0.0), 4)

        println(scene_corners.dump())

        val center = Core.mean(scene_corners)


        println(scene_corners.dump())

        println("the center is  ${center}")

        val goodMatches = MatOfDMatch()
        goodMatches.fromList(goodMatchesList)

        val colorScalar = Scalar(255.0, .0, .0)

        Features2d.drawMatches(
                targetImage, targetFeatureKeyPoints,
                screenImage, screenFeatureKeyPoints,
                goodMatches,
                matchoutput, matchestColor,
                colorScalar, MatOfByte(), 2)

        Highgui.imwrite("matchoutput.jpg", matchoutput)
        Highgui.imwrite("img.jpg", img)
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





