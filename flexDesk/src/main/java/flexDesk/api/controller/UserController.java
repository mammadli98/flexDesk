package flexDesk.api.controller;

import flexDesk.api.ModelMapper;
import flexDesk.api.contract.UserDto;
import flexDesk.api.contract.interfaces.IUserController;
import flexDesk.backend.entities.derivatedAttributes.Group;
import flexDesk.backend.services.UserService;
import flexDesk.tools.PasswordUtil;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserController implements IUserController {

  @Autowired
  private UserService userService;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public UserDto create(
    String userName,
    String passwordHash,
    Group group,
    boolean projectLead
  ) {
    return modelMapper.userToUserDto(
      userService.create(userName, passwordHash, group, projectLead)
    );
  }

  @Override
  public List<UserDto> users() {
    return modelMapper.usersToUserDtos(userService.users());
  }

  @Override
  public boolean checkExistingUsername(String userName) {
    return userService.existsByUserName(userName);
  }

  @Override
  public UserDto findByUserName(String userName) {
    return modelMapper.userToUserDto(userService.findByUserName(userName));
  }

  @Override
  public boolean checkLoginCredentials(String userName, String passWord) {
    return userService.existsByUserNameAndPasswordHash(
      userName,
      PasswordUtil.hashPassword(passWord)
    );
  }
}
