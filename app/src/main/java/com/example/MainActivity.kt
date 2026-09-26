package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.MissionTopBar
import com.example.ui.screens.AiCompanionScreen
import com.example.ui.screens.BioMonitoringScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.MissionGuardianScreen
import com.example.ui.screens.ModuleTab
import com.example.ui.screens.ModulesHubScreen
import com.example.ui.screens.PersonalizedInterventionScreen
import com.example.ui.screens.TeamDynamicsScreen
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BioGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.SpaceDarkBg
import com.example.ui.theme.SpacePanelBg
import com.example.ui.theme.SpacePanelBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSilver
import com.example.ui.theme.TextWhite
import com.example.viewmodel.AstroMindViewModel

sealed class Screen {
    data object Dashboard : Screen()
    data object BioMonitoring : Screen()
    data object PersonalizedIntervention : Screen()
    data object MissionGuardian : Screen()
    data object TeamDynamics : Screen()
    data object AiCompanion : Screen()
    data class ModulesHub(val tab: ModuleTab) : Screen()
}

class MainActivity : ComponentActivity() {

    private val viewModel: AstroMindViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                AstroMindApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AstroMindApp(viewModel: AstroMindViewModel) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }
    var showEmergencyDialog by remember { mutableStateOf(false) }

    val utcTime by viewModel.utcTime.collectAsState()
    val sol by viewModel.sol.collectAsState()
    val isOfflineAi by viewModel.isOfflineAi.collectAsState()
    val alerts by viewModel.missionAlerts.collectAsState()
    val activeAlertsCount = alerts.count { !it.isResolved }

    // Android back button handling
    BackHandler(enabled = currentScreen !is Screen.Dashboard) {
        currentScreen = Screen.Dashboard
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(SpaceDarkBg),
        topBar = {
            MissionTopBar(
                sol = sol,
                utcTime = utcTime,
                isOfflineAi = isOfflineAi,
                activeAlertsCount = activeAlertsCount,
                onToggleAiMode = { viewModel.toggleAiMode() },
                onAlertsClick = { currentScreen = Screen.MissionGuardian },
                onEmergencyClick = { showEmergencyDialog = true }
            )
        },
        bottomBar = {
            MissionBottomNav(
                currentScreen = currentScreen,
                onSelectScreen = { currentScreen = it }
            )
        },
        containerColor = SpaceDarkBg
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(SpaceDarkBg)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { screen ->
                when (screen) {
                    is Screen.Dashboard -> {
                        DashboardScreen(
                            viewModel = viewModel,
                            onNavigateToBio = { currentScreen = Screen.BioMonitoring },
                            onNavigateToIntervention = { currentScreen = Screen.PersonalizedIntervention },
                            onNavigateToGuardian = { currentScreen = Screen.MissionGuardian },
                            onNavigateToTeam = { currentScreen = Screen.TeamDynamics },
                            onNavigateToCompanion = { currentScreen = Screen.AiCompanion },
                            onNavigateToCircadian = { currentScreen = Screen.ModulesHub(ModuleTab.CIRCADIAN) },
                            onNavigateToDiagnostics = { currentScreen = Screen.ModulesHub(ModuleTab.DIAGNOSTICS) },
                            onNavigateToExercise = { currentScreen = Screen.ModulesHub(ModuleTab.EXERCISE) },
                            onNavigateToEye = { currentScreen = Screen.ModulesHub(ModuleTab.EYE) },
                            onNavigateToVoice = { currentScreen = Screen.ModulesHub(ModuleTab.VOICE) },
                            onNavigateToRadiation = { currentScreen = Screen.ModulesHub(ModuleTab.RADIATION) },
                            onNavigateToEnvironment = { currentScreen = Screen.ModulesHub(ModuleTab.ENVIRONMENT) },
                            onNavigateToCrisis = { currentScreen = Screen.ModulesHub(ModuleTab.CRISIS_ENGINE) }
                        )
                    }

                    is Screen.BioMonitoring -> {
                        BioMonitoringScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = Screen.Dashboard }
                        )
                    }

                    is Screen.PersonalizedIntervention -> {
                        PersonalizedInterventionScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = Screen.Dashboard }
                        )
                    }

                    is Screen.MissionGuardian -> {
                        MissionGuardianScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = Screen.Dashboard }
                        )
                    }

                    is Screen.TeamDynamics -> {
                        TeamDynamicsScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = Screen.Dashboard }
                        )
                    }

                    is Screen.AiCompanion -> {
                        AiCompanionScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = Screen.Dashboard }
                        )
                    }

                    is Screen.ModulesHub -> {
                        ModulesHubScreen(
                            viewModel = viewModel,
                            initialTab = screen.tab,
                            onBack = { currentScreen = Screen.Dashboard }
                        )
                    }
                }
            }

            // Emergency SOS Modal
            if (showEmergencyDialog) {
                EmergencyProtocolDialog(
                    onDismiss = { showEmergencyDialog = false },
                    onConsultAi = {
                        showEmergencyDialog = false
                        viewModel.sendChatMessage("CRITICAL EMERGENCY: Spacecraft anomaly protocol requested. Stand by for triage checklist.")
                        currentScreen = Screen.AiCompanion
                    },
                    onOpenGuardian = {
                        showEmergencyDialog = false
                        currentScreen = Screen.MissionGuardian
                    }
                )
            }
        }
    }
}

