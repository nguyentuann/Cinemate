package vn.tutorial.cinemate.presentation.more.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.presentation.authentication.components.EmailTextField
import vn.tutorial.cinemate.presentation.more.viewModels.CurrentPlanViewModel

enum class MemberType {
    ADULT,
    KID
}@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteMemberBottomSheet(
    onDismiss: () -> Unit,
    onSend: (email: String, type: MemberType) -> Unit,
    viewModel: CurrentPlanViewModel
) {
    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var memberType by remember { mutableStateOf(MemberType.ADULT) }
    var isValidEmail by remember { mutableStateOf<Boolean?>(null) }
    var expanded by remember { mutableStateOf(false) }

    /** 🔁 Debounce + gọi API */
    LaunchedEffect(email) {
        if (email.isEmpty()) {
            expanded = false
            return@LaunchedEffect
        }

        delay(300)
        viewModel.searchEmail(email)
    }

    /** 🔁 Mở dropdown khi có kết quả */
    LaunchedEffect(email, state.emailList) {
        if (email.isNotEmpty()) {
            expanded = state.emailList.isNotEmpty()
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .imePadding(),
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            /** Title */
            Text(
                text = "Invite Member",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            /** Email + Autocomplete */
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { }
            ) {
                EmailTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isValidEmail = Validator.isValidEmail(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    isValidEmail = isValidEmail
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.heightIn(max = 250.dp)
                ) {
                    when {
                        state.isLoading -> {
                            DropdownMenuItem(
                                text = { Text("Searching...") },
                                onClick = {}
                            )
                        }

                        state.emailList.isEmpty() -> {
                            DropdownMenuItem(
                                text = { Text("No results") },
                                onClick = {}
                            )
                        }

                        else -> {
                            state.emailList.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(item) },
                                    onClick = {
                                        email = item
                                        expanded = false
                                        isValidEmail = Validator.isValidEmail(item)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            /** Member type */
            Text("Member type", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = memberType == MemberType.ADULT,
                    onClick = { memberType = MemberType.ADULT }
                )
                Text(
                    "Adult",
                    modifier = Modifier.clickable {
                        memberType = MemberType.ADULT
                    },
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(Modifier.width(24.dp))

                RadioButton(
                    selected = memberType == MemberType.KID,
                    onClick = { memberType = MemberType.KID }
                )
                Text(
                    "Kid",
                    modifier = Modifier.clickable {
                        memberType = MemberType.KID
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(24.dp))

            /** Send button */
            Button(
                onClick = { onSend(email.trim(), memberType) },
                modifier = Modifier.fillMaxWidth(),
                enabled = isValidEmail == true
            ) {
                Text("Send Invitation")
            }
        }
    }
}


