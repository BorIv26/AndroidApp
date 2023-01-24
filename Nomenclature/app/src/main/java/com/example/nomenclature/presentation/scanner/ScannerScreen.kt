package com.example.nomenclature.presentation.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.RectF
import android.util.Rational
import android.view.Surface
import android.util.Size as utilSize
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nomenclature.R
import com.example.nomenclature.presentation.Screen
import com.example.nomenclature.presentation.components.ErrorTextHandler
import com.example.nomenclature.presentation.scanner.qr_code.BarcodeScannerAnalyzer

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ScannerScreen(
    navController: NavController,
    isSearchMode: Boolean = false,
    viewModel: ScannerViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val state = viewModel.state.collectAsState().value
        var code by remember {
            mutableStateOf("")
        }
        var rectF by remember {
            mutableStateOf(RectF())
        }
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(context)
        }
        var hasCamPermission by remember {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            )
        }
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                hasCamPermission = granted
            }
        )
        LaunchedEffect(key1 = true) {
            launcher.launch(Manifest.permission.CAMERA)
        }
        if (hasCamPermission) {
            AndroidView(
                factory = { factoryContext ->
                    val previewView = PreviewView(factoryContext)

                    val preview = Preview.Builder()
                        .build()
                        .also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setTargetResolution(
                            utilSize(
                                previewView.width,
                                previewView.height
                            )
                        )
                        .build()
                        .also {
                            it.setAnalyzer(
                                ContextCompat.getMainExecutor(factoryContext),
                                BarcodeScannerAnalyzer(
                                    isSearchMode = isSearchMode
                                ) { result, rectFResult ->
                                    code = result
                                    rectF = rectFResult
                                    if (code.isNotEmpty()) {
                                        viewModel.handleScannerResult(
                                            code,
                                            isSearchMode
                                        ) { storageId ->
                                            navController
                                                .navigate(
                                                    Screen.StorageItemScreen.withArgs(
                                                        storageId
                                                    )
                                                )
                                        }
                                    }
                                }
                            )
                        }
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    val viewPort = ViewPort.Builder(Rational(350, 100), Surface.ROTATION_0).build()
                    val useCaseGroup = UseCaseGroup.Builder()
                        //.setViewPort(viewPort)
                        .addUseCase(preview)
                        .addUseCase(imageAnalyzer)
                        .build()
                    try {

                        cameraProviderFuture.get().bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            useCaseGroup
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    previewView
                },
                modifier = Modifier
                    .fillMaxSize(),
            )
        }
        Image(
            painter = painterResource(id = R.drawable.scanner_searching_png),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .padding(20.dp),
        )
        if(state.isCreateExtIdDialogOpened) {
            AlertDialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                modifier = Modifier
                    .wrapContentHeight()
                    ,
                onDismissRequest = viewModel::onCreateExtIdDialogHideClick,
                title = {
                    state.foundProduct?.let {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = viewModel::onUnitCreateWithBarcodeConfirm,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                        ,
                    ) {
                        Text(
                            text = stringResource(R.string.add_storage_unit_button_text),
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick =  {
                            viewModel.onUnitCreateWithBarcodeConfirm { id ->
                                navController.navigate(Screen.FromAddingScannerStorageItemScreen.withArgs(id))
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                        ,
                    ) {
                        Text(
                            text = stringResource(R.string.add_and_open_storage_unit_button_text)
                        )
                    }
                },
                text = {
                    if(state.isLoading) {
                        ListItem(
                            headlineText = {
                                Text(
                                    text = stringResource(R.string.loading_text)
                                )
                            },
                            trailingContent = {
                                CircularProgressIndicator(Modifier.size(30.dp))
                            }
                        )
                    }
                    ErrorTextHandler(
                        error = state.error,
                        modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.Center),
                        onRefreshClick = {
                            viewModel.onCreateExtIdDialogHideClick()
                        }
                    )
                    if(!state.isLoading && state.error == null) {
                        OutlinedTextField(
                            value = state.extIdValue,
                            onValueChange = viewModel::onExtIdValueChange,
                            trailingIcon = {
                                if(state.extIdValue.isNotEmpty()) {
                                    IconButton(
                                        onClick = viewModel::onExtIdValueClear,
                                    ) {
                                        Icon(
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            imageVector = Icons.Default.Clear,
                                        )
                                    }
                                }
                            },
                            label = {
                                Text(
                                    text = stringResource(R.string.ext_id_number_text)
                                )
                            }
                        )
                    }
                }
            )

        }
        if(!isSearchMode) {
            ElevatedButton(
                onClick = {
                    navController.navigate(Screen.ProductSearchScreen.route)
                },
                modifier = Modifier
                    .padding(bottom = 90.dp)
                    .align(Alignment.BottomCenter)
                    .width(200.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
                Text(
                    text = stringResource(R.string.manual_input_button_text),
                    modifier = Modifier
                        .padding(
                            start = 14.dp
                        )
                )
            }
        }
    }
}

@Composable
fun DrawTracking(
    modifier: Modifier,
    rectF : RectF,
) {
    Canvas(
        modifier = modifier.fillMaxSize(),
        onDraw = {
            val temp = 100f
            val color = Color.White
            if(rectF == RectF()) {
                return@Canvas
            }
            val leftBottomPoint = Offset(rectF.left - temp, rectF.bottom)
            val rightTopPoint = Offset(rectF.right + temp, rectF.top)
            val strokeWidth = 18F
            val cap = StrokeCap.Round
            drawLine(
                color = color,
                start = leftBottomPoint,
                end = Offset(leftBottomPoint.x, rightTopPoint.y),
                strokeWidth = strokeWidth,
                cap = cap,
            )
            drawLine(
                color = color,
                start = leftBottomPoint,
                end = Offset(rightTopPoint.x, leftBottomPoint.y),
                strokeWidth,
                cap = cap,
            )
            drawLine(
                color = color,
                start = rightTopPoint,
                end = Offset(leftBottomPoint.x, rightTopPoint.y),
                strokeWidth,
                cap = cap,
            )
            drawLine(
                color = color,
                start = rightTopPoint,
                end = Offset(rightTopPoint.x, leftBottomPoint.y),
                strokeWidth,
                cap = cap,
            )
        }
    )
}