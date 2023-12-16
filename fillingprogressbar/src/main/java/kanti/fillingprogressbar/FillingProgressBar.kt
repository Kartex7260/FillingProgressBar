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
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.content.res.getFloatOrThrow

class FillingProgressBar @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0,
	defStyleRes: Int = R.style.FillingProgressBar
) : View(context, attrs, defStyleAttr, defStyleRes) {

	@Dimension private var mDiameter: Float
	@Dimension private var mStrokeWidth: Float

	@ColorInt private var mBackStrokeColor: Int
	@ColorInt private var mFrontFillColor: Int

	@FloatRange(from = 0.0, to = 1.0) private var mProgress: Float
	@FloatRange(from = 0.0, to = 1.0) private val mLightModifier: Float = 0.9f

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
			paintBackStroke.color = value
			invalidate()
		}

	var frontFillColor: Int
		@ColorInt get() = mFrontFillColor
		set(@ColorInt value) {
			mFrontFillColor = value
			paintFrontStroke.color = colorLightModifying()
			paintFill.color = value
			invalidate()
		}

	var progress: Float
		@FloatRange(from = 0.0, to = 1.0) get() = mProgress
		set(@FloatRange(from = 0.0, to = 1.0) value) {
			checkProgress(value)
			mProgress = value
			paintFill.alpha = (255 * value).toInt()
			paintFrontStroke.alpha = (255 * value).toInt()
			invalidate()
		}

	init {
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

				mProgress = getFloatOrThrow(R.styleable.FillingProgressBar_progress).also {
					checkProgress(it)
				}
			} finally {
				recycle()
			}
		}

		paintBackStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = mBackStrokeColor
			strokeWidth = mStrokeWidth
			style = Paint.Style.STROKE
		}
		paintFrontStroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = colorLightModifying()
			alpha = (255 * mProgress).toInt()
			strokeWidth = mStrokeWidth
			style = Paint.Style.STROKE
		}
		paintFill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
			color = mFrontFillColor
			alpha = (255 * mProgress).toInt()
		}
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)

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
}