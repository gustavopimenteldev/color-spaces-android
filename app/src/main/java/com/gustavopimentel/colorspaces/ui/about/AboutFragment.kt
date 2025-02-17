package com.gustavopimentel.colorspaces.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.fragment.app.Fragment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gustavopimentel.colorspaces.R

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {
                AboutScreen()
            }
        }
    }
}

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 135.dp, start = 16.dp, top = 16.dp, end = 16.dp)
            .background(colorResource(id = R.color.dark)),
        horizontalAlignment = Alignment.Start
    ) {
        ExpandableSection(title = stringResource(id = R.string.title_spaces)) {
            Column {
                Text(
                    text = stringResource(id = R.string.spaces_intro),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.spaces_how_to),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• " + stringResource(id = R.string.spaces_step1))
                Text("• " + stringResource(id = R.string.spaces_step2))
                Text("• " + stringResource(id = R.string.spaces_step3))
                Text("• " + stringResource(id = R.string.spaces_step4))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.spaces_tips),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("✔ " + stringResource(id = R.string.spaces_tip1))
                Text("✔ " + stringResource(id = R.string.spaces_tip2))
            }
        }

        ExpandableSection(title = stringResource(id = R.string.title_lights)) {
            Column {
                Text(
                    text = stringResource(id = R.string.lights_intro),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.lights_how_to),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• " + stringResource(id = R.string.lights_step1))
                Text("• " + stringResource(id = R.string.lights_step2))
                Text("• " + stringResource(id = R.string.lights_step3))
                Text("• " + stringResource(id = R.string.lights_step4))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.lights_how_it_works),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("🔹 " + stringResource(id = R.string.lights_detail1))
                Text("🔹 " + stringResource(id = R.string.lights_detail2))
            }
        }

        ExpandableSection(title = stringResource(id = R.string.title_guess)) {
            Column {
                Text(
                    text = stringResource(id = R.string.guess_intro),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.guess_how_to),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• " + stringResource(id = R.string.guess_step1))
                Text("• " + stringResource(id = R.string.guess_step2))
                Text("• " + stringResource(id = R.string.guess_step3))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.guess_feedback),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("✅ " + stringResource(id = R.string.guess_correct))
                Text("🔵 " + stringResource(id = R.string.guess_lower))
                Text("🔴 " + stringResource(id = R.string.guess_higher))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.guess_win_lose),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("🎉 " + stringResource(id = R.string.guess_win))
                Text("❌ " + stringResource(id = R.string.guess_lose))
            }
        }

        ExpandableSection(title = stringResource(id = R.string.title_about)) {
            Column {
                Text(
                    text = stringResource(id = R.string.about_intro),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.about_why),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("✔ " + stringResource(id = R.string.about_reason1))
                Text("✔ " + stringResource(id = R.string.about_reason2))

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.about_features),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("🎨 " + stringResource(id = R.string.about_feature1))
                Text("💡 " + stringResource(id = R.string.about_feature2))
                Text("🎯 " + stringResource(id = R.string.about_feature3))

                Spacer(modifier = Modifier.height(8.dp))
                val context = LocalContext.current
                val versionName = context.packageManager
                    .getPackageInfo(context.packageName, 0).versionName
                Text( stringResource(id = R.string.app_version) + versionName)
            }
        }
    }
}

@Composable
fun ExpandableSection(title: String, content: @Composable () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
            }
        }
    }
}

