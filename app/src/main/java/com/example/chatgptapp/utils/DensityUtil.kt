package com.example.chatgptapp.utils

import android.content.Context
import android.util.TypedValue

/**
 * @author :lwh
 * @date : 2023/3/17
 */
class DensityUtil {
    companion object {
        /**
         * dp转px
         *
         * @param context
         * @param dpVal
         * @return
         */
        fun dp2px(context: Context, dpVal: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpVal,
                context.resources.displayMetrics
            ).toInt()
        }

        /**
         * sp转px
         *
         * @param context
         * @param spVal
         * @return
         */
        fun sp2px(context: Context, spVal: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, spVal,
                context.resources.displayMetrics
            ).toInt()
        }

        /**
         * px转dp
         *
         * @param context
         * @param pxVal
         * @return
         */
        fun px2dp(context: Context, pxVal: Float): Float {
            val scale = context.resources.displayMetrics.density
            return pxVal / scale
        }

        /**
         * px转sp
         *
         * @param pxVal
         * @param pxVal
         * @return
         */
        fun px2sp(context: Context, pxVal: Float): Float {
            return pxVal / context.resources.displayMetrics.scaledDensity
        }


    }
}