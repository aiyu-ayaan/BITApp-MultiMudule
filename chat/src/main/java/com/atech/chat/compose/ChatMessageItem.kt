package com.atech.chat.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BubbleChart
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.atech.chat.ChatMessage
import com.atech.chat.Participant
import com.atech.chat.R
import com.atech.core.utils.openLinks
import dev.jeziellago.compose.markdowntext.MarkdownText


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatList(
    chatMessages: List<ChatMessage>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onDeleteClick: (ChatMessage) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        reverseLayout = false,
        state = listState,
    ) {
        items(
            chatMessages,
            key = { item -> item.id }
        ) { message ->
            ChatMessageItem(
                model = message,
                onDeleteClick = {
                    onDeleteClick.invoke(message)
                }
            )
        }
    }
}

@Composable
fun ChatMessageItem(
    modifier: Modifier = Modifier,
    model: ChatMessage,
    onDeleteClick: () -> Unit = {},
) {
    var isContextMenuVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val density = LocalDensity.current
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Surface(
        modifier = modifier
            .indication(interactionSource, LocalIndication.current),
    ) {
        val hasError = model.participant == Participant.ERROR
        Column(
            Modifier
                .padding(8.dp)
                .pointerInput(true) {
                    detectTapGestures(onLongPress = {
                        isContextMenuVisible = true
                    }, onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    })
                },
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    modifier = Modifier.size(MaterialTheme.typography.titleSmall.fontSize.value.dp),
                    imageVector = when (model.participant) {
                        Participant.MODEL -> Icons.Outlined.BubbleChart
                        Participant.USER -> Icons.Outlined.Person
                        Participant.ERROR -> Icons.Outlined.Error
                    },
                    contentDescription = null,
                    tint = if (hasError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        LocalContentColor.current
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (model.participant) {
                        Participant.MODEL -> "Bot"
                        Participant.USER -> "Me"
                        Participant.ERROR -> "Error"
                    },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (hasError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        LocalContentColor.current
                    }
                )
            }
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                MarkdownText(
                    modifier = Modifier
                        .padding(
                            top = 8.dp,
                            start = MaterialTheme.typography.titleSmall.fontSize.value.dp + 8.dp
                        ),
                    markdown = model.text,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    onLinkClicked = {
                        it.openLinks(
                            context
                        )
                    },
                    linkColor = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start,
                    imageLoader = ImageLoader(context),
                    isTextSelectable = true
                )
            }
        }
        DropdownMenu(
            expanded = isContextMenuVisible,
            onDismissRequest = { isContextMenuVisible = false },
        ) {
            DropdownMenuItem(text = { Text(text = stringResource(R.string.copy)) }, leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = stringResource(R.string.copy)
                )
            }, onClick = {
                clipboardManager.setText(AnnotatedString(model.text))
                isContextMenuVisible = false
            }
            )
            DropdownMenuItem(text = { Text(text = stringResource(R.string.delete)) },
                onClick = {
                    onDeleteClick.invoke()
                    isContextMenuVisible = false
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )
                })
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChatMessagePreview() {
    MaterialTheme {
        ChatList(
            chatMessages = listOf(
                ChatMessage(
                    text = "Hello world", participant = Participant.MODEL
                ), ChatMessage(
                    text = "Hello world", participant = Participant.USER
                )
            ), listState = rememberLazyListState()
        )
    }
}