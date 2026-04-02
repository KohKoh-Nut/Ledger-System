package model;

public class InvalidDateException extends Exception {
  public InvalidDateException(String date) {
    StringBuilder s = new StringBuilder().append(date)
                                         .append(" is invalid. (DD/MM/YYYY)");
    super(s.toString());
  }
}
