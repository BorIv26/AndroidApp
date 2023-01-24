package com.example.nomenclature.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nomenclature.R
import com.example.nomenclature.presentation.Screen

@Composable
fun MainScreen(
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                ,
        ) {
            Button(
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                    ,
                onClick = {
                    navController.navigate(Screen.ScannerScreen.route)
                }
            ) {
                Icon(
                    modifier = Modifier
                        .size(18.dp)
                            ,
                    painter = painterResource(id = R.drawable.ic_scanner),
                    contentDescription = null,
                )
                Text(
                    text = stringResource(id = R.string.add_shipment_button_text),
                    modifier = Modifier
                        .padding(
                            start = 18.dp
                        )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                modifier = Modifier
                    .width(200.dp)
                    .height(50.dp)
                ,
                onClick = {
                    navController.navigate(Screen.SearchScreen.route)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
                Text(
                    text = stringResource(R.string.shipment_search_button_text),
                    modifier = Modifier
                        .padding(
                            start = 14.dp
                        )
                )
            }
        }
    }
}