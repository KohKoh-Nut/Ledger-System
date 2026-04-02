import model.Date;
import model.InvalidDateException;

public class Main {
  public static void main(String[] args) {
    try {
      System.out.print(Date.of(29, 2, 2025).toString());
    } catch (InvalidDateException e) {
      throw new RuntimeException(e);
    }
  }
}
