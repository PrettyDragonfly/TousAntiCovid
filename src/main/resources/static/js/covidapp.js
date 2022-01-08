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

async function joinEvent(eventId) {
    await jQuery.get("/api/v1/events/" + eventId + "/join")
    window.location.reload();
}

async function leaveEvent(eventId) {
    await jQuery.get("/api/v1/events/" + eventId + "/leave")
    window.location.reload();
}