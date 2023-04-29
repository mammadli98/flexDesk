package flexDesk.tools;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.util.StringConverter;

public final class DateFormat {

  public static String formatDate(LocalDate localDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    return localDate.format(formatter);
  }

  public static StringConverter<LocalDate> getConverter() {
    return new StringConverter<LocalDate>() {
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(
        "dd-MM-yyyy"
      );

      @Override
      public String toString(LocalDate date) {
        if (date != null) {
          return dateFormatter.format(date);
        } else {
          return "";
        }
      }

      @Override
      public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
          return LocalDate.parse(string, dateFormatter);
        } else {
          return null;
        }
      }
    };
  }
}
