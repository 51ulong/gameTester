
package com.ulong51

import org.opencv.calib3d.Calib3d
import org.opencv.core.*
import org.opencv.features2d.DMatch
import org.opencv.features2d.DescriptorExtractor
import org.opencv.features2d.DescriptorMatcher
import org.opencv.features2d.FeatureDetector
import java.util.*

class TargetFinder {

    init {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
    }

    private val descExtractor: DescriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF)

    private val featureDetector: FeatureDetector = FeatureDetector.create(FeatureDetector.SURF)

    private lateinit var screenImage: Mat
    private lateinit var targetImage: Mat

    fun withScreen(screen: Mat): TargetFinder {

        screenImage = screen
        return this
    }

    fun findTarget(target: Mat): Point {
        targetImage = target
        return surfFindTarget()
    }

    private fun surfFindTarget(): Point {

        val targetFeatureKeyPoints = MatOfKeyPoint()
        val targetDescriptorKeyPoints = MatOfKeyPoint()

        featureDetector.detect(targetImage, targetFeatureKeyPoints)
        descExtractor.compute(targetImage, targetFeatureKeyPoints, targetDescriptorKeyPoints)


        val screenFeatureKeyPoints = MatOfKeyPoint()
        val screenDescriptorKeyPoints = MatOfKeyPoint()
        featureDetector.detect(screenImage, screenFeatureKeyPoints)
        descExtractor.compute(screenImage, screenFeatureKeyPoints, screenDescriptorKeyPoints)


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

        if (goodMatchesList.size >= 7) {
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

            Core.perspectiveTransform(obj_corners, scene_corners, homography)

            val center = Core.mean(scene_corners)
            return Point(center.`val`[0], center.`val`[0])

        } else {
            throw RuntimeException("No Such Target in Screen")
        }
    }
}
