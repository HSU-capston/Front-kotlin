package com.example.tflitetestapp

data class DrawObject(
    val box: FloatArray,   // [left, top, right, bottom]
    val label: String,
    val score: Float
)

