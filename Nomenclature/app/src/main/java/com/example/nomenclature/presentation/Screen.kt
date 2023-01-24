package com.example.nomenclature.presentation

import com.example.nomenclature.R
import com.example.nomenclature.core.util.UiText

enum class Screen(val route: String, val screenName: UiText, val subRoutes: List<String>? = null) {
    MainScreen("MainScreen", UiText.StringResource(R.string.MainScreenName)),
    ScannerScreen("ScannerScreen", UiText.StringResource(R.string.ScannerScreenName)),
    QRScannerScreen("QRScannerScreen", UiText.StringResource(R.string.ScannerScreenName)),
    SearchScreen("SearchScreen", UiText.StringResource(R.string.ScannerScreenName)),
    ProductSearchScreen("ProductSearchScreen", UiText.StringResource(R.string.ScannerScreenName)),
    StorageItemScreen("StorageItemScreen", UiText.StringResource(R.string.StorageItemScreen)),
    FromAddingScannerStorageItemScreen("SearchStorageItemScreen", UiText.StringResource(R.string.StorageItemScreen)),
    ;

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}