package org.notes.multi.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import notesmulti.composeapp.generated.resources.Res
import notesmulti.composeapp.generated.resources.compose_multiplatform
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.notes.multi.action.HomeAction
import org.notes.multi.module.AppModule
import org.notes.multi.state.HomeState
import org.notes.multi.viewmodel.HomeViewModel

@Composable
fun App() {
    KoinApplication(
        {
            modules(AppModule.getAll())
        }
    ) {
        HomeScreen()
    }
}

@Composable
private fun HomeScreen(viewModel: HomeViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    ScaffoldScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScaffoldScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home") },
                actions = {
                }
            )
        },

    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            state = state,
            onAction = onAction
        )
    }

}

@Composable
private fun ContentScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onAction: (HomeAction) -> Unit
) {
    Column(
        modifier = modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        var textField by rememberSaveable { mutableStateOf("") }
        TextField(
            value = state.test,
            onValueChange = {
                textField = it
                onAction(HomeAction.Test(it))
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                onAction(HomeAction.Test(textField))
            }
        ) {
            Text(text = "Click")
        }
        Text(text = state.test)
        Spacer(Modifier.height(20.dp))
        Text(text = state.injectTest)
    }
}