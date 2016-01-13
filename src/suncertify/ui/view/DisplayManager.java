package suncertify.ui.view;

public interface DisplayManager {

  void displayFatalException(final Exception exception);

  void displayWarningException(final Exception exception, final String title);
}
