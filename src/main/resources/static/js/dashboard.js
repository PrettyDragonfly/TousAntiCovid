async function promoteAdmin(userId) {
    await jQuery.get("/api/v1/dashboard/promote/" + userId)
    window.location.reload();
}
