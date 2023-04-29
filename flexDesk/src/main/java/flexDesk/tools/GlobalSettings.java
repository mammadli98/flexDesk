package flexDesk.tools;

public class GlobalSettings {

  private static GlobalSettings globalSettings;
  private boolean parentalModeSetting = true;

  private int months;
  private int weeks;
  private int days = 7;

  private int minimalPasswordLength = 5;

  private int bookingTimeFrameLenght = 7;

  //timeframes for half days

  private GlobalSettings() {}

  public static synchronized GlobalSettings getInstance() {
    if (globalSettings == null) {
      globalSettings = new GlobalSettings();
    }
    return globalSettings;
  }

  public void setParentalModeSetting(boolean parentalModeSetting) {
    this.parentalModeSetting = parentalModeSetting;
  }

  public boolean isParentalModeSetting() {
    return parentalModeSetting;
  }

  public int getMinimalPasswordLength() {
    return minimalPasswordLength;
  }

  public void setMinimalPasswordLength(int minimalPasswordLength) {
    this.minimalPasswordLength = minimalPasswordLength;
  }

  public int getBookingTimeFrameLenght() {
    return bookingTimeFrameLenght;
  }

  public void setBookingTimeFrameLenght(int bookingTimeFrameLenght) {
    this.bookingTimeFrameLenght = bookingTimeFrameLenght;
  }

  public int getMonths() {
    return this.months;
  }

  public int getWeeks() {
    return this.weeks;
  }

  public int getDays() {
    return this.days;
  }

  public void setMonths(int months) {
    this.months = months;
  }

  public void setWeeks(int weeks) {
    this.weeks = weeks;
  }

  public void setDays(int days) {
    this.days = days;
  }
}