@Composable
fun MissionBottomNav(
    currentScreen: Screen,
    onSelectScreen: (Screen) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars),
        color = SpacePanelBg.copy(alpha = 0.98f),
        border = BorderStroke(1.dp, SpacePanelBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                icon = Icons.Default.Dashboard,
                label = "Dashboard",
                isSelected = currentScreen is Screen.Dashboard,
                testTag = "nav_dashboard",
                onClick = { onSelectScreen(Screen.Dashboard) }
            )
            NavItem(
                icon = Icons.Default.Favorite,
                label = "Bio-Vest",
                isSelected = currentScreen is Screen.BioMonitoring,
                testTag = "nav_bio",
                onClick = { onSelectScreen(Screen.BioMonitoring) }
            )
            NavItem(
                icon = Icons.Default.Psychology,
                label = "AI Rx",
                isSelected = currentScreen is Screen.PersonalizedIntervention,
                testTag = "nav_intervention",
                onClick = { onSelectScreen(Screen.PersonalizedIntervention) }
            )
            NavItem(
                icon = Icons.Default.Security,
                label = "Guardian",
                isSelected = currentScreen is Screen.MissionGuardian,
                testTag = "nav_guardian",
                onClick = { onSelectScreen(Screen.MissionGuardian) }
            )
            NavItem(
                icon = Icons.Default.SmartToy,
                label = "AstroMind",
                isSelected = currentScreen is Screen.AiCompanion,
                testTag = "nav_companion",
                onClick = { onSelectScreen(Screen.AiCompanion) }
            )
            NavItem(
                icon = Icons.Default.Groups,
                label = "Team AI",
                isSelected = currentScreen is Screen.TeamDynamics,
                testTag = "nav_team",
                onClick = { onSelectScreen(Screen.TeamDynamics) }
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .testTag(testTag)
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) NeonCyan else TextMuted,
                modifier = Modifier.size(19.dp)
            )
        }
        Text(
            text = label,
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) NeonCyan else TextMuted
        )
    }
}

@Composable
private fun EmergencyProtocolDialog(
    onDismiss: () -> Unit,
    onConsultAi: () -> Unit,
    onOpenGuardian: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SpacePanelBg,
        shape = RoundedCornerShape(14.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = AlertRed)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MISSION CONTROL SOS PROTOCOL",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = AlertRed
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "AstroMind autonomous triage is ready. In an event of solar storm, sudden depressurization, or acute crew medical emergency:",
                    fontSize = 12.sp,
                    color = TextSilver
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text("1. Confirm all 5 crew seals in Storm Core", fontSize = 11.sp, color = TextWhite)
                Text("2. Hold EVA operations & verify dosimeters", fontSize = 11.sp, color = TextWhite)
                Text("3. Initiate autonomous life support oxygen scrub", fontSize = 11.sp, color = TextWhite)
            }
        },
        confirmButton = {
            Button(
                onClick = onConsultAi,
                colors = ButtonDefaults.buttonColors(containerColor = AlertRed, contentColor = TextWhite)
            ) {
                Text("TRIAGE WITH AI", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Button(
                onClick = onOpenGuardian,
                colors = ButtonDefaults.buttonColors(containerColor = SpaceDarkBg, contentColor = NeonCyan),
                border = BorderStroke(1.dp, NeonCyan)
            ) {
                Text("OPEN GUARDIAN", fontFamily = FontFamily.Monospace)
            }
        }
    )
}
