package flexDesk.api.contract.interfaces;

import flexDesk.api.contract.UserDto;
import flexDesk.backend.entities.derivatedAttributes.Group;
import java.util.List;

public interface IUserController {
  UserDto create(
    String userName,
    String password,
    Group group,
    boolean projectLead
  );

  List<UserDto> users();

  boolean checkExistingUsername(String userName);

  UserDto findByUserName(String userName);

  boolean checkLoginCredentials(String userName, String passWord);
}
