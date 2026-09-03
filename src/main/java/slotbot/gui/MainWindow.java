package slotbot.gui;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import slotbot.SlotBot;

/**
 * Presents the conversation and forwards submitted commands to SlotBot.
 */
public class MainWindow {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private SlotBot bot;
    private Runnable closeWindow;

    /**
     * Connects the chatbot and displays its greeting and startup warnings.
     *
     * @param bot Chatbot for this conversation.
     * @param closeWindow Action that closes the window after goodbye.
     */
    public void setBot(SlotBot bot, Runnable closeWindow) {
        this.bot = bot;
        this.closeWindow = closeWindow;

        addMessage(bot.getWelcome(), false);
    }

    /**
     * Places keyboard focus in the command field.
     */
    public void focusInput() {
        userInput.requestFocus();
    }

    /**
     * Submits one nonblank command and displays its response.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank() || bot.isExiting()) {
            userInput.clear();
            focusInput();
            return;
        }

        addMessage(input, true);
        addMessage(bot.getResponse(input), false);
        userInput.clear();

        if (bot.isExiting()) {
            userInput.setDisable(true);
            sendButton.setDisable(true);

            PauseTransition farewell = new PauseTransition(Duration.seconds(1));
            farewell.setOnFinished(event -> closeWindow.run());
            farewell.play();
        } else {
            focusInput();
        }
    }

    /**
     * Adds a wrapping message bubble and scrolls to the latest response.
     *
     * @param text Message to display.
     * @param isUser Whether the user sent this message.
     */
    private void addMessage(String text, boolean isUser) {
        String displayedText = isUser ? text : text.replaceAll("(?m)^_{60}\\R?", "").strip();
        Label author = new Label(isUser ? "YOU" : "SLOTBOT");
        author.getStyleClass().add("author");

        Label message = new Label(displayedText);
        message.setWrapText(true);
        message.setMinWidth(0);
        message.setMaxWidth(Double.MAX_VALUE);
        message.getStyleClass().add(isUser ? "user-message" : "bot-message");

        VBox bubble = new VBox(5, author, message);
        bubble.setFillWidth(true);
        bubble.setMaxWidth(Double.MAX_VALUE);
        bubble.setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
        dialogContainer.getChildren().add(bubble);

        Platform.runLater(() -> {
            // Finish the viewport and content layout before calculating the bottom position.
            Parent root = scrollPane.getScene().getRoot();
            root.applyCss();
            root.layout();
            scrollPane.setVvalue(scrollPane.getVmax());
        });
    }
}
