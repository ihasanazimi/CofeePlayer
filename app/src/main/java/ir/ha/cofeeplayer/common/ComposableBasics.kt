package ir.ha.cofeeplayer.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


val BASIC_TAG = "BASIC_TAG"

@Composable
fun <T> BaseScreen(
    viewModel: BaseViewModel<T>,
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
    onItemClick: (T, Int) -> Unit, // Click listener for items with index
    itemContent: @Composable (T, Int) -> Unit // Item content with index
) {
    LazyColumn {
        itemsIndexed(items, key = { _, item -> key(item) }) { index, item ->
            // Modifier.clickable is applied to each item with index
            Box(modifier = Modifier.clickable { onItemClick(item, index) }) {
                itemContent(item, index)
            }
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

abstract class BaseViewModel<T> : ViewModel() {

    open val TAG = this::class.java.simpleName
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _data = MutableStateFlow<T?>(null)
    val data: StateFlow<T?> = _data

    protected fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    protected fun setError(message: String) {
        _error.value = message
    }

    protected fun setData(newData: T) {
        _data.value = newData
    }
}



