package com.example.noteapp.feature_note.presentstion.add_edit_note

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.noteapp.R
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.presentstion.add_edit_note.component.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val imageState = viewModel.noteImage.value
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Background color animation
    val noteBackgroundAnimatable = remember {
        Animatable(Color(if (noteColor != -1) noteColor else viewModel.noteColor.value))
    }

    // Launchers
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { viewModel.onEvent(AddEditNoteEvent.ImageSelected(it)) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            viewModel.tempCameraUri.value?.let { uri ->
                viewModel.onEvent(AddEditNoteEvent.CameraImageCaptured(uri))
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = viewModel.getTemporaryFileUri(context)
            viewModel.tempCameraUri.value = uri
            cameraLauncher.launch(uri)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar("Camera permission is required")
            }
        }
    }

    // Collect events
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiEvent.SaveNote -> navController.navigateUp()
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    // Animate screen entrance
    val animatedAlpha = remember { Animatable(0f) }
    val animatedOffset = remember { Animatable(50f) }
    LaunchedEffect(Unit) {
        animatedAlpha.animateTo(1f, tween(600))
        animatedOffset.animateTo(0f, tween(600))
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add / Edit Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painterResource(R.drawable.arrow_back),
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = animatedAlpha.value > 0.8f,
                enter = androidx.compose.animation.scaleIn(tween(400)),
                exit = androidx.compose.animation.scaleOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                    icon = { Icon(Icons.Default.Done, "Save") },
                    text = { Text("Save Note") },
                    containerColor = MaterialTheme.colorScheme.primary
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(padding)
                .padding(16.dp)
                .alpha(animatedAlpha.value)
                .offset(y = animatedOffset.value.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Color Picker
            Text("Pick a color", style = MaterialTheme.typography.labelLarge)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    val selected = viewModel.noteColor.value == colorInt

                    Box(
                        modifier = Modifier
                            .size(if (selected) 54.dp else 46.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                3.dp,
                                if (selected) MaterialTheme.colorScheme.onSurface
                                else Color.Transparent,
                                CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    noteBackgroundAnimatable.animateTo(
                                        Color(colorInt),
                                        tween(600)
                                    )
                                }
                                viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                            }
                    )
                }
            }

            // Image picker + preview
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Icon(
                        painterResource(R.drawable.add_photo),
                        "Pick from gallery",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = {
                        when {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED -> {
                                val uri = viewModel.getTemporaryFileUri(context)
                                viewModel.tempCameraUri.value = uri
                                cameraLauncher.launch(uri)
                            }
                            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Icon(
                        painterResource(R.drawable.camera),
                        "Take photo",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                AnimatedVisibility(
                    visible = true,
                    enter = androidx.compose.animation.fadeIn(tween(500)) +
                            androidx.compose.animation.scaleIn(),
                    exit = androidx.compose.animation.fadeOut() +
                            androidx.compose.animation.scaleOut()
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(imageState)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Note image",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Title field
            TransparentHintTextField(
                text = titleState.text,
                hint = titleState.hint,
                onValueChanged = { viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it)) },
                onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it)) },
                isHintVisible = titleState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
                    .copy(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth()
            )

            // Content field
            TransparentHintTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChanged = { viewModel.onEvent(AddEditNoteEvent.EnteredContent(it)) },
                onFocusChange = { viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it)) },
                isHintVisible = contentState.isHintVisible,
                textStyle = MaterialTheme.typography.bodyLarge
                    .copy(color = MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp)
            )
        }
    }
}

