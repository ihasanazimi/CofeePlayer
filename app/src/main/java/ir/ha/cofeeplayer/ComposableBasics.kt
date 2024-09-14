package ir.ha.meproject.common.base

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun <T> BaseScreen(
    viewModel: BaseComposedViewModel<T>,
    content: @Composable (data: T) -> Unit
) {
    val isLoading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val data by viewModel.data.collectAsState()

    when {
        isLoading -> {
            CircularProgressIndicator()
        }
        error != null -> {
            Text("Error: $error")
        }
        else -> {
            data?.let {
                content(it)
            } ?: Text("No Data Available")
        }
    }
}


@Composable
fun <T> BaseLazyColumn(
    items: List<T>,
    key: (T) -> Any = { it.hashCode() },
    itemContent: @Composable (T) -> Unit
) {
    LazyColumn {
        items(items, key = key) { item ->
            itemContent(item)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorMessage: String = "",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            isError = isError,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError) {
            Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
        }
    }
}



@Composable
fun BaseButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = textColor, modifier = Modifier.size(16.dp))
        } else {
            Text(text = text, color = textColor)
        }
    }
}





@Composable
fun BaseDialog(
    title: String,
    description: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dismissText: String = "Cancel",
    confirmText: String = "OK"
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = title) },
        text = { Text(text = description) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}


