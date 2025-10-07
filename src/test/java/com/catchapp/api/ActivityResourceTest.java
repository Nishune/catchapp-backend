package com.catchapp.api;

import com.catchapp.model.Activity;
import com.catchapp.service.ActivityService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ActivityResourceTest {

    private ActivityService service;
    private ActivityResource resource;

    @BeforeEach
    void setUp() {
        // mocks the service layer so we only test the REST-endpoint
        service = mock(ActivityService.class);
        resource = new ActivityResource();
        resource.activityService = service; // inject mock manually
    }

    @Test
    void listActivitiesShouldReturnOkAndActivities() {
        // testing that the endpoint returns 200 OK and a list of activities when they exist

        var a1 = Activity.builder().id(1L).title("Filmkväll").date(LocalDate.now()).build();
        var a2 = Activity.builder().id(2L).title("Löpning").date(LocalDate.now().plusDays(1)).build();

        when(service.listActivities()).thenReturn(List.of(a1, a2));

        Response response = resource.listActivities();

        // Status should be 200 OK
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        // body should be a list with 2 activities
        List<Activity> body = (List<Activity>) response.getEntity();
        assertThat(body).hasSize(2);
        assertThat(body.get(0).getTitle()).isEqualTo("Filmkväll");

        // verify that the service method was called
        verify(service).listActivities();
    }

    @Test
    void listActivitiesShouldReturnOkAndEmptyListWhenNoneExist() {
        // testing that the endpoint returns 200 OK and an empty list when no activities exist

        when(service.listActivities()).thenReturn(List.of());

        Response response = resource.listActivities();

        // status should be 200 OK
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        // body should be an empty list
        List<Activity> body = (List<Activity>) response.getEntity();
        assertThat(body).isEmpty();

        verify(service).listActivities();
    }
}
