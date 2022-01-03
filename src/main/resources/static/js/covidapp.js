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