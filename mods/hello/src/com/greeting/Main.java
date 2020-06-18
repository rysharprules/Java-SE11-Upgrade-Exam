package com.greeting;

import com.name.Names;
import com.question.Questions;
import java.util.logging.Logger;

public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {

    LOGGER.info("Hello " + Names.getName() + " "
        + Questions.getQuestion());
  }
}
