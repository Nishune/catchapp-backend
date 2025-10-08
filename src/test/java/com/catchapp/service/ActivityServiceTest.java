package com.catchapp.service;

import com.catchapp.model.Activity;
import com.catchapp.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    private ActivityRepository repo;
    private ActivityService service;

    @BeforeEach
    void setUp() {
        // Mock repo so that we can test the service layer in isolation
        repo = mock(ActivityRepository.class);
        service = new ActivityService(repo);
    }

    @Test
    void listActivitiesShouldReturnAllActivities() {
        // Testing so all activities are returned correctly

        var a1 = Activity.builder()
                .id(1L)
                .title("Filmkväll")
                .date(LocalDate.now())
                .build();

        var a2 = Activity.builder()
                .id(2L)
                .title("Löpning")
                .date(LocalDate.now().plusDays(1))
                .build();

        when(repo.findAll()).thenReturn(List.of(a1, a2));

        List<Activity> result = service.listActivities();

        // verify that the correct data is returned
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("Filmkväll");
        verify(repo).findAll();
    }

    @Test
    void listActivitiesShouldReturnEmptyListWhenNoActivitiesExist() {
        // testing so an empty list is returned if no activities exist

        when(repo.findAll()).thenReturn(List.of());

        List<Activity> result = service.listActivities();

        // should be empty
        assertThat(result).isEmpty();
        verify(repo).findAll();
    }

    @Test
    void getActivityByIdShouldReturnActivityWhenFound() {
        // testing so an activity is returned when it exists

        var a1 = Activity.builder().id(1L).title("Bio").build();
        when(repo.findById(1L)).thenReturn(Optional.of(a1));

        Activity result = service.getActivityById(1L);

        // activity found and correct
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Bio");
        verify(repo).findById(1L);
    }

    @Test
    void getActivityByIdShouldThrowWhenNotFound() {
        // testing so an exception is thrown when the activity does not exist

        when(repo.findById(99L)).thenReturn(Optional.empty());

        // should throw exception
        assertThatThrownBy(() -> service.getActivityById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Activity not found");
    }

    @Test
    void shouldReturnListOfActivities() {
        var a1 = Activity.builder()
                .id(1L)
                .title("Filmkväll")
                .date(LocalDate.now())
                .build();
        var a2 = Activity.builder()
                .id(2L)
                .title("Spela brädspel")
                .date(LocalDate.now())
                .build();

        when(repo.findAll()).thenReturn(List.of(a1, a2));

        var result = service.listActivities();

        assertThat(result).hasSize(2);
        verify(repo).findAll();
    }

    @Test
    void shouldReturnActivityWhenFound() {
        var a1 = Activity.builder().id(1L).title("Filmkväll").build();

        when(repo.findById(1L)).thenReturn(Optional.of(a1));

        var result = service.getActivityById(1L);

        assertThat(result.getTitle()).isEqualTo("Filmkväll");
        verify(repo).findById(1L);
    }

    @Test
    void shouldThrowWhenActivityNotFound() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getActivityById(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Activity not found");

        verify(repo).findById(99L);
    }
}
