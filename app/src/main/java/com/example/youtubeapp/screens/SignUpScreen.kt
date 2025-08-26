package com.example.youtubeapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.youtubeapp.R
import com.example.youtubeapp.components.HeadingTextComponent
import com.example.youtubeapp.components.NormalTextComponent
import com.example.youtubeapp.components.PasswordTextField
import com.example.youtubeapp.components.TextField
import com.example.youtubeapp.viewmodel.SignUpViewModel


@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(28.dp),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            NormalTextComponent(value = stringResource(R.string.hello))
            HeadingTextComponent(value = stringResource(R.string.create_account))

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                labelValue = stringResource(R.string.name),
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                painterResource = painterResource(R.drawable.profile)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                labelValue = stringResource(R.string.email),
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                error = viewModel.emailError,
                painterResource = painterResource(R.drawable.email)
            )

            Spacer(modifier = Modifier.height(8.dp))


            PasswordTextField(
                labelValue = stringResource(R.string.password),
                value = viewModel.password,
                onValueChange = { viewModel.password = it },
                error = viewModel.passwordError,
                painterResource = painterResource(R.drawable.lock)
            )


            Spacer(modifier = Modifier.height(20.dp))

            androidx.compose.material3.Button(
                onClick = { viewModel.submit() },
                modifier = Modifier.fillMaxWidth()
            ) {
                androidx.compose.material3.Text("Sign Up")
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewSignUp(){
    SignUpScreen()
}
