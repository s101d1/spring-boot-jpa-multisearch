package com.example;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.controllers.UserController;
import com.example.repositories.UserRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @Test
    public void testSearchUser() {
        ArgumentCaptor<BooleanBuilder> argumentCaptor = ArgumentCaptor.forClass(BooleanBuilder.class);

        when(userRepository.findAll(argumentCaptor.capture(), any(Pageable.class))).thenReturn(mock(Page.class));

        userController.searchUser(Arrays.asList("john,bob", "doe"), mock(Pageable.class));

        verify(userRepository).findAll(any(BooleanBuilder.class), any(Pageable.class));

        BooleanBuilder booleanBuilder = argumentCaptor.getValue();
        assertEquals("lower(user.name) in [john, bob] || lower(user.name) like %doe", booleanBuilder.getValue().toString());
    }
}
