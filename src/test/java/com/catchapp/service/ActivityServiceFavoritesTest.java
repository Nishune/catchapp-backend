package com.catchapp.service;

import com.catchapp.model.Activity;
import com.catchapp.model.Favorite;
import com.catchapp.model.User;
import com.catchapp.repository.ActivityRepository;
import com.catchapp.repository.FavoriteRepository;
import com.catchapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

public class ActivityServiceFavoritesTest {

    private ActivityRepository activityRepo;
    private FavoriteRepository favoriteRepo;
    private UserRepository userRepo;
    private ActivityService service;

    private User user;
    private Activity activity;

    @BeforeEach
    void setUp() {

        activityRepo = mock(ActivityRepository.class);
        favoriteRepo = mock(FavoriteRepository.class);
        userRepo = mock(UserRepository.class);
        service = new ActivityService(activityRepo, favoriteRepo, userRepo);

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .passwordHash("hash")
                .build();

        activity = Activity.builder()
                .id(10L)
                .title("Träning")
                .date(LocalDate.now())
                .build();
    }

    @Test
    void shouldSaveFavoriteWhenValidUserLikesActivity() {
        // Testing so that a favorite is saved when a valid user likes a valid activity
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user)); // user exists
        when(activityRepo.findById(10L)).thenReturn(Optional.of(activity)); // activity exists
        when(favoriteRepo.existsByUserAndActivity(user, activity)).thenReturn(false); // user hasn't liked activity yet

        service.likeActivity("testuser", 10L); // the action being tested

        // verifies that the favorite was saved
        verify(favoriteRepo).save(any(Favorite.class));
    }

    @Test
    void shouldThrowWhenNonExistingUserTriedToLikeActivity() {
        // testing so that an exception is thrown if the user does not exist
        when(userRepo.findByUsername("cantbefound")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.likeActivity("cantbefound", 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");

        verify(favoriteRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenActivityNotFoundOnLike() {
        // testing so that an exception is thrown if the activity does not exist
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(activityRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.likeActivity("testuser", 99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Activity not found");

        verify(favoriteRepo, never()).save(any());
    }

    @Test
    void shouldThrowWhenActivityAlreadyLiked() {
        // testing so that an exception is thrown if the activity has already been liked by the user
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(activityRepo.findById(10L)).thenReturn(Optional.of(activity));
        when(favoriteRepo.existsByUserAndActivity(user, activity)).thenReturn(true);

        assertThatThrownBy(() -> service.likeActivity("testuser", 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Activity already liked");

        verify(favoriteRepo, never()).save(any());
    }

    @Test
    void shouldReturnFavoritesForUser() {
        // Testing so that the correct list of favorite activities is returned for a user
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(favoriteRepo.findActivitiesByUser(user)).thenReturn(List.of(activity));

        List<Activity> favorites = service.listFavorites("testuser");

        // verifies that the returned list is correct
        assertThat(favorites).hasSize(1);
        assertThat(favorites.get(0).getTitle()).isEqualTo("Träning");
        verify(favoriteRepo).findActivitiesByUser(user);
    }

    @Test
    void shouldReturnEmptyListWhenNoFavoritesExist() {
        // empty list is returned if the user has no favorite activities
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(favoriteRepo.findActivitiesByUser(user)).thenReturn(List.of());

        List<Activity> favorites = service.listFavorites("testuser");

        assertThat(favorites).isEmpty();
    }

}
