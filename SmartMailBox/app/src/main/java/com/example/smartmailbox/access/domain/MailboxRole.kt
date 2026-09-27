package com.example.smartmailbox.access.domain

import com.example.smartmailbox.access.domain.MailboxPermission

enum class MailboxRole(val permissions: Set<MailboxPermission>) {
    OWNER(MailboxPermission.entries.toSet()),
    MANAGER(
        setOf(
        MailboxPermission.UNLOCK,
        MailboxPermission.VIEW_ACTIVITY,
        MailboxPermission.MANAGE_ACCESS
    )
    ),
    MEMBER(
        setOf(
        MailboxPermission.UNLOCK,
        MailboxPermission.VIEW_ACTIVITY)
    ),
    VISITOR(setOf(MailboxPermission.UNLOCK))
}
