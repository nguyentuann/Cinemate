package vn.tutorial.cinemate.presentation.more.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import vn.tutorial.cinemate.core.util.Validator
import vn.tutorial.cinemate.presentation.authentication.components.EmailTextField

enum class MemberType {
    ADULT,
    KID
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteMemberBottomSheet(
    onDismiss: () -> Unit,
    onSend: (email: String, type: MemberType) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var memberType by remember { mutableStateOf(MemberType.ADULT) }
    var isValidEmail by remember { mutableStateOf<Boolean?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Invite Member",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email TextField
            EmailTextField(
                value = email,
                onValueChange = {
                    email = it
                    isValidEmail = Validator.isValidEmail(it)
                },
                modifier = Modifier.fillMaxWidth(),
                isValidEmail = isValidEmail
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Radio buttons
            Text(
                text = "Member type",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = memberType == MemberType.ADULT,
                    onClick = { memberType = MemberType.ADULT }
                )
                Text(
                    text = "Adult",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        memberType = MemberType.ADULT
                    }
                )

                Spacer(modifier = Modifier.width(24.dp))

                RadioButton(
                    selected = memberType == MemberType.KID,
                    onClick = { memberType = MemberType.KID }
                )
                Text(
                    text = "Kid",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable {
                        memberType = MemberType.KID
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Send button
            Button(
                onClick = {
                    onSend(email.trim(), memberType)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = isValidEmail == true
            ) {
                Text("Send Invitation")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
