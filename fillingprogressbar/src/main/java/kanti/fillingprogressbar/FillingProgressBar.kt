package kanti.fillingprogressbar

import android.content.Context
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
			invalidate()
		}

	var frontFillColor: Int
		@ColorInt get() = mFrontFillColor
		set(@ColorInt value) {
			mFrontFillColor = value
			invalidate()
		}

	var progress: Float
		@FloatRange(from = 0.0, to = 1.0) get() = mProgress
		set(@FloatRange(from = 0.0, to = 1.0) value) {
			mProgress = value
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

				mProgress = getFloatOrThrow(R.styleable.FillingProgressBar_progress)
			} finally {
				recycle()
			}
		}
	}
}