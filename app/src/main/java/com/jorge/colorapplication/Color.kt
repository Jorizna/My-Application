package com.jorge.colorapplication

import com.jorge.colorapplication.R

sealed class Color(
    private val res20: Int,
    private val res35: Int,
    private val res50: Int,
    private val res65: Int,
    private val res80: Int
) {
    fun getRes(opacity: Int): Int {
        return when (opacity) {
            20 -> res20
            35 -> res35
            50 -> res50
            65 -> res65
            else -> res80
        }
    }

    object White : Color(R.color.white_20, R.color.white_35, R.color.white_50, R.color.white_65, R.color.white_80)
    object Red : Color(R.color.red_20, R.color.red_35, R.color.red_50, R.color.red_65, R.color.red_80)
    object Orange : Color(R.color.orange_20, R.color.orange_35, R.color.orange_50, R.color.orange_65, R.color.orange_80)
    object Yellow : Color(R.color.yellow_20, R.color.yellow_35, R.color.yellow_50, R.color.yellow_65, R.color.yellow_80)
    object Green : Color(R.color.green_20, R.color.green_35, R.color.green_50, R.color.green_65, R.color.green_80)
    object Cyan : Color(R.color.cyan_20, R.color.cyan_35, R.color.cyan_50, R.color.cyan_65, R.color.cyan_80)
    object Blue : Color(R.color.blue_20, R.color.blue_35, R.color.blue_50, R.color.blue_65, R.color.blue_80)
    object Violet : Color(R.color.violet_20, R.color.violet_35, R.color.violet_50, R.color.violet_65, R.color.violet_80)
    object Black : Color(R.color.black_20, R.color.black_35, R.color.black_50, R.color.black_65, R.color.black_80)
}