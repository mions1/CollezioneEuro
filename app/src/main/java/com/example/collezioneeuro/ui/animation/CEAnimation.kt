package com.example.collezioneeuro.ui.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.squareup.picasso.Picasso

/**
 * Questa classe gestisce le animazioni.
 */
class CEAnimation(val listener: AnimationListener) {

    /**
     * Ascoltatori
     */
    interface AnimationListener {
        fun onAnimationStart()
        fun onAnimationStop()
        fun onAnimationCancel()
    }

    companion object {
        private var currentAnimator: Animator? = null
    }

    /**
     * Inizia l'animazione di ingrandimento di un'immagine
     */
    fun startZoomImage(
        clickedImage: View,
        expandedImageView: ImageView,
        imageUrl: String,
        animDuration: Long
    ) {
        // annulla eventuali animazioni in corso
        currentAnimator?.cancel()

        // carica l'immagine da visualizzare in grande
        Picasso.get().load(imageUrl).into(expandedImageView)

        val startFinalBoundsStartScale =
            getStartBoundsAndStartScale(clickedImage, expandedImageView)
        val startBounds = startFinalBoundsStartScale.first
        val finalBounds = startFinalBoundsStartScale.second
        val startScale = startFinalBoundsStartScale.third

        // nascondi l'immagine cliccata e mostra quella ingrandita
        clickedImage.alpha = 0f
        expandedImageView.visibility = View.VISIBLE

        expandedImageView.pivotX = 0f
        expandedImageView.pivotY = 0f

        // costruisci l'animazione
        currentAnimator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    expandedImageView,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(
                    ObjectAnimator.ofFloat(
                        expandedImageView,
                        View.Y,
                        startBounds.top,
                        finalBounds.top
                    )
                )
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f))
            }
            duration = animDuration
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    currentAnimator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    listener.onAnimationCancel()
                    currentAnimator = null
                }
            })
            start()
            listener.onAnimationStart()
        }
    }

    /**
     * Inizia l'animazione di rimpicciolimento dell'immagine ingrandita con startZoomImage
     */
    fun finishZoomImage(
        clickedImage: View,
        expandedImageView: ImageView,
        animDuration: Long
    ) {
        currentAnimator?.cancel()

        val startFinalBoundsStartScale =
            getStartBoundsAndStartScale(clickedImage, expandedImageView)
        val startBounds = startFinalBoundsStartScale.first
        val startScale = startFinalBoundsStartScale.third

        currentAnimator = AnimatorSet().apply {
            play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left)).apply {
                with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
                with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale))
            }
            duration = animDuration
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    clickedImage.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    listener.onAnimationStop()
                }

                override fun onAnimationCancel(animation: Animator) {
                    clickedImage.alpha = 1f
                    expandedImageView.visibility = View.GONE
                    currentAnimator = null
                    listener.onAnimationStart()
                }
            })
            start()
            listener.onAnimationStart()
        }
    }

    private fun getStartBoundsAndStartScale(
        view: View,
        container: View
    ): Triple<RectF, RectF, Float> {
        // calcola i confini iniziali e finali per l'immagine zoommata. Tanta matematica.
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        view.getGlobalVisibleRect(startBoundsInt)
        container
            .getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)

        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // Extend start bounds horizontally
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // Extend start bounds vertically
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        return Triple(startBounds, finalBounds, startScale)
    }

}