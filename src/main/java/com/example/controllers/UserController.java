package com.example.controllers;

import com.example.models.QUser;
import com.example.models.User;
import com.example.repositories.UserRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    /** Search user by name with pagination support.
     *
     * There could be one or two query parameters "name" which gives different query behavior:
     *  * First "name" parameter is a comma delimited string, e.g. "name=john,bob,lisa".
     *    The method will perform incase-sensitive equal query for each string value.
     *  * Second "name" parameter is a plain string value which is optional.
     *    If the second name parameter appears, the method will perform LIKE "%{name}%" query for the parameter,
     *    as well as the first name parameter's query.
     *
     * Example of the query parameters:
     *  * /users?name=john%20doe,bob,lisa
     *  * /users?name=john%20doe,bob,lisa&name=claudia
     *
     * @param names The name parameter in list form
     * @return List of User entities
     */
    @RequestMapping(method = RequestMethod.GET, value = "users")
    public Page<User> searchUser(@RequestParam(value = "name", required = true) List<String> names, Pageable pageable) {
        names.replaceAll(String::toLowerCase);
        String[] equalNameParams = names.get(0).split(",");

        QUser qUser = QUser.user;
        BooleanBuilder queryBuilder = new BooleanBuilder();

        // First "name" parameter query
        queryBuilder.or(qUser.name.toLowerCase().in(equalNameParams));

        // Second "name" parameter query
        if (names.size() > 1) {
            queryBuilder.or(qUser.name.likeIgnoreCase("%" + names.get(1)+ "%"));
        }

        return userRepository.findAll(queryBuilder, pageable);
    }

}
