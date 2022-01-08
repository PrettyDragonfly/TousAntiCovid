async function makeFriendRequest(userId) {
    await jQuery.get("/api/v1/request-friend/" + userId)
    window.location.reload();
}

async function acceptFriendRequest(userId) {
    await jQuery.get("/api/v1/friend-request/" + userId + "/accept")
    window.location.reload();
}

async function rejectFriendRequest(userId) {
    await jQuery.get("/api/v1/friend-request/" + userId + "/reject")
    window.location.reload();
}

async function makeCurrentUserPositive() {
    await jQuery.get("/api/v1/users/me/positive")
    window.location.reload();
}

async function markNotificationAsRead(id) {
    await jQuery.get("/api/v1/notifications/" + id + "/mark-as-read")
    window.location.reload();
}

async function markNotificationAsUnread(id) {
    await jQuery.get("/api/v1/notifications/" + id + "/mark-as-unread")
    window.location.reload();
}