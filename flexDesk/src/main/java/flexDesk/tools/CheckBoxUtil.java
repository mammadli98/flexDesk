package flexDesk.tools;

import flexDesk.api.contract.interfaces.Selectable;
import java.util.List;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public final class CheckBoxUtil {

  public static void addSelectableToVbox(
    Selectable selectable,
    VBox vBox,
    EventHandler<ActionEvent> actionEventEventHandler
  ) {
    CheckBox checkBox = new CheckBox(selectable.getSelectableName());
    checkBox.setId(selectable.getSelectableId().toString());
    checkBox.setOnAction(actionEventEventHandler);
    vBox.getChildren().add(checkBox);
  }

  public static CheckBox nodeToCheckBox(Node node) {
    CheckBox checkBox = (CheckBox) node;
    return checkBox;
  }

  public static boolean getCheckBoxSelection(Node node) {
    return nodeToCheckBox(node).isSelected();
  }

  public static List<Boolean> getSelectionMapping(VBox vBox) {
    List<Boolean> selection = vBox
      .getChildren()
      .stream()
      .map(node -> getCheckBoxSelection(node))
      .collect(Collectors.toList());
    return selection;
  }

  public static List<Object> getSelectedItems(VBox vBox) {
    List<Object> selection = vBox
      .getChildren()
      .stream()
      .filter(node -> getCheckBoxSelection(node) == true)
      .collect(Collectors.toList());
    return selection;
  }
}
