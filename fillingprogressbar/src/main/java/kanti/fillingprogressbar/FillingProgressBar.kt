package kanti.fillingprogressbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.FloatRange
import androidx.core.content.res.getBooleanOrThrow
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.res.getFloatOrThrow

class FillingProgressBar @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	defStyleRes: Int = R.style.Widget_Kanti_FillingProgressBar
) : View(context, attrs, defStyleAttr, defStyleRes) {

	@Dimension private var mDiameter: Float
	@Dimension private var mStrokeWidth: Float

	@ColorInt private var mBackStrokeColor: Int
	@ColorInt private var mFrontFillColor: Int
	@ColorInt private var mDisabledColor: Int

	@FloatRange(from = 0.0, to = 1.0) private var mProgress: Float
	@FloatRange(from = 0.0, to = 1.0) private val mLightModifier: Float = 0.9f
	@FloatRange(from = 0.0, to = 1.0) private val mOpacity: Float = 0.38f

	private val paintBackStroke: Paint
	private val paintFrontStroke: Paint
	private val paintFill: Paint

	var diameter: Float
		@Dimension get() = mDiameter
		set(@Dimension value) {
			mDiameter = value
			invalidate()
			requestLayout()
		}

	var strokeWidth: Float
		@Dimension get() = mStrokeWidth
		set(@Dimension value) {
			mStrokeWidth = value
			invalidate()
		}

	var backStrokeColor: Int
		@ColorInt get() = mBackStrokeColor
		set(@ColorInt value) {
			mBackStrokeColor = value
			if (isEnabled) {
				paintBackStroke.color = value
				invalidate()
			}
		}

	var frontFillColor: Int
		@ColorInt get() = mFrontFillColor
		set(@ColorInt value) {
			mFrontFillColor = value
			if (isEnabled) {
				paintFrontStroke.color = colorLightModifying()
				paintFrontStroke.computeAlpha(isEnabled)

				paintFill.color = value
				paintFill.computeAlpha(isEnabled)
				invalidate()
			}
		}

	var disabledColor: Int
		@ColorInt get() = mDisabledColor
		set(@ColorInt value) {
			mDisabledColor = value
			if (!isEnabled) {
				paintBackStroke.color = mDisabledColor
				paintBackStroke.computeBackStrokeAlpha(isEnabled)

				paintFrontStroke.color = mDisabledColor
				paintFrontStroke.computeAlpha(isEnabled)

				paintFill.color = mDisabledColor
				paintFill.computeAlpha(isEnabled)
				invalidate()
			}
		}

	var progress: Float
		@FloatRange(from = 0.0, to = 1.0) get() = mProgress
		set(@FloatRange(from = 0.0, to = 1.0) value) {
			checkProgress(value)
			mProgress = value
			paintFill.computeAlpha(isEnabled)
			paintFrontStroke.computeAlpha(isEnabled)
			invalidate()
		}

	init {
		var enabled = true
		context.theme.obtainStyledAttributes(
			attrs,
			R.styleable.FillingProgressBar,
			defStyleAttr,
			defStyleRes
		).apply {
			try {
				mDiameter = getDimensionOrThrow(R.styleable.FillingProgressBar_diameter)
				mStrokeWidth = getDimensionOrThrow(R.styleable.FillingProgressBar_strokeWidth)

				mBackStrokeColor = getColorOrThrow(R.styleable.FillingProgressBar_backStrokeColor)
				mFrontFillColor = getColorOrThrow(R.styleable.FillingProgressBar_frontFillColor)
				mDisabledColor = getColorOrThrow(R.styleable.FillingProgressBar_disabledColor)

				mProgress = getFloatOrThrow(R.styleable.FillingProgressBar_progress).also {
					checkProgress(it)
				}

				enabled = getBooleanOrThrow(R.styleable.FillingProgressBar_android_enabled)
			} finally {
				recycle()
			}
		}

		paintBackStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = mBackStrokeColor
			computeBackStrokeAlpha(enabled)
			strokeWidth = mStrokeWidth
			style = Paint.Style.STROKE
		}
		paintFrontStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = colorLightModifying()
			computeAlpha(enabled)
			strokeWidth = mStrokeWidth
			style = Paint.Style.STROKE
		}
		paintFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = mFrontFillColor
			computeAlpha(enabled)
		}

		isEnabled = enabled
	}

	override fun setEnabled(enabled: Boolean) {
		if (enabled) {
			paintBackStroke.color = mBackStrokeColor
			paintFrontStroke.color = mFrontFillColor
			paintFill.color = mFrontFillColor
		} else {
			paintBackStroke.color = mDisabledColor
			paintFrontStroke.color = mDisabledColor
			paintFill.color = mDisabledColor

		}
		paintBackStroke.computeBackStrokeAlpha(enabled)
		paintFrontStroke.computeAlpha(enabled)
		paintFill.computeAlpha(enabled)
		super.setEnabled(enabled)
	}

	override fun onDraw(canvas: Canvas) {
		val centerX = paddingLeft + (mDiameter / 2f)
		val centerY = paddingTop + (mDiameter / 2f)

		val radius = mDiameter / 2
		val strokeRadius = radius - mStrokeWidth / 2

		canvas.drawCircle(centerX, centerY, radius, paintFill)
		canvas.drawCircle(centerX, centerY, strokeRadius, paintBackStroke)
		canvas.drawCircle(centerX, centerY, strokeRadius, paintFrontStroke)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		val minWidth = (paddingStart + mDiameter + paddingEnd).toInt()
		val minHeight = (paddingBottom + mDiameter + paddingTop).toInt()

		val width = resolveSizeAndState(minWidth, widthMeasureSpec, 0)
		val height = resolveSizeAndState(minHeight, heightMeasureSpec, 0)

		setMeasuredDimension(width, height)
	}

	private fun checkProgress(value: Float) {
		if (value !in 0.0..1.0)
			throw IllegalArgumentException("The progress cannot be greater than 1 or less than 0, actual = $value")
	}

	private fun colorLightModifying(): Int {
		val alpha = Color.alpha(mFrontFillColor)
		val red = Color.red(mFrontFillColor)
		val green = Color.green(mFrontFillColor)
		val blue = Color.blue(mFrontFillColor)

		return Color.argb(
			alpha,
			(red * mLightModifier).toInt(),
			(green * mLightModifier).toInt(),
			(blue * mLightModifier).toInt()
		)
	}

	private fun Paint.computeAlpha(enabled: Boolean) {
		alpha = if (enabled) {
			(alpha * mProgress).toInt()
		} else {
			(alpha * mProgress * mOpacity).toInt()
		}
	}

	private fun Paint.computeBackStrokeAlpha(enabled: Boolean) {
		alpha = if (enabled) {
			alpha
		} else {
			(alpha * mOpacity).toInt()
		}
	}
}