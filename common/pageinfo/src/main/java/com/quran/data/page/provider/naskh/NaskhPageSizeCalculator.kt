package com.quran.data.page.provider.naskh

import com.quran.data.source.DisplaySize
import com.quran.data.source.PageSizeCalculator

internal class NaskhPageSizeCalculator(displaySize: DisplaySize) : PageSizeCalculator {
  private val screenRatios = doubleArrayOf(4.0 / 3.0, 16.0 / 10.0, 5.0 / 3.0, 16.0 / 9.0)
  private val ratioIndex = getScreenRatioIndex(displaySize.x, displaySize.y)

  private fun getScreenRatioIndex(width: Int, height: Int): Int {
    var aspectRatio = height.toDouble() / width
    if (aspectRatio < 1) {
      // getRealSize "size is adjusted based on the current rotation of the display"
      aspectRatio = 1.0 / aspectRatio
    }

    // pick the closest
    var closest = 0 //keeps the id of the array
    var minDelta = aspectRatio

    for (i in screenRatios.indices) {
      val difference = Math.abs(aspectRatio - screenRatios[i])
      if (difference < minDelta) {
        closest = i
        minDelta = difference
      } else {
        // once minDelta > difference, the difference will only grow since
        // screen ratios is incremental.
        break
      }
    }
    return closest
  }

  override fun getWidthParameter(): String {
    return when (ratioIndex) {
      0 -> { "1536" } // 4:3
      1 -> { "1280" } // 16:10
      2 -> { "1227" } // 5:3
      3 -> { "1152" } // 16:9 and fallback
      else -> { "1152" }
    }
  }

  override fun getTabletWidthParameter(): String {
    // use the same size for tablet landscape
    return getWidthParameter()
  }

  override fun setOverrideParameter(parameter: String) {
    // override parameter is irrelevant for naskh pages
  }
}
