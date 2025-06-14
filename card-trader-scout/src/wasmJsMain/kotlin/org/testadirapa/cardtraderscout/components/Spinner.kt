package org.testadirapa.cardtraderscout.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Spinner() {
	Row(
		modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
		horizontalArrangement = Arrangement.Center,
	) {
		CircularProgressIndicator()
	}
}
