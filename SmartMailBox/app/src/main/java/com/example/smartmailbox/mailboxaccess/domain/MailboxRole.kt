package com.example.smartmailbox.mailboxaccess.domain

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
