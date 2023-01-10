package com.mrntlu.PassVault.utils

enum class SortType(val title: String, val sort: String?, val isDescending: Boolean) {
    Default("Default", null, false),
    TitleAsc("Title: Alphabetical", "title",false),
    TitleDesc("Title: Reverse Alph.", "title",true),
    UsernameAsc("Username: Alphabetical", "username",false),
    UsernameDesc("Username: Reverse Alph.", "username",true),
}