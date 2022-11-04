package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.BaseMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final BaseMapper baseMapper;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, BaseMapper baseMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.baseMapper = baseMapper;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        List<User> userList = userRepository.findAll(Sort.by("firstName"));

        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());

    }

    @Override
    public UserDTO findByUserName(String username) {

//        return baseMapper.convert(userRepository.findByUserName(username), UserDTO.class);

        return userMapper.convertToDto(userRepository.findByUserName(username));

    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(userMapper.convertToEntity(user));
    }

    @Override
    public void deleteByUserName(String username) {
//        userRepository.delete(userRepository.findByUserName(username));
        userRepository.deleteByUserName(username);
    }

    @Override
    public UserDTO update(UserDTO user) {

        // find current user
        User user1 = userRepository.findByUserName(user.getUserName()); // not updated one

        // map update user dto to entity object
        User convertedUser = userMapper.convertToEntity(user);

        // set id from the  old user to the updated one
        convertedUser.setId(user1.getId());

        // save the updated user to db
         userRepository.save(convertedUser);

        return findByUserName(user.getUserName());
    }
}
