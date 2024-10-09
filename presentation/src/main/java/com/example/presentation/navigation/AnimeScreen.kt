package com.example.presentation.navigation

enum class AnimeScreen(val route: String) {
    Start("start"),
    LogIn("login"),
    SignUp("signup"),
    HomePage("homePage"),
    AnimeDetailsPage("animeDetails/{animeId}"),
    SavedAnimeScreen("savedAnimeScreen"),
    AllAnimeScreen("allAnimeScreen"),
    AnimeChaptersScreen("animeChaptersScreen/{animeId}"),
    ProfilePage("profilePage"),
    CharacterDetailsPage("characterDetailsPage/{characterId}"),
    UserDetailsPage("userDetailsPage"),
    EmailChangePage("emailChangePage"),
    PasswordChangePage("passwordChangePage"),
    ForgetPasswordPage("forgetPasswordPage"),
    SearchScreen("searchScreen")
}