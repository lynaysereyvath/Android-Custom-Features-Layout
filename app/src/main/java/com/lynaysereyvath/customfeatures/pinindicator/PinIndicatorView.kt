package com.lynaysereyvath.customfeatures.pinindicator

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.lynaysereyvath.customfeatures.R
import com.lynaysereyvath.customfeatures.databinding.ViewPinIndicatorBinding

interface IPinEnterListener {
    fun onPinEntered(length: Int, delete: Boolean = false)
}

class PinIndicatorView : FrameLayout, IPinIndicatorView {

    private var binding: ViewPinIndicatorBinding? = null

    var enterPinListener: IPinEnterListener? = null
    private val pinIndicators = ArrayList<ImageView?>()
    private var onFinishedListener: (() -> Unit)? = null
    private var onFinishedLoadingListener: (() -> Unit)? = null
    private var enableLoading = false

    private val animator = PinIndicatorAnimator()

    constructor(context: Context) : super(context) {
        initViews()
    }

    constructor(context: Context, attrSet: AttributeSet) : super(context, attrSet) {
        initViews()
    }

    constructor(context: Context, attrSet: AttributeSet, defStyle: Int) : super(
        context,
        attrSet,
        defStyle
    ) {
        initViews()
    }

    private fun initViews() {
        binding = ViewPinIndicatorBinding.inflate(LayoutInflater.from(context), this, true)
        pinIndicators.addAll(
            arrayListOf(
                binding?.ind1,
                binding?.ind2,
                binding?.ind3,
                binding?.ind4
            )
        )
        setUpEnterListener()
    }

    private fun setUpEnterListener() {
        enterPinListener = object : IPinEnterListener {
            override fun onPinEntered(length: Int, delete: Boolean) {
                if (length <= 4) {
                    if (delete) {
                        pinIndicators[length]?.setImageResource(R.drawable.ic_pin_indicator_unfilled)
                    } else {
                        pinIndicators[length - 1]?.apply {
                            setImageResource(R.drawable.ic_pin_indicator_filled)
                            animator.animateEnteredPin(this)
                        }
                    }

                    if (length == 4) {
                        onFinished()
                    }
                }
            }
        }
    }

    private fun load() {
        binding?.loading?.visibility = View.VISIBLE

        animator.animateLoading(pinIndicators, binding?.loading)
    }

    override fun onFinished() {
        if (enableLoading) load()
        onFinishedListener?.let { it() }
    }

    override fun enableLoading(enable: Boolean): PinIndicatorView {
        enableLoading = enable
        return this
    }

    override fun onFinishedLoading() {
        onFinishedLoadingListener?.let { it() }
    }

    override fun addOnFinishedListener(listener: () -> Unit): PinIndicatorView {
        onFinishedListener = listener
        return this
    }

    override fun addOnFinishedLoadingListener(listener: () -> Unit): PinIndicatorView {
        onFinishedLoadingListener = listener
        return this
    }


    override fun setError() {
        binding?.background?.setBackgroundResource(R.drawable.bg_pin_indicator_error)
    }

    class PinIndicatorAnimator() {

        private val animationDuration = 200L

        fun animateLoading(pinIndicators: ArrayList<ImageView?>, circularProgressIndicator: CircularProgressIndicator?) {
            val animatorSet = AnimatorSet()
            val anim = arrayListOf<ObjectAnimator>()
            pinIndicators[0]?.apply {
                anim.add(animateAlpha(this))
                anim.add(animateTranslate(this, 40f))
            }
            pinIndicators[1]?.apply {
                anim.add(animateAlpha(this))
                anim.add(animateTranslate(this, 20f))
            }
            pinIndicators[2]?.apply {
                anim.add(animateAlpha(this))
                anim.add(animateTranslate(this, -20f))
            }
            pinIndicators[3]?.apply {
                anim.add(animateAlpha(this))
                anim.add(animateTranslate(this, -40f))
            }
            anim.addAll(animateProgress(circularProgressIndicator))
            animatorSet.apply {
                playTogether(anim as Collection<Animator>?)
                doOnEnd {
                    pinIndicators.forEach {
                        it?.visibility = View.INVISIBLE
                    }
                }
                start()
            }
        }
        private fun animateProgress(view: CircularProgressIndicator?): ArrayList<ObjectAnimator> {
            return arrayListOf(
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
                    duration = animationDuration
                },
                ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f).apply {
                    duration = animationDuration
                },
                ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f).apply {
                    duration = animationDuration
                }
            )
        }

        private fun animateAlpha(imageView: ImageView?): ObjectAnimator {
            return ObjectAnimator.ofFloat(imageView, "alpha", 1f, 0f).apply {
                duration = animationDuration
            }
        }

        private fun animateTranslate(imageView: ImageView?, distance: Float): ObjectAnimator {
            return ObjectAnimator.ofFloat(imageView, "translationX", 0f, distance).apply {
                duration = animationDuration
            }
        }

        fun animateEnteredPin(imageView: ImageView?) {
            AnimatorSet().apply {
                playTogether(
                    ObjectAnimator.ofFloat(imageView, "scaleX", 1f, 1.2f).apply {
                        duration = 100
                        repeatMode = ObjectAnimator.REVERSE
                        repeatCount = 1
                    },
                    ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 1.2f).apply {
                        duration = 100
                        repeatMode = ObjectAnimator.REVERSE
                        repeatCount = 1
                    }
                )
                start()
            }
        }
    }

}