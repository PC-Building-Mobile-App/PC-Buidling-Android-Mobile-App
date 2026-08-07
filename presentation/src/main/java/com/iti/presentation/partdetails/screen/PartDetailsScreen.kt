package com.iti.presentation.partdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.iti.presentation.core.pccomponents.model.ComponentUiModel
import com.iti.presentation.core.uicomponents.shimmerEffect
import com.iti.presentation.ui.theme.SuccessGreen
import kotlinx.serialization.json.Json

private val HERO_IMAGE_HEIGHT = 340.dp

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PartDetailsScreen(

    componentJson: String,
    viewModel: PartDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onAddToBuildClick: (ComponentUiModel) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(componentJson) {
        val component = Json.decodeFromString(ComponentUiModel.serializer(), componentJson)
        viewModel.setComponent(component)
    }

    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PartDetailsContract.Effect.NavigateBack -> onBackClick()
                is PartDetailsContract.Effect.NavigateToBuildGeneration -> onAddToBuildClick(effect.component)
                is PartDetailsContract.Effect.ShowToast -> {
                    android.widget.Toast.makeText(context, effect.message, android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    if (state.isDrawerOpen) {
        com.iti.presentation.partdetails.components.AddToBuildBottomSheet(
            builds = state.builds,
            isLoading = state.isBuildsLoading,
            isCheckingCompatibility = state.isCheckingCompatibility,
            isAddingToBuild = state.isAddingToBuild,
            selectedBuildId = state.selectedBuildId,
            compatibilityError = state.compatibilityError,
            isCreatingNewBuild = state.isCreatingNewBuild,
            newBuildName = state.newBuildName,
            selectedCategory = state.selectedCategory,
            onDismissRequest = { viewModel.onEvent(PartDetailsContract.Event.DismissDrawer) },
            onBuildSelected = { build -> viewModel.onEvent(PartDetailsContract.Event.BuildSelected(build)) },
            onCreateNewBuildClicked = { viewModel.onEvent(PartDetailsContract.Event.CreateNewBuildClicked) },
            onNewBuildNameChanged = { name -> viewModel.onEvent(PartDetailsContract.Event.NewBuildNameChanged(name)) },
            onCategorySelected = { category -> viewModel.onEvent(PartDetailsContract.Event.CategorySelected(category)) },
            onConfirmNewBuild = { viewModel.onEvent(PartDetailsContract.Event.ConfirmNewBuild) },
            onClearError = { viewModel.onEvent(PartDetailsContract.Event.ClearCompatibilityError) },
        )
    }


    Scaffold(

        // No TopAppBar — the back button floats over the full-bleed hero
        // image instead, matching the mockup. See BackButtonOverlay below.
        bottomBar = {
            state.component?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        // Extra bottom clearance: BottomNavBar's AI FAB floats
                        // ~28dp above the nav bar itself, so without this the
                        // button visually collides with it.
                        .padding(bottom = 28.dp),
                ) {
                    Button(
                        onClick = { viewModel.onEvent(PartDetailsContract.Event.AddToBuildClicked) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary))),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                "+  Add to Build",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        val component = state.component
        if (component == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Component not found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = padding.calculateBottomPadding()),
            ) {
                // Full-bleed hero image with the back button floating on top —
                // no side padding, no rounded corners, extends under the
                // status bar, matching the mockup exactly.
                item {
                    HeroImageGallery(
                        images = component.images.ifEmpty { listOf(component.imageUrl) },
                        productName = component.productName,
                        onBackClick = { viewModel.onEvent(PartDetailsContract.Event.BackClicked) },
                    )
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 24.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .padding(20.dp),
                    ) {
                        Text(
                            text = component.subtitle,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = component.productName,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Market Price",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = component.formattedPrice,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 20.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.14f),
                                    ),
                                ),
                            )
                            .padding(16.dp),
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "AI EXPLANATION",
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))

                        if (state.isAiLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmerEffect(),
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .height(18.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .shimmerEffect(),
                            )
                        } else {
                            Text(
                                text = state.aiExplanation ?: "No summary available.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                if (state.specs.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 20.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                                .padding(16.dp),
                        ) {
                            Text(
                                text = "Full Specifications",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            state.specs.toList().forEachIndexed { index, (key, value) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = key.replace("_", " ").replaceFirstChar { it.uppercase() },
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f),
                                    )
                                    Text(
                                        text = value,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.End,
                                    )
                                }
                                if (index != state.specs.size - 1) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun HeroImageGallery(
    images: List<String>,
    productName: String,
    onBackClick: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(HERO_IMAGE_HEIGHT)
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = productName,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                images.forEachIndexed { index, _ ->
                    val isSelected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                    )
                }
            }
        }

        // Subtle scrim so the floating back button stays legible over bright photos.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Black.copy(alpha = 0.45f), Color.Transparent),
                    ),
                ),
        )

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 4.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.45f)),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
            )
        }
    }
}
