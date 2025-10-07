package com.catchapp.api;

import com.catchapp.model.Activity;
import com.catchapp.service.ActivityService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

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
}
