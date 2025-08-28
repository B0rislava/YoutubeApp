package com.example.youtubeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youtubeapp.R
import com.example.youtubeapp.components.ButtonComponent
import com.example.youtubeapp.components.ClickableTextComponent
import com.example.youtubeapp.components.DividerTextComponent
import com.example.youtubeapp.components.HeadingTextComponent
import com.example.youtubeapp.components.NormalTextComponent
import com.example.youtubeapp.components.PasswordTextFieldComponent
import com.example.youtubeapp.components.TextFieldComponent
import com.example.youtubeapp.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = viewModel(),
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = 60.dp, start = 30.dp, end = 30.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NormalTextComponent(value = stringResource(R.string.hello))
            HeadingTextComponent(value = stringResource(R.string.welcome))

            Spacer(modifier = Modifier.height(20.dp))

            TextFieldComponent(
                labelValue = stringResource(R.string.email),
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                error = viewModel.emailError,
                painterResource = painterResource(R.drawable.email)
            )

            Spacer(modifier = Modifier.height(8.dp))

            PasswordTextFieldComponent(
                labelValue = stringResource(R.string.password),
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                error = viewModel.passwordError,
                painterResource = painterResource(R.drawable.lock)
            )

            Spacer(modifier = Modifier.height(70.dp))

            ButtonComponent(
                value = stringResource(R.string.signin),
                onClick = {
                    if (viewModel.validateCredentials()) {
                        onSignInSuccess()
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            DividerTextComponent()

            Spacer(modifier = Modifier.height(20.dp))

            ClickableTextComponent(
                stringResource(R.string.dont_have_account),
                stringResource(R.string.signup)
            ) { onNavigateToSignUp() }
        }
    }
}
