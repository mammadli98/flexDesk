package flexDesk.backend.services;

import flexDesk.backend.entities.User;
import flexDesk.backend.entities.derivatedAttributes.Group;
import flexDesk.backend.repositories.UserRepository;
import flexDesk.backend.services.exceptions.GenericServiceException;
import flexDesk.tools.PasswordUtil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public List<User> users() {
    return userRepository
      .findAll()
      .stream()
      .filter(user -> user.isDeletedFlag() == false)
      .collect(Collectors.toList());
  }

  public User create(
    String userName,
    String password,
    Group group,
    boolean projectLead
  ) {
    User user = new User();
    user.setUserName(userName);
    user.setPasswordHash(PasswordUtil.hashPassword(password));
    user.setGroupMember(group);
    user.setProjectLead(projectLead);
    user.setDeletedFlag(false);
    return userRepository.save(user);
  }

  public boolean existsByUserName(String userName) {
    Optional<User> matches =
      this.users()
        .stream()
        .filter(user -> user.getUserName().equals(userName))
        .findAny();
    return !matches.isEmpty();
  }

  public boolean existsByUserNameAndPasswordHash(
    String userName,
    String passwordHash
  ) {
    Optional<User> matches =
      this.users()
        .stream()
        .filter(user -> user.getUserName().equals(userName))
        .filter(user -> user.getPasswordHash().equals(passwordHash))
        .findAny();
    return !matches.isEmpty();
  }

  public User findById(long userId) throws GenericServiceException {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isEmpty()) {
      throw new GenericServiceException(
        "Keine Ergebnisse für User mit ID: \"" + userId + "\" gefunden"
      );
    } else if (userOptional.get().isDeletedFlag()) {
      throw new GenericServiceException(
        "User mit ID: \"" + userId + "\" wurde gelöscht"
      );
    }
    return userOptional.get();
  }

  public User findByUserName(String userName) {
    Optional<User> loggedIn =
      this.users()
        .stream()
        .filter(user -> user.getUserName().equals(userName))
        .findAny();
    return loggedIn.get();
  }
}
