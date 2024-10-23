package com.example.presentation.EmailAndPasswordChangePage

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.animeapp.ui.screens.EmailAndPasswordChangePage.VerifyEmailAndPasswordChangeScreen
import com.example.presentation.AnimeTopAppBar
import com.example.presentation.LoadingScreen
import com.example.presentation.LoginAndSignUpPage.LoginAndSignUpTextField


@Composable
fun EmailAndPasswordChangeScreen(
    onBackButtonClicked : () -> Unit,
    isPasswordChangePage : Boolean,
    onSignOutClicked : () -> Unit,
    emailAndPasswordChangeViewModel: EmailAndPasswordChangeViewModel,
) {
    val context = LocalContext.current
    val emailAndPasswordUiState =emailAndPasswordChangeViewModel
        .emailAndPasswordChangeUiState.collectAsState()
    val oldEmailFocusRequester = remember { FocusRequester() }
    val newEmailFocusRequester = remember { FocusRequester() }
    val confirmEmailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember {FocusRequester()}
    val oldPasswordFocusRequester = remember { FocusRequester() }
    val newPasswordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    if (emailAndPasswordUiState.value.isSuccess && !emailAndPasswordUiState.value.isLoading) {
        emailAndPasswordChangeViewModel.SignOut()
        onSignOutClicked()
        if(isPasswordChangePage){
            Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(context, "Email changed successfully", Toast.LENGTH_SHORT).show()
        }
    }
    else if(emailAndPasswordUiState.value.isLoading && !isPasswordChangePage){
        VerifyEmailAndPasswordChangeScreen()
    }
    else if(emailAndPasswordUiState.value.isLoading && isPasswordChangePage){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadingScreen()
        }
    }
    else if(!emailAndPasswordUiState.value.isLoading && !emailAndPasswordUiState.value.isSuccess){
        Scaffold (
            topBar = { AnimeTopAppBar(
                title = "",
                isBackButton = true,
                onBackButtonClicked = onBackButtonClicked,
                backGroundColor = MaterialTheme.colorScheme.primary
            )}
        ){ innerPadding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){

                Text(
                    text = if (isPasswordChangePage) "Password Change Page" else "Email Change Page",
                    style = MaterialTheme.typography.headlineLarge
                )
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .width(400.dp)
                        .height(400.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 10.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {

                    Spacer(modifier = Modifier.height(20.dp))
                    LoginAndSignUpTextField(
                        textFieldValue = if (isPasswordChangePage) emailAndPasswordUiState.value.oldPassword
                        else emailAndPasswordUiState.value.oldEmail,
                        onTextFieldValueChange = {
                            if (isPasswordChangePage) emailAndPasswordChangeViewModel.updateOldPasswordTextFieldValue(
                                it
                            )
                            else emailAndPasswordChangeViewModel.updateOldEmailTextFieldValue(it)
                        },
                        placeHolderValue = if (isPasswordChangePage) "Enter Your Current Password" else "Enter Your Current Email",
                        focusRequester = if (isPasswordChangePage) oldPasswordFocusRequester else oldEmailFocusRequester,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        isPassword = isPasswordChangePage,
                        onImeAction = {
                            if (isPasswordChangePage) newPasswordFocusRequester.requestFocus()
                            else newEmailFocusRequester.requestFocus()
                        },
                        errorMessage = if (isPasswordChangePage) emailAndPasswordUiState.value.currentPasswordError else emailAndPasswordUiState.value.currentEmailError
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LoginAndSignUpTextField(
                        textFieldValue = if (isPasswordChangePage) emailAndPasswordUiState.value.newPassword
                        else emailAndPasswordUiState.value.newEmail,
                        onTextFieldValueChange = {
                            if (isPasswordChangePage) emailAndPasswordChangeViewModel.updateNewPasswordTextFieldValue(
                                it
                            )
                            else emailAndPasswordChangeViewModel.updateNewEmailTextFieldValue(it)
                        },
                        placeHolderValue = if (isPasswordChangePage) "Enter New Password" else "Enter New Email",
                        focusRequester = if (isPasswordChangePage) newPasswordFocusRequester else newEmailFocusRequester,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Next
                        ),
                        isPassword = isPasswordChangePage,
                        onImeAction = {
                            if (isPasswordChangePage) confirmPasswordFocusRequester.requestFocus()
                            else confirmEmailFocusRequester.requestFocus()
                        },
                        errorMessage = if (isPasswordChangePage) emailAndPasswordUiState.value.passwordError else emailAndPasswordUiState.value.emailError
                    )
                    LoginAndSignUpTextField(
                        textFieldValue = if (isPasswordChangePage) emailAndPasswordUiState.value.confirmPassword
                        else emailAndPasswordUiState.value.confirmEmail,
                        onTextFieldValueChange = {
                            if (isPasswordChangePage) emailAndPasswordChangeViewModel.updateConfirmPasswordTextFieldValue(
                                it
                            )
                            else emailAndPasswordChangeViewModel.updateConfirmEmailTextFieldValue(it)
                        },
                        placeHolderValue = if (isPasswordChangePage) "Confirm new Password" else "Confirm New Email",
                        focusRequester = if (isPasswordChangePage) confirmPasswordFocusRequester else confirmEmailFocusRequester,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        isPassword = isPasswordChangePage,
                        onImeAction = {if(isPasswordChangePage) emailAndPasswordChangeViewModel.updatePassword(emailAndPasswordUiState.value)
                        else passwordFocusRequester.requestFocus()},
                        errorMessage = if (isPasswordChangePage) emailAndPasswordUiState.value.confirmPasswordError else emailAndPasswordUiState.value.confirmEmailError
                    )
                    if(!isPasswordChangePage) {
                        LoginAndSignUpTextField(
                            textFieldValue =  emailAndPasswordUiState.value.password,

                            onTextFieldValueChange = { emailAndPasswordChangeViewModel.updatePasswordTextFieldValue(it) },
                            placeHolderValue ="Confirm your Password To Change Email",
                            focusRequester =passwordFocusRequester,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Done
                            ),
                            isPassword = true,
                            onImeAction = { emailAndPasswordChangeViewModel.updateEmail(emailAndPasswordChangeUiState = emailAndPasswordUiState.value, emailAndPasswordUiState.value.password)},
                            errorMessage = emailAndPasswordUiState.value.passwordError
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (isPasswordChangePage) {
                            emailAndPasswordChangeViewModel.updatePassword(
                                emailAndPasswordChangeUiState = emailAndPasswordUiState.value,
                            )
                        } else {
                            emailAndPasswordChangeViewModel
                                .updateEmail(
                                    emailAndPasswordChangeUiState = emailAndPasswordUiState.value,
                                    password = emailAndPasswordUiState.value.password
                                )
                        }
                    }
                    ,
                    modifier = Modifier
                        .width(232.dp)
                        .height(61.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(
                        text = if (isPasswordChangePage) "Change Your Password" else "Change Email Address",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

        }
    }
}
