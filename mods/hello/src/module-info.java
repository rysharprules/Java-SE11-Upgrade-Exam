module hello {
  requires people;
  /*
  With the modularization of Java SE 9, Java packages themselves have been sorted into modules. Not
  all Java packages are automatically accessible to a modular application. A large number of packages
  are stored in the java.base module. There's no need to explicitly require this. module because it's
  automatically accessible to all modules. The Logger class is stored in the java.logging module. This
  module does need to be explicitly required.
   */
  requires java.logging;
}