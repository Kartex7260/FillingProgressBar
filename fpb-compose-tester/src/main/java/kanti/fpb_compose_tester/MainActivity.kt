package kanti.fpb_compose_tester

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kanti.fpb_compose_tester.ui.theme.FillingProgressBarTheme
import knati.fillingprogressbar.FPBDefaults
import knati.fillingprogressbar.FillingProgressBar

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			MainActivityContent()
		}
	}
}

@Composable
fun MainActivityContent() {
	FillingProgressBarTheme {
		Surface(
			modifier = Modifier.fillMaxSize(),
			color = MaterialTheme.colorScheme.background
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
			) {
				Row(
					modifier = Modifier
						.fillMaxWidth()
				) {
					FillingProgressBar(
						progress = 1f,
						enabled = true
					)

					FillingProgressBar(
						progress = 0.75f,
						enabled = true
					)

					FillingProgressBar(
						progress = 0.5f,
						enabled = true
					)

					FillingProgressBar(
						progress = 0.25f,
						enabled = true
					)

					FillingProgressBar(
						progress = 0f,
						enabled = true
					)
				}

				Row(
					modifier = Modifier
						.fillMaxWidth()
				) {
					FillingProgressBar(
						progress = 1f,
						enabled = false
					)

					FillingProgressBar(
						progress = 0.75f,
						enabled = false
					)

					FillingProgressBar(
						progress = 0.5f,
						enabled = false
					)

					FillingProgressBar(
						progress = 0.25f,
						enabled = false
					)

					FillingProgressBar(
						progress = 0f,
						enabled = false
					)
				}

				Row(
					modifier = Modifier
						.fillMaxWidth()
				) {
					Checkbox(checked = true, onCheckedChange = {})
					Checkbox(
						checked = false,
						onCheckedChange = {},
						enabled = false
					)
				}
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	MainActivityContent()
}