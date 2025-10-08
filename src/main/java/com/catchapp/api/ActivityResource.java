package com.catchapp.api;

import com.catchapp.model.Activity;
import com.catchapp.security.JwtSecured;
import com.catchapp.service.ActivityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.List;
import java.util.Map;

@Path("/activities")
@Produces(MediaType.APPLICATION_JSON)
public class ActivityResource {

    @Inject
    ActivityService activityService;

    @GET
    public Response listActivities() {
        List<Activity> activities = activityService.listActivities();
        return Response.ok(activities).build();
    }

    @GET
    @Path("/{id}")
    public Response getActivityById(@PathParam("id") Long id) {
        Activity activity = activityService.getActivityById(id);
        return Response.ok(activity).build();
    }

    @POST
    @Path("/{id}/like")
    @JwtSecured
    public Response likeActivity(@PathParam("id") Long id, @Context SecurityContext context) {
        String username = context.getUserPrincipal().getName();
        activityService.likeActivity(username, id);
        return Response.ok(Map.of("message", "Activity liked")).build();
    }
}
