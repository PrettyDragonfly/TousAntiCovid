async function promoteAdmin(userId) {
    await jQuery.get("/api/v1/dashboard/promote/" + userId)
    window.location.reload();
}

async function deleteLocation(locatonId) {
    await jQuery.get("/dashboard/locations/" + locatonId + "/delete")
    window.location.reload();
}

async function deleteActivity(activityId) {
    await jQuery.get("/dashboard/activities" + activityId + "/delete")
    window.location.reload();
}
