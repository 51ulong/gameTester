/**
 * Created by administrator on 6/13/17.
 */


package com.ulong51

import org.opencv.core.Core


class Simulator {


    fun click( btn: String) {
        println(btn)
    }
}




fun main(args: Array<String>) {

    println(System.getProperty("java.library.path"))

    System.loadLibrary(Core.NATIVE_LIBRARY_NAME)


    val sim1 = Simulator()

    sim1.click("start_png.png")

    println(Core.NATIVE_LIBRARY_NAME)
}





